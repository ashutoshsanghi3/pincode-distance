package com.ashutosh.pincodeDistance.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
public class LocationService {
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search?q=%s&format=json";
    private final HttpClient httpClient;

    public LocationService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public double[] getCoordinates(String location) throws IOException, InterruptedException {
        String formattedLocation = URLEncoder.encode(location + ", India", StandardCharsets.UTF_8);
        String url = String.format(NOMINATIM_URL, formattedLocation);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        double[] coordinates = parseCoordinates(response.body());

        System.out.println("Coordinates for " + location + ": Lat=" + coordinates[0] + ", Lon=" + coordinates[1]);

        return coordinates;
    }

    private double[] parseCoordinates(String jsonResponse) {
        JsonArray jsonArray = new Gson().fromJson(jsonResponse, JsonArray.class);
        if (jsonArray.isEmpty()) {
            throw new RuntimeException("Location not found");
        }
        JsonObject firstResult = jsonArray.get(0).getAsJsonObject();
        double lat = firstResult.get("lat").getAsDouble();
        double lon = firstResult.get("lon").getAsDouble();
        return new double[]{lat, lon};
    }
}
