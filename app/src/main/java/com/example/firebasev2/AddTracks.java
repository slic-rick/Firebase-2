package com.example.firebasev2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddTracks extends AppCompatActivity {

    TextView textViewArtistName;
    EditText editTextTrackName;
    SeekBar seekBarRating;
    Button buttonAddTrack;

    ListView listViewTracks;

    List<TrackModel> trackModels;

    DatabaseReference tracksDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tracks);

        textViewArtistName = (TextView) findViewById(R.id.textViewArtistName);
        editTextTrackName = (EditText) findViewById(R.id.editTextTrackName);
        seekBarRating = (SeekBar) findViewById(R.id.trackSeekBar);
        Button buttonAddTrack = (Button) findViewById(R.id.buttonAddTrack);

        listViewTracks = (ListView) findViewById(R.id.listViewTracks);
        trackModels = new ArrayList<>();


        Intent intent = getIntent();                                        // getting info  from the intent from mainActivity
         String artistId = intent.getStringExtra(MainActivity.ARTIST_ID);
         String artistName = intent.getStringExtra(MainActivity.ARTIST_NAME);
        textViewArtistName.setText(artistName);

        // Linking the two tables, Artists and tracks, create an artistNode in the tracks node
        tracksDatabaseReference = FirebaseDatabase.getInstance().getReference("Tracks").child(artistId);

        buttonAddTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTrack();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        //detects any changes to our database;
        tracksDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                trackModels.clear();

                for (DataSnapshot trackSnapshot : dataSnapshot.getChildren()) {

                    TrackModel trackModel = trackSnapshot.getValue(TrackModel.class);  // getting the track model from firebase and saving it;
                    trackModels.add(trackModel);
                }

                TrackList trackListAdapter = new TrackList(AddTracks.this,trackModels);
                listViewTracks.setAdapter(trackListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void saveTrack(){
    String trackName = editTextTrackName.getText().toString().trim();
    int trackRating = seekBarRating.getProgress();

    if (!TextUtils.isEmpty(trackName)){
        String trackId = tracksDatabaseReference.push().getKey();             // creating and getting trackId;
        TrackModel trackModel = new TrackModel(trackId,trackName,trackRating);

        tracksDatabaseReference.child(trackId).setValue(trackModel);                 // saving @trackModel to database reference
        Toast.makeText(this,"Track saved!!",Toast.LENGTH_SHORT).show();

    }else {
        Toast.makeText(this,"Add track name",Toast.LENGTH_SHORT).show();

    }


    }
}
