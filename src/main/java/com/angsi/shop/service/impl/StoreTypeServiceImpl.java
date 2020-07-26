package com.angsi.shop.service.impl;
import com.angsi.shop.VM.StoreTypeVM;
import com.angsi.shop.domain.StoreType;
import com.angsi.shop.dto.StoreType.StoreTypeQO;
import com.angsi.shop.dto.StoreType.NewStoreTypeDTO;
import com.angsi.shop.dto.StoreType.UpdateStoreTypeDTO;
import com.angsi.shop.repository.StoreType.StoreTypeRepository;
import com.angsi.shop.service.StoreTypeService;
import com.angsi.shop.service.mapper.StoreTypeMapper;
import com.basic.core.domain.PageDTO;
import com.basic.core.exception.BusinessException;
import com.basic.starter.dubbo.annotation.Provider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.Date;
import java.util.List;

@Provider
@Slf4j
public class StoreTypeServiceImpl implements StoreTypeService {
  @Autowired
  private StoreTypeRepository StoreTypeRepository;

  @Autowired
  private StoreTypeMapper StoreTypeMapper;

  @Override
  public StoreTypeVM createStoreType(NewStoreTypeDTO dto) {
    log.debug("Request to save NewStoreTypeDTO : {}", dto);
    StoreType StoreType = StoreTypeMapper.toEntity(dto);
    StoreType.setCreatedTime(new Date());
    return StoreTypeMapper.toVM(
      StoreTypeRepository.save(StoreType)
    );
  }

  @Override
  public StoreTypeVM updateStoreType(UpdateStoreTypeDTO dto) {
    return StoreTypeRepository
      .findById(dto.getId())
      .map(
        shop -> {
          shop.setName(dto.getName());
          return StoreTypeRepository.save(shop);
        }
      )
      .map(StoreTypeMapper::toVM)
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
  public PageDTO<StoreTypeVM> findAllStoreType(
    int current,
    int size,
    StoreTypeQO query
  ) {
    Page<StoreTypeVM> page = StoreTypeRepository
      .find(query.toQuery(), PageRequest.of(current-1, size))
      .map(StoreTypeMapper::toVM);
    return new PageDTO<>(
      page.getNumber(),
      page.getSize(),
      page.getTotalElements(),
      page.getContent()
    );
  }

  @Override
  public List<StoreTypeVM> findAllStoreType(StoreTypeQO query) {
    return StoreTypeMapper.toVMList(StoreTypeRepository.find(query.toQuery()));
  }

  @Override
  public StoreTypeVM findStoreType(String id) {
    return StoreTypeRepository
      .findById(id)
      .map(StoreTypeMapper::toVM)
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
  public void deleteStoreType(String id) {
    StoreType StoreType = new StoreType();
    StoreType.setId(id);
    StoreTypeRepository.delete(StoreType);
  }
}
