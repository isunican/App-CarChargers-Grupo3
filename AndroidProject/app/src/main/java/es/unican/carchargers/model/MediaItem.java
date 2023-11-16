package es.unican.carchargers.model;

import android.provider.MediaStore;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Objects;

@Parcel
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MediaItem other = (MediaItem) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
