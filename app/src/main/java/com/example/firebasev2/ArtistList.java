package com.example.firebasev2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ArtistList extends ArrayAdapter {      // this are custom adapter class;
    private Activity context;
    private List<Artist> artistList;   // list where we store artists from firebase

    public ArtistList(Activity context,List <Artist>artistList){
        super(context,R.layout.list_view,artistList);
        this.context = context;
        this.artistList =artistList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listItemView = inflater.inflate(R.layout.list_view,null,true);

        TextView textViewName = (TextView) listItemView.findViewById(R.id.listViewName);
        TextView textViewGenre = (TextView) listItemView.findViewById(R.id.listViewGenre);

        Artist artist = artistList.get(position);
        textViewName.setText(artist.getArtistName());
        textViewGenre.setText(artist.artistGenre);

        return listItemView;



    }
}
