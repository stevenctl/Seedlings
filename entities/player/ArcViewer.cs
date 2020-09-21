using Godot;
using System;

[Tool]
public class ArcViewer : Node2D {
    [Export] private float _timeStep = 0.1f;
    [Export] private bool _inGame = false;

    private const int MaxIterations = 1000;
    private const float _updateInterval = 1.0f;
    private float _updateTimer;

    public override void _Ready() {
        if (!Engine.EditorHint || _inGame) {
            QueueFree();
            return;
        }
        SetProcess(true);
    }

    public override void _Process(float delta) {
        _updateTimer -= delta;
        if (_updateTimer < 0) {
            _updateTimer = _updateInterval;
            Update();
        }
    }

    public override void _Draw() {
        var player = GetParent() as PlayerMovement;
        var collider = player?.GetNode("CollisionShape2D") as CollisionShape2D;
        if (collider == null) {
            var cc = player.GetNode("CollisionShape2D");
            return;
        }

        var pos = collider.Position;
        pos.y += ((RectangleShape2D) collider.Shape).Extents.y;
        var initPosY = pos.y;

        var vel = new Vector2(player.WalkSpeed, 0);
        if (!player.FacingRight) {
            vel *= -1;
        }

        var jt = player.MaxJumpTime;
        var iter = 0;
        while (iter < MaxIterations && pos.y < initPosY + 200) {
            iter++;
            var lastPos = pos;
            if (jt > 0) {
                jt -= _timeStep;
                vel.y = -player.JumpSpeed;
            } else {
                vel.y += player.GravAccel * _timeStep;
            }

            vel.y = Math.Min(vel.y, 2.25f * player.JumpSpeed);
            pos += vel * _timeStep;
            DrawLine(lastPos, pos, Colors.Red);
        }
    }
}