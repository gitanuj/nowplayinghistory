
package com.tanuj.nowplayinghistory.lastfm.pojos;

import com.squareup.moshi.Json;

public class Wiki {

    @Json(name = "published")
    private String published;
    @Json(name = "summary")
    private String summary;
    @Json(name = "content")
    private String content;

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
