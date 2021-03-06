package cn.zxtaotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import cn.zxtaotao.manage.pojo.ItemDesc;
import cn.zxtaotao.web.bean.Item;
import cn.zxtaotao.web.service.ItemService;

@Controller
@RequestMapping("item")
public class ItemController {

    @Autowired
    private ItemService itemService;
    
    @RequestMapping(value="{itemId}",method=RequestMethod.GET)
    public ModelAndView queryItemById(@PathVariable("itemId")Long itemId){
        ModelAndView modelAndView = new ModelAndView("item");
        
        Item item = this.itemService.queryItemById(itemId);
        modelAndView.addObject("item", item);
        
        ItemDesc itemDesc = this.itemService.queryItemDescByItemId(itemId);
        modelAndView.addObject("itemDesc", itemDesc);
        
        String html = this.itemService.queryItemParamByItemId(itemId);
        modelAndView.addObject("itemParam", html);
        
        return modelAndView;
    }
}
