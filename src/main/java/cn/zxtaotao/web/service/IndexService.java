package cn.zxtaotao.web.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.zxtaotao.common.service.ApiService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Service
public class IndexService {
    
    @Autowired
    private ApiService apiService;
    
    @Value("${TAOTAO_MANAGE_URL}")
    private String TAOTAO_MANAGE_URL;
    @Value("${INDEX_AD1_URL}")
    private String INDEX_AD1_URL;
    @Value("${INDEX_AD2_URL}")
    private String INDEX_AD2_URL;
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    /**
     * 访问后台系统接口，获取首页大广告内容数据
     * @return
     */
    public String queryIndexAD1(){
        
        String url=this.TAOTAO_MANAGE_URL+this.INDEX_AD1_URL;
        try {
            String jsonData = this.apiService.doGet(url);//后台接口返回的是一个json字符串
            if(null==jsonData){
                return null;
            }
            //解析json数据
            JsonNode jsonNode = MAPPER.readTree(jsonData);//解析读取json字符串树，获得json节点
            ArrayNode rows=(ArrayNode) jsonNode.get("rows");//获取json中rows节点的数据，因为rows节点返回的数据是一个数组，所以用jsonNode的子类对象ArrayNode接收
            
            //因为父类JsonNode实现了Iterable接口，所以是可迭代的，我们可以遍历rows，将单对json数据封装进map,然后装入集合
            List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
            for (JsonNode row : rows) {
                Map<String, Object> map = new LinkedHashMap<String, Object>();//让Map中的元素保持存入时的顺序
                map.put("srcB", row.get("pic").asText());//获取指定的node节点，转换成文本内容
                map.put("height", 240);
                map.put("alt", row.get("title").asText());
                map.put("width", 670);
                map.put("src", row.get("pic").asText());
                map.put("widthB", 550);
                map.put("href", row.get("url").asText());
                map.put("heightB", 240); 
                
                result.add(map);
            }
            
            return MAPPER.writeValueAsString(result);//用于将任何Java值序列化为String的方法
            
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
        
    }

    public String queryIndexAD2() {
        String url=this.TAOTAO_MANAGE_URL+this.INDEX_AD2_URL;
        try {
            String jsonData = this.apiService.doGet(url);//后台接口返回的是一个json字符串
            if(null==jsonData){
                return null;
            }
            //解析json数据
            JsonNode jsonNode = MAPPER.readTree(jsonData);//解析读取json字符串树，获得json节点
            ArrayNode rows=(ArrayNode) jsonNode.get("rows");//获取json中rows节点的数据，因为rows节点返回的数据是一个数组，所以用jsonNode的子类对象ArrayNode接收
            
            //因为父类JsonNode实现了Iterable接口，所以是可迭代的，我们可以遍历rows，将单对json数据封装进map,然后装入集合
            List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
            /*[
             {
                 "width": 310,
                 "height": 70,
                 "src": "/images/5440ce68Na00d019e.jpg",
                 "href": "http://c.fa.jd.com/adclick?sid=2&cid=601&aid=3614&bid=4196&unit=35984&advid=109277&guv=&url=http://sale.jd.com/mall/FQLUNlG53wbX7m.html",
                 "alt": "",
                 "widthB": 210,
                 "heightB": 70,
                 "srcB": "http://img14.360buyimg.com/da/jfs/t334/155/1756719493/14371/e367c503/5440ce6dNd056ce39.jpg"
             }
         ];*/
            for (JsonNode row : rows) {
                Map<String, Object> map = new LinkedHashMap<String, Object>();//让Map中的元素保持存入时的顺序
                map.put("width", 310);
                map.put("height", 70);
                map.put("src", row.get("pic").asText());
                map.put("href", row.get("url").asText());
                map.put("alt", row.get("title").asText());
                map.put("widthB", 210);
                map.put("heightB", 70); 
                map.put("srcB", row.get("pic").asText());//获取指定的node节点，转换成文本内容
                               
                result.add(map);
            }
            
            return MAPPER.writeValueAsString(result);//用于将任何Java值序列化为String的方法
            
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }

}
