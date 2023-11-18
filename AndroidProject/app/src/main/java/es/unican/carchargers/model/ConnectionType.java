package es.unican.carchargers.model;

import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;

import java.util.Objects;

@Parcel
public class ConnectionType implements Comparable<ConnectionType>{
    @SerializedName("ID")               public int id;
    @SerializedName("Title")            public String title;

    @Override
    public int compareTo(ConnectionType other) {
        return this.title.compareTo(other.title);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ConnectionType other = (ConnectionType) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
