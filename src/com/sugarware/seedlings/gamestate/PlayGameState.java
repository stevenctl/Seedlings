 package com.sugarware.seedlings.gamestate;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.sugarware.seedlings.GdxGame;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.ScrollSelector;
import com.sugarware.seedlings.entities.Block;
import com.sugarware.seedlings.entities.Bulldozer;
import com.sugarware.seedlings.entities.ColEntity;
import com.sugarware.seedlings.entities.Entity;
import com.sugarware.seedlings.entities.Explosion;
import com.sugarware.seedlings.entities.Fireball;
import com.sugarware.seedlings.entities.FireballPool;
import com.sugarware.seedlings.entities.Light;
import com.sugarware.seedlings.entities.Powerup;
import com.sugarware.seedlings.entities.Robot;
import com.sugarware.seedlings.entities.Rope;
import com.sugarware.seedlings.entities.SmallExplosion;
import com.sugarware.seedlings.entities.Water;
import com.sugarware.seedlings.entities.platforms.Platform;
import com.sugarware.seedlings.entities.platforms.VertPlatform;
import com.sugarware.seedlings.entities.players.FireCharacter;
import com.sugarware.seedlings.entities.players.FlowerCharacter;
import com.sugarware.seedlings.entities.players.IceCharacter;
import com.sugarware.seedlings.entities.players.Player;
import com.sugarware.seedlings.entities.players.VanillaCharacter;
import com.sugarware.seedlings.entities.scenery.Hut;

public abstract class PlayGameState extends GameState {
	private ShaderProgram finalShader;
	FrameBuffer lightFbo;
	FrameBuffer screenFbo;
	
	
	public int score = 0;
	
	protected boolean dark;
	
	//values passed to the lighting shader
		public static final float ambientIntensity = .3f;
		public static final Vector3 ambientColor = new Vector3(0.3f, 0.3f, 0.7f);

	//used to make the light flicker
		public float zAngle;
		public static final float zSpeed = 15.0f;
		public static final float PI2 = 3.1415926535897932384626433832795f * 2.0f;

	//character change cooldown
		int chchcool;
	
	//if true show collision rectangles
	boolean debugCollisions;
	
	//shape renderer for showing collision rects
	ShapeRenderer s;
	
	//values for light flicker
	float maxrad= 81;
	boolean radin = false;
	float darkness = 0.2f;
	
	
	//Fun stuff
	boolean trail = false;
	int trailtimer = 20;
	Vector2 p1,p2,p3,p4;
	
	
	//indexes for map layers to be drawn behind or above entities
	int[] backlayer = new int[]{0,1};
	int[] frontlayer = new int[]{2,3};
	
	//The players and the entities
	public Player p;
	public ArrayList<ColEntity> entities;
	
	//map
	public TiledMap tilemap;
	OrthogonalTiledMapRenderer maprenderer;
	
	//pool to reuse fireballs
	FireballPool fbp;
	
	//background image
	TextureRegion bg;
	
	//frame buffer for water
	public FrameBuffer frameBuffer;
	private Matrix4 idt = new Matrix4();
	
	//for changing characters
	protected ScrollSelector ss;
	
	
	protected boolean inited;

