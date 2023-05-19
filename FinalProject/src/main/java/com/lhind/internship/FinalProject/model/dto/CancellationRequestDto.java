package com.lhind.internship.FinalProject.model.dto;

import com.lhind.internship.FinalProject.model.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancellationRequestDto {
    private Long id;
    private Long bookingId;
    private RequestStatus status;

    private Date requestDate;
    private String reason;

    private String adminResponse;
}
