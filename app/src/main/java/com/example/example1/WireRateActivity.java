package com.example.example1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.impl.conn.Wire;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

import android.widget.Button;

import static com.example.example1.MainActivity.Email;

public class WireRateActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView [] tname=new TextView[24];
    TextView [] tcnbuy=new TextView[24];
    String email="";
    ConstraintLayout tab;
    ProgressDialog nDialog;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wire_rate);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, WireRateActivity.MODE_PRIVATE);
        email=sharedpreferences.getString(Email,"");
        isConnected(WireRateActivity.this);
        if(!isConnected(WireRateActivity.this)) buildDialog(WireRateActivity.this).show();
        else {
            if(email == ""){
                Intent login =new Intent(getApplicationContext(),MainActivity.class);
                startActivity(login);
            }
            else { getJSON("https://www.orientexchange.in/bankagent/request_new.php",email);
                nDialog = new ProgressDialog(WireRateActivity.this);
                nDialog.setMessage("Loading..");
                nDialog.setTitle("");
                nDialog.setIndeterminate(false);
                nDialog.setCancelable(true);
                nDialog.show();
            }
        }

        tab =  findViewById(R.id.tab_parent);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        tname[0]=findViewById(R.id.cnname_0);tcnbuy[0]=findViewById(R.id.cnbuy_0);
        tname[1]=findViewById(R.id.cnname_1);tcnbuy[1]=findViewById(R.id.cnbuy_1);
        tname[2]=findViewById(R.id.cnname_2);tcnbuy[2]=findViewById(R.id.cnbuy_2);
        tname[3]=findViewById(R.id.cnname_3);tcnbuy[3]=findViewById(R.id.cnbuy_3);
        tname[4]=findViewById(R.id.cnname_4);tcnbuy[4]=findViewById(R.id.cnbuy_4);
        tname[5]=findViewById(R.id.cnname_5);tcnbuy[5]=findViewById(R.id.cnbuy_5);
        tname[6]=findViewById(R.id.cnname_6);tcnbuy[6]=findViewById(R.id.cnbuy_6);
        tname[7]=findViewById(R.id.cnname_7);tcnbuy[7]=findViewById(R.id.cnbuy_7);
        tname[8]=findViewById(R.id.cnname_8);tcnbuy[8]=findViewById(R.id.cnbuy_8);
        tname[9]=findViewById(R.id.cnname_9);tcnbuy[9]=findViewById(R.id.cnbuy_9);
        tname[10]=findViewById(R.id.cnname_10);tcnbuy[10]=findViewById(R.id.cnbuy_10);
        tname[11]=findViewById(R.id.cnname_11);tcnbuy[11]=findViewById(R.id.cnbuy_11);
        tname[12]=findViewById(R.id.cnname_12);tcnbuy[12]=findViewById(R.id.cnbuy_12);
        tname[13]=findViewById(R.id.cnname_13);tcnbuy[13]=findViewById(R.id.cnbuy_13);
        tname[14]=findViewById(R.id.cnname_14);tcnbuy[14]=findViewById(R.id.cnbuy_14);
        tname[15]=findViewById(R.id.cnname_15);tcnbuy[15]=findViewById(R.id.cnbuy_15);
        tname[16]=findViewById(R.id.cnname_16);tcnbuy[16]=findViewById(R.id.cnbuy_16);
        tname[17]=findViewById(R.id.cnname_17);tcnbuy[17]=findViewById(R.id.cnbuy_17);
        tname[18]=findViewById(R.id.cnname_18);tcnbuy[18]=findViewById(R.id.cnbuy_18);
        tname[19]=findViewById(R.id.cnname_19);tcnbuy[19]=findViewById(R.id.cnbuy_19);
        tname[20]=findViewById(R.id.cnname_20);tcnbuy[20]=findViewById(R.id.cnbuy_20);
        tname[21]=findViewById(R.id.cnname_21);tcnbuy[21]=findViewById(R.id.cnbuy_21);
        tname[22]=findViewById(R.id.cnname_22);tcnbuy[22]=findViewById(R.id.cnbuy_22);
        tname[23]=findViewById(R.id.cnname_23);tcnbuy[23]=findViewById(R.id.cnbuy_23);
        get_fb();
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
        if(id == R.id.reset_pass) {
            Intent i = new Intent(getApplicationContext(), ResetPassActivity.class);
            startActivity(i);
        }
        if(id == R.id.action_logout){
            logout_click();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // finish();
            }
        });

        return builder;
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
    public void moveTobuy(View v){
        Intent i2 = new Intent(getApplicationContext(),BuyRateActivity.class);
        startActivity(i2);
    }
    public void moveTosell(View v){
        Intent i2 = new Intent(getApplicationContext(),RateActivity.class);
        startActivity(i2);
    }
    public void moveTowire(View v){
        final Button button = findViewById(R.id.buy);
        button.setBackgroundColor(getResources().getColor(R.color.colorCurrency));

        Intent i2 = new Intent(getApplicationContext(),WireRateActivity.class);
        startActivity(i2);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent i= new Intent(getApplicationContext(),RateActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_porder) {
            Intent i = new Intent(getApplicationContext(),PlaceOrderActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_reset) {
            Intent i = new Intent(getApplicationContext(),ResetPassActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_dreport) {
            Intent i = new Intent(getApplicationContext(),DailyReport.class);
            startActivity(i);
        } else if (id == R.id.nav_wireorder) {
            Intent i = new Intent(getApplicationContext(),WireOrderActivity.class);
            startActivity(i);
        } else if(id == R.id.nav_sporeder){
           Intent i=new Intent(getApplicationContext(),Sell_placeActivity.class);
           startActivity(i);
        }
        else if (id == R.id.nav_logout) {
            logout_click();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //this method is actually fetching the json string
    private void getJSON(final String urlWebService,final String user_email) {
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
                JSONArray arr = null;
                if(s !=null) {
                    nDialog.hide();
                    try {
                        arr = new JSONArray(s);
                        // fetch JSONObject named employee
                   /* JSONObject NewData =arr.getJSONObject(0);
                    JSONObject Table1=NewData.getJSONObject("NewDataSet");
                    JSONObject str=Table1.getJSONObject("Table");
                    String curr1=str.getString("CurrencyName");
                    tname[0].setText(curr1);
                    tcnbuy[0].setText(str.getString("BestBuyPrice"));
                    tcdbuy[0].setText(str.getString("BestBuyPrice"));

                    JSONObject NewData1 =arr.getJSONObject(1);
                    JSONObject Table=NewData1.getJSONObject("NewDataSet");
                    JSONArray seg=Table.getJSONArray("Table"); */
                        for (int j = 0; j < arr.length(); j++) {
                            JSONObject NewData = arr.getJSONObject(j);
                            String Currency = NewData.getString("currency");
                            String WiretransferSell = NewData.getString("WiretransferSell");
                            Log.i("Currency name", Currency);
                            tname[j].setText(Currency);
                            if(WiretransferSell.equals("0")){ tcnbuy[j].setText("N/A");}
                            else tcnbuy[j].setText(WiretransferSell);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                    String post_data = URLEncoder.encode("user_email","UTF-8")+"="+URLEncoder.encode(user_email,"UTF-8");
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
    public void get_fb() {
        timer = new Timer();
        TimerTask hourlyTask = new TimerTask() {
            @Override
            public void run() {
                getJSON("https://www.orientexchange.in/bankagent/request_new.php",email);
            }
        };
        // schedule the task to run starting now and then every hour...
        timer.schedule(hourlyTask, 0, 60000 * 5);
    }
    public void call_func(){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:+91-8884734422"));
        startActivity(callIntent);
    }
}