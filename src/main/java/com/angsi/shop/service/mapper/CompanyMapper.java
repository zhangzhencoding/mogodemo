package com.angsi.shop.service.mapper;

import com.angsi.shop.VM.CompanyVM;
import com.angsi.shop.domain.Company;
import com.angsi.shop.dto.company.NewCompanyDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface CompanyMapper extends EntityMapper<NewCompanyDTO, Company> {
  CompanyVM toVM(Company company);

  List<CompanyVM> toVMList(List<Company> entityList);
}
