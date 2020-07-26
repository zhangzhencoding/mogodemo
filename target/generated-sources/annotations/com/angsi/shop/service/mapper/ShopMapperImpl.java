package com.angsi.shop.service.mapper;

import com.angsi.shop.VM.BigShopVM;
import com.angsi.shop.VM.FactoryVM;
import com.angsi.shop.VM.SeatingAreaVM;
import com.angsi.shop.VM.ShopTimeVM;
import com.angsi.shop.VM.ShopVM;
import com.angsi.shop.VM.SubShopVM;
import com.angsi.shop.common.AuditRecord;
import com.angsi.shop.domain.Shop;
import com.angsi.shop.dto.bigshop.NewBigShopDTO;
import com.angsi.shop.dto.common.BusyConfiguration;
import com.angsi.shop.dto.common.DiningTable;
import com.angsi.shop.dto.common.InventoryWarning;
import com.angsi.shop.dto.factory.NewFactoryDTO;
import com.angsi.shop.dto.seatingarea.NewSeatingAreaDTO;
import com.angsi.shop.dto.shop.NewShopDTO;
import com.angsi.shop.dto.shop.UpdateShopTimeDTO;
import com.angsi.shop.dto.subshop.NewSubShopDTO;
import com.angsi.shop.enums.ShopInventorytype;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-07-26T12:59:49+0800",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_252 (AdoptOpenJDK)"
)
@Component
public class ShopMapperImpl implements ShopMapper {

    @Override
    public Shop toEntity(NewShopDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Shop shop = new Shop();

        shop.setId( dto.getId() );
        shop.setInternalId( dto.getInternalId() );
        shop.setParentId( dto.getParentId() );
        shop.setName( dto.getName() );
        shop.setStoreType( dto.getStoreType() );
        shop.setShoptype( dto.getShoptype() );
        shop.setSubType( dto.getSubType() );
        shop.setOperationType( dto.getOperationType() );
        shop.setFloor( dto.getFloor() );
        shop.setPmc( dto.getPmc() );
        shop.setPickingAddress( dto.getPickingAddress() );
        shop.setDescription( dto.getDescription() );
        shop.setTel( dto.getTel() );
        shop.setOrderConditions( dto.getOrderConditions() );
        shop.setStartDeskNo( dto.getStartDeskNo() );
        shop.setSeatCount( dto.getSeatCount() );
        shop.setOpenTime( dto.getOpenTime() );
        shop.setManageScope( dto.getManageScope() );
        List<String> list = dto.getPapersImage();
        if ( list != null ) {
            shop.setPapersImage( new ArrayList<String>( list ) );
        }
        else {
            shop.setPapersImage( null );
        }
        List<String> list1 = dto.getSceneImage();
        if ( list1 != null ) {
            shop.setSceneImage( new ArrayList<String>( list1 ) );
        }
        else {
            shop.setSceneImage( null );
        }
        shop.setDelete( dto.getDelete() );
        List<UpdateShopTimeDTO> list2 = dto.getUpdateShopTime();
        if ( list2 != null ) {
            shop.setUpdateShopTime( new ArrayList<UpdateShopTimeDTO>( list2 ) );
        }
        else {
            shop.setUpdateShopTime( null );
        }

        return shop;
    }

