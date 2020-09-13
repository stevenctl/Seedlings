extends KinematicBody2D
class_name PlayerMovement

# tuneable constants
export(int) var walk_speed: int = 500
export(int) var jump_speed: int = 1000
export(int) var grav_accel: int = 600 * 6

export(bool) var facing_right: bool = true

onready var input: Vector2 = Vector2()
onready var velocity: Vector2 = Vector2()

onready var sprite: AnimatedSprite =  get_node("AnimatedSprite")

func _ready():
	sprite.connect("animation_finished", self, "end_dive")
	

func _physics_process(delta: float):
	# do movement - move and slide must happen to know if we're on ground/ceil
	velocity = move_and_slide(velocity, Vector2.UP)
	
	# precompute movement for next frame
	_get_input()
	_move_h()
	_move_y(delta)
	_resolve_animation()
	

func _get_input():
	input = Vector2.ZERO
	if Input.is_action_pressed("left"):
		input.x -= 1
	if Input.is_action_pressed("right"):
		input.x += 1

var dive_dir = 0

func _move_h():
	if dive_dir == 0:
		if input.x > 0:
			facing_right = true
		elif input.x < 0:
			facing_right= false
			
	if Input.is_action_just_pressed("dive"):
		dive_dir = 1 if facing_right else -1
		velocity.y = 0
		jump_time = 0
	
	if dive_dir == 0:
		velocity.x = input.x * walk_speed
	else:
		velocity.x = dive_dir * walk_speed * 2
		var dive_cancel = (Input.is_action_just_pressed("left") && dive_dir == 1) || (Input.is_action_just_pressed("right") && dive_dir == -1)
		if dive_cancel and sprite.animation == "dive":
			sprite.frame = max(sprite.frame, sprite.frames.get_frame_count("dive") - 6)
			
			
		
		
	
	
func end_dive():
	dive_dir = 0
		


export(float) var max_coyote_time: float = .1
var coyote_time: float = max_coyote_time

# time we can hold the jump button down for
export(float) var max_jump_time: float = .25
var jump_time: float = max_jump_time

# jumping state - can't use is_action_pressed because we only flip this under
# certain conditions (can jump, debounce)
var jumping: bool = false

# allows the player to hit jump max_jump_debounce seconds before hitting the ground
const max_jump_debounce: float = .2
var jump_debounce: float = 0

func _move_y(delta: float):
	# quick jump cancel
	if jumping and !Input.is_action_pressed("jump"):
		jumping = false
		jump_time = 0
		jump_debounce = 0
		# 2 frames of hangtime
		velocity.y = max(2*grav_accel*delta, velocity.y)

	# can we jump
	if is_on_floor():
		jumping = false
		jump_time = max_jump_time
		coyote_time = max_coyote_time
	else:
		coyote_time -= delta
		
	# should we jump
	if Input.is_action_just_pressed("jump"):
		jump_debounce = max_jump_debounce
	else:
		jump_debounce -= delta
	
	if coyote_time > 0 and jump_debounce > 0 and dive_dir == 0:
		jumping = true
	
	# jump or fall
	if jumping and jump_time > 0:
		velocity.y = -jump_speed
		jump_time -= delta
	else:
		velocity.y += grav_accel * delta
		
	velocity.y = min(velocity.y, 2.25 * jump_speed)
	
func _resolve_animation():
	# TODO - move animation logic into a proper animation controller
	sprite.flip_h = !facing_right	
	if dive_dir != 0:
		sprite.play("dive")
		return
		
	
	if !is_on_floor():
		if velocity.y > 0:
			sprite.play('fall')
		else:
			sprite.play('jump')
	elif input.x != 0:
		sprite.play('run')
	else:
		sprite.play('idle')
