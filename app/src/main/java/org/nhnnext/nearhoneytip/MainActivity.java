package org.nhnnext.nearhoneytip;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.nhnnext.nearhoneytip.adapter.TipListAdapter;
import org.nhnnext.nearhoneytip.item.TipItem;
import org.nhnnext.nearhoneytip.library.ImageLib;
import org.nhnnext.nearhoneytip.remote.RemoteService;
import org.nhnnext.nearhoneytip.remote.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private TipListAdapter tipListAdapter;
    private List<TipItem> tipItems;
    private String uid;
    private String nickname;
    private String profilephoto;
    private SharedPreferences pref;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;

    public final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    public final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2;
    boolean isStorageOk = false;
    boolean isGPSOk = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        pref = getSharedPreferences("gps", MODE_PRIVATE);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            isStorageOk = true;
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            isGPSOk = true;
        } else {
            getGPSPermission();
        }

        locationRequest = new LocationRequest();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(20);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (googleApiClient == null) {

            Log.d("location", "setGoogleApiClient");
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            isGPSOk = true;
//            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//
//            if (location == null) {
//                Log.d("location", "null");
//            }
//            else Log.d("location", location.getLatitude() + " : " + location.getLongitude());

        } else {
            getGPSPermission();
        }

        setFAB();
        setDrawer(toolbar);
    }

    private void setFAB() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStorageOk) {
                    startWritingActivity();
                } else {
                    Snackbar.make(view, "갤러리 읽기 권한을 설정해야 새 글을 쓸 수 있습니다", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    getStoragePermission();
                }
            }
        });
    }

    private void startWritingActivity() {
        Intent intent = new Intent(MainActivity.this, WritingTipActivity.class);
        startActivity(intent);
    }

    private void setDrawer(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences pref = getSharedPreferences("login", MODE_PRIVATE);
        uid = pref.getString("uuid", "");
        nickname = pref.getString("nickname", "");
        profilephoto = pref.getString("profilephoto", "");

        setNavHeader(navigationView);
    }

    private void setNavHeader(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);

        TextView navNickName = (TextView) headerView.findViewById(R.id.nickName);
        ImageView navProfile = (ImageView) headerView.findViewById(R.id.profilePhoto);

        ImageLib imageLib = new ImageLib(this);

        imageLib.setIconImage(navProfile, profilephoto);
        navNickName.setText(nickname);
    }

    private void getStoragePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(this, "갤러리 읽기 권한을 설정해야 새 글을 쓸 수 있습니다", Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        } else isStorageOk = true;
    }

    private void getGPSPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                Toast.makeText(this, "GPS를 설정해야 새 글을 쓸 수 있습니다", Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        } else isGPSOk = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                isStorageOk = ((grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED));
                if (isStorageOk) startWritingActivity();
                break;

            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                isGPSOk = ((grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED));
                break;
        }
    }

    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    private void setTipList() {
        tipItems = new ArrayList<>();
        tipListAdapter = new TipListAdapter(this, tipItems, uid);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.tiplist);
        StaggeredGridLayoutManager staggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredLayoutManager);
        recyclerView.setAdapter(tipListAdapter);

        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class, RemoteService.BASE_URL);
        remoteService.getAllTips(new Callback<List<TipItem>>() {
            @Override
            public void success(List<TipItem> tipItem, Response response) {
                Log.d("retrofit", "test success");

                for (TipItem tip : tipItem) {
                    tipItems.add(0, tip);
                }

                tipListAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("retrofit", "test failure");
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the profile action
        } else if (id == R.id.nav_mytip) {

        } else if (id == R.id.nav_setting) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d("location", "onConnected");
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                return;

        Log.d("location", "Request fusedLocation");
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("location", "lat: " + location.getLatitude() + "long: " + location.getLongitude());

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }


//    public void showSettingAlert(){
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//
//        alertDialog.setTitle("GPS setting");
//
//        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
//
//        alertDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog,int which) {
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                getApplicationContext().startActivity(intent);
//            }
//        });
//
//        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        alertDialog.show();
//    }

//    private void putLocationInPref(Location location) {
//        SharedPreferences.Editor editor = pref.edit();
//
//        editor.putString("latitude", location.getLatitude() + "");
//        editor.putString("longitude", location.getLongitude() + "");
//
//        editor.commit();
//    }

//    LocationListener locationListener = new LocationListener() {
//        public void onLocationChanged(Location location) {
//            double latitude = location.getLatitude();
//            double longitude = location.getLongitude();
//
//            Log.d("location", "lat: " + latitude + "long: " + longitude);
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//    };
}
