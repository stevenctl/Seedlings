using Godot;
using System;

[Tool]
public class PlayerMovement : KinematicBody2D {
	// Tunable movement parameters
	[Export] public int WalkSpeed { get; set; } = 500;
	[Export] public int JumpSpeed { get; set; } = 750;
	[Export] public int GravAccel { get; set; } = 1800;

	// Node refs
	private AnimatedSprite _sprite;

	// State
	[Export] public bool FacingRight { get; set; } = true;
	private Vector2 _velocity;
	private Vector2 _input;
	private Vector2 _startPosition;

	private int _diveDir;

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
		_sprite = GetNode("AnimatedSprite") as AnimatedSprite;
		if (_sprite == null) {
			throw new Exception("PlayerMovement should have a child AnimatedSprite");
		}
		_sprite.Connect("animation_finished", this, "EndDive");
		_startPosition = Position;
	}

	public override void _PhysicsProcess(float delta) {
		// do this at the beginning of the frame
		// built-in "on floor" check relies on this
		_velocity = MoveAndSlide(_velocity, Vector2.Up);

		ReadInput();
		MoveH(delta);
		MoveV(delta);
		ResolveAnimation();

		if (Position.y > 100) {
			Position = _startPosition;
		}
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
			_velocity.x = _diveDir * WalkSpeed * 2;
		} else { // walk move
			_velocity.x = _input.x * WalkSpeed;
		}

		// dive start
		if (Input.IsActionJustPressed("dive")) {
			_jumping = false;
			_diveDir = FacingRight ? 1 : -1;
			_velocity.y = -2 * GravAccel * delta;
			_jumpTime = 0;
		}
		// dive cancel
		if (_diveDir != 0 && _input.x + _diveDir == 0) {
			_sprite.Frame = Math.Max(_sprite.Frame, _sprite.Frames.GetFrameCount("dive") - 6);
			_velocity.y = 0;
		}
	}

	public void EndDive() {
		_diveDir = 0;
	}

	// Vertical Movement
	private void MoveV(float delta) {
		// quick jump cancel
		if (IsOnCeiling() || (_jumping && !Input.IsActionPressed("jump"))) {
			_jumping = false;
			_jumpTime = 0;
			_jumpDebounce = 0;
			// some hang time
			_velocity.y = Math.Max(2 * GravAccel * delta, _velocity.y);
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
			_diveJump = _diveDir != 0;
			_jumpDebounce = _maxJumpDebounce;
		} else {
			_jumpDebounce -= delta;
		}

		if (_coyoteTime > 0 && _jumpDebounce > 0 && _diveDir == 0)
			_jumping = true;

		if (_jumping && _jumpTime > 0) {
			_velocity.y = -JumpSpeed;
			if (_diveJump) {
				_velocity.y *= 1.25f;
			}
			_jumpTime -= delta;
		} else {
			_velocity.y += GravAccel * delta;
		}

		_velocity.y = Math.Min(_velocity.y, 2.25f * JumpSpeed);
	}

	private void ResolveAnimation() {
		_sprite.FlipH = !FacingRight;

		string play;
		if (_diveDir != 0) {
			play = "dive";
		} else if (!IsOnFloor()) {
			if (_velocity.y > 0) {
				play = "fall";
			} else {
				play = "jump";
			}
		} else if (_input.x != 0) {
			play = "run";
		} else {
			play = "idle";
		}

		_sprite.Play(play);
	}
}
