package cn.zxtaotao.web.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.zxtaotao.common.service.ApiService;
import cn.zxtaotao.sso.query.bean.User;
import cn.zxtaotao.web.bean.Cart;
import cn.zxtaotao.web.threadLocal.UserThreadLocal;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CartService {
    
    @Value("${TAOTAO_CART_URL}")
    private String TAOTAO_CART_URL;
    
    @Autowired
    private ApiService apiService;
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    /**
     * 通过购物车系统的接口获得购物车商品列表数据
     * @return
     */
    public List<Cart> queryCartList() {
        User user = UserThreadLocal.get();
        String url = this.TAOTAO_CART_URL + "/service/api/cart/" + user.getId();
        try {
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isEmpty(jsonData)) {
                return null;
            }
            return MAPPER.readValue(jsonData,
                    MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
