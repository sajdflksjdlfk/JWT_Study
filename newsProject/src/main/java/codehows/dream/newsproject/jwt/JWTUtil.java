package codehows.dream.newsproject.jwt;

import codehows.dream.newsproject.constants.Roles;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

//JWTUtil은 JWT를 생성하고, JWT에서 정보를 추출하며, JWT가 유효한지 확인하는 메서드를 제공합니다.
@Component
public class JWTUtil {

    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret){
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    //1.username을 검증하는 매서드 - jwt에서 username을 추출하는 매서드
    public String getUsername(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }
    //2.roll 검증하는 매서드
    public Roles getRole(String token) {

        String role = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
        return Roles.valueOf(role.toUpperCase()); // 문자열을 Role enum 타입으로 변환
    }

    //3.토큰의 만료 상태롤 확인하는 매서드
    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    //4. 카테고리(refresh, access)를 확인하는 매서드
    public String getCategory(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }



    //토큰을 생성하는 매서드
    //로그인이 성공했을 때, successfulHandler를 통해 username,role, expired를 전달받아 토큰을 생성해주는 토큰 생성 매서드
    public String createJwt(String category, String username, String role, Long expiredMs) {

        return Jwts.builder()
                .claim("category", category)
                .claim("username", username)      //claim에 키,값을 넣어줄 수 있음, 토큰의 페이로드에 해당하는 부분
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))       //토큰 발행 시간(현재시간)
                .expiration(new Date(System.currentTimeMillis() + expiredMs))  //토큰 소멸 시간= 토큰발행시간 + 토큰의 유효 시간
                .signWith(secretKey)  //secretKey를 통해 암호화 진행
                .compact();
    }


}
