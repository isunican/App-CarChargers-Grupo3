package es.unican.carchargers.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Connection {

    @SerializedName("ID")                   public int id;
    @SerializedName("ConnectionType")       public ConnectionType connectionType;

}
