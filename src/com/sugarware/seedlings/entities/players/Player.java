package com.sugarware.seedlings.entities.players;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sugarware.lone.LoneBad;
import com.sugarware.lone.LonePlayer;
import com.sugarware.seedlings.entities.Animation;
import com.sugarware.seedlings.entities.Block;
import com.sugarware.seedlings.entities.Bulldozer;
import com.sugarware.seedlings.entities.ColEntity;
import com.sugarware.seedlings.entities.Robot;
import com.sugarware.seedlings.entities.Rope;
import com.sugarware.seedlings.entities.Water;
import com.sugarware.seedlings.entities.platforms.MovingPlatform;
import com.sugarware.seedlings.entities.platforms.Platform;
import com.sugarware.seedlings.entities.platforms.VertPlatform;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;

public abstract class Player extends ColEntity {
	public Animation transition;
	//oldanimatiion
	public Animation oldanim;
	protected float oldalpha;
	float ox;
	float oy;
	public float alpha;
	
	
	//Vector
	public Vector2 poly1;
	public Vector2 poly2;
	
	Vector2 fix;
	Vector2 p1;
	Vector2 p2;
	Vector2 p3;
	Vector2 p4;
	float camx;
	float camy;
	//CONTROLS
	public boolean LEFT;
	public boolean RIGHT;
	public boolean UP;
	public boolean DOWN;
	public boolean trueUP;
	int lastKey;
	//position and size
	public int width;
	public int height;
	public boolean camera = true;;
	
	//sloping
	public float slopelimit = 100;
	public 	boolean slopeadj = false;
	public float slopeadjustment;
	//vectors and movement
	public float dx;
	public float dy;
	
	public float decel = 0.5f;
	public float accel = 0.4f;
	public float defaccel = 0.4f;
	public float maxspeed = 2;
	public float defmaxspeed = 2;
	public float slomaxspeed = 1f;
	public float fallspeed = 0.4f;
	
	public float def_fallspeed = fallspeed;
	public 	float maxfallspeed = 5;
	public 	float jumpspeed = 6f;
	//health
	int health;
	
	public boolean unhappy = false;

	//states
	protected boolean wasslope;
	public boolean jumping = false;
	boolean stopjump;
	public boolean facingRight= true;
	public boolean ridingPlatform = false;
	public boolean dead;
	public boolean inWater;
	public boolean underWater;
	//collisions
	public boolean bl = false;
	public boolean br = false;
	public boolean tr = false;
	public boolean tl = false;;
	public Rectangle mapTangle;
	public int hitCooldown = -1;
	//platforms
	public ArrayList<Platform> plats;
	//ropes
	public boolean ropeSwing = false;
	boolean canSwing = true;
	boolean didSwing = false;
	boolean canRelease = false;
	int rindex = -1;
	protected Rope myrope;
    protected float lastX;
    float rope_moment;
    float rope_ltheta  = -1f;
    
	//abstract
	public Animation animation;
	public ArrayList < TextureRegion[] > frames;
	public int currentAction;
	
	protected TextureRegion[] transframes;
	
	
	public Player(PlayGameState ps, float x, float y) {
		super(ps);
		this.x = x;
		this.y = y;
		width = 48;
		height = 48;
		cwidth = 26;
		cheight = 32;
		
		colRect = new Rectangle();
		mapTangle = new Rectangle();
		UP = false;
		DOWN = false;
		RIGHT = false;
		LEFT = false;
		plats = new ArrayList<Platform>();
		
		poly1 = new Vector2();
		poly2 = new Vector2();
		
		alpha = 1;
		
		hitCooldown = -1;
		rope_moment = 0;
		health = 3;
	}
	
