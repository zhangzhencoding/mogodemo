package com.angsi.shop.repository.material;

import com.angsi.shop.common.AbstractRepositoryCustom;
import com.angsi.shop.domain.Material;
import org.springframework.stereotype.Repository;

/**
 *  Shop entity.
 */
@Repository
public class MaterialRepositoryImpl
  extends AbstractRepositoryCustom<Material>
  implements MaterialRepositoryCustom {}
