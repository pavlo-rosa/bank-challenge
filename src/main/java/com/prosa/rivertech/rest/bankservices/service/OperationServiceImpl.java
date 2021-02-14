package com.prosa.rivertech.rest.bankservices.service;

import com.prosa.rivertech.rest.bankservices.entity.Beneficiary;
import com.prosa.rivertech.rest.bankservices.entity.Operation;
import com.prosa.rivertech.rest.bankservices.repository.BeneficiaryRepository;
import com.prosa.rivertech.rest.bankservices.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OperationServiceImpl implements OperationService {

    private OperationRepository operationRepository;


    @Autowired
    public OperationServiceImpl(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    @Override
    public Operation findById(int id) {
        Optional<Operation> result = operationRepository.findById(id);
        if (!result.isPresent()) {
            throw new RuntimeException("Did not find Operation id: " + id);
        }
        return result.get();
    }

}
