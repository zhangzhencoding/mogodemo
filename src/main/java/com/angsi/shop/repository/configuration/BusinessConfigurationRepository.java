package com.angsi.shop.repository.configuration;

import com.angsi.shop.domain.BusinessConfiguration;
import com.angsi.shop.domain.Fence;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * BusinessConfiguration entity.
 */
@Repository
public interface BusinessConfigurationRepository
  extends BusinessConfigurationRepositoryCustom, MongoRepository<BusinessConfiguration, String> {}
