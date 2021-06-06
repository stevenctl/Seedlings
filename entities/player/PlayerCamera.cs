using System;
using Godot;

[Tool]
public class PlayerCamera : Camera2D {
	[Export] public float DefaultZoom { get; set; } = 1.2f;
	[Export] public float WideZoom { get; set; } = 1.5f;
	[Export] public bool Wide { get; set; }

	private Vector2 _target;

	public override void _Ready() {
		_Process(0);
		_target = Zoom;
		SetProcess(true);
	}

	public override void _Process(float delta) {
		if(Engine.EditorHint) return;
		if (Input.IsActionJustPressed("zoom")) {
			Wide = !Wide;
		}

		if (Engine.EditorHint || Input.IsActionJustPressed("zoom")) {
			var z = Wide ? WideZoom : DefaultZoom;
			_target = new Vector2(z, z);
		}

		var diff = Zoom.x - _target.x;
		if (Math.Abs(diff) > 0.01) {
			var inout = diff > 0 ? -1 : 1;
			Zoom += Vector2.One * .05f * inout;
		}
	}
}