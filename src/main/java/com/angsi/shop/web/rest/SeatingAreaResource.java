package com.angsi.shop.web.rest;

import com.angsi.shop.VM.SeatingAreaVM;
import com.angsi.shop.dto.common.DiningTable;
import com.angsi.shop.dto.seatingarea.*;
import com.angsi.shop.service.ShopBizService;
import com.basic.core.domain.PageDTO;
import com.basic.core.exception.BusinessException;
import com.vmcshop.mall.common.constant.CookieConstant;
import com.vmcshop.mall.common.enums.commodity.Shoptype;
import com.vmcshop.mall.common.model.PageResultBean;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(tags = "SeatingArea API", description = "大店子门店座位区接口")
@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class SeatingAreaResource {

    private final ShopBizService subShopMsService;

    private static final String ENTITY_NAME = "SeatingArea";

    /**
     * 创建子门店座位区
     * @param newSubShopDTO
     */
    @PostMapping("/seating-areas")
    @ApiOperation(value = "创建子门店座位区")
    public SeatingAreaVM createSeatingArea(@RequestBody @Valid NewSeatingAreaDTO newSubShopDTO) {
        log.debug("REST request to save Shop : {}", newSubShopDTO);
        if (newSubShopDTO.getId() != null) {
            throw new BusinessException("message.noid","\\u521B\\u5EFA\\u4E0D\\u80FD\\u4F20\\u5165ID","zh_CN");
        }
        //todo 分页查询自带店铺类型
        return subShopMsService.createSeatingArea(newSubShopDTO);
    }

    /**
     *
     * @param dto
     * @return
     */
    @PutMapping("/seating-areas")
    @ApiOperation(value = "更新子门店座位区")
    public SeatingAreaVM updateSeatingArea(@RequestBody UpdateSeatingAreaDTO dto){
        log.debug("REST request to update Shop : {}", dto);
        if (dto.getId() == null) {
            throw new BusinessException("message.noid","Id\\u4E0D\\u80FD\\u4E3A\\u7A7A","zh_CN");
        }
        return subShopMsService.updateSeatingArea(dto);
    }

    @GetMapping("/seating-areas/getdining-table")
    @ApiOperation(value = "获取大店座位区桌台号详情")
    public DiningTable getDiningTable(@RequestParam("shopId") String shopId,@RequestParam("DeskNumber") String DeskNumber){
        log.debug("REST request to update Shop : {}", shopId);
        if (shopId == null) {
            throw new BusinessException("message.noid","Id\\u4E0D\\u80FD\\u4E3A\\u7A7A","zh_CN");
        }
        return subShopMsService.getDiningTable(shopId,DeskNumber);
    }

    /**
     * @param dto
     * @return
     */
    @PutMapping("/seating-areas/dining-table")
    @ApiOperation(value = "更新大店座位区桌台号,删除,新增桌台")
    public SeatingAreaVM updateDiningTable(@RequestBody UpdateDiningTableDTO dto){
        log.debug("REST request to update Shop : {}", dto);
        if (dto.getId() == null) {
            throw new BusinessException("message.noid","Id\\u4E0D\\u80FD\\u4E3A\\u7A7A","zh_CN");
        }
        return subShopMsService.updateDiningTable(dto);
    }

    /**
     *
     * @param current
     * @param size
     * @param
     * @return
     */
    @GetMapping("/seating-areas")
    @ApiOperation(value = "分页查询大店座位区")
    public PageDTO<SeatingAreaVM> getAllSeatingArea(@RequestParam(defaultValue = "1") int current,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    SeatingAreaQO query) {
        log.debug("REST request to get a page of Shops");
        query.setShopType(Shoptype.SEATINGAREA.toString());
        query.setDelete(0);
        return subShopMsService.getAllSeatingArea(current,size,query);
    }

    /**
     *
     * @param query
     * @return
     */
    @GetMapping("/seating-areas/list")
    @ApiOperation(value = "查询大店座位区")
    public List<SeatingAreaVM> listSeatingArea(SeatingAreaQO query) {
        log.debug("REST request to get a page of Shops");
        query.setShopType(Shoptype.SEATINGAREA.toString());
        query.setDelete(0);
        return subShopMsService.listSeatingArea(query);
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/seating-areas/{id}")
    @Timed
    @ApiOperation(value = "查询详情大店座位区")
    public SeatingAreaVM getSeatingArea(@PathVariable String id) {
        log.debug("REST request to get Shop : {}", id);
        return subShopMsService.getSeatingArea(id);
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/dining-table/{id}")
    @Timed
    @ApiOperation(value = "查询详情大店座位区桌台列表所有")
    public List<DiningTable> getDiningTables(@PathVariable String id) {
        log.debug("REST request to get DiningTable : {}", id);
        return subShopMsService.listDiningTables(id);
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/dining-table/page/{id}")
    @Timed
    @ApiOperation(value = "查询详情大店座位区桌台列表分页")
    public PageResultBean<DiningTable> getDiningTables(@RequestParam(defaultValue = "1") int current,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @PathVariable String id) {
        log.debug("REST request to get DiningTable : {}", id);
        List<DiningTable> diningTables = subShopMsService.listDiningTables(id);
        ArrayList<DiningTable> diningTables1 = new ArrayList<>();
        for (int i=current-1;i<size&&i<diningTables.size();i++){
            if(diningTables.get(i)!=null){
                diningTables1.add(diningTables.get(i));
            }
        }
        PageResultBean<DiningTable> diningTable = new PageResultBean<>();
        diningTable.setTotal(diningTables1.size());
        diningTable.setRows(diningTables1);
        diningTable.setCurrent(current);
        diningTable.setPageSize(size);
        return diningTable;
    }
}
