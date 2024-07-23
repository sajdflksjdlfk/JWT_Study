package codehows.dream.newsproject.entitiy;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RefreshEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String user;        //어떤 유저의 토큰인지, 하나의 유저가 여러 토큰을 발급할 수도 있기 때문에 unique설정은 하면 안됨

    private String refresh;     //토큰
    private String expiration;      //만료일

}