	@SuppressWarnings("unchecked")
	public Player(PlayGameState ps, Player pl){
		this(ps, pl.x, pl.y);
		animation = new Animation();
		oldanim = pl.animation;
		LEFT = pl.LEFT; RIGHT = pl.RIGHT; UP = pl.UP; DOWN = pl.DOWN;
		width = pl.width; height = pl.height; cwidth = pl.cwidth; cheight = pl.cheight;
		slopeadj = pl.slopeadj;
		dx = pl.dx; dy = pl.dy;
		colRect = pl.colRect;
		jumping = pl.jumping;
		facingRight = pl.facingRight;
		ridingPlatform = pl.ridingPlatform;
		bl = pl.bl; br = pl.br; tl = pl.tl; tr = pl.tr;
		plats = (ArrayList<Platform>) pl.plats.clone();
		alpha = 0.0f;
		oldalpha = 1f;
		ox = pl.x;
		oy = pl.y;
		health = pl.health;
		
	}

	
	public void alphaUpdate(){
if(oldanim !=null ){
			
			if(oldalpha>0)oldalpha -= 0.02f;
			if(oldalpha<=0)oldanim = null;
			
		}

if(transition != null){
	
	if(transition.hasPlayedOnce())transition = null;
	else transition.update();
}

		if(alpha<1 && !dead)alpha += 0.015f;
		if(alpha > 1)alpha = 1;
	}
	
	
	
	@Override
	public void update() {
		
		alphaUpdate();
		
		
		for(int i = 0; i < plats.size(); i++){
			Platform p = plats.get(i);
			p.update();
			if(p.killme){plats.remove(i);
			i--;}
		}
		if(hitCooldown > -1)hitCooldown--;
		
		
		updatePosition();
		if((dy < 0 && jumping) || stopjump){jumping = false; stopjump = false;}
		if(dy == 0 && jumping)stopjump = true;
		
		camUpdate(); 
		
		mapTangle.width = cwidth;
		mapTangle.height = cheight;
		mapTangle.x = x+ (width - cwidth) / 4;
		mapTangle.y = y ;

	}
	
	public void camUpdate(){
		//camera position
		if(camera){
		 camx = x;
		 camy = y;
		 
		 if(gs.cam.viewportWidth < gs.w)
		while(!(camx - gs.cam.viewportWidth / 2 >= 0 && camx + gs.cam.viewportWidth / 2 <= gs.w)){
			if(camx - gs.cam.viewportWidth / 2 < 0)camx+=0.01;
			if(camx + gs.cam.viewportWidth / 2 > gs.w)camx-=0.01;
			
		}
		gs.cam.position.x = camx;
		
		 if(gs.cam.viewportHeight < gs.h)
		while(!(camy - gs.cam.viewportHeight / 2 >= 0 && camy + gs.cam.viewportHeight / 2 <= gs.h)) {
			if(camy - gs.cam.viewportHeight / 2 < 0)camy+=0.01;
			if(camy + gs.cam.viewportHeight / 2 > gs.h)camy-=0.01;
		}
		gs.cam.position.y = camy;
		if ((x - gs.cam.viewportWidth / 2 > 0 && x + gs.cam.viewportWidth / 2 < gs.w) || (y - gs.cam.viewportHeight / 2 > 0 && y + gs.cam.viewportHeight / 2 < gs.h)) gs.cam.update();
		}
	}

