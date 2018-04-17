package cn.zxtaotao.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.zxtaotao.common.httpclient.HttpResult;
import cn.zxtaotao.common.service.ApiService;
import cn.zxtaotao.web.bean.Order;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OrderService {

    @Value("${TAOTAO_ORDER_URL}")
    private String TAOTAO_ORDER_URL;
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    @Autowired
    private ApiService apiService;
    
    public String submitOrder(Order order) {
        String url = TAOTAO_ORDER_URL+"/order/create";
        try {
            HttpResult httpResult = this.apiService.doPostJson(url, MAPPER.writeValueAsString(order));
            if(httpResult.getCode().intValue()==200){
                String jsonData=httpResult.getBody();
                JsonNode jsonNode = MAPPER.readTree(jsonData);
                if(jsonNode.get("status").intValue()==200){
                    return jsonNode.get("data").asText();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return null;
    }

    public Order queryOrderById(String orderId) {
        String url = TAOTAO_ORDER_URL+"/order/query/"+orderId;
        try {
            String jsonData = this.apiService.doGet(url);
            if(StringUtils.isNoneEmpty(jsonData)){
                return MAPPER.readValue(jsonData, Order.class);
            }            
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return null;
    }

}
