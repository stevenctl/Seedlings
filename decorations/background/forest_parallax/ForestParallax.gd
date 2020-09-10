extends ParallaxBackground

func _ready() -> void:
	var i = 0
	for c in get_children():
		var pl = c as ParallaxLayer
		var denom = (get_child_count() - i)
		pl.motion_scale = Vector2.RIGHT / denom
		for cc in c.get_children():
			var s: Sprite = cc as Sprite
			s.region_enabled = true
			s.region_rect.size = Vector2(1920 * 20, 1080)
		i += 1
	