	protected  void updatePosition() {
		
		if(didSwing && dx > 0){
			facingRight = true;
		}else if(didSwing && dx < 0){
			facingRight = false;
		}
		
		
		dy  =  (ridingPlatform ? -1 : dy -( trueUP && dy > 0 ?2 *fallspeed / 3: fallspeed));
		if (dy < -maxfallspeed) dy = -maxfallspeed;
		maxspeed = (inWater || block ? slomaxspeed : defmaxspeed);
	
		if (RIGHT && canSwing && lastKey != 0) {
			dx += accel;
			if (dx > maxspeed ) dx = maxspeed;
		} else if (LEFT & canSwing) {
			dx -= accel;
			if (dx < -maxspeed ) dx = -maxspeed;
		}else {
			boolean r = false, l = false;
			if (dx > 0) r = true;
			if (dx < 0) l = true;
			if(r && !didSwing){
				 dx -= decel;
				 if (dx < 0) dx = 0;
			}else if(l && !didSwing){
				dx += decel;
				if (dx > 0) dx = 0;
			}else if(r && didSwing){
				 dx -= decel / 4;
				 if (dx < 0) dx = 0;
			}else if(l && didSwing){
				dx += decel / 4;
				if (dx > 0) dx = 0;
			}
			
		}
	
		for(int i = 0; i < gs.tilemap.getLayers().get("Collisions").getObjects().getCount() && UP; i++){
			if(gs.tilemap.getLayers().get("Collisions").getObjects().get(i).getName() != null)
				if(gs.tilemap.getLayers().get("Collisions").getObjects().get(i).getName().equals("block"))continue;
			
			calcCorners(x , y + dy, i);
		
			if (bl || br || slopeadj) {
			if(slopeadj)dy-=slopeadjustment;
			dy += jumpspeed; jumping = true;
			UP = false;
			}
		
			
		}
		
		
		

		
		//movement and collision stuff
		
		//platforms

		ridingPlatform = false;
		for(Platform plt : plats){
			if(plt instanceof VertPlatform){
				if(dy < 0){
					if(y >= plt.y && y + dy < plt.y){
						if((x  + (width - cwidth ) / 4 > plt.x && x < plt.x + plt.width) ||	(x + (width - cwidth ) / 4 + cwidth> plt.x && x + cwidth < plt.x + plt.width)){
							dy = 0;
							canSwing = true;
							didSwing = false;
							if(UP){
								dy += jumpspeed; jumping = true;
								UP = false;
							}
						}
					}
				}
			}else if(plt instanceof MovingPlatform){
				MovingPlatform p = (MovingPlatform) plt;
			calcCorners(p.colRect, x , y + dy -1 - p.vspeed );
			
			if ((bl || br || slopeadj) && UP) {
				if(slopeadj)dy-=slopeadjustment;
				dy += jumpspeed; jumping = true;
				UP = false;
				
			}
			
			
			calcCorners(p.colRect, x , y + dy -1 - p.vspeed);
			if(dy < 0 && (br || bl)){
				ridingPlatform = true;
				dy = 0;
				canSwing = true;
				didSwing = false;
				if(p.vspeed != 0){y = p.y + p.height; }else{dy = 0;}
				
				if(p.hspeed != 0){
					if(!LEFT && !RIGHT){
						dx = p.dx; 
					}
				}
			}
			
			calcCorners(p.colRect, x + dx, y + 1 );
			if(dx > 0 && (br || tr) && !ridingPlatform){dx = 0; }
			
			if(dx < 0 && (bl || tl) && !ridingPlatform)dx = 0;
			

			
			
			
			if(dy > 0 && (tl || tr)){dy = 0; canSwing = true; didSwing = false; }
		}
		}
		
		
		
		
		
		MapObjects obs = gs.tilemap.getLayers().get("Collisions").getObjects();

		
		//slopes
		wasslope = slopeadj;
		slopeadj = false;
		for (int i = 0; i < obs.getCount(); i++) {
			if(obs.get(i) != null)if(obs.get(i).getName() != null)if(obs.get(i).getName().equals("block"))continue;
			if (obs.get(i) instanceof PolylineMapObject ) {
				if(obs.get(i).getName() != null){
					if(obs.get(i).getName().equals("slope")){////////////////////////////
				Polyline poly = ((PolylineMapObject) obs.get(i)).getPolyline();
				float cx1 = x + dx  + ((width - cwidth) / 4) - 4 ;
				float cx2 = x + dx  + cwidth + 4;
				float cy = y + dy;
				 if(p1 == null)p1 = new Vector2();
				 if(p2 == null)p2 = new Vector2();
				 if(p3 == null)p3 = new Vector2();
				 if(p4 == null)p4 = new Vector2();
				 p1.set(poly.getVertices()[0] + poly.getX() - 8 , poly.getVertices()[1] + poly.getY()- 4) ;
				 p2.set(poly.getVertices()[2] + poly.getX(), poly.getVertices()[3] + poly.getY());
				p3.set(cx1, cy);
				p4.set(cx2, cy);

				if(fix == null)fix = new Vector2();
				if (Intersector.intersectSegments(p1, p2, p3, p4, fix)) {
					slopeadj = true;
					canSwing = true;
					didSwing = false;
					for (float j = 0; j < slopelimit; j+=fallspeed / 4) {
						cy = y + dy + j;
						p3.set(cx1, cy);
						p4.set(cx2, cy);
						poly1 = p1;
						poly2 = p2;
						//dy +=  y + dy - fix.y;
						if (!Intersector.intersectSegments(p1, p2, p3, p4, fix) ) {
							dy += j;
							slopeadjustment = j;
							slopeadj = true;
							break;
						}
					}
				}
				//TODO upsidedown sloping
					}///////////////////////////////////////////////////////
				}
			}
			
			

			
		}

		
		//Rectangular
		
			for(int i = 0; i < gs.tilemap.getLayers().get("Collisions").getObjects().getCount(); i++){
				if(gs.tilemap.getLayers().get("Collisions").getObjects().get(i).getName() != null)
					if(gs.tilemap.getLayers().get("Collisions").getObjects().get(i).getName().equals("block"))continue;
				
			calcCorners(x + dx, y, i);
			if(dx > 0 && (br || tr)){dx = 0;}
			if(dx < 0 && (bl || tl)){ dx = 0;}
			
		
			
		
		
			float d = calcCorners(x, y + dy, i);
			
			if(dy < 0 && (br || bl)){dy  = d; canSwing = true; didSwing = false;}
			if(dy > 0 && (tl || tr) && !ridingPlatform){dy = 0; canSwing = true; didSwing = false;}
			
			}
		
		
		

		
			if(this instanceof IceCharacter && inWater && dy< 0){dy = 0;
			canSwing = true;
			didSwing = false;
			if(UP){dy += jumpspeed; jumping = true;
			UP = false;}
			}
		
			
			if(transition != null || alpha < 0 || oldanim != null){
				dx = 0;
				dy = 0;
			}
			
			
			//Vines
			if(!ropeSwing && trueUP && canSwing)
			for(ColEntity c : ((PlayGameState) gs).entities){
				
				if(c instanceof Rope){
					Rope r = (Rope)c;
					int i = r.collide(this);
					if(i > 0){
						myrope = r;
						rindex = i;
						ropeSwing = true;
						canRelease = false;
						
						break;
					}
				
				}
				
			}
			
			
			if(ropeSwing ){
				 
				//float x1 = myrope.x;
				//float y1 = myrope.y;
				if(rope_ltheta == -1f){
					rope_ltheta = myrope.a;
				}else{
				rope_moment += 	(float)Math.abs(myrope.a - rope_ltheta)/(Math.PI / 2);
				if(rope_moment > 1)rope_moment = 1;
				rope_ltheta = myrope.a;
				}
				float x2 = myrope.vertices[rindex];
				float y2 = myrope.vertices[rindex + 1];
				x = x2 - (facingRight ? cwidth :cwidth / 4  );
				y = y2 - height + 38;
				//System.out.println((Math.sqrt(Math.pow(myrope.x - myrope.vertices[rindex ],2) + Math.pow(myrope.y - myrope.vertices[rindex + 1],2))));
				
			     	
				
				
				if(!trueUP && canRelease){
					canSwing = false;
					ropeSwing = false;
				
		                			//float len = (float) Math.sqrt(Math.pow(x2 - x1,2) + Math.pow(y2 - y1,2));
				
				
					
					
				
					
					//float myang = (float) (myrope.a + (r ? Math.PI / 16 : - Math.PI / 16));
					//if(myang > 7 * Math.PI / 4)myang = (float) (7 * Math.PI / 4);
					//if(myang < 5 * Math.PI / 4)myang = (float) (5 * Math.PI / 4);
					//float hor_scale = (float) (r ?  (myang - 5 * Math.PI / 4)/ (Math.PI / 2) : 
					//	( 7 * Math.PI / 4 - myang)/ (Math.PI / 2));
					float hor_scale;
					if(myrope.r){
						hor_scale = (float) Math.abs((myrope.a - (5*Math.PI / 4)) / (Math.PI/ 2));
					}else{
						hor_scale = (float) - Math.abs((myrope.a - (7*Math.PI / 4))/ (Math.PI/ 2));
					
					}
					hor_scale = (float) (hor_scale + ((myrope.r ? 1 : -1) * Math.sqrt(Math.pow(myrope.x - myrope.vertices[rindex ],2) + Math.pow(myrope.y - myrope.vertices[rindex + 1],2))/myrope.l)) ;
					System.out.println("Hor " + hor_scale);
				
					
					dx = rope_moment *  (maxspeed * 0.9f) *  hor_scale;
					facingRight = dx > 0;
					dy =     (float) (jumpspeed )  ;
					canSwing = true;
					didSwing = true;
					rope_ltheta = -1f;
					rope_moment = 0f;
					updatePosition();
				}
				lastX = x;
			}
		
		for(ColEntity en : gs.entities)	{
		if(en instanceof Block){
				if(colRect.overlaps(en.colRect)){
					block = true;
					
				}
				//fix weird -0.4 dy when standing on a block
			
				
				if(!((Block) en).PUSHABLE_L || !(dy >= -0.4f && dy <= 0)){
					if(dx < 0)if(en.colRect.contains(mapTangle.x + dx, mapTangle.y) ||  en.colRect.contains(mapTangle.x + dx, mapTangle.y + mapTangle.height))dx = 0;
					
				}
				if(!((Block) en).PUSHABLE_R || !(dy >= -0.4f && dy <= 0)){
				if(dx > 0)if(en.colRect.contains(mapTangle.x + mapTangle.width + dx, mapTangle.y) ||  en.colRect.contains(mapTangle.x + mapTangle.width + dx, mapTangle.y + mapTangle.height))dx = 0;
				}
			
				if(dy < 0){
					if(en.colRect.contains(mapTangle.x , mapTangle.y + dy) ||  en.colRect.contains(mapTangle.x + mapTangle.width, mapTangle.y + dy)){dy = 0; if(UP){dy += jumpspeed; jumping = true;
					UP = false;}}
				}
				
				
				}
		}
		maxspeed = (inWater || block ? slomaxspeed : defmaxspeed);
		if (dx > maxspeed ) dx = maxspeed;
		if (dx < -maxspeed ) dx = -maxspeed;
		
		if(!ropeSwing){
			x += dx;
			y += dy;
			lastX = x;
			}	
			
			boolean thisLoop = false;
			
			for(ColEntity c : ((PlayGameState) gs).entities){
				if(c instanceof Water){
					calcCorners(c.colRect,x,y);
					 
					if(bl || br){
						thisLoop = true;
						inWater = true;
						if(tr || tl)underWater = true;
						break;
					}else if(!thisLoop){inWater = false; underWater = false;};
					
				}
			}
			
			
	}

	
	private void calcCorners(Rectangle r1, float ax, float ay){
		bl = r1.contains(ax + (width - cwidth) / 4 , ay);
		br = r1.contains(ax + (width - cwidth) / 4  + cwidth, ay);
		tr = r1.contains(ax + cwidth + (width - cwidth) / 4 , ay + cheight);
		tl = r1.contains(ax + (width - cwidth) / 4 , ay + cheight);
	}

	
	@SuppressWarnings("unused")
	private void calcCorners(float ax, float ay, Rectangle r1){

		bl = r1.contains(ax + (width - cwidth) / 4 , ay);
		br = r1.contains(ax + cwidth + (width - cwidth) / 4, ay);
		tr = r1.contains(ax + cwidth + (width - cwidth) / 4, ay + cheight);
		tl = r1.contains(ax + (width - cwidth) / 4 , ay + cheight);
	}
	
