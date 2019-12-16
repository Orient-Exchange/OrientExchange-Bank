package com.example.example1;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import java.time.Month;
import java.util.Calendar;

import static com.example.example1.MainActivity.Email;

public class BuyhistoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    String email;
    TableLayout ll;
    TextView tv1,tv2,tv3,tv4,tv5;
    LinearLayout layout1,layout2,layout3,layout4,layout5;
    EditText txtDate, txtTime;
    public static final String Order = "";
    public static final String status = "";
    public static final String amt = "";
    SharedPreferences sharedPreferences1,sharedPreferences2,sharedPreferences3;
    public static final String orderPREFERENCES = "OrderNumber" ;
    public static final String statusPREFERENCES = "OrderStatus" ;
    public static final String amountPREFERENCES = "Orderamount" ;
    TextView ed1,ed2;
    String date1,date2;
    LinearLayout dlayout;
    private int mYear, mMonth, mDay, mHour, mMinute;
    int parrlength;
    ProgressDialog nDialog;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyhistory);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

            ed1=findViewById(R.id.in_date);
            ed2=findViewById(R.id.in_date1);
            date1=ed1.getText().toString();
            date2=ed2.getText().toString();
       // Toast.makeText(this, date1+" , "+date2, Toast.LENGTH_SHORT).show();
            dlayout =findViewById(R.id.dynamic_layou);
            SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, RateActivity.MODE_PRIVATE);
            email=sharedpreferences.getString(Email,"");
            //  Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
        if(email == ""){
                Intent login =new Intent(getApplicationContext(),MainActivity.class);
                startActivity(login);
            }
        else { getJSON("https://www.orientexchange.in/bankagent/daily-report-app-buy.php",email,date1,date2);}
        nDialog = new ProgressDialog(BuyhistoryActivity.this);
        nDialog.setMessage("Loading..");
        nDialog.setTitle("");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();

        sharedPreferences1=BuyhistoryActivity.this.getSharedPreferences(orderPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences2=BuyhistoryActivity.this.getSharedPreferences(statusPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences3=BuyhistoryActivity.this.getSharedPreferences(amountPREFERENCES, Context.MODE_PRIVATE);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        layout1=findViewById(R.id.col1); layout2=findViewById(R.id.col2);
        //  layout3=findViewById(R.id.col3);
        layout4=findViewById(R.id.col4);
        layout5=findViewById(R.id.col5);
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
        if(id == R.id.action_logout){
            logout_click();
        }
        if(id == R.id.reset_pass){
            Intent i =new Intent(getApplicationContext(),ResetPassActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
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
            Intent i = new Intent(getApplicationContext(),ResetPassActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_dreport) {
            Intent i = new Intent(getApplicationContext(),DailyReport.class);
            startActivity(i);
        } else if (id == R.id.nav_wireorder) {
            Intent i = new Intent(getApplicationContext(), WireOrderActivity.class);
            startActivity(i);
        } else if(id == R.id.nav_sporeder){
            Intent i= new Intent(getApplicationContext(),Sell_placeActivity.class);
            startActivity(i);
        }
        else if(id == R.id.nav_logout){
            logout_click();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //this method is actually fetching the json string
    private void getJSON(final String urlWebService,final String user_email,final String start_date,final String end_date) {
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
            @SuppressLint("ResourceAsColor")
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSONArray arr = null;
                //Toast.makeText(DailyReport.this, s, Toast.LENGTH_SHORT).show();
                nDialog.hide();
                try {
                    if(s !="") {
                        int a=layout1.getChildCount();
                        int b=layout5.getChildCount();
                        int c=layout2.getChildCount();
                        int d=layout4.getChildCount();
                        for(int k=a;k>0;k--){
                            View thisChild = layout1.getChildAt(k);
                            layout1.removeView(thisChild);
                        }
                        for(int p=b;p>0;p--){
                            View thisChild = layout5.getChildAt(p);
                            layout5.removeView(thisChild);
                        }
                        for(int q=c;q>0;q--){
                            View thisChild = layout2.getChildAt(q);
                            layout2.removeView(thisChild);
                        }
                        for(int r=d;r>0;r--){
                            View thisChild = layout4.getChildAt(r);
                            layout4.removeView(thisChild);
                        }
                        arr = new JSONArray(s);
                        Log.i("json array", arr.toString());
                        for (int j = 0; j < arr.length(); j++) {
                            JSONObject NewData = arr.getJSONObject(j);
                            final String orderNum = NewData.getString("bagentOrderNum");
                            String submitted = NewData.getString("OrderAt");
                            String type = NewData.getString("order_type");
                            final String amount_t = NewData.getString("amount");
                            final String status_s = NewData.getString("status");

                        /*  TableRow row= new TableRow(DailyReport.this);
                            TableRow.LayoutParams lp = new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT,
                                    TableRow.LayoutParams.WRAP_CONTENT, 1f);
                                    row.setLayoutParams(lp);*/
                            tv1 = new TextView(BuyhistoryActivity.this);
                            LinearLayout.LayoutParams textview1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                            textview1Params.setMargins(10, 10, 10, 15);
                            tv1.setLayoutParams(textview1Params);

                            tv5 = new TextView(BuyhistoryActivity.this);
                            LinearLayout.LayoutParams textview5Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            textview5Params.setMargins(2, 0, 0, 0);
                            tv5.setLayoutParams(textview5Params);

                            tv2 = new TextView(BuyhistoryActivity.this);
                            LinearLayout.LayoutParams textview2Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            textview2Params.setMargins(10, 10, 10, 15);
                            tv2.setLayoutParams(textview2Params);

                            tv3 = new TextView(BuyhistoryActivity.this);
                            LinearLayout.LayoutParams textview3Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            textview3Params.setMargins(10, 10, 10, 15);
                            tv3.setLayoutParams(textview3Params);

                            tv4 = new TextView(BuyhistoryActivity.this);
                            LinearLayout.LayoutParams textview4Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            textview4Params.setMargins(10, 10, 0, 15);
                            tv4.setLayoutParams(textview4Params);

                            //  row.addView(tv1);  row.addView(tv2);  row.addView(tv3);  row.addView(tv4);
                            //  tv1.setGravity(Gravity.CENTER);tv2.setGravity(Gravity.CENTER);tv3.setGravity(Gravity.CENTER);tv4.setGravity(Gravity.CENTER);
                            //   ll.addView(row,j);*/
                            tv1.setText(String.valueOf(j + 1));
                            tv5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icons_eye_3, 0, 0, 0);
                            tv1.setId(j+1);
                            tv2.setText(String.valueOf(orderNum));
                            tv3.setText(String.valueOf(type));
                            tv4.setText(String.valueOf(submitted));

                            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
                            tv1.setTextColor(Color.WHITE);
                            tv5.setTextColor(Color.WHITE);
                            tv5.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {

                                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                                    editor1.putString(Order, orderNum);
                                    editor1.commit();
                                    Log.i("amount =", amount_t);
                                    SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                                    editor2.putString(status,status_s);
                                    editor2.commit();
                                    SharedPreferences.Editor editor3 = sharedPreferences3.edit();
                                    editor3.putString(amt,amount_t);
                                    editor3.commit();
                                    Intent history = new Intent(getApplicationContext(), OrderHistory.class);
                                    startActivity(history);
                                }
                            });

                            tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
                            tv2.setTextColor(Color.WHITE);
                            tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
                            tv3.setTextColor(Color.WHITE);
                            tv4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
                            tv4.setTextColor(Color.WHITE);

                            // tv2.setBackgroundResource(R.drawable.currencytext);
                            LinearLayout layout = new LinearLayout(BuyhistoryActivity.this);
                            LinearLayout.LayoutParams lv = new TableRow.LayoutParams('0',
                                    LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                            layout.setLayoutParams(lv);
                            //  layout1.removeView(tv1); layout2.removeView(tv2); layout5.removeView(tv5); layout4.removeView(tv4);
                            layout1.addView(tv1);
                            layout5.addView(tv5);
                            layout2.addView(tv2);
                            layout4.addView(tv4);
                        }
                    }
                    else {
                        Snackbar snackbar = Snackbar
                                .make(drawer, "Empty result Set! Try again with different date", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                            +URLEncoder.encode("start_date","UTF-8")+"="+URLEncoder.encode(start_date,"UTF-8")+"&"
                            +URLEncoder.encode("end_date","UTF-8")+"="+URLEncoder.encode(end_date,"UTF-8");;
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

    public void Date_pick1(View v) {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        ed2.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void Date_pick(View v) {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        ed1.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    public void remittanceOrder(View v){
        Intent i2 = new Intent(getApplicationContext(),WirehistoryActivity.class);
        startActivity(i2);
    }
    public void buyOrder(View v){
        final Button button = findViewById(R.id.buy);
        button.setBackgroundColor(getResources().getColor(R.color.colorCurrency));

        Intent i2 = new Intent(getApplicationContext(),BuyhistoryActivity.class);
        startActivity(i2);
    }
    public void sellOrder(View v){
        Intent i2 = new Intent(getApplicationContext(),DailyReport.class);
        startActivity(i2);
    }

    public void fetch_data(View view){
        date1=ed1.getText().toString();
        date2=ed2.getText().toString();
        //Toast.makeText(this, date1+" , "+date2, Toast.LENGTH_SHORT).show();
        getJSON("https://www.orientexchange.in/bankagent/daily-report-app-buy.php",email,date1,date2);
        nDialog = new ProgressDialog(BuyhistoryActivity.this);
        nDialog.setMessage("Loading..");
        nDialog.setTitle("");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();
    }

    public void call_func(){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:+91-8884734422"));
        startActivity(callIntent);
    }
}
