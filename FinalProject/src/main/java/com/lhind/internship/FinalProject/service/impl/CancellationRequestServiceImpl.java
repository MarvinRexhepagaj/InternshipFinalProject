package com.lhind.internship.FinalProject.service.impl;

import com.lhind.internship.FinalProject.repository.CancellationRequestRepository;
import com.lhind.internship.FinalProject.service.CancellationRequestService;
import org.springframework.stereotype.Service;

@Service
public class CancellationRequestServiceImpl implements CancellationRequestService {

    private final CancellationRequestRepository cancellationRequestRepository;

    public CancellationRequestServiceImpl(CancellationRequestRepository cancellationRequestRepository) {
        this.cancellationRequestRepository = cancellationRequestRepository;
    }
}
