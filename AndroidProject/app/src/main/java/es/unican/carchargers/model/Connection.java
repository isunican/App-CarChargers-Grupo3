package es.unican.carchargers.model;

import com.google.gson.annotations.SerializedName;

public class Connection {
    @SerializedName("ID")                   public int id;
    @SerializedName("connectionType")       public ConnectionType connectionType;

    public Connection() {
        this.connectionType = new ConnectionType();
    }
}
