package com.myapp.suresh.tasktimer;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText etEmpName, etPassword, etConfirmPassword, etEmpId ;

    private Button btSignUp;
    private TextView alreadyaMember;
    private CheckBox term_conditions;
    Toast toast;
    LinearLayout signUplayout;
    Animation shakeAnim;
    MediaPlayer opps, thanku,slide;
    Intent incomingIntent;
    private ImageView ivResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiaView();
        signUplayout = (LinearLayout) findViewById(R.id.signUplayout);

        // Load ShakeAnimation
        shakeAnim = AnimationUtils.loadAnimation(this,
                R.anim.shake);
    }
        //load country


    //Initialize all view in initiaView()
    private void initiaView() {
        etEmpName = (EditText) findViewById(R.id.etEmpName);
        etEmpId = (EditText) findViewById(R.id.etEmpId);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        btSignUp = (Button) findViewById(R.id.btSave);
        term_conditions = (CheckBox) findViewById(R.id.terms_conditions);
        alreadyaMember = (TextView) findViewById(R.id.tvAlreadyaMember);
        slide = MediaPlayer.create(this, R.raw.slide);
        alreadyaMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSinUp = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intentSinUp);
            }
        });
    }

    private void checkValidation() {
        //get strings of all editText for validation
        String getFirstName = etEmpName.getText().toString();
        String getEmailId = etEmpId.getText().toString();
        String getPassword = etPassword.getText().toString();
        String getConfirmPassword = etConfirmPassword.getText().toString();

        //check if all the string are null or not
        try {
            if (getFirstName.equals("") || getFirstName.length() == 0
                    || getEmailId.equals("") || getEmailId.length() == 0
                    || getPassword.equals("") || getPassword.length() == 0
                    || getConfirmPassword.equals("") || getConfirmPassword.length() == 0) {
                Toast.makeText(this, "All the field are required", Toast.LENGTH_LONG).show();
                signUplayout.startAnimation(shakeAnim);
            }//check if mobile number is valid
            //Check if password is valid
            else if (getPassword.length() <= 5) {
                etPassword.setError(Html.fromHtml("<font color='red'>Minimum 8 character required </font>"));
                etPassword.startAnimation(shakeAnim);
                opps.start();
            }
            // Check if both password should be equal
            else if (!getConfirmPassword.equals(getPassword)) {
                Toast.makeText(this, "Both password does not match", Toast.LENGTH_LONG).show();
                etConfirmPassword.startAnimation(shakeAnim);
                opps.start();
            }
            //check if check box is checked
            else if (!term_conditions.isChecked()) {
                term_conditions.startAnimation(shakeAnim);
                Toast.makeText(this, "Please select Terms & Conditions", Toast.LENGTH_LONG).show();
            } else {
                // Save the Data in Database
                // reg_btn.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
                Log.d("PASSWORD", getFirstName);
                Log.d("RE PASSWORD", getPassword);
                Log.d("SECURITY HINT", getEmailId);
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);

                Toast.makeText(this, "Yo Yo ! Sign Up done", Toast.LENGTH_LONG).show();
                Intent newIntent = getIntent();
                newIntent.putExtra("tag_person_name", getFirstName);
                newIntent.putExtra("tag_person_pin", getEmailId);
                newIntent.putExtra("tag_person_pass", getPassword);

                    this.setResult(RESULT_OK, newIntent);

                    finish();
                thanku.start();
            }
       /* } catch (Exception e) {

        }*/
        } catch (Exception e){}
    }

    public void signUp(View view) {
        Log.i("suresh", "Navigator called" + view.getId());
        switch (view.getId()) {
            case R.id.btSave:
                checkValidation();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.right_enter, R.anim.left_out);
        slide.start();
    }

}
