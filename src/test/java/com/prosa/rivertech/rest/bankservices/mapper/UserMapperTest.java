package com.prosa.rivertech.rest.bankservices.mapper;

import com.prosa.rivertech.rest.bankservices.dto.UserDto;
import com.prosa.rivertech.rest.bankservices.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


class UserMapperTest {

    @Test
    void map_ShouldMapUserDtoToUser() {
        UserDto userDto = new UserDto(1L, "Homer");

        User user = UserMapper.INSTANCE.map(userDto);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("Homer");
        assertThat(user.getAccounts()).isNull();
        assertThat(user.getCreatedDate()).isNull();
        assertThat(user.getUpdatedDate()).isNull();

    }

    @Test
    void mapToDto_ShouldMapUserToUserDto() {
        User user = new User(1L, "Homer");

        UserDto userDto = UserMapper.INSTANCE.mapToDto(user);

        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isEqualTo(1L);
        assertThat(userDto.getName()).isEqualTo("Homer");
    }

}