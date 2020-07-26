package com.angsi.shop.repository.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vmcshop.oms.bo.DistanceBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;


/**
 * 根据经纬度算距离
 * @author gaobin
 */
@Slf4j
public class GeoUtil {
    private static final String DISTANCE_URL = "https://restapi.amap.com/v3/distance";

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    //调用第三方接口，根据区域查询经纬度
    public static String getCoordinate(String address){
        RestTemplate restTemplate = new RestTemplate();
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
        MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
        //  添加请求的参数
        params.add("hello", "hello");             //必传
        params.add("world", "world");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        //  执行HTTP请求
        String url = "http://restapi.amap.com/v3/geocode/geo?key=4f6ee1da3789622cfc23bc913c078b23&s=rsv3&address="+address+"";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);  //最后的参数需要用String.class  使用其他的会报错
        String body = response.getBody();
        System.out.println(body);
        String coordinate=null;
        if(body != null){
            JSONObject jsonObject = (JSONObject) JSON.parse(body);
            JSONArray data =jsonObject.getJSONArray("geocodes");
            System.out.println(data.get(0));
            JSONObject location = (JSONObject) JSON.parse(data.get(0).toString());
            coordinate = location.getString("location");
            System.out.println(coordinate);
        }
        return coordinate;
    }


    /**
     * 地球半径：6378.137KM
     */
    private static double EARTH_RADIUS = 6378.137;
    /**
     * 根据经纬度和距离返回一个矩形范围
     *
     * @param lng
     *            经度
     * @param lat
     *            纬度
     * @param distance
     *            距离(单位为米)
     * @return [lng1,lat1, lng2,lat2] 矩形的左下角(lng1,lat1)和右上角(lng2,lat2)
     */
    public static double[] getRectangle(double lng, double lat, long distance) {
        float delta = 111000;
        if (lng != 0 && lat != 0) {
            double lng1 = lng - distance
                    / Math.abs(Math.cos(Math.toRadians(lat)) * delta);
            double lng2 = lng + distance
                    / Math.abs(Math.cos(Math.toRadians(lat)) * delta);
            double lat1 = lat - (distance / delta);
            double lat2 = lat + (distance / delta);
            return new double[] { lng1, lat1, lng2, lat2 };
        } else {
            // TODO ZHCH 等于0时的计算公式
            double lng1 = lng - distance / delta;
            double lng2 = lng + distance / delta;
            double lat1 = lat - (distance / delta);
            double lat2 = lat + (distance / delta);
            return new double[] { lng1, lat1, lng2, lat2 };
        }
    }

    /**
     * 得到两点间的距离 米
     *
     * @param lat1 第一点纬度
     * @param lng1 第一点经度
     * @param lat2 第二点纬度
     * @param lng2 第二点经度
     * @return
     */
    public static double getDistanceOfMeter(double lat1, double lng1,
                                            double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10;
        return s;
    }
    /**
     * 距离测量API服务地址
     *
     * @param origins     出发点
     * @param destination 目的地
     * @param key 请求服务权限标识(高德)
     * @return
     */
    public static String getDistanceUrl(String origins, String destination,String key,Integer type) {
        String urlString = DISTANCE_URL+"?origins=" + origins + "&destination=" + destination + "&key=" + key+ "&type=" + type;
        return urlString;
    }
    /**
     * 获取二个地点之间的路径距离
     * @param origins 出发地
     * @param destination 目的地
     * @param key 请求服务权限标识(高德)
     * @param type 路径计算的方式和方法 0：直线距离   1：驾车导航距离（仅支持国内坐标） 3：步行规划距离（仅支持5km之间的距离）
     * @return
     */
    public static String getDistance(RestTemplate restTemplate,String origins, String destination,String key,Integer type) {
        String jsonString = restTemplate.getForObject(getDistanceUrl(origins,destination,key,type),String.class);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        String distance = jsonObject.getJSONArray("results").getJSONObject(0).getString("distance");
        return distance;
    }

    /**
     * 获取二个地点之间的路径距离
     * @param origins 出发地
     * @param destination 目的地
     * @param key 请求服务权限标识(高德)
     * @param type 路径计算的方式和方法 0：直线距离   1：驾车导航距离（仅支持国内坐标） 3：步行规划距离（仅支持5km之间的距离）
     * @return
     */
    public static DistanceBO getDistanceBO(RestTemplate restTemplate, String origins, String destination, String key, Integer type) {
        String jsonString = restTemplate.getForObject(getDistanceUrl(origins,destination,key,type),String.class);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        String distance = jsonObject.getJSONArray("results").getJSONObject(0).getString("distance");
        String duration = jsonObject.getJSONArray("results").getJSONObject(0).getString("duration");
        DistanceBO distanceBO = new DistanceBO();
        distanceBO.setDistance(Long.parseLong(distance));
        distanceBO.setDuration(Long.parseLong(duration));
        return distanceBO;
    }
    /**
     * 改变经纬度位置
     * @param latitudeLongitude
     * @return latitudeLongitude
     */
    public static String changeStrPosition(String latitudeLongitude){
        String[] latitudeLongitudes =latitudeLongitude.split(",");
        String longitudeLatitude = latitudeLongitudes[1]+","+latitudeLongitudes[0];
        return longitudeLatitude;
    }

    /**
     *
     * @param distance 距离 单位米
     * @return
     */
    public static String getDisTance(int distance){
        int limit = 1000;
        int limit2 = 100;
        if(distance<limit2){
            return "<100m";
        }
        if(distance>=limit){
            if(distance%limit==0){
                return distance/limit+"km";
            }
            return new BigDecimal(distance).divide(new BigDecimal(limit),6,BigDecimal.ROUND_HALF_UP).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue()+"km";
        }
        return distance+"m";
    }
}
