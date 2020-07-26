package com.angsi.shop.repository.ShopLog;

import com.angsi.shop.domain.ShopLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Shop entity.
 */
@Repository
public interface ShopLogRepository
  extends ShopLogRepositoryCustom, MongoRepository<ShopLog, String> {}
