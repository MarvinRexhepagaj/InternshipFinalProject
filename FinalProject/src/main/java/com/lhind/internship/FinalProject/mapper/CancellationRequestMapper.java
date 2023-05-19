package com.lhind.internship.FinalProject.mapper;

import com.lhind.internship.FinalProject.model.dto.CancellationRequestDto;
import com.lhind.internship.FinalProject.model.entity.Booking;
import com.lhind.internship.FinalProject.model.entity.CancellationRequest;
import org.springframework.stereotype.Component;

@Component
public class CancellationRequestMapper extends AbstractMapper<CancellationRequest, CancellationRequestDto> {

    @Override
    public CancellationRequest toEntity(CancellationRequestDto dto) {
        if (dto == null) {
            return null;
        }

        CancellationRequest cancellationRequest = new CancellationRequest();
        cancellationRequest.setId(dto.getId());
        cancellationRequest.setBooking(new Booking(dto.getBookingId()));
        cancellationRequest.setStatus(dto.getStatus());
        cancellationRequest.setRequestDate(dto.getRequestDate());
        cancellationRequest.setReason(dto.getReason());
        cancellationRequest.setAdminResponse(dto.getAdminResponse());

        return cancellationRequest;
    }

    @Override
    public CancellationRequestDto toDto(CancellationRequest entity) {
        if (entity == null) {
            return null;
        }

        CancellationRequestDto cancellationRequestDto = new CancellationRequestDto();
        cancellationRequestDto.setId(entity.getId());
        cancellationRequestDto.setBookingId(entity.getBooking().getId());
        cancellationRequestDto.setStatus(entity.getStatus());
        cancellationRequestDto.setRequestDate(entity.getRequestDate());
        cancellationRequestDto.setReason(entity.getReason());
        cancellationRequestDto.setAdminResponse(entity.getAdminResponse());

        return cancellationRequestDto;
    }
}