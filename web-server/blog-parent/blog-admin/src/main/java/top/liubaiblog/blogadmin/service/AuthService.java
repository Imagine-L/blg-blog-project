package top.liubaiblog.blogadmin.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 留白
 * @description 认证相关服务
 */
public interface AuthService {

    boolean auth(HttpServletRequest request, Authentication authentication);

}
