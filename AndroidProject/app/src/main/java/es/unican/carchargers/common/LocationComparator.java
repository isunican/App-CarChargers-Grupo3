package es.unican.carchargers.common;

import java.util.Comparator;

import es.unican.carchargers.model.Charger;

public class LocationComparator implements Comparator<Charger> {

    private final double userLat;
    private final double userLon;

    public LocationComparator(double userLat, double userLon) {
        this.userLat = userLat;
        this.userLon = userLon;

    }

    @Override
    public int compare(Charger o1, Charger o2) {
        double lat1 = Double.parseDouble(o1.address.latitude);
        double lon1 = Double.parseDouble(o1.address.longitude);
        double lat2 = Double.parseDouble(o2.address.latitude);
        double lon2 = Double.parseDouble(o2.address.longitude);

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat1 - userLat);
        double lonDistance = Math.toRadians(lon1 - userLon);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(userLat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distanceO1 = R * c * 1000; // convert to meters
        distanceO1 = Math.pow(distanceO1, 2);

        latDistance = Math.toRadians(lat2 - userLat);
        lonDistance = Math.toRadians(lon2 - userLon);
        a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(userLat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distanceO2 = R * c * 1000; // convert to meters
        distanceO2 = Math.pow(distanceO2, 2);


        return Double.compare(distanceO1, distanceO2);

    }

}