package cn.zxtaotao.web.mq;

import org.springframework.beans.factory.annotation.Autowired;

import cn.zxtaotao.common.service.RedisService;
import cn.zxtaotao.web.service.ItemService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ItemMQHandler {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private RedisService redisService;

    public void execute(String msg) {
        try {
            // 消息是一个json字符串，向将其转化，取出其中的值
            JsonNode jsonNode = MAPPER.readTree(msg);
            Long itemId = jsonNode.get("itemId").asLong();

            // 删除redis中该商品的缓存数据
            String key = ItemService.REDIS_KEY + itemId;
            this.redisService.del(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
