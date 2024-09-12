package dmu.cheek.global.config.security.service;

import dmu.cheek.member.model.Member;
import dmu.cheek.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email).map(this::createUser)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    private User createUser(Member member) {
        List<GrantedAuthority> grantedAuthorities = Collections.singletonList(
                new SimpleGrantedAuthority(ROLE_PREFIX + member.getRole().name())
        );

        return new User(member.getEmail(), null, grantedAuthorities);
    }
}