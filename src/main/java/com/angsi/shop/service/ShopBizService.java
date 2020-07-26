package com.angsi.shop.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.angsi.shop.VM.*;
import com.angsi.shop.domain.NameLocation;
import com.angsi.shop.domain.Shop;
import com.angsi.shop.dto.BusyConfigurationQO;
import com.angsi.shop.dto.bigshop.BigShopQO;
import com.angsi.shop.dto.bigshop.NewBigShopDTO;
import com.angsi.shop.dto.bigshop.UpdateBigShopDTO;
import com.angsi.shop.dto.common.*;
import com.angsi.shop.dto.factory.FactoryQO;
import com.angsi.shop.dto.factory.NewFactoryDTO;
import com.angsi.shop.dto.factory.UpadteFactoryDTO;
import com.angsi.shop.dto.seatingarea.NewSeatingAreaDTO;
import com.angsi.shop.dto.seatingarea.SeatingAreaQO;
import com.angsi.shop.dto.seatingarea.UpdateDiningTableDTO;
import com.angsi.shop.dto.seatingarea.UpdateSeatingAreaDTO;
import com.angsi.shop.dto.shop.NewShopDTO;
import com.angsi.shop.dto.shop.ShopQO;
import com.angsi.shop.dto.shop.UpdateShopDTO;
import com.angsi.shop.dto.subshop.NewSubShopDTO;
import com.angsi.shop.dto.subshop.SubShopQO;
import com.angsi.shop.dto.subshop.UpdateSubShopDTO;
import com.angsi.shop.enums.CategoryType;
import com.angsi.shop.enums.Shoptype;
import com.angsi.shop.enums.StatusConstants;
import com.angsi.shop.repository.shop.ShopRepository;
import com.angsi.shop.repository.util.GeoUtil;
import com.angsi.shop.service.mapper.ShopMapper;
import com.basic.core.domain.PageDTO;
import com.basic.core.exception.BusinessException;
import com.basic.starter.dubbo.annotation.Consumer;
import com.vmcshop.mall.common.enums.category.OperationCatEnum;
import com.vmcshop.mall.common.model.PageResultBean;
import com.vmcshop.mall.common.model.ResultBean;
import com.vmcshop.mall.common.model.commodity.OrdinaryInfo;
import com.vmcshop.mall.common.model.commodity.SkuStateEnum;
import com.vmcshop.mall.common.model.commodity.bo.SpuInfoBO;
import com.vmcshop.mall.common.model.commodity.dto.QO.ShopSpuQO;
import com.vmcshop.mall.common.model.commodity.dto.saveDTO.ShopSpuSaveDTO;
import com.vmcshop.mall.common.util.CollectionUtil;
import com.vmcshop.mall.dubbo.api.commodity.shop.ShopSpuCategoryService;
import com.vmcshop.mall.dubbo.api.commodity.shop.ShopSpuService;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation
 */
@Service
@Slf4j
public class ShopBizService{
  @Autowired
  private ShopRepository shopRepository;

  @Autowired
  private ShopMapper shopMapper;
  @Resource
  ShopService shopService;
  @Consumer(check = false)
  private ShopSpuCategoryService shopSpuCategoryService;

  public List<BigShopVM> getBigShopRecentList(String longitude,String latitude){

    BigShopQO query = new BigShopQO();
    query.setShopType(String.valueOf(Shoptype.BIGSHOP));
    query.setOpeningStatus(1);
    query.setAuditStatus(1);
    query.setOpeningStatus(1);
    List<BigShopVM> bigShopLists = shopService.getBigShopLists(query);
    if(StringUtil.isBlank(longitude)||StringUtil.isBlank(latitude)){
      //如果坐标不存在直接返回大店集合
      return bigShopLists;
    }
    for (int i=0;i<bigShopLists.size();i++) {
      //获取大店的坐标集合
      NameLocation nameLocation = bigShopLists.get(i).getManageAddress().getNameLocation();
      if(nameLocation!=null&&!"".equals(nameLocation)){
        //得到大店与当前位置间的距离 米
        double temp = GeoUtil.getDistanceOfMeter(Double.valueOf(latitude), Double.valueOf(longitude),
                Double.valueOf(nameLocation.getLatitude()), Double.valueOf(nameLocation.getLongitude()));
        String distance = bigShopLists.get(i).getDistance();
        //设置与大店距离
        bigShopLists.get(i).setDistance(String.valueOf(temp/1000));
      }else{
        //设置与大店距离
        bigShopLists.get(i).setDistance(String.valueOf(0.00));
      }
    }
    bigShopLists.sort((x, y) -> Double.compare(Double.valueOf(x.getDistance()), Double.valueOf(y.getDistance())));
    return bigShopLists;
  }