    @Override
    public NewShopDTO toDto(Shop entity) {
        if ( entity == null ) {
            return null;
        }

        NewShopDTO newShopDTO = new NewShopDTO();

        newShopDTO.setId( entity.getId() );
        newShopDTO.setInternalId( entity.getInternalId() );
        newShopDTO.setParentId( entity.getParentId() );
        newShopDTO.setName( entity.getName() );
        newShopDTO.setStoreType( entity.getStoreType() );
        newShopDTO.setShoptype( entity.getShoptype() );
        newShopDTO.setSubType( entity.getSubType() );
        newShopDTO.setOperationType( entity.getOperationType() );
        newShopDTO.setFloor( entity.getFloor() );
        newShopDTO.setPmc( entity.getPmc() );
        newShopDTO.setPickingAddress( entity.getPickingAddress() );
        newShopDTO.setDescription( entity.getDescription() );
        newShopDTO.setTel( entity.getTel() );
        newShopDTO.setOrderConditions( entity.getOrderConditions() );
        newShopDTO.setStartDeskNo( entity.getStartDeskNo() );
        newShopDTO.setSeatCount( entity.getSeatCount() );
        newShopDTO.setOpenTime( entity.getOpenTime() );
        List<String> list = entity.getPapersImage();
        if ( list != null ) {
            newShopDTO.setPapersImage( new ArrayList<String>( list ) );
        }
        else {
            newShopDTO.setPapersImage( null );
        }
        List<String> list1 = entity.getSceneImage();
        if ( list1 != null ) {
            newShopDTO.setSceneImage( new ArrayList<String>( list1 ) );
        }
        else {
            newShopDTO.setSceneImage( null );
        }
        newShopDTO.setDelete( entity.getDelete() );
        List<UpdateShopTimeDTO> list2 = entity.getUpdateShopTime();
        if ( list2 != null ) {
            newShopDTO.setUpdateShopTime( new ArrayList<UpdateShopTimeDTO>( list2 ) );
        }
        else {
            newShopDTO.setUpdateShopTime( null );
        }
        newShopDTO.setManageScope( entity.getManageScope() );

        return newShopDTO;
    }

    @Override
    public List<Shop> toEntity(List<NewShopDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Shop> list = new ArrayList<Shop>( dtoList.size() );
        for ( NewShopDTO newShopDTO : dtoList ) {
            list.add( toEntity( newShopDTO ) );
        }

        return list;
    }

    @Override
    public List<NewShopDTO> toDto(List<Shop> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<NewShopDTO> list = new ArrayList<NewShopDTO>( entityList.size() );
        for ( Shop shop : entityList ) {
            list.add( toDto( shop ) );
        }

        return list;
    }

    @Override
    public BigShopVM toBigShopVm(Shop var1) {
        if ( var1 == null ) {
            return null;
        }

        BigShopVM bigShopVM = new BigShopVM();

        bigShopVM.setId( var1.getId() );
        bigShopVM.setInternalId( var1.getInternalId() );
        bigShopVM.setBigShoptype( var1.getBigShoptype() );
        bigShopVM.setName( var1.getName() );
        bigShopVM.setOperationType( var1.getOperationType() );
        bigShopVM.setDeliveryType( var1.getDeliveryType() );
        bigShopVM.setPmc( var1.getPmc() );
        bigShopVM.setPickingAddress( var1.getPickingAddress() );
        bigShopVM.setDescription( var1.getDescription() );
        bigShopVM.setTel( var1.getTel() );
        bigShopVM.setOpeningStatus( var1.getOpeningStatus() );
        bigShopVM.setEndTime( var1.getEndTime() );
        bigShopVM.setStartTime( var1.getStartTime() );
        bigShopVM.setOpenTime( var1.getOpenTime() );
        if ( var1.getCategoryType() != null ) {
            bigShopVM.setCategoryType( var1.getCategoryType().name() );
        }
        bigShopVM.setCentre( var1.getCentre() );
        bigShopVM.setOperator( var1.getOperator() );
        bigShopVM.setOperatorMsg( var1.getOperatorMsg() );
        bigShopVM.setProgramManager( var1.getProgramManager() );
        bigShopVM.setPeopleNum( var1.getPeopleNum() );
        bigShopVM.setOperationTime( var1.getOperationTime() );
        bigShopVM.setManageAddress( var1.getManageAddress() );
        bigShopVM.setOkManageAddress( var1.getOkManageAddress() );
        bigShopVM.setQrCode( var1.getQrCode() );
        bigShopVM.setOkAffirm( var1.getOkAffirm() );
        bigShopVM.setOkReserve( var1.getOkReserve() );
        bigShopVM.setOkSubMeanu( var1.getOkSubMeanu() );
        List<String> list = var1.getPapersImage();
        if ( list != null ) {
            bigShopVM.setPapersImage( new ArrayList<String>( list ) );
        }
        else {
            bigShopVM.setPapersImage( null );
        }
        List<String> list1 = var1.getSceneImage();
        if ( list1 != null ) {
            bigShopVM.setSceneImage( new ArrayList<String>( list1 ) );
        }
        else {
            bigShopVM.setSceneImage( null );
        }
        bigShopVM.setManageScope( var1.getManageScope() );
        bigShopVM.setAuditStatus( var1.getAuditStatus() );
        bigShopVM.setAuditOpinion( var1.getAuditOpinion() );
        List<AuditRecord> list2 = var1.getAuditOpinionLogs();
        if ( list2 != null ) {
            bigShopVM.setAuditOpinionLogs( new ArrayList<AuditRecord>( list2 ) );
        }
        else {
            bigShopVM.setAuditOpinionLogs( null );
        }
        bigShopVM.setCreatedTime( var1.getCreatedTime() );
        bigShopVM.setDelete( var1.getDelete() );

        return bigShopVM;
    }

