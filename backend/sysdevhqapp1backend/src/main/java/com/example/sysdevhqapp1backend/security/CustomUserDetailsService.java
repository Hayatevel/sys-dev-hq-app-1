package com.example.sysdevhqapp1backend.security;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.sysdevhqapp1backend.user.User;
import com.example.sysdevhqapp1backend.user.UserRepository;

/**
 * カスタムUserDetailsServiceの実装 Spring Securityの認証に使用
 *
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  /**
   * ユーザー名でユーザー情報を読み込む
   *
   * @param username ユーザー名
   * @return UserDetails
   * @throws UsernameNotFoundException ユーザーが見つからない場合
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));

    return new org.springframework.security.core.userdetails.User(user.getUsername(),
        user.getPassword(),
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
  }
}
