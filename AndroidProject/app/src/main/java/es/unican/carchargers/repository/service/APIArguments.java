package es.unican.carchargers.repository.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Arguments that can be requested directly to the OpenChargeMap REST API.
 * This is currently a sub-set of the total number of arguments that the API accepts.
 * Doc: https://openchargemap.org/site/develop/api#/operations/get-poi
 */
public class APIArguments {
    private String countryCode;

    private Integer connectionTypeId;
    private List<Integer> operatorIds = new ArrayList<>();
    private double lat;
    private double lon;
    private double distance;
    private Integer maxResults;

    public static APIArguments builder() {
        return new APIArguments();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> args = new HashMap<>();
        args.put(IOpenChargeMapAPI.COUNTRY_CODE, countryCode);
        args.put(IOpenChargeMapAPI.OPERATOR_ID, operatorIds);
        args.put(IOpenChargeMapAPI.LATITUDE, lat);
        args.put(IOpenChargeMapAPI.LONGITUDE, lon);
        args.put(IOpenChargeMapAPI.MAX_RESULTS, maxResults);
        args.put(IOpenChargeMapAPI.CONNECTION_TYPE_ID, connectionTypeId);
        args.put(IOpenChargeMapAPI.DISTANCE, distance);

        return args;
    }

    public APIArguments setCountryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public APIArguments setConnectionTypeId(int connectionTypeId) {
        this.connectionTypeId = connectionTypeId;
        return this;
    }

    public APIArguments setOperatorId(int ...operatorId) {
        for (int i : operatorId) {
            this.operatorIds.add(i);
        }
        return this;
    }


    public APIArguments setLocation(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
        return this;
    }
    public APIArguments setDistance(double distance) {
        this.distance = distance;
        return this;
    }

    public APIArguments setMaxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }


    public String getCountryCode() {
        return countryCode;
    }

    public double getLocationLatitude() {
        return lat;
    }

    public double getLocationLongitude() {
        return lon;
    }

    public int getConnectionTypeId() {
        return connectionTypeId;
    }

    public Integer[] getOperatorIds() {
        if (operatorIds.size() == 0) {
            return null;
        }
        return operatorIds.toArray(new Integer[0]);
    }

    public int getMaxResults() {
        return maxResults;
    }

    public double getDistance() {
        return distance;
    }
}