    @Override
    public Shop toBigShopEntity(NewBigShopDTO var1) {
        if ( var1 == null ) {
            return null;
        }

        Shop shop = new Shop();

        shop.setId( var1.getId() );
        shop.setName( var1.getName() );
        shop.setBigShoptype( var1.getBigShoptype() );
        shop.setOperationType( var1.getOperationType() );
        shop.setDeliveryType( var1.getDeliveryType() );
        shop.setPmc( var1.getPmc() );
        shop.setPickingAddress( var1.getPickingAddress() );
        shop.setDescription( var1.getDescription() );
        shop.setTel( var1.getTel() );
        shop.setOpeningStatus( var1.getOpeningStatus() );
        shop.setAuditStatus( var1.getAuditStatus() );
        shop.setAuditOpinion( var1.getAuditOpinion() );
        shop.setOpenTime( var1.getOpenTime() );
        shop.setCentre( var1.getCentre() );
        shop.setOperator( var1.getOperator() );
        shop.setProgramManager( var1.getProgramManager() );
        shop.setManageScope( var1.getManageScope() );
        shop.setManageAddress( var1.getManageAddress() );
        shop.setQrCode( var1.getQrCode() );
        shop.setOkAffirm( var1.getOkAffirm() );
        shop.setOkReserve( var1.getOkReserve() );
        shop.setOkSubMeanu( var1.getOkSubMeanu() );
        List<String> list = var1.getPapersImage();
        if ( list != null ) {
            shop.setPapersImage( new ArrayList<String>( list ) );
        }
        else {
            shop.setPapersImage( null );
        }
        List<String> list1 = var1.getSceneImage();
        if ( list1 != null ) {
            shop.setSceneImage( new ArrayList<String>( list1 ) );
        }
        else {
            shop.setSceneImage( null );
        }
        shop.setDelete( var1.getDelete() );
        List<UpdateShopTimeDTO> list2 = var1.getUpdateShopTime();
        if ( list2 != null ) {
            shop.setUpdateShopTime( new ArrayList<UpdateShopTimeDTO>( list2 ) );
        }
        else {
            shop.setUpdateShopTime( null );
        }

        return shop;
    }

