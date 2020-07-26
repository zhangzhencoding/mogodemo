package com.angsi.shop.repository.configuration;

import com.angsi.shop.common.AbstractRepositoryCustom;
import com.angsi.shop.domain.BusinessConfiguration;
import org.springframework.stereotype.Repository;

/**
 *  Shop entity.
 */
@Repository
public class BusinessConfigurationRepositoryImpl
  extends AbstractRepositoryCustom<BusinessConfiguration>
  implements BusinessConfigurationRepositoryCustom {}
