package com.angsi.shop.web.rest;

import com.angsi.shop.VM.BigShopVM;
import com.angsi.shop.VM.SubShopVM;
import com.angsi.shop.config.ApplicationProperties;
import com.angsi.shop.dto.bigshop.BigShopQO;
import com.angsi.shop.dto.bigshop.NewBigShopDTO;
import com.angsi.shop.dto.bigshop.UpdateBigShopDTO;
import com.angsi.shop.dto.subshop.SubShopQO;
import com.angsi.shop.enums.CategoryType;
import com.angsi.shop.enums.Shoptype;
import com.angsi.shop.service.ShopBizService;
import com.basic.core.domain.PageDTO;
import com.basic.core.exception.BusinessException;
import com.vmcshop.mall.common.model.ResultBean;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "BigShop API", description = "大店接口")
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class BigShopResource {


    private final ShopBizService shopMsService;

    private final ApplicationProperties applicationProperties;

    private static final String ENTITY_NAME = "BigShop";

    public BigShopResource(ShopBizService shopMsService, ApplicationProperties applicationProperties) {
        this.shopMsService = shopMsService;
        this.applicationProperties = applicationProperties;
    }

    /**
     * 根据坐标返回大店集合及距离
     */
    @GetMapping("/big-shops/getBigShopRecentList")
    @ApiOperation(value = "根据坐标返回大店集合及距离")
    public ResultBean<List<BigShopVM>> getBigShopRecentList(String longitude,String latitude) {
        log.debug("REST request to save Shop : {}");
        return ResultBean.createBySuccess(shopMsService.getBigShopRecentList(longitude,latitude));
    }

    /**
     * 根据坐标获取距离最近的大店
     */
    @GetMapping("/big-shops/getBigShopRecent")
    @ApiOperation(value = "根据坐标获取距离最近的大店")
    public ResultBean<BigShopVM> getBigShopRecent(String longitude, String latitude) {
        log.debug("REST request to save Shop : {}");
        //如果经纬度未获取到获取指定大店信息 todo 上线后更新为需要到大店 海岸城大店
        BigShopVM bigShopVM = new BigShopVM();
        if(StringUtils.isBlank(longitude)&& StringUtils.isBlank(latitude)){
            //String shopid=applicationProperties.getBigShopId();
            //log.debug("REST request to save shopid : {}"+shopid);
            bigShopVM = shopMsService.findBigShop(applicationProperties.getBigShopId());
        }else {
            List<BigShopVM> bigShopRecentList = shopMsService.getBigShopRecentList(longitude, latitude);
            if(bigShopRecentList!=null){
                bigShopVM = bigShopRecentList.get(0);
                return ResultBean.createBySuccess(bigShopVM);
            }
        }
        return ResultBean.createBySuccess(bigShopVM);
    }

    /**
     * 根据店铺id修改运营类目类别
     */
    @PutMapping("/big-shops/updateCategoryType")
    @ApiOperation(value = "根据shopid修改运营类目类型")
    public BigShopVM updateCategoryType(@RequestParam String shopId, CategoryType categoryType) {
        log.debug("REST request to save Shop : {}", shopId);
        return shopMsService.updateBigShopCategoryType(shopId,categoryType);
    }

    /**
     * 获取大店下子门店列表
     */
    @GetMapping("/big-shops/getSubShopByShopIds")
    @ApiOperation(value = "获取大店下子门店列表所有")
    public List<SubShopVM> getSubShopByShopIds(SubShopQO query) {
        log.debug("REST request to save Shop : {}", query);
        query.setShopType(String.valueOf(Shoptype.SUBSHOP));
        query.setDelete(0);
        query.setOpeningStatus(1);
        query.setOperationType("sale");
        return shopMsService.getSubShopByShopId(query);
    }

    /**
     * 获取大店下子门店列表分页
     */
    @GetMapping("/big-shops/getSubShopByShopId")
    @ApiOperation(value = "获取大店下子门店列表分页")
    public PageDTO<SubShopVM> getSubShopByShopId(@RequestParam(defaultValue = "1") int current,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 SubShopQO query) {
        log.debug("REST request to save Shop : {}", query);
        query.setShopType(Shoptype.SUBSHOP.toString());
        query.setDelete(0);
        query.setOpeningStatus(1);
        query.setOperationType("sale");
        return shopMsService.getSubShopByShopId(current,size,query);
    }

    /**
     * 创建大店
     * @param dto
     */
    @PostMapping("/big-shops")
    @ApiOperation(value = "创建大店")
    public BigShopVM createBigShop(@ApiParam("请求体") @RequestBody @Valid NewBigShopDTO dto) {
        log.debug("REST request to save Shop : {}", dto);
        if (dto.getId() != null) {
            throw new BusinessException("message.noid","\\u521B\\u5EFA\\u4E0D\\u80FD\\u4F20\\u5165ID","zh_CN");
        }
        BigShopVM result = shopMsService.createBigShop(dto);
        return result;
    }

    /**
     *
     * @param dto
     * @return
     */
    @PutMapping("/big-shops")
    @ApiOperation(value = "更新大店信息/关闭大店(openingStatus=0)")
    public BigShopVM updateBigShop(@RequestBody UpdateBigShopDTO dto){
        log.debug("REST request to update Shop : {}", dto);
        if (dto.getId() == null) {
            throw new BusinessException("message.noid","Id\\u4E0D\\u80FD\\u4E3A\\u7A7A","zh_CN");
        }
        return  shopMsService.updateBigShop(dto);
    }

    /**
     *
     * @param current
     * @param size
     * @param subquery
     * @return
     */
    @GetMapping("/big-shops")
    @ApiOperation(value = "分页查询")
    public PageDTO<BigShopVM> getAllBigShops(@RequestParam(defaultValue = "1") int current,
                                             @RequestParam(defaultValue = "10") int size,
                                             BigShopQO subquery) {
        log.debug("REST request to get a page of Shops");
        //todo 分页查询自带店铺类型
        subquery.setShopType(Shoptype.BIGSHOP.toString());
        return shopMsService.findAllBigShop(current,size,subquery);
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/big-shops/{id}")
    @Timed
    @ApiOperation(value = "查询详情")
    public BigShopVM getBigShop(@PathVariable String id) {
        log.debug("REST request to get Shop : {}", id);
        return shopMsService.findBigShop(id);
    }

}