    @Override
    public SubShopVM toSubShopVm(Shop var1) {
        if ( var1 == null ) {
            return null;
        }

        SubShopVM subShopVM = new SubShopVM();

        subShopVM.setId( var1.getId() );
        subShopVM.setInternalId( var1.getInternalId() );
        subShopVM.setParentId( var1.getParentId() );
        subShopVM.setName( var1.getName() );
        subShopVM.setStoreType( var1.getStoreType() );
        subShopVM.setShoptype( var1.getShoptype() );
        subShopVM.setManageAddress( var1.getManageAddress() );
        subShopVM.setCategoryType( var1.getCategoryType() );
        subShopVM.setSubType( var1.getSubType() );
        subShopVM.setOperationType( var1.getOperationType() );
        subShopVM.setDeliveryType( var1.getDeliveryType() );
        subShopVM.setDeliveryStation( var1.getDeliveryStation() );
        subShopVM.setFactoryId( var1.getFactoryId() );
        subShopVM.setInventoryWarning( var1.getInventoryWarning() );
        Map<String, BusyConfiguration> map = var1.getBusyConfigurationsMaps();
        if ( map != null ) {
            subShopVM.setBusyConfigurationsMaps( new HashMap<String, BusyConfiguration>( map ) );
        }
        else {
            subShopVM.setBusyConfigurationsMaps( null );
        }
        subShopVM.setFloor( var1.getFloor() );
        subShopVM.setPmc( var1.getPmc() );
        subShopVM.setCentre( var1.getCentre() );
        subShopVM.setOperator( var1.getOperator() );
        subShopVM.setOperatorMsg( var1.getOperatorMsg() );
        subShopVM.setProgramManager( var1.getProgramManager() );
        subShopVM.setPickingAddress( var1.getPickingAddress() );
        subShopVM.setDescription( var1.getDescription() );
        subShopVM.setTel( var1.getTel() );
        subShopVM.setOrderConditions( var1.getOrderConditions() );
        subShopVM.setStartDeskNo( var1.getStartDeskNo() );
        subShopVM.setSeatCount( var1.getSeatCount() );
        subShopVM.setOpeningStatus( var1.getOpeningStatus() );
        subShopVM.setOpenTime( var1.getOpenTime() );
        subShopVM.setEndTime( var1.getEndTime() );
        subShopVM.setStartTime( var1.getStartTime() );
        subShopVM.setAuditStatus( var1.getAuditStatus() );
        subShopVM.setPeopleNum( var1.getPeopleNum() );
        subShopVM.setOperationTime( var1.getOperationTime() );
        subShopVM.setQrCode( var1.getQrCode() );
        subShopVM.setQrCodeText( var1.getQrCodeText() );
        subShopVM.setOkAffirm( var1.getOkAffirm() );
        subShopVM.setOkReserve( var1.getOkReserve() );
        subShopVM.setReserveDay( var1.getReserveDay() );
        subShopVM.setOkSubMeanu( var1.getOkSubMeanu() );
        List<String> list = var1.getPapersImage();
        if ( list != null ) {
            subShopVM.setPapersImage( new ArrayList<String>( list ) );
        }
        else {
            subShopVM.setPapersImage( null );
        }
        List<String> list1 = var1.getSceneImage();
        if ( list1 != null ) {
            subShopVM.setSceneImage( new ArrayList<String>( list1 ) );
        }
        else {
            subShopVM.setSceneImage( null );
        }
        subShopVM.setForFigure( var1.getForFigure() );
        subShopVM.setManageScope( var1.getManageScope() );
        subShopVM.setOkManageAddress( var1.getOkManageAddress() );
        subShopVM.setAuditOpinion( var1.getAuditOpinion() );
        List<AuditRecord> list2 = var1.getAuditOpinionLogs();
        if ( list2 != null ) {
            subShopVM.setAuditOpinionLogs( new ArrayList<AuditRecord>( list2 ) );
        }
        else {
            subShopVM.setAuditOpinionLogs( null );
        }
        subShopVM.setCreatedTime( var1.getCreatedTime() );
        subShopVM.setDelete( var1.getDelete() );

        return subShopVM;
    }

