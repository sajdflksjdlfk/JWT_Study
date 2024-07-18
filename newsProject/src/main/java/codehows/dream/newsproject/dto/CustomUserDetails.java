package codehows.dream.newsproject.dto;

import codehows.dream.newsproject.constants.Roles;
import codehows.dream.newsproject.entitiy.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

//데이터를 넘겨주는 dto역할을 하는 클래스
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    //role 값을 반환하는 매서드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {
                Roles role = user.getRoles();

                return role.toString();
            }
        });

        return collection;
    }

    //pw값을 반환하는 매서드    
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    //id값을 반환하는 매서드
    @Override
    public String getUsername() {
        return user.getUserName();
    }

    //계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {return true;}

    //lock되었는지?
    @Override
    public boolean isAccountNonLocked() {return true;}

    
    @Override
    public boolean isCredentialsNonExpired() {return true;}

    @Override
    public boolean isEnabled() {return true;}
}
