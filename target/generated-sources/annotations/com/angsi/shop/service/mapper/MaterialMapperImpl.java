package com.angsi.shop.service.mapper;

import com.angsi.shop.VM.MaterialVM;
import com.angsi.shop.domain.Material;
import com.angsi.shop.dto.material.NewMaterialDTO;
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
public class MaterialMapperImpl implements MaterialMapper {

    @Override
    public Material toEntity(NewMaterialDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Material material = new Material();

        material.setId( dto.getId() );
        material.setInternalId( dto.getInternalId() );
        material.setShopId( dto.getShopId() );
        material.setName( dto.getName() );
        material.setStandard( dto.getStandard() );
        material.setUnit( dto.getUnit() );
        material.setImage( dto.getImage() );
        material.setInventory( dto.getInventory() );

        return material;
    }

    @Override
    public NewMaterialDTO toDto(Material entity) {
        if ( entity == null ) {
            return null;
        }

        NewMaterialDTO newMaterialDTO = new NewMaterialDTO();

        newMaterialDTO.setId( entity.getId() );
        newMaterialDTO.setInternalId( entity.getInternalId() );
        newMaterialDTO.setShopId( entity.getShopId() );
        newMaterialDTO.setName( entity.getName() );
        newMaterialDTO.setStandard( entity.getStandard() );
        newMaterialDTO.setUnit( entity.getUnit() );
        newMaterialDTO.setImage( entity.getImage() );
        newMaterialDTO.setInventory( entity.getInventory() );

        return newMaterialDTO;
    }

    @Override
    public List<Material> toEntity(List<NewMaterialDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Material> list = new ArrayList<Material>( dtoList.size() );
        for ( NewMaterialDTO newMaterialDTO : dtoList ) {
            list.add( toEntity( newMaterialDTO ) );
        }

        return list;
    }

    @Override
    public List<NewMaterialDTO> toDto(List<Material> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<NewMaterialDTO> list = new ArrayList<NewMaterialDTO>( entityList.size() );
        for ( Material material : entityList ) {
            list.add( toDto( material ) );
        }

        return list;
    }

    @Override
    public MaterialVM toVM(Material material) {
        if ( material == null ) {
            return null;
        }

        MaterialVM materialVM = new MaterialVM();

        materialVM.setId( material.getId() );
        materialVM.setInternalId( material.getInternalId() );
        materialVM.setShopId( material.getShopId() );
        materialVM.setName( material.getName() );
        materialVM.setStandard( material.getStandard() );
        materialVM.setUnit( material.getUnit() );
        materialVM.setImage( material.getImage() );
        materialVM.setInventory( material.getInventory() );
        materialVM.setCreatedTime( material.getCreatedTime() );

        return materialVM;
    }

    @Override
    public List<MaterialVM> toVMList(List<Material> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<MaterialVM> list = new ArrayList<MaterialVM>( entityList.size() );
        for ( Material material : entityList ) {
            list.add( toVM( material ) );
        }

        return list;
    }
}
