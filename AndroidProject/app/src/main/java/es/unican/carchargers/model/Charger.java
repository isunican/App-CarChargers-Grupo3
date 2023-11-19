package es.unican.carchargers.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A charging station according to the OpenChargeMap API
 * Documentation: https://openchargemap.org/site/develop/api#/operations/get-poi
 *
 * Currently it only includes a sub-set of the complete model returned by OpenChargeMap
 */
@Parcel
public class Charger implements Comparable<Charger> {
    
    @SerializedName("ID")                   public String id;
    @SerializedName("NumberOfPoints")       public int numberOfPoints;
    @SerializedName("UsageCost")            public String usageCost;
    @SerializedName("OperatorInfo")         public Operator operator;
    @SerializedName("AddressInfo")          public Address address;
    @SerializedName("UserComments")         public ArrayList<UserComment> userComments;

    @SerializedName("Connections")          public ArrayList<Connection> connections;
    @SerializedName("MediaItems")           public ArrayList<MediaItem> mediaItems;


    public Charger() {
        this.operator = new Operator();
        this.address = new Address();
        this.connections = new ArrayList<>();
        this.userComments = new ArrayList<>();
        this.mediaItems = new ArrayList<>();
    }
    @Override
    public int compareTo(Charger other) {
        return this.address.title.compareTo(other.address.title);
    }

    public Set<ConnectionType> getConnectionTypes() {
        Set<ConnectionType> chargerTypes = new HashSet<>();
        Log.d("[DEBUG EN PRESENTER]", "Conexiones" + connections.size());
        for (Connection c : connections) {
            chargerTypes.add(c.connectionType);
        }
        return chargerTypes;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Charger other = (Charger) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