    @Override
    public InventoryWarning toInventoryWarning(Shop var1) {
        if ( var1 == null ) {
            return null;
        }

        InventoryWarning inventoryWarning = new InventoryWarning();

        String inventoryVaue = var1InventoryWarningInventoryVaue( var1 );
        if ( inventoryVaue != null ) {
            inventoryWarning.setInventoryVaue( inventoryVaue );
        }
        List<String> mobilePhone = var1InventoryWarningMobilePhone( var1 );
        List<String> list = mobilePhone;
        if ( list != null ) {
            inventoryWarning.setMobilePhone( new ArrayList<String>( list ) );
        }
        else {
            inventoryWarning.setMobilePhone( null );
        }
        String copywriting = var1InventoryWarningCopywriting( var1 );
        if ( copywriting != null ) {
            inventoryWarning.setCopywriting( copywriting );
        }
        List<String> warningMethods = var1InventoryWarningWarningMethods( var1 );
        List<String> list1 = warningMethods;
        if ( list1 != null ) {
            inventoryWarning.setWarningMethods( new ArrayList<String>( list1 ) );
        }
        else {
            inventoryWarning.setWarningMethods( null );
        }
        String email = var1InventoryWarningEmail( var1 );
        if ( email != null ) {
            inventoryWarning.setEmail( email );
        }
        if ( var1.getShopInventorytype() != null ) {
            inventoryWarning.setShopInventorytype( var1.getShopInventorytype().name() );
        }

        return inventoryWarning;
    }

    @Override
    public Shop toShopEntity(InventoryWarning var1) {
        if ( var1 == null ) {
            return null;
        }

        Shop shop = new Shop();

        if ( var1.getShopInventorytype() != null ) {
            shop.setShopInventorytype( Enum.valueOf( ShopInventorytype.class, var1.getShopInventorytype() ) );
        }

        return shop;
    }

    @Override
    public Shop toSubShopEntity(NewSubShopDTO var1) {
        if ( var1 == null ) {
            return null;
        }

        Shop shop = new Shop();

        shop.setId( var1.getId() );
        shop.setParentId( var1.getParentId() );
        shop.setName( var1.getName() );
        shop.setStoreType( var1.getStoreType() );
        shop.setOperationType( var1.getOperationType() );
        shop.setDeliveryType( var1.getDeliveryType() );
        shop.setFloor( var1.getFloor() );
        shop.setPmc( var1.getPmc() );
        shop.setPickingAddress( var1.getPickingAddress() );
        shop.setDescription( var1.getDescription() );
        shop.setTel( var1.getTel() );
        shop.setOpeningStatus( var1.getOpeningStatus() );
        shop.setOpenTime( var1.getOpenTime() );
        shop.setDeliveryStation( var1.getDeliveryStation() );
        shop.setManageAddress( var1.getManageAddress() );
        shop.setForFigure( var1.getForFigure() );
        shop.setDelete( var1.getDelete() );
        List<UpdateShopTimeDTO> list = var1.getUpdateShopTime();
        if ( list != null ) {
            shop.setUpdateShopTime( new ArrayList<UpdateShopTimeDTO>( list ) );
        }
        else {
            shop.setUpdateShopTime( null );
        }

        return shop;
    }

    @Override
    public FactoryVM toFactoryVm(Shop var1) {
        if ( var1 == null ) {
            return null;
        }

        FactoryVM factoryVM = new FactoryVM();

        factoryVM.setId( var1.getId() );
        factoryVM.setInternalId( var1.getInternalId() );
        factoryVM.setName( var1.getName() );
        factoryVM.setOperationType( var1.getOperationType() );
        factoryVM.setPmc( var1.getPmc() );
        factoryVM.setPickingAddress( var1.getPickingAddress() );
        factoryVM.setDescription( var1.getDescription() );
        factoryVM.setTel( var1.getTel() );
        factoryVM.setOpeningStatus( var1.getOpeningStatus() );
        factoryVM.setOpenTime( var1.getOpenTime() );
        factoryVM.setCreatedTime( var1.getCreatedTime() );
        factoryVM.setDelete( var1.getDelete() );

        return factoryVM;
    }

    @Override
    public List<FactoryVM> toFactoryVm(List<Shop> var1) {
        if ( var1 == null ) {
            return null;
        }

        List<FactoryVM> list = new ArrayList<FactoryVM>( var1.size() );
        for ( Shop shop : var1 ) {
            list.add( toFactoryVm( shop ) );
        }

        return list;
    }

