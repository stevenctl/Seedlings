package com.sugarware.seedlings.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class Rope extends ColEntity{

	public float x, y,X3, Y3, a, l;
	float def_angvel;
	public float angvel;
	public boolean r = true;
	Texture rt;
	public float[] vertices;
	int segments = 13;
	Sprite[] sprites;
	
	public Rope(PlayGameState gs, float x, float y, float l, boolean starta){
		super(gs);
		rt = new Texture(Gdx.files.internal("rope.jpg"));
		this.x = x;
		this.y = y;
		this.l = l;
		angvel = (float) (Math.PI  / 120);
		def_angvel = angvel;
		a = (float) (starta ? 5 * Math.PI / 4 : 7 * Math.PI / 4);
		sprites = new Sprite[segments];
		for(int i = 0; i < segments; i++){
			sprites[i] = new Sprite(rt);
			sprites[i].setColor(Color.GREEN);
		}
	}
	public float max = 0f;
	public void update(){
		if(r && a > Math.PI * 7 / 4)r = false;
		if(!r && a < Math.PI * 5 / 4)r = true;
		float thetadelta = (float) Math.abs(a - 3 * Math.PI / 2);
		angvel = (float) (0.0021f +  def_angvel * ((Math.PI / 4 - thetadelta)/(Math.PI / 6) ));
		if(angvel > max)max = angvel;
		
		a += (r ? 1 : -1) *  angvel;
		
	}
	
	public float nextAngle(){
		float da = a;
		da += (r ? 1 : -1) *  angvel;
		return da;
	}
	
	public void draw(SpriteBatch g){
		
		
		float X1 = x; Float Y1 =y;
		 X3 = (float) (x + l * Math.cos(a));
		Y3 = (float) (y + l * Math.sin(a));
		float X2 =  (float) (X1 + l * Math.cos(a) / 8);
		float Y2 =  (float) (Y1 + l * Math.sin(a) / 4);
		float X4 =  (float) (X1 + l * Math.cos(a) / 4);
		float Y4 =  (float) (Y1 + l * Math.sin(a) / 2);
		
	
	
		if(vertices == null)vertices = new float[26];
		vertices = getVerticesFromCurve(X1, Y1, X2, Y2, X4, Y4, X3, Y3, segments);
		drawCurveFromVertices(vertices, g, sprites);
	}
	
	
	
	 
	
	 
	public static void drawCurveFromVertices(float[] vs ,SpriteBatch g, Texture rt){
		 for(int i = 0; i < vs.length - 2; i += 2){
				drawTexturedLine(vs[i], vs[i + 1],vs[i + 2], vs[i + 3],rt,g);
			}
	 }
	
	public static void drawCurveFromVertices(float[] vs ,SpriteBatch g, Sprite[] sprs){
		 for(int i = 0; i < vs.length - 2; i += 2){
				drawTexturedLine(vs[i], vs[i + 1],vs[i + 2], vs[i + 3],sprs[i / 2],g);
			}
	 }
	 
	 
	 public static void drawTexturedLine(float x1, float y1, float x2, float y2, Texture t, SpriteBatch g){
		 float d = (float) Math.sqrt(Math.pow(x2 - x1,2) + Math.pow(y2 - y1,2)) * 1.3f;
		 int h = 10;
		 Sprite spr = new Sprite(t, 0,0,(int) d,h);
		 spr.setOrigin(0, h/2);
		 spr.setPosition(x1, y1);
		 float deg = (float) Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
		 spr.setRotation(deg);
		 
		 spr.draw(g);
		 
	 }
	 
	 public static void drawTexturedLine(float x1, float y1, float x2, float y2, Sprite spr,  SpriteBatch g){
		 float d = (float) Math.sqrt(Math.pow(x2 - x1,2) + Math.pow(y2 - y1,2)) * 1.3f;
		 int h = 7;
		 spr.setSize(d, h);
		 spr.setOrigin(0, h/2);
		 spr.setPosition(x1, y1);
		 float deg = (float) Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
		 spr.setRotation(deg);
		
		 spr.draw(g);
		
		 
	 }
	 
	 
	 public static float[] getVerticesFromCurve (float x1, float y1, float cx1, float cy1, float cx2, float cy2, float x2, float y2, int vertices) {
		return   getVerticesFromCurve ( x1,  y1,  cx1, cy1, cx2,  cy2,  x2, y2,  vertices, new float[vertices * 2]);
				
	 }
	 
	 public static float[] getVerticesFromCurve (float x1, float y1, float cx1, float cy1, float cx2, float cy2, float x2, float y2, int vertices, float[] r) {
			vertices--;
		
			
			float subdiv_step = 1f / vertices;
			float subdiv_step2 = subdiv_step * subdiv_step;
			float subdiv_step3 = subdiv_step * subdiv_step * subdiv_step;

			float pre1 = 3 * subdiv_step;
			float pre2 = 3 * subdiv_step2;
			float pre4 = 6 * subdiv_step2;
			float pre5 = 6 * subdiv_step3;

			float tmp1x = x1 - cx1 * 2 + cx2;
			float tmp1y = y1 - cy1 * 2 + cy2;

			float tmp2x = (cx1 - cx2) * 3 - x1 + x2;
			float tmp2y = (cy1 - cy2) * 3 - y1 + y2;

			float fx = x1;
			float fy = y1;
			
			r[0] = fx;
			r[1] = fy;
			float dfx = (cx1 - x1) * pre1 + tmp1x * pre2 + tmp2x * subdiv_step3;
			float dfy = (cy1 - y1) * pre1 + tmp1y * pre2 + tmp2y * subdiv_step3;

			float ddfx = tmp1x * pre4 + tmp2x * pre5;
			float ddfy = tmp1y * pre4 + tmp2y * pre5;

			float dddfx = tmp2x * pre5;
			float dddfy = tmp2y * pre5;
			int i = 2;
			while (vertices-- > 0) {
				
				//renderer.vertex(fx, fy, 0);
				
				fx += dfx;
				fy += dfy;
				dfx += ddfx;
				dfy += ddfy;
				ddfx += dddfx;
				ddfy += dddfy;
				
				r[i] = fx;
				i++;
				r[i] = fy;
				i++;
				//renderer.vertex(fx, fy, 0);
			}
		
			//renderer.vertex(fx, fy, 0);
			
			//renderer.vertex(x2, y2, 0);
			
			return r;
		}
	 
	 
	 	public int collide(ColEntity e	){
	 		
	 		
	 		if(vertices != null){
	 		
	 			for(int i = 0; i < vertices.length ; i += 2){
	 				if(e.colRect.contains(vertices[i], vertices[i + 1]))return i;
	 				
	 			}
	 		}
	 			
	 		
	 		return -1;
	 	}

	
	
}
