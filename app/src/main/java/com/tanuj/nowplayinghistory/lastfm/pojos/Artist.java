
package com.tanuj.nowplayinghistory.lastfm.pojos;

import com.squareup.moshi.Json;

public class Artist {

    @Json(name = "name")
    private String name;
    @Json(name = "mbid")
    private String mbid;
    @Json(name = "url")
    private String url;

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

}