    public PageDTO<FactoryVM> getFirstFactories(
        int current,
        int size,
        FactoryQO query
    ) {
        return shopService.getFirstFactories(current,size,query);
    }

  public BigShopVM updateBigShopCategoryType(String shopId,CategoryType categoryType) {
    return shopService.updateBigShopCategoryType(shopId,categoryType);
  }

  public List<SubShopVM> getSubShopByShopId(SubShopQO query) {
    List<SubShopVM> subShopVMS = shopService.getSubShopsByShopId(query);
    for (SubShopVM shop:subShopVMS) {
      ShopSpuSaveDTO shopSpuSaveDTO = new ShopSpuSaveDTO();
      shopSpuSaveDTO.setBigShopId(shop.getParentId());
      shopSpuSaveDTO.setShopId(shop.getId());
      shopSpuSaveDTO.setPageNum(1);
      shopSpuSaveDTO.setPageSize(50);
      shopSpuSaveDTO.setDisplayed(1);
      PageResultBean<SpuInfoBO> spus = shopSpuCategoryService.shopSpuPage(shopSpuSaveDTO, String.valueOf(Shoptype.SUBSHOP.getCode()));
      List<SpuInfoBO> rows = spus.getRows();//子门店下商品
      rows = rows.stream().filter(item -> !OperationCatEnum.SYS_UNCATEGORY.val().equals(item.getFcategoryid())).collect(
              Collectors.collectingAndThen(Collectors.toCollection(
                      () -> new TreeSet<>(Comparator.comparing(SpuInfoBO::getSpuId))), ArrayList::new
              )
      );
      if(rows.size()>0){
        List<SpuInfoVO> shopSpuSaveDTOs = new ArrayList<>(5);
        for (SpuInfoBO spu:rows) {
          SpuInfoVO spuInfoVO = new SpuInfoVO();
          BeanUtils.copyProperties(spu,spuInfoVO);
          shopSpuSaveDTOs.add(spuInfoVO);
        }
        shopSpuSaveDTOs.stream().limit(5);
        shop.setShopSpuSaveDTOs(shopSpuSaveDTOs);
      }
    }
    return subShopVMS;
  }

