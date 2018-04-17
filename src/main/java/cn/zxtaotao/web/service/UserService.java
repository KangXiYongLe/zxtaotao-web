package cn.zxtaotao.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.zxtaotao.sso.query.api.UserQueryService;
import cn.zxtaotao.sso.query.bean.User;

@Service
public class UserService {
    
    @Value("${TAOTAO_SSO_URL}")
    public String TAOTAO_SSO_URL;
    
    @Autowired
    private UserQueryService userQueryService;
    
    public User queryByToken(String token){      
        try {
           return this.userQueryService.queryUserByToken(token);
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return null;        
    }    
}
