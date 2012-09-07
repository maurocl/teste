
package br.com.mltech.modelo;

import android.app.backup.BackupAgentHelper;
import android.app.backup.SharedPreferencesBackupHelper;

/**
 * MyBackupAgent
 * 
 * @author maurocl
 * 
 */
public class MyBackupAgent extends BackupAgentHelper {

  // The names of the SharedPreferences groups that the application maintains.
  // These are the same strings that are passed to getSharedPreferences(String, int).

  public static final String PREFS_ALL = "preferencias";

  public static final String PREFS_EMAIL = "pref_email";

  //static final String PREFS_DISPLAY = "displayprefs";
  //static final String PREFS_SCORES = "highscores";

  // An arbitrary string used within the BackupAgentHelper implementation to
  // identify the SharedPreferenceBackupHelper's data.
  static final String MY_PREFS_BACKUP_KEY = "myprefs";

  // Simply allocate a helper and install it
  public void onCreate() {

    SharedPreferencesBackupHelper helper =
        new SharedPreferencesBackupHelper(this, PREFS_ALL, PREFS_EMAIL);

    addHelper(MY_PREFS_BACKUP_KEY, helper);

  }

}