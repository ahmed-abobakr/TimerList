package com.andlausia.timerlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.andlausia.timerlist.adapters.TimesSchedulerAdapter;

public class MainActivity extends AppCompatActivity implements TimesSchedulerAdapter.OnItemClicked{

    RecyclerView timerRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerRecyclerView = (RecyclerView) findViewById(R.id.timer_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        timerRecyclerView.setLayoutManager(mLayoutManager);
        timerRecyclerView.setItemAnimator(new DefaultItemAnimator());
        timerRecyclerView.setAdapter(new TimesSchedulerAdapter(MainActivity.this, MainActivity.this));


    }

    @Override
    public void itemClicked() {
        startActivity(new Intent(this, SecondActivity.class));
    }
}