	private float calcCorners(float ax, float ay, int i){
		
		MapObjects obs = gs.tilemap.getLayers().get("Collisions").getObjects();
		
			if (obs.get(i) instanceof RectangleMapObject) {
				Rectangle r1 = ((RectangleMapObject) obs.get(i)).getRectangle();
				
				
			
			
					bl = r1.contains(ax + (width - cwidth) / 4 , ay);
					br = r1.contains(ax + cwidth + (width - cwidth) / 4, ay);
					tr = r1.contains(ax + cwidth + (width - cwidth) / 4, ay + cheight);
					tl = r1.contains(ax + (width - cwidth) / 4 , ay + cheight);
					
					
						
					if(bl || br){
						return 0  ;
					}
				
				
			
			}
			
			
		
			return 0;
		
		
		
		
		
	}

	@Override
	public void draw(SpriteBatch g) {
		for(Platform p : plats){
			p.draw(g);
			/*g.end(); 
			s.setProjectionMatrix(gs.cam.combined);
			s.setColor(Color.YELLOW);
			s.begin(ShapeType.Line);
			s.rect(p.colRect.x, p.colRect.y, p.colRect.width, p.colRect.height);
			s.end();
			g.begin(); */
			
		}
		if(oldanim!=null){
			
			g.setColor(1.0f, 1.0f, 1.0f, oldalpha);
			if (facingRight) {	
				if(oldanim.getImage() != null)g.draw(oldanim.getImage(), x -cwidth /4 ,y + height , width, -height);	
			} else {
				if(oldanim.getImage() != null)g.draw(oldanim.getImage(),  x + width -cwidth / 4 , y + height ,-width, -height);
			}
		}
		

		
		
		//font.draw(g, x + ", " + y, x, y);

	}

