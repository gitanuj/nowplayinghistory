package com.tanuj.nowplayinghistory.persistence;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.tanuj.nowplayinghistory.Utils;

@Entity(tableName = "songs")
public class Song implements Parcelable {
    @PrimaryKey
    private long timestamp;
    private String songText;
    private double lat = -1;
    private double lon = -1;

    public Song(long timestamp, String songText) {
        this.timestamp = timestamp;
        this.songText = songText;
    }

    protected Song(Parcel in) {
        timestamp = in.readLong();
        songText = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public long getTimestamp() {
        return timestamp;
    }

    public String getTimestampText() {
        return Utils.getTimestampString(timestamp);
    }

    public String getSongText() {
        return songText;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Song song = (Song) o;

        return songText.equals(song.songText);
    }

    @Override
    public int hashCode() {
        return songText.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(timestamp);
        dest.writeString(songText);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
    }
}
