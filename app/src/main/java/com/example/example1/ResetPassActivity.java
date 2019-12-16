package com.example.example1;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static com.example.example1.MainActivity.Email;

public class ResetPassActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    EditText ed1,ed2;
    String pass,cpass,email="";
    Button button;
    ProgressBar p1;
    ProgressDialog nDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ed1=findViewById(R.id.password);
        ed2=findViewById(R.id.cpass);
        button=findViewById(R.id.reset_btn);
        p1=findViewById(R.id.progress1);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, RateActivity.MODE_PRIVATE);
        email=sharedpreferences.getString(Email,"");

        toolbar.setTitle("Welcome , "+email);
        if(email == ""){
            Intent login =new Intent(getApplicationContext(),MainActivity.class);
            startActivity(login);

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(getApplicationContext(), DashBoardActivity.class);
            startActivity(i);
        }
        return false;
        // Disable back button..............
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.rate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            call_func();
        }
        if(id == R.id.reset_pass){
            Intent i =new Intent(getApplicationContext(),ResetPassActivity.class);
            startActivity(i);
        }
        if(id == R.id.action_logout){
            logout_click();
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout_click(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setMessage("Are you sure want to Logout?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

                SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, RateActivity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                String username=sharedpreferences.getString(Email,"");
                if(Email == ""){
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent i = new Intent(getApplicationContext(),RateActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_porder) {
            Intent i = new Intent(getApplicationContext(),PlaceOrderActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_reset) {

        } else if (id == R.id.nav_dreport) {
            Intent i = new Intent(getApplicationContext(),DailyReport.class);
            startActivity(i);
        } else if (id == R.id.nav_wireorder) {
            Intent i = new Intent(getApplicationContext(),WireOrderActivity.class);
            startActivity(i);
        }else if(id == R.id.nav_sporeder){
            Intent i = new Intent(getApplicationContext(),Sell_placeActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_logout) {
            logout_click();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void reset_pass(View view){
        pass=ed1.getText().toString();
        cpass=ed2.getText().toString();

        if(!TextUtils.isEmpty(pass) && pass.length() >5){
            if(!TextUtils.isEmpty(cpass) && pass.equals(cpass)){
                Toast.makeText(this, "function is calling", Toast.LENGTH_SHORT).show();
                p1.setVisibility(View.VISIBLE);
                button.setBackground(ContextCompat.getDrawable(ResetPassActivity.this, R.drawable.login_2));
                button.setText("Processing .....");
                getJSON("https://www.orientexchange.in/bankagent/rest-app.php",email,cpass);
            }else {
                ed2.setError("Please confirm your password");
            }
        }
        else {
            ed1.setError("Please Enter new password and min length should be 6 chars");
        }
    }

    //this method is actually fetching the json string
    private void getJSON(final String urlWebService,final String user_email,final String passwords) {
        /*
         * As fetching the json string is a network operation
         * And we cannot perform a network operation in main thread
         * so we need an AsyncTask
         * The constrains defined here are
         * Void -> We are not passing anything
         * Void -> Nothing at progress update as well
         * String -> After completion it should return a string and it will be the json string
         * */
        class GetJSON extends AsyncTask<String, Void, String> {

            //this method will be called before execution
            //you can display a progress bar or something
            //so that user can understand that he should wait
            //as network operation may take some time
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            //this method will be called after execution
            //so here we are displaying a toast with the json string
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s !="") {
                    p1.setVisibility(View.INVISIBLE);
                    button.setBackground(ContextCompat.getDrawable(ResetPassActivity.this, R.drawable.sellcashtext));
                    button.setText("Reset");
                    ed1.getText().clear();
                    ed2.getText().clear();
                    Log.i("result set",s);
                    try {
                       JSONObject json=new JSONObject(s);
                       String status=json.getString("status");

                       if(status.equals("success")){
                           AlertDialog.Builder alert = new AlertDialog.Builder(ResetPassActivity.this);
                           alert.setTitle("");
                           alert.setMessage("Your password reset successfully");
                           alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int whichButton) {

                               }
                           });

                           alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int whichButton) {
                                   // Canceled.
                               }
                           });
                           alert.show();
                           new Handler().postDelayed(new Runnable() {
                               @Override
                               public void run() {
                                   SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, RateActivity.MODE_PRIVATE);
                                   SharedPreferences.Editor editor = sharedpreferences.edit();
                                   editor.clear();
                                   editor.commit();
                                   String username=sharedpreferences.getString(Email,"");
                                   if(Email == ""){
                                       Intent i = new Intent(getApplicationContext(),MainActivity.class);
                                       startActivity(i);
                                   }
                               }
                           }, 5000);
                       }
                       else {
                           AlertDialog.Builder alert = new AlertDialog.Builder(ResetPassActivity.this);
                           alert.setTitle("");
                           alert.setMessage("Error in restting password!please try after some times");
                           alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int whichButton) {
                               }
                           });
                           alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int whichButton) {
                                   // Canceled.
                               }
                           });
                           alert.show();
                       }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(ResetPassActivity.this);
                    alert.setTitle("");
                    alert.setMessage("Error in restting password!please try after some times");
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                        }
                    });
                    alert.show();
                }
            }
            //in this method we are fetching the json string
            @Override
            protected String doInBackground(String... params) {
                try {
                    //creating a URL
                    URL url = new URL(urlWebService);
                    //Opening the URL using HttpURLConnection
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    //StringBuilder object to read the string from the service
                    StringBuilder sb = new StringBuilder();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("user_email","UTF-8")+"="+URLEncoder.encode(user_email,"UTF-8")+"&"
                            +URLEncoder.encode("user_password","UTF-8")+"="+URLEncoder.encode(passwords,"UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = con.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    //We will use a buffered reader to read the string from service
                    //A simple string to read values from each line
                    String json;

                    //reading until we don't find null
                    while ((json = bufferedReader.readLine()) != null) {
                        //appending it to string builder
                        sb.append(json + "\n");
                    }

                    //finally returning the read string
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        //creating asynctask object and executing it
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }
    public void call_func(){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:+91-8884734422"));
        startActivity(callIntent);
    }
}