package com.angsi.shop.service.impl;
import com.angsi.shop.VM.*;
import com.angsi.shop.common.AuditRecord;
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
import com.angsi.shop.dto.shop.*;
import com.angsi.shop.dto.subshop.NewSubShopDTO;
import com.angsi.shop.dto.subshop.SubShopQO;
import com.angsi.shop.dto.subshop.UpdateSubShopDTO;
import com.angsi.shop.enums.CategoryType;
import com.angsi.shop.enums.ShopInventorytype;
import com.angsi.shop.enums.Shoptype;
import com.angsi.shop.enums.StatusConstants;
import com.angsi.shop.repository.okNowTimeUtil;
import com.angsi.shop.repository.shop.ShopRepository;
import com.angsi.shop.repository.util.GeoUtil;
import com.angsi.shop.repository.util.TimeUtil;
import com.angsi.shop.service.BusinessConfigurationService;
import com.angsi.shop.service.ShopService;
import com.angsi.shop.service.StoreTypeService;
import com.angsi.shop.service.mapper.ShopMapper;
import com.basic.core.domain.PageDTO;
import com.basic.core.exception.BusinessException;
import com.basic.starter.dubbo.annotation.Consumer;
import com.basic.starter.dubbo.annotation.Provider;

import java.text.ParseException;
import java.time.Instant;
import java.util.*;
import com.qianbao.weixin.client.miniapp.WxMiniappClient;
import com.qianbao.weixin.client.miniapp.model.qrcode.QRCodeReq;
import com.qianbao.weixin.client.mp.WxMpClient;
import com.qianbao.weixin.client.mp.model.base.token.TokenRes;
import com.vmcshop.mall.common.model.CommResp;
import com.vmcshop.mall.common.model.ResultBean;
import com.vmcshop.mall.common.model.address.AddressList;
import com.vmcshop.mall.common.model.authority.UserModel;
import com.vmcshop.mall.common.model.category.OperationCateTree;
import com.vmcshop.mall.common.util.Result;
import com.vmcshop.mall.common.vm.PubNavEntryVM;
import com.vmcshop.mall.dubbo.api.address.AddressInfoService;
import com.vmcshop.mall.dubbo.api.authority.NewUserService;
import com.vmcshop.mall.dubbo.api.category.CategoryService;
import com.vmcshop.mall.dubbo.api.category.OperationCategoryService;
import com.vmcshop.mall.dubbo.api.fileupload.ImageHandleService;
import com.vmcshop.mall.dubbo.api.order.AngsiOrderServiceMs;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Service Implementation
 */
@Provider
@Slf4j
public class ShopServiceImpl implements ShopService {
      @Autowired
      private ShopRepository shopRepository;

      @Resource
      StoreTypeService storeTypeService;

      @Autowired
      private ShopMapper shopMapper;

      @Consumer
      private NewUserService newUserService;

      @Consumer
      private AddressInfoService addressInfoService;

      @Consumer
      private AngsiOrderServiceMs angsiOrderServiceMs;

      @Consumer
      private OperationCategoryService operationCategoryService;

    @Consumer
    BusinessConfigurationService businessConfigurationService;

    @Consumer
    private CategoryService categoryService;

    @Autowired
    HttpServletRequest request;

    @Autowired
    private WxMpClient wxMpClient;

    @Autowired
    private WxMiniappClient wxMiniappClient;

    @Consumer(check = false, timeout = 10000)
    private ImageHandleService imageHandleService;

    private final static String path="/pages/menu/index?shopId=";

    private final static String seatIdContant="&seatId";

