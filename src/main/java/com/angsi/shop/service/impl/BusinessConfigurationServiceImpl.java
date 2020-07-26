package com.angsi.shop.service.impl;

import com.angsi.shop.VM.BusinessConfigurationVM;
import com.angsi.shop.domain.BusinessConfiguration;
import com.angsi.shop.dto.common.BusyConfiguration;
import com.angsi.shop.dto.configuration.BusinessConfigurationDTO;
import com.angsi.shop.dto.configuration.BusinessDeliveryConfigurationDTO;
import com.angsi.shop.dto.configuration.BusinessOrderConfigurationDTO;
import com.angsi.shop.dto.configuration.BusinessQrcodeConfigurationDTO;
import com.angsi.shop.repository.configuration.BusinessConfigurationRepository;
import com.angsi.shop.service.BusinessConfigurationService;
import com.angsi.shop.service.mapper.BusinessConfigurationMapper;
import com.basic.core.exception.BusinessException;
import com.basic.starter.dubbo.annotation.Provider;
import com.vmcshop.mall.common.util.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Provider
@Slf4j
public class BusinessConfigurationServiceImpl implements BusinessConfigurationService {

    @Autowired
    private BusinessConfigurationRepository businessConfigurationRepository;

    @Autowired
    private BusinessConfigurationMapper businessConfigurationMapper;


    @Override
    public BusinessConfigurationVM createOrUpdate(BusinessConfigurationDTO dto) {
        return businessConfigurationRepository.findById(dto.getId()).map(
                businessConfiguration -> {
                    businessConfiguration.setCityTimes(dto.getCityTimes());
                    businessConfiguration.setCityTypes(dto.getCityTypes());
                    businessConfiguration.setStore3km(dto.getStore3km());
                    businessConfiguration.setStore3kmTimes(dto.getStore3kmTimes());
                    businessConfiguration.setTitle(dto.getTitle());
                    businessConfiguration.setText(dto.getText());
                    businessConfiguration.setDeliveryText(dto.getDeliveryText());
                    businessConfiguration.setDeliveryPicUrl(dto.getDeliveryPicUrl());
                    businessConfiguration.setBusyConfigurations(dto.getBusyConfigurations());
                    businessConfigurationRepository.save(businessConfiguration);
                    return businessConfiguration;
                }
        ).map(businessConfigurationMapper::toVM)
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
    public BusinessConfigurationVM createOrUpdateDeliveryConfiguration(BusinessDeliveryConfigurationDTO dto) {
        return businessConfigurationRepository.findById(dto.getId()).map(
                businessConfiguration -> {
                    businessConfiguration.setDeliveryFee(dto.getDeliveryFee());
                    businessConfiguration.setFullOrder(dto.getFullOrder());
                    businessConfiguration.setFreeShippingFee(dto.getFreeShippingFee());
                    businessConfigurationRepository.save(businessConfiguration);
                    return businessConfiguration;
                }
        ).map(businessConfigurationMapper::toVM)
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
    public BusinessConfigurationVM createOrUpdateOrderConfiguration(BusinessOrderConfigurationDTO dto) {
        return businessConfigurationRepository.findById(dto.getId()).map(
                businessConfiguration -> {
                    businessConfiguration.setTakingTime(dto.getTakingTime());
                    businessConfiguration.setFaultTime(dto.getFaultTime());
                    businessConfiguration.setProductionTimeout(dto.getProductionTimeout());
                    businessConfiguration.setProductionSurplus(dto.getProductionSurplus());
                    businessConfiguration.setOrderDistance(dto.getOrderDistance());
                    businessConfiguration.setOrderExceeded(dto.getOrderExceeded());
                    businessConfiguration.setOrderConfirmed(dto.getOrderConfirmed());
                    businessConfiguration.setOrderClosed(dto.getOrderClosed());
                    businessConfiguration.setPaymentClosed(dto.getPaymentClosed());
                    businessConfiguration.setLeastModifyOrderTimes(dto.getLeastModifyOrderTimes());
                    businessConfiguration.setStartMakingInfo(dto.getStartMakingInfo());
                    businessConfiguration.setStartMakingInfoTips(dto.getStartMakingInfoTips());
                    businessConfigurationRepository.save(businessConfiguration);
                    return businessConfiguration;
                }
        ).map(businessConfigurationMapper::toVM)
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
    public BusinessConfigurationVM createOrUpdateQrcodeConfiguration(BusinessQrcodeConfigurationDTO dto) {
        return businessConfigurationRepository.findById(dto.getId()).map(
                businessConfiguration -> {
                    businessConfiguration.setQrCode(dto.getQrCode());
                    businessConfiguration.setQrCodeDesc(dto.getQrCodeDesc());
                    businessConfigurationRepository.save(businessConfiguration);
                    return businessConfiguration;
                }
        ).map(businessConfigurationMapper::toVM)
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
    public List<BusinessConfigurationVM> findAll(BusinessConfigurationVM query) {
        return businessConfigurationMapper.toVMList(businessConfigurationRepository.findAll());
    }

    @Override
    public BusyConfiguration findBusinessConfigurationVM(String categoryId) {
        List<BusinessConfiguration> all = businessConfigurationRepository.findAll();
        log.info("categoryId=======+{}"+categoryId);
        log.info("all=======+{}"+all.get(0).toString());
        BusinessConfiguration businessConfiguration = null;
        if(CollectionUtil.isNotEmpty(all)) {
            log.info("===========");
           businessConfiguration = all.get(0);
            return businessConfiguration.getBusyConfigurations().stream().filter(busyConfiguration -> busyConfiguration.getCategoryId().equals(categoryId)).findFirst().get();
        }
        return null;
    }


}
