package com.angsi.shop.repository.ShopLog;

import com.angsi.shop.common.AbstractRepositoryCustom;
import com.angsi.shop.domain.ShopLog;
import org.springframework.stereotype.Repository;

/**
 *  Shop entity.
 */
@Repository
public class ShopLogRepositoryImpl
  extends AbstractRepositoryCustom<ShopLog>
  implements ShopLogRepositoryCustom {}