	Rectangle getRekt(Rectangle r, int x, int y) {
		r.x = x + (width - cwidth) / 2 ;
		r.y = y;
		r.width = cwidth;
		r.height = cheight;
		return r;
	}
	//Gets small rectangle from the bottom of the player
	Rectangle getBRekt(Rectangle r, int x, int y) {
		r.x = x + (width - cwidth) / 2 ;
		r.y = y;
		r.width = cwidth;
		r.height = 1;
		return r;
	}


	public void keyPressed(int k) {
		if(transition == null){if (k == Keys.A && transition == null && oldanim == null ){lastKey = 0; LEFT = true; if(!ropeSwing) facingRight = false;}
		if (k == Keys.D  && oldanim == null ){lastKey = 1; RIGHT = true; if(!ropeSwing)facingRight = true;}
		if (k == Keys.SPACE && (dy == 0 || slopeadj)){ UP = true;
		
		}
		if(k == Keys.SPACE)trueUP = true;
		}
		//if(ropeSwing && k == Keys.SPACE) canRelease = true;
		
	}

	public void keyReleased(int k) {
		if (k == Keys.A){ LEFT = false; if(lastKey == 0)lastKey = -1;}
		if (k == Keys.D){ RIGHT = false;  if(lastKey == 1)lastKey = -1;}
if(k == Keys.HOME)System.out.println(tl +", " + tr + ", " + bl + ", " + br);
		if(k == Keys.SPACE && ropeSwing){canSwing  = true;
		
		canRelease=true;
		}
		if(k == Keys.SPACE)trueUP = false;

	}

