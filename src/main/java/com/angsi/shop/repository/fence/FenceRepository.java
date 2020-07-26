package com.angsi.shop.repository.fence;

import com.angsi.shop.domain.Fence;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Material entity.
 */
@Repository
public interface FenceRepository
  extends FenceRepositoryCustom, MongoRepository<Fence, String> {}
