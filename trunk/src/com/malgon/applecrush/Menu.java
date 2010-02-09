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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Menu extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        setContentView(R.layout.menu);
        
        Button button = (Button) findViewById(R.id.Button01);
	    button.setOnClickListener(new View.OnClickListener() {
	                    public void onClick(View v) {
	                    	Intent i = new Intent(Menu.this, Game1.class);
	                        startActivity(i);
	                    }
	            	});
	    
	    Button button3 = (Button) findViewById(R.id.Button03);
	    button3.setOnClickListener(new View.OnClickListener() {
	                    public void onClick(View v) {
	                    	System.exit(0);
	                    	Menu.this.finish();
	                    }
	            	}); 
	    
	    Button button4 = (Button) findViewById(R.id.Button04);
	    button4.setOnClickListener(new View.OnClickListener() {
	                    public void onClick(View v) {
	                    	Intent i = new Intent(Menu.this, Game3.class);
	                        startActivity(i);
	                    }
	            	}); 
	    
	    Button button5 = (Button) findViewById(R.id.Button05);
	    button5.setOnClickListener(new View.OnClickListener() {
	                    public void onClick(View v) {
	                    	Intent i = new Intent(Menu.this, Settings.class);
	                        startActivity(i);
	                    }
	            	}); 
	    
	    
	    SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(this);
	    String username=pref.getString("username", "Player Default");

        if(username.equals("Player Default"))
        {
	        AlertDialog usernameDialog = new AlertDialog.Builder(this).create();
	        usernameDialog.setTitle("Change your username");
	        usernameDialog.setMessage("Think to change your username in settings !");
	        usernameDialog.setButton("Ok", new DialogInterface.OnClickListener() {
	    	      public void onClick(DialogInterface dialog, int which) {
	    	    	  Intent i = new Intent(Menu.this, Settings.class);
	                  startActivity(i);
	    	      }
	    	});
	        
	        usernameDialog.show();
        }
    }
}