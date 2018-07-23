
package com.tanuj.nowplayinghistory.lastfm.pojos;

import java.util.List;
import com.squareup.moshi.Json;

public class Album {

    @Json(name = "artist")
    private String artist;
    @Json(name = "title")
    private String title;
    @Json(name = "mbid")
    private String mbid;
    @Json(name = "url")
    private String url;
    @Json(name = "image")
    private List<Image> image = null;
    @Json(name = "@attr")
    private Attr attr;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Image> getImage() {
        return image;
    }

    public void setImage(List<Image> image) {
        this.image = image;
    }

    public Attr getAttr() {
        return attr;
    }

    public void setAttr(Attr attr) {
        this.attr = attr;
    }

}
