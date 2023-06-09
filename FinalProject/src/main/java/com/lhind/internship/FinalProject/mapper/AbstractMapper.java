package com.lhind.internship.FinalProject.mapper;


import org.springframework.stereotype.Component;

@Component
public abstract class AbstractMapper<ENTITY,DTO> {
    public abstract ENTITY toEntity(DTO dto);
    public abstract DTO toDto(ENTITY entity);
}