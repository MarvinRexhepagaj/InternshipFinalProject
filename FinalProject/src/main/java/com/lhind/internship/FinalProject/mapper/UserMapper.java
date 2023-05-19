package com.lhind.internship.FinalProject.mapper;

import com.lhind.internship.FinalProject.model.dto.UserDto;
import com.lhind.internship.FinalProject.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends AbstractMapper<User, UserDto> {

    @Override
    public User toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setMiddleName(dto.getMiddleName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setRole(dto.getRole());

        return user;
    }

    @Override
    public UserDto toDto(User entity) {
        if (entity == null) {
            return null;
        }

        UserDto userDto = new UserDto();
        userDto.setId(entity.getId());
        userDto.setFirstName(entity.getFirstName());
        userDto.setMiddleName(entity.getMiddleName());
        userDto.setLastName(entity.getLastName());
        userDto.setEmail(entity.getEmail());
        userDto.setPhoneNumber(entity.getPhoneNumber());
        userDto.setAddress(entity.getAddress());
        userDto.setRole(entity.getRole());

        return userDto;
    }
}