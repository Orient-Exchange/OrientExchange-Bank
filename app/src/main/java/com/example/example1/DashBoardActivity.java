package com.example.example1;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.example1.MainActivity.Email;
import static android.Manifest.permission.CALL_PHONE;
import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

public class DashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int MY_REQUEST_CODE = 0;
    String connet="false";
    String email;
    AppUpdateManager appUpdateManager ;
    Task<AppUpdateInfo> appUpdateInfoTask ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        isConnected(DashBoardActivity.this);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},200);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},200);
        if(!isConnected(DashBoardActivity.this)) buildDialog(DashBoardActivity.this).show();
        else {
            SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, RateActivity.MODE_PRIVATE);
            email=sharedpreferences.getString(Email,"");
            if(email == ""){
                Intent login =new Intent(getApplicationContext(),MainActivity.class);
                startActivity(login);
            }
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(DashBoardActivity.this);

        appUpdateManager = AppUpdateManagerFactory.create(this);

        // Returns an intent object that you use to check for an update.
        appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // For a flexible update, use AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            IMMEDIATE,
                            // The current activity making the update request.
                            this,
                            // Include a request code to later monitor this update request.
                            MY_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Log.i("Update flow failed:" ,String.valueOf( resultCode));
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }
    }

    // Checks that the update is not stalled during 'onResume()'.
// However, you should execute this check at all entry points into the app.
    @Override
    protected void onResume() {
        super.onResume();

        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                // If an in-app update is already running, resume the update.
                                try {

                                    appUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo,
                                            IMMEDIATE,
                                            this,
                                            MY_REQUEST_CODE);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
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
            Intent i = new Intent(getApplicationContext(),ResetPassActivity.class);
            startActivity(i);
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
        } else if (id == R.id.nav_logout) {
            logout_click();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("");
            builder.setMessage("Are you sure want to Exit?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog
                    dialog.dismiss();
                    Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                    homeIntent.addCategory( Intent.CATEGORY_HOME );
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
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
        return false;
        // Disable back button..............
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

    public void rate_func(View view){
        isConnected(DashBoardActivity.this);
        if(!isConnected(DashBoardActivity.this)) buildDialog(DashBoardActivity.this).show();
        else {
            Intent i = new Intent(getApplicationContext(), RateActivity.class);
            startActivity(i);
        }
    }

    public void buyorder_func(View view){
        isConnected(DashBoardActivity.this);
        if(!isConnected(DashBoardActivity.this)) buildDialog(DashBoardActivity.this).show();
        else {
            Intent i = new Intent(getApplicationContext(), PlaceOrderActivity.class);
            startActivity(i);
        }
    }
    public void sellOrder_func(View view){
        isConnected(DashBoardActivity.this);
        if(!isConnected(DashBoardActivity.this)) buildDialog(DashBoardActivity.this).show();
        else {
            Intent i = new Intent(getApplicationContext(), Sell_placeActivity.class);
            startActivity(i);
        }
    }
    public void wire_func(View view){
        isConnected(DashBoardActivity.this);
        if(!isConnected(DashBoardActivity.this)) buildDialog(DashBoardActivity.this).show();
        else {
            Intent i = new Intent(getApplicationContext(), WireOrderActivity.class);
            startActivity(i);
        }
    }

    public void call_func(){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:8884734422"));
        startActivity(callIntent);
    }
}
