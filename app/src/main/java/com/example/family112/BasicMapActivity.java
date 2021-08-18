package com.example.family112;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
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
        uiSettings.setZoomControlsEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(5));

        initMarkers();

        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

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
                    new MarkerOptions().position(info.getLatLng()));
            marker.setInfoWindowEnable(false);
            marker.setIcon(getMarkerDescriptor(info.getNick(), 1.0f));
            markers.add(marker);
        }
        return markers;
    }

    private BitmapDescriptor getMarkerDescriptor(String nick, float alpha){
        View view = null;
        view = View.inflate(this, R.layout.view_marker, null);
        TextView textView = ((TextView) view.findViewById(R.id.title));
        textView.setText(nick);
        textView.setAlpha(alpha);
        textView.setTypeface(Typeface.createFromAsset(getAssets(), "font/HGDBS_CNKI.TTF"));
        return BitmapDescriptorFactory.fromView(view);
    }
}