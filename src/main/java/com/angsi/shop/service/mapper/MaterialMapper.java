package com.angsi.shop.service.mapper;

import com.angsi.shop.VM.MaterialVM;
import com.angsi.shop.domain.Material;
import com.angsi.shop.dto.material.NewMaterialDTO;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface MaterialMapper extends EntityMapper<NewMaterialDTO, Material> {
  MaterialVM toVM(Material material);

  List<MaterialVM> toVMList(List<Material> entityList);
}
