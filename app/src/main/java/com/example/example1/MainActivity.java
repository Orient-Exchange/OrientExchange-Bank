package com.example.example1;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String TempName, TempPass,TempBtn ;
    EditText name,pass;
    Button button;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Email = "";
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES_BRANCH = "MyPrefs_branch" ;
    public static final String Branch_add = "";
    SharedPreferences sharedpreferences1;
    private ProgressDialog mProgress;
    ProgressBar p1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        button = findViewById(R.id.login);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},200);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},200);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedpreferences1=getSharedPreferences(MyPREFERENCES_BRANCH, Context.MODE_PRIVATE);
        isConnected(MainActivity.this);
        if(!isConnected(MainActivity.this)) buildDialog(MainActivity.this).show();
        mProgress =new ProgressDialog(this);
        String titleId="Signing in...";
        mProgress.setTitle(titleId);
        mProgress.setMessage("Please Wait...");

        p1=findViewById(R.id.progress1);
        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, MainActivity.MODE_PRIVATE);
        String email=sharedpreferences.getString(Email,"");
        if(email != ""){
            Intent login =new Intent(getApplicationContext(),RateActivity.class);
            startActivity(login);
        }
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
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }
        return false;
        // Disable back button..............
    }
    public void login_click(View v){
        isConnected(MainActivity.this);
        if(!isConnected(MainActivity.this)) buildDialog(MainActivity.this).show();
        else {
            TempBtn = "clicked";
            if (!isConnected(MainActivity.this)) buildDialog(MainActivity.this).show();
            else {
                GetData();
                if (!TempName.trim().equalsIgnoreCase("")) {
                    if (!TempPass.trim().equalsIgnoreCase("")) {
                        //Toast.makeText(this, TempName, Toast.LENGTH_SHORT).show();
                        InsertData(TempName, TempPass, TempBtn);
                        p1.setVisibility(View.VISIBLE);
                        button.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.login_2));
                        button.setText("Signing in.....");
                        pass.setError(null);
                    } else {
                        pass.setError("This field can not be blank");
                    }
                    name.setError(null);
                } else {
                    name.setError("This field can not be blank");
                }
            }
        }
    }

    public void logout_func(View view){
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

    public void GetData(){
        TempName = name.getText().toString();
        TempPass = pass.getText().toString();
    }

    public void call_func(View view){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:+91-8884734422"));
        startActivity(callIntent);
    }

    public void InsertData(final String username, final String userpass, final String btn){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            AlertDialog alertDialog;
            String regex = "\\s++$";
            @Override
            protected String doInBackground(String... params) {
                String login_url = "https://www.orientexchange.in/bankagent/login_process.php";
                    try {
                        String user_name = params[0];
                        String password = params[1];
                        String btn_login= params[2];
                        StringBuilder sb = new StringBuilder();
                        URL url = new URL(login_url);
                        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setDoInput(true);
                        OutputStream outputStream = httpURLConnection.getOutputStream();
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                        String post_data = URLEncoder.encode("user_email","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                                +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"
                                +URLEncoder.encode("btn-login","UTF-8")+"="+URLEncoder.encode(btn_login,"UTF-8");
                        bufferedWriter.write(post_data);
                        bufferedWriter.flush();
                        bufferedWriter.close();
                        outputStream.close();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                        String json;
                        //reading until we don't find null
                        while ((json = bufferedReader.readLine()) != null) {
                            //appending it to string builder
                            sb.append(json + "\n");
                        }
                        bufferedReader.close();
                        inputStream.close();
                        httpURLConnection.disconnect();

                        return sb.toString().trim();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                return null;
            }

            @Override
            protected void onPreExecute() {
                alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Login Status");
            }

            @Override
            protected void onPostExecute(String result) {
                if(result != null && !result.isEmpty()){
                    JSONArray arr = null;
                    //Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                    Log.i("Response",result);
                    try {
                        JSONObject json=new JSONObject(result);
                        String status=json.getString("status");
                        if(status.equals("success")) {
                            String BranchEmail = json.getString("branch");
                            Log.i("Branch Email",BranchEmail);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Email, username);
                            editor.commit();
                            SharedPreferences.Editor editor1=sharedpreferences1.edit();
                            editor1.putString(Branch_add,BranchEmail);
                            editor1.commit();
                            //  mProgress.hide();
                            Intent i = new Intent(getApplicationContext(), DashBoardActivity.class);
                            startActivity(i);
                        }else{
                            alertDialog.setMessage("User or password is wrong"); alertDialog.show();
                            p1.setVisibility(View.INVISIBLE);
                            button.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sellcashtext));
                            button.setText("Login");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    alertDialog.setMessage("User or password is wrong"); alertDialog.show();
                    p1.setVisibility(View.INVISIBLE);
                    button.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sellcashtext));
                    button.setText("Login");
                }
            }
            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(username, userpass, btn);
    }
    public void resend_reg(View view){
        isConnected(MainActivity.this);
        if(!isConnected(MainActivity.this)) buildDialog(MainActivity.this).show();
        else {
            Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.orientexchange.in/bank/registration.php"));
            startActivity(viewIntent);
        }
    }
}
