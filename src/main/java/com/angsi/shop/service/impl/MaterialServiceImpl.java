package com.angsi.shop.service.impl;

import com.angsi.shop.VM.MaterialVM;
import com.angsi.shop.domain.Material;
import com.angsi.shop.dto.material.MaterialQO;
import com.angsi.shop.dto.material.NewMaterialDTO;
import com.angsi.shop.dto.material.UpdateMaterialDTO;
import com.angsi.shop.repository.material.MaterialRepository;
import com.angsi.shop.service.MaterialService;
import com.angsi.shop.service.mapper.MaterialMapper;
import com.basic.core.domain.PageDTO;
import com.basic.core.exception.BusinessException;
import com.basic.starter.dubbo.annotation.Provider;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Provider
@Slf4j
public class MaterialServiceImpl implements MaterialService {
  @Autowired
  private MaterialRepository materialRepository;

  @Autowired
  private MaterialMapper materialMapper;

  @Override
  public List<MaterialVM> updateMaterialInventory(UpdateMaterialDTO[] dto) {
    List<MaterialVM> lists = new ArrayList<>();
    for (int i=0;i<dto.length;i++) {
      MaterialVM materialVM=new MaterialVM();
      materialVM = updateMaterial(dto[i]);
      lists.add(materialVM);
    }
    return lists;
  }

  @Override
  public MaterialVM createMaterial(NewMaterialDTO dto) {
    log.debug("Request to save NewMaterialDTO : {}", dto);
    Material material = materialMapper.toEntity(dto);
    material.setInventory(0l);
    material.setCreatedTime(new Date());
    return materialMapper.toVM(
      materialRepository.save(material)
    );
  }

  @Override
  public MaterialVM updateMaterial(UpdateMaterialDTO dto) {
    return materialRepository
      .findById(dto.getId())
      .map(
        material -> {
          //原料名称
          if(dto.getName()!=null&&!"".equals(dto.getName())){
            material.setName(dto.getName());
          }
          if(dto.getStandard()!=null&&!"".equals(dto.getStandard())){
            material.setStandard(dto.getStandard());
          }
          if(dto.getUnit()!=null&&!"".equals(dto.getUnit())){
            material.setUnit(dto.getUnit());
          }
          if(dto.getImage()!=null&&!"".equals(dto.getImage())){
            material.setImage(dto.getImage());
          }
          //原料库存
          if(dto.getInventory()!=null&&!"".equals(dto.getInventory())){
            long l=0l;
            if(dto.getInventory()>0){
              l = material.getInventory() + dto.getInventory();
            }
            if(dto.getInventory()<0){
              if(material.getInventory()<dto.getInventory()){
                return new Material().setName("库存不足以抵扣");
              }
              l = material.getInventory() + dto.getInventory();
            }
            material.setInventory(l);
          }
          return materialRepository.save(material);
        }
      )
      .map(materialMapper::toVM)
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
  public PageDTO<MaterialVM> findAllMaterial(
    int current,
    int size,
    MaterialQO query
  ) {
    Page<MaterialVM> page = materialRepository
      .find(query.toQuery(), PageRequest.of(current-1, size))
      .map(materialMapper::toVM);
    return new PageDTO<>(
      page.getNumber(),
      page.getSize(),
      page.getTotalElements(),
      page.getContent()
    );
  }

  @Override
  public List<MaterialVM> findAllMaterial(MaterialQO query) {
    return materialMapper.toVMList(materialRepository.find(query.toQuery()));
  }

  @Override
  public MaterialVM findMaterial(String id) {
    return materialRepository
      .findById(id)
      .map(materialMapper::toVM)
      .orElseThrow(
        () ->
          new BusinessException(
            "message.noshop",
            "\\u6839\\u636E\\u6B64id\\u672A\\u627E\\u5230\\u5E97\\u94FA",
            "zh_CN"
          )
      );
  }

  @Override
  public void deleteMaterial(String id) {
    Material material = new Material();
    material.setId(id);
    materialRepository.delete(material);
  }
}
