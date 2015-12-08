package com.sugarware.seedlings.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.entities.platforms.MovingPlatform;
import com.sugarware.seedlings.entities.platforms.Platform;
import com.sugarware.seedlings.entities.platforms.VertPlatform;
import com.sugarware.seedlings.entities.players.Player;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class Robot extends ColEntity{
	
	public Vector2 poly1;
	public Vector2 poly2;
	//sloping
	

	static Vector2 fix;
	static Vector2 p1;
	static Vector2 p2;
	static Vector2 p3;
	static Vector2 p4;
		public int slopelimit = 24;
		public 	boolean slopeadj = false;
		boolean wasslope = slopeadj;
		//vectors and movement
		public float dx;
		public float dy;
		public boolean ridingPlatform = false;
	public float decel = 1;
	public float accel = 1;
	public float maxspeed = 3;
	public float fallspeed = 1f;
	public 	float maxfallspeed = 6;
	//collisions
		public boolean bl = false;
		public boolean br = false;
		public boolean tr = false;
		public boolean tl = false;
		
		//position and size
		public int width;
		public int height;
	
		//CONTROLS
		public boolean LEFT;
		public boolean RIGHT;
		
		
		//Animation Indicies
		public static final int IDLE = 0;
		public static final int RUN = 1;
		public static final int FALL = 2;
		int currentAction;
		
	ArrayList<TextureRegion[]> frames;
	Animation animation;
	public boolean facingRight;
	public boolean hiding;
	public Robot(PlayGameState gs,float x, float y, boolean right){
		super(gs);
		this.x = x;
		this.y = y;
		this.facingRight = right;
		RIGHT = right;
		LEFT = !right;
		frames = new ArrayList<TextureRegion[]>();
		animation = new Animation();
		currentAction = IDLE;
		width = 48;
		height = 48;
		cwidth = 18;
		cheight = 28;
		poly1 = new Vector2();
		poly2 = new Vector2();
		GameStateManager.rm.loadImages(Resources.Images.ROBOT);
		frames = GameStateManager.rm.getImages(Resources.Images.ROBOT);
		animation.setFrames(frames.get(currentAction));
		colRect = new Rectangle();
		hiding = false;
	}
	
	public Robot(PlayGameState gs,float x, float y, boolean right,boolean hide){
		this(gs, x,y,right);
		hiding = true;
	}
	
	

	public Robot(PlayGameState gs, float x, float y) {
		this(gs,x,y,true);
	}



	@Override
	public void update() {
		maxspeed = 1;
		if(hiding)if(Math.sqrt(Math.pow(gs.p.x - x,2)+Math.pow(gs.p.y - y,2)) < 100)hiding = false;
		updatePosition(((PlayGameState) gs).p.plats);
		
		
		if(dx == 0 && dy == 0 && !slopeadj && !wasslope){
			if(currentAction != IDLE){
				currentAction = IDLE;
				animation.setFrames(frames.get(currentAction));
				animation.setDelay(-1);
			}
		}else if(dx != 0 && ( dy == 0|| ridingPlatform || slopeadj || wasslope) &&  (LEFT || RIGHT)){
			if(currentAction != RUN){
				currentAction = RUN;
				animation.setFrames(frames.get(currentAction));
				animation.setDelay(16);
			}
		}else if(dy < 0 && !ridingPlatform){
			if(currentAction != FALL){
				currentAction = FALL;
				animation.setFrames(frames.get(FALL));
				animation.setDelay(-1);
			}
		}else{
			if(currentAction != IDLE){
				currentAction = IDLE;
				animation.setFrames(frames.get(currentAction));
				animation.setDelay(-1);
			}
		}
		
		if(dx == 0 && dy == 0 && !hiding){
			LEFT = !LEFT;
			RIGHT = !RIGHT;
			facingRight = RIGHT;
		}
		
		animation.update();
		
		
	}

	
protected  void updatePosition(ArrayList<Platform> plats) {
		
	
		dy  =  (ridingPlatform ? -1 : dy - fallspeed);
		if (dy < -maxfallspeed) dy = -maxfallspeed;

		if (RIGHT) {
			dx += accel;
			if (dx > maxspeed) dx = maxspeed;
		} else  {
			dx -= accel;
			if (dx < -maxspeed) dx = -maxspeed;
		} 
		
		
		
		

		
		//movement and collision stuff
		
		//platforms
		
		
		
		ridingPlatform = false;
		for(Platform plt : plats){
			if(plt instanceof VertPlatform){
				if(y >= plt.y && dy < plt.y && ((x > plt.x && x < plt.x + plt.width)||(x + width > plt.x && x + width < plt.x + plt.width))){
						dy = y - plt.y;
						break;
				}
			}else if(plt instanceof MovingPlatform){
				MovingPlatform p = (MovingPlatform) plt;
			calcCorners(p.colRect, x , y + dy -1 - p.vspeed );
			
			
			
			
			calcCorners(p.colRect, x , y + dy -1 - p.vspeed);
			if(dy < 0 && (br || bl)){
				ridingPlatform = true;
				dy = 0;
				if(p.vspeed != 0){y = p.y + p.height; }else{dy = 0;}
				
				if(p.hspeed != 0){
					if(!LEFT && !RIGHT){
						dx = p.dx; 
					}
				}
				break;
			}
			
			calcCorners(p.colRect, x + dx, y + 1 );
			if(dx > 0 && (br || tr) && !ridingPlatform){dx = 0; }
			
			if(dx < 0 && (bl || tl) && !ridingPlatform)dx = 0;
			

			
			
			
			if(dy > 0 && (tl || tr)){dy = 0; }
		}
		}

		
		
		
		MapObjects obs = gs.tilemap.getLayers().get("Collisions").getObjects();


		//slopes
				wasslope = slopeadj;
				slopeadj = false;
				for (int i = 0; i < obs.getCount(); i++) {
					if (obs.get(i) instanceof PolylineMapObject ) {
						if(obs.get(i).getName() != null){
							if(obs.get(i).getName().equals("slope")){
						Polyline poly = ((PolylineMapObject) obs.get(i)).getPolyline();
						float cx1 = x + dx;
						float cx2 = x + dx + width;
						float cy = y + dy;
						 if(p1 == null)p1 = new Vector2();
						 if(p2 == null)p2 = new Vector2();
						 if(p3 == null)p3 = new Vector2();
						 if(p4 == null)p4 = new Vector2();
						 p1.set(poly.getVertices()[0] + poly.getX()  , poly.getVertices()[1] + poly.getY());
						 p2.set(poly.getVertices()[2] + poly.getX(), poly.getVertices()[3] + poly.getY());
						p3.set(cx1, cy);
						p4.set(cx2, cy);

						if(fix == null)fix = new Vector2();
						if (Intersector.intersectSegments(p1, p2, p3, p4, fix)) {
							slopeadj = true;
							for (float j = 0; j < slopelimit; j+=fallspeed) {
								cy = y + dy + j;
								p3.set(cx1, cy);
								p4.set(cx2, cy);
								poly1 = p1;
								poly2 = p2;
								
								if (!Intersector.intersectSegments(p1, p2, p3, p4, fix)) {
									dy += j;
									
									break;
								}
							}
							break;
						}
						//TODO upsidedown sloping
							}//
						}
					}
					
					

					
				}

			

			//Rectangular
		

		
		
		
			for(int i = 0; i < gs.tilemap.getLayers().get("Collisions").getObjects().getCount(); i++){
			if(obs.get(i).getName() != null)	if(obs.get(i).getName().equals("block"))continue;
			calcCorners(x + dx, y, i);
			if(dx > 0 && (br || tr))dx = 0;
			if(dx < 0 && (bl || tl))dx = 0;
			
		
			
		
		
			calcCorners(x, y + dy, i);
			
			if(dy < 0 && (br || bl))dy = 0;
			if(dy > 0 && (tl || tr) && !ridingPlatform)dy = 0;
			
			if(dx==0 && dy==0)break;
			}
		
		
		

		
			if(hiding)dx = 0;
		
			x += dx;
		y += dy;
	}

	
	private void calcCorners(Rectangle r1, float ax, float ay){
		bl = r1.contains(ax, ay);
		br = r1.contains(ax + cwidth, ay);
		tr = r1.contains(ax + cwidth, ay + cheight);
		tl = r1.contains(ax, ay + cheight);
	}

	
	private void calcCorners(float ax, float ay, int i){
		tl = false;
		MapObjects obs = gs.tilemap.getLayers().get("Collisions").getObjects();
		
			if (obs.get(i) instanceof RectangleMapObject) {
				Rectangle r1 = ((RectangleMapObject) obs.get(i)).getRectangle();
				
				
				if (!(obs.get(i).getName() == null)) {
					
					if (!obs.get(i).getName().equals("fall")){ 	
						bl = r1.contains(ax, ay);
						br = r1.contains(ax + cwidth, ay);
						tr = r1.contains(ax + cwidth, ay + cheight);
						tl = r1.contains(ax, ay + cheight);
						
					
					}else{
						if(dx < 0){
						bl = r1.contains(ax, ay);
						br = r1.contains(ax + cwidth, ay);
						tr = r1.contains(ax + cwidth, ay + cheight);
						tl = r1.contains(ax, ay + cheight);
						}
					}
				} else {
				if( r1.contains(ax, ay)) bl = true; else bl = false;
				
				if(r1.contains(ax + cwidth, ay))br = true; else br = false;
				if(r1.contains(ax + cwidth, ay + cheight))tr = true; else tr = false;
				if(r1.contains(ax, ay + cheight))tl = true; else tr = false;
				
					
						
					
				}
				
			
			}
			
			
		

		
		
		
		
		
	}

	
	
	@Override
	public void draw(SpriteBatch g) {
		if (facingRight) {	
			if(animation.getImage() != null)g.draw(animation.getImage(), x -cwidth /4 ,y + height , width, -height);	
		} else {
			if(animation.getImage() != null)g.draw(animation.getImage(),  x + width -cwidth / 4 , y + height ,-width, -height);
		}
		
	}



	@Override
	public void collide(ArrayList<ColEntity> e) {
		super.collide(e);
		for(ColEntity en : e){
			if(en instanceof Fireball){
				Fireball f = (Fireball) en;
				if(f.colRect.overlaps(colRect)){killme = true; }
			}else if(en instanceof Player){
				
				
			}
		}
	}
}
