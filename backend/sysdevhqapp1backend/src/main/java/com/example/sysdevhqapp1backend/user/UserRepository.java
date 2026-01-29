package com.example.sysdevhqapp1backend.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ユーザーリポジトリインターフェース
 *
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * ユーザー名でユーザーを検索
   *
   * @param username ユーザー名
   * @return ユーザー情報（存在する場合）
   */
  Optional<User> findByUsername(String username);

  /**
   * メールアドレスでユーザーを検索
   *
   * @param email メールアドレス
   * @return ユーザー情報（存在する場合）
   */
  Optional<User> findByEmail(String email);

  /**
   * ユーザー名の存在確認
   *
   * @param username ユーザー名
   * @return 存在する場合true
   */
  boolean existsByUsername(String username);

  /**
   * メールアドレスの存在確認
   *
   * @param email メールアドレス
   * @return 存在する場合true
   */
  boolean existsByEmail(String email);
}
