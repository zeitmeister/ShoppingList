package com.sporrong.shoppinglist2;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity{

    private ArrayList<ShoppingItem> shoppingItems = new ArrayList<>();
    public static final String ITEMS_FIREBASE_KEY = "ItemsList";

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference ref = firebaseDatabase.getReference(ITEMS_FIREBASE_KEY);

    FirebaseRecyclerAdapter firebaseRecyclerAdapter;



    private RecyclerView itemList;

    private EditText itemEntry;
    private TextView totalSumTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        itemList = findViewById(R.id.item_recycler_list);
        itemList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemEntry = findViewById(R.id.addItemEditText);
        totalSumTextView = findViewById(R.id.totalSumTextView);


        itemEntry.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean enterWasPressed = event == null
                        || (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN);
                if (enterWasPressed) {
                    pushToFirebase();
                    return true;
                }

                return false;
            }
        });


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
        totalSumTextView.setText(String.valueOf(TotalSum.getTotalSum()));
        itemList.setAdapter(firebaseRecyclerAdapter);

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                totalSumTextView.setText(String.valueOf(TotalSum.getTotalSum()));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                totalSumTextView.setText(String.valueOf(TotalSum.getTotalSum()));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            removeItemsFromFirebase();
            return true;
        }

        if (id == R.id.startMeetingButton) {
            Intent intent = new Intent(this, MeetingPoints.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void pushToFirebase() {
        String item = itemEntry.getText().toString();
        boolean bought = false;
        ShoppingItem shoppingItem = new ShoppingItem(item, bought, 0);
        shoppingItem.pushKey = ref.push().getKey();
        ref.child(shoppingItem.pushKey).setValue(shoppingItem);
        itemEntry.setText("");
        scrollToMostRecentMessage();

    }

    private void scrollToMostRecentMessage() {
        int mostRecentMessageIndex = firebaseRecyclerAdapter.getItemCount() - 1;
        itemList.scrollToPosition(mostRecentMessageIndex);
    }

    private void removeItemsFromFirebase() {

      DatabaseReference queryRef = FirebaseDatabase.getInstance().getReference(ITEMS_FIREBASE_KEY);
//      queryRef.removeValue();
      Query boughyQuery = queryRef.orderByChild("bought").equalTo("true");


        boughyQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot querySnap : dataSnapshot.getChildren()){
                    querySnap.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
