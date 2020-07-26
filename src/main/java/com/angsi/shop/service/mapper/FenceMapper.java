package com.angsi.shop.service.mapper;

import com.angsi.shop.VM.FenceVM;
import com.angsi.shop.VM.MaterialVM;
import com.angsi.shop.domain.Fence;
import com.angsi.shop.domain.Material;
import com.angsi.shop.dto.Fence.FenceDTO;
import com.angsi.shop.dto.material.NewMaterialDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface FenceMapper extends EntityMapper<FenceDTO, Fence> {

  FenceVM toVM(Fence fence);

  List<FenceVM> toVMList(List<Fence> entityList);



}
