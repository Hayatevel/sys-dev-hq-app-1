package com.example.sysdevhqapp1backend.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.sysdevhqapp1backend.security.JwtUtil;
import com.example.sysdevhqapp1backend.user.User;
import com.example.sysdevhqapp1backend.user.UserRepository;

/**
 * 認証サービスクラス ユーザーのサインアップとログインを処理
 *
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */
@Service
public class AuthService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserDetailsService userDetailsService;

  /**
   * ユーザーをサインアップ
   *
   * @param request サインアップリクエスト
   * @return 認証レスポンス
   * @throws RuntimeException ユーザー名またはメールアドレスが既に存在する場合
   */
  @Transactional
  public AuthResponse signup(SignupRequest request) {
    if (userRepository.existsByUsername(request.getUsername())) {
      throw new RuntimeException("ユーザー名は既に使用されています");
    }

    if (userRepository.existsByEmail(request.getEmail())) {
      throw new RuntimeException("メールアドレスは既に使用されています");
    }

    User user = new User();
    user.setUsername(request.getUsername());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setEmail(request.getEmail());
    user.setRole(User.UserRole.USER);

    User savedUser = userRepository.save(user);

    UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getUsername());
    String token = jwtUtil.generateToken(userDetails);

    return new AuthResponse(token, savedUser.getUsername(), savedUser.getEmail(),
        savedUser.getRole().name());
  }

  /**
   * ユーザーをログイン
   *
   * @param request ログインリクエスト
   * @return 認証レスポンス
   * @throws RuntimeException 認証に失敗した場合
   */
  public AuthResponse login(LoginRequest request) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

      User user = userRepository.findByUsername(request.getUsername())
          .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));

      UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
      String token = jwtUtil.generateToken(userDetails);

      return new AuthResponse(token, user.getUsername(), user.getEmail(), user.getRole().name());
    } catch (Exception e) {
      throw new RuntimeException("ユーザー名またはパスワードが正しくありません");
    }
  }
}
