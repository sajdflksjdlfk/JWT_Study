package codehows.dream.newsproject.service;

import codehows.dream.newsproject.constants.Roles;
import codehows.dream.newsproject.dto.JoinDto;
import codehows.dream.newsproject.entitiy.User;
import codehows.dream.newsproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;



    public void joinProcess(JoinDto joinDto) throws IllegalArgumentException{
        String username = joinDto.getUsername();
        String password = joinDto.getPassword();

        Boolean isExist = userRepository.existsByUserName(username);
        System.out.println("isExist = " + isExist);

        if(isExist){
            throw new IllegalArgumentException("중복된 회원입니다");
        }else {

            User data = new User();

            data.setUserName(username);
            data.setPassword(bCryptPasswordEncoder.encode(password));
            data.setRoles(Roles.ROLE_ADMIN);
            userRepository.save(data);
        }
    }



}
