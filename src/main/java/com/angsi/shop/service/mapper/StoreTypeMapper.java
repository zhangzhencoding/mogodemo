package com.angsi.shop.service.mapper;

import com.angsi.shop.VM.StoreTypeVM;
import com.angsi.shop.domain.StoreType;
import com.angsi.shop.dto.StoreType.NewStoreTypeDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface StoreTypeMapper extends EntityMapper<NewStoreTypeDTO, StoreType> {
  StoreTypeVM toVM(StoreType StoreType);

  List<StoreTypeVM> toVMList(List<StoreType> entityList);
}
