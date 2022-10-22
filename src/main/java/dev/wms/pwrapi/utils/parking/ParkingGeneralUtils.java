package dev.wms.pwrapi.utils.parking;

import java.util.Set;

public class ParkingGeneralUtils {

    /**
     * Determines parking based on ID in JSON response from parking server. Returns "Unknown Id" on unknown id
     * @param id ID from JSON response
     * @return Name of Parking
     */
    public static String determineParking(String id){

        switch (id) {
            case "5" -> {
                return "D20";
            }
            case "4" -> {
                return "Parking Wrońskiego";
            }
            case "2" -> {
                return "C13";
            }
            case "6" -> {
                return "Geocentrum";
            }
            case "7" -> {
                return "Architektura";
            }
        }

        return "Unknown parking id: " + id;

    }

    public static Set<Integer> getParkingIds(){
        return Set.of(5,4,2,6,7);
    }

}