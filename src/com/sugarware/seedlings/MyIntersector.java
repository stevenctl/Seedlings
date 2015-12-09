package com.sugarware.seedlings;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MyIntersector {
	
	static Polygon rPoly;
	static float[] fl;
	static Vector2 v2;
	static Vector2 v2_;
	
	//Custom intersector for polylines and rectangles. Used for the slope collisions.
	public static boolean isCollision(Polygon p, Rectangle r) {
		if(rPoly == null)rPoly = new Polygon();
		if(fl == null)fl = new float[8];
		
		fl[0] = 0; fl[1] = 0; fl[2] = r.width; fl[3] = 0; fl[4] = r.width; fl[5] = r.height; fl[6] = 0; fl[7] = r.height;
		rPoly.setVertices(fl);
	    rPoly.setPosition(r.x, r.y);
	   
	    return Intersector.overlapConvexPolygons(rPoly, p);
	   
	}
	
	public static boolean isCollision(Polyline p, float x1, float x2, float y1, float y2, Vector2 ip) {
		 
		return Intersector.intersectLines(p.getVertices()[0],p.getVertices()[1],p.getVertices()[2], p.getVertices()[3],
											x1, y1, x2,y2, ip);
	}
	
	public static boolean isCollision(Polygon p, float x1, float x2, float y1, float y2){
		if(v2 == null)v2 = new Vector2();
		if(v2_ == null)v2_ = new Vector2();
		v2.set(x1, y1);
		v2_.set(x2, y2);
		
		return Intersector.intersectLinePolygon(v2,v2_, p);
	}
}
