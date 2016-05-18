package com.myapp.suresh.tasktimer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "LoginPrefs";
    private Spinner spProject;
    private EditText etTask,etempId;
    TextView tvTime,tvTimeTaken;
    private Button btStart;
    private ProjectDatabaseHelper databaseHelper;
    private String projects,getEmpid;
    LoginActivity loginActivity;
    CustomeCursorAdapter customeCursorAdapter;
    Button butnstart, butnreset;
    TextView time;
    long starttime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedtime = 0L;
    int t = 1;
    int secs = 0;
    int mins = 0;
    int hours=0;
    int milliseconds = 0;
    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        initiaView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.remove("logged");
            editor.commit();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initiaView() {

       spProject = (Spinner)findViewById(R.id.spProject) ;
        Bundle bundle = getIntent().getExtras();
        getEmpid = bundle.toString();
        loadProjectSpinner();
        spProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                projects = tv.getText().toString();
                loadProjectSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        etTask = (EditText)findViewById(R.id.etTask);
        tvTime = (TextView)findViewById(R.id.tvTime);
        tvTimeTaken = (TextView)findViewById(R.id.tvTimeTaken);
        btStart = (Button) findViewById(R.id.btStart);
         etempId = (EditText) findViewById(R.id.etEmpida);
    }

    private void loadProjectSpinner() {
        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(this, R.array.project, android.R.layout.simple_spinner_item);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spProject.setAdapter(countryAdapter);
    }

    public void onClickStart (View v) {
        String getTask = etTask.getText().toString();
        String getTimeTaken = time.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTimeStamp = dateFormat.format(new Date()); // Find todays date
            if (t == 1&& projects.length()!=0&&getTask.length()!=0&&getEmpid.length()!=0) {
                btStart.setText("Pause");
                starttime = SystemClock.uptimeMillis();
                handler.postDelayed(updateTimer, 0);
                t=0;
            }else {
            butnstart.setText("Start");
            timeSwapBuff += timeInMilliseconds;
            handler.removeCallbacks(updateTimer);
                databaseHelper.insertData(currentTimeStamp, getEmpid, projects,getTask,getTimeTaken);
                customeCursorAdapter.changeCursor(databaseHelper.getAllData());
            t = 1;
        }
    }

    public Runnable updateTimer = new Runnable() {
    public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - starttime;
            updatedtime = timeSwapBuff + timeInMilliseconds;
            secs = (int) (updatedtime / 1000);
            mins = secs / 60;
            secs = secs % 60;
            hours = mins/60;
            mins=mins%60;
            milliseconds = (int) (updatedtime % 1000);
            time.setText("" + hours + ":" + String.format("%02d", mins) + ":"
                    + String.format("%03d", secs));
            handler.postDelayed(this, 0);
            }};
            }
