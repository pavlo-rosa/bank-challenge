package com.prosa.rivertech.rest.bankservices.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.prosa.rivertech.rest.bankservices.entity.Beneficiary;
import com.prosa.rivertech.rest.bankservices.service.BeneficiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BeneficiaryController {
    private BeneficiaryService beneficiaryService;

    @Autowired
    public BeneficiaryController(BeneficiaryService beneficiaryService) {
        this.beneficiaryService = beneficiaryService;
    }

    @GetMapping("/beneficiaries")
    public List<Beneficiary> retrieveAllBeneficiaries(){
        return beneficiaryService.findAll();
    }

    @GetMapping("/beneficiaries/{id}")
    public Beneficiary retrieveBeneficiaryById(@PathVariable long id){
        Beneficiary beneficiary = beneficiaryService.findById(id);
        return beneficiary;
    }

    @PostMapping("/beneficiaries")
    public Beneficiary addBeneficiary(@RequestBody Beneficiary beneficiary){
        return beneficiaryService.save(beneficiary);
    }

    @PutMapping("/beneficiaries")
    public  Beneficiary updateBeneficiary(@RequestBody Beneficiary employee){
        beneficiaryService.save(employee);
        return employee;
    }

    @DeleteMapping("/beneficiaries/{id}")
    public String deleteBeneficiary(@PathVariable Long id){
        Beneficiary beneficiary = beneficiaryService.findById(id);
        if(beneficiary == null){
            throw new RuntimeException("Beneficiary is not found: "+id);
        }
        beneficiaryService.delete(id);
        return "Deleted beneficiary id: "+id;
    }
}
