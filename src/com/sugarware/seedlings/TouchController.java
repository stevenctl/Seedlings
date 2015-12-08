package com.sugarware.seedlings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.sugarware.seedlings.entities.players.FireCharacter;
import com.sugarware.seedlings.entities.players.IceCharacter;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class TouchController implements InputProcessor{

	
	
	boolean down;
	private int scheme;
	GdxGame game;
	private Vector3 coords;
	int[] pointer;
	//GUI Batch
	SpriteBatch h;
	//Images
	TextureRegion btnUp;
	TextureRegion btnAction;
	TextureRegion btnPause;
	TextureRegion btnLight;
	
	public TouchController(GdxGame game){
		System.out.println("Touch Controller Initialized");
		h = new SpriteBatch();
		
		scheme = Gdx.app.getPreferences("SeedPeopleControls").getInteger("scheme", 0);
		coords = new Vector3(-1,-1, 0);
		pointer = new int[10];
		this.game = game;
		Gdx.input.setInputProcessor(this);
	}
	
	public boolean touchDown(int screenX, int screenY, int p, int button) {
		coords.set(screenX,screenY,0);
		
		int x = (int) coords.x;
		int y = Gdx.graphics.getHeight() -  (int) coords.y;
		
		switch(scheme){
		case 0:
			if(x > 0 && x < Gdx.graphics.getWidth() / 10 && y > Gdx.graphics.getHeight() - Gdx.graphics.getWidth() / 10 && y < Gdx.graphics.getHeight() ){
				handle(p, true, Keys.ESCAPE);
			}else  if(x > 0 && x <= Gdx.graphics.getWidth() / 4){
				handle(p, true, Keys.A);
			}else if(x > Gdx.graphics.getWidth() / 4 && x <= Gdx.graphics.getWidth() / 2){
				handle(p, true, Keys.D);
			}else if(x > Gdx.graphics.getWidth()  - Gdx.graphics.getWidth() / 8 && x < Gdx.graphics.getWidth() && y > 0 && y < Gdx.graphics.getWidth() / 8){
				if(game.gsm.currentState instanceof PlayGameState)	{
					PlayGameState st = (PlayGameState)game.gsm.currentState;

					if(st.p instanceof FireCharacter ||
						st.p instanceof IceCharacter)handle(p, true, Keys.J);
					
					else handle(p, true, Keys.SPACE);
				}
			}else if(x >Gdx.graphics.getWidth() -   Gdx.graphics.getWidth() / 10 && x < Gdx.graphics.getWidth()  && y > Gdx.graphics.getHeight() - Gdx.graphics.getWidth() / 10 && y < Gdx.graphics.getHeight() ){
				handle(p, true, Keys.I);
			}else if(x >= Gdx.graphics.getWidth() / 2 - 96 && x <= Gdx.graphics.getWidth() / 2 + 32 && y > Gdx.graphics.getHeight() - 80){
				handle(p, true, Keys.T);
			}else if(x > Gdx.graphics.getWidth() / 2){
				handle(p, true, Keys.SPACE);
			}
			
			
			
			break;
			
		case 1:
			
			break;
		}
		
		
		return false;
	}
	
	public boolean touchDragged(int x, int y, int p){
		y = Gdx.graphics.getHeight() -  y;

		switch(scheme){
		case 0:
			if(x > 0 && x < Gdx.graphics.getWidth() / 10 && y > Gdx.graphics.getHeight() - Gdx.graphics.getWidth() / 10 && y < Gdx.graphics.getHeight() ){
				//dummy
			}else if(x > 0 && x <= Gdx.graphics.getWidth() / 4 && pointer[p] == Keys.D){
				touchUp(x,Gdx.graphics.getHeight() + y,p,-1);
				handle(p, true, Keys.A);
			}else if(x > Gdx.graphics.getWidth() / 4 && x <= Gdx.graphics.getWidth() / 2 && pointer[p] == Keys.A){
				touchUp(x,Gdx.graphics.getHeight() + y,p,-1);
				handle(p, true, Keys.D);
			}else if(x > Gdx.graphics.getWidth() / 4 - Gdx.graphics.getWidth() / 8 && x < Gdx.graphics.getWidth() && y > 0 && y < Gdx.graphics.getWidth() / 8 && pointer[p] ==Keys.SPACE){
			
				
				touchUp(x,Gdx.graphics.getHeight() + y,p,-1);
				if(game.gsm.currentState instanceof PlayGameState)	{
					PlayGameState st = (PlayGameState)game.gsm.currentState;

					if(st.p instanceof FireCharacter ||
						st.p instanceof IceCharacter)handle(p, true, Keys.J);
					
					else handle(p, true, Keys.SPACE);
				}
			}
		break;
		}
		
		return false;
	}
	
	public boolean touchUp(int screenX, int screenY, int p, int button) {	
		handle(p,false, pointer[p]);

		return false;
	}
	
	
	
	
	
	public void draw(SpriteBatch g){
		
		g.end();
		h.begin();
		if(btnUp == null){GameStateManager.rm.loadGuiImages();
		btnUp = GameStateManager.rm.btnUp;
		btnAction = GameStateManager.rm.btnAction;
		btnPause = GameStateManager.rm.btnPause;
		btnLight = GameStateManager.rm.btnLight;
		}
		
		
		if(game.gsm.currentState instanceof PlayGameState)	{
			PlayGameState st = (PlayGameState)game.gsm.currentState;
		switch(scheme){
		
		case 0:
			
			if(st.p instanceof FireCharacter ||
				st.p instanceof IceCharacter)h.draw(btnAction, Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 8,  0, Gdx.graphics.getWidth() / 8, Gdx.graphics.getWidth() / 8);
			h.draw(btnPause,0,  Gdx.graphics.getHeight() - Gdx.graphics.getWidth() / 10, Gdx.graphics.getWidth() / 10,Gdx.graphics.getWidth() / 10);
			h.draw(btnLight, Gdx.graphics.getWidth() / 2 , Gdx.graphics.getHeight() - 48,
					48, 48);
			break;
		case 1:
			break;
		}
	}
		h.end();
		g.begin();
	}
	
	
	
	public void setScheme(int s){
		scheme = s;
		Gdx.app.getPreferences("SeedPeopleControls").putInteger("scheme", s);
		Gdx.app.getPreferences("SeedPeopleControls").flush();
	}
	
	public int getScheme(){
		return scheme;
	}
	
	
	
	
	
	
	void handle(int p, boolean d, int k){
		pointer[p] = d ? k : -1;
		down = d ? game.keyDown(k) : game.keyUp(k);
	}
	
	public boolean isDown(int k){
		for(int c : pointer){
			if(k == c)return true;
		}
		return false;
	}
	
	//Unused
	
	public boolean keyDown(int keycode) {if(keycode == Keys.Z)System.out.println(down); return false;}
	public boolean keyUp(int keycode) {return false;}
	public boolean keyTyped(char character) {return false;}
	public boolean mouseMoved(int screenX, int screenY) {return false;}
	public boolean scrolled(int amount) {return false;}

}
