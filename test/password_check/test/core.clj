(ns password-check.test.core
  (:use password-check.core
        password-check.util
        clojure.test))

(defmacro equal-pairs [& body]
  `(are [x y] (= x y)
    ~@body))

(deftest test-contains-uppercase?
  (equal-pairs
    true  (contains-uppercase? "A")
    false (contains-uppercase? "a")
    false (contains-uppercase? "0")
    false (contains-uppercase? "!")
    false (contains-uppercase? "")))

(deftest test-contains-lowercase?
  (equal-pairs
    false (contains-lowercase? "A")
    true  (contains-lowercase? "a")
    false (contains-lowercase? "0")
    false (contains-lowercase? "!")
    false (contains-lowercase? "")))

(deftest test-contains-alphabet?
  (equal-pairs
    true  (contains-alphabet? "A")
    true  (contains-alphabet? "a")
    false (contains-alphabet? "0")
    false (contains-alphabet? "!")
    false (contains-alphabet? "")))

(deftest test-contains-number?
  (equal-pairs
    false (contains-number? "A")
    false (contains-number? "a")
    true  (contains-number? "0")
    false (contains-number? "!")
    false (contains-number? "")))

(deftest test-length-range
  (let [f (length-range 3 4)
        g (length-range 3)]
    (equal-pairs
      false (f "")
      false (f "aa")
      true  (f "aaa")
      true  (f "aaaa")
      false (f "aaaaa")
      false (g "")
      false (g "aa")
      true  (g "aaa")
      true  (g "aaaa")
      true  (g (apply str (take 10000 (repeat "a")))))))

(deftest test-not-same-characters?
  (is (not-same-characters? "aaaaAaaa"))
  (is (not (not-same-characters? "aaaaaaaa")))
  (is (not (not-same-characters? ""))))

(deftest test-not-sequential-password?
  (equal-pairs
    true  (not-sequential-password? "hello")
    true  (not-sequential-password? "1253")
    false (not-sequential-password? "abcdefg")
    false (not-sequential-password? "gfedcba")
    false (not-sequential-password? "0123456")
    false (not-sequential-password? "6543210")
    true  (not-sequential-password? "0123457")
    true  (not-sequential-password? "")))

(deftest test-contains-symbol?
  (equal-pairs
    false (contains-symbol? "A")
    false (contains-symbol? "a")
    false (contains-symbol? "0")
    true  (contains-symbol? "!")
    false (contains-symbol? "")))

(deftest test-not-contains-multi-byte-character?
  (equal-pairs
    true  (not-contains-multi-byte-character? "hello")
    true  (not-contains-multi-byte-character? "!@#")
    false (not-contains-multi-byte-character? "はろー")
    true  (not-contains-multi-byte-character? "123")
    true  (not-contains-multi-byte-character? "")))

(deftest test-not-contains-sequence?
  (equal-pairs
    true (not-contains-sequence? "12458")
    false (not-contains-sequence? "hello123")
    false (not-contains-sequence? "12abc3")
    false (not-contains-sequence? "heyyouabcdefgh")))

(deftest test-not-contains-repeats?
  (equal-pairs
   true (not-contains-repeats? "whatsup")
   false (not-contains-repeats? "yooosup")
   true (not-contains-repeats? "greetings")
   false (not-contains-repeats? "thisismypassssword")))
