extends ParallaxBackground
tool

func _ready() -> void:
	var i = 0
	for c in get_children():
		var pl = c as ParallaxLayer
		pl.motion_scale = Vector2.RIGHT / (get_child_count() - i)
		for cc in c.get_children():
			var s: Sprite = cc as Sprite
			s.region_enabled = true
			s.region_rect.size = Vector2(1920 * 20, 1080)
		i += 1
	
