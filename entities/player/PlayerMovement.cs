using Godot;
using System;

[Tool]
public class PlayerMovement : KinematicBody2D {
	// Tunable movement parameters
	[Export] public int WalkSpeed { get; set; } = 500;
	[Export] public int JumpSpeed { get; set; } = 750;
	[Export] public int GravAccel { get; set; } = 1800;

	// Node refs
	private Sprite _sprite;
	private AnimationPlayer _animation;

	// State
	[Export] public bool FacingRight { get; set; } = true;
	public Vector2 Velocity;
	private Vector2 _input;
	private Vector2 _startPosition;

	private int _diveDir;
	private float _diveDuration;
	private float _minDiveDuration = .5f;

	private bool _jumping;
	private bool _diveJump;
	[Export] public float MaxJumpTime = .25f;
	private float _jumpTime;
	[Export] private float _maxCoyoteTime = .1f;
	private float _coyoteTime;
	[Export] private float _maxJumpDebounce = .2f;
	private float _jumpDebounce;

	public override void _Ready() {
		if (Engine.EditorHint) {
			SetProcess(false);
			SetPhysicsProcess(false);
		}

		_sprite = GetNode<Sprite>("Sprite");
		_animation = GetNode<AnimationPlayer>("AnimationPlayer");
		if (_sprite == null) {
			throw new Exception("PlayerMovement should have a child AnimatedSprite");
		}

		_animation.Connect("animation_finished", this, "EndDive");
		_startPosition = Position;
	}

	public override void _PhysicsProcess(float delta) {
		// do this at the beginning of the frame
		// built-in "on floor" check relies on this
		Velocity = MoveAndSlide(Velocity, Vector2.Up);

		ReadInput();
		MoveH(delta);
		MoveV(delta);
		ResolveAnimation();
		//
		// if (Position.y > 100) {
		// 	Position = _startPosition;
		// }
	}

	// Calculates the input vector each frame

	private void ReadInput() {
		_input = Vector2.Zero;
		if (Input.IsActionPressed("left"))
			_input.x--;
		if (Input.IsActionPressed("right"))
			_input.x++;
		if (Input.IsActionPressed("up"))
			_input.y--;
		if (Input.IsActionPressed("down"))
			_input.y++;
	}

	// Horizontal movement
	private void MoveH(float delta) {
		// if we're not diving, set the sprite-flip based on input (not movement)
		if (_diveDir == 0) {
			FacingRight = _input.x != 0 && _input.x > 0 || FacingRight;
			FacingRight = (_input.x == 0 || !(_input.x < 0)) && FacingRight;
		}


		if (_diveDir != 0) { // dive move
			Velocity.x = _diveDir * WalkSpeed * 2;
			_diveDuration += delta;
		} else { // walk move
			Velocity.x = _input.x * WalkSpeed;
		}

		// dive start
		if (Input.IsActionJustPressed("dive")) {
			_diveDuration = 0;
			_jumping = false;
			_diveDir = FacingRight ? 1 : -1;
			// nerf jump and roll by reducing vertical speed
			Velocity.y = Math.Max(Velocity.y, -.5f * JumpSpeed);
			if (_input.y > 0) {
				// if pressing down, cut upwards momentum
				Velocity.y = Math.Max(Velocity.y, 0);
			} else if (Velocity.y > 0) {
				// otherwise slow the fall
				Velocity.y *= .5f;
			}

			_jumpTime = 0;
		}

		// dive cancel
		if (_diveDir != 0) {
			if(IsOnWall()) {
				EndDive("");
			} else if (Input.IsActionJustReleased("dive")) {
				var diveAnim = _animation.GetAnimation("dive");
				diveAnim.Step = Math.Max(diveAnim.Step, diveAnim.Length / 2);
			}
		}
	}

	public void EndDive(string name) {
		_diveDir = 0;
		GD.Print(_diveDuration);
	}

	// Vertical Movement
	private void MoveV(float delta) {
		// quick jump cancel
		if (IsOnCeiling() || (_jumping && !Input.IsActionPressed("jump"))) {
			_jumping = false;
			_jumpTime = 0;
			_jumpDebounce = 0;
			// some hang time
			Velocity.y = Math.Max(2 * GravAccel * delta, Velocity.y);
		}

		// can we jump
		if (IsOnFloor()) {
			_jumping = false;
			_jumpTime = MaxJumpTime;
			_coyoteTime = _maxCoyoteTime;
		} else {
			_coyoteTime -= delta;
		}

		// jump debounce allows pressing the jump button on an early frame
		if (Input.IsActionJustPressed("jump")) {
			// only get extra jump speed when pushing in the direction of the dive
			_diveJump = _diveDir != 0 && Math.Abs(_input.x - _diveDir) < .01f;
			// more tolerant with early jump while diving
			_jumpDebounce = _maxJumpDebounce * (_diveDir != 0 ? 2 : 1);
		} else {
			_jumpDebounce -= delta;
		}

		if (_coyoteTime > 0 && _jumpDebounce > 0 && _diveDir == 0)
			_jumping = true;

		if (_jumping && _jumpTime > 0) {
			Velocity.y = -JumpSpeed;
			// only do the dive if most recent lasted at least some time
			if (_diveJump && _diveDuration > _minDiveDuration) {
				Velocity.y *= 1.25f;
			}

			_jumpTime -= delta;
		} else {
			Velocity.y += GravAccel * delta;
		}

		Velocity.y = Math.Min(Velocity.y, 2.25f * JumpSpeed);
	}

	private void ResolveAnimation() {
		// TODO maybe animation tree will be worth it.. eventually
		_sprite.FlipH = !FacingRight;

		string play;
		if (_diveDir != 0) {
			play = "dive";
		} else if (!IsOnFloor()) {
			play = Velocity.y > 0 ? "fall" : "jump";
		} else if (_input.x != 0) {
			play = "run";
		} else {
			play = "idle";
		}

		if (_animation.AssignedAnimation != play)
			_animation.Play(play);
	}
}
