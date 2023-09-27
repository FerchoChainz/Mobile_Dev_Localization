package com.example.practica6_localizacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.security.Permission;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener {

    GoogleMap gMap;
    private final int PERMISSION_LOCATION = 999;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback locationCallback;
    FrameLayout map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        map = findViewById(R.id.map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED){
            gMap.setMyLocationEnabled(true);
        } else{
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
            },
                    PERMISSION_LOCATION);
        }

        LatLng gdl = new LatLng(20.666667, -103.333333);
        this.gMap.addMarker(new MarkerOptions().position(gdl).title("Guadalajar"));
        this.gMap.moveCamera(CameraUpdateFactory.newLatLng(gdl));
        gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    public void onClick(View v){
        if (v.getId() == R.id.activity_maps_hybrid){
            gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        } else if (v.getId() == R.id.activity_maps_map) {
            gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } else if (v.getId() == R.id.activity_maps_terrain ) {
            gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else if (v.getId() == R.id.activity_maps_polylines) {
            showPolylines();
        }
    }

    public void showPolylines(){
        if(gMap != null){
            //El parámetro 2 es un rango de valores de 2.0 a 21.0
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new
                    LatLng(20.68697,-103.35339),12));
            gMap.addPolyline(new PolylineOptions().geodesic(true)
                    .add(new LatLng(20.73882,-103.40063)) //Auditorio Telmex
                    .add(new LatLng(20.69676, -103.37541)) //Plaza Midtown
                    .add(new LatLng(20.67806, -103.34673)) //Catedral GDL
                    .add(new LatLng(20.64047, -103.31154)) //Parian
            );
        }//if
    }//showPolylines

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        gMap.addMarker(new MarkerOptions()
                .title("Marca personal")
                .snippet("Mi sitio marcado")
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_background))
                .position(latLng));
    }
}