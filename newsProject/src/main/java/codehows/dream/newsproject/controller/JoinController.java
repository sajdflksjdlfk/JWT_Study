package codehows.dream.newsproject.controller;

import codehows.dream.newsproject.dto.JoinDto;
import codehows.dream.newsproject.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    public ResponseEntity<?> joinProcess(JoinDto joinDto){
        try{
        //회원가입 시 id, pw 중복검사
        joinService.joinProcess(joinDto);

        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("회원가입 완료", HttpStatus.OK);
    }
}
