package com.angsi.shop.web.rest;

import com.angsi.shop.dto.common.InventoryWarning;
import com.angsi.shop.service.ShopBizService;
import com.basic.core.exception.BusinessException;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "InventoryWarn API", description = "门店/工厂/大店/子门店库存设置接口")
@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class InventoryWarningResource {


    private final ShopBizService shopMsService;

    private static final String ENTITY_NAME = "InventoryWarning";

    /**
     * 新增/保存门店预警设置
     * @param dto
     */
    @PostMapping("/inventory-warning")
    @ApiOperation(value = "新增/保存门店预警设置")
    public InventoryWarning createInventoryWarning(@ApiParam("请求体") @RequestBody @Valid InventoryWarning dto) {
        log.debug("REST request to save InventoryWarning : {}", dto);
//        if (dto.getId() == null) {
//            throw new BusinessException("message.noid","Id\\u4E0D\\u80FD\\u4E3A\\u7A7A","zh_CN");
//        }
        InventoryWarning result = shopMsService.updateInventoryWarning(dto);
        return result;
    }

    /**
     * 查询门店预警设置
     * @param id
     * @return
     */
    @GetMapping("/inventory-warning/{id}")
    @Timed
    @ApiOperation(value = "查询门店/工厂/大店/子门店库存预警详情")
    public InventoryWarning getInventoryWarning(@PathVariable String id) {
        log.debug("REST request to get InventoryWarning : {}", id);
        return shopMsService.findShopInventoryWarning(id);
    }

}
