package com.example.uas_byk.Mapel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uas_byk.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapelSenBudActivity extends AppCompatActivity {
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.friend_list)
    RecyclerView friendList;

    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapel);
        ButterKnife.bind(this);
        init();
        getFriendList();
    }

    @SuppressLint("WrongConstant")
    private void init() {
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        friendList.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();
    }

    private void getFriendList() {
        Query query = db.collection("senbud");

        FirestoreRecyclerOptions<MapelItem> response = new FirestoreRecyclerOptions.Builder<MapelItem>()
                .setQuery(query, MapelItem.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<MapelItem, MapelHolder>(response) {
            @Override
            public void onBindViewHolder(MapelHolder holder, int position, MapelItem model) {
                progressBar.setVisibility(View.GONE);
                holder.textTitle.setText(model.getJudul());
                holder.textDescription.setText(model.getPenjelasan());

                holder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(MapelSenBudActivity.this, DetailMapelActivity.class);
                    intent.putExtra("judul", model.getJudul());
                    intent.putExtra("penjelasan", model.getPenjelasan());
                    startActivity(intent);
                });
            }

            @Override
            public MapelHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.layout_mapel, group, false);

                return new MapelHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        friendList.setAdapter(adapter);
    }

    public class MapelHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_title)
        TextView textTitle;
        @BindView(R.id.txt_desc)
        TextView textDescription;


        public MapelHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
