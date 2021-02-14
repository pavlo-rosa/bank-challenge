package com.prosa.rivertech.rest.bankservices.repository;

import com.prosa.rivertech.rest.bankservices.entity.Transaction;
import com.prosa.rivertech.rest.bankservices.entity.Transference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferenceRepository extends JpaRepository<Transference, Long> {
}
