package es.unican.carchargers.constants;

import java.util.List;

import es.unican.carchargers.R;

/**
 * Static list of charging stations operators. Each operator includes its OpenChargeMap id
 * and the link to the logo resource id.
 */
public enum ECharger {
    TYPE2_SOCKET(25, "Type 2 (Socket Only)"),  // ID Connections:354871
    CCS(33, "CCS (Type 2)"),
    CEE(28, "CEE 7/4 - Schuko - Type F"),
    CHADEMO(2, "CHAdeMO"),
    TYPE2_TETHERED(1036, "Type 2 (Tethered Connector) "),
    GENERIC(-1, "Not Found");

    public final static List<Integer> ALL = null;


    /** Connection type id */
    public final int id;

    /** Connection type title*/
    public final String title;

    private ECharger(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public static ECharger fromId(int id) {
        for (ECharger connection : ECharger.values()) {
            if (id == connection.id) {
                return connection;
            }
        }
        return GENERIC;
    }


}