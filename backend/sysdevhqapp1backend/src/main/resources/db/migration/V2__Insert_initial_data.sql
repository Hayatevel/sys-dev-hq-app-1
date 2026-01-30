-- ===================================================================
-- V2: サンプルデータ投入
-- ===================================================================
-- サンプルタイピングチャレンジの挿入
INSERT INTO
  typing_challenges (
    language,
    code_snippet,
    character_count,
    difficulty
  )
VALUES
  -- Java
  (
    'Java',
    'public class Main { public static void main(String[] args) { System.out.println("Hello"); } }',
    93,
    'EASY'
  ),
  (
    'Java',
    'public List<String> filterList(List<String> items) { return items.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList()); }',
    137,
    'MEDIUM'
  ),
  (
    'Java',
    'private void quickSort(int[] arr, int low, int high) { if (low < high) { int pi = partition(arr, low, high); } }',
    114,
    'MEDIUM'
  ),
  -- Python
  (
    'Python',
    'def factorial(n): if n <= 1: return 1 return n * factorial(n - 1)',
    67,
    'EASY'
  ),
  (
    'Python',
    'def binary_search(arr, target): left, right = 0, len(arr) - 1 while left <= right: mid = (left + right) // 2 if arr[mid] == target: return mid return -1',
    159,
    'MEDIUM'
  ),
  (
    'Python',
    'class TreeNode: def __init__(self, val=0, left=None, right=None): self.val = val self.left = left self.right = right',
    120,
    'MEDIUM'
  ),
  -- JavaScript
  (
    'JavaScript',
    'const greet = (name) => { console.log("Hello, " + name + "!"); } greet("World");',
    80,
    'EASY'
  ),
  (
    'JavaScript',
    'async function fetchData(url) { const response = await fetch(url); const data = await response.json(); return data; }',
    120,
    'MEDIUM'
  ),
  (
    'JavaScript',
    'class Calculator { constructor() { this.result = 0; } add(n) { this.result += n; return this; } }',
    100,
    'MEDIUM'
  ),
  -- C++
  (
    'C++',
    '#include <iostream> int main() { std::cout << "Hello, World!" << std::endl; return 0; }',
    91,
    'EASY'
  ),
  (
    'C++',
    'template<typename T> T max(T a, T b) { return (a > b) ? a : b; }',
    65,
    'MEDIUM'
  ),
  (
    'C++',
    'class Node { public: int data; Node* next; Node(int val) : data(val), next(nullptr) {} };',
    91,
    'MEDIUM'
  ),
  -- Go
  (
    'Go',
    'package main import "fmt" func main() { fmt.Println("Hello, Go!") }',
    68,
    'EASY'
  ),
  (
    'Go',
    'func fibonacci(n int) int { if n <= 1 { return n } return fibonacci(n-1) + fibonacci(n-2) }',
    93,
    'MEDIUM'
  ),
  (
    'Go',
    'type Person struct { Name string Age int } func NewPerson(name string, age int) *Person { return &Person{Name: name, Age: age} }',
    132,
    'MEDIUM'
  ),
  -- TypeScript
  (
    'TypeScript',
    'interface User { name: string; age: number; } const user: User = { name: "John", age: 30 };',
    91,
    'EASY'
  ),
  (
    'TypeScript',
    'function sum<T extends number>(a: T, b: T): T { return (a + b) as T; }',
    71,
    'MEDIUM'
  ),
  (
    'TypeScript',
    'type Result<T> = { success: true; data: T } | { success: false; error: string }; function parse(input: string): Result<number> { const num = parseInt(input); if (isNaN(num)) { return { success: false, error: "Invalid number" }; } return { success: true, data: num }; }',
    276,
    'HARD'
  );

-- 管理者ユーザーの挿入
-- パスワード: admin (BCrypt ハッシュ化済み)
INSERT INTO users (username, password, email, role, created_at, updated_at)
VALUES ('admin', '$2a$10$k5BJbqb754wb8m9Nm2dc/.Gx7H7xu48I8Ly3XzUU2ISJqJpccc8km', 'admin@example.com', 'ADMIN', NOW(), NOW());
