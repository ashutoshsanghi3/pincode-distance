package com.ashutosh.pincodeDistance.repository;

import com.ashutosh.pincodeDistance.entity.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<RouteEntity, Long> {
    Optional<RouteEntity> findByFromPincodeAndToPincode(String fromPincode, String toPincode);
}
