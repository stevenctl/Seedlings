package com.sugarware.seedlings.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.sugarware.seedlings.Shaders;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;
public class Water extends ColEntity {
	boolean d = true;
	int width;
	int height;
	int c = 0;

	TextureRegion scr;
	ShapeRenderer s;
	float time;
	float time2;
	ShaderProgram waterShader;
	ShaderProgram waterShader2;
	float scaleX = Gdx.graphics.getWidth() / gs.cam.viewportWidth;
	float scaleY = Gdx.graphics.getHeight() / gs.cam.viewportHeight;
	Vector3 coords;
	public Water(PlayGameState gs, float x, float y, int width, int height) {
		super(gs);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.cwidth = width;
		this.cheight = height;
		colRect.x = x;
		colRect.y = y;
		colRect.width = cwidth;
		colRect.height = cheight;
		coords = gs.cam.project(new Vector3(x, y, 0));
		waterShader = new ShaderProgram(Shaders.vertexShader, Shaders.waterFragmentShader);
		waterShader2 = new ShaderProgram(Shaders.vertexShader, Shaders.water2FragmentShader);
		s = new ShapeRenderer();
		scr = new TextureRegion();
	}

	@Override
	public void update() {


	}


	public void updateshaders() {

		float dt = Gdx.graphics.getDeltaTime();

		if (waterShader != null && waterShader2 != null) {
			time2 += dt / 2;
			time += dt;
			float angle = time * (2 * MathUtils.PI);
			if (angle > (2 * MathUtils.PI)) angle -= (2 * MathUtils.PI);

			float angle2 = time2 * (2 * MathUtils.PI);
			if (angle2 > (2 * MathUtils.PI)) angle2 -= (2 * MathUtils.PI);
			Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			Gdx.gl20.glEnable(GL20.GL_BLEND);
			waterShader.setUniformMatrix("u_projTrans", gs.cam.combined);
			waterShader2.setUniformMatrix("u_projTrans", gs.cam.combined);
			waterShader.begin();
			waterShader.setUniformf("timedelta", -angle);
			waterShader.setUniformf("ampdelta", -angle2);
			waterShader.end();
			waterShader2.begin();
			waterShader2.setUniformf("timedelta", -angle);

			waterShader2.end();
			Gdx.gl20.glDisable(GL20.GL_BLEND);
		}
	}

	ShapeRenderer sr;@Override
	public void draw(SpriteBatch g) {

		g.end();
		
		updateshaders();
		
		//Project world coords to screen coords
		coords.set(x, y, 0);
		coords = gs.cam.project(coords);
		
		//Take texture of the current screen
		scr.setTexture(((PlayGameState) gs).frameBuffer.getColorBufferTexture());
		
		//Cut out the area to distort
		scr.setRegion(
		(int) coords.x, (int) coords.y + 65, 
		(int)((float) width * scaleX), (int)(((float) height * scaleY)));
		
		scr.flip(false, true);

	
		//Apply distortion shader and draw to screen

		if (scr != null) {

			g.setProjectionMatrix(gs.cam.combined);
			g.setShader(waterShader2);
			g.begin();

			g.draw(scr, (int) x, (int) y, (int)(width), (int)((height - GameStateManager.rm.water_top.getRegionHeight() / 4)));

			g.end();


		}
		
		//SURFACE WAVES
		g.setShader(waterShader);
		g.begin();
		g.draw(GameStateManager.rm.water_top, x, y + height - GameStateManager.rm.water_top.getRegionHeight(), width, GameStateManager.rm.water_top.getRegionHeight());


		g.end();
		//BACK TO NORMAL
		g.setShader(null);

		g.begin();
		g.draw(GameStateManager.rm.water, x, y, width, height - GameStateManager.rm.water_top.getRegionHeight());

	}

	/*
     @Deprecated
     void getScreenTex(int x, int y, int w, int h){
        final Pixmap pixmap = new Pixmap(w, h, Format.RGBA8888);
        ByteBuffer pixels = pixmap.getPixels();
       
        Gdx.gl.glReadPixels(x, y, w, h, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, pixels);
        if(scr != null)scr.dispose();
        scr = new Texture(pixmap);
        pixmap.dispose();
       
    }*/

}