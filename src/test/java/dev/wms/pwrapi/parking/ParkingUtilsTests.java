package dev.wms.pwrapi.parking;

import dev.wms.pwrapi.utils.parking.ParkingGeneralUtils;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParkingUtilsTests {

    @Test
    public void determineParkingShouldReturnUnknownIdOnUnknownId(){

        Random random = new Random();

        for(int i = 0; i < 30; i++){
            int nonExistingParkingId = random.nextInt()+200;
            assertEquals("Unknown parking id: " + nonExistingParkingId,
                    ParkingGeneralUtils.determineParking(String.valueOf(nonExistingParkingId)));

        }

    }


    @Test
    public void determineParkingShouldReturnParkingNameOnKnownIds(){

        List<String> knownIds = List.of("5","4","2","6","7");
        List<String> expectedResponses = List.of("D20", "Parking Wrońskiego", "C13", "Geocentrum", "Architektura");

        for(int i = 0; i < knownIds.size(); i++){
            assertEquals(expectedResponses.get(i), ParkingGeneralUtils.determineParking(knownIds.get(i)));
        }

    }


}
