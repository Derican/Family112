package com.example.family112;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

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
import android.widget.ImageView;
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

    final int[] elements = {R.drawable.element1, R.drawable.element2, R.drawable.element3, R.drawable.element4, R.drawable.element5,
            R.drawable.element6, R.drawable.element7, R.drawable.element8, R.drawable.element9, R.drawable.element10, R.drawable.element11, R.drawable.element12, R.drawable.element13, R.drawable.element14, R.drawable.element15, R.drawable.element16, R.drawable.element17, R.drawable.element18, R.drawable.element19, R.drawable.element20, R.drawable.element21, R.drawable.element22, R.drawable.element23, R.drawable.element24, R.drawable.element25, R.drawable.element26, R.drawable.element27, R.drawable.element28, R.drawable.element29, R.drawable.element30, R.drawable.element31, R.drawable.element32, R.drawable.element33, R.drawable.element34, R.drawable.element35, R.drawable.element36, R.drawable.element37, R.drawable.element38, R.drawable.element39, R.drawable.element40, R.drawable.element41, R.drawable.element42, R.drawable.element43, R.drawable.element44, R.drawable.element45, R.drawable.element46, R.drawable.element47, R.drawable.element48, R.drawable.element49, R.drawable.element50, R.drawable.element51, R.drawable.element52, R.drawable.element53, R.drawable.element54, R.drawable.element55, R.drawable.element56, R.drawable.element57};

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

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int id = markers.indexOf(marker);
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.info_drawer);
                TextView nameText = (TextView) findViewById(R.id.name);
                nameText.setText(studentInfos.get(id).getName());
                nameText.setTypeface(Typeface.createFromAsset(getAssets(), "font/HGDBS_CNKI.TTF"));
                TextView cityText = (TextView) findViewById(R.id.city);
                cityText.setText(studentInfos.get(id).getCity());
                cityText.setTypeface(Typeface.createFromAsset(getAssets(), "font/HGDBS_CNKI.TTF"));
                TextView univText = (TextView) findViewById(R.id.univ);
                univText.setText(studentInfos.get(id).getUniversity());
                univText.setTypeface(Typeface.createFromAsset(getAssets(), "font/HGDBS_CNKI.TTF"));
                ImageView bg = (ImageView) findViewById(R.id.elembg);
                bg.setImageResource(elements[id]);
                drawerLayout.openDrawer(findViewById(R.id.right_layout));
                return true;
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

    private void initMarkers() {
        startService(new Intent(this, ImportXlsxService.class));
        bindService(new Intent(this, ImportXlsxService.class), serviceConnection, BIND_AUTO_CREATE);
    }

    private ArrayList<Marker> drawMarkers() {
        ArrayList<Marker> markers = new ArrayList<>();
        for (StudentInfo info : studentInfos) {
            Marker marker = aMap.addMarker(
                    new MarkerOptions().position(info.getLatLng()));
            marker.setInfoWindowEnable(false);
            marker.setIcon(getMarkerDescriptor(info.getNick(), 1.0f));
            markers.add(marker);
        }
        return markers;
    }

    private BitmapDescriptor getMarkerDescriptor(String nick, float alpha) {
        View view = null;
        view = View.inflate(this, R.layout.view_marker, null);
        TextView textView = ((TextView) view.findViewById(R.id.title));
        textView.setText(nick);
        textView.setAlpha(alpha);
        textView.setTypeface(Typeface.createFromAsset(getAssets(), "font/HGDBS_CNKI.TTF"));
        return BitmapDescriptorFactory.fromView(view);
    }
}