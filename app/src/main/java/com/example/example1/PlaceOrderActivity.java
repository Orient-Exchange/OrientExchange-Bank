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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.example1.MainActivity.Branch_add;
import static com.example.example1.MainActivity.Email;

public class PlaceOrderActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
String email;
String Branch_Add;
TableRow bt;
TableRow dt1;
TableRow dt2;
int Count=0;
ProgressBar p1;
    EditText cname,cnum,dadd,pincode;
    RadioButton branch,bank,door;
    TextView branch_id;
    String delivery_type="";
    ArrayList<String> carray = new ArrayList<String>();
    ArrayList<String> parray = new ArrayList<String>();
    ArrayList<String> qarray = new ArrayList<String>();
    ArrayList<String> rarray = new ArrayList<String>();
    ArrayList<String> tarray = new ArrayList<String>();
List<String> categories = new ArrayList<String>();
List<String> sellcash =new ArrayList<String>();
List<String> sellcard =new ArrayList<String>();
Float f1,f2;
List<String> product =new ArrayList<String>();
EditText [] edt= new EditText[5];
TableLayout [] tab = new TableLayout[5];
Spinner [] spinner1 = new Spinner[5];
Spinner [] spinner2 = new Spinner[5];
TextView [] rates=new TextView[5];
TextView [] amount=new TextView[5];
TextView totalAmount;
    ProgressDialog nDialog;
    Button button;
