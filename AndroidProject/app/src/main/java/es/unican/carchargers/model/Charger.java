package es.unican.carchargers.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @SerializedName("Connections")           public ArrayList<Connection> connections;


    public Charger() {
        this.operator = new Operator();
        this.address = new Address();
        this.connections = new ArrayList<>();

        this.userComments = new ArrayList<>();
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




    public List<UserComment> getUserComments() {
        return userComments;
    }
    public int getChargerComments () {
        int counter = 0;
        Set<UserComment> userCommentsCountList = new HashSet<>();
        try {
            userCommentsCountList.addAll(userComments);
            for (UserComment uC : userCommentsCountList) {
                counter++;
            }
            return counter;
        } catch (Exception e){
            return 0;
        }
    }

}
