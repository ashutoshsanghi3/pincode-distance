package com.ashutosh.pincodeDistance.service;

import com.ashutosh.pincodeDistance.entity.PincodeEntity;
import com.ashutosh.pincodeDistance.repository.PincodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PincodeService {

    @Autowired
    private PincodeRepository pincodeRepository;

    public void savePincode(String pincode, double lat, double lon) {
        List<Double> polygon = Arrays.asList(lat, lon);

        PincodeEntity pincodeEntity = PincodeEntity.builder()
                .pincode(pincode)
                .lat(lat)
                .lon(lon)
                .polygon(polygon)
                .build();
        pincodeRepository.save(pincodeEntity);
    }
}
