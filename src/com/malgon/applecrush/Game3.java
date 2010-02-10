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

package com.malgon.applecrush;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import android.view.KeyEvent;

import com.stickycoding.Rokon.Debug;
import com.stickycoding.Rokon.Font;
import com.stickycoding.Rokon.Hotspot;
import com.stickycoding.Rokon.RokonActivity;
import com.stickycoding.Rokon.Sprite;
import com.stickycoding.Rokon.Text;
import com.stickycoding.Rokon.Texture;
import com.stickycoding.Rokon.TextureAtlas;
import com.stickycoding.Rokon.TextureManager;
import com.stickycoding.Rokon.Backgrounds.FixedBackground;
import com.stickycoding.Rokon.Handlers.CollisionHandler;

public class Game3 extends RokonActivity {
	public TextureAtlas atlas;
    public Texture backgroundTexture;
    public Texture appleTexture;
    public Texture appleCrunchTexture;
    public Texture androidTexture;
    public Texture androidCrunchTexture;
    
    public Texture laserTexture;
    public Texture canonTexture;
    
    public FixedBackground background;
    
    public List<Sprite> appleSpriteList=new ArrayList<Sprite>();
    public List<Hotspot> appleHotspotList=new ArrayList<Hotspot>();
    
    public List<Sprite> androidSpriteList=new ArrayList<Sprite>();
    public List<Hotspot> androidHotspotList=new ArrayList<Hotspot>();
    
    public List<Sprite> laserSpriteList=new ArrayList<Sprite>();
    public List<Hotspot> laserHotspotList=new ArrayList<Hotspot>();
    
    //public Sprite laserSprite;
    //public Hotspot laserHotspot;
    
    public Sprite canonSprite;
    public Hotspot canonHotspot;
    
    public int score=0,androidTouch=0,appleTouch=0;
    public int timeToPlay=60000;
    public int time=timeToPlay/1000;
    
    public AlertDialog alertDialog;
    public AlertDialog scoreDialog;
    public AlertDialog goalDialog;
   
    public boolean clicked;
    
    public boolean canonTouched;
    
    public Font font;
    
    public Text textScore;
    public Text textTime;
    
    
    public boolean loaded=false;
    