  //调用第三方接口，获取配送站列表
  public ResultBean<List<DeliveryStationVO>> getStationList() {
    RestTemplate restTemplate = new RestTemplate();
    //设置请求头
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
    MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
    //  添加请求的参数
    params.add("hello", "hello");             //必传
    params.add("world", "world");           //选传
    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
    //  执行HTTP请求
    ResponseEntity<String> response = restTemplate.exchange("https://test130-api.angsi.com/management/delivery-app/public/sync/station/list", HttpMethod.GET, requestEntity, String.class);  //最后的参数需要用String.class  使用其他的会报错
    String body = response.getBody();
    List<DeliveryStationVO> deliveryStationVOS = new ArrayList<>();
    if(body != null){
      JSONObject jsonObject = (JSONObject) JSON.parse(body);
      String data = jsonObject.getString("data");
      JSONObject jsonObjects= JSON.parseObject(data);
      String ja = jsonObjects.getJSONArray("data").toString();
      try {
        deliveryStationVOS = JSON.parseArray(ja, DeliveryStationVO.class);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }else{
      ResultBean.createByError("获取失败，请重试");
    }
    return ResultBean.createBySuccess(deliveryStationVOS);
  }

  public PageDTO<SubShopVM> getSubShopByShopId(int current,
                                               int size,
                                               SubShopQO query) {
    log.debug("Request to query newBigShopDTO : {}", query);
    PageDTO<SubShopVM> subShopByShopId = shopService.getSubShopByShopId(current, size, query);
    List<SubShopVM> subShopVMS = subShopByShopId.getRecords();
    //TODO 查询子门店商品
    for (SubShopVM shop:subShopVMS) {
      ShopSpuSaveDTO shopSpuQuery = new ShopSpuSaveDTO();
      shopSpuQuery.setShopId(shop.getId());
      shopSpuQuery.setFskustate(SkuStateEnum.SKU_ONLINE.getProperty());
      shopSpuQuery.setDisplayed(1);
      shopSpuQuery.setPageNum(1);
      shopSpuQuery.setPageSize(5000);
      PageResultBean<SpuInfoBO> spus = shopSpuCategoryService.shopSpuPage(shopSpuQuery, null != shop ? String.valueOf(shop.getShoptype().getCode()) : "");
      List<SpuInfoBO> rows = spus.getRows();//子门店下商品;;
      if(CollectionUtil.isNotEmpty(rows)) {
        rows = rows.stream().filter(item -> !OperationCatEnum.SYS_UNCATEGORY.val().equals(item.getOperateCategoryId())).collect(
                Collectors.collectingAndThen(Collectors.toCollection(
                        () -> new TreeSet<>(Comparator.comparing(SpuInfoBO::getSpuId))), ArrayList::new
                )
        );
        // 过滤掉spu商品的非显示和非上架商品
        rows = rows.stream().filter(item -> (1 == item.getDisplayed())).collect(Collectors.toList());
        if(CollectionUtil.isNotEmpty(rows)) {
          Iterator<SpuInfoBO> spuIerator = rows.iterator();
          while (spuIerator.hasNext()) {
            List<OrdinaryInfo> skuInfos = spuIerator.next().getSkuInfos();
            if(skuInfos.stream().allMatch(item -> item.getFskustate() != 1)) {
              spuIerator.remove();
            }
          }
        }
      }
      if(rows.size()>0){
        List<SpuInfoVO> shopSpuSaveDTOs = new ArrayList<>(5);
        for (SpuInfoBO spu:rows) {
          SpuInfoVO spuInfoVO = new SpuInfoVO();
          BeanUtils.copyProperties(spu,spuInfoVO);
          shopSpuSaveDTOs.add(spuInfoVO);
        }
        shopSpuSaveDTOs.stream().limit(5);
        shop.setShopSpuSaveDTOs(shopSpuSaveDTOs);
      }
    }
    return subShopByShopId;
  }

  public BigShopVM createBigShop(NewBigShopDTO newBigShopDTO) {
    return shopService.createBigShop(newBigShopDTO);
  }

  public SubShopVM createSubShop(NewSubShopDTO dto) {
    return shopService.createSubShop(dto);
  }

  public ShopVM createShop(NewShopDTO shopDTO) {
    return shopService.createShop(shopDTO);
  }

  public FactoryVM createFactory(NewFactoryDTO shopDTO) {
    return shopService.createFactory(shopDTO);
  }


  public SeatingAreaVM createSeatingArea(NewSeatingAreaDTO dto) {
    return shopService.createSeatingArea(dto);
  }


  public SeatingAreaVM updateSeatingArea(
    UpdateSeatingAreaDTO updateSeatingAreaDTO
  ) {
    return shopService.updateSeatingArea(updateSeatingAreaDTO);
  }

  /**
   * 查询桌台号详情
   * @param shopId,DeskNumber
   * @return
   */
  public DiningTable getDiningTable(String shopId,String DeskNumber){
    return shopService.getDiningTable(shopId,DeskNumber);
  }

  public BigShopVM updateBigShop(UpdateBigShopDTO dto) {
    return shopService.updateBigShop(dto,null);
  }

  public ShopVM updateShop(UpdateShopDTO updateShopDTO) {
    return shopService.updateShop(updateShopDTO);
  }


  public FactoryVM updateFactory(UpadteFactoryDTO upadteFactoryDTO) {
    return shopService.updateFactory(upadteFactoryDTO);
  }


    public Shop findShopById(String id) {
        return shopService.findShopById(id);
    }


  public SeatingAreaVM findSeatingArea(String id) {
    return shopService.findSeatingArea(id);
  }


  public SubShopVM updateSubShop(UpdateSubShopDTO dto) {
    return shopService.updateSubShop(dto,null);
  }


  public ShopVM findShop(String id) {
    return shopService.findShop(id);
  }


  public SubShopVM findSubShop(String id) {
    return shopService.findSubShop(id);
  }


  public BigShopVM findBigShop(String id) {
    return shopService.findBigShop(id);
  }


  public FactoryVM findFactory(String id) {
    return shopService.findFactory(id);
  }


  public void delete(String id) {
      shopService.delete(id);
  }


  public PageDTO<SeatingAreaVM> findAllSeatingArea(
    int current,
    int size,
    SeatingAreaQO query
  ) {
    return shopService.findAllSeatingArea(current,size,query);
  }


  public List<SeatingAreaVM> findAllSeatingArea(SeatingAreaQO query) {
    return shopMapper.toSeatingAreaVm(shopRepository.find(query.toQuery()));
  }


  public PageDTO<ShopVM> findAllShop(int current, int size, ShopQO query) {
//      query.setShopType("STORE");
    return shopService.findAllShop(current,size,query);
  }


  public PageDTO<BigShopVM> findAllBigShop(
    int current,
    int size,
    BigShopQO query
  ) {
//      query.setShopType("BIGSHOP");
    return shopService.findAllBigShop(current,size,query);
  }


  public PageDTO<SubShopVM> findAllSubShop(
    int current,
    int size,
    SubShopQO query
  ) {
//      query.setShopType("SUBSHOP");
    return shopService.findAllSubShop(current,size,query);
  }


  public PageDTO<FactoryVM> findAllFactory(
    int current,
    int size,
    FactoryQO query
  ) {
    return shopService.findAllFactory(current,size,query);
  }


  public InventoryWarning createInventoryWarning(InventoryWarning dto) {
    return null;
  }


  public InventoryWarning updateInventoryWarning(
    InventoryWarning inventoryWarning
  ) {
    return shopRepository
      .findById(inventoryWarning.getShopId())
      .map(
        shop -> {
          shop.setInventoryWarning(inventoryWarning);
          return shopRepository.save(shop);
        }
      )
      .map(shopMapper::toInventoryWarning)
      .orElseThrow(
        () ->
          new BusinessException(
            "message.noshopId",
            "\\u5E97\\u94FAId\\u4E0D\\u5B58\\u5728",
            "zh_CN"
          )
      );
  }


  public InventoryWarning findShopInventoryWarning(String id) {
    return shopRepository
      .findById(id)
      .map(shopMapper::toInventoryWarning)
      .orElseThrow(
        () ->
          new BusinessException(
            "message.noshop",
            "\\u6839\\u636E\\u6B64id\\u672A\\u627E\\u5230\\u5E97\\u94FA",
            "zh_CN"
          )
      );
  }


  /**
   * 更新桌台号
   * @param dto
   * @return
   */
  public SeatingAreaVM updateDiningTable(@RequestBody UpdateDiningTableDTO dto){
    log.debug("REST request to update Shop : {}", dto);
    if (dto.getId() == null) {
      throw new BusinessException("message.noid","Id\\u4E0D\\u80FD\\u4E3A\\u7A7A","zh_CN");
    }
    SeatingAreaVM result = shopService.updateDiningTable(dto);
    return result;
  }

  /**
   *
   * @param current
   * @param size
   * @param subquery
   * @return
   */
  public PageDTO<SeatingAreaVM> getAllSeatingArea(int current,int size,
                                                  SeatingAreaQO subquery) {
    log.debug("REST request to get a page of Shops");
    return shopService.findAllSeatingArea(current,size,subquery);
  }

  /**
   *
   * @param subquery
   * @return
   */
  public List<SeatingAreaVM> listSeatingArea(SeatingAreaQO subquery) {
    log.debug("REST request to get a page of Shops");
    return shopService.findAllSeatingArea(subquery);
  }

  /**
   *
   * @param id
   * @return
   */
  public SeatingAreaVM getSeatingArea(String id) {
    log.debug("REST request to get Shop : {}", id);
    SeatingAreaVM shopDTO = shopService.findSeatingArea(id);
    return shopDTO;
  }

  /**
   *
   * @param
   * @return
   */
  public List<DiningTable> listDiningTables(String id) {
    log.debug("REST request to get a page of Shops");
    return shopService.getDiningTables(id);
  }

  public List<DiningTable> getDiningTables(String id) {
    return shopRepository
      .findById(id)
      .map(
        shop -> {
          Map<String, DiningTable> diningTableMap = shop.getDiningStoreTables();
          List<DiningTable> diningTableList = new ArrayList<>();
          if (!diningTableMap.isEmpty()) {
            for (DiningTable diningTable : diningTableMap.values()) {
              diningTableList.add(diningTable);
            }
          }
          return diningTableList;
        }
      )
      .orElseThrow(
        () ->
          new BusinessException(
            "message.nodata",
            "\\u672A\\u67E5\\u8BE2\\u5230\\u6570\\u636E",
            "zh_CN"
          )
      );
  }


  public List<BusyConfiguration> createOrUpdateBusyConfiguration(
    List<BusyConfiguration> busyConfigurations,
    StatusConstants statusConstants
  ) {
    BusyConfigurationQO busyConfigurationQO = new BusyConfigurationQO();
    busyConfigurationQO.setStatusConstants(statusConstants);
    Map<String, BusyConfiguration> busyConfigurationsMaps = new HashMap<String, BusyConfiguration>();
    if (
      shopRepository.find(busyConfigurationQO.toQuery()) != null &&
      shopRepository.find(busyConfigurationQO.toQuery()).size() == 1
    ) {
      Shop shop = shopRepository.findOne(busyConfigurationQO.toQuery()).get();
      if (busyConfigurations != null && busyConfigurations.size() > 0) {
        if (shop.getBusyConfigurationsMaps() != null) {
          busyConfigurationsMaps = shop.getBusyConfigurationsMaps();
        }
        for (BusyConfiguration configuration : busyConfigurations) {
          if (
            busyConfigurationsMaps.containsKey(configuration.getCategoryId())
          ) {
            busyConfigurationsMaps.remove(configuration.getCategoryId());
            busyConfigurationsMaps.put(
              configuration.getCategoryId(),
              configuration
            );
          } else {
            busyConfigurationsMaps.put(
              configuration.getCategoryId(),
              configuration
            );
          }
        }
      }
      shop.setBusyConfigurationsMaps(busyConfigurationsMaps);
      shop.setBusyConfigurations(busyConfigurations);
      shop = shopRepository.save(shop);
      return shop.getBusyConfigurations();
    } else {
      Shop shop = new Shop();
      if (busyConfigurations != null && busyConfigurations.size() > 0) {
        for (BusyConfiguration configuration : busyConfigurations) {
          busyConfigurationsMaps.put(
            configuration.getCategoryId(),
            configuration
          );
        }
      }
      shop.setBusyConfigurations(busyConfigurations);
      shop.setStatusConstants(statusConstants);
      shop.setBusyConfigurationsMaps(busyConfigurationsMaps);
      shop = shopRepository.save(shop);
      return shop.getBusyConfigurations();
    }
  }


  public List<BusyConfiguration> getBusyConfiguration(
    StatusConstants statusConstants
  ) {
    BusyConfigurationQO busyConfigurationQO = new BusyConfigurationQO();
    busyConfigurationQO.setStatusConstants(statusConstants);
    return shopRepository
      .findOne(busyConfigurationQO.toQuery())
      .map(
        shop -> {
          return shop.getBusyConfigurations();
        }
      )
      .orElseThrow(
        () ->
          new BusinessException(
            "message.noshopId",
            "\\u5E97\\u94FAId\\u4E0D\\u5B58\\u5728",
            "zh_CN"
          )
      );
  }
}
