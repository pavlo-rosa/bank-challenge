package com.prosa.rivertech.rest.bankservices.repository;

import com.prosa.rivertech.rest.bankservices.entity.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
}
