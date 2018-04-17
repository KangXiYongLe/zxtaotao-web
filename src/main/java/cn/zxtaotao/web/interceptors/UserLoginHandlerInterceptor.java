package cn.zxtaotao.web.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.zxtaotao.common.utils.CookieUtils;
import cn.zxtaotao.sso.query.bean.User;
import cn.zxtaotao.web.service.UserService;
import cn.zxtaotao.web.threadLocal.UserThreadLocal;

public class UserLoginHandlerInterceptor implements HandlerInterceptor {
    
    public static final String COOKIE_NAME = "ZXTT_TOKEN";
    
    @Autowired
    private UserService userService;
    

    @Override//目标conroller执行前调用此方法，返回true表示继续执行，返回false表示继续执行
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String loginUrl = userService.TAOTAO_SSO_URL+"/user/login.html";
        String token = CookieUtils.getCookieValue(request, COOKIE_NAME);
//        String sendUrl = request.getRequestURL().toString();
        String sendUrl = request.getHeader("referer");
        if(StringUtils.isEmpty(token)){//没有token的cookie,表示未登录
            //重定向到登陆页面
            response.sendRedirect(loginUrl+"?sendUrl="+sendUrl);
            return false;
        }
        //如果有token，根据token查询redis中用户是否存在，如果不存在，表示登陆超时
        User user = userService.queryByToken(token);
        if(user == null){
          //重定向到登陆页面
            response.sendRedirect(loginUrl+"?sendUrl="+sendUrl);
            return false; 
        }
        //是出于登陆状态
        UserThreadLocal.set(user);//将user对象存入线程容器
        
        return true;
    }

    @Override//目标controller执行后，但在视图返回前，执行此方法
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        

    }

    @Override//目标controller执行后，且视图返回后调用此方法
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {
        
        //在返回视图前，清空线程容器中的对象
        UserThreadLocal.set(null);
    }

}
