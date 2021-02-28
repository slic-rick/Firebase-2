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

public class TrackList extends ArrayAdapter<TrackModel> {

    private Activity context;
    private List<TrackModel> trackList;   // list where we store artists from firebase


    public TrackList(Activity context,List <TrackModel>trackList){
        super(context,R.layout.track_list,trackList);
        this.context = context;
        this.trackList =trackList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listItemView = inflater.inflate(R.layout.track_list,null,true);

        TextView textViewTrackName = (TextView) listItemView.findViewById(R.id.textViewTrackName);
        TextView textViewRating = (TextView) listItemView.findViewById(R.id.textViewRating);

        TrackModel track = trackList.get(position);
        textViewTrackName.setText(track.getTrackName());
        textViewRating.setText(String.valueOf(track.getTrackRating()));   // its int so we cast it;

        return listItemView;



    }

}
