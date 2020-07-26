package com.angsi.shop.service.mapper;

import com.angsi.shop.VM.CompanyVM;
import com.angsi.shop.domain.Company;
import com.angsi.shop.dto.company.NewCompanyDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-07-26T12:59:49+0800",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_252 (AdoptOpenJDK)"
)
@Component
public class CompanyMapperImpl implements CompanyMapper {

    @Override
    public Company toEntity(NewCompanyDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Company company = new Company();

        company.setId( dto.getId() );
        company.setInternalId( dto.getInternalId() );
        company.setCompanyName( dto.getCompanyName() );
        company.setName( dto.getName() );
        company.setTel( dto.getTel() );
        company.setStates( dto.getStates() );
        company.setComment( dto.getComment() );

        return company;
    }

    @Override
    public NewCompanyDTO toDto(Company entity) {
        if ( entity == null ) {
            return null;
        }

        NewCompanyDTO newCompanyDTO = new NewCompanyDTO();

        newCompanyDTO.setId( entity.getId() );
        newCompanyDTO.setInternalId( entity.getInternalId() );
        newCompanyDTO.setCompanyName( entity.getCompanyName() );
        newCompanyDTO.setName( entity.getName() );
        newCompanyDTO.setTel( entity.getTel() );
        newCompanyDTO.setStates( entity.getStates() );
        newCompanyDTO.setComment( entity.getComment() );

        return newCompanyDTO;
    }

    @Override
    public List<Company> toEntity(List<NewCompanyDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Company> list = new ArrayList<Company>( dtoList.size() );
        for ( NewCompanyDTO newCompanyDTO : dtoList ) {
            list.add( toEntity( newCompanyDTO ) );
        }

        return list;
    }

    @Override
    public List<NewCompanyDTO> toDto(List<Company> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<NewCompanyDTO> list = new ArrayList<NewCompanyDTO>( entityList.size() );
        for ( Company company : entityList ) {
            list.add( toDto( company ) );
        }

        return list;
    }

    @Override
    public CompanyVM toVM(Company company) {
        if ( company == null ) {
            return null;
        }

        CompanyVM companyVM = new CompanyVM();

        companyVM.setId( company.getId() );
        companyVM.setInternalId( company.getInternalId() );
        companyVM.setCompanyName( company.getCompanyName() );
        companyVM.setName( company.getName() );
        companyVM.setTel( company.getTel() );
        companyVM.setStates( company.getStates() );
        companyVM.setComment( company.getComment() );

        return companyVM;
    }

    @Override
    public List<CompanyVM> toVMList(List<Company> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<CompanyVM> list = new ArrayList<CompanyVM>( entityList.size() );
        for ( Company company : entityList ) {
            list.add( toVM( company ) );
        }

        return list;
    }
}