    public Map<String,Map<String, DiningTable>> generateQrcode(String name,String shopId,Map<String, DiningTable> diningStoreTables){
        TokenRes tokenRes = wxMpClient.fetchToken();
        for (String mkey : diningStoreTables.keySet()){
            String qRcode = "";
            try {
                qRcode = diningStoreTables.get(mkey).getQRcode();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(!"".equals(qRcode)&&qRcode!=null){
                continue;
            }
                QRCodeReq qrCodeReq = new QRCodeReq();
                qrCodeReq.setWidth(430);
                qrCodeReq.setPath(path+shopId+seatIdContant+mkey);
                byte[] bytes= wxMiniappClient.getwxacode(tokenRes.getAccessToken(),qrCodeReq);
                String   barCodeUrl = null;
                //File file = new File("/tmp/pic/+"+shopId+name+".jpg");

                    //ZxingUtil.createFile(bytes, file);
                    try {
                        barCodeUrl= imageHandleService.uploadFileUrlTransform(bytes,shopId+".jpg");
                        diningStoreTables.get(mkey).setQRcode(barCodeUrl);
                        diningStoreTables.get(mkey).setPic(bytes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            }
        Map<String,Map<String, DiningTable>>  result= new HashMap<String,Map<String, DiningTable>>();
        result.put(shopId,diningStoreTables);
        return result;
    }

//    public DiningTable generateSingleQrcode(String name,String shopId,DiningTable diningTable){
//        TokenRes tokenRes = wxMpClient.fetchToken();
//        QRCodeReq qrCodeReq = new QRCodeReq();
//        qrCodeReq.setWidth(430);
//        qrCodeReq.setPath(path+shopId+seatIdContant+diningTable.getTableNo());
//        byte[] bytes= wxMiniappClient.getwxacode(tokenRes.getAccessToken(),qrCodeReq);
//        String   barCodeUrl = null;
//        File file = new File("/tmp/pic/+"+shopId+name+".jpg");
//        try {
//            ZxingUtil.createFile(bytes, file);
//            try {
//                barCodeUrl= imageHandleService.uploadFileUrlTransform(bytes,shopId+".jpg");
//                diningTable.setQRcode(barCodeUrl);
//                diningTable.setPic(bytes);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return diningTable;
//    }

    public String getProvinceList(Integer id){
        String province="";
        CommResp<List<AddressList>> listCommResp = addressInfoService.GetProvinceList();
        List<AddressList> data = listCommResp.getData();
        for (AddressList a:data) {
            if((int)a.getAddressid()==(int)id){
                province = a.getAddressname();
            }
        }
        return province;
    }
    public String getCityList(Integer id,Integer provinceId){
        String city = "";
        CommResp<List<AddressList>> listCommResp1 = addressInfoService.GetCityList(provinceId);
        List<AddressList> data1 = listCommResp1.getData();
        for (AddressList a:data1) {
            if((int)a.getAddressid()==(int)id){
                city = a.getAddressname();
            }
        }
        return city;
    }
    public String getDistrictList(Integer id,Integer cityId){
        String district = "";
        CommResp<List<AddressList>> listCommResp2 = addressInfoService.GetDistrictList(cityId);
        List<AddressList> data2 = listCommResp2.getData();
        for (AddressList a:data2) {
            if((int)a.getAddressid()==(int)id){
                district = a.getAddressname();
            }
        }
        return district;
    }

    @Override
    public List<String> getOrderListByshopId(String shopId) {
        log.debug("REST request to save Shop : {}",shopId);
        return angsiOrderServiceMs.getOrderListByShopId(shopId);
    }

    @Override
    public List<SubShopVM> getSubshopByOkAffirm() {
        SubShopQO subShopQO = new SubShopQO();
        subShopQO.setShopType(String.valueOf(Shoptype.SUBSHOP));
        subShopQO.setOkAffirm(1);
        List<SubShopVM> subShopsByShopId = getSubShopsByShopId(subShopQO);
        return subShopsByShopId;
    }

    @Override
    public List<Shop> getShopByIds(List<String> shopIds) {
        ArrayList<Shop> objects = new ArrayList<>();
        for (String shopId:
                shopIds) {
            Shop shopById = findShopById(shopId);
            objects.add(shopById);
        }
        return objects;
    }

    @Override
    public PageDTO<FactoryVM> getFirstFactories(
        int current,
        int size,
        FactoryQO query
    ) {
        Page<FactoryVM> page = shopRepository
                .find(query.toQuery(), PageRequest.of(current-1, size))
                .map(shopMapper::toFactoryVm);
        return new PageDTO<>(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getContent()
        );
    }

    @Override
    public PageDTO<Shop> getShopAndShop(int current,
                                           int size,ShopQO query) {
        Page<Shop> page = shopRepository.find(query.toQuery(), PageRequest.of(current - 1, size));
        return new PageDTO<>(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getContent()
        );
    }

    @Override
  public BigShopVM updateBigShopCategoryType(String shopId,CategoryType categoryType) {
    return shopRepository
    .findById(shopId)
    .map(
        shop -> {
          shop.setCategoryType(categoryType);
          return shopRepository.save(shop);
        }
    )
    .map(shopMapper::toBigShopVm)
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
    public PageDTO<SubShopVM> getSubShopByShopId(
            int current,
            int size,
            SubShopQO query) {
        log.info(String.valueOf(query.toQuery()));
        Page<SubShopVM> page = shopRepository
        .find(query.toQuery(), PageRequest.of(current-1, size))
        .map(
            shop -> {
                //全局二维码
                if(shop.getQrCode()==null||"".equals(shop.getQrCode())){
                    List<BusinessConfigurationVM> all = businessConfigurationService.findAll(null);
                    BusinessConfigurationVM businessConfigurationVM = null;
                    if(all!=null&&all.size()>0){
                        businessConfigurationVM =all.get(0);
                        shop.setQrCode(businessConfigurationVM.getQrCode());
                        shop.setQrCodeText(businessConfigurationVM.getQrCodeDesc());
                    }
                }
                //门店营业状态
                shop.getOpenTime().setStatus(0);//歇业
                if(shop.getOpenTime().getOpenTimeType()!=null){
                    if(shop.getOpenTime().getOpenTimeType()==0){
                        try {
                            boolean timeRange = TimeUtil.isTimeRange(shop.getOpenTime().getTimeStart(), shop.getOpenTime().getTimeEnd());
                            if(timeRange){
                                shop.getOpenTime().setStatus(1);//营业中
                            }else{
                                shop.getOpenTime().setStatus(0);//歇业
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    if(shop.getOpenTime().getOpenTimeType()==1&&shop.getOpenTime().getManageTimes()!=null){
                        List<TimeQuantum> manageTimes = shop.getOpenTime().getManageTimes();
                        for (TimeQuantum time :manageTimes) {
                            try {
                                Date openingTime = TimeUtil.StringToDate(time.getOpeningTime());
                                Date closingTime = TimeUtil.StringToDate(time.getClosingTime());
                                boolean effectiveDate = TimeUtil.isEffectiveDate(openingTime, closingTime);
                                if(effectiveDate){
                                    shop.getOpenTime().setStatus(1);//营业中
                                }else{
                                    shop.getOpenTime().setStatus(0);//歇业
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                return shop;
            }
        )
        .map(shopMapper::toSubShopVm);
        return new PageDTO<>(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getContent()
        );
    }
    @Override
    public List<SubShopVM> getSubShopsByShopId(
            SubShopQO query) {
        List<Shop> shops = shopRepository.find(query.toQuery());
        for (Shop shop : shops) {
            //全局二维码
            if(shop.getQrCode()==null||"".equals(shop.getQrCode())){
                List<BusinessConfigurationVM> all = businessConfigurationService.findAll(null);
                BusinessConfigurationVM businessConfigurationVM = null;
                if(all!=null&&all.size()>0){
                    businessConfigurationVM =all.get(0);
                    shop.setQrCode(businessConfigurationVM.getQrCode());
                    shop.setQrCodeText(businessConfigurationVM.getQrCodeDesc());
                }
            }
            //门店营业状态
            shop.getOpenTime().setStatus(0);//歇业
            if(shop.getOpenTime().getOpenTimeType()!=null){
                if(shop.getOpenTime().getOpenTimeType()==0){
                    try {
                        boolean timeRange = TimeUtil.isTimeRange(shop.getOpenTime().getTimeStart(), shop.getOpenTime().getTimeEnd());
                        if(timeRange){
                            shop.getOpenTime().setStatus(1);//营业中
                        }else{
                            shop.getOpenTime().setStatus(0);//歇业
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if(shop.getOpenTime().getOpenTimeType()==1&&shop.getOpenTime().getManageTimes()!=null){
                    List<TimeQuantum> manageTimes = shop.getOpenTime().getManageTimes();
                    for (TimeQuantum time :manageTimes) {
                        try {
                            Date openingTime = TimeUtil.StringToDate(time.getOpeningTime());
                            Date closingTime = TimeUtil.StringToDate(time.getClosingTime());
                            boolean effectiveDate = TimeUtil.isEffectiveDate(openingTime, closingTime);
                            if(effectiveDate){
                                shop.getOpenTime().setStatus(1);//营业中
                            }else{
                                shop.getOpenTime().setStatus(0);//歇业
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return shopMapper.toSubShopVm(shops);
    }

    @Override
    public List<Shop> getShopLists(BigShopQO query) {
        List<Shop> shops = shopRepository.find(query.toQuery());
        return shops;
    }

    @Override
    public List<BigShopVM> getBigShopLists(BigShopQO query) {
        List<Shop> shops = shopRepository.find(query.toQuery());
        return shopMapper.toBigShopVm(shops);
    }

    @Override
  public BigShopVM createBigShop(NewBigShopDTO newBigShopDTO) {
    log.debug("Request to save newBigShopDTO : {}", newBigShopDTO);
    Shop shop = shopMapper.toBigShopEntity(newBigShopDTO);
    String operator = newBigShopDTO.getOperator();
    UserModel byUserId = newUserService.getByUserId(Long.valueOf(operator));
    shop.setOperatorMsg(byUserId);

    ManageAddress manageAddress = newBigShopDTO.getManageAddress();
    String province = manageAddress.getProvince();
    String city = manageAddress.getCity();
    String district = manageAddress.getDistrict();
    String detailAddress = manageAddress.getDetailAddress();
    String provinceList = null;
    String cityList = null;
    String districtList = null;
    try {
        provinceList = getProvinceList(Integer.parseInt(province));
        cityList = getCityList(Integer.parseInt(city), Integer.parseInt(province));
        districtList = getDistrictList(Integer.parseInt(district), Integer.parseInt(city));
    } catch (NumberFormatException e) {
        e.printStackTrace();
    }
    StringBuffer buffer = new StringBuffer();
    buffer.append(provinceList);
    buffer.append(cityList);
    buffer.append(districtList);
    buffer.append(detailAddress);
    shop.setOkManageAddress(buffer.toString());
    //根据大店经营地址查询经纬度并保存
    String coordinate = GeoUtil.getCoordinate(buffer.toString());
    NameLocation nameLocation = new NameLocation();
    nameLocation.setLongitude("0.00");//经度
    nameLocation.setLatitude("0.00");//维度
    if(StringUtil.isNotBlank(coordinate)){
        String[] split = coordinate.split(",");
        nameLocation.setLongitude(split[0]);//经度
        nameLocation.setLatitude(split[1]);//维度
    }
    manageAddress.setNameLocation(nameLocation);
    shop.setManageAddress(manageAddress);

    Integer openTimeType = newBigShopDTO.getOpenTime().getOpenTimeType();
    if(openTimeType==0){
        String timeStart = newBigShopDTO.getOpenTime().getTimeStart();
        StringBuffer hour = new StringBuffer();
        if(timeStart!=null&&!"".equals(timeStart)){
            hour.append(timeStart.substring(0, 2));
            hour.append(timeStart.substring(3, 5));
        }
        String timeEnd = newBigShopDTO.getOpenTime().getTimeEnd();
        StringBuffer hour1 = new StringBuffer();
        if(timeEnd!=null&&!"".equals(timeEnd)){
            hour1.append(timeEnd.substring(0, 2));
            hour1.append(timeEnd.substring(3, 5));
        }
        shop.getOpenTime().setManageTime(hour.toString()+","+hour1.toString());
    }
    if(openTimeType==1){
        List<TimeQuantum> manageTimes = newBigShopDTO.getOpenTime().getManageTimes();
        for (TimeQuantum time:manageTimes) {
            String openingTime = time.getOpeningTime();
            String closingTime = time.getClosingTime();
            boolean effectiveDate = okNowTimeUtil.isEffectiveDate(openingTime, closingTime);
            if(effectiveDate){
                StringBuffer hour = new StringBuffer();
                if(time.getOpeningTime()!=null&&!"".equals(time.getOpeningTime())){
                    hour.append(time.getOpeningTime().substring(11,13));
                    hour.append(time.getOpeningTime().substring(14, 16));
                }
                StringBuffer hour1 = new StringBuffer();
                if(time.getClosingTime()!=null&&!"".equals(time.getClosingTime())){
                    hour1.append(time.getClosingTime().substring(11,13));
                    hour1.append(time.getClosingTime().substring(14, 16));
                }
                shop.getOpenTime().setManageTime(hour.toString()+","+hour1.toString());
            }
        }
    }
    shop.setShoptype(Shoptype.BIGSHOP);
    shop.setCategoryType(CategoryType.GLOBAL);
    shop.setAuditStatus(0);
    shop.setOpeningStatus(0);
    shop.setDelete(0);
    shop.setPushId(0L);
    shop.setPushFactoryId(0L);
    shop.setCreatedTime(new Date());
    shop = shopRepository.save(shop);
    BigShopVM result = shopMapper.toBigShopVm(shop);
    return result;
  }

  @Override
  public SubShopVM createSubShop(NewSubShopDTO dto) {
    log.debug("Request to save newBigShopDTO : {}", dto);
    //获取大店信息
    BigShopVM bigShop = findBigShop(dto.getParentId());
    Shop shop = shopMapper.toSubShopEntity(dto);

    //通过配送站id查询factoryId
    if(StringUtil.isNotBlank(dto.getDeliveryStation())) {
        String[] split = dto.getDeliveryStation().split(",");
        if(split!=null&&split.length>1){
            shop.setDeliveryStation(split[0]);

            shop.setFactoryId(split[1]);
        }else {
            shop.setDeliveryStation(dto.getDeliveryStation());
        }
    }
    shop.setOperatorMsg(bigShop.getOperatorMsg());
    shop.setOperator(bigShop.getOperator());
    //拉取大店营业时间
    shop.setOpenTime(bigShop.getOpenTime());
    //拉取大店营业地址
    shop.setManageAddress(bigShop.getManageAddress());
    shop.setOkManageAddress(bigShop.getOkManageAddress());
    shop.setQrCode("");
    shop.setQrCodeText("");
    shop.setShoptype(Shoptype.SUBSHOP);
    shop.setDelete(0);
    shop.setOpeningStatus(0);
    shop.setCategoryType(CategoryType.GLOBAL);
    shop.setAuditStatus(0);
    shop.setPushId(0L);
    shop.setPushFactoryId(0L);
    shop.setManageAddress(bigShop.getManageAddress());
    shop.setCreatedTime(new Date());
    shop = shopRepository.save(shop);
    SubShopVM result = shopMapper.toSubShopVm(shop);
    return result;
  }

  @Override
  public ShopVM createShop(NewShopDTO shopDTO) {
      log.debug("Request to save Shop : {}", shopDTO);
      Shop shop = shopMapper.toEntity(shopDTO);
      String operator = shopDTO.getOperator();//经营人
      UserModel byUserId = newUserService.getByUserId(Long.valueOf(operator));
      shop.setOperatorMsg(byUserId);

      if(StringUtil.isNotBlank(shopDTO.getDeliveryStation())) {
          String[] split = shopDTO.getDeliveryStation().split(",");
          if(split!=null&&split.length>0){
              shop.setDeliveryStation(split[0]);
              shop.setFactoryId(split[1]);
              shop.setCentre(split[2]+":"+split[3]);
          }else {
              shop.setDeliveryStation(shopDTO.getDeliveryStation());
          }
      }

      ManageAddress manageAddress = shopDTO.getManageAddress();
      String province = manageAddress.getProvince();
      String city = manageAddress.getCity();
      String district = manageAddress.getDistrict();
      String detailAddress = manageAddress.getDetailAddress();
      String provinceList = null;
      String cityList = null;
      String districtList = null;
      try {
          provinceList = getProvinceList(Integer.parseInt(province));
          cityList = getCityList(Integer.parseInt(city), Integer.parseInt(province));
          districtList = getDistrictList(Integer.parseInt(district), Integer.parseInt(city));
      } catch (NumberFormatException e) {
          e.printStackTrace();
      }
      StringBuffer buffer = new StringBuffer();
      buffer.append(provinceList);
      buffer.append(cityList);
      buffer.append(districtList);
      buffer.append(detailAddress);
      shop.setOkManageAddress(buffer.toString());
      //根据店铺经营地址查询经纬度并保存
      String coordinate = GeoUtil.getCoordinate(buffer.toString());
      NameLocation nameLocation = new NameLocation();
      nameLocation.setLongitude("0.00");//经度
      nameLocation.setLatitude("0.00");//维度
      if(StringUtil.isNotBlank(coordinate)){
          String[] split = coordinate.split(",");
          nameLocation.setLongitude(split[0]);//经度
          nameLocation.setLatitude(split[1]);//维度
      }
      manageAddress.setNameLocation(nameLocation);
      shop.setManageAddress(manageAddress);

      Integer openTimeType = shopDTO.getOpenTime().getOpenTimeType();
      if(openTimeType==0){
          String timeStart = shopDTO.getOpenTime().getTimeStart();
          StringBuffer hour = new StringBuffer();
          if(timeStart!=null&&!"".equals(timeStart)){
              hour.append(timeStart.substring(0, 2));
              hour.append(timeStart.substring(3, 5));
          }
          String timeEnd = shopDTO.getOpenTime().getTimeEnd();
          StringBuffer hour1 = new StringBuffer();
          if(timeEnd!=null&&!"".equals(timeEnd)){
              hour1.append(timeEnd.substring(0, 2));
              hour1.append(timeEnd.substring(3, 5));
          }
          shop.getOpenTime().setManageTime(hour.toString()+","+hour1.toString());
      }
      if(openTimeType==1){
          List<TimeQuantum> manageTimes = shopDTO.getOpenTime().getManageTimes();
          for (TimeQuantum time:manageTimes) {
              String openingTime = time.getOpeningTime();
              String closingTime = time.getClosingTime();
              boolean effectiveDate = okNowTimeUtil.isEffectiveDate(openingTime, closingTime);
              if(effectiveDate){
                  StringBuffer hour = new StringBuffer();
                  if(time.getOpeningTime()!=null&&!"".equals(time.getOpeningTime())){
                      hour.append(time.getOpeningTime().substring(11,13));
                      hour.append(time.getOpeningTime().substring(14, 16));
                  }
                  StringBuffer hour1 = new StringBuffer();
                  if(time.getClosingTime()!=null&&!"".equals(time.getClosingTime())){
                      hour1.append(time.getClosingTime().substring(11,13));
                      hour1.append(time.getClosingTime().substring(14, 16));
                  }
                  shop.getOpenTime().setManageTime(hour.toString()+","+hour1.toString());
              }
          }
      }
    shop.setShoptype(Shoptype.STORE);
    shop.setCategoryType(CategoryType.GLOBAL);
    shop.setDelete(0);
//    shop.setOpeningStatus(0);
    shop.setAuditStatus(0);
    shop.setPushId(0L);
    shop.setPushFactoryId(0L);
    shop.setCreatedTime(new Date());
    shop = shopRepository.save(shop);
    ShopVM result = shopMapper.toShopVm(shop);
    return result;
  }

  @Override
  public FactoryVM createFactory(NewFactoryDTO shopDTO) {
    log.debug("Request to save Shop : {}", shopDTO);
    Shop shop = shopMapper.toFactoryEntity(shopDTO);
    if(StringUtil.isNotBlank(shopDTO.getParentId())){
        Shop shopById = findShopById(shopDTO.getParentId());
        shop.setParentName(shopById.getName());
    }
      String operator = shopDTO.getOperator();//经营人
      UserModel byUserId = newUserService.getByUserId(Long.valueOf(operator));
      shop.setOperatorMsg(byUserId);

      if(StringUtil.isNotBlank(shopDTO.getDeliveryStation())) {
          String[] split = shopDTO.getDeliveryStation().split(",");
          if(split!=null&&split.length>0){
              shop.setDeliveryStation(split[0]);
              shop.setFactoryId(split[1]);
              shop.setCentre(split[2]+":"+split[3]);
          }else {
              shop.setDeliveryStation(shopDTO.getDeliveryStation());
          }
      }

      ManageAddress manageAddress = shopDTO.getManageAddress();
      String province = manageAddress.getProvince();
      String city = manageAddress.getCity();
      String district = manageAddress.getDistrict();
      String detailAddress = manageAddress.getDetailAddress();
      String provinceList = null;
      String cityList = null;
      String districtList = null;
      try {
          provinceList = getProvinceList(Integer.parseInt(province));
          cityList = getCityList(Integer.parseInt(city), Integer.parseInt(province));
          districtList = getDistrictList(Integer.parseInt(district), Integer.parseInt(city));
      } catch (NumberFormatException e) {
          e.printStackTrace();
      }
      StringBuffer buffer = new StringBuffer();
      buffer.append(provinceList);
      buffer.append(cityList);
      buffer.append(districtList);
      buffer.append(detailAddress);
      shop.setOkManageAddress(buffer.toString());
      //根据店铺经营地址查询经纬度并保存
      String coordinate = GeoUtil.getCoordinate(buffer.toString());
      NameLocation nameLocation = new NameLocation();
      nameLocation.setLongitude("0.00");//经度
      nameLocation.setLatitude("0.00");//维度
      if(StringUtil.isNotBlank(coordinate)){
          String[] split = coordinate.split(",");
          nameLocation.setLongitude(split[0]);//经度
          nameLocation.setLatitude(split[1]);//维度
      }
      manageAddress.setNameLocation(nameLocation);
      shop.setManageAddress(manageAddress);

      Integer openTimeType = shopDTO.getOpenTime().getOpenTimeType();
      if(openTimeType==0){
          String timeStart = shopDTO.getOpenTime().getTimeStart();
          StringBuffer hour = new StringBuffer();
          if(timeStart!=null&&!"".equals(timeStart)){
              hour.append(timeStart.substring(0, 2));
              hour.append(timeStart.substring(3, 5));
          }
          String timeEnd = shopDTO.getOpenTime().getTimeEnd();
          StringBuffer hour1 = new StringBuffer();
          if(timeEnd!=null&&!"".equals(timeEnd)){
              hour1.append(timeEnd.substring(0, 2));
              hour1.append(timeEnd.substring(3, 5));
          }
          shop.getOpenTime().setManageTime(hour.toString()+","+hour1.toString());
      }
      if(openTimeType==1){
          List<TimeQuantum> manageTimes = shopDTO.getOpenTime().getManageTimes();
          for (TimeQuantum time:manageTimes) {
              String openingTime = time.getOpeningTime();
              String closingTime = time.getClosingTime();
              boolean effectiveDate = okNowTimeUtil.isEffectiveDate(openingTime, closingTime);
              if(effectiveDate){
                  StringBuffer hour = new StringBuffer();
                  if(time.getOpeningTime()!=null&&!"".equals(time.getOpeningTime())){
                      hour.append(time.getOpeningTime().substring(11,13));
                      hour.append(time.getOpeningTime().substring(14, 16));
                  }
                  StringBuffer hour1 = new StringBuffer();
                  if(time.getClosingTime()!=null&&!"".equals(time.getClosingTime())){
                      hour1.append(time.getClosingTime().substring(11,13));
                      hour1.append(time.getClosingTime().substring(14, 16));
                  }
                  shop.getOpenTime().setManageTime(hour.toString()+","+hour1.toString());
              }
          }
      }
    shop.setCategoryType(CategoryType.GLOBAL);
    shop.setDelete(0);
    shop.setShoptype(Shoptype.CENTERFACTORY.toString().equals(shopDTO.getShoptype())?Shoptype.CENTERFACTORY:Shoptype.FIRSTFACTORY);
//    shop.setOpeningStatus(0);
    shop.setAuditStatus(0);
    shop.setPushId(0L);
    shop.setPushFactoryId(0L);
    shop.setCreatedTime(new Date());
    shop = shopRepository.save(shop);
    FactoryVM result = shopMapper.toFactoryVm(shop);
    return result;
  }

  @Override
  public SeatingAreaVM createSeatingArea(NewSeatingAreaDTO dto) {
    log.debug("Request to save  SeatingArea: {}", dto);

      if(dto.getSeatCount()>15){
           throw new BusinessException(
                  "message.seatCount",
                  "\\u684c\\u4f4d\\u6570\\u91cf\\u4e00\\u6b21\\u6700\\u591a\\u521b\\u5efa\\u0031\\u0035\\u4e2a"
          );
      }

    String startEnglishDeskNo = dto.getStartEnglishDeskNo();
    String startDeskNo = dto.getStartDeskNo();
    Integer seatCount = dto.getSeatCount();

    List<String> diningTables = new ArrayList<String>();
    Map<String, DiningTable> diningStoreTables = new HashMap<String, DiningTable>();
    for (int i = 0; i < seatCount; i++) {
      DiningTable diningTable = new DiningTable();
      Integer temp = Integer.parseInt(startDeskNo) + i;
      diningTable.setDelete(0);
      diningTable.setTableNo(startEnglishDeskNo + temp);

        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int i2 = instance.get(Calendar.HOUR_OF_DAY);//时（24小时制）
        int i3 = instance.get(Calendar.MINUTE);//分
        int i4 = instance.get(Calendar.SECOND);//秒

        diningTable.setCreatedTime(i2+":"+i3+":"+i4);
        diningTable.setModifiedTime(i2+":"+i3+":"+i4);

      diningTable.setQRcode("");
      diningTable.setTableLink("");
      diningTable.setOpenState(1);//桌台默认为开状态
      diningStoreTables.put(diningTable.getTableNo(), diningTable);
      diningTables.add(diningTable.getTableNo());
    }
    Shop shop = shopMapper.toSeatingAreaEntity(dto);
      //获取大店信息
      BigShopVM bigShop = findBigShop(dto.getParentId());
      //营业时间
      if(dto.getOpenTime()==null)shop.setOpenTime(bigShop.getOpenTime());
      shop.setShoptype(Shoptype.SEATINGAREA);
      shop.setDelete(0);
      shop.setOpeningStatus(1);
      shop.setAuditStatus(0);
      shop.setCategoryType(CategoryType.GLOBAL);
      shop.setCreatedTime(new Date());
      shop.setDiningViewTables(diningTables);
      shop.setDiningStoreTables(diningStoreTables);
      Shop result=shopRepository.save(shop);
      return shopRepository.findById(result.getId())
        .map(
          shop1 -> {
              Map<String,Map<String, DiningTable>>  temp = generateQrcode(shop1.getName(),shop1.getId(),shop1.getDiningStoreTables());
              shop1.setDiningStoreTables(temp.get(shop1.getId()));
              shopRepository.save(shop1);
             return shop1;
          }
        ).map(shopMapper::toSeatingAreaVm)
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
  public SeatingAreaVM updateSeatingArea(
    UpdateSeatingAreaDTO dto
  ) {
    return shopRepository
      .findById(dto.getId())
      .map(
        shop -> {
          if(dto.getName()!=null&&!"".equals(dto.getName()))
          shop.setName(dto.getName());
          //删除状态
          if(dto.getDelete()!=null)
          shop.setDelete(dto.getDelete());
          //楼层
          if(dto.getFloor()!=null)
          shop.setFloor(dto.getFloor());
          //money
          if(dto.getOrderConditions()!=null)
          shop.setOrderConditions(dto.getOrderConditions());
          //开关状态
          if(dto.getOpeningStatus()!=null)
          shop.setOpeningStatus(dto.getOpeningStatus());
          //营业时间
          if(dto.getOpenTime()!=null){
          shop.setOpenTime(dto.getOpenTime());
          }
          //若是审核状态不为0,修改大店审核状态
          if(dto.getAuditStatus()!=null&&!"".equals(dto.getAuditStatus())){
            //设置审核状态，在添加一条审核记录
            shop.setAuditStatus(dto.getAuditStatus());
//              List<AuditRecord> auditOpinionLogs = null;
//              try {
//                  auditOpinionLogs = shop.getAuditOpinionLogs();
//              } catch (Exception e) {
//                  e.printStackTrace();
//              }
//              AuditRecord auditRecord = new AuditRecord();
//            auditRecord.setAuditStatusRecord(dto.getAuditStatus());
//            //若是审核意见不为Null,修改大店审核意见
//            if(dto.getAuditOpinion()!=null&&!"".equals(dto.getAuditOpinion()))auditRecord.setAuditOpinionRecord(dto.getAuditOpinion());
//            auditRecord.setAuditTime(new Date());
//            CurrentBakUser currentBakUser = BakUserUtils.getCurrentBakUser(request);
//            UserModel byUserId = newUserService.getByUserId(currentBakUser.getUserId());
//            auditRecord.setAuditor(byUserId.getNickname());
//            auditOpinionLogs.add(auditRecord);
//            shop.setAuditOpinionLogs(auditOpinionLogs);
          }
          return shopRepository.save(shop);
        }
      )
      .map(shopMapper::toSeatingAreaVm)
      .orElseThrow(
        () ->
          new BusinessException(
            "message.noshopId",
            "\\u5E97\\u94FAId\\u4E0D\\u5B58\\u5728",
            "zh_CN"
          )
      );
  }
    //修改大店开关
    public BigShopVM updateBigShopOpenState(UpdateBigShopDTO dto){
      return shopRepository
        .findById(dto.getId())
        .map(
            shop -> {
                if(dto.getOpeningStatus()!=null)
                    shop.setOpeningStatus(dto.getOpeningStatus());
                return shopRepository.save(shop);
            }
        )
        .map(shopMapper::toBigShopVm)
        .orElseThrow(
        () ->
        new BusinessException(
                "message.noshopId",
                "\\u5E97\\u94FAId\\u4E0D\\u5B58\\u5728",
                "zh_CN"
        )
        );
    }
    //修改子门店开关
    public SubShopVM updateSubShopOpenState(UpdateSubShopDTO dto){
      return shopRepository
        .findById(dto.getId())
        .map(
        shop -> {
            if(dto.getOpeningStatus()!=null)
                shop.setOpeningStatus(dto.getOpeningStatus());
            return shopRepository.save(shop);
        }
        )
        .map(shopMapper::toSubShopVm)
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
  public BigShopVM updateBigShop(UpdateBigShopDTO dto,String nickName) {
    return shopRepository
      .findById(dto.getId())
      .map(
        shop -> {
            if(dto.getPushId()!=null&&!"".equals(dto.getPushId())){//同步到振邦的id
                shop.setPushId(dto.getPushId());
            }
            if(dto.getPushFactoryId()!=null&&!"".equals(dto.getPushFactoryId())){//同步到振邦的factoryId
                shop.setPushFactoryId(dto.getPushFactoryId());
            }
            //门店名称
            if(dto.getName()!=null&&!"".equals(dto.getName()))
            shop.setName(dto.getName());
            //经营人
            if(dto.getOperator()!=null&&!"".equals(dto.getName())){
                String operator = dto.getOperator();
                shop.setOperator(operator);
                UserModel byUserId = newUserService.getByUserId(Long.valueOf(operator));
                shop.setOperatorMsg(byUserId);
            }
            //经营地址
            if(dto.getManageAddress()!=null){
                ManageAddress manageAddress = dto.getManageAddress();
                shop.setManageAddress(manageAddress);
                String province = manageAddress.getProvince();
                String city = manageAddress.getCity();
                String district = manageAddress.getDistrict();
                String detailAddress = manageAddress.getDetailAddress();
                String provinceList = null;
                String cityList = null;
                String districtList = null;
                try {
                    provinceList = getProvinceList(Integer.parseInt(province));
                    cityList = getCityList(Integer.parseInt(city), Integer.parseInt(province));
                    districtList = getDistrictList(Integer.parseInt(district), Integer.parseInt(city));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                StringBuffer buffer = new StringBuffer();
                buffer.append(provinceList);
                buffer.append(cityList);
                buffer.append(districtList);
                buffer.append(detailAddress);
                shop.setOkManageAddress(buffer.toString());
                //根据大店经营地址查询经纬度并保存
                String coordinate = GeoUtil.getCoordinate(buffer.toString());
                NameLocation nameLocation = new NameLocation();
                nameLocation.setLongitude("0.00");//经度
                nameLocation.setLatitude("0.00");//维度
                if(StringUtil.isNotBlank(coordinate)){
                    String[] split = coordinate.split(",");
                    nameLocation.setLongitude(split[0]);//经度
                    nameLocation.setLatitude(split[1]);//维度
                }
                manageAddress.setNameLocation(nameLocation);
                shop.setManageAddress(manageAddress);
            }
            //删除状态
            if(dto.getDelete()!=null)
            shop.setDelete(dto.getDelete());
            //配送方式修改
            if(dto.getDeliveryType()!=null&&!"".equals(dto.getDeliveryType()))
            shop.setDeliveryType(dto.getDeliveryType());
            //门店二维码
            if(StringUtil.isNotBlank(dto.getQrCode()))shop.setQrCode(dto.getQrCode());
            //经营范围
            if(dto.getManageScope()!=null)shop.setManageScope(dto.getManageScope());
            //是否自动确认制作
            if(dto.getOkAffirm()!=null&&!"".equals(dto.getOkAffirm()))shop.setOkAffirm(dto.getOkAffirm());
            //是否支持预约当天
            if(dto.getOkReserve()!=null&&!"".equals(dto.getOkReserve()))shop.setOkReserve(dto.getOkReserve());
            //会籍顾问自动分单
            if(dto.getOkSubMeanu()!=null&&!"".equals(dto.getOkSubMeanu()))shop.setOkSubMeanu(dto.getOkSubMeanu());
            //证件信息
            if(dto.getPapersImage()!=null)shop.setPapersImage(dto.getPapersImage());
            //现场图
            if(dto.getSceneImage()!=null)shop.setSceneImage(dto.getSceneImage());
            //若是审核状态不为0,修改大店审核状态
            if(dto.getAuditStatus()!=null&&!"".equals(dto.getAuditStatus())){
                //设置审核状态，在添加一条审核记录
                shop.setAuditStatus(dto.getAuditStatus());
                List<AuditRecord> auditOpinionLogs = null;
                try {
                    if(shop.getAuditOpinionLogs()!=null){
                        auditOpinionLogs = shop.getAuditOpinionLogs();
                    }else{
                        auditOpinionLogs = new ArrayList<>();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                AuditRecord auditRecord = new AuditRecord();
                auditRecord.setAuditStatusRecord(dto.getAuditStatus());
                //若是审核意见不为Null,修改大店审核意见
                if(dto.getAuditOpinion()!=null&&!"".equals(dto.getAuditOpinion()))
                auditRecord.setAuditOpinionRecord(dto.getAuditOpinion());
                auditRecord.setAuditTime(new Date());
                auditRecord.setAuditor(nickName);
                try {
                    auditOpinionLogs.add(auditRecord);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                shop.setAuditOpinionLogs(auditOpinionLogs);
            }
            //营业时间
            if(dto.getOpenTime()!=null){
            shop.setOpenTime(dto.getOpenTime());
                Integer openTimeType = dto.getOpenTime().getOpenTimeType();
                if(openTimeType==0){
                    String timeStart = dto.getOpenTime().getTimeStart();
                    StringBuffer hour = new StringBuffer();
                    if(timeStart!=null&&!"".equals(timeStart)){
                        hour.append(timeStart.substring(0, 2));
                        hour.append(timeStart.substring(3, 5));
                    }
                    String timeEnd = dto.getOpenTime().getTimeEnd();
                    StringBuffer hour1 = new StringBuffer();
                    if(timeEnd!=null&&!"".equals(timeEnd)){
                        hour1.append(timeEnd.substring(0, 2));
                        hour1.append(timeEnd.substring(3, 5));
                    }
                    shop.getOpenTime().setManageTime(hour.toString()+","+hour1.toString());
                }
                if(openTimeType==1){
                    List<TimeQuantum> manageTimes = dto.getOpenTime().getManageTimes();
                    for (TimeQuantum time:manageTimes) {
                        String openingTime = time.getOpeningTime();
                        String closingTime = time.getClosingTime();
                        boolean effectiveDate = okNowTimeUtil.isEffectiveDate(openingTime, closingTime);
                        if(effectiveDate){
                            StringBuffer hour = new StringBuffer();
                            if(time.getOpeningTime()!=null&&!"".equals(time.getOpeningTime())){
                                hour.append(time.getOpeningTime().substring(11,13));
                                hour.append(time.getOpeningTime().substring(14, 16));
                            }
                            StringBuffer hour1 = new StringBuffer();
                            if(time.getClosingTime()!=null&&!"".equals(time.getClosingTime())){
                                hour1.append(time.getClosingTime().substring(11,13));
                                hour1.append(time.getClosingTime().substring(14, 16));
                            }
                            shop.getOpenTime().setManageTime(hour.toString()+","+hour1.toString());
                        }
                    }
                }
            }
            //若是开关状态不为null,修改大店开关状态
            if(dto.getOpeningStatus()!=null){
                shop.setOpeningStatus(dto.getOpeningStatus());
                if(dto.getOpeningStatus()==0){
                    //大店关，子门店全关
                    SubShopQO subShopQO = new SubShopQO();
                    subShopQO.setShopId(dto.getId());
                    List<Shop> shops = shopRepository.find(subShopQO.toQuery());
                    for (Shop s:
                            shops) {
                        UpdateSubShopDTO updateSubShopDTO = new UpdateSubShopDTO();
                        updateSubShopDTO.setId(s.getId());
                        updateSubShopDTO.setOpeningStatus(0);
                        updateSubShopOpenState(updateSubShopDTO);
                    }

                }
                if(dto.getOpeningStatus()==1){
                    //大店开，子门店全开
                    SubShopQO subShopQO = new SubShopQO();
                    subShopQO.setShopId(dto.getId());
                    List<Shop> shops = shopRepository.find(subShopQO.toQuery());
                    for (Shop s:
                            shops) {
                        UpdateSubShopDTO updateSubShopDTO = new UpdateSubShopDTO();
                        updateSubShopDTO.setId(s.getId());
                        updateSubShopDTO.setOpeningStatus(1);
                        updateSubShopOpenState(updateSubShopDTO);
                    }
                }
            }
          return shopRepository.save(shop);
        }
      )
      .map(shopMapper::toBigShopVm)
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
  public ShopVM updateShop(UpdateShopDTO dto) {
    return shopRepository
      .findById(dto.getId())
      .map(
        shop -> {
            if(dto.getName()!=null&&!"".equals(dto.getName()))
            shop.setName(dto.getName());
            if(dto.getStoreType()!=null&&!"".equals(dto.getStoreType()))
          shop.setStoreType(dto.getStoreType());
            //经营人
            if(dto.getOperator()!=null&&!"".equals(dto.getName())){
                String operator = dto.getOperator();
                shop.setOperator(operator);
                UserModel byUserId = newUserService.getByUserId(Long.valueOf(operator));
                shop.setOperatorMsg(byUserId);
            }
            //删除状态
            if(dto.getDelete()!=null){
                shop.setDelete(dto.getDelete());
                shop.setStoreType("");
                shop.setOpenTime(new OpenTime());
                shop.setDeliveryType("");
                shop.setPeopleNum(null);
                shop.setOpeningStatus(null);
                shop.setAuditStatus(null);
            }
            if(dto.getDelVoucher()!=null){//注销凭证
            shop.setDelVoucher(dto.getDelVoucher());}
            if(dto.getDelRemark()!=null){//注销备注
            shop.setDelRemark(dto.getDelRemark());}
            //配送方式修改
            if(dto.getDeliveryType()!=null&&!"".equals(dto.getDeliveryType()))
            shop.setDeliveryType(dto.getDeliveryType());
            //修改经营范围
            if(dto.getManageScope()!=null)shop.setManageScope(dto.getManageScope());
            //门店二维码
            if(StringUtil.isNotBlank(dto.getQrCode()))shop.setQrCode(dto.getQrCode());
            //是否自动确认制作
            if(dto.getOkAffirm()!=null)shop.setOkAffirm(dto.getOkAffirm());
            //是否支持预约当天
            if(dto.getOkReserve()!=null&&!"".equals(dto.getOkReserve()))shop.setOkReserve(dto.getOkReserve());
            //会籍顾问自动分单
            if(dto.getOkSubMeanu()!=null&&!"".equals(dto.getOkSubMeanu()))shop.setOkSubMeanu(dto.getOkSubMeanu());
            //证件信息
            if(dto.getPapersImage()!=null)shop.setPapersImage(dto.getPapersImage());
            //现场图
            if(dto.getSceneImage()!=null)shop.setSceneImage(dto.getSceneImage());
            //若是审核状态不为0,修改店铺审核状态
            if(dto.getAuditStatus()!=null&&!"".equals(dto.getAuditStatus())){
                //设置审核状态，在添加一条审核记录
                shop.setAuditStatus(dto.getAuditStatus());
                List<AuditRecord> auditOpinionLogs = null;
                try {
                    if(shop.getAuditOpinionLogs()!=null){
                        auditOpinionLogs = shop.getAuditOpinionLogs();
                    }else{
                        auditOpinionLogs = new ArrayList<>();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                AuditRecord auditRecord = new AuditRecord();
                auditRecord.setAuditStatusRecord(dto.getAuditStatus());
                //若是审核意见不为Null,修改大店审核意见
                if(dto.getAuditOpinion()!=null&&!"".equals(dto.getAuditOpinion()))
                    auditRecord.setAuditOpinionRecord(dto.getAuditOpinion());
                auditRecord.setAuditTime(new Date());
                auditRecord.setAuditor("匿名");
                try {
                    auditOpinionLogs.add(auditRecord);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                shop.setAuditOpinionLogs(auditOpinionLogs);
            }
            //若是审核意见不为Null,修改大店审核意见
            if(dto.getAuditOpinion()!=null&&!"".equals(dto.getAuditOpinion()))shop.setAuditOpinion(dto.getAuditOpinion());
          //营业时间
          if(dto.getOpenTime()!=null){
            shop.setOpenTime(dto.getOpenTime());
          }
          return shopRepository.save(shop);
        }
      )
      .map(shopMapper::toShopVm)
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
  public FactoryVM updateFactory(UpadteFactoryDTO dto) {
    return shopRepository
      .findById(dto.getId())
      .map(
        shop -> {
          if(dto.getName()!=null&&!"".equals(dto.getName()))
          shop.setName(dto.getName());
          //上级工厂
          if(dto.getParentId()!=null&&!"".equals(dto.getParentId()))
          {shop.setParentId(dto.getParentId());}
          //配送方式
          if(dto.getDeliveryType()!=null&&!"".equals(dto.getDeliveryType()))
          {shop.setDeliveryType(dto.getDeliveryType());}
          //经营人
          if(dto.getOperator()!=null&&!"".equals(dto.getOperator()))
          {shop.setOperator(dto.getOperator());}
          if(dto.getPmc()!=null&&!"".equals(dto.getPmc())){
          shop.setPmc(dto.getPmc());
          }
            //开关状态
            if(dto.getOpeningStatus()!=null&&!"".equals(dto.getOpeningStatus()))
            {shop.setOpeningStatus(dto.getOpeningStatus());}
            //若是审核状态不为0,修改店铺审核状态
            if(dto.getAuditStatus()!=null&&!"".equals(dto.getAuditStatus())){
                //设置审核状态，在添加一条审核记录
                shop.setAuditStatus(dto.getAuditStatus());
                List<AuditRecord> auditOpinionLogs = null;
                try {
                    if(shop.getAuditOpinionLogs()!=null){
                        auditOpinionLogs = shop.getAuditOpinionLogs();
                    }else{
                        auditOpinionLogs = new ArrayList<>();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                AuditRecord auditRecord = new AuditRecord();
                auditRecord.setAuditStatusRecord(dto.getAuditStatus());
                //若是审核意见不为Null,修改大店审核意见
                if(dto.getAuditOpinion()!=null&&!"".equals(dto.getAuditOpinion()))
                    auditRecord.setAuditOpinionRecord(dto.getAuditOpinion());
                auditRecord.setAuditTime(new Date());
                auditRecord.setAuditor("匿名");
                try {
                    auditOpinionLogs.add(auditRecord);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                shop.setAuditOpinionLogs(auditOpinionLogs);
            }
            //门店二维码
            if(StringUtil.isNotBlank(dto.getQrCode()))shop.setQrCode(dto.getQrCode());
            //是否自动确认制作
            if(dto.getOkAffirm()!=null)shop.setOkAffirm(dto.getOkAffirm());
            //是否支持预约当天
            if(dto.getOkReserve()!=null&&!"".equals(dto.getOkReserve()))shop.setOkReserve(dto.getOkReserve());
            //会籍顾问自动分单
            if(dto.getOkSubMeanu()!=null&&!"".equals(dto.getOkSubMeanu()))shop.setOkSubMeanu(dto.getOkSubMeanu());
            //证件信息
            if(dto.getPapersImage()!=null)shop.setPapersImage(dto.getPapersImage());
            //现场图
            if(dto.getSceneImage()!=null)shop.setSceneImage(dto.getSceneImage());
          //工厂地址
          if(dto.getManageAddress()!=null&&!"".equals(dto.getManageAddress()))
          shop.setManageAddress(dto.getManageAddress());
          //经营范围
          if(dto.getManageScope()!=null&&!"".equals(dto.getManageScope()))
          shop.setManageScope(dto.getManageScope());
        //删除状态
        if(dto.getDelete()!=null){
            shop.setDelete(dto.getDelete());
            shop.setStoreType("");
            shop.setOpenTime(new OpenTime());
            shop.setDeliveryType("");
            shop.setPeopleNum(null);
            shop.setOpeningStatus(null);
            shop.setAuditStatus(null);
        }
        if(dto.getDelVoucher()!=null){//注销凭证
        shop.setDelVoucher(dto.getDelVoucher());}
        if(dto.getDelRemark()!=null){//注销备注
        shop.setDelRemark(dto.getDelRemark());}
          //营业时间
          if(dto.getOpenTime()!=null){
          shop.setOpenTime(dto.getOpenTime());
          }
          return shopRepository.save(shop);
        }
      )
      .map(shopMapper::toFactoryVm)
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
    public Shop findShopById(String id) {
        return shopRepository
        .findById(id)
        .map(
           shop -> {
               if(shop.getDiningStoreTables()!=null){
                   Map<String, DiningTable> map = shop.getDiningStoreTables();
                   for (String key : map.keySet()) {
                       DiningTable diningTable = map.get(key);
                       byte[] pic = new byte[1];
                       pic[0]=1;
                       diningTable.setPic(pic);
                   }
               }
               //门店营业状态
               shop.getOpenTime().setStatus(0);//歇业
               if(shop.getOpenTime().getOpenTimeType()!=null){
                   if(shop.getOpenTime().getOpenTimeType()==0){
                       try {
                           boolean timeRange = TimeUtil.isTimeRange(shop.getOpenTime().getTimeStart(), shop.getOpenTime().getTimeEnd());
                           if(timeRange){
                               shop.getOpenTime().setStatus(1);//营业中
                           }else{
                               shop.getOpenTime().setStatus(0);//歇业
                           }
                       } catch (ParseException e) {
                           e.printStackTrace();
                       }
                   }
                   if(shop.getOpenTime().getOpenTimeType()==1&&shop.getOpenTime().getManageTimes()!=null){
                       List<TimeQuantum> manageTimes = shop.getOpenTime().getManageTimes();
                       for (TimeQuantum time :manageTimes) {
                           try {
                               Date openingTime = TimeUtil.StringToDate(time.getOpeningTime());
                               Date closingTime = TimeUtil.StringToDate(time.getClosingTime());
                               boolean effectiveDate = TimeUtil.isEffectiveDate(openingTime, closingTime);
                               if(effectiveDate){
                                   shop.getOpenTime().setStatus(1);//营业中
                               }else{
                                   shop.getOpenTime().setStatus(0);//歇业
                               }
                           } catch (ParseException e) {
                               e.printStackTrace();
                           }
                       }
                   }
               }
               return shop;
           }
        )
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
  public SeatingAreaVM findSeatingArea(String id) {
    return shopRepository
      .findById(id)
      .map(shopMapper::toSeatingAreaVm)
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
  public SubShopVM updateSubShop(UpdateSubShopDTO dto,String nickName) {
    return shopRepository
      .findById(dto.getId())
      .map(
        shop -> {
            if(dto.getPushId()!=null){//同步到振邦的返回id
                shop.setPushId(dto.getPushId());
            }
            if(dto.getPushFactoryId()!=null){//同步到振邦的返回factoryId
                shop.setPushFactoryId(dto.getPushFactoryId());
            }
            //门店名称
            if(dto.getName()!=null&&!"".equals(dto.getName()))
            shop.setName(dto.getName());
            //门店删除状态
            if(dto.getDelete()!=null)
            shop.setDelete(dto.getDelete());
            //配送方式修改
            if(dto.getDeliveryType()!=null&&!"".equals(dto.getDeliveryType()))
            shop.setDeliveryType(dto.getDeliveryType());
            //pcm账号信息
            if(dto.getPmc()!=null&&!"".equals(dto.getPmc()))
            shop.setPmc(dto.getPmc());
            //子门店类型 product sale
            if(dto.getOperationType()!=null&&!"".equals(dto.getOperationType()))
            shop.setOperationType(dto.getOperationType());
            //取货点
            if(dto.getPickingAddress()!=null&&!"".equals(dto.getPickingAddress()))
            shop.setPickingAddress(dto.getPickingAddress());
            //介绍
            if(dto.getDescription()!=null&&!"".equals(dto.getDescription()))
            shop.setDescription(dto.getDescription());
            //楼层
            if(dto.getFloor()!=null&&!"".equals(dto.getFloor()))
            shop.setFloor(dto.getFloor());
            //门店二维码
            if(dto.getQrCode()!=null)shop.setQrCode(dto.getQrCode());
            //门店二维码文字描述
            if(dto.getQrCodeText()!=null){shop.setQrCodeText(dto.getQrCodeText());}
            //是否自动确认制作
            if(dto.getOkAffirm()!=null)shop.setOkAffirm(dto.getOkAffirm());
            //是否支持预约当天
            if(dto.getOkReserve()!=null&&!"".equals(dto.getOkReserve()))shop.setOkReserve(dto.getOkReserve());
            //支持预约n天
            if(dto.getReserveDay()!=null&&!"".equals(dto.getReserveDay())){
                shop.setReserveDay(dto.getReserveDay());
            }
            //会籍顾问自动分单
            if(dto.getOkSubMeanu()!=null&&!"".equals(dto.getOkSubMeanu()))shop.setOkSubMeanu(dto.getOkSubMeanu());
            //证件信息
            if(dto.getPapersImage()!=null)shop.setPapersImage(dto.getPapersImage());
            //现场图
            if(dto.getSceneImage()!=null)shop.setSceneImage(dto.getSceneImage());
            //店招图
            if(StringUtil.isNotBlank(dto.getForFigure()))shop.setForFigure(dto.getForFigure());
            //若是审核状态不为0,修改子门店审核状态
            if(dto.getAuditStatus()!=null&&!"".equals(dto.getAuditStatus())){
                //设置审核状态，在添加一条审核记录
                shop.setAuditStatus(dto.getAuditStatus());
                List<AuditRecord> auditOpinionLogs = null;
                try {
                    auditOpinionLogs = shop.getAuditOpinionLogs();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                AuditRecord auditRecord = new AuditRecord();
                auditRecord.setAuditStatusRecord(dto.getAuditStatus());
                //若是审核意见不为Null,修改大店审核意见
                if(dto.getAuditOpinion()!=null&&!"".equals(dto.getAuditOpinion()))auditRecord.setAuditOpinionRecord(dto.getAuditOpinion());
                auditRecord.setAuditTime(new Date());
                auditRecord.setAuditor(nickName);
                auditOpinionLogs.add(auditRecord);
                shop.setAuditOpinionLogs(auditOpinionLogs);
            }
          //营业时间
          if(dto.getOpenTime()!=null){
          shop.setOpenTime(dto.getOpenTime());
          }
          //若是开关状态不为null,修改大店开关状态
          if(dto.getOpeningStatus()!=null){shop.setOpeningStatus(dto.getOpeningStatus());}
          //若是审核状态不为0,修改子门店审核状态
          if(dto.getAuditStatus()!=null&&!"".equals(dto.getAuditStatus()))shop.setAuditStatus(dto.getAuditStatus());
          //子门店关联配送站
          if(dto.getDeliveryStation()!=null&&!"".equals(dto.getDeliveryStation()))
          shop.setDeliveryStation(dto.getDeliveryStation());
          Shop save = shopRepository.save(shop);
          //开关是否传入
          if(dto.getOpeningStatus()!=null){
              //标识子门店是关
              Boolean flag = false;
              if(dto.getOpeningStatus()==0){
                  //子门店全关，关闭大店
                  SubShopQO subShopQO = new SubShopQO();
                  subShopQO.setShopId(shop.getParentId());
                  subShopQO.setShopType("SUBSHOP");
                  List<Shop> shops = shopRepository.find(subShopQO.toQuery());
                  for (Shop s:shops) {
                      //判断子门店是否开，有一个开为false,不关闭大店
                      if(s.getOpeningStatus()==1){
                          flag = true;
                      }
                  }
                  if(!flag){
                      UpdateBigShopDTO updateBigShopDTO = new UpdateBigShopDTO();
                      updateBigShopDTO.setId(shop.getParentId());
                      updateBigShopDTO.setOpeningStatus(0);
                      updateBigShopOpenState(updateBigShopDTO);
                  }
              }
              if(dto.getOpeningStatus()==1){
                  //判断大店是否开
                  BigShopVM bigShop = findBigShop(shop.getParentId());
                  if(bigShop.getOpeningStatus()==0){
                      //子门店开一个，大店开
                      UpdateBigShopDTO updateBigShopDTO = new UpdateBigShopDTO();
                      updateBigShopDTO.setId(shop.getParentId());
                      updateBigShopDTO.setOpeningStatus(1);
                      updateBigShopOpenState(updateBigShopDTO);
                  }
              }
          }
          return save;
        }
      )
      .map(shopMapper::toSubShopVm)
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
  public ShopVM findShop(String id) {
    return shopRepository
      .findById(id)
        .map(
        shop -> {
            if(StringUtil.isNotBlank(shop.getStoreType())){
                StoreTypeVM storeType = storeTypeService.findStoreType(shop.getStoreType());
                shop.setStoreType(storeType.getName());
            }
            //经营时长
            Date date = shop.getCreatedTime();
            long ts = date.getTime();
            Date date1 = new Date();
            long ts1 = date1.getTime();
            long l = (ts1 - ts) / (1000 * 60 * 60 * 24);
            System.out.println(l);
            shop.setOperationTime(String.valueOf(l));
            ResultBean<List<UserModel>> listResultBean = newUserService.listUserByShopId(shop.getId());
            shop.setPeopleNum(String.valueOf(listResultBean.getData().size()));
            return shop;
        }
        )
      .map(shopMapper::toShopVm)
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
  public SubShopVM findSubShop(String id) {
    return shopRepository
      .findById(id)
        .map(
            shop -> {
                //全局二维码
                if(shop.getQrCode()==null||"".equals(shop.getQrCode())){
                    List<BusinessConfigurationVM> all = businessConfigurationService.findAll(null);
                    BusinessConfigurationVM businessConfigurationVM = null;
                    if(all!=null&&all.size()>0){
                        businessConfigurationVM =all.get(0);
                        shop.setQrCode(businessConfigurationVM.getQrCode());
                        shop.setQrCodeText(businessConfigurationVM.getQrCodeDesc());
                    }
                }
                //门店营业状态
                shop.getOpenTime().setStatus(0);//歇业
                if(shop.getOpenTime().getOpenTimeType()!=null){
                    if(shop.getOpenTime().getOpenTimeType()==0){
                        try {
                            boolean timeRange = TimeUtil.isTimeRange(shop.getOpenTime().getTimeStart(), shop.getOpenTime().getTimeEnd());
                            if(timeRange){
                                shop.getOpenTime().setStatus(1);//营业中
                            }else{
                                shop.getOpenTime().setStatus(0);//歇业
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    if(shop.getOpenTime().getOpenTimeType()==1&&shop.getOpenTime().getManageTimes()!=null){
                        List<TimeQuantum> manageTimes = shop.getOpenTime().getManageTimes();
                        for (TimeQuantum time :manageTimes) {
                            try {
                                Date openingTime = TimeUtil.StringToDate(time.getOpeningTime());
                                Date closingTime = TimeUtil.StringToDate(time.getClosingTime());
                                boolean effectiveDate = TimeUtil.isEffectiveDate(openingTime, closingTime);
                                if(effectiveDate){
                                    shop.getOpenTime().setStatus(1);//营业中
                                }else{
                                    shop.getOpenTime().setStatus(0);//歇业
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                return shop;
            }
        )
        .map(shopMapper::toSubShopVm)
        .map(
            subShopVm -> {
                //返回子门店所属大点名称
                Shop shopById = findShopById(subShopVm.getParentId());
                subShopVm.setParentName(shopById.getName());
                subShopVm.setOperator(shopById.getOperator());
                subShopVm.setOperatorMsg(shopById.getOperatorMsg());
                return subShopVm;
            }
        )
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
  public BigShopVM findBigShop(String id) {
      return shopRepository
      .findById(id)
      .map(shopMapper::toBigShopVm)
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
  public FactoryVM findFactory(String id) {
    return shopRepository
      .findById(id)
      .map(shopMapper::toFactoryVm)
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
  public void delete(String id) {
    Shop shop = new Shop();
    shop.setId(id);
    shopRepository.delete(shop);
  }

  @Override
  public PageDTO<SeatingAreaVM> findAllSeatingArea(
    int current,
    int size,
    SeatingAreaQO query
  ) {
    Page<SeatingAreaVM> page = shopRepository
      .find(query.toQuery(), PageRequest.of(current-1, size))
      .map(
          shop -> {
              shop.setDiningStoreTables(new HashMap<String,DiningTable>());
              return shop;
          }
      )
      .map(shopMapper::toSeatingAreaVm);
    return new PageDTO<>(
      page.getNumber(),
      page.getSize(),
      page.getTotalElements(),
      page.getContent()
    );
  }

  @Override
  public List<SeatingAreaVM> findAllSeatingArea(SeatingAreaQO query) {
    return shopMapper.toSeatingAreaVm(shopRepository.find(query.toQuery()));
  }

  @Override
  public List<Shop> findAllShop(ShopQO query){
      return shopRepository.find(query.toQuery());
  }

  @Override
  public PageDTO<ShopVM> findAllShop(int current, int size, ShopQO query) {
    Page<ShopVM> page = shopRepository
      .find(query.toQuery(), PageRequest.of(current-1, size))
        .map(
            shop -> {
                if(StringUtil.isNotBlank(shop.getStoreType())){
                    StoreTypeVM storeType = storeTypeService.findStoreType(shop.getStoreType());
                    shop.setStoreType(storeType.getName());
                }
                //经营时长
                Date date = shop.getCreatedTime();
                long ts = date.getTime();
                Date date1 = new Date();
                long ts1 = date1.getTime();
                long l = (ts1 - ts) / (1000 * 60 * 60 * 24);
                System.out.println(l);
                shop.setOperationTime(String.valueOf(l));
                ResultBean<List<UserModel>> listResultBean = newUserService.listUserByShopId(shop.getId());
                shop.setPeopleNum(String.valueOf(listResultBean.getData().size()));
                return shop;
            }
        )
      .map(shopMapper::toShopVm);
    return new PageDTO<>(
      page.getNumber(),
      page.getSize(),
      page.getTotalElements(),
      page.getContent()
    );
  }

  @Override
  public PageDTO<BigShopVM> findAllBigShop(
    int current,
    int size,
    BigShopQO query
  ) {
    Page<BigShopVM> page = shopRepository
      .find(query.toQuery(), PageRequest.of(current-1, size))
        .map(
        shop -> {
            //经营时长
            Date date = shop.getCreatedTime();
            long ts = date.getTime();
            Date date1 = new Date();
            long ts1 = date1.getTime();
            long l = (ts1 - ts) / (1000 * 60 * 60 * 24);
            System.out.println(l);
            shop.setOperationTime(String.valueOf(l));
            ResultBean<List<UserModel>> listResultBean = newUserService.listUserByShopId(shop.getId());
            shop.setPeopleNum(String.valueOf(listResultBean.getData().size()));
            return shop;
        }
        )
      .map(shopMapper::toBigShopVm);
    return new PageDTO<>(
      page.getNumber(),
      page.getSize(),
      page.getTotalElements(),
      page.getContent()
    );
  }

  @Override
  public PageDTO<SubShopVM> findAllSubShop(
    int current,
    int size,
    SubShopQO query
  ) {
    Page<SubShopVM> page = shopRepository
      .find(query.toQuery(), PageRequest.of(current-1, size))
        .map(
            shop -> {
                //全局二维码
                if(shop.getQrCode()==null||"".equals(shop.getQrCode())){
                    List<BusinessConfigurationVM> all = businessConfigurationService.findAll(null);
                    BusinessConfigurationVM businessConfigurationVM = null;
                    if(all!=null&&all.size()>0){
                        businessConfigurationVM =all.get(0);
                        shop.setQrCode(businessConfigurationVM.getQrCode());
                        shop.setQrCodeText(businessConfigurationVM.getQrCodeDesc());
                    }
                }
                //门店营业状态
                shop.getOpenTime().setStatus(0);//歇业
                if(shop.getOpenTime().getOpenTimeType()!=null){
                    if(shop.getOpenTime().getOpenTimeType()==0){
                        try {
                            boolean timeRange = TimeUtil.isTimeRange(shop.getOpenTime().getTimeStart(), shop.getOpenTime().getTimeEnd());
                            if(timeRange){
                                shop.getOpenTime().setStatus(1);//营业中
                            }else{
                                shop.getOpenTime().setStatus(0);//歇业
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    if(shop.getOpenTime().getOpenTimeType()==1&&shop.getOpenTime().getManageTimes()!=null){
                        List<TimeQuantum> manageTimes = shop.getOpenTime().getManageTimes();
                        for (TimeQuantum time :manageTimes) {
                            try {
                                Date openingTime = TimeUtil.StringToDate(time.getOpeningTime());
                                Date closingTime = TimeUtil.StringToDate(time.getClosingTime());
                                boolean effectiveDate = TimeUtil.isEffectiveDate(openingTime, closingTime);
                                if(effectiveDate){
                                    shop.getOpenTime().setStatus(1);//营业中
                                }else{
                                    shop.getOpenTime().setStatus(0);//歇业
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                return shop;
            }
        )
      .map(shopMapper::toSubShopVm);
    return new PageDTO<>(
      page.getNumber(),
      page.getSize(),
      page.getTotalElements(),
      page.getContent()
    );
  }

    @Override
    public List<FactoryVM> findAllFactorys(FactoryQO query) {
        return shopMapper.toFactoryVm(shopRepository.find(query.toQuery()));
    }

  @Override
  public PageDTO<FactoryVM> findAllFactory(
    int current,
    int size,
    FactoryQO query
  ) {
    Page<FactoryVM> page = shopRepository
      .find(query.toQuery(), PageRequest.of(current-1, size))
        .map(
            shop -> {
                //经营时长
                Date date = shop.getCreatedTime();
                long ts = date.getTime();
                Date date1 = new Date();
                long ts1 = date1.getTime();
                long l = (ts1 - ts) / (1000 * 60 * 60 * 24);
                System.out.println(l);
                shop.setOperationTime(String.valueOf(l));
                ResultBean<List<UserModel>> listResultBean = newUserService.listUserByShopId(shop.getId());
                shop.setPeopleNum(String.valueOf(listResultBean.getData().size()));
                return shop;
            }
        )
      .map(shopMapper::toFactoryVm);
    return new PageDTO<>(
      page.getNumber(),
      page.getSize(),
      page.getTotalElements(),
      page.getContent()
    );
  }

  @Override
  public InventoryWarning createInventoryWarning(InventoryWarning dto) {
      log.debug("Request to save newBigShopDTO : {}", dto);
      Shop shop = shopMapper.toShopEntity(dto);
      shop = shopRepository.save(shop);
      InventoryWarning inventoryWarning = shopMapper.toInventoryWarning(shop);
      return inventoryWarning;
  }

  @Override
  public InventoryWarning updateInventoryWarning(InventoryWarning inventoryWarning) {
              if(inventoryWarning==null){
                  return null;
              }
              if(inventoryWarning!=null && inventoryWarning.getShopId()==null){
                    ShopQO shopQO = new ShopQO();
                    shopQO.setShopInventorytype(ShopInventorytype.GLOBALBIGSHOP);
                    //inventoryWarning.setShopInventorytype(ShopInventorytype.GLOBALBIGSHOP);
                    Optional<Shop>  shop =shopRepository.findOne(shopQO.toQuery());
                     Shop shop1=shop.get();
                    log.info("=================>{}"+shop.get().toString());
                    //inventoryWarning.setShopInventorytype(ShopInventorytype.GLOBALBIGSHOP);
                    shop1.setInventoryWarning(inventoryWarning);

                    if(shop.isPresent()){
                        Shop result=  shopRepository.save(shop1);
                        return shopMapper.toInventoryWarning(result);
                    }else {

                        inventoryWarning.setShopInventorytype("GLOBALBIGSHOP");
                        Shop shop2 = new Shop();
                        shop2.setInventoryWarning(inventoryWarning);
                        Shop result=  shopRepository.save(shop2);
                        return shopMapper.toInventoryWarning(result);

                    }

              }else  if(inventoryWarning!=null && inventoryWarning.getShopId()!=null && !"".equals(inventoryWarning.getShopId())){
                  Optional<Shop>  shop=shopRepository.findById(inventoryWarning.getShopId());
                  Shop shop1=shop.get();
                  shop1.setInventoryWarning(inventoryWarning);
                  Shop result= shopRepository.save(shop1);
                  return shopMapper.toInventoryWarning(result);
              }else {
                  return null;
              }


  }

  @Override
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

    @Override
    public InventoryWarning findShopInventoryWarning(ShopInventorytype shopInventorytype) {
        ShopQO shopQO = new ShopQO();
        shopQO.setShopInventorytype(ShopInventorytype.GLOBALBIGSHOP);
        return shopRepository
        .findOne(shopQO.toQuery())
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

    @Override
    public DiningTable getDiningTable(String shopId,String DeskNumber) {
        return shopRepository.findById(shopId)
        .map(
            shop -> {
                DiningTable diningTable = shop
                        .getDiningStoreTables()
                        .get(DeskNumber);
                return diningTable;
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

    @Override
    public List<BusyConfiguration> createOrUpdateBusyConfiguration(ShopBusynessDTO shopBusynessDTO) {

      return   shopRepository.findById(shopBusynessDTO.getShopid()).map(
                shop -> {
                    shop.setBusyConfigurations(shopBusynessDTO.getBusyConfigurations());
                    shopRepository.save(shop);
                    return shop;
                }
         ).get().getBusyConfigurations();

    }

    @Override
  public SeatingAreaVM updateDiningTable(
    UpdateDiningTableDTO updateDiningTableDTO
  ) {
        //TODO 查询桌号下的订单，存在未完成的订单，不可修改删除
    return shopRepository
      .findById(updateDiningTableDTO.getId())
      .map(
        shop -> {
            Map<String, DiningTable> diningStoreTables = new HashMap<>();
            try {
                diningStoreTables = shop.getDiningStoreTables();
            } catch (Exception e) {
                e.printStackTrace();
            }
            DiningTable diningTable = new DiningTable();
            if(diningStoreTables!=null){
                //原桌台对象
                diningTable = shop
                        .getDiningStoreTables()
                        .get(updateDiningTableDTO.getOriginalDeskNumber());
            }
          //新增桌台
          if(updateDiningTableDTO.getStartEnglishDeskNo()!=null&&!"".equals(updateDiningTableDTO.getStartEnglishDeskNo())&&
             updateDiningTableDTO.getStartDeskNo()!=null&&!"".equals(updateDiningTableDTO.getStartDeskNo())&&
             updateDiningTableDTO.getSeatCount()!=null){
              String startEnglishDeskNo = updateDiningTableDTO.getStartEnglishDeskNo();
              String startDeskNo = updateDiningTableDTO.getStartDeskNo();
              Integer seatCount = updateDiningTableDTO.getSeatCount();
              Map<String, DiningTable> diningStoreTables1 = new HashMap<>();
              List<String> diningViewTables = new ArrayList<>();
              for (int i = 0; i < seatCount; i++) {
                  //新桌台
                  DiningTable addiningTable = new DiningTable();
                  Integer temp = Integer.parseInt(startDeskNo) + i;
                  addiningTable.setTableNo(startEnglishDeskNo + temp);

                  Calendar instance = Calendar.getInstance();
                  instance.setTime(new Date());
                  int i2 = instance.get(Calendar.HOUR_OF_DAY);//时（24小时制）
                  int i3 = instance.get(Calendar.MINUTE);//分
                  int i4 = instance.get(Calendar.SECOND);//秒

                  addiningTable.setCreatedTime(i2+":"+i3+":"+i4);
                  addiningTable.setModifiedTime(i2+":"+i3+":"+i4);
                  addiningTable.setDelete(0);
                  addiningTable.setQRcode("");
                  addiningTable.setTableLink("");
                  try {
                      if(shop.getDiningStoreTables()!=null){
                          diningStoreTables1 = shop.getDiningStoreTables();
                      }
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
                  try {
                      if(shop.getDiningViewTables().size()>0){
                          diningViewTables = shop.getDiningViewTables();
                      }
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
                  //该桌台号是否存在，若存在则添加失败，若删除修改状态
                  boolean contains = diningViewTables.contains(addiningTable.getTableNo());
                  if(contains){
                      if(diningStoreTables1.get(addiningTable.getTableNo()).getDelete()==null||
                              diningStoreTables1.get(addiningTable.getTableNo()).getDelete()==0){
                          return new Shop()
                                  .setName(addiningTable.getTableNo()+"桌号已存在")
                                  .setParentId(addiningTable.getTableNo()+"桌号已存在");
                      }else{
                          //修改桌号状态
                          diningStoreTables1.get(addiningTable.getTableNo()).setDelete(0);
                          continue;
                      }
                  }
                  diningStoreTables1.put(addiningTable.getTableNo(), addiningTable);
                  diningViewTables.add(addiningTable.getTableNo());
              }
              shop.setDiningStoreTables(diningStoreTables1);
              shop.setDiningViewTables(diningViewTables);
          }
          //删除桌台
          if(updateDiningTableDTO.getDelete()!=null){
              diningTable.setDelete(updateDiningTableDTO.getDelete());
          }
          //关闭开启桌台
          if(updateDiningTableDTO.getOpenState()!=null){
             diningTable.setOpenState(updateDiningTableDTO.getOpenState());
          }
          //修改桌台号
          if(updateDiningTableDTO.getOriginalDeskNumber()!=null&&!"".equals(updateDiningTableDTO.getOriginalDeskNumber())&&
            updateDiningTableDTO.getNewDeskNumber()!=null&&!"".equals(updateDiningTableDTO.getNewDeskNumber())){
              if(diningTable!=null){
                  if(updateDiningTableDTO.getNewDeskNumber()!=null&&!"".equals(updateDiningTableDTO.getNewDeskNumber())){
                      List<String> diningViewTables = shop.getDiningViewTables();
                      boolean contains = diningViewTables.contains(updateDiningTableDTO.getNewDeskNumber());
                      if(contains){
                          return new Shop()
                                  .setName("桌号已存在")
                                  .setParentId("桌号已存在");
                      }
                      //修改桌台号
                      diningTable.setTableNo(updateDiningTableDTO.getNewDeskNumber());
                      //二维码设为""
                      diningTable.setQRcode("");

                      Calendar instance = Calendar.getInstance();
                      instance.setTime(new Date());
                      int i2 = instance.get(Calendar.HOUR_OF_DAY);//时（24小时制）
                      int i3 = instance.get(Calendar.MINUTE);//分
                      int i4 = instance.get(Calendar.SECOND);//秒

                      diningTable.setModifiedTime(i2+":"+i3+":"+i4);
                      diningStoreTables.remove(
                              updateDiningTableDTO.getOriginalDeskNumber()
                      );
                      diningStoreTables.put(
                              updateDiningTableDTO.getNewDeskNumber(),
                              diningTable
                      );
                      shop.setDiningStoreTables(diningStoreTables);
                      List<String> dingtable = shop.getDiningViewTables();
                      dingtable.remove(updateDiningTableDTO.getOriginalDeskNumber());
                      dingtable.add(updateDiningTableDTO.getNewDeskNumber());
                      shop.setDiningViewTables(dingtable);
                  }
              }else{
                  log.debug("桌号不能为空");
                  Shop shopError = new Shop()
                          .setParentId("桌号不存在")
                          .setName("桌号不存在");
                  return shopError;
              }
          }
            Shop save = shopRepository.save(shop);
            Map<String,Map<String, DiningTable>>  temp = generateQrcode(save.getName(),save.getId(),save.getDiningStoreTables());
            save.setDiningStoreTables(temp.get(save.getId()));
            List<DiningTable> diningTables = getDiningTables(save.getId());
            save.setSeatCount(diningTables.size());
            return shopRepository.save(shop);
        }
      )
      .map(shopMapper::toSeatingAreaVm)
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
  public List<DiningTable> getDiningTables(String id) {
      List<DiningTable> diningTableList = new ArrayList<>();
      Shop shop = shopRepository.findById(id).get();
      Map<String, DiningTable> diningTableMap = shop.getDiningStoreTables();
      if (!diningTableMap.isEmpty()) {
        for (DiningTable diningTable : diningTableMap.values()) {
            if(diningTable.getDelete()==null||diningTable.getDelete()==0){
                diningTableList.add(diningTable);
            }
        }
      }else{
          new BusinessException(
                  "message.nodata",
                  "\\u672A\\u67E5\\u8BE2\\u5230\\u6570\\u636E",
                  "zh_CN"
          );
      }
      return diningTableList;
  }

  @Override
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
    @Override
    public ShopTimeVM updateShopTime(Shop shopdto) {
        return shopRepository
            .findById(shopdto.getId())
            .map(
                shop -> {
                    shop.setUpdateShopTime(shopdto.getUpdateShopTime());
                    shopRepository.save(shop);
                    return shop;
                }
            )
            .map(shopMapper::toShopTimeVm)
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
    public Boolean updateShopTime(List<Shop> shops) {
        for (Shop shop:shops) {
            try {
                updateShopTime(shop);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }


    public  Shop gerernateShopTime(String shopId,Shop shop){
        Result<List<OperationCateTree>> result =operationCategoryService.queryOperationCateShopTreeFront(shopId,null);
        List<OperationCateTree> operationCateTreeList = result.getData();
        List<UpdateShopTimeDTO> updateShopTime = new ArrayList<UpdateShopTimeDTO>();
        if(operationCateTreeList!=null){
            for(OperationCateTree operationCateTree : operationCateTreeList){
                UpdateShopTimeDTO  updateShop= new  UpdateShopTimeDTO();
                updateShop.setName(operationCateTree.getName());
                updateShop.setParentId(operationCateTree.getParentId());
                updateShop.setCategoryId(operationCateTree.getVirtualKey());
                updateShop.setSduration("");
                List<UpdateShopTimeDTO> newUpdate = new ArrayList<UpdateShopTimeDTO>();
                List<OperationCateTree> tempUpdate =operationCateTree.getChildren();
                if(tempUpdate!=null){
                    for(OperationCateTree variable : tempUpdate) {
                        UpdateShopTimeDTO updateShopTimeDTO = new UpdateShopTimeDTO();
                        updateShopTimeDTO.setSduration("");
                        updateShopTimeDTO.setCategoryId(variable.getVirtualKey());
                        updateShopTimeDTO.setParentId(variable.getParentId());
                        updateShopTimeDTO.setName(variable.getName());
                        newUpdate.add(updateShopTimeDTO);
                    }
                    updateShop.setChildren(newUpdate);
                }
                updateShopTime.add(updateShop);
            }
        }
        shop.setUpdateShopTime(updateShopTime);
        return shop;
    }




    @Override
    public ShopTimeVM findShopTime(String id) {
        return findNewShopTime(id);
    }

    public Map<Long,List<PubNavEntryVM>> getMap(List<PubNavEntryVM> pubNavEntryVMS){
        Map<Long,List<PubNavEntryVM>> map = new HashMap<Long,List<PubNavEntryVM>>();
        for(PubNavEntryVM pubNavEntry : pubNavEntryVMS){
            if(pubNavEntry.getParentClassId()==0){
                map.put(pubNavEntry.getClassId(), new ArrayList<PubNavEntryVM>());
            }
        }
        for (Long key : map.keySet()){
            for(PubNavEntryVM pubNavEntry : pubNavEntryVMS){
                if(pubNavEntry.getParentClassId().equals(key)){
                    map.get(key).add(pubNavEntry);
                }
            }
        }
        return map;
    }

    public Map<Long,List<UpdateShopTimeDTO>> getShoptimeMap(Map<Long,List<PubNavEntryVM>> longListMap){
        Map<Long,List<UpdateShopTimeDTO>> tempmap = new HashMap<Long,List<UpdateShopTimeDTO>>();

        for (Long key : longListMap.keySet()){
            List<PubNavEntryVM>  pubNavEntryVMS = longListMap.get(key);
            List<UpdateShopTimeDTO> temp = new ArrayList<UpdateShopTimeDTO>();
            for(PubNavEntryVM pubNav : pubNavEntryVMS){
                UpdateShopTimeDTO updateShopTimeDTO = new UpdateShopTimeDTO();
                updateShopTimeDTO.setCategoryId(pubNav.getClassId()+"");
                updateShopTimeDTO.setSduration("");
                updateShopTimeDTO.setName(pubNav.getClassName());
                updateShopTimeDTO.setParentId(pubNav.getParentClassId()+"");
            }
            tempmap.put(key,temp);
        }
        return tempmap;
    }



    public Map<Long,PubNavEntryVM> getMapPub(List<PubNavEntryVM> pubNavEntryVMS){
        Map<Long,PubNavEntryVM> map = new HashMap<Long,PubNavEntryVM>();
        for(PubNavEntryVM pubNavEntry : pubNavEntryVMS){
            if(pubNavEntry.getParentClassId()==0){
                PubNavEntryVM pubNavEntryVM = new PubNavEntryVM();
                pubNavEntryVM.setParentClassId(pubNavEntry.getParentClassId());
                pubNavEntryVM.setLeaf(pubNavEntry.getLeaf());
                pubNavEntryVM.setClassId(pubNavEntry.getClassId());
                pubNavEntryVM.setClassName(pubNavEntry.getClassName());
                map.put(pubNavEntry.getClassId(), pubNavEntryVM);
            }
        }
        return map;
    }

    public List<UpdateShopTimeDTO> getLastShopTime(){//生成基础类目生产时长
        List<UpdateShopTimeDTO> updateShopTimeDTOS = new ArrayList<UpdateShopTimeDTO>();//生产时长集合
        List<PubNavEntryVM> pubNavEntryVMS = categoryService.getAll();
        if(pubNavEntryVMS!=null&&pubNavEntryVMS.size()>0){//非空判断
            Map<Long,List<PubNavEntryVM>>  map = getMap(pubNavEntryVMS);
            Map<Long,PubNavEntryVM> firstmap = getMapPub(pubNavEntryVMS);
            Map<Long,List<UpdateShopTimeDTO>> listMap =  getShoptimeMap(map);
            //如果未设置生产时长，会拉取所有类目 并返回到前端
            log.info("一级类目插入");
            for(PubNavEntryVM pubNavEntry : pubNavEntryVMS){
                if(pubNavEntry.getParentClassId()==0){
                    UpdateShopTimeDTO  updateShop= new  UpdateShopTimeDTO();
                    updateShop.setParentId(pubNavEntry.getParentClassId()+"");
                    updateShop.setName(pubNavEntry.getClassName());
                    updateShop.setSduration("");
                    updateShop.setCategoryId(pubNavEntry.getClassId()+"");
                    updateShopTimeDTOS.add(updateShop);
                }
            }
            for(UpdateShopTimeDTO updateShopTimeDTO:updateShopTimeDTOS){
                List<UpdateShopTimeDTO> newupdate = new ArrayList<UpdateShopTimeDTO>();//二级类目集合
                for (Long key : map.keySet()){
                    if(updateShopTimeDTO.getCategoryId().equals(key.toString())){
                        List<PubNavEntryVM> pubNavEntryVMS1= map.get(key);
                        for(PubNavEntryVM pubNavEntryVM : pubNavEntryVMS1){
                            UpdateShopTimeDTO update =  new UpdateShopTimeDTO();
                            update.setCategoryId(pubNavEntryVM.getClassId()+"");
                            update.setSduration("");
                            update.setName(pubNavEntryVM.getClassName());
                            update.setParentId(pubNavEntryVM.getParentClassId()+"");
                            newupdate.add(update);
                        }
                    }
                }
                updateShopTimeDTO.setChildren(newupdate);
            }
        }
        return updateShopTimeDTOS;
    }

    public ShopTimeVM findNewShopTime(String id) {
      log.info("findNewShopTime:id{}"+id);
      return
          shopRepository.findById(id).map(
           shop -> {
               if(shop.getUpdateShopTime()!=null&&shop.getUpdateShopTime().size()>0){
                   List<UpdateShopTimeDTO> lastShopTime = getLastShopTime();
                   List<UpdateShopTimeDTO> updateShopTime = shop.getUpdateShopTime();
                   for (UpdateShopTimeDTO shopTime:lastShopTime){//全局
                       for (UpdateShopTimeDTO oldShopTime:updateShopTime) {//门店
                           if(shopTime.getCategoryId().equals(oldShopTime.getCategoryId())){
                               shopTime.setCategoryId(oldShopTime.getCategoryId());
                               shopTime.setName(oldShopTime.getName());
                               shopTime.setParentId(oldShopTime.getParentId());
                               shopTime.setSduration(oldShopTime.getSduration());
                               shopTime.setChildren(oldShopTime.getChildren());
                           }
                       }
                   }
                   shop.setUpdateShopTime(lastShopTime);
               }else{
                   List<UpdateShopTimeDTO> lastShopTime = getLastShopTime();//生成基础类目生产时长
                   shop.setUpdateShopTime(lastShopTime);
               }
               return shop;
           }
           ).map(shopMapper::toShopTimeVm)
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
