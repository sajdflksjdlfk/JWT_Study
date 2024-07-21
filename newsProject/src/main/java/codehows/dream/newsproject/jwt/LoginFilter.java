package codehows.dream.newsproject.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
      
        //jwtfilter에서 등록한 authentication객체가 있는지 확인.
        //login경로로 요청된 post라면 작동하는 클래스이기 때문에, jwtfilter 에서 인증을 거쳤다면 로그인 과정을 거칠 필요가 없는 것임
        //댓글 참조
        if (SecurityContextHolder.getContext().getAuthentication() == null) {

            System.out.println("LoginFilter.attemptAuthentication111");
            //obtain00매서드는  UsernamePasswordAuthenticationFilter에 정의되어 있음
            //각 매서드에 request를 넣으면 username과 password를 추출할 수 있음
            String username = obtainUsername(request);
            String password = obtainPassword(request);


            System.out.println("username" + username + ", password = " + password);

            //스프링 시큐리티에서 username과 password를 검증하기 위해서 token에 담아야 함
            //id,pw,role을 매개변수로 갖는 생성자
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);


            return authenticationManager.authenticate(authToken);
        }
        System.out.println("LoginFilter.attemptAuthentication22");
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        //유저 정보
        String username = authentication.getName();     //username값

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();              //role값

        //토큰 생성
        String access = jwtUtil.createJwt("access", username, role, 600000L);
        String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        //응답 설정
        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println("로그인 실패");


        //로그인 실패시 401 응답 코드 반환
        response.setStatus(401);
    }

    //키(refresh) 값(refresh token)을 인자로 받음
    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);     //쿠키의 생명 주기
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);   //클라이언트단에서 js로 쿠키에 접근하지 못하게 함

        return cookie;
    }
}
