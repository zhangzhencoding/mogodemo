package com.angsi.shop.service.mapper;

import com.angsi.shop.VM.BusinessConfigurationVM;
import com.angsi.shop.VM.FenceVM;
import com.angsi.shop.domain.BusinessConfiguration;
import com.angsi.shop.domain.Fence;
import com.angsi.shop.dto.Fence.FenceDTO;
import com.angsi.shop.dto.configuration.BusinessConfigurationDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface BusinessConfigurationMapper extends EntityMapper<BusinessConfigurationDTO, BusinessConfiguration> {

  BusinessConfigurationVM toVM(BusinessConfiguration businessConfiguration);

  List<BusinessConfigurationVM> toVMList(List<BusinessConfiguration> entityList);

}
