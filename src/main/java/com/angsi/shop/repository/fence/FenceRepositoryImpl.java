package com.angsi.shop.repository.fence;

import com.angsi.shop.common.AbstractRepositoryCustom;
import com.angsi.shop.domain.Fence;
import org.springframework.stereotype.Repository;

/**
 *  Shop entity.
 */
@Repository
public class FenceRepositoryImpl
  extends AbstractRepositoryCustom<Fence>
  implements FenceRepositoryCustom {}
