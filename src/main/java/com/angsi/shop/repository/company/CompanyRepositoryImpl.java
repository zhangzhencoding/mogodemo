package com.angsi.shop.repository.company;

import com.angsi.shop.common.AbstractRepositoryCustom;
import com.angsi.shop.domain.Company;
import org.springframework.stereotype.Repository;

/**
 *  Shop entity.
 */
@Repository
public class CompanyRepositoryImpl
  extends AbstractRepositoryCustom<Company>
  implements CompanyRepositoryCustom {}
