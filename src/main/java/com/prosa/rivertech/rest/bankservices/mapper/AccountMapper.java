package com.prosa.rivertech.rest.bankservices.mapper;

import com.prosa.rivertech.rest.bankservices.dto.AccountDto;
import com.prosa.rivertech.rest.bankservices.dto.UserDto;
import com.prosa.rivertech.rest.bankservices.entity.Account;
import com.prosa.rivertech.rest.bankservices.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper( AccountMapper.class );

//    No usages
//    @Mapping(target = "password", ignore = true)
//    Account map(AccountDto accountDto);

    AccountDto mapToDto(Account account);
}