    @Override
    public Shop toFactoryEntity(NewFactoryDTO var1) {
        if ( var1 == null ) {
            return null;
        }

        Shop shop = new Shop();

        shop.setId( var1.getId() );
        shop.setParentId( var1.getParentId() );
        shop.setName( var1.getName() );
        shop.setShoptype( var1.getShoptype() );
        shop.setOperationType( var1.getOperationType() );
        shop.setDeliveryType( var1.getDeliveryType() );
        shop.setPmc( var1.getPmc() );
        shop.setPickingAddress( var1.getPickingAddress() );
        shop.setDescription( var1.getDescription() );
        shop.setTel( var1.getTel() );
        shop.setOpeningStatus( var1.getOpeningStatus() );
        shop.setOpenTime( var1.getOpenTime() );
        shop.setDelete( var1.getDelete() );
        List<UpdateShopTimeDTO> list = var1.getUpdateShopTime();
        if ( list != null ) {
            shop.setUpdateShopTime( new ArrayList<UpdateShopTimeDTO>( list ) );
        }
        else {
            shop.setUpdateShopTime( null );
        }

        return shop;
    }

    @Override
    public ShopVM toShopVm(Shop var1) {
        if ( var1 == null ) {
            return null;
        }

        ShopVM shopVM = new ShopVM();

        shopVM.setId( var1.getId() );
        shopVM.setInternalId( var1.getInternalId() );
        shopVM.setParentId( var1.getParentId() );
        shopVM.setName( var1.getName() );
        shopVM.setBigShoptype( var1.getBigShoptype() );
        shopVM.setDeliveryType( var1.getDeliveryType() );
        shopVM.setDeliveryStation( var1.getDeliveryStation() );
        shopVM.setFactoryId( var1.getFactoryId() );
        shopVM.setShoptype( var1.getShoptype() );
        shopVM.setSubType( var1.getSubType() );
        shopVM.setOperationType( var1.getOperationType() );
        shopVM.setFloor( var1.getFloor() );
        shopVM.setPmc( var1.getPmc() );
        shopVM.setPickingAddress( var1.getPickingAddress() );
        shopVM.setDescription( var1.getDescription() );
        shopVM.setTel( var1.getTel() );
        shopVM.setOrderConditions( var1.getOrderConditions() );
        if ( var1.getCategoryType() != null ) {
            shopVM.setCategoryType( var1.getCategoryType().name() );
        }
        shopVM.setStartDeskNo( var1.getStartDeskNo() );
        shopVM.setCentre( var1.getCentre() );
        shopVM.setOperator( var1.getOperator() );
        shopVM.setProgramManager( var1.getProgramManager() );
        shopVM.setPeopleNum( var1.getPeopleNum() );
        shopVM.setOperationTime( var1.getOperationTime() );
        shopVM.setSeatCount( var1.getSeatCount() );
        shopVM.setManageAddress( var1.getManageAddress() );
        shopVM.setQrCode( var1.getQrCode() );
        shopVM.setOkReserve( var1.getOkReserve() );
        shopVM.setOkSubMeanu( var1.getOkSubMeanu() );
        List<String> list = var1.getPapersImage();
        if ( list != null ) {
            shopVM.setPapersImage( new ArrayList<String>( list ) );
        }
        else {
            shopVM.setPapersImage( null );
        }
        List<String> list1 = var1.getSceneImage();
        if ( list1 != null ) {
            shopVM.setSceneImage( new ArrayList<String>( list1 ) );
        }
        else {
            shopVM.setSceneImage( null );
        }
        shopVM.setManageScope( var1.getManageScope() );
        shopVM.setAuditStatus( var1.getAuditStatus() );
        shopVM.setAuditOpinion( var1.getAuditOpinion() );
        List<AuditRecord> list2 = var1.getAuditOpinionLogs();
        if ( list2 != null ) {
            shopVM.setAuditOpinionLogs( new ArrayList<AuditRecord>( list2 ) );
        }
        else {
            shopVM.setAuditOpinionLogs( null );
        }
        shopVM.setOpeningStatus( var1.getOpeningStatus() );
        shopVM.setOpenTime( var1.getOpenTime() );
        shopVM.setEndTime( var1.getEndTime() );
        shopVM.setStartTime( var1.getStartTime() );
        shopVM.setCreatedTime( var1.getCreatedTime() );
        shopVM.setDelete( var1.getDelete() );
        List<BusyConfiguration> list3 = var1.getBusyConfigurations();
        if ( list3 != null ) {
            shopVM.setBusyConfigurations( new ArrayList<BusyConfiguration>( list3 ) );
        }
        else {
            shopVM.setBusyConfigurations( null );
        }

        return shopVM;
    }

