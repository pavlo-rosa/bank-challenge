package com.prosa.rivertech.rest.bankservices.service;

import com.prosa.rivertech.rest.bankservices.entity.Beneficiary;

import java.util.List;

public interface BeneficiaryService {

    public List<Beneficiary> findAll();

    public Beneficiary findById(Long id);

    public Beneficiary save (Beneficiary beneficiary);

    public void delete(Long id);
}
