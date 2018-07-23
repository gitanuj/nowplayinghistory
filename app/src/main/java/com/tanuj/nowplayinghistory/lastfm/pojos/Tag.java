
package com.tanuj.nowplayinghistory.lastfm.pojos;

import com.squareup.moshi.Json;

public class Tag {

    @Json(name = "name")
    private String name;
    @Json(name = "url")
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
