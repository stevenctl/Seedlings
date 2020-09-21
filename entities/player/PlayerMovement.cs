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

	private int _diveDir;

	private bool _jumping;
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
		_sprite.Connect("animation_finished", this, "EndDive");
	}

	public override void _PhysicsProcess(float delta) {
		// do this at the beginning of the frame
		// built-in "on floor" check relies on this
		_velocity = MoveAndSlide(_velocity, Vector2.Up);

		ReadInput();
		MoveH();
		MoveV(delta);
		ResolveAnimation();
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
	private void MoveH() {
		// if we're not diving, set the sprite-flip based on input (not movement)
		if (_diveDir == 0) {
			FacingRight = _input.x != 0 && _input.x > 0 || FacingRight;
			FacingRight = (_input.x == 0 || !(_input.x < 0)) && FacingRight;
		}

		if (Input.IsActionJustPressed("dive")) {
			_diveDir = FacingRight ? 1 : -1;
			_velocity.y = 0;
			_jumpTime = 0;
		}

		if (_diveDir == 0) {
			_velocity.x = _input.x * WalkSpeed;
		} else {
			_velocity.x = _diveDir * WalkSpeed * 2;
			// cancel dive by moving in opposite direction
			if (_input.x + _diveDir == 0) {
				_sprite.Frame = Math.Max(_sprite.Frame, _sprite.Frames.GetFrameCount("dive") - 4);
			}
		}
	}

	public void EndDive() {
		_diveDir = 0;
	}

	// Vertical Movement
	private void MoveV(float delta) {
		// quick jump cancel
		if (_jumping && !Input.IsActionPressed("jump")) {
			_jumping = false;
			_jumpTime = 0;
			_jumpDebounce = 0;
			// some hang time
			_velocity.y = Math.Max(2 * GravAccel * delta, _velocity.x);
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
			_jumpDebounce = _maxJumpDebounce;
		} else {
			_jumpDebounce -= delta;
		}

		if (_coyoteTime > 0 && _jumpDebounce > 0 && _diveDir == 0)
			_jumping = true;

		if (_jumping && _jumpTime > 0) {
			_velocity.y = -JumpSpeed;
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
