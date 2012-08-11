package com.facebook.android;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Splash Screen ou tela de splash. Tela inicial exibida no início da aplicação
 *
 * 
 *
 */
public class SplashActivity extends Activity {
	
	/**
	 * 
	 */
    private long splashDelay = 1500;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.splash);
        
        TimerTask task = new TimerTask() {
        	
            @Override
            public void run() {
            	
                finish();
                
                Intent hackbookIntent = new Intent().setClass(SplashActivity.this, Hackbook.class);
                
                startActivity(hackbookIntent);
                
            }
            
        };

        // cria um timer
        Timer timer = new Timer();
        
        // executa a tarefa
        timer.schedule(task, splashDelay);
        
    }
    
}
