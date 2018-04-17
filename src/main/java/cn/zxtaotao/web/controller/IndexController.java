package cn.zxtaotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import cn.zxtaotao.web.service.IndexService;

@Controller
public class IndexController {
    
    @Autowired
    private IndexService indexService;
    
    @RequestMapping(value="index",method=RequestMethod.GET)
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("index");
        
        String indexAD1 = this.indexService.queryIndexAD1();
        
        String indexAD2 =this.indexService.queryIndexAD2();
        
        modelAndView.addObject("indexAD1", indexAD1);//向model中添加一个属性，类似于在request域中添加了一个属性
        
        modelAndView.addObject("indexAD2", indexAD2);
        
        return modelAndView;
    }

}