	public void onTouchDown(float x, float y) {
		
	}
	
	boolean block = false;
	@Override
	public void collide(ArrayList<ColEntity> e) {
		block = false;
		colRect.width = cwidth;
		colRect.height = cheight;
		colRect.x = x+ (width - cwidth) / 4;
		colRect.y = y ;
		for(ColEntity en : e){
			if(en instanceof Robot || en instanceof LoneBad){
				ColEntity r = en;
				if(r.colRect.overlaps(colRect) && hitCooldown < 0){
					//dx += r.facingRight ? 6 : -6;
					//dy += 3;
					
					if(r.x>x)dx = 2 * -maxspeed;
					if(r.x < x)dx =2 * maxspeed;
					dy = jumpspeed / 2;
					hitCooldown = 80;
					health--;
					if(health <= 0)dead=true;
					
				
				}
			} else if(en instanceof Bulldozer){
				if(en.colRect.overlaps(colRect)){
					dead = true;
				}
				
			}
					
			
		
		}
	}
	
	//entity holding
		public void add(Platform p){
			
			plats.add(p);
		}

		TextureRegion[] healthImg;
		public void drawHealth(SpriteBatch g) {
			float drx =  (gs.cam.position.x - gs.cam.viewportWidth / 2);
			float dry =  (gs.cam.position.y + gs.cam.viewportHeight / 2);
			if(healthImg == null){
				if(this instanceof LonePlayer)GameStateManager.rm.loadAltHearts();
				else GameStateManager.rm.loadGuiImages();
			healthImg = GameStateManager.rm.healthIcon;
			}
			for(int i = 0; i < 3; i++){
				if(i < health)g.draw(healthImg[0], drx , dry - 24,24,24);
				else g.draw(healthImg[1], drx , dry - 24,24,24);
				drx +=  (24 + 1);
			}
			
			
		}
	

}