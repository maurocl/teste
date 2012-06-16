package br.com.mltech;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

public class MyApplication extends Application {

  private static final String TAG = "MyApplication";
  
  private static MyApplication singleton;

  /**
   * Constructor 
   * 
   * @return uma instância da classe
   * 
   */
  public static MyApplication getInstance() {

    return singleton;
  }

  @Override
  public void onCreate() {

    // TODO Auto-generated method stub
    super.onCreate();
    singleton = this;
    //singleton = getInstance();
    
    Log.d(TAG,"*** onCreate() ***");
    
  }

  @Override
  public void onTerminate() {

    // TODO Auto-generated method stub
    super.onTerminate();
    Log.d(TAG,"*** onTerminate() ***");
  }

  @Override
  public void onLowMemory() {

    // TODO Auto-generated method stub
    super.onLowMemory();
    Log.d(TAG,"*** onLowMemory() ***");
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {

    // TODO Auto-generated method stub
    super.onConfigurationChanged(newConfig);
    Log.d(TAG,"*** onConfigurationChanged() ***");
    
    Log.d(TAG,"newConfig="+newConfig);
    
    Log.d(TAG,"newConfig.orientation="+newConfig.orientation);
    
    
  

    
  }

}
