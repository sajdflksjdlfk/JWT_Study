package codehows.dream.newsproject.jwt;



import codehows.dream.newsproject.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
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

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        System.out.println("로그인 성공");

        //현재 로그인한 유저의 정보를 가져와 userDetails객체에 담음
        //일반적으로 getPrincipal()은 object 타입을 반환함. 따라서 CustomUserDetails객체 타입의 객체에 담기 위해서는 타입변환이 필요.
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        //username 뽑아내기
        String username = customUserDetails.getUsername();

        //role 뽑아내기
        //role 값이 여러개일 경우가 있어 role은 collection타입으로 관리됨
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();  //최종적인 role 값

        String token = jwtUtil.createJwt(username, role, 60*60*10L);

        response.addHeader("Authorization", "Bearer " + token);




    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println("로그인 실패");


        //로그인 실패시 401 응답 코드 반환
        response.setStatus(401);
    }
}
