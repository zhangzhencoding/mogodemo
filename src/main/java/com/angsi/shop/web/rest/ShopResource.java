package com.angsi.shop.web.rest;
import com.angsi.shop.VM.ShopVM;
import com.angsi.shop.domain.Shop;
import com.angsi.shop.dto.shop.NewShopDTO;
import com.angsi.shop.dto.shop.UpdateShopDTO;
import com.angsi.shop.service.ShopBizService;
import com.angsi.shop.service.ShopService;
import com.basic.core.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing
 */
@Api(tags = "Shop API", description = "门店接口")
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class ShopResource {

    @Autowired
    private  ShopBizService shopMsService;

    @Autowired
    private  ShopService shopService;

    private static final String ENTITY_NAME = "shop";

    @GetMapping("/shops/{id}")
    @ApiOperation(value = "创建门店")
    public Shop get(@PathVariable String id){

        //todo 分页查询自带店铺类型
        return shopService.findShopById(id);
    }

    /**
     *
     * @param dto
     * @return
     */
    @PostMapping("/shops")
    @ApiOperation(value = "创建门店")
    public ShopVM createShop(@RequestBody NewShopDTO dto){
        log.debug("REST request to save Shop : {}", dto);
        if (dto.getId() != null) {
            throw new BusinessException("message.noid","\\u521B\\u5EFA\\u4E0D\\u80FD\\u4F20\\u5165ID","zh_CN");
        }
        //todo 分页查询自带店铺类型
        return shopMsService.createShop(dto);
    }

    /**
     *
     * @param dto
     * @return
     */
    @PutMapping("/shops")
    @ApiOperation(value = "更新")
    public ShopVM updateShop(@RequestBody UpdateShopDTO dto){
        log.debug("REST request to update Shop : {}", dto);
        if (dto.getId() == null) {
            throw new BusinessException("message.noid","Id\\u4E0D\\u80FD\\u4E3A\\u7A7A","zh_CN");
        }
        ShopVM result = shopMsService.updateShop(dto);
        return result;
    }




}
