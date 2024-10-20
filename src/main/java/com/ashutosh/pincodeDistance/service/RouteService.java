package com.ashutosh.pincodeDistance.service;

import com.ashutosh.pincodeDistance.entity.PincodeEntity;
import com.ashutosh.pincodeDistance.entity.RouteEntity;
import com.ashutosh.pincodeDistance.model.RouteResponse;
import com.ashutosh.pincodeDistance.repository.RouteRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RouteService {
    private static final String OSRM_URL = "https://router.project-osrm.org/route/v1/driving/%s,%s;%s,%s?overview=false";
    private final HttpClient httpClient;
    @Autowired
    private RouteRepository routeRepository;
    public RouteService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public RouteResponse getRoute(double startLat, double startLng, double endLat, double endLng) throws IOException, InterruptedException {
        String url = String.format(OSRM_URL, startLng, startLat, endLng, endLat);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return parseRoute(response.body());
    }

    private RouteResponse parseRoute(String jsonResponse) {
        JsonObject jsonObject = new Gson().fromJson(jsonResponse, JsonObject.class);
        JsonArray routes = jsonObject.getAsJsonArray("routes");

        if (routes.isEmpty()) {
            throw new RuntimeException("Route not found");
        }

        JsonObject route = routes.get(0).getAsJsonObject();
        double distance = route.get("distance").getAsDouble(); // meters
        double duration = route.get("duration").getAsDouble(); // seconds

        RouteResponse routeResponse = new RouteResponse();
        routeResponse.setDistance(distance);
        routeResponse.setDuration(duration);
        routeResponse.setRoute(route);
        return routeResponse;
    }

    public void saveRoute(String fromPincode, String toPincode, RouteResponse response) {

        RouteEntity routeEntity = RouteEntity.builder()
                .fromPincode(fromPincode)
                .toPincode(toPincode)
                .distance(response.getDistance())
                .duration(response.getDuration())
                .route(response.getRoute())
                .build();

        routeRepository.save(routeEntity);
    }

    public RouteResponse checkIfDataIsCached(String fromPincode, String toPincode) {
        Optional<RouteEntity> cachedRoute = routeRepository.findByFromPincodeAndToPincode(fromPincode, toPincode);
        if (cachedRoute.isPresent()) {
            RouteEntity routeEntity = cachedRoute.get();

            JsonObject routeAsJson = new Gson().fromJson(routeEntity.getRoute(), JsonObject.class);

            return new RouteResponse(routeEntity.getDistance(), routeEntity.getDuration(), routeAsJson);
        }
        return null;
    }

}

