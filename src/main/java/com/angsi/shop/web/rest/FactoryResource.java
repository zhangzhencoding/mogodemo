package com.angsi.shop.web.rest;

import com.angsi.shop.VM.FactoryVM;
import com.angsi.shop.dto.factory.FactoryQO;
import com.angsi.shop.dto.factory.NewFactoryDTO;
import com.angsi.shop.dto.factory.UpadteFactoryDTO;
import com.angsi.shop.service.ShopBizService;
import com.basic.core.domain.PageDTO;
import com.basic.core.exception.BusinessException;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing
 */
@Api(tags = "Factory API", description = "工厂接口")
@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class FactoryResource {

    private final ShopBizService factoryMsService;

    private static final String ENTITY_NAME = "factory";

    @GetMapping("/getFirstFactories")
    @ApiOperation(value = "获取中央工厂下的一级工厂(parentId)")
    public PageDTO<FactoryVM> getFirstFactories(@RequestParam(defaultValue = "1") int current,
                                                @RequestParam(defaultValue = "10") int size,
                                                FactoryQO query){
        log.debug("REST request to save Shop : {}", query);
        return factoryMsService.getFirstFactories(current,size,query);
    }
    /**
     *
     * @param dto
     * @return
     */
    @PostMapping("/factories")
    @ApiOperation(value = "创建工厂")
    public FactoryVM createFactory(@RequestBody NewFactoryDTO dto){
        log.debug("REST request to save Shop : {}", dto);
        return factoryMsService.createFactory(dto);
    }

    /**
     *
     * @param dto
     * @return
     */
    @PutMapping("/factories")
    @ApiOperation(value = "更新")
    public FactoryVM updateFactory(@RequestBody UpadteFactoryDTO dto){
        log.debug("REST request to update Shop : {}", dto);
        if (dto.getId() == null) {
            throw new BusinessException("message.noid","Id\\u4E0D\\u80FD\\u4E3A\\u7A7A","zh_CN");
        }
        return factoryMsService.updateFactory(dto);
    }

    /**
     *
     * @param current
     * @param size
     * @param subquery
     * @return
     */
    @GetMapping("/factories")
    @ApiOperation(value = "分页查询")
    public PageDTO<FactoryVM> getAllFactories(@RequestParam(defaultValue = "1") int current,
                                   @RequestParam(defaultValue = "10") int size,
                                    FactoryQO subquery) {
        log.debug("REST request to get a page of Shops");
         //todo 分页查询自带店铺类型
        return factoryMsService.findAllFactory(current,size,subquery);
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/factories/{id}")
    @Timed
    @ApiOperation(value = "查询详情")
    public FactoryVM getFactory(@PathVariable String id) {
        log.debug("REST request to get Shop : {}", id);
        return factoryMsService.findFactory(id);
    }

}
