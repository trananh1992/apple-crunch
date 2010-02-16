/*
 * 
	This file is part of Apple Crunch.

    Apple Crunch is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Apple Crunch is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Apple Crunch.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.malgon.applecrunch;

import java.io.IOException;
import java.util.Random;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import com.stickycoding.Rokon.Font;
import com.stickycoding.Rokon.Hotspot;
import com.stickycoding.Rokon.RokonActivity;
import com.stickycoding.Rokon.Sprite;
import com.stickycoding.Rokon.Text;
import com.stickycoding.Rokon.Texture;
import com.stickycoding.Rokon.TextureAtlas;
import com.stickycoding.Rokon.TextureManager;
import com.stickycoding.Rokon.Backgrounds.FixedBackground;

public class Game1 extends RokonActivity {
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
    
    public int score=0,androidTouch=0,appleTouch=0;
    public int timeToPlay=60000;
    public int time=timeToPlay/1000;
    
    public AlertDialog alertDialog;
    public AlertDialog scoreDialog;
    public AlertDialog goalDialog;
    
    public boolean clicked;
    
    public Font font;
    
    public Text textScore;
    public Text textTime;
    
    public String url="http://malgonstudio.sfhost.net/apple-crunch/add_score.php";
    
    
    private Handler timerGoal=new Handler();
    private Runnable showGoal=new Runnable()
    {
    	@Override
		public void run() {	
    		goalDialog.show();
    	}
    };
    
    
    private Handler timerTime=new Handler();
    private Runnable checkTime=new Runnable()
    {
		@Override
		public void run() {			
				time--;
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
    private Runnable endOfGame1=new Runnable()
    {
		@Override
		public void run() {		
			time--;
			textTime.setText(String.valueOf(time));
			
			appleSprite.setVisible(false);
	    	androidSprite.setVisible(false);
			
			rokon.freeze();
			
			SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(Game1.this);
			if(pref.getBoolean("vibrate", true))
				rokon.vibrate(1000);
			
			scoreDialog.setMessage("Do you want to save score "+score+" ? (you can see high-scores on http://pixellostudio.sfhost.net/apple-crunch/score)");
			scoreDialog.show();
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
	DisplayMetrics dm = new DisplayMetrics(); 
	getWindowManager().getDefaultDisplay().getMetrics(dm); 
	
    createEngine("graphics/splash.png",dm.widthPixels, dm.heightPixels, true);
    
    alertDialog = new AlertDialog.Builder(Game1.this).create();
	alertDialog.setTitle("Game1 Finished");
	alertDialog.setButton("Play again", new DialogInterface.OnClickListener() {
	      public void onClick(DialogInterface dialog, int which) {
	    	score=0;
	    	appleTouch=0;
	    	androidTouch=0;
	    	time=timeToPlay/1000;
	        rokon.unfreeze();
	        
	        timer.postDelayed(endOfGame1, timeToPlay);
	      }
	}); 
	alertDialog.setButton2("Quit", new DialogInterface.OnClickListener() {
	      public void onClick(DialogInterface dialog, int which) {
	    	  System.exit(0);
	    	  Game1.this.finish();
	      }
	}); 
	
	
	scoreDialog = new AlertDialog.Builder(Game1.this).create();
	scoreDialog.setTitle("Send score ?");
	scoreDialog.setButton("Yes", new DialogInterface.OnClickListener() {
	      public void onClick(DialogInterface dialog, int which) {
	    	  SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(Game1.this);
				
				HttpClient httpclient = new DefaultHttpClient();
	            HttpGet get = new HttpGet(url+"?key=o2BY3XUF0AgytDdaLmugnXiFUfRRQF&username="+pref.getString("username","Player Default").replace(" ","+")+"&score="+score+"&type=1");
	            try {
					httpclient.execute(get);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				String msg="Game1 finished, you've crush apple "+appleTouch+" time ! ";
				
				if(androidTouch>0)
				{
					msg += "But you killed Android "+androidTouch+" time :( ";
					msg+= "Next time don't crush Android, because it will eat apples :D ";
				}
				else if(appleTouch==0)
				{
					msg+="Don't be afraid of apple ;) ";
				}
				else
					msg+="You are now an Android lover, and an apple killer :D ";
				
				msg+= "Your final score is "+score;
				
				alertDialog.setMessage(msg);
				
				alertDialog.show();
	      }
	}); 
	scoreDialog.setButton2("No", new DialogInterface.OnClickListener() {
	      public void onClick(DialogInterface dialog, int which) {
	    	  String msg="Game1 finished, you've crush apple "+appleTouch+" time ! ";
				
				if(androidTouch>0)
				{
					msg += "But you killed Android "+androidTouch+" time :( ";
					msg+= "Next time don't crush Android, because it will eat apples :D ";
				}
				else if(appleTouch==0)
				{
					msg+="Don't be afraid of apple ;) ";
				}
				else
					msg+="You are now an Android lover, and an apple killer :D ";
				
				msg+= "Your final score is "+score;
				
				alertDialog.setMessage(msg);
				
				alertDialog.show();
	      }
	}); 
	
	goalDialog = new AlertDialog.Builder(Game1.this).create();
	goalDialog.setTitle("Goal");
	goalDialog.setMessage("The goal of this game, is to crush apple sprites, without crushing Android sprites !");
	goalDialog.setButton("Ok", new DialogInterface.OnClickListener() {
	      public void onClick(DialogInterface dialog, int which) {
	    	    timer.postDelayed(endOfGame1, timeToPlay);
	            timerTime.postDelayed(checkTime, 1000);
	            timerAndroid.postDelayed(checkAndroid, 0);
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
            
            androidSprite.setVisible(false);
            
            textScore = new Text(String.valueOf(score), font, 20, 275, 32);
            textTime = new Text(String.valueOf(time), font, 10, 10, 32);
    		rokon.addText(textScore);
    		rokon.addText(textTime);
    		
    		timerGoal.postDelayed(showGoal, 0);
    }

    @Override
    public void onGameLoop() {

    }     
    
    /*public void onHotspotTouchDown(Hotspot hotspot) { 
    	SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(Game1.this);
    		if(hotspot.equals(appleHotspot))
    		{
	            appleSprite.setTexture(appleCrunchTexture);
	            
				if(pref.getBoolean("vibrate", true))
					rokon.vibrate(100);
	            score++;
	            appleTouch++;
    		}
    		else if(hotspot.equals(androidHotspot))
    		{
    			androidSprite.setTexture(androidCrunchTexture);
    			
    			if(pref.getBoolean("vibrate", true))
    				rokon.vibrate(500);
    			
    			score-=5;
	            androidTouch++;
    		}
    		
    		textScore.setText(String.valueOf(score));
    		
    		clicked=true;
    		//moveSprites();
       }*/
    
    @Override
    public void onTouchDown(int x, int y, boolean hotspot)
    {
    	SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(Game1.this);
    	if(x>appleSprite.getX()-15 && x<appleSprite.getX()+appleSprite.getWidth()+15 && y<appleSprite.getY()+appleSprite.getHeight()+15 && y>appleSprite.getY()-15)
    	{
    		appleSprite.setTexture(appleCrunchTexture);
            
			if(pref.getBoolean("vibrate", true))
				rokon.vibrate(100);
            score++;
            appleTouch++;
    	}
    	
    	else if(x>androidSprite.getX()-15 && x<androidSprite.getX()+androidSprite.getWidth()+15 && y<androidSprite.getY()+androidSprite.getHeight()+15 && y>androidSprite.getY()-15)
    	{
            androidSprite.setTexture(androidCrunchTexture);
			
			if(pref.getBoolean("vibrate", true))
				rokon.vibrate(500);
			
			score-=5;
            androidTouch++;
    	}
    	
    	textScore.setText(String.valueOf(score));
    }
}