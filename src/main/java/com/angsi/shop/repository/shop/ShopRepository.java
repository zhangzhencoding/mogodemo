package com.angsi.shop.repository.shop;

import com.angsi.shop.domain.Shop;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Shop entity.
 */
@Repository
public interface ShopRepository
  extends ShopRepositoryCustom, MongoRepository<Shop, String> {}
