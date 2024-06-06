package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.EntityId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntityIdRepository extends JpaRepository<EntityId, String> {
}
