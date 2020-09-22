using Godot;

public class grass1 : AnimatedSprite {

    [Export] private bool _flipOnMove = true;
    private Area2D _area2D;

    // Called when the node enters the scene tree for the first time.
    public override void _Ready() {
        Connect("animation_finished", this, "ResetFrame");
        
        _area2D = GetChild<Area2D>(0);
        _area2D.Connect("body_entered", this, "TriggerAnimation");
        _area2D.Connect("body_exited", this, "TriggerAnimation");

        ResetFrame();
    }

    private void ResetFrame() {
        Frame = 0;
        Stop();
    }

    public void TriggerAnimation(Node body) {
        if (IsPlaying()) return;

        if (_flipOnMove) {
            // TODO this effect is pretty ugly
            var vel = (body as PlayerMovement)?.Velocity.x;
            if (vel != null && vel != 0) {
                FlipH = vel < 0;
            }    
        }
        
        Frame = 0;
        Play(Animation);
    }
}