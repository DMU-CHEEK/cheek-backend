package dmu.cheek.global.config.security.service;

import dmu.cheek.member.constant.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Slf4j
public class MemberDetails implements UserDetails {

    private final String email;
    private final Collection<Role> roles;

    public MemberDetails(String email, Collection<Role> roles) {
        this.email = email;
        this.roles = roles;
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return roles.stream()
//                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
//                .toList();
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<? extends GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .toList();
        log.info("Granted Authorities: {}", authorities);
        return authorities;
    }


    @Override
    public String getPassword() {
        return null;  //비밀번호 없음
    }

    @Override
    public String getUsername() {
        return email;  //이메일을 사용자 이름으로 반환
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
