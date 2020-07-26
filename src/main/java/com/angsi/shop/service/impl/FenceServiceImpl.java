package com.angsi.shop.service.impl;

import com.angsi.shop.VM.FenceVM;
import com.angsi.shop.domain.Fence;
import com.angsi.shop.dto.Fence.FenceDTO;
import com.angsi.shop.dto.Fence.FenceQO;
import com.angsi.shop.repository.fence.FenceRepository;
import com.angsi.shop.service.FenceService;
import com.angsi.shop.service.mapper.FenceMapper;
import com.angsi.shop.utils.StringUtil;
import com.basic.starter.dubbo.annotation.Provider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Provider
public class FenceServiceImpl implements FenceService {

    @Autowired
    private FenceRepository fenceRepository;

    @Autowired
    private FenceMapper fenceMapper;

    @Override
    public FenceVM createOrUpdate(FenceDTO dto) {
        return fenceMapper.toVM(fenceRepository.save(fenceMapper.toEntity(dto)));
    }

    @Override
    public List<FenceVM> createOrUpdateBatch(List<FenceDTO> fenceDTOList) {

       return   fenceDTOList.stream().map(
                fenceDTO -> {
                    Fence fence=null;
                    if (fenceDTO.getId()!=null && !StringUtil.isEmpty(fenceDTO.getId()))
                    {
                        fence=fenceRepository.update(fenceMapper.toEntity(fenceDTO));
                    }else {
                        fenceDTO.setId(null);
                        fence=  fenceRepository.insert(fenceMapper.toEntity(fenceDTO));
                    }
                    return fenceMapper.toVM(fence);
                }
        ).collect(Collectors.toList());

    }

    @Override
    public List<FenceVM> findAll(FenceQO query) {
        return fenceMapper.toVMList(fenceRepository.find(query.toQuery()));
    }
}
