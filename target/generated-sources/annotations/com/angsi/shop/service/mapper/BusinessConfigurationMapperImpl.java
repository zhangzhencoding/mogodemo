package com.angsi.shop.service.mapper;

import com.angsi.shop.VM.BusinessConfigurationVM;
import com.angsi.shop.domain.BusinessConfiguration;
import com.angsi.shop.dto.common.BusyConfiguration;
import com.angsi.shop.dto.configuration.BusinessConfigurationDTO;
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
public class BusinessConfigurationMapperImpl implements BusinessConfigurationMapper {

    @Override
    public BusinessConfiguration toEntity(BusinessConfigurationDTO dto) {
        if ( dto == null ) {
            return null;
        }

        BusinessConfiguration businessConfiguration = new BusinessConfiguration();

        businessConfiguration.setId( dto.getId() );
        businessConfiguration.setCityTypes( dto.getCityTypes() );
        businessConfiguration.setStore3km( dto.getStore3km() );
        businessConfiguration.setCityTimes( dto.getCityTimes() );
        businessConfiguration.setStore3kmTimes( dto.getStore3kmTimes() );
        businessConfiguration.setTitle( dto.getTitle() );
        businessConfiguration.setText( dto.getText() );
        businessConfiguration.setDeliveryText( dto.getDeliveryText() );
        businessConfiguration.setDeliveryPicUrl( dto.getDeliveryPicUrl() );
        List<BusyConfiguration> list = dto.getBusyConfigurations();
        if ( list != null ) {
            businessConfiguration.setBusyConfigurations( new ArrayList<BusyConfiguration>( list ) );
        }
        else {
            businessConfiguration.setBusyConfigurations( null );
        }

        return businessConfiguration;
    }

    @Override
    public BusinessConfigurationDTO toDto(BusinessConfiguration entity) {
        if ( entity == null ) {
            return null;
        }

        BusinessConfigurationDTO businessConfigurationDTO = new BusinessConfigurationDTO();

        businessConfigurationDTO.setId( entity.getId() );
        businessConfigurationDTO.setCityTypes( entity.getCityTypes() );
        businessConfigurationDTO.setStore3km( entity.getStore3km() );
        businessConfigurationDTO.setCityTimes( entity.getCityTimes() );
        businessConfigurationDTO.setStore3kmTimes( entity.getStore3kmTimes() );
        businessConfigurationDTO.setTitle( entity.getTitle() );
        businessConfigurationDTO.setText( entity.getText() );
        businessConfigurationDTO.setDeliveryText( entity.getDeliveryText() );
        businessConfigurationDTO.setDeliveryPicUrl( entity.getDeliveryPicUrl() );
        List<BusyConfiguration> list = entity.getBusyConfigurations();
        if ( list != null ) {
            businessConfigurationDTO.setBusyConfigurations( new ArrayList<BusyConfiguration>( list ) );
        }
        else {
            businessConfigurationDTO.setBusyConfigurations( null );
        }

        return businessConfigurationDTO;
    }

    @Override
    public List<BusinessConfiguration> toEntity(List<BusinessConfigurationDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<BusinessConfiguration> list = new ArrayList<BusinessConfiguration>( dtoList.size() );
        for ( BusinessConfigurationDTO businessConfigurationDTO : dtoList ) {
            list.add( toEntity( businessConfigurationDTO ) );
        }

        return list;
    }

    @Override
    public List<BusinessConfigurationDTO> toDto(List<BusinessConfiguration> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<BusinessConfigurationDTO> list = new ArrayList<BusinessConfigurationDTO>( entityList.size() );
        for ( BusinessConfiguration businessConfiguration : entityList ) {
            list.add( toDto( businessConfiguration ) );
        }

        return list;
    }

    @Override
    public BusinessConfigurationVM toVM(BusinessConfiguration businessConfiguration) {
        if ( businessConfiguration == null ) {
            return null;
        }

        BusinessConfigurationVM businessConfigurationVM = new BusinessConfigurationVM();

        businessConfigurationVM.setId( businessConfiguration.getId() );
        businessConfigurationVM.setCityTypes( businessConfiguration.getCityTypes() );
        businessConfigurationVM.setStore3km( businessConfiguration.getStore3km() );
        businessConfigurationVM.setCityTimes( businessConfiguration.getCityTimes() );
        businessConfigurationVM.setStore3kmTimes( businessConfiguration.getStore3kmTimes() );
        businessConfigurationVM.setTitle( businessConfiguration.getTitle() );
        businessConfigurationVM.setText( businessConfiguration.getText() );
        businessConfigurationVM.setDeliveryText( businessConfiguration.getDeliveryText() );
        businessConfigurationVM.setDeliveryPicUrl( businessConfiguration.getDeliveryPicUrl() );
        businessConfigurationVM.setOrderClosed( businessConfiguration.getOrderClosed() );
        businessConfigurationVM.setPaymentClosed( businessConfiguration.getPaymentClosed() );
        businessConfigurationVM.setOrderConfirmed( businessConfiguration.getOrderConfirmed() );
        businessConfigurationVM.setOrderExceeded( businessConfiguration.getOrderExceeded() );
        businessConfigurationVM.setOrderDistance( businessConfiguration.getOrderDistance() );
        businessConfigurationVM.setProductionSurplus( businessConfiguration.getProductionSurplus() );
        businessConfigurationVM.setProductionTimeout( businessConfiguration.getProductionTimeout() );
        businessConfigurationVM.setFaultTime( businessConfiguration.getFaultTime() );
        businessConfigurationVM.setTakingTime( businessConfiguration.getTakingTime() );
        businessConfigurationVM.setDeliveryFee( businessConfiguration.getDeliveryFee() );
        businessConfigurationVM.setFullOrder( businessConfiguration.getFullOrder() );
        businessConfigurationVM.setFreeShippingFee( businessConfiguration.getFreeShippingFee() );
        businessConfigurationVM.setQrCode( businessConfiguration.getQrCode() );
        businessConfigurationVM.setQrCodeDesc( businessConfiguration.getQrCodeDesc() );
        List<BusyConfiguration> list = businessConfiguration.getBusyConfigurations();
        if ( list != null ) {
            businessConfigurationVM.setBusyConfigurations( new ArrayList<BusyConfiguration>( list ) );
        }
        else {
            businessConfigurationVM.setBusyConfigurations( null );
        }

        return businessConfigurationVM;
    }

    @Override
    public List<BusinessConfigurationVM> toVMList(List<BusinessConfiguration> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<BusinessConfigurationVM> list = new ArrayList<BusinessConfigurationVM>( entityList.size() );
        for ( BusinessConfiguration businessConfiguration : entityList ) {
            list.add( toVM( businessConfiguration ) );
        }

        return list;
    }
}
