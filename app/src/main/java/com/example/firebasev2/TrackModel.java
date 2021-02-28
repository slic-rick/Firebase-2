package com.example.firebasev2;

public class TrackModel {

    private String trackID;
    private String trackName;
    private int trackRating;

    public TrackModel() {}

    public TrackModel(String trackID, String trackName, int trackRating) {
        this.trackID = trackID;
        this.trackName = trackName;
        this.trackRating = trackRating;
    }

    public String getTrackID() {
        return trackID;
    }

    public void setTrackID(String trackID) {
        this.trackID = trackID;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public int getTrackRating() {
        return trackRating;
    }

    public void setTrackRating(int trackRating) {
        this.trackRating = trackRating;
    }
}
