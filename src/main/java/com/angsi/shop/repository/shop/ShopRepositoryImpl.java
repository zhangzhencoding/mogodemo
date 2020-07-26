package com.angsi.shop.repository.shop;

import com.angsi.shop.common.AbstractRepositoryCustom;
import com.angsi.shop.domain.Shop;
import org.springframework.stereotype.Repository;

/**
 *  Shop entity.
 */
@Repository
public class ShopRepositoryImpl
  extends AbstractRepositoryCustom<Shop>
  implements ShopRepositoryCustom {}
