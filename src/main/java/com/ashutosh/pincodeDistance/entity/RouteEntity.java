package com.ashutosh.pincodeDistance.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonObject;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RouteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String fromPincode;
    private String toPincode;
    private double distance;
    private double duration;
    @JsonProperty("route")
    @Column(columnDefinition = "TEXT")
    @Convert(converter = JsonObjectConverter.class)
    private JsonObject route;
    public String getRouteAsString() {
        return route.toString();  // Converts JsonObject to a JSON String
    }
}