    @Override
    public ShopTimeVM toShopTimeVm(Shop var1) {
        if ( var1 == null ) {
            return null;
        }

        ShopTimeVM shopTimeVM = new ShopTimeVM();

        shopTimeVM.setId( var1.getId() );
        List<UpdateShopTimeDTO> list = var1.getUpdateShopTime();
        if ( list != null ) {
            shopTimeVM.setUpdateShopTime( new ArrayList<UpdateShopTimeDTO>( list ) );
        }
        else {
            shopTimeVM.setUpdateShopTime( null );
        }

        return shopTimeVM;
    }

    @Override
    public Shop toShopEntity(NewFactoryDTO var1) {
        if ( var1 == null ) {
            return null;
        }

        Shop shop = new Shop();

        shop.setId( var1.getId() );
        shop.setParentId( var1.getParentId() );
        shop.setName( var1.getName() );
        shop.setShoptype( var1.getShoptype() );
        shop.setOperationType( var1.getOperationType() );
        shop.setDeliveryType( var1.getDeliveryType() );
        shop.setPmc( var1.getPmc() );
        shop.setPickingAddress( var1.getPickingAddress() );
        shop.setDescription( var1.getDescription() );
        shop.setTel( var1.getTel() );
        shop.setOpeningStatus( var1.getOpeningStatus() );
        shop.setOpenTime( var1.getOpenTime() );
        shop.setDelete( var1.getDelete() );
        List<UpdateShopTimeDTO> list = var1.getUpdateShopTime();
        if ( list != null ) {
            shop.setUpdateShopTime( new ArrayList<UpdateShopTimeDTO>( list ) );
        }
        else {
            shop.setUpdateShopTime( null );
        }

        return shop;
    }

    @Override
    public Shop toSeatingAreaEntity(NewSeatingAreaDTO var1) {
        if ( var1 == null ) {
            return null;
        }

        Shop shop = new Shop();

        shop.setId( var1.getId() );
        shop.setParentId( var1.getParentId() );
        shop.setName( var1.getName() );
        shop.setFloor( var1.getFloor() );
        shop.setOrderConditions( var1.getOrderConditions() );
        shop.setStartDeskNo( var1.getStartDeskNo() );
        shop.setSeatCount( var1.getSeatCount() );
        shop.setOpeningStatus( var1.getOpeningStatus() );
        shop.setOpenTime( var1.getOpenTime() );
        shop.setDelete( var1.getDelete() );

        return shop;
    }

