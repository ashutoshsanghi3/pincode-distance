package com.ashutosh.pincodeDistance.controller;

import com.ashutosh.pincodeDistance.model.RouteResponse;
import com.ashutosh.pincodeDistance.service.LocationService;
import com.ashutosh.pincodeDistance.service.PincodeService;
import com.ashutosh.pincodeDistance.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RouteController {
    @Autowired
    private LocationService locationService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private PincodeService pincodeService;
    @GetMapping("/distance_duration")
    public ResponseEntity<?> getDistanceAndDuration(@RequestParam("fromPincode") String fromPincode,
                                     @RequestParam("toPincode") String toPincode) throws IOException, InterruptedException {

        if (!isValidPincode(fromPincode) || !isValidPincode(toPincode)) {
            return ResponseEntity.badRequest().body("Invalid pincode");
        }
        RouteResponse response = routeService.checkIfDataIsCached(fromPincode, toPincode);
        if(response != null) return ResponseEntity.ok(response);
        // Get lat/lng for from and to pincode
        double[] fromCoordinates = locationService.getCoordinates(fromPincode);
        double[] toCoordinates = locationService.getCoordinates(toPincode);
        // Get the route from OSRM
        RouteResponse routeResponse = routeService.getRoute(fromCoordinates[0], fromCoordinates[1], toCoordinates[0], toCoordinates[1]);
        routeService.saveRoute(fromPincode,toPincode,routeResponse);
        pincodeService.savePincode(fromPincode,fromCoordinates[0],fromCoordinates[1]);
        pincodeService.savePincode(toPincode,toCoordinates[0],toCoordinates[1]);

        return ResponseEntity.ok(routeResponse.getRouteAsString());
    }
    private boolean isValidPincode(String pincode) {
        boolean isValid = pincode != null && pincode.length() == 6 && pincode.matches("\\d{6}");
        System.out.println("Is pincode valid: " + isValid + " for pincode: " + pincode);
        return isValid;
    }
}