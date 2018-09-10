package com.sporrong.shoppinglist2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MeetingPoints extends AppCompatActivity {

    public static final String ITEMS_FIREBASE_KEY = "ItemsList";

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference ref = firebaseDatabase.getReference(ITEMS_FIREBASE_KEY);

    FirebaseRecyclerAdapter firebaseRecyclerAdapter;



    private RecyclerView itemList;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_points);

        itemList = findViewById(R.id.meetingList);
        itemList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));

        Query query = FirebaseDatabase.getInstance()
                .getReference(ITEMS_FIREBASE_KEY);

        FirebaseRecyclerOptions<ShoppingItem> options =
                new FirebaseRecyclerOptions.Builder<ShoppingItem>().setQuery(query, ShoppingItem.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ShoppingItem, ShoppingItemViewHolder>(options) {


            @NonNull
            @Override
            public ShoppingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                return new ShoppingItemViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ShoppingItemViewHolder holder, int position, @NonNull ShoppingItem model) {
                holder.bind(model);
            }
        };
        itemList.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }
}
