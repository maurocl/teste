package br.com.mltech;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * @class  MailSenderActivity
 * @extends Activity
 * 
 * @author maurocl
 *
 */
public class MailSenderActivity extends Activity {

  public static final String TAG = "MailSenderActivity";

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    Log.v(TAG, "*** onCreate() ***");

    setContentView(R.layout.main);

    final Button send = (Button) this.findViewById(R.id.send_email);
    
    send.setOnClickListener(new View.OnClickListener() {

      public void onClick(View v) {

        Log.v(TAG, "*** onClick() ***");

        try {
          GMailSender sender = new GMailSender(
              "maurocl.lopes@gmail.com", "Mcl16dcjl");
          sender.sendMail("This is Subject", "This is Body",
              "maurocl.lopes@gmail.com", "maurocl@terra.com.br");
        } catch (Exception e) {
          Log.e("SendMail", e.getMessage(), e);
        }

      }
    });

  }
}
