package edu.northeastern.numad23sp_zhangjinpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

public class location extends AppCompatActivity {

//      button and textview
    private Button buttonReset;
    private TextView textLatitude;
    private TextView textLongitude;
    private TextView textTotalDistance;

//    thread
    private MyThread endThread; //thread
    private final Handler textHandler = new Handler(); //communicate for the main thread and the Backend thread

//    data
    private static double currentLatitude;
    private static double currentLongitude;
    private static double lastLatitude;
    private static double lastLongitude;
    private static double totalDistance;

//    get location changes
    private LocationRequest locationRequest;
    String locationProvider = null;
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

//        textview
        textLatitude = findViewById(R.id.text_latitude);
        textLongitude = findViewById(R.id.text_longitude);
        textTotalDistance = findViewById(R.id.text_distance);


//        button
        buttonReset = (Button) findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalDistance = 0;
            }
        });

//        实例化locationRequest，刷新记录的位置
        locationRequest = LocationRequest.create(); //创建监督位置改变的对象
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationProvider = LocationManager.GPS_PROVIDER;

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (!enableGPS()) {
                turnOnGPS();
            }
        } else {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        beginThread();

    }


    private boolean enableGPS() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(enableGPS()) {
                    getCurLocation();
                } else {
                    turnOnGPS();
                }
            }
        }

    }

//    turn on the GPS: 1.check the location setting request 2.
    private void turnOnGPS() {
//        1.添加创建位置信息的请求
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        builder.setAlwaysShow(true); //如果设备位置设置不满足应用程序的要求，则始终显示一个对话框，提示用户进行必要的更改
//        2.检查是否满足当前位置信息设置需求
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnCompleteListener(task1 -> {
            try{
                LocationSettingsResponse response = task1.getResult(ApiException.class);
                Toast.makeText(this, "GPS is already turned on.", Toast.LENGTH_SHORT).show();
            } catch (ApiException e){  //2 kind of exceptions
                if(e.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED){
                    try{
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        resolvableApiException.startResolutionForResult(this, 2);
                    } catch (IntentSender.SendIntentException ee){
                        ee.printStackTrace();
                    }
                }
//                else if (e.getStatusCode() == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE){
//                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                getCurLocation();
            }
        }
    }




    private void getCurLocation() {
//        1.turn on the permission
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return ;
        }

        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                new Thread(() -> {  //添加一个线程
                    LocationServices.getFusedLocationProviderClient(location.this).removeLocationUpdates(this);

                    if (locationResult.getLocations().size() != 0) {
                        int id = locationResult.getLocations().size() - 1;

                        lastLongitude = currentLongitude;
                        lastLatitude = currentLatitude;
                        currentLongitude = locationResult.getLocations().get(id).getLongitude();
                        currentLatitude = locationResult.getLocations().get(id).getLatitude();

                        if (lastLongitude != 0 && lastLatitude != 0) {
                            float[] cal = new float[1];
                            Location.distanceBetween(lastLongitude, lastLatitude, currentLongitude, currentLatitude, cal);
                            totalDistance += cal[0];
                        }
                    }
                }).start();
            }
        }, Looper.getMainLooper());
    }


    //    start the thread
    private void beginThread() {
        if(endThread == null){
            endThread = new MyThread();
            endThread.start();
        }
    }

    //    stop the thread
    private void endThread(){
        if(endThread != null){
            endThread.stopRun();
            endThread = null;
        }
    }



    private class MyThread extends Thread {
        private volatile boolean exit = false;
        private static final int UPDATE_UI_MESSAGE = 1;//speed up

        private Handler handler = new Handler(Looper.getMainLooper()){
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case UPDATE_UI_MESSAGE:
                        textLatitude.setText("Latitude: " + currentLatitude);
                        textLongitude.setText("Longitude: " + currentLongitude);
                        textTotalDistance.setText("Distance Traveled: " + String.format("%.2f", totalDistance) + "m");
                        break;
                }
            }
        };

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        public void run() {
            super.run();
            while(!exit){
                getCurLocation();
                handler.sendEmptyMessage(UPDATE_UI_MESSAGE);
            }
        }

        public void stopRun(){
            exit = true;
        }
    }

//    退出方法
    public void onBackPressed() {
        if (totalDistance == 0) {
            super.onBackPressed();
            finish();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Warning")
                    .setMessage("If you quit, the total distance will be cleaned.")
                    .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                        totalDistance = 0;
                        location.super.onBackPressed();
                        finish();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    @Override
    protected void onDestroy() {
        endThread();
        super.onDestroy();
    }

//    save the state when rotate the mobile
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble("current latitude", currentLatitude);
        outState.putDouble("current longitude", currentLongitude);
        outState.putDouble("last latitude", lastLatitude);
        outState.putDouble("last longitude", lastLongitude);
        outState.putDouble("total distance", totalDistance);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        currentLatitude = savedInstanceState.getDouble("current latitude");
        currentLongitude = savedInstanceState.getDouble("current longitude");
        lastLatitude = savedInstanceState.getDouble("last latitude");
        lastLongitude = savedInstanceState.getDouble("last longitude");
        totalDistance = savedInstanceState.getDouble("total distance");
    }


}




