	//textures for light shader
	Texture alphaMask;
	Texture light;
	Texture wlight;
	
	
	public PlayGameState( GameStateManager gsm,String map_path) {
		
		super(gsm);
		//initialize objects and whatnot
		inited = false;
		dark = false;
		alphaMask = new Texture("data/mask.png");
		alphaMask.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		tilemap = new TmxMapLoader().load(map_path);
		
		MapProperties prop = tilemap.getProperties();
		entities = new ArrayList<ColEntity>();
		w = prop.get("width",Integer.class) * prop.get("tilewidth", Integer.class);
		h = prop.get("height", Integer.class)* prop.get("tileheight", Integer.class);
		fbp = new FireballPool(this);
		bg = new TextureRegion(new Texture(Gdx.files.internal("bg2.jpg")));
		cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
		cam.update();
		
		light = new Texture(Gdx.files.internal("data/red.png"));
		wlight = new Texture(Gdx.files.internal("data/light.png"));
		
		chchcool = 40;
		s = new ShapeRenderer();
		lightFbo = new FrameBuffer(Format.RGBA8888, w, h, false);
		screenFbo = new FrameBuffer(Format.RGB565, w,h, false);
		maprenderer = new OrthogonalTiledMapRenderer(tilemap);
		ShaderProgram.pedantic = false;
		finalShader = new ShaderProgram(Gdx.files.internal("data/vertexShader.glsl").readString(),Gdx.files.internal("data/lightShader.glsl").readString());
		finalShader.begin();
		finalShader.setUniformf("resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		finalShader.setUniformi("u_lightmap", 1);
		finalShader.setUniformi("u_texture", 0);
		finalShader.setUniformf("ambientColor", ambientColor.x, ambientColor.y,
				ambientColor.z, ambientIntensity);
		finalShader.end();
		GameStateManager.rm.loadIcons();
	
	}

	//countdown to remove loading screen TODO remove this and make loading screen work properly
	int initcount;
	
	
	public boolean drawGUI = true;
	
	@Override
	protected void init() {
		score = 0;
		inited = false;
		initcount =154;
		
		//remove blocks that added themselves to the collision map
		MapObjects obs = tilemap.getLayers().get("Collisions").getObjects();
		for(int i = 0; i <  obs.getCount(); i++){
			if(obs.get(i).getName() != null){
				if(obs.get(i).getName().equals("block")){
					obs.remove(i);
					i--;
				}
			}
		}
		
		//reset the scroll selector
		ss.theta = 0;
		ss.sel = 0;
	}

	//loads entities and other map objects from the .tmx file to the platform and entity arrays
	protected void loadMapPlatforms(){
		MapObjects obs = tilemap.getLayers().get("Collisions").getObjects();
		for (int i = 0; i < obs.getCount(); i++) {
			if (obs.get(i) instanceof PolylineMapObject ) {
				if(obs.get(i).getName() != null){
					Polyline poly = ((PolylineMapObject) obs.get(i)).getPolyline();
					if(obs.get(i).getName().equals("plat")){
						VertPlatform v = new VertPlatform(this, poly.getX(), poly.getY(), (int) poly.getLength());
						p.add(v);
					}
					
				}
			}
		}
	 obs = tilemap.getLayers().get("Entities").getObjects();
		for (int i = 0; i < obs.getCount(); i++) {
			if (obs.get(i) instanceof PolylineMapObject ) {
				if(obs.get(i).getName() != null){
					Polyline poly = ((PolylineMapObject) obs.get(i)).getPolyline();
					if(obs.get(i).getName().equals("vine")){
						Rope r;
						if(obs.get(i).getProperties().get("opp") != null)  r = new Rope(this, poly.getX(), poly.getY(), (int) poly.getLength(),false);
						else  r = new Rope(this, poly.getX(), poly.getY(), (int) poly.getLength(),true);
						entities.add(r);
					}
					
				}
			}else if(obs.get(i) instanceof RectangleMapObject){
				if(obs.get(i).getName() != null){
					if(obs.get(i).getName().equals("robot")){
						Rectangle r = ((RectangleMapObject) obs.get(i)).getRectangle();
						entities.add(new Robot(this, r.x, r.y));
					}else if(obs.get(i).getName().equals("roboth")){
						Rectangle r = ((RectangleMapObject) obs.get(i)).getRectangle();
						entities.add(new Robot(this, r.x, r.y,false, true));
					}else if(obs.get(i).getName().equals("block")){
						Rectangle r = ((RectangleMapObject) obs.get(i)).getRectangle();
						entities.add(new Block(this,r.x,r.y,r.width, r.height));
					}
				}
			}
		}
		
	}

	
	
	@Override
	protected void unload() {
		maprenderer.dispose();
		tilemap.dispose();

	}
	
	public void update(){
	
		//Decrement Character Change Cooldown
		if(chchcool >=0)chchcool--;
	
		//For light flicker
		if(radin){
			maxrad+=0.1f;
			if(maxrad > 130)radin = false;
		}else{
			maxrad-=0.1f;
			if(maxrad <72)radin = true;
		}
		
		
		//Distort bg music if you are under water
		if(p.underWater){
			if(GameStateManager.rm.getMusic() != null)if(GameStateManager.rm.getMusic().getVolume() != 0.3f)GameStateManager.rm.getMusic().setVolume(0.3f);
		}else{
			if(GameStateManager.rm.getMusic() != null)if(GameStateManager.rm.getMusic().getVolume() != 1f)GameStateManager.rm.getMusic().setVolume(1f);
		}
		
		
		//make sure player is stil updated to get the camera setup even before the game is officially inited
		if(!inited)p.update();
		
		if(initcount < 0)inited = true;
		if(initcount >= 0)initcount--;
	
		if(!p.dead && p.transition == null && p.oldanim == null && inited){
		
		//TODO put this snippet into the fire character object
		if(p instanceof FireCharacter){
			FireCharacter fp = (FireCharacter) p;
			if(fp.shootball){
				Fireball fb = fbp.obtain();
				fb.init((int)p.x, (int)p.y + 4, p.facingRight);
				entities.add(fb);
				fp.shootball = false;
				
			}
		}
		
		//make entities interact with eachother
		for(ColEntity e : entities){e.update(); }
		p.update();
		p.collide(entities);
		for(ColEntity e : entities){if(e instanceof Block)((Block) e).collide(p);}
		
		//Will probably remove this with the addition of the scroll character selection rather than powerup character changing
		for(int i = 0; i < entities.size(); i++){
			ColEntity e = entities.get(i);
			if(e instanceof Powerup){
				boolean del= false;
				if(p.colRect.overlaps(((Powerup) e).colRect)){
					switch(((Powerup) e).type){
					case VANILLA:
						if(!(p instanceof VanillaCharacter)){
							p = new VanillaCharacter(this, p);
							del = true;
						}
						break;
					case FLOWER:
						if(!(p instanceof FlowerCharacter)){
						p = new FlowerCharacter(this, p);
						del = true;
						}
						break;
					case FIRE:
						if(!(p instanceof FireCharacter)){
						p = new FireCharacter(this, p);
						del = true;
						}
						break;
					default:
						break;
					}
					if(del){entities.remove(i);
					i--;}
				}
			}else{
				e.collide(entities);
				
			}
			
		}
		
		
		//get rid of entities that are no longer needed
		for(int i = 0; i < entities.size(); i++){
			ColEntity e = entities.get(i);
			if(e.killme){
				if(e instanceof Robot){
					entities.set(i, new Explosion(this, e.x, e.y));
					GameStateManager.rm.playSound(Resources.Sounds.GENERAL, Resources.Sounds.General.EXPL1, 0.12f);
				}else if(e instanceof Fireball && !((Fireball) e).hitenemy){
					entities.set(i, new SmallExplosion(this, e.x, e.y));
				}else{
					entities.remove(i);
					i--;
				}
			}
		}
		
		
		}
			
		
	
		
		alphaUpdate();
	}
	
	//manages character changing
	void alphaUpdate(){
				if(ss.cd == 0 && !ss.scrolling && ss.list.size() >0){
				System.out.println((ss.list.get(ss.sel) ==  GameStateManager.rm.icons.get(2)));
				if(ss.list.size() > 0){
				
				if(ss.list.get(ss.sel) ==  GameStateManager.rm.icons.get(0) && !(p instanceof VanillaCharacter)) p = new VanillaCharacter(this,p);
			
				if(ss.list.get(ss.sel) ==  GameStateManager.rm.icons.get(1) &&!(p instanceof FlowerCharacter)){p = new FlowerCharacter(this,p);}
	
				if(ss.list.get(ss.sel) ==  GameStateManager.rm.icons.get(2) &&!(p instanceof FireCharacter))p = new FireCharacter(this,p);
			
				if(ss.list.get(ss.sel) ==  GameStateManager.rm.icons.get(3) &&!(p instanceof IceCharacter))p = new IceCharacter(this,p);
			
				ss.cd = -1;
				}
			}	
			if(!p.dead)p.alphaUpdate();
			else{p.alpha -= 0.025;
			if(p.alpha <=0)gsm.currentState.init();}
			
	
			
		
	}
	
	protected void draw(SpriteBatch g){
			
		g.end();
		//initialize frame buffer to catch water images
		 if (frameBuffer == null){
		        try {
		        	
		            frameBuffer = new FrameBuffer(Format.RGB565, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		            
		        } catch (GdxRuntimeException e){ 
		        	 frameBuffer = new FrameBuffer(Format.RGB565, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		        }
		    }
		
		 if(inited){
		    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		    Gdx.gl.glClearColor(0, 0, 0, darkness);
		 
			//draw back layer to frame buffer
			  frameBuffer.begin();
			  g.begin();
			  g.draw(bg, 0, 0, w, h);
		    g.end();
		g.setProjectionMatrix(cam.combined);
		g.begin();
		//make sure bulldozer is drawn behind everything
		for(Entity e : entities)if(e instanceof Bulldozer)e.draw(g);
		g.end();
		
		//draw  map and entities
		maprenderer.setView(cam);
		maprenderer.render(backlayer);
		g.setProjectionMatrix(cam.combined);
		g.begin();
		for(Entity e : entities)if(e instanceof Powerup || e instanceof Rope || e instanceof Hut)e.draw(g);
		if(p.hitCooldown % 8 != 0)p.draw(g);
		for(Entity e : entities)if(!(e instanceof Water)&&!(e instanceof Bulldozer) && !(e instanceof Powerup) && !(e instanceof Rope) && !(e instanceof Hut)) e.draw(g);
		g.end();
		frameBuffer.end();
		
		
		//start lighting frame buffer and draw the captured image
		g.setProjectionMatrix(idt);
		screenFbo.begin();
	    g.begin();
	   g.draw(frameBuffer.getColorBufferTexture(), -1, 1, 2, -2); 
	    g.end();
	    
	    g.setProjectionMatrix(cam.combined);
	    g.begin();
	   
	    //draw the water and use the first frame buffer to generate the distortion behind the water
	    //See Water.draw()
		for(Entity e : entities)if(e instanceof Water)e.draw(g);

		g.end();
		
		maprenderer.setView(cam);
		maprenderer.render(frontlayer);
		
		
		screenFbo.end();
		
		
		

		
		if(dark){
		//draw the light to the FBO
				final float dt = Gdx.graphics.getRawDeltaTime();
				
				zAngle += dt * zSpeed;
				while(zAngle > PI2)
					zAngle -= PI2;
				lightFbo.begin();
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				g.setProjectionMatrix(cam.combined);
				g.setShader(null);
				
				Gdx.gl.glBlendFunc(GL20.GL_BLEND_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
				Gdx.gl.glEnable(GL20.GL_BLEND);
				//Gdx.gl.glDepthMask(false);
				g.setBlendFunction(GL20.GL_BLEND_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
				g.enableBlending();
				g.begin();
				
				float lightSize =  120f + 2f * (float)Math.sin(zAngle) + .2f*MathUtils.random();
				float tlightSize =  lightSize / 2;
					for(Entity ent : entities){
						if(ent instanceof Fireball)g.draw(light,ent.x - tlightSize / 3.85f,ent.y - tlightSize * 0.25f, tlightSize, tlightSize);
						if(ent instanceof Light)g.draw(wlight,ent.x - tlightSize / 3.85f,ent.y - tlightSize * 0.25f, tlightSize, tlightSize);
					}
				if (p instanceof FireCharacter){
					g.draw(light,p.x - lightSize / 2.5f,p.y - lightSize * 0.25f, lightSize, lightSize);
					
				}
				
				/* TODO 
				*Make fireball lights smaller
				liightSize = lightSize / 4;
				for(Entity e : entities){
					if(e instanceof Fireball)g.draw(light,e.x - lightSize / 2.5f,e.y - lightSize * 0.25f, lightSize, lightSize);
				}*/
				
				g.end();
				lightFbo.end();
		}
		
		//draw light mask
		idt = new Matrix4();
		g.setProjectionMatrix(idt);
		if(dark)
		g.setShader(finalShader);
		g.begin();
		if(dark){lightFbo.getColorBufferTexture().bind(1); //this is important! bind the FBO to the 2nd texture unit
		light.bind(0);
		}
		g.draw(screenFbo.getColorBufferTexture(), -1,1,2,-2);
		g.end();
		
		g.setShader(null);
		g.setProjectionMatrix(cam.combined);
		 
		
		//Draw the collision map for debugging
   if(debugCollisions){
    if(s == null) s = new ShapeRenderer();
		s.setProjectionMatrix(cam.combined);
		s.begin(ShapeType.Line);
		s.setColor(Color.RED);
		for(Platform p : p.plats){
			if(p instanceof VertPlatform){
				s.line(p.x, p.y, p.x + p.width, p.y);
			}
		}
	
		s.setColor(Color.GREEN);
		s.rect(p.mapTangle.x, p.mapTangle.y, p.mapTangle.width, p.mapTangle.height);
		s.setColor(Color.RED);
		s.rect(p.colRect.x, p.colRect.y, p.colRect.width, p.colRect.height);
		
		for(MapObject obj : tilemap.getLayers().get("Collisions").getObjects()){
			if(obj instanceof RectangleMapObject){
				if(obj.getName() != null)if(obj.getName().equals("block"))continue;
				s.setColor(Color.ORANGE);
				Rectangle r = ((RectangleMapObject) obj).getRectangle();
				s.rect(r.x, r.y, r.width, r.height);
			}
			
			if (obj instanceof PolylineMapObject ) {
				s.setColor(Color.MAGENTA);
				if(obj.getName() != null){
					if(obj.getName().equals("slope")){////////////////////////////
				Polyline poly = ((PolylineMapObject) obj).getPolyline();
				float cx1 = p.x + p.dx  + ((p.width - p.cwidth) / 4) - 2;
				float cx2 =p.x  + p.dx  + ((p.width - p.cwidth) / 4) + p.cwidth + 2;
				float cy = p.y + p.dy;
				 if(p1 == null)p1 = new Vector2();
				 if(p2 == null)p2 = new Vector2();
				 if(p3 == null)p3 = new Vector2();
				 if(p4 == null)p4 = new Vector2();
				 p1.set(poly.getVertices()[0] + poly.getX() -8 , poly.getVertices()[1] + poly.getY()- 4);
				 p2.set(poly.getVertices()[2] + poly.getX(), poly.getVertices()[3] + poly.getY());
				 p3.set(cx1, cy);
				 p4.set(cx2, cy);

				
				s.line(p1, p2);
				s.setColor(Color.NAVY);
				s.line(p3, p4);
				
				
					}
				}
			}
		}
		
		for(ColEntity e : entities){
			if(e instanceof Block){
				Block b = (Block) e;
				if(((Block) e).PUSHABLE_L){
					s.setColor(Color.GREEN);
				}else{
					s.setColor(Color.RED);
				}
				s.rect(b.x, b.y, b.cwidth / 2, b.cheight);
				if(((Block) e).PUSHABLE_R){
					s.setColor(Color.GREEN);
				}else{
					s.setColor(Color.RED);
				}
				s.rect(b.x + b.cwidth /2 , b.y, b.cwidth / 2, b.cheight);
			}
			if(e instanceof Water){
				s.setColor(Color.BLUE);
				s.rect(e.x, e.y, e.cwidth, e.cheight);
			}
			if(e instanceof Rope){
				Rope r = (Rope) e;
				for(int i = 0; i < r.vertices.length; i+=2){
					s.circle(r.vertices[i], r.vertices[i + 1], 2);
				}
			}else{
				s.setColor(Color.TEAL);
				s.rect(e.colRect.x,e.colRect.y,e.colRect.width,e.colRect.height);
			}
		}
		
	//	s.setColor(Color.BLUE);
		//s.rect(p.colRect.x, p.colRect.y, p.colRect.width, p.colRect.height);
		
		
		
				
				
		
		
		
		s.end();
		}
		g.setProjectionMatrix(cam.combined);
    
    
    g.begin();

    if(drawGUI){
    	ss.draw(g);
    	p.drawHealth(g);
    	//score TODO change to text with bitmap font
    	float sx=  cam.position.x + cam.viewportWidth / 2 - 30;
    	float sy =  cam.position.y + cam.viewportHeight / 2 - 30;
    
    	GameStateManager.rm.loadImages(Resources.Images.LEAF2);
    	for(int i = 0; i < score; i++){
    		g.draw(GameStateManager.rm.getImages(Resources.Images.LEAF2).get(0)[0], sx - 30 * i, sy + 30, 30, -30);
	
  }
    
    }
		 }else{
			 
			// g.draw(gsm.rm.loadbg,cam.position.x - cam.viewportWidth / 2, cam.position.y - cam.viewportHeight / 2, cam.viewportWidth, cam.viewportHeight);
		 }
	}
	
	
	protected void keyPressed(int k){
		
		
		if (k == Keys.I & !ss.scrolling && !p.ropeSwing ){
			 ss.touchDown(0, 0);
		}
		
		//toggles for debugging
		if(k == Keys.T && GdxGame.test)dark = !dark;
		if(k == Keys.EQUALS)debugCollisions = !debugCollisions;
	}

	
	
	
	

}
