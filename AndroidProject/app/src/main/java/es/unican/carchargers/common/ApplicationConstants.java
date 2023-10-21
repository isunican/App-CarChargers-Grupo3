package es.unican.carchargers.common;

public class ApplicationConstants {
    private static Double userLatSim = null;
    private static Double userLonSim = null;

    public static boolean isLocationMocked() {
        return userLatSim != null && userLonSim != null;
    }

    public static void setLocationMock(Double uLat, Double uLon) {
        userLatSim = uLat;
        userLonSim = uLon;
    }

    public static Double getLatMock () {
        return userLatSim;
    }
    public static Double getLonMock () {
        return userLonSim;
    }
}
