package br.com.mltech;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * EnviaEmailExternoActivity
 * 
 * @author maurocl
 *
 */
public class EnviaEmailExternoActivity extends Activity {

  private static final String TAG = "EnviaEmailExternoActivity";

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    Button addImage = (Button) findViewById(R.id.send_email);

    addImage.setOnClickListener(new View.OnClickListener() {

      public void onClick(View view) {

        //Mail m = new Mail("maurocl.lopes@gmail.com", "Mcl16dcjl");
        Mail m = new Mail("maurocl@mltech.com.br", "mcl16d");

        String[] toArr = { "maurocl@terra.com.br",
            "maurocl.lopes@gmail.com" };

        m.setTo(toArr);
        m.setFrom("maurocl@mltech.com.br");
        m.setSubject("This is an email sent using my Mail JavaMail wrapper from an Android device.");
        m.setBody("Email body.");

        try {

          // m.addAttachment("/sdcard/filelocation");

          if (m.send()) {
            
            Toast.makeText(EnviaEmailExternoActivity.this,
                "Email was sent successfully.",
                Toast.LENGTH_LONG).show();
            Log.v(TAG, "Email was sent successfully.");
            
          } else {
            
            Toast.makeText(EnviaEmailExternoActivity.this,
                "Email was not sent.", Toast.LENGTH_LONG)
                .show();
            Log.v(TAG, "Email was not sent.");
            
          }
        } catch (Exception e) {
          
          Toast.makeText(EnviaEmailExternoActivity.this,
              "There was a problem sending the email.",
              Toast.LENGTH_LONG).show();
          
          Log.e("MailApp", "Could not send email", e);
          
        }

      }

    });

  }
}