  //les variables de déplacement
    public int moveCanonX=20;
    public int laserVelocity=500;
    
    
    public int screenHeight=430;
    
    
    public String url="http://malgonstudio.sfhost.net/apple-crunch/add_score.php";
    
    
    public CollisionHandler collisionHandler=new CollisionHandler()
    {
    	public void collision(Sprite source, Sprite target)
    	{
    		if((source.getTexture().equals(androidTexture) && target.getTexture().equals(canonTexture)))
    		{
    			source.setVisible(false);
    			target.removeCollisionSprite(source);
    			source.removeCollisionSprite(target);
    			
    			Hotspot hot1;
    			hot1=androidHotspotList.get(androidSpriteList.indexOf(source));
    			
    			androidSpriteList.remove(source);
    			
    			androidHotspotList.remove(hot1);
    			
    			rokon.removeSprite(source);
    			
    			source.markForRemoval();
    			
    			score+=5;
    			
    			
    			Debug.print("COLLISION Android Canon !" + String.valueOf(score));
    		}
    		else if((source.getTexture().equals(canonTexture) && target.getTexture().equals(androidTexture)))
    		{
    			target.setVisible(false);
    			target.removeCollisionSprite(target);
    			source.removeCollisionSprite(target);
    			
    			Hotspot hot1;
    			hot1=androidHotspotList.get(androidSpriteList.indexOf(target));
    			
    			androidSpriteList.remove(target);
    			
    			androidHotspotList.remove(hot1);
    			
    			rokon.removeSprite(target);
    			
    			target.markForRemoval();
    			
    			score+=5;
    			
    			Debug.print("COLLISION Android Canon !" + String.valueOf(score));
    		}
    		else if((source.getTexture().equals(appleTexture) && target.getTexture().equals(canonTexture)))
    		{
    			source.setVisible(false);
    			target.removeCollisionSprite(source);
    			source.removeCollisionSprite(target);
    			
    			Hotspot hot1;
    			hot1=appleHotspotList.get(appleSpriteList.indexOf(source));
    			
    			appleSpriteList.remove(source);
    			
    			appleHotspotList.remove(hot1);
    			
    			rokon.removeSprite(source);
    			
    			source.markForRemoval();
    			
    			score-=5;
    			
    			
    			Debug.print("COLLISION Apple Canon !" + String.valueOf(score));
    		}
    		else if((source.getTexture().equals(canonTexture) && target.getTexture().equals(appleTexture)))
    		{
    			target.setVisible(false);
    			target.removeCollisionSprite(target);
    			source.removeCollisionSprite(target);
    			
    			Hotspot hot1;
    			hot1=appleHotspotList.get(appleSpriteList.indexOf(target));
    			
    			appleSpriteList.remove(target);
    			
    			appleHotspotList.remove(hot1);
    			
    			rokon.removeSprite(target);
    			
    			target.markForRemoval();
    			
    			score-=5;
    			
    			Debug.print("COLLISION Apple Canon !" + String.valueOf(score));
    		}
    		else if((source.getTexture().equals(laserTexture) && target.getTexture().equals(appleTexture)))
    		{
    			source.setVisible(false);
    			source.removeCollisionSprite(target);
    			
    			target.setVisible(false);
    			
    			Hotspot hot1,hot2;
    			    			
    			hot1=appleHotspotList.get(appleSpriteList.indexOf(target));
    			hot2=laserHotspotList.get(laserSpriteList.indexOf(source));
    			
    			rokon.removeHotspot(hot1);
    			rokon.removeHotspot(hot2);
    			
    			appleSpriteList.remove(target);
    			laserSpriteList.remove(source);
    			
    			appleHotspotList.remove(hot1);
    			laserHotspotList.remove(hot2);
    			
    			rokon.removeSprite(target);
    			rokon.removeSprite(source);
    			
    			target.markForRemoval();
    			source.markForRemoval();
    			
    			Debug.print("COLLISION !");
    			
    			appleTouch++;
    			score+=2;
    		}
    		else if(source.getTexture().equals(appleTexture) && target.getTexture().equals(laserTexture))
    		{
    			source.setVisible(false);
    			source.removeCollisionSprite(target);
    			
    			target.setVisible(false);
    			
    			Hotspot hot1,hot2;
    			
    			Debug.print("test : "+String.valueOf(appleSpriteList.indexOf(target)));
    			
    			hot1=laserHotspotList.get(laserSpriteList.indexOf(target));
    			hot2=appleHotspotList.get(appleSpriteList.indexOf(source));
    			
    			rokon.removeHotspot(hot1);
    			rokon.removeHotspot(hot2);
    			
    			laserSpriteList.remove(target);
    			appleSpriteList.remove(source);
    			
    			laserHotspotList.remove(hot1);
    			appleHotspotList.remove(hot2);
    			
    			rokon.removeSprite(target);
    			rokon.removeSprite(source);
    			
    			target.markForRemoval();
    			source.markForRemoval();
    			
    			
    			
    			Debug.print("COLLISION !");
    			
    			appleTouch++;
    			score+=2;
    		}
    		else if(source.getTexture().equals(laserTexture) && target.getTexture().equals(androidTexture))
    		{
    			source.setVisible(false);
    			source.removeCollisionSprite(target);
    			
    			target.setVisible(false);
    			
    			Hotspot hot1,hot2;
    			
    			Debug.print("test : "+String.valueOf(appleSpriteList.indexOf(target)));
    			
    			hot1=androidHotspotList.get(androidSpriteList.indexOf(target));
    			hot2=laserHotspotList.get(laserSpriteList.indexOf(source));
    			
    			rokon.removeHotspot(hot1);
    			rokon.removeHotspot(hot2);
    			
    			androidSpriteList.remove(target);
    			laserSpriteList.remove(source);
    			
    			androidHotspotList.remove(hot1);
    			laserHotspotList.remove(hot2);
    			
    			rokon.removeSprite(target);
    			rokon.removeSprite(source);
    			
    			target.markForRemoval();
    			source.markForRemoval();
    			    			
    			Debug.print("COLLISION Android !");
    			
    			androidTouch++;
    			score-=5;
    		}
    		else if(source.getTexture().equals(androidTexture) && target.getTexture().equals(laserTexture))
    		{
    			source.setVisible(false);
    			source.removeCollisionSprite(target);
    			
    			target.setVisible(false);
    			
    			Hotspot hot1,hot2;
    			
    			Debug.print("test : "+String.valueOf(appleSpriteList.indexOf(target)));
    			
    			hot1=laserHotspotList.get(laserSpriteList.indexOf(target));
    			hot2=androidHotspotList.get(androidSpriteList.indexOf(source));
    			
    			rokon.removeHotspot(hot1);
    			rokon.removeHotspot(hot2);
    			
    			laserSpriteList.remove(target);
    			androidSpriteList.remove(source);
    			
    			androidHotspotList.remove(hot1);
    			laserHotspotList.remove(hot2);
    			
    			rokon.removeSprite(target);
    			rokon.removeSprite(source);
    			
    			target.markForRemoval();
    			source.markForRemoval();
    			    			
    			Debug.print("COLLISION Android !");
    			
    			androidTouch++;
    			score-=5;
    		}
    		
    		textScore.setText(String.valueOf(score));
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
				timerAndroid.postDelayed(checkAndroid, 2500);
		}
    };
    
