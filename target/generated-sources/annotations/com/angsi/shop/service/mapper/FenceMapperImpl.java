package com.angsi.shop.service.mapper;

import com.angsi.shop.VM.FenceVM;
import com.angsi.shop.domain.Fence;
import com.angsi.shop.domain.Point;
import com.angsi.shop.dto.Fence.FenceDTO;
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
public class FenceMapperImpl implements FenceMapper {

    @Override
    public Fence toEntity(FenceDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Fence fence = new Fence();

        fence.setId( dto.getId() );
        fence.setName( dto.getName() );
        fence.setShopId( dto.getShopId() );
        fence.setFenceType( dto.getFenceType() );
        fence.setRadius( dto.getRadius() );
        List<Point> list = dto.getPoints();
        if ( list != null ) {
            fence.setPoints( new ArrayList<Point>( list ) );
        }
        else {
            fence.setPoints( null );
        }
        fence.setNameLocation( dto.getNameLocation() );

        return fence;
    }

    @Override
    public FenceDTO toDto(Fence entity) {
        if ( entity == null ) {
            return null;
        }

        FenceDTO fenceDTO = new FenceDTO();

        fenceDTO.setId( entity.getId() );
        fenceDTO.setName( entity.getName() );
        fenceDTO.setShopId( entity.getShopId() );
        fenceDTO.setFenceType( entity.getFenceType() );
        fenceDTO.setRadius( entity.getRadius() );
        List<Point> list = entity.getPoints();
        if ( list != null ) {
            fenceDTO.setPoints( new ArrayList<Point>( list ) );
        }
        else {
            fenceDTO.setPoints( null );
        }
        fenceDTO.setNameLocation( entity.getNameLocation() );

        return fenceDTO;
    }

    @Override
    public List<Fence> toEntity(List<FenceDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Fence> list = new ArrayList<Fence>( dtoList.size() );
        for ( FenceDTO fenceDTO : dtoList ) {
            list.add( toEntity( fenceDTO ) );
        }

        return list;
    }

    @Override
    public List<FenceDTO> toDto(List<Fence> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<FenceDTO> list = new ArrayList<FenceDTO>( entityList.size() );
        for ( Fence fence : entityList ) {
            list.add( toDto( fence ) );
        }

        return list;
    }

    @Override
    public FenceVM toVM(Fence fence) {
        if ( fence == null ) {
            return null;
        }

        FenceVM fenceVM = new FenceVM();

        fenceVM.setId( fence.getId() );
        fenceVM.setName( fence.getName() );
        fenceVM.setShopId( fence.getShopId() );
        fenceVM.setFenceType( fence.getFenceType() );
        fenceVM.setRadius( fence.getRadius() );
        List<Point> list = fence.getPoints();
        if ( list != null ) {
            fenceVM.setPoints( new ArrayList<Point>( list ) );
        }
        else {
            fenceVM.setPoints( null );
        }
        fenceVM.setNameLocation( fence.getNameLocation() );

        return fenceVM;
    }

    @Override
    public List<FenceVM> toVMList(List<Fence> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<FenceVM> list = new ArrayList<FenceVM>( entityList.size() );
        for ( Fence fence : entityList ) {
            list.add( toVM( fence ) );
        }

        return list;
    }
}
