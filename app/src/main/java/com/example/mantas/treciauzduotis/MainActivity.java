package com.example.mantas.treciauzduotis;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mp;
    FirebaseRecyclerOptions<Model> options;
    FirebaseRecyclerAdapter<Model, ViewHolder> adapter;

    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PersonFragment()).commit();


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Football players information");
        actionBar.setSubtitle("Top players");


        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Data");
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    switch (menuItem.getItemId()) {
                        case R.id.nav_stop:
                            MusicStop();
                            selectedFragment = new PersonFragment();
                            break;
                        case R.id.nav_play:
                            selectedFragment = new LanguageFragment();
                            MusicStart();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };

    @Override
    protected void onStart() {
        super.onStart();

        options = new FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(mRef, Model.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Model model) {

                holder.setDetails(getApplicationContext(), model.getTitle(), model.getDescription(), model.getImage());

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View itemView = LayoutInflater.from(getBaseContext()).inflate(R.layout.row, viewGroup, false);
                return new ViewHolder(itemView);
            }
        };
        adapter.startListening();


        mRecyclerView.setAdapter(adapter);
    }

    public void MusicStart() {
        mp = MediaPlayer.create(this, R.raw.uefa);
        mp.start();
    }

    public void MusicStop() {
        mp.stop();
        mp = MediaPlayer.create(this, R.raw.uefa);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
