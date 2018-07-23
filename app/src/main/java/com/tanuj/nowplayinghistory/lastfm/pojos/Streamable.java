
package com.tanuj.nowplayinghistory.lastfm.pojos;

import com.squareup.moshi.Json;

public class Streamable {

    @Json(name = "#text")
    private String text;
    @Json(name = "fulltrack")
    private String fulltrack;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFulltrack() {
        return fulltrack;
    }

    public void setFulltrack(String fulltrack) {
        this.fulltrack = fulltrack;
    }

}