LinearLayout lc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, WireRateActivity.MODE_PRIVATE);
        email=sharedpreferences.getString(Email,"");

        SharedPreferences sharedpreferences1 = getSharedPreferences(MainActivity.MyPREFERENCES_BRANCH, WireRateActivity.MODE_PRIVATE);
        Branch_Add=sharedpreferences1.getString(Branch_add,"");

        isConnected(PlaceOrderActivity.this);
        if(!isConnected(PlaceOrderActivity.this)) buildDialog(PlaceOrderActivity.this).show();
        else {
            if(email == ""){
                Intent login =new Intent(getApplicationContext(),MainActivity.class);
                startActivity(login);
            }
            else { getJSON("https://www.orientexchange.in/bankagent/request_new.php",email);
                nDialog = new ProgressDialog(PlaceOrderActivity.this);
                nDialog.setMessage("Loading..");
                nDialog.setTitle("");
                nDialog.setIndeterminate(false);
                nDialog.setCancelable(true);
                nDialog.show();
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        button=findViewById(R.id.sub_order);
        totalAmount=findViewById(R.id.total_amount);
        bt=findViewById(R.id.branch_add);
        dadd=findViewById(R.id.door_add);
        pincode=findViewById(R.id.pincode);
        cname=findViewById(R.id.pname);
        cnum=findViewById(R.id.pnum);
        door=findViewById(R.id.door_select);
        bank=findViewById(R.id.bank_select);
        branch=findViewById(R.id.branch_select);
        dt1=findViewById(R.id.door_add1);
        //dt2=findViewById(R.id.door_add2);
        int maxLength = 10;
        cnum.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});

        p1=findViewById(R.id.progress1);
        branch_id=findViewById(R.id.branch_name);
        branch_id.setText("Branch Name : "+Branch_Add);
        spinner1[0]=findViewById(R.id.spiner_curreny1); spinner1[1]=findViewById(R.id.spiner_curreny2);
        spinner1[2]=findViewById(R.id.spiner_curreny3); spinner1[3]=findViewById(R.id.spiner_curreny4);
        spinner1[4]=findViewById(R.id.spiner_curreny5); spinner2[0]=findViewById(R.id.spinner_product1);
        spinner2[1]=findViewById(R.id.spinner_product2); spinner2[2]=findViewById(R.id.spinner_product3);
        spinner2[3]=findViewById(R.id.spinner_product4); spinner2[4]=findViewById(R.id.spinner_product5);
        edt[0]=findViewById(R.id.qty1);
        edt[1]=findViewById(R.id.qty2);
        edt[2]=findViewById(R.id.qty3);
        edt[3]=findViewById(R.id.qty4);
        edt[4]=findViewById(R.id.qty5);
        tab[0]=findViewById(R.id.pro_tab1);
        tab[1]=findViewById(R.id.pro_tab2);
        tab[2]=findViewById(R.id.pro_tab3);
        tab[3]=findViewById(R.id.pro_tab4);
        tab[4]=findViewById(R.id.pro_tab5);
        rates[0]=findViewById(R.id.rate1); amount[0]=findViewById(R.id.total1);
        rates[1]=findViewById(R.id.rate2); amount[1]=findViewById(R.id.total2);
        rates[2]=findViewById(R.id.rate3); amount[2]=findViewById(R.id.total3);
        rates[3]=findViewById(R.id.rate4); amount[3]=findViewById(R.id.total4);
        rates[4]=findViewById(R.id.rate5); amount[4]=findViewById(R.id.total5);

       /* mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mListViewName = (LinearLayout) findViewById(R.id.listViewName);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                getJSON("https://www.orientexchange.in/bankagent/request_new.php",email);
                nDialog = new ProgressDialog(PlaceOrderActivity.this);
                nDialog.setMessage("Loading..");
                nDialog.setTitle("");
                nDialog.setIndeterminate(false);
                nDialog.setCancelable(true);
                nDialog.show();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }); */

        lc=findViewById(R.id.customer_details);
        spinner1[0].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                edt[0].getText().clear();
                amount[0].setText("");
             if(position == 0){
                 product.remove("product");
                 product.remove("cash");
                 product.remove("card");
                 product.add("product");
                 Toast.makeText(PlaceOrderActivity.this, "Select Currency", Toast.LENGTH_SHORT).show();
             }
             else {
                 product.remove("cash");
                 product.remove("card");
                 product.remove("product");
                 f1=Float.parseFloat(sellcash.get(position-1));
                 f2=Float.parseFloat(sellcard.get(position-1));
                 Log.i("Cash value",String.valueOf(f1));
                 Log.i("Card value",String.valueOf(f2));
                 if(f1 == 0){product.remove("cash");}
                 else {product.add("cash");}
                 if(f2 == 0){product.remove("card");}
                 else product.add("card");
                 ArrayAdapter<String> dataAdapter_pro = new ArrayAdapter<String>(PlaceOrderActivity.this, android.R.layout.simple_spinner_item, product);
                 spinner2[0].setAdapter(dataAdapter_pro);
             }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner2[0].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                edt[0].getText().clear();
                amount[0].setText("");
                if(spinner2[0].getSelectedItem().toString().equals("cash")){
                    rates[0].setText(String.valueOf(f1));
                }
                else if(spinner2[0].getSelectedItem().toString().equals("card")){
                    rates[0].setText(String.valueOf(f2));
                }
                else {

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        edt[0].addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //here is your code
                float rate=0;
                if(!TextUtils.isEmpty(rates[0].getText().toString())) {
                    rate = Float.parseFloat(rates[0].getText().toString());
                }
                String quantity=edt[0].getText().toString();
                if(!TextUtils.isEmpty(quantity) ) {
                    int qty= Integer.parseInt(quantity);
                    float tot = rate * qty;
                    DecimalFormat df = new DecimalFormat("#.##");
                    amount[0].setText(String.valueOf(df.format(tot)));
                    Total_func();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Total_func();
            }
        });
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
        if(id == R.id.action_logout){
            logout_click();
        }
        if(id == R.id.reset_pass){
            Intent i= new Intent(getApplicationContext(),ResetPassActivity.class);
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
        } else if (id == R.id.nav_sporeder) {
            Intent i = new Intent(getApplicationContext(),Sell_placeActivity.class);
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

    public void door_func(View view){
        dt1.setVisibility(View.VISIBLE);
        //dt2.setVisibility(View.VISIBLE);
        bt.setVisibility(View.INVISIBLE);
    }

    public void branch_func(View view){
        dt1.setVisibility(View.INVISIBLE);
       // dt2.setVisibility(View.INVISIBLE);
        bt.setVisibility(View.VISIBLE);
    }

    public void bank_func(View view){
        dt1.setVisibility(View.INVISIBLE);
      //  dt2.setVisibility(View.INVISIBLE);
        bt.setVisibility(View.INVISIBLE);
    }

    public void own_func(View view){
        Intent i = new Intent(getApplicationContext(), OwnbuyActivity.class);
        startActivity(i);
    }
    public void referal_func(View view){
        lc.setVisibility(View.VISIBLE);
    }

    public void refresh(View view){
        getJSON("https://www.orientexchange.in/bankagent/request_new.php",email);
        nDialog = new ProgressDialog(PlaceOrderActivity.this);
        nDialog.setMessage("Loading..");
        nDialog.setTitle("");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();
    }

    public void Total_func(){
        float Tamount = (float) 0.0;
        for (int i=0;i<=Count;i++){
            if(!TextUtils.isEmpty(amount[Count].getText().toString())) {
                Tamount = Tamount + Float.parseFloat(amount[i].getText().toString());
                DecimalFormat df = new DecimalFormat("#.##");
                totalAmount.setText(String.valueOf(df.format(Tamount)));
            }
        }
    }

    public void add_more(View view){
        String edt_val= edt[Count].getText().toString();
        String curreny=spinner1[Count].getSelectedItem().toString();
        String pros=spinner2[Count].getSelectedItem().toString();
        if(Count < 4 && !TextUtils.isEmpty(edt_val) && Integer.parseInt(edt_val) !=0){
            if(!curreny.equals("Currency")) {
                if(pros.equals("cash") || pros.equals("card")) {
                    edt[Count].setEnabled(false);
                    spinner1[Count].setEnabled(false);
                    spinner2[Count].setEnabled(false);
                    Count++;
                    tab[Count].setVisibility(View.VISIBLE);
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(PlaceOrderActivity.this, android.R.layout.simple_spinner_item, categories);
                    spinner1[Count].setAdapter(dataAdapter);

                    spinner1[Count].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            edt[Count].getText().clear();
                            amount[Count].setText("");
                            if(position ==0 ){
                               /* product.remove("product");
                                product.remove("cash");
                                product.remove("card");
                                product.add("product"); */
                              Log.i("second feild","select");
                            }
                            else {
                                product.remove("cash");
                                product.remove("card");
                                product.remove("product");
                                f1=Float.parseFloat(sellcash.get(position-1));
                                f2=Float.parseFloat(sellcard.get(position-1));
                                Log.i("Cash value",String.valueOf(f1));
                                Log.i("Card value",String.valueOf(f2));
                                if(f1 == 0){product.remove("cash");}
                                else {product.add("cash");}
                                if(f2 == 0){product.remove("card");}
                                else product.add("card");
                                ArrayAdapter<String> dataAdapter_pro = new ArrayAdapter<String>(PlaceOrderActivity.this, android.R.layout.simple_spinner_item, product);
                                spinner2[Count].setAdapter(dataAdapter_pro);
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                    spinner2[Count].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            edt[Count].getText().clear();
                            amount[Count].setText("");
                            if(spinner2[Count].getSelectedItem().toString().equals("cash")){
                                rates[Count].setText(String.valueOf(f1));
                            }
                            else if(spinner2[Count].getSelectedItem().toString().equals("card")){
                                rates[Count].setText(String.valueOf(f2));
                            }
                            else {

                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                    edt[Count].addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            //here is your code
                            float rate=0;
                            if(!TextUtils.isEmpty(rates[Count].getText().toString())) {
                                rate = Float.parseFloat(rates[Count].getText().toString());
                            }
                            String quantity= edt[Count].getText().toString();
                            if(!TextUtils.isEmpty(quantity) && Integer.parseInt(quantity)!=0) {
                                int qty= Integer.parseInt(quantity);
                                float tot = rate * qty;
                                DecimalFormat df = new DecimalFormat("#.##");
                                amount[Count].setText(String.valueOf(df.format(tot)));
                                Total_func();
                            }
                        }
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            // TODO Auto-generated method stub
                        }
                        @Override
                        public void afterTextChanged(Editable s) {
                            // TODO Auto-generated method stub
                            Total_func();
                        }
                    });

                }else {
                    ((TextView)spinner2[Count].getSelectedView()).setError("Select Product");
                }
            } else {
                ((TextView)spinner1[Count].getSelectedView()).setError("Select Currency");
            }
        } else {
            if (Count < 4) {
                edt[Count].setError("Please Enter Valid Qty");
            }
            else {
                Toast.makeText(this, "Maximum limit is 5", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void remove_product(View view){
        if(Count >0){
            Log.i(" Add Count", String.valueOf(Count));
            tab[Count].setVisibility(View.INVISIBLE);
            Count --;
            edt[Count].setEnabled(true);
            spinner1[Count].setEnabled(true);
            spinner2[Count].setEnabled(true);
            Total_func();
        }
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
              //  Toast.makeText(PlaceOrderActivity.this, s, Toast.LENGTH_SHORT).show();
                categories.add("Currency");
                if(s !=null && s!="") {
                    nDialog.hide();
                    try {
                        arr = new JSONArray(s);
                        for (int j = 0; j < arr.length(); j++) {
                            JSONObject NewData = arr.getJSONObject(j);
                            String Currency = NewData.getString("currency");
                            categories.add(Currency);
                            String sell_cash = NewData.getString("sellcash");
                            String sell_card = NewData.getString("sellcard");
                            sellcash.add(sell_cash);
                            sellcard.add(sell_card);
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(PlaceOrderActivity.this, android.R.layout.simple_spinner_item, categories);
                        spinner1[0].setAdapter(dataAdapter);
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


    public boolean check_radiogroup(){
        String door_add=dadd.getText().toString();
        String pin=pincode.getText().toString();
        String Branch=branch_id.getText().toString();
        String num=cnum.getText().toString();
        Boolean Rtest=false;
        if(door.isChecked()){
            if(!num.isEmpty() && num.length() == 10){
                Rtest=true;
                delivery_type="door";
            }
            else {
                cnum.setError("Enter contact number");
                Rtest=false;
            }
        }
        else if(bank.isChecked()){
            Rtest=true;
            delivery_type="bank";
        }else if(branch.isChecked()){
            if(!Branch.isEmpty()){
                if(!num.isEmpty() && num.length() == 10){
                    Rtest=true;
                    delivery_type="branch";
                }
                else {
                    cnum.setError("Enter contact number");
                    Rtest=false;
                }
            }else{Rtest=false;}
        }else{
            Rtest=false;
        }
        return Rtest;
    }

    public void submit_func(View view){
       Log.i("Count val",String.valueOf(Count));
        String edt_val= edt[Count].getText().toString();
        String curreny=spinner1[Count].getSelectedItem().toString();
        String pros=spinner2[Count].getSelectedItem().toString();
        final String name=cname.getText().toString();
        final String num=cnum.getText().toString();
        final String Total_Amount=totalAmount.getText().toString();
        final String door_add=dadd.getText().toString();
        final String pin=pincode.getText().toString();
        final String Branch=branch_id.getText().toString();
        for(int i=0;i<=Count;i++) {
            carray.add(spinner1[i].getSelectedItem().toString());
            parray.add(spinner2[i].getSelectedItem().toString());
            qarray.add(edt[i].getText().toString());
            rarray.add(rates[i].getText().toString());
            tarray.add(amount[i].getText().toString());
        }
        //if(!TextUtils.isEmpty(edt_val) && Integer.parseInt(edt_val)!=0){
          //  if(!curreny.equals("Currency")) {
               // if(pros.equals("cash") || pros.equals("card")) {
                    if(!name.isEmpty()){
                            Boolean RR=check_radiogroup();
                            if(RR){
                                AlertDialog.Builder alert = new AlertDialog.Builder(PlaceOrderActivity.this);
                                alert.setTitle("");
                                alert.setMessage("Service charges of \u20B9 50/- and GST will be applied on final invoice \n GST slab : \n  0 to 25000 => \u20B9 45 \n 25001 to 100000 => 0.18% \n 100001 to 1000000 =>\u20B9 180+0.09% (of the amount exceeding \u20B9 1lakh) \n above 1000000 =>\u20B9 990+0.018% (of the amount exceeding \u20B9 10lakh)  ");
                                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        p1.setVisibility(View.VISIBLE);
                                        button.setBackground(ContextCompat.getDrawable(PlaceOrderActivity.this, R.drawable.login_2));
                                        button.setText("Processing .....");
                                        getresult("https://www.orientexchange.in/bankagent/rbuycash.php",email,carray,parray,qarray,tarray,rarray,name,num,Total_Amount,door_add,pin,Branch,delivery_type);
                                    }
                                });
                              /*  alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        // Canceled.

                                    }
                                }); */
                                alert.show();
                            }else{
                                AlertDialog alertDialog = new AlertDialog.Builder(PlaceOrderActivity.this).create();
                                alertDialog.setTitle("Login Status");
                                alertDialog.setMessage("Please Provide Customer details!"); alertDialog.show();
                            }
                    }else {
                        cname.setError("Enter customer name");
                    }
               /* }else {
                    ((TextView)spinner2[Count].getSelectedView()).setError("Select Product");
                }
            }else {
                ((TextView)spinner1[Count].getSelectedView()).setError("Select Product");
            }
        }else {
            edt[Count].setError("Enter Quantity");
        } */
    }

    //this method is actually fetching the json string
    private void getresult(final String urlWebService, final String user_email, final ArrayList<String> ccode, final ArrayList<String> ppro, final ArrayList<String> qnt, final ArrayList<String> inr, final ArrayList<String> crate, final String custname, final String custnum, final String TotalInr, final String dooraddress, final String cpincode, final String branch_name, final String Delivery) {
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
                if(!s.isEmpty()) {
                    Toast.makeText(PlaceOrderActivity.this, s, Toast.LENGTH_SHORT).show();
                    Log.i("Response",s);
                    p1.setVisibility(View.INVISIBLE);
                    button.setBackground(ContextCompat.getDrawable(PlaceOrderActivity.this, R.drawable.sellcashtext));
                    button.setText("Submit");
                    if(s.equals("ok")){
                        Intent i = new Intent(getApplicationContext(),ThankyouActivity.class);
                        startActivity(i);
                    }
                    else if(s.equals("0")){
                        AlertDialog.Builder alert = new AlertDialog.Builder(PlaceOrderActivity.this);
                        alert.setTitle("");
                        alert.setMessage("You can Only place order between 10:15am to 5:30pm ");
                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent i = new Intent(getApplicationContext(),Sell_placeActivity.class);
                                startActivity(i);
                            }
                        });
                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Canceled.
                                Intent i = new Intent(getApplicationContext(),Sell_placeActivity.class);
                                startActivity(i);
                            }
                        });
                        alert.show();
                    }
                    else if(s.equals("2")){
                        AlertDialog.Builder alert = new AlertDialog.Builder(PlaceOrderActivity.this);
                        alert.setTitle("");
                        alert.setMessage("You can Only place order between 10:15am to 12:30pm ");
                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent i = new Intent(getApplicationContext(),Sell_placeActivity.class);
                                startActivity(i);
                            }
                        });
                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Canceled.
                                Intent i = new Intent(getApplicationContext(),Sell_placeActivity.class);
                                startActivity(i);
                            }
                        });
                        alert.show();
                    }
                    else if(s.equals("3")){
                        AlertDialog.Builder alert = new AlertDialog.Builder(PlaceOrderActivity.this);
                        alert.setTitle("");
                        alert.setMessage("Oops! You cant place order on sunday ");
                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent i = new Intent(getApplicationContext(),Sell_placeActivity.class);
                                startActivity(i);
                            }
                        });
                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Canceled.
                                Intent i = new Intent(getApplicationContext(),Sell_placeActivity.class);
                                startActivity(i);
                            }
                        });
                        alert.show();
                    }
                    else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(PlaceOrderActivity.this);
                        alert.setTitle("");
                        alert.setMessage("Error in Placing Order!");
                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent i = new Intent(getApplicationContext(),Sell_placeActivity.class);
                                startActivity(i);
                            }
                        });
                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Canceled.
                                Intent i = new Intent(getApplicationContext(),Sell_placeActivity.class);
                                startActivity(i);
                            }
                        });
                        alert.show();
                        new Timer().scheduleAtFixedRate(new TimerTask(){
                            @Override
                            public void run(){

                            }
                        },0,5000);

                    }
                }
                else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(PlaceOrderActivity.this);
                    alert.setTitle("");
                    alert.setMessage("Error in Placing Order!");
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent i = new Intent(getApplicationContext(),Sell_placeActivity.class);
                            startActivity(i);
                        }
                    });
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                            Intent i = new Intent(getApplicationContext(),Sell_placeActivity.class);
                            startActivity(i);
                        }
                    });
                    alert.show();
                    new Timer().scheduleAtFixedRate(new TimerTask(){
                        @Override
                        public void run(){

                        }
                    },0,5000);
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
                    JSONArray array1 = new JSONArray();
                    JSONArray array2 = new JSONArray();
                    JSONArray array3 = new JSONArray();
                    JSONArray array4 = new JSONArray();
                    JSONArray array5 = new JSONArray();
                    for (int i = 0; i < ccode.size(); i++) {
                        array1.put(ccode.get(i));
                    }
                    for (int i = 0; i < ppro.size(); i++) {
                        array2.put(ppro.get(i));
                    }
                    for (int i = 0; i < qnt.size(); i++) {
                        array3.put(qnt.get(i));
                    }
                    for (int i = 0; i < crate.size(); i++) {
                        array4.put(crate.get(i));
                    }
                    for (int i = 0; i < inr.size(); i++) {
                        array5.put(inr.get(i));
                    }

                    JSONObject obj1 = new JSONObject();
                    JSONObject obj2 = new JSONObject();
                    JSONObject obj3 = new JSONObject();
                    JSONObject obj4 = new JSONObject();
                    JSONObject obj5 = new JSONObject();
                    try {
                        obj1.put("currency", array1);
                        obj2.put("product", array2);
                        obj3.put("quantity", array3);
                        obj4.put("rates", array4);
                        obj5.put("amount", array5);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("user_email","UTF-8")+"="+URLEncoder.encode(user_email,"UTF-8")+"&"
                            +URLEncoder.encode("address","UTF-8")+"="+URLEncoder.encode(dooraddress,"UTF-8")+"&"
                            +URLEncoder.encode("pin","UTF-8")+"="+URLEncoder.encode(cpincode,"UTF-8")+"&"
                            +URLEncoder.encode("bid","UTF-8")+"="+URLEncoder.encode(branch_name,"UTF-8")+"&"
                            +URLEncoder.encode("total","UTF-8")+"="+URLEncoder.encode(TotalInr,"UTF-8")+"&"
                            +URLEncoder.encode("passname","UTF-8")+"="+URLEncoder.encode(custname,"UTF-8")+"&"
                            +URLEncoder.encode("passnum","UTF-8")+"="+URLEncoder.encode(custnum,"UTF-8")+"&"
                            +URLEncoder.encode("type","UTF-8")+"="+URLEncoder.encode(Delivery,"UTF-8")+"&"
                            +URLEncoder.encode("sellcurrency","UTF-8")+"="+URLEncoder.encode(obj1.toString(),"UTF-8") +"&"
                            +URLEncoder.encode("sellproduct","UTF-8")+"="+URLEncoder.encode(obj2.toString(),"UTF-8") +"&"
                            +URLEncoder.encode("sellqty","UTF-8")+"="+URLEncoder.encode(obj3.toString(),"UTF-8") +"&"
                            +URLEncoder.encode("coveredrate","UTF-8")+"="+URLEncoder.encode(obj4.toString(),"UTF-8") +"&"
                            +URLEncoder.encode("inr","UTF-8")+"="+URLEncoder.encode(obj5.toString(),"UTF-8");
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

    public void call_func(){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:+91-8884734422"));
        startActivity(callIntent);
    }
}
