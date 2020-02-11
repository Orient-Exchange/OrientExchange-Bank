package com.example.example1;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;

import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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

import static com.example.example1.MainActivity.Email;

public class ThankyouActivity extends AppCompatActivity {
    TextView t1;
    String email;
    TableRow tb1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, RateActivity.MODE_PRIVATE);
        email=sharedpreferences.getString(Email,"");
        t1=findViewById(R.id.dis_msg);
        tb1=findViewById(R.id.dis_btn);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(),DashBoardActivity.class);
                //startActivity(i);
            }
        }, 5000);

        getJSON("https://www.orientexchange.in/bankagent/get_dis.php",email);
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

    public void order_func(View view){
        Intent i = new Intent(getApplicationContext(),DailyReport.class);
        startActivity(i);
    }

    public void Review(View view){
        Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.OrientExchange.example1"));
        startActivity(viewIntent);
    }

    public void mail_send(View view){
        InsertData("https://www.orientexchange.in/bank/send_mail", email);

    }
    public void disable_msg(View view){
        t1.setVisibility(View.INVISIBLE);
        tb1.setVisibility(View.INVISIBLE);
        InsertData("https://www.orientexchange.in/bank/callback_disable", email);

    }

    public void InsertData(final String login_url, final String username){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            AlertDialog alertDialog;
            String regex = "\\s++$";
            @Override
            protected String doInBackground(String... params) {

                try {
                    String user_name = params[1];
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("email","UTF-8")+"="+ URLEncoder.encode(user_name,"UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result="";
                    String line = bufferedReader.readLine();
                    if(line!= null){
                        result = line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    String res=result.trim();
                    return res;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected void onPostExecute(String result) {
                if(result.equals("sent")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ThankyouActivity.this);
                    builder.setTitle("");
                    builder.setMessage("Thank you!Our Customer Service Executive will call you soon.");

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // finish();
                        }
                    });
                    builder.show();
                }else if(result.equals("1")){
                    t1.setVisibility(View.INVISIBLE);
                    tb1.setVisibility(View.INVISIBLE);
                }
                else {}
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(login_url,username);
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
                if(s.equals("NO")) {
                    t1.setVisibility(View.INVISIBLE);
                    tb1.setVisibility(View.INVISIBLE);
                }
                else {
                    t1.setVisibility(View.VISIBLE);
                    tb1.setVisibility(View.VISIBLE);
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

}
