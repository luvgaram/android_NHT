package org.nhnnext.nearhoneytip;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.nhnnext.nearhoneytip.adapter.TipListAdapter;
import org.nhnnext.nearhoneytip.item.TipItem;
import org.nhnnext.nearhoneytip.remote.RemoteService;
import org.nhnnext.nearhoneytip.remote.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private TipListAdapter tipListAdapter;
    private List<TipItem> tipItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, WritingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTipList();
    }

    private void setTipList() {
        tipItems = new ArrayList<>();
        tipListAdapter = new TipListAdapter(this, tipItems);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.tiplist);
        StaggeredGridLayoutManager staggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredLayoutManager);
        recyclerView.setAdapter(tipListAdapter);

        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class, RemoteService.BASE_URL);
        remoteService.getAllTips(new Callback<List<TipItem>>() {
            @Override
            public void success(List<TipItem> tipItem, Response response) {
                Log.i("retrofit", "test success");

                for (TipItem tip : tipItem) {
                    tipItems.add(0, tip);
                }

                tipListAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("retrofit", "test failure");
            }
        });
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

}
