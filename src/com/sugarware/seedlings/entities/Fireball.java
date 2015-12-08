package com.sugarware.seedlings.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.entities.players.Player;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class Fireball extends ColEntity implements Poolable{

	boolean RIGHT;
	
	int height;
	int width;
	boolean bl;
	boolean br;
	boolean tr;
	boolean tl;
	boolean killbuffer;
	public boolean hitenemy;
	int dx;
	int dy;
	
	static Vector2 fix;
	static Vector2 p1;
	static Vector2 p2; 
	static Vector2 p3; 
	static Vector2 p4; 
	
	Animation animation;
	public Fireball(PlayGameState gs,int x,int  y, boolean r) {
		super(gs);
		init(x, y, r);
	}
	public Fireball(PlayGameState gs) {
		super(gs);
	
	}
	
	public void init( int x,int  y, boolean r){
		RIGHT = r;
		this.x = x;
		this.y = y;
		cheight = 32;
		cwidth = 32;
		width = 32;
		height = 32;
		killbuffer = false;
		colRect= new Rectangle(x, y, cwidth, cheight);
		animation = new Animation();
		
		animation.setFrames(GameStateManager.rm.getImages(Resources.Images.FIREBALL).get(0));
		animation.setDelay(25);
	}


	@Override
	public void update() {
		killme = killbuffer;
		killbuffer = hitenemy;
		//Movement
		if(RIGHT){
			dx = (int) (4f * 60f * Gdx.graphics.getDeltaTime());
		}else{
			dx = (int) (-4f * 60f * Gdx.graphics.getDeltaTime());
		}
		dy = 0;
		//Collisions
		MapObjects obs = gs.tilemap.getLayers().get("Collisions").getObjects();
		//Slopes
		for (int i = 0; i < obs.getCount(); i++) {
			if (obs.get(i) instanceof PolylineMapObject) {

				Polyline poly = ((PolylineMapObject) obs.get(i)).getPolyline();
				float cx1 = x + dx;
				float cx2 = x + dx + width;
				float cy = y + dy;
				if(fix == null)fix = new Vector2();
				if(p1 == null)p1 = new Vector2();
				if(p2 == null)p2 = new Vector2();
				if(p3 == null)p3 = new Vector2();
				if(p4 == null)p4 = new Vector2();
				
				 p1.set(poly.getVertices()[0] + poly.getX(), poly.getVertices()[1] + poly.getY());
				 p2.set(poly.getVertices()[2] + poly.getX(), poly.getVertices()[3] + poly.getY());
				 p3.set(cx1, cy);
				 p4.set(cx2, cy);


				if (Intersector.intersectSegments(p1, p2, p3, p4, fix)) {
					killbuffer = true;
				}
			}	
		}
		//Rectangles
		for(int i = 0; i < gs.tilemap.getLayers().get("Collisions").getObjects().getCount(); i++){
			calcCorners(x + dx, y, i);
			if(br || tr || bl || tl){killbuffer = true; }
			calcCorners(x, y + dy, i);
			if(br || tr || bl || tl)killbuffer = true;
		}
		/*Platforms
		for(Platform p : gs.p.plats){
			if(p.colRect.overlaps(colRect)){
				killbuffer = true;
			}
			
		}*/

		x += dx;
		animation.update();
		
	}

	
	public void collide(ArrayList<ColEntity> e){
		super.collide(e);
		for(ColEntity en : e){
			if(colRect.overlaps(en.colRect) && !(en instanceof Player) && en != this){hitenemy = true; }
		}
	
	}
	
	private void calcCorners(float ax, float ay, int i){
		
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
		if(RIGHT)g.draw(animation.getImage(), x, y+ height, width, -height);
		else g.draw(animation.getImage(), x + width, y + height, -width, -height);
		
	}

	@Override
	public void reset() {
		x = -1;
		y = -1;
		RIGHT = false;
	}

}
