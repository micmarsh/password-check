(ns password-check.core
  (:require [clojure.contrib.string :as string]
            [password-check.util :refer (combine-checkers-or not-nil?)]))

(def ^{:doc "return last checker function name"} last-checker (ref nil))

(defn- in-range? [n min max] (and (>= n min) (< n max)))
(defn- re-contains? [re s] (not-nil? (re-find re s)))

; blank checker
(def not-blank? (comp not string/blank?))

; alphabet checker
(def contains-uppercase? (partial re-contains? #"[A-Z]"))

(def contains-lowercase? (partial re-contains? #"[a-z]"))

(def contains-alphabet? (combine-checkers-or contains-lowercase? contains-uppercase?))

; number checker
(def contains-number? (partial re-contains? #"[0-9]"))

; symbol checker
(defn contains-symbol?
  "return boolean whether password contain symbols or not"
  [password]
  (not-nil?
    (some #(let [in? (partial in-range? (int %))]
             (or (in? 33 48) (in? 58 65) (in? 91 96) (in? 123 127)))
          password)))

; character checker
(defn not-same-characters?
  "return boolean whether password characters are same or not
  ex) (not-same-characters? \"aaaaa\")
      ; false"
  [password]
  (if-let [c (first password)]
    (not-nil? (some #(not= % c) password))
    false))

(defn not-sequential-password?
  "return boolean whether password is not sequencial or not
  ex) (not-sequential-password? \"abcdefg\")
      ; false (this is sequencial password)"
  [password]
  (let [l (map #(apply - %) (partition 2 1 (map int password)))]
    (if (empty? l) true
      (not (or (every? #(= -1 %) l) (every? #(= 1 %) l))))))

; multi byte character checker
; cf. http://www.alqmst.co.jp/tech/040601.html
(defn not-contains-multi-byte-character?
  "return boolean whether password do not contain multi byte characters or not"
  [password]
  (every? #(let [i (int %)]
             (or (<= i 126) (= i 165) (= i 8254) ;\u007e, \u00a5, \u203e
                      (in-range? i 65377 65440) ;\uff61 - \uff9f
                      ))
          password))

; length checker
(defn length-range
  "return function which return boolean whether password length is in specified range or not
  ex) (length-range 3 5)
      ; 3 <= length <= 5
      (length-range 3)
      ; 3 <= length"
  ([min-len] (length-range min-len nil))
  ([min-len max-len]
     (fn [password]
        (let [length (count password)]
          (cond
            (and max-len min-len)
              (in-range? length min-len (inc max-len))
            min-len
              (>= length min-len))))))

