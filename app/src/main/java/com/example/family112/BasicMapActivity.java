package com.example.family112;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;


import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import java.util.ArrayList;

public class BasicMapActivity extends AppCompatActivity {

    MapView mapView;
    AMap aMap;
    UiSettings uiSettings;
    ArrayList<StudentInfo> studentInfos;
    ArrayList<Marker> markers;

    private ImportXlsxService.ImportXlsxBinder importXlsxBinder;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            importXlsxBinder = (ImportXlsxService.ImportXlsxBinder) iBinder;
            studentInfos = importXlsxBinder.readXlsx();
            markers = drawMarkers();
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
        uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(5));

        initMarkers();

        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                if(cameraPosition.zoom >= 12){
                    for(int i = 0; i < markers.size(); i++){
//                        markers.set(i, aMap.addMarker(new MarkerOptions().position(studentInfos.get(i).getLatLng()).title(studentInfos.get(i).getName())));
                            markers.get(i).setTitle(studentInfos.get(i).getName());
                        Log.i("BasicMapActivity -> Detail",cameraPosition.toString());
                    }
                }
                else{
                    for(int i = 0; i < markers.size(); i++){
                        markers.get(i).setTitle("");
                        Log.i("BasicMapActivity",cameraPosition.toString());
                    }
                }
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {

            }
        });
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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
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

    private ArrayList<Marker> drawMarkers(){
        ArrayList<Marker> markers = new ArrayList<>();
        for (StudentInfo info: studentInfos) {
            Marker marker = aMap.addMarker(
                    new MarkerOptions().position(info.getLatLng()).title(""));
            marker.setInfoWindowEnable(false);
            markers.add(marker);
        }
        return markers;
    }
}