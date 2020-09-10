tool

extends Node2D

export(float) var time_step = 0.1
export(bool) var in_game = false

# prevent infinite loops
const max_iter = 100

func _ready():
	if not (Engine.editor_hint or in_game):
		queue_free()
		return
	set_process(true)

const update_interval: float  = 1.0
var _update_timer: float = update_interval

func _process(delta: float):
	# refresh on timer since we cant respond to parent var changes in a easy way
	_update_timer -= delta
	if _update_timer < 0:
		_update_timer = update_interval
		update()
		

func _draw():
	var player: PlayerMovement = get_parent()
	if player == null:
		return
	
	_draw_jump_arc(player)
	_draw_walk_dist(player)
	
func _draw_jump_arc(player: PlayerMovement):
	var collider =  player.get_node("CollisionShape2D")
	if collider == null:
		return
	var pos = collider.position
	pos.y += (collider.shape as RectangleShape2D).extents.y
	var base = pos.y

	var vel = Vector2(player.walk_speed, 0)
	if !player.facing_right:
		vel *= -1
	var jt = player.max_jump_time
	var iter = 0
	while pos.y <= base + 200:
		iter += 1
		if iter > max_iter:
			break
		var last_pos = pos	
		if jt > 0:
			jt -= time_step
			vel.y = -player.jump_speed
		else:
			vel.y += player.grav_accel * time_step
			
		vel.y = min(2.25*player.jump_speed, vel.y)
		pos += vel * time_step
		draw_line(last_pos, pos, Color.red)

func _draw_walk_dist(player: PlayerMovement):
	var collider =  player.get_node("CollisionShape2D")
	if collider == null:
		return
	var dir = 1
	if !player.facing_right:
		dir = -1
	draw_line(collider.position, collider.position + Vector2.RIGHT * player.walk_speed * dir, Color.blue)
