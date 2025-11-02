package com.api.franchise.infrastructure.persistence.repository;

import com.api.franchise.domain.model.Franchise;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranchiseRepository extends ReactiveCrudRepository<Franchise,Long> {
}
