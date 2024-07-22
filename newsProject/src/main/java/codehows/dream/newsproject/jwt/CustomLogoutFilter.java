package codehows.dream.newsproject.jwt;

import codehows.dream.newsproject.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }
    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        //path and method verify
        //로그아웃인지 확인하는 매서드
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {

            filterChain.doFilter(request, response);            //로그아웃 경로가 아니라면 다음 필터로 넘김
                                                                // (로그아웃이 아닌데 로그아웃을 수행할 필요는 없으니)
            return;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {                    //로그아웃이더라도 POST요청이 아닌 경우

            filterChain.doFilter(request, response);
            return;
        }



        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();        //쿠키를 모두 불러와 refresh토큰이 있는지 확인하는 매서드
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }


        //refresh토큰이 없는 경우
        //refresh null check
        if (refresh == null) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //refresh토큰이 있다면, 만료 여부 확인
        //expired check(만료 되었는지 확인)
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //refresh에 담겨온 토큰이 실제 refresh토큰이 맞는지 확인하는 매서드
        // (refresh키에 해커가 다른 토큰을 집어넣어 해킹 시도를 할 수도 있음)
        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {

            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {

            //db에 해당하는 refresh토큰이 없다면 로그아웃이 된 상태로 간주할 수 있음
            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);     //이미 로그아웃이 되었다는 응답으로 바꾸어도 됨
            return;
        }

        //로그아웃 진행
        //Refresh 토큰 DB에서 제거
        refreshRepository.deleteByRefresh(refresh);

        //Refresh 토큰 Cookie 값 0
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}