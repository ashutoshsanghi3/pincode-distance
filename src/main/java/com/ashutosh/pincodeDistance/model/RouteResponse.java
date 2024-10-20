package com.ashutosh.pincodeDistance.model;

import com.ashutosh.pincodeDistance.entity.JsonObjectConverter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonObject;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Lob;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RouteResponse {
    private double distance;  // Distance in meters
    private double duration;
    @JsonProperty("route")
    @Column(columnDefinition = "TEXT")
    @Convert(converter = JsonObjectConverter.class)
    private JsonObject route;
    public String getRouteAsString() {
        return route.toString();  // Converts JsonObject to a JSON String
    }
}
