package com.sugarware.seedlings;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ResourceManager {
	
	
	
	//Audio
	float volAdj;
	Music music;
	ArrayList<ArrayList<Sound>> soundpacks;
	//ArrayList<MediaPlayer> soundplayers;
	ArrayList<ArrayList<String>> sndPaths;
	ArrayList<String> sngPaths;
	public ArrayList<TextureRegion> icons;
	//Indicies for sound packages
		protected static final int GENERAL = 0;
		//Indicies for sounds in packages
			protected static final int WHOOSH1 = 0;
			protected static final int EXPL1 = 1;
			protected static final int CRICKETS = 2;
			protected static final int BULLDOZER_LOOP = 3;
			protected static final int TREE_FALL = 4;
			protected static final int CRUNCH = 5;
			protected static final int WATER = 6;
	//Indicies for songs
			protected static final int MEADOW = 0;
			protected static final int INTENSE =1;
			protected static final int BOSS1 = 2;
			protected static final int FUNKYFOREST = 3;
			protected static final int MEADOW2 =4;
			protected static final int PERIWINKLE = 5;
		
	//Images
	private ArrayList<ArrayList<TextureRegion[]>> images;
	private ArrayList<String> imgpaths;
	private ArrayList<int[]> frameCounts;
	private ArrayList<int[]> dimensions;
	
	//Indices for objects that need images
		protected static final int VANILLA_SEED = 0;
		protected static final int FLOWER_SEED = 1;
		protected static final int PLATFORM_1 = 2;
		protected static final int TESTSEED = 3;
		protected static final int ROBOT = 4;
		protected static final int FIRE_SEED = 5;
		protected static final int FIREBALL = 6;
		protected static final int EXPLOSION_1 = 7;
		protected static final int EXPLOSION_2 = 8;
		protected static final int FIRETRANSITION = 9;
		protected static final int ICE_SEED = 10;
		protected static final int ICE_PLAT = 11;
		protected static final int VANILLA_TRANSITION = 12;
		protected static final int BULLDOZER = 13;
		protected static final int HUT = 14;
		protected static final int VANILLA_UNHAPPY = 15;
		protected static final int FLOWER_UNHAPPY = 16;
		protected static final int ROCKBLOCK = 17;
		//loneguy reserves spot 18
		protected static final int LEAF2 = 19;
		
		
		
		
		//LONE stuff
		protected static final int LONEGUY = 18;
		
		
		
		
		public TextureRegion loadbg;
		
		
		/***Licenses
		 * CCby3.0 http://creativecommons.org/licenses/by/3.0/
		 * 
		 ***/
		
		
		//CCby3.0 surt for the Lone tileset
	public ResourceManager(){
    		System.out.println("Resource Manager Initialized");
    		
    		loadbg = new TextureRegion(new Texture(Gdx.files.internal("loadbg.jpg")));
    		
    		soundpacks = new ArrayList<ArrayList<Sound>>();
    		sndPaths = new ArrayList<ArrayList<String>>();
    		sngPaths = new ArrayList<String>();
    		//General Sound Pack
    			soundpacks.add(null);
    			ArrayList<String> sp = new ArrayList<String>();
    			sp.add("sounds/whoosh.wav");//Free
    			sp.add("sounds/explosion.wav");//Free
    			sp.add("sounds/crickets.mp3");//Free
    			sp.add("sounds/tractor.mp3");//Free
    			sp.add("sounds/tree_falling.wav"); //CCby3.0 Blender Foundation
    			sp.add("sounds/crunch.ogg");
    			
    			sndPaths.add(sp);
    			
    		
    		//Songs
    			sngPaths.add("songs/lost_in_meadows.mp3");//Unkown
    			sngPaths.add("songs/intense.mp3");//CCby3.0 HorrorPen
    			sngPaths.add("songs/lastmin.mp3");  //CCby3.0 HorrorPen
    			sngPaths.add("songs/funkymeadow.mp3");//CCby3.0 Music by Dan Knoflicek. 
    			sngPaths.add("songs/meadow2.ogg");//pub domain
    			sngPaths.add("songs/periwinkle.mp3");//CCby3.0 Axton Crolley
    			//Sprites
    		
			images = new ArrayList<ArrayList<TextureRegion[]>>();
    		imgpaths = new ArrayList<String>();
    		frameCounts = new ArrayList<int[]>();
    		dimensions = new ArrayList<int[]>();
		
    		
			//Vanilla Seed
            
              frameCounts.add(new int[]{1, 16, 5, 1,2});
               dimensions.add(new int[]{64, 64});
            	 imgpaths.add("sprites/vanilla_seed.png");
            	 images.add(null);
             
           
           //Flower Seed
            	 frameCounts.add(new int[]{1, 16, 5, 1,4});
                 dimensions.add(new int[]{64, 64});
              	 imgpaths.add("sprites/flower_seed.png");
              	 images.add(null);
            	 
	 
            //Platform 1
            	 frameCounts.add(new int[]{1});
            	 imgpaths.add("sprites/platform.png");
            	 dimensions.add(new int[]{48,24});
            	 images.add(null);
            	 
            //Test Powerup
            	 frameCounts.add(new int[]{1});
            	 imgpaths.add("testseed.png");
            	 dimensions.add(new int[]{64,64});
            	 images.add(null);
            //Robot
            	 frameCounts.add(new int[]{1,16,1});
            	 imgpaths.add("sprites/robot.png");
            	 dimensions.add(new int[]{64,64});
            	 images.add(null);
            	 
            //Fire Seedling
            	 frameCounts.add(new int[]{10, 16, 4, 10, 10, 10, 10, 16});
            	 imgpaths.add("sprites/fire_seed.png");
            	 dimensions.add(new int[]{64,64});
            	 images.add(null);
            //Fireball
            	 frameCounts.add(new int[]{10});
            	 imgpaths.add("sprites/fireball.png");
            	 dimensions.add(new int[]{32,32});
            	 images.add(null);
            //Explosion Big
            	 frameCounts.add(new int[]{7});
            	 imgpaths.add("sprites/exp1.png");
            	 dimensions.add(new int[]{64,64});
            	 images.add(null);
            //Explosion Small
            	 frameCounts.add(new int[]{7});
            	 imgpaths.add("sprites/exp2.png");
            	 dimensions.add(new int[]{32,32});
            	 images.add(null);
            //Fire Transition
            	 frameCounts.add(new int[]{38});
            	 imgpaths.add("sprites/fire_trans.png");
            	 dimensions.add(new int[]{128,128});
            	 images.add(null);
         //Ice Seed
            	 frameCounts.add(new int[]{1, 16, 5, 1});
                 dimensions.add(new int[]{64, 64});
              	 imgpaths.add("sprites/snow_seed.png");
              	 images.add(null);
        //Platform 2
            	 frameCounts.add(new int[]{1});
            	 imgpaths.add("sprites/ice_plat.png");
            	 dimensions.add(new int[]{192,96});
            	 images.add(null);     	
            	 
       //Fire Transition
            	 frameCounts.add(new int[]{64});
            	 imgpaths.add("sprites/vanilla_trans.png");
            	 dimensions.add(new int[]{128,128});
            	 images.add(null);
        //Bulldozer
            	 frameCounts.add(new int[]{4});
            	 imgpaths.add("sprites/bulldozer.png");
            	 dimensions.add(new int[]{256,256});
            	 images.add(null);
        //HUT
            	 frameCounts.add(new int[]{6});
             	 imgpaths.add("sprites/hut.png");
             	 dimensions.add(new int[]{256,256});
             	 images.add(null);
             	 
             	//Vanilla Seed
                 
                 frameCounts.add(new int[]{1, 16, 5, 1,2});
                  dimensions.add(new int[]{64, 64});
               	 imgpaths.add("sprites/vanilla_un.png");
               	 images.add(null);
               	 //Flower
                 
               	 frameCounts.add(new int[]{1, 16, 5, 1,4});
                 dimensions.add(new int[]{64, 64});
              	 imgpaths.add("sprites/flower_un.png");
              	 images.add(null);
              	 //RockBlock
                 
               	 frameCounts.add(new int[]{1});
                 dimensions.add(new int[]{85, 85});
              	 imgpaths.add("sprites/rockBlock.png");
              	 images.add(null);
              	 
              	 //Loneguy

                 
                 frameCounts.add(new int[]{1, 16, 5, 1,2});
                  dimensions.add(new int[]{64, 64});
               	 imgpaths.add("lsprites/sillou.png");
               	 images.add(null);
 	 //Loneguy

                 
                 frameCounts.add(new int[]{1});
                  dimensions.add(new int[]{32, 32});
               	 imgpaths.add("sprites/leaf2.png");
               	 images.add(null);
	}
	
	
	
	
	
	public void loadSoundPack(int index){
		if(soundpacks.get(index) == null){
		
		ArrayList<Sound> pack = new ArrayList<Sound>();
		for(int i = 0; i < sndPaths.get(index).size(); i++){
			Sound snd = Gdx.audio.newSound(Gdx.files.internal(sndPaths.get(index).get(i)));
			System.out.println(sndPaths.get(index).get(i));
			
			pack.add(snd);
		}
		soundpacks.set(index, pack);
		}
	}
	
	public void unloadSoundPack(int index){
		try{soundpacks.set(index, null);}catch(Exception e){}
	}
	
	public void playSound(int packindex, int soundindex){
		soundpacks.get(packindex).get(soundindex).play();
	}
	public void playSound(int packindex, int soundindex, float volume){
		soundpacks.get(packindex).get(soundindex).play(volume);
	}
	public Sound getSound(int packindex, int soundindex){
		return soundpacks.get(packindex).get(soundindex);
	}
	
	
	
	public void playSong(int index){
		stopSong();
		music = Gdx.audio.newMusic(Gdx.files.internal(sngPaths.get(index)));
		music.setLooping(true);
		//music.setVolume(1);
		music.play();
	}
	
	
	
	public void stopSong(){
		if(music != null){ 
			music.stop(); music.dispose();
		}
	}
	
	public Music getMusic(){
		return music;
	}
	
	
	public void loadLongImage(int index){
		try{
			if(images.get(index) != null)return;
			System.out.println("Loading " + imgpaths.get(index));
		}catch(Exception e){
			return;
		}
		
		
		int imwidth = dimensions.get(index)[0];
		int imheight = dimensions.get(index)[1];
		System.out.println(imwidth + "x" + imheight);
		int numFrames = frameCounts.get(index)[0];
		ArrayList<TextureRegion[]> sprites = new ArrayList<TextureRegion[]>();
		try {
			Texture spritesheet = new Texture(Gdx.files.internal(imgpaths.get(index)));
			TextureRegion[][] sheet = TextureRegion.split(spritesheet,imwidth,imheight);
			TextureRegion[] sprite = new TextureRegion[numFrames];
			int w = spritesheet.getWidth() / imwidth;
			int h = spritesheet.getHeight() / imheight;
			System.out.println(w + "<> " + h);
			int c = 0;
			
				for(int j = 0; j < h; j++){
					for(int i = 0; i < w; i++){
					if(c >= numFrames)break;
					sheet[j][i].flip(false, true);
					sprite[c] = sheet[j][i];
					c++;
					
				}
				if(c >= numFrames)break;
			}
			sprites.add(sprite);
			
		
			images.set(index, sprites);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	
	public void loadImages(int index){
		
		
		try{
			if(images.get(index) != null)return;
			System.out.println("Loading " + imgpaths.get(index));
		}catch(NullPointerException e){
			return;
		}
		
		int imwidth = dimensions.get(index)[0];
		int imheight = dimensions.get(index)[1];
		System.out.println(imwidth + "x" + imheight);
		int[] numFrames = frameCounts.get(index);
		
		int numSprites = numFrames.length;
		ArrayList<TextureRegion[]> sprites = new ArrayList<TextureRegion[]>();
		
		
		try {
			Texture spritesheet = new Texture(Gdx.files.internal(imgpaths.get(index)));
			TextureRegion[][] sheet = TextureRegion.split(spritesheet,imwidth,imheight);
			
			for(int i = 0; i < numSprites; i++){
				TextureRegion[] sprite = new TextureRegion[frameCounts.get(index)[i]];
				for(int j = 0; j < frameCounts.get(index)[i]; j++){
					sheet[i][j].flip(false, true);
					sprite[j] = sheet[i][j];
				}
				sprites.add(sprite);
			}
			

			
			
			images.set(index, sprites);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	
	public void unloadImages(int index){
		images.set(index, null);
	}
	
	public ArrayList<TextureRegion[]> getImages(int index){
		return images.get(index);
	}

	/*
	 * SIMPLE IMAGE LOADING
	 */
	
	//WATER
		public TextureRegion water_top;
		public TextureRegion water;
	public void loadWaterImages(){
		if(water_top == null){water_top = new TextureRegion(new Texture(Gdx.files.internal("tilemaps/tilesets/water_top.png")));
		water = new TextureRegion(new Texture(Gdx.files.internal("tilemaps/tilesets/water.png")));
		System.out.println("Loaded tilemaps/tilesets/water.png");
		System.out.println("Loaded tilemaps/tilesets/water_top.png");
		
		}
	}
	
	//GUI IMAGES
		TextureRegion btnUp;
		TextureRegion btnAction;
		TextureRegion btnPause;
		TextureRegion btnLight;
		public TextureRegion[] healthIcon;
	public void loadGuiImages(){
		if(btnUp == null){btnUp = new TextureRegion(new Texture(Gdx.files.internal("gui/up.png")));
		btnAction = new TextureRegion(new Texture(Gdx.files.internal("gui/action.png")));
		btnPause = new TextureRegion(new Texture(Gdx.files.internal("gui/pause.png")));
		btnLight = new TextureRegion(new Texture(Gdx.files.internal("gui/lit.png")));
		healthIcon = new TextureRegion[]{new TextureRegion(new Texture(Gdx.files.internal("gui/health.png"))).split(32,32)[0][0],
				new TextureRegion(new Texture(Gdx.files.internal("gui/health.png"))).split(32, 32)[0][1]};
		System.out.println("Loaded gui/up.png");
		System.out.println("gui/action.png");
		System.out.println("Loaded gui/pause.png");
		System.out.println("Loaded gui/lit.png");
		}
		
	}
	
	public void loadAltHearts(){
		healthIcon = new TextureRegion[]{new TextureRegion(new Texture(Gdx.files.internal("lsprites/h2.png"))).split(170, 170)[0][0],
		new TextureRegion(new Texture(Gdx.files.internal("lsprites/h2.png"))).split(170, 170)[0][1]};
}
	
	public void loadIcons(){
		if(icons == null)icons = new ArrayList<TextureRegion>();
		else icons.clear();
		icons.add(new TextureRegion(new Texture(Gdx.files.internal("icons/v.png"))));
		icons.add(new TextureRegion(new Texture(Gdx.files.internal("icons/f.png"))));
		icons.add(new TextureRegion(new Texture(Gdx.files.internal("icons/fi.png"))));
		icons.add(new TextureRegion(new Texture(Gdx.files.internal("icons/i.png"))));
	}





	public ArrayList<Sound> getSoundPack(int index) {
		return soundpacks.get(index);
	}


	

	
}
