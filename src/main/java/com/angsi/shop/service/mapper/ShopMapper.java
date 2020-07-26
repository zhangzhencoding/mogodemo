package com.angsi.shop.service.mapper;

import com.angsi.shop.VM.*;
import com.angsi.shop.domain.Shop;
import com.angsi.shop.dto.bigshop.NewBigShopDTO;
import com.angsi.shop.dto.common.InventoryWarning;
import com.angsi.shop.dto.factory.NewFactoryDTO;
import com.angsi.shop.dto.seatingarea.NewSeatingAreaDTO;
import com.angsi.shop.dto.shop.NewShopDTO;
import com.angsi.shop.dto.subshop.NewSubShopDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity
 */
@Mapper(componentModel = "spring", uses = {})
public interface ShopMapper extends EntityMapper<NewShopDTO, Shop> {
  default Shop fromId(String id) {
    if (id == null) {
      return null;
    }
    Shop shop = new Shop();
    shop.setId(id);
    return shop;
  }

  BigShopVM toBigShopVm(Shop var1);

  Shop toBigShopEntity(NewBigShopDTO var1);

  SubShopVM toSubShopVm(Shop var1);

  @Mapping(target = "email", source = "inventoryWarning.email")
  @Mapping(target = "mobilePhone", source = "inventoryWarning.mobilePhone")
  @Mapping(
    target = "warningMethods",
    source = "inventoryWarning.warningMethods"
  )
  @Mapping(target = "copywriting", source = "inventoryWarning.copywriting")
  @Mapping(target = "inventoryVaue", source = "inventoryWarning.inventoryVaue")
  InventoryWarning toInventoryWarning(Shop var1);

  Shop toShopEntity(InventoryWarning var1);

  Shop toSubShopEntity(NewSubShopDTO var1);

  FactoryVM toFactoryVm(Shop var1);

  List<FactoryVM> toFactoryVm(List<Shop> var1);

  Shop toFactoryEntity(NewFactoryDTO var1);

  ShopVM toShopVm(Shop var1);

  ShopTimeVM toShopTimeVm(Shop var1);

  Shop toShopEntity(NewFactoryDTO var1);

  Shop toSeatingAreaEntity(NewSeatingAreaDTO var1);

  SeatingAreaVM toSeatingAreaVm(Shop var1);

  List<SubShopVM> toSubShopVm(List<Shop> entityList);

  List<BigShopVM> toBigShopVm(List<Shop> entityList);

  List<SeatingAreaVM> toSeatingAreaVm(List<Shop> entityList);
}
