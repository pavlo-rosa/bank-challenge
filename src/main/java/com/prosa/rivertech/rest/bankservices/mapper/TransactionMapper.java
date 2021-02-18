package com.prosa.rivertech.rest.bankservices.mapper;

import com.prosa.rivertech.rest.bankservices.dto.AccountDto;
import com.prosa.rivertech.rest.bankservices.dto.TransactionDto;
import com.prosa.rivertech.rest.bankservices.entity.Account;
import com.prosa.rivertech.rest.bankservices.entity.Operation;
import com.prosa.rivertech.rest.bankservices.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper( TransactionMapper.class );

//    No usages
//    @Mapping(source = "transactionDto.id", target = "id")
//    @Mapping(source = "transactionDto.amount", target = "amount")
//    @Mapping(source = "transactionDto.balance", target = "balance")
//    @Mapping(source = "operation.id", target = "operation.id")
//    @Mapping(source = "operation.name", target = "operation.name")
//    Transaction map(TransactionDto transactionDto, Operation operation);

    @Mapping(source = "transaction.id", target = "id")
    @Mapping(source = "transaction.amount", target = "amount")
    @Mapping(source = "transaction.balance", target = "balance")
    @Mapping(source = "transaction.operation.name", target = "operation")
    TransactionDto mapToDto(Transaction transaction);
}
