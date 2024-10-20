package com.ashutosh.pincodeDistance.repository;

import com.ashutosh.pincodeDistance.entity.PincodeEntity;
import com.ashutosh.pincodeDistance.entity.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PincodeRepository extends JpaRepository<PincodeEntity, String> {
}
