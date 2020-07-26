package com.angsi.shop.repository.StoreType;

import com.angsi.shop.domain.StoreType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * StoreType entity.
 */
@Repository
public interface StoreTypeRepository
  extends StoreTypeRepositoryCustom, MongoRepository<StoreType, String> {}
