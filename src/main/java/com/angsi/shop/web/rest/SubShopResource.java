package com.angsi.shop.web.rest;

import com.angsi.shop.VM.SubShopVM;
import com.angsi.shop.dto.common.DeliveryStationVO;
import com.angsi.shop.dto.subshop.NewSubShopDTO;
import com.angsi.shop.dto.subshop.SubShopQO;
import com.angsi.shop.dto.subshop.UpdateSubShopDTO;
import com.angsi.shop.enums.Shoptype;
import com.angsi.shop.service.ShopBizService;
import com.basic.core.domain.PageDTO;
import com.basic.core.exception.BusinessException;
import com.vmcshop.mall.common.model.ResultBean;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "SubShop API", description = "大店子门店接口")
@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class SubShopResource {

    private final ShopBizService subShopMsService;
    private static final String ENTITY_NAME = "SubShop";

    /**
     * 获取所有配送点
     */
    @GetMapping("/sub-shops/station/list")
    @ApiOperation(value = "配送站点列表所有")
    public ResultBean<List<DeliveryStationVO>> getStationList() {
        return subShopMsService.getStationList();
    }

    /**
     * 子门店关联配送点
     * @param id 配送站id
     * @param shopId 子门店id
     */
    @PutMapping("/sub-shops/subShopDeliveryDot")
    @ApiOperation(value = "子门店关联配送点")
    public SubShopVM subShopDeliveryDot(@RequestParam("id") String id,
                                        @RequestParam("shopId") String shopId) {
        log.debug("REST request to save Shop : {}", id,shopId);
        UpdateSubShopDTO dto = new UpdateSubShopDTO();
        dto.setId(shopId);
//        dto.setDeliveryStation(id);
        return subShopMsService.updateSubShop(dto);
    }

    /**
     * 创建子门店
     * @param newSubShopDTO
     */
    @PostMapping("/sub-shops")
    @ApiOperation(value = "创建大店子门店")
    public SubShopVM createSubShop(@RequestBody @Valid NewSubShopDTO newSubShopDTO) {
        log.debug("REST request to save Shop : {}", newSubShopDTO);
        if (newSubShopDTO.getId() != null) {
            throw new BusinessException("message.noid","\\u521B\\u5EFA\\u4E0D\\u80FD\\u4F20\\u5165ID","zh_CN");
        }
        return subShopMsService.createSubShop(newSubShopDTO);
    }

    /**
     *
     * @param dto
     * @return
     */
    @PutMapping("/sub-shops")
    @ApiOperation(value = "更新子门店信息/关闭子门店(openingStatus=0)")
    public SubShopVM updateSubShop(@RequestBody UpdateSubShopDTO dto){
        log.debug("REST request to update Shop : {}", dto);
        if (dto.getId() == null) {
            throw new BusinessException("message.noid","Id\\u4E0D\\u80FD\\u4E3A\\u7A7A","zh_CN");
        }
        return subShopMsService.updateSubShop(dto);
    }

    /**
     *
     * @param current
     * @param size
     * @param subquery
     * @return
     */
    @GetMapping("/sub-shops")
    @ApiOperation(value = "分页查询")
    public PageDTO<SubShopVM> getAllSubShops(@RequestParam(defaultValue = "1") int current,
                                             @RequestParam(defaultValue = "10") int size,
                                             SubShopQO subquery) {
        log.debug("REST request to get a page of Shops");
        subquery.setDelete(0);
        subquery.setShopType(Shoptype.SUBSHOP.toString());
        return subShopMsService.findAllSubShop(current,size,subquery);
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/sub-shops/{id}")
    @Timed
    @ApiOperation(value = "查询详情")
    public SubShopVM getSubShop(@PathVariable String id) {
        log.debug("REST request to get Shop : {}", id);
        return subShopMsService.findSubShop(id);
    }

}
