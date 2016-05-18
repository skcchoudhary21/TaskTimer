package com.myapp.suresh.tasktimer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Animation shakeAnim;
    EditText etEmailId, etPassword;
    private Button btSignin;
    private LinearLayout signInlayout;
    private TextView tvNotaMember, tvForgetPassword;
    private CheckBox cbShowPassword;
    private MediaPlayer opps, thanku, slide;
    private CustomeCursorAdapter customAdapter;
    private PersonDatabaseHelper databaseHelper;
    private static final int ENTER_DATA_REQUEST_CODE = 1;
    ListView lvList;
    String storedPassword;
    public static final String PREFS_NAME = "LoginPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initiaView();
        signInlayout = (LinearLayout) findViewById(R.id.signInlayout);
        tvNotaMember = (TextView) findViewById(R.id.tvAlreadyaMember);


        tvNotaMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(getBaseContext(), MainActivity.class);
                startActivityForResult(intentLogin, ENTER_DATA_REQUEST_CODE);
            }
        });


        // Load ShakeAnimation
        shakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake);
        overridePendingTransition(R.anim.right_enter, R.anim.left_out);
        databaseHelper = new PersonDatabaseHelper(this);

       /* listView = (ListView) findViewById(R.id.list_data);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });*/

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                customAdapter = new CustomeCursorAdapter(LoginActivity.this, databaseHelper.getAllData());
                //listView.setAdapter(customAdapter);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.left_enter, R.anim.right_out);
        slide.start();
    }

    private void initiaView() {
        etEmailId = (EditText) findViewById(R.id.etEmailIdSignin);
        etPassword = (EditText) findViewById(R.id.etPasswordSignIn);
        btSignin = (Button) findViewById(R.id.btSignIn);
        cbShowPassword = (CheckBox) findViewById(R.id.cbShowPassword);
        slide = MediaPlayer.create(this, R.raw.slide);
        cbShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    cbShowPassword.setText(R.string.hide_pwd);// change
                    // checkbox
                    // text

                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());// show password
                } else {
                    cbShowPassword.setText(R.string.show_pwd);// change
                    // checkbox
                    // text

                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    etPassword.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());// hide password

                }

            }
        });

    }

    private void checkValidation() {
        //get strings of all editText
        String getEmailId = etEmailId.getText().toString();
        String getPassword = etPassword.getText().toString();
        //check if all the string are null or not
        try {
            if (getEmailId.equals("") || getEmailId.length() == 0
                    || getPassword.equals("") || getPassword.length() == 0) {
                Toast.makeText(this, "All the field are required", Toast.LENGTH_LONG).show();
                signInlayout.startAnimation(shakeAnim);
            }
            //check if first name is filled
            else if (getEmailId.equals("") || getEmailId.length() == 0)
                etEmailId.setError(Html.fromHtml("<font color='red'>Mobile Number can't be empty</font>"));
                //check if email Id is valid
            else if (getPassword.length() <= 4) {
                etPassword.startAnimation(shakeAnim);
                etPassword.setError(Html.fromHtml("<font color='red'>Minimum 8 character required </font>"));
            }
            //Toast if everything is ok*/
            else {
                if(databaseHelper.Login(getEmailId, getPassword))
                { Intent intent = new Intent(this,TaskActivity.class);
                    intent.putExtra("empid",getEmailId);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this,"Successfully Logged In", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Password Incorrect", Toast.LENGTH_LONG).show();
                    Intent intentLogin = new Intent(getBaseContext(), TaskActivity.class);
                    startActivity(intentLogin);
                }
            }


        } catch (Exception e) {
        }
    }

    public void signUp(View view) {
        Log.i("suresh", "Navigator called" + view.getId());
                checkValidation();

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ENTER_DATA_REQUEST_CODE && resultCode == RESULT_OK) {

            databaseHelper.insertData(data.getExtras().getString("tag_person_name"), data.getExtras().getString("tag_person_pin"),data.getExtras().getString("tag_person_name"));

            customAdapter.changeCursor(databaseHelper.getAllData());
        }
    }
}