    @Override
    public SeatingAreaVM toSeatingAreaVm(Shop var1) {
        if ( var1 == null ) {
            return null;
        }

        SeatingAreaVM seatingAreaVM = new SeatingAreaVM();

        seatingAreaVM.setId( var1.getId() );
        seatingAreaVM.setInternalId( var1.getInternalId() );
        seatingAreaVM.setName( var1.getName() );
        seatingAreaVM.setParentId( var1.getParentId() );
        seatingAreaVM.setSubType( var1.getSubType() );
        seatingAreaVM.setFloor( var1.getFloor() );
        seatingAreaVM.setOrderConditions( var1.getOrderConditions() );
        List<String> list = var1.getDiningViewTables();
        if ( list != null ) {
            seatingAreaVM.setDiningViewTables( new ArrayList<String>( list ) );
        }
        else {
            seatingAreaVM.setDiningViewTables( null );
        }
        Map<String, DiningTable> map = var1.getDiningStoreTables();
        if ( map != null ) {
            seatingAreaVM.setDiningStoreTables( new HashMap<String, DiningTable>( map ) );
        }
        else {
            seatingAreaVM.setDiningStoreTables( null );
        }
        seatingAreaVM.setSeatCount( var1.getSeatCount() );
        seatingAreaVM.setOpeningStatus( var1.getOpeningStatus() );
        seatingAreaVM.setAuditStatus( var1.getAuditStatus() );
        seatingAreaVM.setAuditOpinion( var1.getAuditOpinion() );
        List<AuditRecord> list1 = var1.getAuditOpinionLogs();
        if ( list1 != null ) {
            seatingAreaVM.setAuditOpinionLogs( new ArrayList<AuditRecord>( list1 ) );
        }
        else {
            seatingAreaVM.setAuditOpinionLogs( null );
        }
        seatingAreaVM.setCreatedTime( var1.getCreatedTime() );
        seatingAreaVM.setDelete( var1.getDelete() );

        return seatingAreaVM;
    }

    @Override
    public List<SubShopVM> toSubShopVm(List<Shop> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<SubShopVM> list = new ArrayList<SubShopVM>( entityList.size() );
        for ( Shop shop : entityList ) {
            list.add( toSubShopVm( shop ) );
        }

        return list;
    }

    @Override
    public List<BigShopVM> toBigShopVm(List<Shop> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<BigShopVM> list = new ArrayList<BigShopVM>( entityList.size() );
        for ( Shop shop : entityList ) {
            list.add( toBigShopVm( shop ) );
        }

        return list;
    }

    @Override
    public List<SeatingAreaVM> toSeatingAreaVm(List<Shop> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<SeatingAreaVM> list = new ArrayList<SeatingAreaVM>( entityList.size() );
        for ( Shop shop : entityList ) {
            list.add( toSeatingAreaVm( shop ) );
        }

        return list;
    }

    private String var1InventoryWarningInventoryVaue(Shop shop) {
        if ( shop == null ) {
            return null;
        }
        InventoryWarning inventoryWarning = shop.getInventoryWarning();
        if ( inventoryWarning == null ) {
            return null;
        }
        String inventoryVaue = inventoryWarning.getInventoryVaue();
        if ( inventoryVaue == null ) {
            return null;
        }
        return inventoryVaue;
    }

    private List<String> var1InventoryWarningMobilePhone(Shop shop) {
        if ( shop == null ) {
            return null;
        }
        InventoryWarning inventoryWarning = shop.getInventoryWarning();
        if ( inventoryWarning == null ) {
            return null;
        }
        List<String> mobilePhone = inventoryWarning.getMobilePhone();
        if ( mobilePhone == null ) {
            return null;
        }
        return mobilePhone;
    }

    private String var1InventoryWarningCopywriting(Shop shop) {
        if ( shop == null ) {
            return null;
        }
        InventoryWarning inventoryWarning = shop.getInventoryWarning();
        if ( inventoryWarning == null ) {
            return null;
        }
        String copywriting = inventoryWarning.getCopywriting();
        if ( copywriting == null ) {
            return null;
        }
        return copywriting;
    }

    private List<String> var1InventoryWarningWarningMethods(Shop shop) {
        if ( shop == null ) {
            return null;
        }
        InventoryWarning inventoryWarning = shop.getInventoryWarning();
        if ( inventoryWarning == null ) {
            return null;
        }
        List<String> warningMethods = inventoryWarning.getWarningMethods();
        if ( warningMethods == null ) {
            return null;
        }
        return warningMethods;
    }

    private String var1InventoryWarningEmail(Shop shop) {
        if ( shop == null ) {
            return null;
        }
        InventoryWarning inventoryWarning = shop.getInventoryWarning();
        if ( inventoryWarning == null ) {
            return null;
        }
        String email = inventoryWarning.getEmail();
        if ( email == null ) {
            return null;
        }
        return email;
    }
}
