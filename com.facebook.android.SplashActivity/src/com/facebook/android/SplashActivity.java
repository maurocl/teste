
package com.facebook.android;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Splash Screen ou tela de splash. Tela inicial exibida no início da aplicação.
 * 
 * 
 * 
 */
public class SplashActivity extends Activity {

  /**
   * Nº de milisegundos que serão esperados antes da execução da tarefa.
   */
  private long splashDelay = 1500;

  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setContentView(R.layout.splash);

    // The TimerTask class represents a task to run at a specified time.
    // The task may be run once or repeatedly.
    TimerTask task = new TimerTask() {

      @Override
      public void run() {

        // finaliza a activity
        finish();

        // cria uma nova intent com uma instância da classe Hackbook
        Intent hackbookIntent = new Intent().setClass(SplashActivity.this, Hackbook.class);

        // inicia a activity
        startActivity(hackbookIntent);

      }

    };

    // Timers schedule one-shot or recurring tasks for execution.
    Timer timer = new Timer();

    // executa a tarefa
    // Schedule a task for single execution after a specified delay.
    timer.schedule(task, splashDelay);

  }

}
