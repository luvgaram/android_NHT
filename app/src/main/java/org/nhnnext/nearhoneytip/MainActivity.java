package org.nhnnext.nearhoneytip;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TipListAdapter tipListAdapter;
    private List<TipItem> tipItems;
    private String uid;
    private String nickname;
    private String profilephoto;

    public final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    boolean isPermissionOk = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getPermission();
        setFAB();
        setDrawer(toolbar);
    }

    private void setFAB() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isPermissionOk) {
                    Intent intent = new Intent(MainActivity.this, WritingTipActivity.class);
                    startActivity(intent);
                } else {
                    Snackbar.make(view, "갤러리 읽기 권한을 설정해야 새 글을 쓸 수 있습니다", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
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

        setNavHeader(navigationView, nickname, profilephoto);
    }

    private void setNavHeader(NavigationView navigationView, String nickname, String profilPhoto) {
        View headerView = navigationView.getHeaderView(0);

        TextView navNickName = (TextView) headerView.findViewById(R.id.nickName);
        ImageView navProfile = (ImageView) headerView.findViewById(R.id.profilePhoto);

        ImageLib imageLib = new ImageLib(this);

        imageLib.setIconImage(navProfile, profilephoto);
        navNickName.setText(nickname);
    }

    private void getPermission() {
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
        } else isPermissionOk = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    isPermissionOk = true;

                } else isPermissionOk = false;
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTipList();
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
    protected void onPause() {
        super.onPause();

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
        } else if (id == R.id.nav_mytip) {

        } else if (id == R.id.nav_mytip) {

        } else if (id == R.id.nav_setting) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
