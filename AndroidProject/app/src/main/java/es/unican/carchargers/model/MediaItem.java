package es.unican.carchargers.model;

import com.google.gson.annotations.SerializedName;

public class MediaItem implements Comparable<MediaItem> {
    @SerializedName("ID")                   public int id;
    @SerializedName("ItemURL")              public String itemUrl;
    @SerializedName("ItemThumbnailURL")     public String itemThumbnailUrl;
    @SerializedName("IsEnabled")            public Boolean isEnabled; //Tiene qie se true para permitir anhadirlas
    @SerializedName("IsVideo")              public Boolean isVideo; //Tiene que ser false para imagenes

    @Override
    public int compareTo(MediaItem other) {
        return this.itemUrl.compareTo(other.itemUrl);
    }
}
