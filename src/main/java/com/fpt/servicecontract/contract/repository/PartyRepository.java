package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartyRepository extends JpaRepository<Party, String> {
    Optional<Party> findByTaxNumber(String id);

    Optional<Party> findByEmail(String email);

    @Query(value = """
               select count(c.id) from party cp join contract c
                               where (cp.id = c.partyaid or cp.id = c.partybid)
                               and c.id = :contractId
                               and cp.email = :email
            """, nativeQuery = true)
    int checkMailContractParty(String contractId, String email);

    @Query(value = """
               select * from party p where p.type_party = 1
            """, nativeQuery = true)
    Party manualParty();
}
