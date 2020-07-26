package com.angsi.shop.service.impl;

import com.angsi.shop.VM.CompanyVM;
import com.angsi.shop.domain.Company;
import com.angsi.shop.dto.company.CompanyQO;
import com.angsi.shop.dto.company.NewCompanyDTO;
import com.angsi.shop.dto.company.UpdateCompanyDTO;
import com.angsi.shop.repository.company.CompanyRepository;
import com.angsi.shop.service.CompanyService;
import com.angsi.shop.service.mapper.CompanyMapper;
import com.basic.core.domain.PageDTO;
import com.basic.core.exception.BusinessException;
import com.basic.starter.dubbo.annotation.Provider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Provider
@Slf4j
public class CompanyServiceImpl implements CompanyService {
  @Autowired
  private CompanyRepository companyRepository;

  @Autowired
  private CompanyMapper companyMapper;

  @Override
  public CompanyVM createCompany(NewCompanyDTO dto) {
    log.debug("Request to save NewCompanyDTO : {}", dto);
    Company Company = companyMapper.toEntity(dto);
    Company.setCreatedTime(new Date());
    return companyMapper.toVM(
            companyRepository.save(Company)
    );
  }

  @Override
  public CompanyVM updateCompany(UpdateCompanyDTO dto) {
    return companyRepository
      .findById(dto.getId())
      .map(
        shop -> {
          shop.setName(dto.getName());
          return companyRepository.save(shop);
        }
      )
      .map(companyMapper::toVM)
      .orElseThrow(
        () ->
          new BusinessException(
            "message.noshopId",
            "\\u5E97\\u94FAId\\u4E0D\\u5B58\\u5728",
            "zh_CN"
          )
      );
  }

  @Override
  public PageDTO<CompanyVM> findAllCompany(
    int current,
    int size,
    CompanyQO query
  ) {
    Page<CompanyVM> page = companyRepository
      .find(query.toQuery(), PageRequest.of(current-1, size))
      .map(companyMapper::toVM);
    return new PageDTO<>(
      page.getNumber(),
      page.getSize(),
      page.getTotalElements(),
      page.getContent()
    );
  }

  @Override
  public List<CompanyVM> findAllCompany(CompanyQO query) {
    return companyMapper.toVMList(companyRepository.find(query.toQuery()));
  }

  @Override
  public CompanyVM findCompany(String id) {
    return companyRepository
      .findById(id)
      .map(companyMapper::toVM)
      .orElseThrow(
        () ->
          new BusinessException(
            "message.noshop",
            "\\u6839\\u636E\\u6B64id\\u672A\\u627E\\u5230\\u5E97\\u94FA",
            "zh_CN"
          )
      );
  }

  @Override
  public void deleteCompany(String id) {
    Company company = new Company();
    company.setId(id);
    companyRepository.delete(company);
  }
}
