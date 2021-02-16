package com.prosa.rivertech.rest.bankservices.mapper;

import com.prosa.rivertech.rest.bankservices.dto.UserDto;
import com.prosa.rivertech.rest.bankservices.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

//    @Mapping(source = "id", target = "id")
//    @Mapping(source = "name", target = "name")
    User map(UserDto user);

//    @Mapping(source = "id", target = "id")
//    @Mapping(source = "name", target = "name")
    UserDto mapToDto(User user);
}
