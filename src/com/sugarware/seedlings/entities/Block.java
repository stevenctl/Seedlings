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

public class Block extends ColEntity {

	/*
	 * 
	 * Collisions
	 * 
	 */
	
	boolean PUSHED = false;
	
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
	private RectangleMapObject mapObj;
	//collisions
		public boolean bl = false;
		public boolean br = false;
		public boolean tr = false;
		public boolean tl = false;
	
	
	//////////////////////
	
	
	float width, height;
	
	TextureRegion img;
	MapObjects obs;
	public Block(PlayGameState gs, float x, float y, float w, float h) {
		
		this( gs,  x, y,  w,  h, Resources.Images.ROCKBLOCK);
		
	}
	
	public Block(PlayGameState gs, float x, float y, float w, float h, int img){
		super(gs);
		// TODO Auto-generated constructor stub
		GameStateManager.rm.loadImages(img);
		this.img = GameStateManager.rm.getImages(img).get(0)[0];
		this.x = x;
		this.y = y;
		width = w;
		height = h;
		cwidth = (int) w;
		cheight = (int) h;
		mapObj = new RectangleMapObject(x,y,w,h);
		mapObj.setName("block");
		obs = gs.tilemap.getLayers().get("Collisions").getObjects();
		obs.add(mapObj);
	}

	@Override
	public void update() {
		updatePosition(((PlayGameState) gs).p.plats);
		mapObj.getRectangle().set(x, y, cwidth, cheight);
		
		//System.out.println(dx + " < >"  + dy);
	}
	
	
	
	@Override
	public void draw(SpriteBatch g) {
		
		g.draw(img, colRect.x,colRect.y, colRect.width,colRect.height);
		
	}
	@Override
	public void collide(ArrayList<ColEntity> e){
		super.collide(e);
		
	}
	public boolean PUSHABLE_L;
	public boolean PUSHABLE_R;
	public void collide(Player p){
		if(PUSHABLE_R || PUSHABLE_L){
		PUSHED = false;
		if(p.colRect.overlaps(colRect)){
		
			if((/*p.y >= y - 5 && p.y <= y + 5 && */p.dy == 0)){
			if((PUSHABLE_R && p.dx > 0 && p.x < x) || (PUSHABLE_L && p.dx < 0 && p.x > x)){
				
				dx = p.dx;
				PUSHED = true;
				
			
			}
			}
		}
		}
	}
	
	protected  void updatePosition(ArrayList<Platform> plats) {
		PUSHABLE_L = true;
		PUSHABLE_R = true;
		
		dy  =  (ridingPlatform ? -1 : dy - fallspeed);
		
		if (dy < -maxfallspeed) dy = -maxfallspeed;

		
		
		
		if(!PUSHED){
			if (dx > 0) {
				dx -=decel;
				if (dx < 0 ) dx = 0;
			} else if(dx < 0)  {
				dx +=decel;
				if (dx > 0 ) dx = 0;
			} 
		}
		
		

		
		//movement and collision stuff
		
		//platforms
		
		
		
		ridingPlatform = false;
		for(Platform plt : plats){
			if(plt instanceof VertPlatform){
				if(y >= plt.y && y + dy < plt.y && ((x > plt.x && x < plt.x + plt.width)||(x + width > plt.x && x + width < plt.x + plt.width))){
						//dy = y - plt.y;
						y= plt.y;
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
					if(PUSHED){
						dx = p.dx; 
					}
				}
			}
			
			calcCorners(p.colRect, x + dx, y + 1 );
			if(dx > 0 && (br || tr) && !ridingPlatform){dx = 0;  PUSHABLE_R = false; }
			
			if(dx < 0 && (bl || tl) && !ridingPlatform){dx = 0; PUSHABLE_L = false;}
			

			
			
			
			if(dy > 0 && (tl || tr)){dy = 0; }
			
			if(Math.abs(dx)==Math.abs(((MovingPlatform) plt).dx) && dy==0)break;
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
				if(obs.get(i) == mapObj){
				
					continue;
				}
			calcCorners(x + dx , y , i);
			if(dx > 0 && (br || tr)){ PUSHABLE_R = false;}
			if(dx < 0 && (bl || tl)){  PUSHABLE_L = false;}
			
		
			
		
		
			calcCorners(x, y + dy, i);
			
			if(dy < 0 && (br || bl))dy = 0;
			if(dy > 0 && (tl || tr) && !ridingPlatform)dy = 0;
		
			
			calcCorners(x  - 1  , y , i);
			if(bl || tl){ PUSHABLE_L = false; }
			calcCorners(x + 1 , y , i);
			if(br || tr){ PUSHABLE_R = false; }
			
			}
		
		
	
		
			
			x += dx;
			y += dy;
		
	
	}

	
	Vector2 tlc, trc, blc, brc;
	
	
	
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
				if(r1.contains(ax, ay + cheight)){tl = true;} else tr = false;
				
					
						
					
				}
				
			
			}
			
			
		

		
		
		
		
		
	}


}
