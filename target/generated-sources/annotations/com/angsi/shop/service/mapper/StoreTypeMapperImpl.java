package com.angsi.shop.service.mapper;

import com.angsi.shop.VM.StoreTypeVM;
import com.angsi.shop.domain.StoreType;
import com.angsi.shop.dto.StoreType.NewStoreTypeDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-07-26T12:59:48+0800",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_252 (AdoptOpenJDK)"
)
@Component
public class StoreTypeMapperImpl implements StoreTypeMapper {

    @Override
    public StoreType toEntity(NewStoreTypeDTO dto) {
        if ( dto == null ) {
            return null;
        }

        StoreType storeType = new StoreType();

        storeType.setId( dto.getId() );
        storeType.setInternalId( dto.getInternalId() );
        storeType.setName( dto.getName() );

        return storeType;
    }

    @Override
    public NewStoreTypeDTO toDto(StoreType entity) {
        if ( entity == null ) {
            return null;
        }

        NewStoreTypeDTO newStoreTypeDTO = new NewStoreTypeDTO();

        newStoreTypeDTO.setId( entity.getId() );
        newStoreTypeDTO.setInternalId( entity.getInternalId() );
        newStoreTypeDTO.setName( entity.getName() );

        return newStoreTypeDTO;
    }

    @Override
    public List<StoreType> toEntity(List<NewStoreTypeDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<StoreType> list = new ArrayList<StoreType>( dtoList.size() );
        for ( NewStoreTypeDTO newStoreTypeDTO : dtoList ) {
            list.add( toEntity( newStoreTypeDTO ) );
        }

        return list;
    }

    @Override
    public List<NewStoreTypeDTO> toDto(List<StoreType> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<NewStoreTypeDTO> list = new ArrayList<NewStoreTypeDTO>( entityList.size() );
        for ( StoreType storeType : entityList ) {
            list.add( toDto( storeType ) );
        }

        return list;
    }

    @Override
    public StoreTypeVM toVM(StoreType StoreType) {
        if ( StoreType == null ) {
            return null;
        }

        StoreTypeVM storeTypeVM = new StoreTypeVM();

        storeTypeVM.setId( StoreType.getId() );
        storeTypeVM.setInternalId( StoreType.getInternalId() );
        storeTypeVM.setName( StoreType.getName() );
        storeTypeVM.setCreatedTime( StoreType.getCreatedTime() );

        return storeTypeVM;
    }

    @Override
    public List<StoreTypeVM> toVMList(List<StoreType> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<StoreTypeVM> list = new ArrayList<StoreTypeVM>( entityList.size() );
        for ( StoreType storeType : entityList ) {
            list.add( toVM( storeType ) );
        }

        return list;
    }
}
