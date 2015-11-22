package org.nhnnext.nearhoneytip;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class MainActivity extends AppCompatActivity{

    private Context context;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredLayoutManager;
    private TipListAdapter tipListAdapter;
    private List<TipItem> tipItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        tipItems = new ArrayList<>();
        tipListAdapter = new TipListAdapter(this, tipItems);
        recyclerView = (RecyclerView) findViewById(R.id.tiplist);
        staggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredLayoutManager);
        recyclerView.setAdapter(tipListAdapter);

        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class, RemoteService.BASE_URL);
        remoteService.getAllTips(new Callback<List<TipItem>>() {
            @Override
            public void success(List<TipItem> tipItem, Response response) {
                Log.i("retrofit", "test success");

                for (TipItem tip : tipItem) {
                    tipItems.add(tip);
                    Log.i("retrofit", tip.getStorename());
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

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
