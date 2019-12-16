package com.example.example1;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.example1.MainActivity.Email;
public class WireOrderActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String email;
    Spinner sp1,sp2;
    List<String> categories = new ArrayList<String>();
    List<String> wiretrans =new ArrayList<String>();
    Float f1;
    List<String> product =new ArrayList<String>();
    TextView rates;
    ProgressDialog nDialog;
    ProgressBar p1;
    EditText ed1,ed2,ed3,purp;
    TextView tot;
    int sppos=0;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wire_order);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        sp1=findViewById(R.id.spiner_curreny1);
        sp2=findViewById(R.id.spinner_product1);
        rates=findViewById(R.id.rate1);
        ed1=findViewById(R.id.qty1);
        ed2=findViewById(R.id.cname);
        ed3=findViewById(R.id.cnum);
        tot=findViewById(R.id.total_amount);
        p1=findViewById(R.id.progress1);
        purp=findViewById(R.id.purpose);
        button=findViewById(R.id.sub_order);
        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, PlaceOrderActivity.MODE_PRIVATE);
        email=sharedpreferences.getString(Email,"");
        if(email == ""){
            Intent login =new Intent(getApplicationContext(),MainActivity.class);
            startActivity(login);
        }
        else { getJSON("https://www.orientexchange.in/bankagent/request_new.php",email);}

        nDialog = new ProgressDialog(WireOrderActivity.this);
        nDialog.setMessage("Loading..");
        nDialog.setTitle("");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();

        ed1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //here is your code
                float rate= 0;
                if(!TextUtils.isEmpty(rates.getText().toString())){
                    rate=Float.parseFloat(rates.getText().toString());
                }
                String quantity=ed1.getText().toString();
                if(!TextUtils.isEmpty(quantity) ) {
                    int qty= Integer.parseInt(quantity);
                    float amount = rate * qty;
                    DecimalFormat df = new DecimalFormat("#.##");
                    tot.setText(String.valueOf(df.format(amount)));
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ed1.getText().clear();
                tot.setText("");
                sppos=position;
                if(position == 0){
                    product.remove("product");
                    product.remove("WireTransfer");
                    product.add("product");
                   // Toast.makeText(WireOrderActivity.this, "Select Currency", Toast.LENGTH_SHORT).show();
                }
                else {
                    product.remove("WireTransfer");
                    product.remove("product");
                    f1=Float.parseFloat(wiretrans.get(position-1));
                    Log.i("Cash value",String.valueOf(f1));
                    if(f1 == 0){product.remove("WireTransfer"); product.add("product");}
                    else {product.add("WireTransfer");}
                    ArrayAdapter<String> dataAdapter_pro = new ArrayAdapter<String>(WireOrderActivity.this, android.R.layout.simple_spinner_item, product);
                    sp2.setAdapter(dataAdapter_pro);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sppos=0;
            }
        });

       sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ed1.getText().clear();
                tot.setText("");
                if(sp2.getSelectedItem().toString() == "WireTransfer"){
                    rates.setText(String.valueOf(f1));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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
            Intent i=new Intent(getApplicationContext(),ResetPassActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")

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

        }
        else if(id == R.id.nav_sporeder){
            Intent i= new Intent(getApplicationContext(),Sell_placeActivity.class);
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
                categories.add("Currency");
                if (s != "") {
                    nDialog.hide();
                    try {
                        arr = new JSONArray(s);
                        for (int j = 0; j < arr.length() - 1; j++) {
                            JSONObject NewData = arr.getJSONObject(j);
                            String Currency = NewData.getString("currency");
                            categories.add(Currency);
                            String remittance = NewData.getString("WiretransferSell");
                            wiretrans.add(remittance);
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(WireOrderActivity.this, android.R.layout.simple_spinner_item, categories);
                        sp1.setAdapter(dataAdapter);
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

    //this method is actually fetching the json string
    private void getresult(final String urlWebService,final String user_email,final String ccode,final String lrate,final String qnt,final String amt,final String pname,final String pnum,final  String send_purpose) {
        /*
         * As fetching the json string is a network operation
         * And we cannot perform a network operation in main thread
         * so we need an AsyncTask
         * The constrains defined here are
         * Void -> We are not passing anything
         * Void -> Nothing at progress update as well
         * String -> After completion it should return a string and it will be the json string
         * */
        class GetResult extends AsyncTask<String, Void, String> {

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
                if (s != "" && s!=null) {
                    Toast.makeText(WireOrderActivity.this, s, Toast.LENGTH_SHORT).show();
                    p1.setVisibility(View.INVISIBLE);
                    button.setBackground(ContextCompat.getDrawable(WireOrderActivity.this, R.drawable.sellcashtext));
                    button.setText("Submit");
                    ed1.getText().clear();
                    ed2.getText().clear();
                    ed3.getText().clear();
                    tot.setText("");
                    rates.setText("");
                    sp2.setAdapter(null);
                    sp1.setAdapter(null);
                   // Toast.makeText(WireOrderActivity.this, s, Toast.LENGTH_SHORT).show();
                    // try {
                    //JSONObject json=new JSONObject(s);
                    //String status=json.getString("status");
                    if (s.equals("ok")) {
                        Intent i = new Intent(getApplicationContext(), ThankyouActivity.class);
                        startActivity(i);
                    } else if (s.equals("0")) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(WireOrderActivity.this);
                        alert.setTitle("");
                        alert.setMessage("You can Only place order between 10:15am to 5:30pm ");
                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent i = new Intent(getApplicationContext(), Sell_placeActivity.class);
                                startActivity(i);
                            }
                        });
                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Canceled.
                                Intent i = new Intent(getApplicationContext(), Sell_placeActivity.class);
                                startActivity(i);
                            }
                        });
                        alert.show();
                    } else if (s.equals("2")) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(WireOrderActivity.this);
                        alert.setTitle("");
                        alert.setMessage("You can Only place order between 10:15am to 12:30pm ");
                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent i = new Intent(getApplicationContext(), Sell_placeActivity.class);
                                startActivity(i);
                            }
                        });
                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Canceled.
                                Intent i = new Intent(getApplicationContext(), Sell_placeActivity.class);
                                startActivity(i);
                            }
                        });
                        alert.show();
                    } else if (s.equals("3")) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(WireOrderActivity.this);
                        alert.setTitle("");
                        alert.setMessage("Oops! You cant place order on sunday ");
                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent i = new Intent(getApplicationContext(), Sell_placeActivity.class);
                                startActivity(i);
                            }
                        });
                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Canceled.
                                Intent i = new Intent(getApplicationContext(), Sell_placeActivity.class);
                                startActivity(i);
                            }
                        });
                        alert.show();
                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(WireOrderActivity.this);
                        alert.setTitle("");
                        alert.setMessage("Error in Placing Order!");
                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent i = new Intent(getApplicationContext(), Sell_placeActivity.class);
                                startActivity(i);
                            }
                        });
                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Canceled.
                                Intent i = new Intent(getApplicationContext(), Sell_placeActivity.class);
                                startActivity(i);
                            }
                        });
                        alert.show();

                        getJSON("https://www.orientexchange.in/bankagent/rateresult.php", email);

                        nDialog = new ProgressDialog(WireOrderActivity.this);
                        nDialog.setMessage("Loading..");
                        nDialog.setTitle("");
                        nDialog.setIndeterminate(false);
                        nDialog.setCancelable(true);
                        nDialog.show();
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
                    String post_data = URLEncoder.encode("user_email","UTF-8")+"="+URLEncoder.encode(user_email,"UTF-8")+"&"
                            +URLEncoder.encode("ccode","UTF-8")+"="+URLEncoder.encode(ccode,"UTF-8")+"&"
                            +URLEncoder.encode("rate","UTF-8")+"="+URLEncoder.encode(lrate,"UTF-8")+"&"
                            +URLEncoder.encode("qnt","UTF-8")+"="+URLEncoder.encode(qnt,"UTF-8")+"&"
                            +URLEncoder.encode("amount","UTF-8")+"="+URLEncoder.encode(amt,"UTF-8")+"&"
                            +URLEncoder.encode("pname","UTF-8")+"="+URLEncoder.encode(pname,"UTF-8")+"&"
                            +URLEncoder.encode("pnum","UTF-8")+"="+URLEncoder.encode(pnum,"UTF-8")+"&"
                            +URLEncoder.encode("purpose","UTF-8")+"="+URLEncoder.encode(send_purpose,"UTF-8");
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
        GetResult getresult = new GetResult();
        getresult.execute();
    }

    public void sub_wire(View view){
        String quantity=ed1.getText().toString();
        String cname=ed2.getText().toString();
        String cnum=ed3.getText().toString();
        String curreny=sp1.getSelectedItem().toString();
        String total=tot.getText().toString();
        String purpose=purp.getText().toString();
        String lr=rates.getText().toString();
        if(sppos != 0 && !TextUtils.isEmpty(lr)){
            if(!TextUtils.isEmpty(quantity)){
                if(!TextUtils.isEmpty(purpose)) {
                    if (!TextUtils.isEmpty(cname)) {
                        if (!TextUtils.isEmpty(cnum) && cnum.length() == 10) {
                            p1.setVisibility(View.VISIBLE);
                            button.setBackground(ContextCompat.getDrawable(WireOrderActivity.this, R.drawable.login_2));
                            button.setText("Processing .....");
                            getresult("https://www.orientexchange.in/bankagent/wireorder.php", email, curreny, lr, quantity, total, cname, cnum,purpose);
                        } else {
                            ed3.setError("Enter Customer Number");
                        }
                    } else {
                        ed2.setError("Enter Customer Name");
                    }
                }else {
                    purp.setError("Provide purpose");
                }
            }
            else {
                ed1.setError("Enter quantity");
            }
        }else {
            ((TextView)sp1.getSelectedView()).setError("Select valid Currency code");
        }
    }

    public void call_func(){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:+91-8884734422"));
        startActivity(callIntent);
    }
}
