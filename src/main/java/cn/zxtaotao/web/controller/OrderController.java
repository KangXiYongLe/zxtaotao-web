package cn.zxtaotao.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.zxtaotao.sso.query.bean.User;
import cn.zxtaotao.web.bean.Cart;
import cn.zxtaotao.web.bean.Item;
import cn.zxtaotao.web.bean.Order;
import cn.zxtaotao.web.service.CartService;
import cn.zxtaotao.web.service.ItemService;
import cn.zxtaotao.web.service.OrderService;
import cn.zxtaotao.web.threadLocal.UserThreadLocal;

@Controller
@RequestMapping("order")
public class OrderController {

    @Autowired
    private ItemService itemService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private CartService cartService;
    
    
    /**
     * 转到订单页
     * @param itemId
     * @return
     */
    @RequestMapping(value="{itemId}",method=RequestMethod.GET)
    public ModelAndView toOrder(@PathVariable("itemId")Long itemId){
        ModelAndView modelAndView = new ModelAndView("order");
        //查询商品数据
        Item item = itemService.queryItemById(itemId);
        modelAndView.addObject("item", item);
        return modelAndView;
    }
    
    /**
     * 提交订单
     * @param order
     * @return
     */
    @RequestMapping(value="submit",method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> submitOrder(Order order){
        Map<String, Object> result = new HashMap<String, Object>();
        User user = UserThreadLocal.get();
        order.setUserId(user.getId());
        order.setBuyerNick(user.getUsername());
        String orderId = this.orderService.submitOrder(order);
        if(StringUtils.isEmpty(orderId)){//提交订单失败
            result.put("status", 300);
        }else{//提交订单成功
            result.put("status", 200);
            result.put("data", orderId);
        }
        return result;
    }
    
    /**
     * 转到订单提交成功页
     * @param orderId
     * @return
     */
    @RequestMapping(value="success",method=RequestMethod.GET)
    public ModelAndView success(@RequestParam("id")String orderId){
        ModelAndView modelAndView = new ModelAndView("success");
        Order order = this.orderService.queryOrderById(orderId);
        modelAndView.addObject("order", order);
        modelAndView.addObject("date", new DateTime().plusDays(2).toString("MM月dd日"));//用时间组件操作时间，在当前时间的基础上加两天，并且格式化成字符串
        return modelAndView;
    }
    
    //基于购物车的订单确认页
    
    @RequestMapping(value="create",method=RequestMethod.GET)
    public ModelAndView toCartOrder(){
        ModelAndView modelAndView = new ModelAndView("order-cart-old");
                
        //通过购物车系统提供的接口服务查询购物车数据
        List<Cart> carts = this.cartService.queryCartList();
        
        modelAndView.addObject("carts", carts);
        return modelAndView;
    }
    
}
