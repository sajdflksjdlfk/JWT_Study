package codehows.dream.newsproject.service;

import codehows.dream.newsproject.dto.CustomUserDetails;
import codehows.dream.newsproject.entitiy.User;
import codehows.dream.newsproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //db에서 조회
        User userData = userRepository.findByUserName(username);

        if(userData != null){
            //userDetails에 담아서 return하면 AutneticationManager가 검증함.
            //userData에는 현재 로그인을 시도한 사용자의 정보가 들어있음
            return new CustomUserDetails(userData);
        }
        // 유저를 찾을 수 없으면 UsernameNotFoundException을 던짐
        //로그인을 실패하면 loginFilter의 unsuccessfulAuthentication매서드 실행됨.
        //에러 메시지를 따로 확인할 수는 없음
        throw new UsernameNotFoundException("사용자를 찾을 수 없습니다");
//        return null;
        //null을 리턴하더라도 매서드를 호출한 곳에서 null값을 처리할 것임.
        //여기서 어차피 UsernameNotFoundException예외를 던질 것이기 때문에 결국은 같은 과정임
    }
}
