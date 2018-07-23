
package com.tanuj.nowplayinghistory.lastfm.pojos;

import java.util.List;
import com.squareup.moshi.Json;

public class Toptags {

    @Json(name = "tag")
    private List<Tag> tag = null;

    public List<Tag> getTag() {
        return tag;
    }

    public void setTag(List<Tag> tag) {
        this.tag = tag;
    }

}
