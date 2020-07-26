package com.angsi.shop.service.impl;

import com.angsi.shop.VM.FenceVM;
import com.angsi.shop.domain.Fence;
import com.angsi.shop.domain.ShopLog;
import com.angsi.shop.dto.Fence.FenceDTO;
import com.angsi.shop.dto.Fence.FenceQO;
import com.angsi.shop.repository.ShopLog.ShopLogRepository;
import com.angsi.shop.repository.fence.FenceRepository;
import com.angsi.shop.service.FenceService;
import com.angsi.shop.service.ShopLogService;
import com.angsi.shop.service.mapper.FenceMapper;
import com.angsi.shop.utils.StringUtil;
import com.basic.starter.dubbo.annotation.Provider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Provider
public class ShopLogServiceImpl implements ShopLogService {

    @Autowired
    ShopLogRepository shopLogRepository;

    @Override
    public ShopLog createOrUpdate(ShopLog dto) {
        return shopLogRepository.save(dto);
    }
}
