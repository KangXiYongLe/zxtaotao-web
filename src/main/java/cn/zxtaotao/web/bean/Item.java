package cn.zxtaotao.web.bean;

import org.apache.commons.lang3.StringUtils;

/**
 * 继承cn.zxtaotao.manage.pojo.Item，添加getImages方法，这是页面要的属性
 * @author zengkang
 *
 */
public class Item extends cn.zxtaotao.manage.pojo.Item {

    public String[] getImages(){
        return StringUtils.split(super.getImage(), ",");
    }
}
