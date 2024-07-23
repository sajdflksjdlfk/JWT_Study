package codehows.dream.newsproject.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Iterator;

@RestController
public class AdminController {

    @GetMapping("/admin")
    public String adminP() {

        //메인페이지에 id를 띄워줌
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        //role값
        //특정 권한일 경우에만 보낼 수 있는 요청을 제작할 수 있음
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        return "admin Controller"+name+role;
    }
}
