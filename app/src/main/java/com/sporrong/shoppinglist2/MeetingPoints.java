package com.sporrong.shoppinglist2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;

public class MeetingPoints extends AppCompatActivity {

    public static final String MEETING_FIREBASE_LIST = "MeetingList";

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference ref = firebaseDatabase.getReference(MEETING_FIREBASE_LIST);

    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    String pattern = "yyyy-MM-dd";
    SimpleDateFormat simpleDate = new SimpleDateFormat(pattern);


    private RecyclerView meetingList;
    private EditText meetingEntry;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.startMeetingButton);
        menuItem.setTitle("Gå till Inköpslistan");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.remove_bought_button || id == R.id.startMeetingButton) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_points);

        meetingEntry = findViewById(R.id.meetingEntry);
        meetingList = findViewById(R.id.meetingList);
        meetingList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));

        Query query = FirebaseDatabase.getInstance()
                .getReference(MEETING_FIREBASE_LIST);
        meetingEntry.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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


        FirebaseRecyclerOptions<Meeting> options =
                new FirebaseRecyclerOptions.Builder<Meeting>().setQuery(query, Meeting.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Meeting, MeetingViewHolder>(options) {


            @NonNull
            @Override
            public MeetingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meeting_point_item, parent, false);
                return new MeetingViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MeetingViewHolder holder, int position, @NonNull Meeting model) {
                holder.bind(model);
            }
        };
        meetingList.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }
    private void pushToFirebase() {
        String enteredMeeting = meetingEntry.getText().toString();
        Meeting meeting = new Meeting("Andra juli ", enteredMeeting);
        meeting.setPushKey(ref.push().getKey());
        ref.child(meeting.getPushKey()).setValue(meeting);
        meetingEntry.setText("");

    }



}
