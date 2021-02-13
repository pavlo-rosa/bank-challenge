package com.prosa.rivertech.rest.bankservices.service;

import com.prosa.rivertech.rest.bankservices.repository.BeneficiaryRepository;
import com.prosa.rivertech.rest.bankservices.entity.Beneficiary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BeneficiaryServiceImpl implements BeneficiaryService {

    private BeneficiaryRepository beneficiaryRepository;


    @Autowired
    public BeneficiaryServiceImpl(BeneficiaryRepository beneficiaryRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
    }

    @Override
    public List<Beneficiary> findAll() {
        return beneficiaryRepository.findAll();
    }

    @Override
    public Beneficiary findById(Long id) {
        Optional<Beneficiary> result = beneficiaryRepository.findById(id);
        Beneficiary beneficiary = null;
        if (result.isPresent()) {
            beneficiary = result.get();
        }else{
            throw new RuntimeException("Did not find beneficiary id: "+ id);
        }
        return beneficiary;
    }

    @Override
    public Beneficiary save(Beneficiary beneficiary) {
        return beneficiaryRepository.save(beneficiary);
    }

    @Override
    public void delete(Long id) {
        beneficiaryRepository.deleteById(id);
    }
}
