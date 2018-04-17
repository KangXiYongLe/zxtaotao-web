package cn.zxtaotao.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.zxtaotao.common.service.ApiService;
import cn.zxtaotao.common.service.RedisService;
import cn.zxtaotao.manage.pojo.ItemDesc;
import cn.zxtaotao.web.bean.Item;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Service
public class ItemService {
    
    @Autowired
    private ApiService apiService;
    @Autowired
    private RedisService redisService;
    
    @Value("${TAOTAO_MANAGE_URL}")
    private String TAOTAO_MANAGE_URL;
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static final String REDIS_KEY = "TAOTAO_WEB_ITEM_DETAIL_";
    private static final Integer REDIS_TIME = 60*60*60*24;
    
    public Item queryItemById(Long itemId){ 
        
        String key = REDIS_KEY+itemId;
        
        try {//从缓存中命中数据，如果命中到就return
            String cacheData = this.redisService.get(key);
            if(StringUtils.isNotEmpty(cacheData)){
                return MAPPER.readValue(cacheData, Item.class);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } 
        
        
        try {
            String url = this.TAOTAO_MANAGE_URL+"/rest/api/item/"+itemId;
            String jsonData = this.apiService.doGet(url);
            if(StringUtils.isEmpty(jsonData)){
                return null;
            }
            
            try {//return之前，把数据set到redis缓存中,为确保缓存不影响业务逻辑的执行，必须catch
                this.redisService.set(key, jsonData, REDIS_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }           
            
            return MAPPER.readValue(jsonData, Item.class);
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return null;
    }

    public ItemDesc queryItemDescByItemId(Long itemId) {
        try {
            String url = this.TAOTAO_MANAGE_URL+"/rest/api/item/desc/"+itemId;
            String jsonData = this.apiService.doGet(url);
            return MAPPER.readValue(jsonData, ItemDesc.class);
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return null;
    }

    public String queryItemParamByItemId(Long itemId) {
        try {
            String url = this.TAOTAO_MANAGE_URL+"/rest/api/item/param/item/"+itemId;
            String jsonData = this.apiService.doGet(url);

            JsonNode jsonNode = MAPPER.readTree(jsonData);
            String paramData = jsonNode.get("paramData").asText();
            ArrayNode arrayNode = (ArrayNode) MAPPER.readTree(paramData);
            
            StringBuilder builder = new StringBuilder();
            builder.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\"><tbody>");
            for (JsonNode param : arrayNode)
            {
                builder.append("<tr><th class=\"tdTitle\" colspan=\"2\">" + param.get("group").asText() + "</th></tr>");
              
              ArrayNode params = (ArrayNode)param.get("params");
              for (JsonNode p : params) {
                  builder.append("<tr><td class=\"tdTitle\">" + p.get("k").asText() + "</td><td>" + p.get("v").asText() + "</td></tr>");
              }
            }
            builder.append("</tbody></table>");
            
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return null;
    }

}