    private Handler timerGoal=new Handler();
    private Runnable showGoal=new Runnable()
    {
    	@Override
		public void run() {	
    		goalDialog.show();
    	}
    };
    
    private Handler timer=new Handler();
    private Runnable endOfGame1=new Runnable()
    {
		@Override
		public void run() {		
			time--;
			textTime.setText(String.valueOf(time));
			
			/*appleSprite.setVisible(false);
	    	androidSprite.setVisible(false);*/

			rokon.freeze();
			
			SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(Game3.this);
			if(pref.getBoolean("vibrate", true))
				rokon.vibrate(1000);
			
			scoreDialog.setMessage("Do you want to save score "+score+" ? (you can see high-scores on http://malgonstudio.sfhost.net/apple-crunch/score)");
			scoreDialog.show();
		}
    };
    
public void moveSprites()
{
	Random random=new Random();
	
	for(int i=0;i<=random.nextInt(5);i++)
    {
		Sprite sprite;
		Hotspot hotspot;
		
		Random random2=new Random();
		if(random2.nextInt(3)==0)
	    {
			sprite = new Sprite(i*androidTexture.getWidth()+10,0,androidTexture);
		    hotspot = new Hotspot(sprite); 
		    
		    androidSpriteList.add(sprite);
		    androidHotspotList.add(hotspot);
	    }
		else
		{
			sprite = new Sprite(i*appleTexture.getWidth()+10,0,appleTexture);
		    hotspot = new Hotspot(sprite);  
		    appleSpriteList.add(sprite);
		    appleHotspotList.add(hotspot);
		}
		
		rokon.addSprite(sprite);
	    rokon.addHotspot(hotspot);
		
	    if(time<timeToPlay/3)
		{
	    	sprite.setVelocityY(100);
		}
		else if(time<(timeToPlay/3)*2)
		{
			sprite.setVelocityY(140);
		}
		else
		{
			sprite.setVelocityY(80);
		}
		
		sprite.addCollisionSprite(canonSprite);
		canonSprite.addCollisionSprite(sprite);
		sprite.setCollisionHandler(collisionHandler);
    }
	
	removeSprites();
}
    

public void removeSprites()
{
	//On supprime tous les anciens Sprites qui sont sortis de l'écran
	for(int i=0;i<appleSpriteList.size();i++)
	{
		if(appleSpriteList.get(i).getY()>screenHeight)
		{
			rokon.removeHotspot(appleHotspotList.get(i));
			rokon.removeSprite(appleSpriteList.get(i));
			
			appleHotspotList.remove(i);
			appleSpriteList.remove(i);
		}
	}
	
	for(int i=0;i<androidSpriteList.size();i++)
	{
		if(androidSpriteList.get(i).getY()>screenHeight)
		{
			rokon.removeHotspot(androidHotspotList.get(i));
			rokon.removeSprite(androidSpriteList.get(i));
			
			androidHotspotList.remove(i);
			androidSpriteList.remove(i);
		}
	}
	
	for(int i=0;i<laserSpriteList.size();i++)
	{
		if(laserSpriteList.get(i).getY()<-laserSpriteList.get(i).getHeight())
		{
			rokon.removeHotspot(laserHotspotList.get(i));
			rokon.removeSprite(laserSpriteList.get(i));
			
			laserHotspotList.remove(i);
			laserSpriteList.remove(i);
		}
	}
}



public void onCreate() {
    createEngine("graphics/splash2.png",320, 430, false);

    
    alertDialog = new AlertDialog.Builder(Game3.this).create();
	alertDialog.setTitle("Game3 Finished");
	alertDialog.setButton("Play again", new DialogInterface.OnClickListener() {
	      public void onClick(DialogInterface dialog, int which) {
	    	score=0;
	    	appleTouch=0;
	    	androidTouch=0;
	    	time=timeToPlay/1000;
	    	textScore.setText(String.valueOf(score));
	        rokon.unfreeze();
	        
	        timer.postDelayed(endOfGame1, timeToPlay);
	      }
	}); 
	alertDialog.setButton2("Quit", new DialogInterface.OnClickListener() {
	      public void onClick(DialogInterface dialog, int which) {
	    	  System.exit(0);
	    	  Game3.this.finish();
	      }
	}); 
	
	scoreDialog = new AlertDialog.Builder(Game3.this).create();
	scoreDialog.setTitle("Send score ?");
	scoreDialog.setButton("Yes", new DialogInterface.OnClickListener() {
	      public void onClick(DialogInterface dialog, int which) {
	    	  SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(Game3.this);
				
				HttpClient httpclient = new DefaultHttpClient();
	            HttpGet get = new HttpGet(url+"?key=o2BY3XUF0AgytDdaLmugnXiFUfRRQF&username="+pref.getString("username","Player Default").replace(" ","+")+"&score="+score+"&type=2");
	            try {
					httpclient.execute(get);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				String msg="Game finished, you've killed apple "+appleTouch+" time ! ";
				
				if(androidTouch>0)
				{
					msg += "But you killed Android "+androidTouch+" time :( ";
					msg+= "Next time don't kill Android, because it will eat apples :D ";
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
	    	  	String msg="Game finished, you've killed apple "+appleTouch+" time ! ";
				
				if(androidTouch>0)
				{
					msg += "But you killed Android "+androidTouch+" time :( ";
					msg+= "Next time don't kill Android, because it will eat apples :D ";
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
	
	goalDialog = new AlertDialog.Builder(Game3.this).create();
	goalDialog.setTitle("Goal");
	goalDialog.setMessage("The goal of this game, is to shoot apple sprites, by clicking on touchpad center. You can move your phone sprite, using screen or touchpad. Care to don't kill Android sprites ! To get more points, catch Android Sprites with the phone sprite.");
	goalDialog.setButton("Ok", new DialogInterface.OnClickListener() {
	      public void onClick(DialogInterface dialog, int which) {
	    	    timer.postDelayed(endOfGame1, timeToPlay);
	    		timerTime.postDelayed(checkTime, 1000);
	    		timerAndroid.postDelayed(checkAndroid, 1000);
	      }
	});
}

    @Override
    public void onLoad() {
	    	DisplayMetrics dm = new DisplayMetrics(); 
	    	getWindowManager().getDefaultDisplay().getMetrics(dm); 
    	
            atlas = new TextureAtlas(1024, 1024);
            atlas.insert(appleTexture = new Texture(R.drawable.apple));
            atlas.insert(appleCrunchTexture = new Texture(R.drawable.crunch));
            atlas.insert(androidTexture = new Texture(R.drawable.android));
            atlas.insert(androidCrunchTexture = new Texture(R.drawable.android_crunch));
            atlas.insert(backgroundTexture = new Texture(R.drawable.background2));
            atlas.insert(laserTexture = new Texture(R.drawable.laser));
            atlas.insert(canonTexture = new Texture(R.drawable.canon));
            atlas.insert(font = new Font("fonts/256BYTES.TTF"));
            TextureManager.load(atlas);
            
            canonSprite = new Sprite(20,dm.widthPixels+canonTexture.getWidth(),canonTexture);
            canonHotspot = new Hotspot(canonSprite);    
                        
            background = new FixedBackground(backgroundTexture);
    }

    @Override
    public void onLoadComplete() {
            rokon.setBackground(background);
            
            //rokon.addSprite(laserSprite);
            //rokon.addHotspot(laserHotspot);
            
            rokon.addSprite(canonSprite);
            rokon.addHotspot(canonHotspot);

            canonSprite.setCollisionHandler(collisionHandler);
            
            textScore = new Text(String.valueOf(score), font, 40, 380, 32);
            textTime = new Text(String.valueOf(time), font, 10, 10, 32);
    		rokon.addText(textScore);
    		rokon.addText(textTime);
    		
    		timerGoal.postDelayed(showGoal, 0);
    		
    		loaded=true;
    }

    @Override
    public void onGameLoop() {
    	
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
    	if(loaded)
    	{
	    	if(keyCode==KeyEvent.KEYCODE_DPAD_LEFT)
	    	{
	    		canonSprite.moveX(-moveCanonX);
	    	}
	    	else if(keyCode==KeyEvent.KEYCODE_DPAD_RIGHT)
	    	{
	    		canonSprite.moveX(moveCanonX);
	    	}
	    	else if(keyCode==KeyEvent.KEYCODE_DPAD_CENTER)
	    	{
	    		Sprite laserSprite = new Sprite(180,80,laserTexture);
	            Hotspot laserHotspot = new Hotspot(laserSprite);  
	            
	            rokon.addSprite(laserSprite);
	            rokon.addHotspot(laserHotspot);
	            
	    		laserSprite.setXY(canonSprite.getX()+20, canonSprite.getY());
	    		laserSprite.setVisible(true);
	    		
	    		laserSprite.setVelocityY(-laserVelocity);
	    		
	    		laserSprite.setCollisionHandler(collisionHandler);
	    		
	    		for(int i=0;i<appleSpriteList.size();i++)
	    			laserSprite.addCollisionSprite(appleSpriteList.get(i));
	    		
	    		for(int i=0;i<androidSpriteList.size();i++)
	    			laserSprite.addCollisionSprite(androidSpriteList.get(i));
	    		
	    		laserSpriteList.add(laserSprite);
	    		laserHotspotList.add(laserHotspot);
	    	}
    	}
    	
    	return super.onKeyDown(keyCode, event);
    }
    
    
    @Override
    public void onTouchDown(int x, int y, boolean hotspot)
    {
    	if(loaded)
    	{
	    	if(x>canonSprite.getX()-30 && x<canonSprite.getX()+canonSprite.getWidth()+30 && y>canonSprite.getY()-30)
	    	{
	    		Sprite laserSprite = new Sprite(180,80,laserTexture);
	            Hotspot laserHotspot = new Hotspot(laserSprite);  
	            
	            rokon.addSprite(laserSprite);
	            rokon.addHotspot(laserHotspot);
	            
	    		laserSprite.setXY(canonSprite.getX()+20, canonSprite.getY());
	    		laserSprite.setVisible(true);
	    		
	    		laserSprite.setVelocityY(-laserVelocity);
	    		
	    		laserSprite.setCollisionHandler(collisionHandler);
	    		
	    		for(int i=0;i<appleSpriteList.size();i++)
	    			laserSprite.addCollisionSprite(appleSpriteList.get(i));
	    		
	    		for(int i=0;i<androidSpriteList.size();i++)
	    			laserSprite.addCollisionSprite(androidSpriteList.get(i));
	    		
	    		laserSpriteList.add(laserSprite);
	    		laserHotspotList.add(laserHotspot);
	    	}
    	}
    }
    
    @Override
    public void onTouch(int x, int y, boolean hotspot)
    {
    	if(loaded)
    	{
	    	if(x<canonSprite.getX()+(canonSprite.getWidth()/2) && y<canonSprite.getY())
				canonSprite.moveX(-(moveCanonX/3));
			else if(x>canonSprite.getX()+(canonSprite.getWidth()/2) && y<canonSprite.getY())
				canonSprite.moveX(moveCanonX/3);
	    	
			else if(x<canonSprite.getX()-30 && y>canonSprite.getY())
				canonSprite.moveX(-(moveCanonX/3));
			else if(x>canonSprite.getX()+canonSprite.getWidth()+30 && y>canonSprite.getY())
				canonSprite.moveX(moveCanonX/3);
    	}
    }
}