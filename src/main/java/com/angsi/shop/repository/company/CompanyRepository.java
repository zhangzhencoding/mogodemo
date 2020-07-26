package com.angsi.shop.repository.company;

import com.angsi.shop.domain.Company;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Material entity.
 */
@Repository
public interface CompanyRepository
  extends CompanyRepositoryCustom, MongoRepository<Company, String> {}
