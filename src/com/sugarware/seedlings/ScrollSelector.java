package com.sugarware.seedlings;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.sugarware.seedlings.gamestate.GameState;

public class ScrollSelector {

 public float theta;
	float startangle;
	float endangle;
	public boolean scrolling;
	float r;
	public ArrayList<TextureRegion> list;
	int iw;
	int ih;
	Vector3 coords;
	Vector3 coordsin;
	GameState gs;
	public int sel = 0;
	public int cd = -1;
	
	public ScrollSelector(ArrayList<TextureRegion> list, float r, int iconW, int iconH, GameState gs){
		this.list = list;
		this.r = r;
		iw =iconW;
		ih =iconH;
		
		coords = new Vector3();
		this.gs = gs;
		if(list.size() < 6){
			int s = list.size();
			for(int i = s; i < 2 * s; i++){
				list.add(list.get(i - s));
			}
		}
	}
	
	public void draw(SpriteBatch g) {	
		coords.x = gs.cam.position.x + gs.cam.viewportWidth / 2;
		coords.y = gs.cam.position.y + gs.cam.viewportHeight / 2;
		 if(cd > 0)cd--;
		 
		
		if(scrolling){
			theta -= Math.PI  * 2 /  list.size() / 16;
			if(theta >= Math.PI * 2)theta -= Math.PI * 2;
			if(theta < 0)theta += Math.PI * 2;
			if(theta >= endangle - 0.02f && theta <= endangle + 0.02f){scrolling = false;
			cd = 30;
			}
			//System.out.println("t = " + theta);
			//System.out.println("e = " + endangle);
		}
		
		float angle = (float) (Math.PI / 4);
		for(int i = 0; i < list.size(); i++){
		
			
			
			float x = (float) (Math.cos(angle + theta) * r);
			float y = (float) (Math.sin(angle + theta) * r);
			
			g.draw(list.get(i), coords.x + x - iw / 2, coords.y + y - ih / 2,iw,ih);
			angle += 2 * Math.PI / list.size();
			if(angle >= Math.PI * 2)angle =0;
		}
		
	}
	
	public void touchDown(int sx, int sy){
		cd = -1;
		if(!scrolling){
			sel++;
			if(sel >= list.size() / 2)sel = 0;
			System.out.println(sel);
			scrolling = true;
			startangle = theta;
			endangle = (float) (theta - Math.PI * 2 / list.size());
			if(endangle >=  Math.PI * 2 )endangle -= Math.PI * 2 ;
			if(endangle < 0)endangle+= Math.PI * 2;
			
		}
	}
}
