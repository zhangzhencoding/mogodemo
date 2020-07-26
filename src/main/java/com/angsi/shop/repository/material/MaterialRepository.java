package com.angsi.shop.repository.material;

import com.angsi.shop.domain.Material;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Material entity.
 */
@Repository
public interface MaterialRepository
  extends MaterialRepositoryCustom, MongoRepository<Material, String> {}
