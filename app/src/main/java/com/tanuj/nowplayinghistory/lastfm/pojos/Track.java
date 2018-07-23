
package com.tanuj.nowplayinghistory.lastfm.pojos;

import com.squareup.moshi.Json;

public class Track {

    @Json(name = "name")
    private String name;
    @Json(name = "mbid")
    private String mbid;
    @Json(name = "url")
    private String url;
    @Json(name = "duration")
    private String duration;
    @Json(name = "streamable")
    private Streamable streamable;
    @Json(name = "listeners")
    private String listeners;
    @Json(name = "playcount")
    private String playcount;
    @Json(name = "artist")
    private Artist artist;
    @Json(name = "album")
    private Album album;
    @Json(name = "toptags")
    private Toptags toptags;
    @Json(name = "wiki")
    private Wiki wiki;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Streamable getStreamable() {
        return streamable;
    }

    public void setStreamable(Streamable streamable) {
        this.streamable = streamable;
    }

    public String getListeners() {
        return listeners;
    }

    public void setListeners(String listeners) {
        this.listeners = listeners;
    }

    public String getPlaycount() {
        return playcount;
    }

    public void setPlaycount(String playcount) {
        this.playcount = playcount;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Toptags getToptags() {
        return toptags;
    }

    public void setToptags(Toptags toptags) {
        this.toptags = toptags;
    }

    public Wiki getWiki() {
        return wiki;
    }

    public void setWiki(Wiki wiki) {
        this.wiki = wiki;
    }

}
