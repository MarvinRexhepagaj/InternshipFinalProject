package com.lhind.internship.FinalProject.repository;

import com.lhind.internship.FinalProject.model.entity.CancellationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CancellationRequestRepository extends JpaRepository<CancellationRequest,Long> {
}
