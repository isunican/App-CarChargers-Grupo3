package es.unican.carchargers.common;

import java.util.Comparator;

import es.unican.carchargers.model.Charger;

public class LocationComparator implements Comparator<Charger> {

    private final double userLat;
    private final double userLon;

    private final int R = 6371; // Radius of the earth

    public LocationComparator(double userLat, double userLon) {
        this.userLat = userLat;
        this.userLon = userLon;

    }

    @Override
    public int compare(Charger o1, Charger o2) {

        if (o1 == null && o2 != null) {
            return -1;
        } else if (o1 != null && o2 == null) {
            return 1;
        } else if (o1 == null && o2 == null) {
            return 0;
        }


        double lat1 = Double.parseDouble(o1.address.latitude);
        double lon1 = Double.parseDouble(o1.address.longitude);
        double lat2 = Double.parseDouble(o2.address.latitude);
        double lon2 = Double.parseDouble(o2.address.longitude);

        double distance01 = calculateDistance(lat1,lon1, userLat,userLon);


        double distance02 = calculateDistance(lat2,lon2,userLat,userLon);


        return Double.compare(distance01, distance02);

    }

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {

        double radioTierra = 6371;

        // Convertir las latitudes y longitudes de grados a radianes
        double latitud1 = Math.toRadians(lat1);
        double longitud1 = Math.toRadians(lon1);
        double latitud2 = Math.toRadians(lat2);
        double longitud2 = Math.toRadians(lon2);

        // Diferencias entre las latitudes y longitudes
        double diferenciaLatitud = latitud2 - latitud1;
        double diferenciaLongitud = longitud2 - longitud1;

        // Calcular la distancia utilizando la fórmula de Haversine
        double a = Math.sin(diferenciaLatitud / 2) * Math.sin(diferenciaLatitud / 2)
                + Math.cos(latitud1) * Math.cos(latitud2) * Math.sin(diferenciaLongitud / 2) * Math.sin(diferenciaLongitud / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distancia = radioTierra * c;

        return distancia * 1000; // Distancia en metros
    }

}