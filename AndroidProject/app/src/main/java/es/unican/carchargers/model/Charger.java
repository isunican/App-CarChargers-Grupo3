package es.unican.carchargers.model;

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
    @SerializedName("TypeInfo")             public ArrayList<Connection> connection;


    public Charger() {
        this.operator = new Operator();
        this.address = new Address();
        this.connection = new ArrayList<Connection>();

    }
    @Override
    public int compareTo(Charger other) {
        return this.address.title.compareTo(other.address.title);
    }

    public Set<ConnectionType> getConnectionTypes() {
        Set<ConnectionType> tipos = new HashSet<>();
        for (Connection c : connection) {
            tipos.add(c.connectionType);
        }
        return tipos;
    }




}
