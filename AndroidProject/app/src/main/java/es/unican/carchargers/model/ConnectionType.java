package es.unican.carchargers.model;

import com.google.gson.annotations.SerializedName;

public class ConnectionType implements Comparable<ConnectionType>{
    @SerializedName("ID")               public int id;
    @SerializedName("Title")            public String title;

    @Override
    public int compareTo(ConnectionType other) {
        return this.title.compareTo(other.title);
    }
}
