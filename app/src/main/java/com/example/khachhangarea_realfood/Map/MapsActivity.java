package com.example.khachhangarea_realfood.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.khachhangarea_realfood.R;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.tapadoo.alerter.Alerter;

import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private int REQUEST_CODE = 1;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationClient;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    String str_address;
    List<Place.Field> fields;
    MapView mapView;
    private LatLng currentPosition = new LatLng(10.853801064658226, 106.75786086078125);
    SupportMapFragment supportMapFragment;
    Map map = null;
    SearchView searchBar;
    Button btnLuuDiaChi;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Places.isInitialized()) {
            Places.initialize(MapsActivity.this, "AIzaSyCogGe3CNe9rABkNnYOsTeuQynPBfCwaUw");
            PlacesClient placesClient = Places.createClient(MapsActivity.this);
        }
        setContentView(R.layout.activity_map);
        setControl();
        setEvent();
        //Init FusedLocationProviderClient to get last GPS
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //SetEvent
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = query;
                List<Address> addresses = null;
                if (addresses != null || !location.isEmpty()) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addresses = geocoder.getFromLocationName(location, 1);
                        str_address = addresses.get(0).getAddressLine(0);
                    } catch (Exception e) {

                    }
                    if (addresses.size() != 0) {
                        mMap.clear();
                        Address address = addresses.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title(address.getAddressLine(0)));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    }
                    else {
                        Alerter.create(MapsActivity.this)
                                .setTitle("Lỗi")
                                .setText("Không tìm thấy địa chỉ bạn cần tìm")
                                .setBackgroundColorRes(R.color.red) // or setBackgroundColorInt(Color.CYAN)
                                .show();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        btnLuuDiaChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",str_address);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (!CheckPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            LoadData();
        }

    }



    private void LoadData() {
        //Get Current positon
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {

                            List<Address>addresses= null;
                            Geocoder geocoder = new Geocoder(MapsActivity.this);
                            try {
                                addresses= geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                str_address = addresses.get(0).getAddressLine(0);
                            }catch (Exception e)
                            {

                            }
                            if (addresses.size()!=0)
                            {
                                Address address = addresses.get(0);
                                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                                mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title( addresses.get(0).getAddressLine(0)));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                                str_address = addresses.get(0).getAddressLine(0);
                                searchBar.setQuery(address.getAddressLine(0),false);
                            }
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                            //set current possition
                            View locationButton = ((View) findViewById(R.id.map).findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
                            // position on right bottom
                            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                            rlp.setMargins(0, 180, 180, 0);
                        }
                    }
                });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }
    //Check permission
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean CheckPermission(String permission) {
        boolean oke = false;
        int check = checkSelfPermission(permission);
        if (check == PackageManager.PERMISSION_GRANTED) {
            oke = true;
        }
        return oke;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (permissions.length ==1&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                LoadData();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public void onMapClick(LatLng point) {


    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(currentPosition).title("Marker in Home"));
        //config maptype
        if (mMap != null) {
            if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mMap.setMyLocationEnabled(true);
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                            mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude())));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                        }
                    }
                });


        //Set onclick on map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull  LatLng latLng) {
                // TODO Auto-generated method stub
                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker

                List<Address>addresses= null;
                Geocoder geocoder = new Geocoder(MapsActivity.this);
                try {
                    addresses= geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                    str_address = addresses.get(0).getAddressLine(0);

                }catch (Exception e)
                {

                }
                if (addresses.size()!=0)
                {
                    Address address = addresses.get(0);
                    markerOptions.title(  addresses.get(0).getAddressLine(0) );
                    searchBar.setQuery(address.getAddressLine(0),false);
                }
                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,20));

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions).setDraggable(true);
            }
        });
        //set current possition
        View locationButton = ((View) findViewById(R.id.map).findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        rlp.setMargins(0, 180, 180, 0);
    }
    public interface Map {
        public void getAdress(String s);
    }
    private void setEvent() {
    }

    private void setControl() {
        btnLuuDiaChi = findViewById(R.id.btnLuuDiaChi);
        searchBar = findViewById(R.id.search_bar);
    }
}