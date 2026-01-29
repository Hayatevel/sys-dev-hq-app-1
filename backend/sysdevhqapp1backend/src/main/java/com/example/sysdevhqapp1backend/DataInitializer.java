package com.example.sysdevhqapp1backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.example.sysdevhqapp1backend.challenge.TypingChallenge;
import com.example.sysdevhqapp1backend.challenge.TypingChallengeRepository;
import com.example.sysdevhqapp1backend.user.User;
import com.example.sysdevhqapp1backend.user.UserRepository;

/**
 * データベース初期化クラス
 *
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */
@Component
public class DataInitializer implements CommandLineRunner {

  private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private TypingChallengeRepository challengeRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private com.example.sysdevhqapp1backend.score.ScoreRepository scoreRepository;

  @Override
  public void run(String... args) throws Exception {
    logger.info("データベース初期化を開始します...");

    // 管理者ユーザーが存在しない場合のみ作成
    if (userRepository.findByUsername("admin").isEmpty()) {
      User admin = new User();
      admin.setUsername("admin");
      admin.setPassword(passwordEncoder.encode("admin123"));
      admin.setEmail("admin@example.com");
      admin.setRole(User.UserRole.ADMIN);
      userRepository.save(admin);
      logger.info("管理者ユーザーを作成しました: admin");
    } else {
      logger.info("管理者ユーザーは既に存在します");
    }

    // チャレンジデータが存在しない場合のみ作成
    long challengeCount = challengeRepository.count();
    if (challengeCount == 0) {
      logger.info("サンプルチャレンジを作成します...");
      createSampleChallenges();
      logger.info("サンプルチャレンジを作成しました。総数: {}", challengeRepository.count());
    } else if (challengeCount == 18) {
      // 既存の18件のチャレンジを削除して新しいデータで置き換え（DataInitializer更新時）
      logger.info("既存のチャレンジ({} 件)を削除して新しいデータで置き換えます...", challengeCount);
      // 外部キー制約のため、まずscoresテーブルを削除
      scoreRepository.deleteAll();
      challengeRepository.deleteAll();
      createSampleChallenges();
      logger.info("サンプルチャレンジを更新しました。総数: {}", challengeRepository.count());
    } else {
      logger.info("チャレンジデータは既に存在します。総数: {}", challengeRepository.count());
    }
  }

  private void createSampleChallenges() {
    // Java
    createChallenge("Java",
        "public class Main { public static void main(String[] args) { System.out.println(\"Hello\"); } }",
        93, TypingChallenge.ChallengeDifficulty.EASY);
    createChallenge("Java",
        "public List<String> filterList(List<String> items) { return items.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList()); }",
        137, TypingChallenge.ChallengeDifficulty.MEDIUM);
    createChallenge("Java",
        "private void quickSort(int[] arr, int low, int high) { if (low < high) { int pi = partition(arr, low, high); } }",
        114, TypingChallenge.ChallengeDifficulty.MEDIUM);

    // Python
    createChallenge("Python", "def factorial(n): if n <= 1: return 1 return n * factorial(n - 1)",
        67, TypingChallenge.ChallengeDifficulty.EASY);
    createChallenge("Python",
        "def binary_search(arr, target): left, right = 0, len(arr) - 1 while left <= right: mid = (left + right) // 2 if arr[mid] == target: return mid return -1",
        159, TypingChallenge.ChallengeDifficulty.MEDIUM);
    createChallenge("Python",
        "class TreeNode: def __init__(self, val=0, left=None, right=None): self.val = val self.left = left self.right = right",
        120, TypingChallenge.ChallengeDifficulty.MEDIUM);

    // JavaScript
    createChallenge("JavaScript",
        "const greet = (name) => { console.log(`Hello, ${name}!`); } greet('World');", 77,
        TypingChallenge.ChallengeDifficulty.EASY);
    createChallenge("JavaScript",
        "async function fetchData(url) { const response = await fetch(url); const data = await response.json(); return data; }",
        120, TypingChallenge.ChallengeDifficulty.MEDIUM);
    createChallenge("JavaScript",
        "class Calculator { constructor() { this.result = 0; } add(n) { this.result += n; return this; } }",
        100, TypingChallenge.ChallengeDifficulty.MEDIUM);

    // C++
    createChallenge("C++",
        "#include <iostream> int main() { std::cout << \"Hello, World!\" << std::endl; return 0; }",
        91, TypingChallenge.ChallengeDifficulty.EASY);
    createChallenge("C++", "template<typename T> T max(T a, T b) { return (a > b) ? a : b; }", 65,
        TypingChallenge.ChallengeDifficulty.MEDIUM);
    createChallenge("C++",
        "class Node { public: int data; Node* next; Node(int val) : data(val), next(nullptr) {} };",
        91, TypingChallenge.ChallengeDifficulty.MEDIUM);

    // Go
    createChallenge("Go", "package main import \"fmt\" func main() { fmt.Println(\"Hello, Go!\") }",
        68, TypingChallenge.ChallengeDifficulty.EASY);
    createChallenge("Go",
        "func fibonacci(n int) int { if n <= 1 { return n } return fibonacci(n-1) + fibonacci(n-2) }",
        93, TypingChallenge.ChallengeDifficulty.MEDIUM);
    createChallenge("Go",
        "type Person struct { Name string Age int } func NewPerson(name string, age int) *Person { return &Person{Name: name, Age: age} }",
        132, TypingChallenge.ChallengeDifficulty.MEDIUM);

    // TypeScript
    createChallenge("TypeScript",
        "interface User { name: string; age: number; } const user: User = { name: 'John', age: 30 };",
        93, TypingChallenge.ChallengeDifficulty.EASY);
    createChallenge("TypeScript",
        "function sum<T extends number>(a: T, b: T): T { return (a + b) as T; }", 71,
        TypingChallenge.ChallengeDifficulty.MEDIUM);
    createChallenge("TypeScript",
        "type Result<T> = { success: true; data: T } | { success: false; error: string }; function parse(input: string): Result<number> { const num = parseInt(input); if (isNaN(num)) { return { success: false, error: 'Invalid number' }; } return { success: true, data: num }; }",
        278, TypingChallenge.ChallengeDifficulty.HARD);
  }

  private void createChallenge(String language, String code, int charCount,
      TypingChallenge.ChallengeDifficulty difficulty) {
    TypingChallenge challenge = new TypingChallenge();
    challenge.setLanguage(language);
    challenge.setCodeSnippet(code);
    challenge.setCharacterCount(charCount);
    challenge.setDifficulty(difficulty);
    challengeRepository.save(challenge);
  }
}
