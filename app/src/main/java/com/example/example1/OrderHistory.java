package com.example.example1;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
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
import java.text.DecimalFormat;

import static com.example.example1.DailyReport.amt;
import static com.example.example1.DailyReport.status;
import static com.example.example1.MainActivity.Email;
import static com.example.example1.DailyReport.Order;
import static java.lang.Float.parseFloat;

public class OrderHistory extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
String email="";
String OrderNo,tstatus,tamount;
TextView tv1,tv2,tv3,tv4,tv5,tv_order,tv_status,tv_amount,category;
LinearLayout tb1,tb2,tb3,tb4,tb5;
ProgressDialog nDialog;
LinearLayout layout_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, OrderHistory.MODE_PRIVATE);
        email=sharedpreferences.getString(Email,"");
        if(email == ""){
            Intent login =new Intent(getApplicationContext(),MainActivity.class);
            startActivity(login);
        }
        SharedPreferences sharedpreferences1 = getSharedPreferences(DailyReport.orderPREFERENCES, OrderHistory.MODE_PRIVATE);
        SharedPreferences sharedpreferences2 = getSharedPreferences(DailyReport.statusPREFERENCES, OrderHistory.MODE_PRIVATE);
        SharedPreferences sharedpreferences3 = getSharedPreferences(DailyReport.amountPREFERENCES, OrderHistory.MODE_PRIVATE);
        OrderNo=sharedpreferences1.getString(Order,"");
        tstatus=sharedpreferences2.getString(status,"");
        tamount=sharedpreferences3.getString(amt,"");
        category=findViewById(R.id.cat_text);
       Log.i("amount tot: ",sharedpreferences1.getString(amt,""));
        tb1=findViewById(R.id.currency_field);
        tb2=findViewById(R.id.product_field);
        tb3=findViewById(R.id.qty_field);
        tb5=findViewById(R.id.amt_field);
        tb4=findViewById(R.id.rate_field);
        layout_info= findViewById(R.id.layout_info);
        tv_amount=findViewById(R.id.total_amount);
        tv_order=findViewById(R.id.order_text);
        tv_status=findViewById(R.id.status_text);

        if(OrderNo !=""){
            getJSON("https://www.orientexchange.in/bankagent/monthly-report-app.php",OrderNo);
            nDialog = new ProgressDialog(OrderHistory.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
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
            Intent i = new Intent(getApplicationContext(),ResetPassActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
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

    public void close_func(View view){
        Intent i =new Intent(getApplicationContext(),DailyReport.class);
        startActivity(i);
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
            @SuppressLint("ResourceAsColor")
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSONArray arr = null;
               // Toast.makeText(OrderHistory.this, s, Toast.LENGTH_SHORT).show();
                nDialog.hide();
                try {
                    if(s !=null && s!="") {
                        layout_info.setVisibility(View.INVISIBLE);
                        arr = new JSONArray(s);
                        Log.i("json array", arr.toString());
                        for (int j = 0; j < arr.length(); j++) {
                            JSONObject NewData = arr.getJSONObject(j);
                            String currency = NewData.getString("currency");
                            String product = NewData.getString("Product");
                            String qty = NewData.getString("Quantity");
                            String amount = NewData.getString("Amount");
                            DecimalFormat df = new DecimalFormat("#.##");
                            String cat=NewData.getString("own");
                            Float rate = parseFloat(NewData.getString("rate"));
                            rate= Float.valueOf(df.format(rate));
                            Log.i("Order Num =", currency);
                            if(cat.equals("yes")){category.setText("Own");}
                            else category.setText("Referal");
                            tv_order.setText(OrderNo);
                            tv_amount.setText(tamount);
                            tv_status.setText(tstatus);
                            tv1 = new TextView(OrderHistory.this);
                            LinearLayout.LayoutParams textview1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            textview1Params.setMargins(30, 15, 10, 15);
                            tv1.setLayoutParams(textview1Params);

                            tv5 = new TextView(OrderHistory.this);
                            LinearLayout.LayoutParams textview5Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            textview5Params.setMargins(20, 15, 10, 15);
                            tv5.setLayoutParams(textview5Params);

                            tv2 = new TextView(OrderHistory.this);
                            LinearLayout.LayoutParams textview2Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            textview2Params.setMargins(20, 15, 10, 15);
                            tv2.setLayoutParams(textview2Params);

                            tv3 = new TextView(OrderHistory.this);
                            LinearLayout.LayoutParams textview3Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            textview3Params.setMargins(20, 15, 10, 15);
                            tv3.setLayoutParams(textview3Params);

                            tv4 = new TextView(OrderHistory.this);
                            LinearLayout.LayoutParams textview4Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            textview4Params.setMargins(20, 15, 10, 15);
                            tv4.setLayoutParams(textview4Params);

                            //  row.addView(tv1);  row.addView(tv2);  row.addView(tv3);  row.addView(tv4);
                            //  tv1.setGravity(Gravity.CENTER);tv2.setGravity(Gravity.CENTER);tv3.setGravity(Gravity.CENTER);tv4.setGravity(Gravity.CENTER);
                            //   ll.addView(row,j);*/
                            tv1.setText(currency);
                            tv2.setText(product);
                            tv3.setText(qty);
                            tv4.setText(rate.toString());
                            tv5.setText(amount);
                            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
                            tv1.setTextColor(Color.BLACK);
                            tv5.setTextColor(Color.BLACK);

                            tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
                            tv2.setTextColor(Color.BLACK);
                            tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
                            tv3.setTextColor(Color.BLACK);
                            tv4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
                            tv4.setTextColor(Color.BLACK);
                            tv5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
                            // tv2.setBackgroundResource(R.drawable.currencytext);
                            LinearLayout layout = new LinearLayout(OrderHistory.this);
                            LinearLayout.LayoutParams lv = new TableRow.LayoutParams('0',
                                    LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                            layout.setLayoutParams(lv);
                            //  layout1.removeView(tv1); layout2.removeView(tv2); layout5.removeView(tv5); layout4.removeView(tv4);
                            tb1.addView(tv1);
                            tb2.addView(tv2);
                            tb3.addView(tv3);
                            tb4.addView(tv4);
                            tb5.addView(tv5);
                        }
                    }
                    else {
                        layout_info.setVisibility(View.VISIBLE);
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
                    String post_data = URLEncoder.encode("OrderNo","UTF-8")+"="+URLEncoder.encode(user_email,"UTF-8");
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
    public void alert_gst(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("GST information");
        builder.setMessage(" 0 to 25000 => \u20B9 45 \n 25001 to 100000 => 0.18% \n 100001 to 1000000 =>\u20B9 180+0.09% (of the amount exceeding \u20B9 1lakh) \n above 1000000 =>\u20B9 990+0.018% (of the amount exceeding \u20B9 10lakh) ");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // finish();
            }
        });
        builder.show();
    }
}
