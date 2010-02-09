package com.malgon.applecrush;

import java.util.Random;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;

import com.stickycoding.Rokon.Font;
import com.stickycoding.Rokon.Hotspot;
import com.stickycoding.Rokon.RokonActivity;
import com.stickycoding.Rokon.Sprite;
import com.stickycoding.Rokon.Text;
import com.stickycoding.Rokon.Texture;
import com.stickycoding.Rokon.TextureAtlas;
import com.stickycoding.Rokon.TextureManager;
import com.stickycoding.Rokon.Backgrounds.FixedBackground;

public class Game2 extends RokonActivity {
	public TextureAtlas atlas;
    public Texture backgroundTexture;
    public Texture appleTexture;
    public Texture appleCrunchTexture;
    public Texture androidTexture;
    public Texture androidCrunchTexture;
    
    public FixedBackground background;
    
    public Sprite appleSprite;
    public Hotspot appleHotspot;
    
    public Sprite androidSprite;
    public Hotspot androidHotspot;
    
    public int score=0,appleTouch=0;
    public int time=0;
    
    public AlertDialog alertDialog;
   
    public boolean clicked;
    
    public Font font;
    
    public Text textScore;
    public Text textTime;
    
    private Handler timerTime=new Handler();
    private Runnable checkTime=new Runnable()
    {
		@Override
		public void run() {			
				time++;
				textTime.setText(String.valueOf(time));
				timerTime.postDelayed(checkTime, 1000);
		}
    };
    
    private Handler timerAndroid=new Handler();
    private Runnable checkAndroid=new Runnable()
    {
		@Override
		public void run() {			
				moveSprites();
				timerAndroid.postDelayed(checkAndroid, 600);
		}
    };
    
    private Handler timer=new Handler();
    private Runnable endOfGame2=new Runnable()
    {
		@Override
		public void run() {
			textTime.setText(String.valueOf(time));
			
			appleSprite.setVisible(false);
	    	//androidSprite.setVisible(false);
			
			String msg="Game finished, you've crush apple "+appleTouch+" time ! ";

			msg+= "Your final score is "+score;
			
			alertDialog.setMessage(msg);
			
			alertDialog.show();
			    			
			rokon.freeze();
			rokon.vibrate(1000);
		}
    };
    
public void moveSprites()
{
	appleSprite.setTexture(appleTexture);
	androidSprite.setTexture(androidTexture);
	
	Random randomX=new Random();
    Random randomY=new Random();
	Random random=new Random();
    
    if(random.nextInt(3)==0)
    {
    	appleSprite.setVisible(false);
    	androidSprite.setVisible(true);
    	
    	androidSprite.setXY(randomX.nextInt(380), randomY.nextInt(240));
    	appleSprite.setXY(800,800);
    }
    else
    {
    	appleSprite.setVisible(true);
    	androidSprite.setVisible(false);
    	
    	appleSprite.setXY(randomX.nextInt(380), randomY.nextInt(240));
    	androidSprite.setXY(800,800);
    }
}
    
public void onCreate() {
    createEngine("graphics/splash.png",430, 320, true);
    
    alertDialog = new AlertDialog.Builder(Game2.this).create();
	alertDialog.setTitle("Game1 Finished");
	alertDialog.setButton("Play again", new DialogInterface.OnClickListener() {
	      public void onClick(DialogInterface dialog, int which) {
	    	score=0;
	    	appleTouch=0;
	    	time=0;
	        rokon.unfreeze();
	      }
	}); 
	alertDialog.setButton2("Quit", new DialogInterface.OnClickListener() {
	      public void onClick(DialogInterface dialog, int which) {
	    	  System.exit(0);
	    	  Game2.this.finish();
	      }
	}); 
}

    @Override
    public void onLoad() {
            atlas = new TextureAtlas(512, 1024);
            atlas.insert(appleTexture = new Texture(R.drawable.apple));
            atlas.insert(appleCrunchTexture = new Texture(R.drawable.crunch));
            atlas.insert(androidTexture = new Texture(R.drawable.android));
            atlas.insert(androidCrunchTexture = new Texture(R.drawable.android_crunch));
            atlas.insert(backgroundTexture = new Texture(R.drawable.background));
            atlas.insert(font = new Font("fonts/256BYTES.TTF"));
            TextureManager.load(atlas);
            
            appleSprite = new Sprite(180,80,appleTexture);
            appleHotspot = new Hotspot(appleSprite);          
            
            androidSprite = new Sprite(68,80,androidTexture);
            androidHotspot = new Hotspot(androidSprite);
            
            background = new FixedBackground(backgroundTexture);
    }

    @Override
    public void onLoadComplete() {
            rokon.setBackground(background);
            rokon.addSprite(appleSprite);
            rokon.addHotspot(appleHotspot);
            
            rokon.addSprite(androidSprite);
            rokon.addHotspot(androidHotspot);
            
            timerTime.postDelayed(checkTime, 1000);
            
            timerAndroid.postDelayed(checkAndroid, 0);
            
            androidSprite.setVisible(false);
            
            textScore = new Text(String.valueOf(score), font, 20, 275, 32);
            textTime = new Text(String.valueOf(time), font, 10, 10, 32);
    		rokon.addText(textScore);
    		rokon.addText(textTime);
    }

    @Override
    public void onGameLoop() {

    }     
    
    public void onHotspotTouchDown(Hotspot hotspot) { 
    		if(hotspot.equals(appleHotspot))
    		{
	            appleSprite.setTexture(appleCrunchTexture);
    			rokon.vibrate(100);
	            score++;
	            appleTouch++;
    		}
    		else if(hotspot.equals(androidHotspot))
    		{
    			androidSprite.setTexture(androidCrunchTexture);
    			
    			timer.postDelayed(endOfGame2, 0);
    		}
    		
    		textScore.setText(String.valueOf(score));
    		
    		clicked=true;
    		//moveSprites();
       }
}