package com.example.family112;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;

import java.util.ArrayList;

public class BasicMapActivity extends AppCompatActivity {

    MapView mapView;
    AMap aMap;
    ArrayList<StudentInfo> studentInfos;

    private ImportXlsxService.ImportXlsxBinder importXlsxBinder;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            importXlsxBinder = (ImportXlsxService.ImportXlsxBinder) iBinder;
            studentInfos = importXlsxBinder.readXlsx();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_map);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();

        initMarkers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private void initMarkers(){
        startService(new Intent(this, ImportXlsxService.class));
        bindService(new Intent(this, ImportXlsxService.class),serviceConnection, BIND_AUTO_CREATE);
    }
}