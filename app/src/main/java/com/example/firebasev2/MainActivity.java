package com.example.firebasev2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String ARTIST_NAME = "artist name";
    public static final String ARTIST_ID = "artist id";

    EditText editTextName;
    Button buttonAddArtist;
    Spinner spinnerGenres;
    List<Artist> artistList;    // storing artists node from the database
    ListView listViewArtists;

    DatabaseReference databaseArtists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseArtists = FirebaseDatabase.getInstance().getReference("artists");

        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonAddArtist = (Button) findViewById(R.id.buttonAddArtist);
        spinnerGenres = (Spinner) findViewById(R.id.spinnerGenre);
        listViewArtists = (ListView) findViewById(R.id.listViewArtist);
        artistList = new ArrayList<>();

        buttonAddArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArtists();

            }
        });

        listViewArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {  //set listener for list view
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = artistList.get(position);  // getting the selected artist

                Intent intent = new Intent(getApplicationContext(),AddTracks.class);
                intent.putExtra(ARTIST_ID,artist.getArtistId());
                intent.putExtra(ARTIST_NAME,artist.getArtistName());   // Tranfering this data to a new activity?

                startActivity(intent);
            }
        });

     listViewArtists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
         @Override
         public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
             Artist artist = artistList.get(position);
             showUpdateDialog(artist.getArtistId(),artist.getArtistName());

             return false;
         }
     });
    }

    @Override
    protected void onStart() {   // everytime new data is added  this method will be called
        super.onStart();


        databaseArtists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { // exec.. everytime we change something in the database

                // getting the artists node from the firebase
                artistList.clear();  // to avoid overriding of info from database;
                for (DataSnapshot artistSnapshot : dataSnapshot.getChildren()){

                    Artist artist = artistSnapshot.getValue(Artist.class);
                    artistList.add(artist);
                }
                ArtistList adapter = new ArtistList(MainActivity.this,artistList);
                listViewArtists.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {  // if there is some errror

            }
        });
    }

    private void addArtists () {
        String artistsName = editTextName.getText().toString().trim();
        String artistGenre = spinnerGenres.getSelectedItem().toString();

        if (!TextUtils.isEmpty(artistsName)){
            String artistId = databaseArtists.push().getKey();   // creates and get the artistsId

            Artist artist = new Artist(artistId,artistsName,artistGenre);
            databaseArtists.child(artistId).setValue(artist);  // store the artist in a unique id

            Toast.makeText(this,"Artists added",Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this,"you should enter a name ",Toast.LENGTH_SHORT).show();

        }

    }

    private void showUpdateDialog(final String artistId, String artistName){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this); // dialog builder
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog,null);
        dialogBuilder.setView(dialogView);

        final EditText editTextArtistName = (EditText) dialogView.findViewById(R.id.editTextArtistName);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdate);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDelete);
        final Spinner spinner = (Spinner) dialogView.findViewById(R.id.spinnerUpdate);

        dialogBuilder.setTitle("Updating artist "+ artistName);

        final AlertDialog alertDialog = dialogBuilder.create();   // create an alertDialog;
        alertDialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String artistName = editTextArtistName.getText().toString().trim();
                String artistGenre = spinner.getSelectedItem().toString();

                if (TextUtils.isEmpty(artistName)){
                    editTextArtistName.setError("Name required");
                    return;
                } else{
                    updateArtist(artistId,artistName,artistGenre);
                    alertDialog.dismiss();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteArtist(artistId);
                alertDialog.dismiss();
            }
        });



    }

    private void deleteArtist(String artistId) {

        // getting reference of the same id in all the nodes in Firebase, reference all the linked nodes
        DatabaseReference drArtist = FirebaseDatabase.getInstance().getReference("artists").child(artistId);
        DatabaseReference drTrack = FirebaseDatabase.getInstance().getReference("Tracks").child(artistId);

        // removing nodes from firebase
         drArtist.removeValue();
         drTrack.removeValue();


        Toast.makeText(this,"Artist deleted ",Toast.LENGTH_SHORT).show();


    }

    private Boolean updateArtist(String artistId,String artistName, String artistGenre){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("artists").child(artistId);
        Artist artist =  new Artist(artistId,artistName,artistGenre);
        databaseReference.setValue(artist);

        Toast.makeText(this,"Artist updated ",Toast.LENGTH_SHORT).show();

        return true;
    }
    
}
