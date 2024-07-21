package codehows.dream.newsproject.jwt;

import codehows.dream.newsproject.constants.Roles;
import codehows.dream.newsproject.dto.CustomUserDetails;
import codehows.dream.newsproject.entitiy.User;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

//JWTFilter는 HTTP 요청에서 JWT를 추출하고, JWTUtil 클래스를 사용하여 JWT가 유효한지 확인하며
// 유효한 경우 사용자 정보를 설정하여 스프링 시큐리티 컨텍스트에 인증 정보를 추가합니다.

// -> JWTFilter는 실제 HTTP 요청을 처리할 때 JWTUtil을 사용하여 JWT의 유효성을 확인하고,
// 필요한 사용자 정보를 추출하는 방식으로 동작합니다.
// 따라서 두 클래스는 서로 협력하여 JWT를 기반으로 하는 인증 및 권한 부여 기능을 제공합니다.
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 헤더에서 access키에 담긴 토큰을 꺼냄
        String accessToken = request.getHeader("access");

        // 토큰이 없다면 다음 필터로 넘김
        //권한이 필요 없는 요청도 있기 때문에 이후의 필터로 넘겨주어야 함
        if (accessToken == null) {

            filterChain.doFilter(request, response);

            return;
        }

        //토큰이 있다면 실행되는 로직
        // 토큰 만료 여부 확인, **만료시 다음 필터로 넘기지 않음**
        try {
            jwtUtil.isExpired(accessToken);     //만료되었다면 ExpiredJwtException 발생


        //ExpiredJwtException 처리
        } catch (ExpiredJwtException e) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);        //토큰이 만료되었을 때 넣어준 상태코드
            return;     //만료 시 다음 필터로 넘기면 안됨
        }

    // 토큰이 access인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;     //이 경우도 뒤의 필터로 넘기면 안됨
        }

        //인증완료됨
        // username, role 값을 획득하여 일시적인 세션을 만들어줌
        String username = jwtUtil.getUsername(accessToken);
        Roles role = jwtUtil.getRole(accessToken);

        User user = new User();
        user.setUserName(username);
        user.setRoles(role);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
//        Authentication authToken = new UsernamePasswordAuthenticationToken(null, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }


    //      dofilterInternal 매서드 수정 전
    //http의 헤더에서 jwt를 찾아
    //1. 토큰이 있는지, 2. 만료되지 않았는지, 확인 후
    //토큰에서 username과  role값을 얻어
    //request에서 Authorization 헤더를 찾음

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        String authorization = request.getHeader("Authorization");
//
//
//        //Authorization 헤더 검증
//        if (authorization == null || !authorization.startsWith("Bearer ")) {
//
//            System.out.println("token null");
//            filterChain.doFilter(request, response);
//
//            //조건이 해당되면 메소드 종료 (필수)
//            //return이 되면 다음 필터인 Login필터로 넘어감
//            return;
//        }
//
//        System.out.println("authorization now");
//        //Bearer 부분 제거 후 순수 토큰만 획득
//        String token = authorization.split(" ")[1];
//
//        //토큰 소멸 시간 검증, 토큰이 소멸되었으면 리턴
//        if (jwtUtil.isExpired(token)) {
//
//            System.out.println("token expired");
//            filterChain.doFilter(request, response);
//
//            //조건이 해당되면 메소드 종료 (필수)
//            return;
//
//        }
//
//
//        //토큰에서 username과 role 획득
//        String username = jwtUtil.getUsername(token);
//        Roles role = jwtUtil.getRole(token);
//
//
//        //user를 생성하여 값 set
//        User user = new User();
//        user.setUserName(username);
//        user.setPassword("temppassword");
//        user.setRoles(role);
//
//        //UserDetails에 회원 정보 객체 담기
//        CustomUserDetails customUserDetails = new CustomUserDetails(user);
//
//        //스프링 시큐리티 인증 토큰 생성
//        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
//        //세션에 사용자 등록
//        SecurityContextHolder.getContext().setAuthentication(authToken);
//
//        filterChain.doFilter(request, response);
//
//
//    }


}
