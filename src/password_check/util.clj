(ns password-check.util)

(def not-nil? (comp not nil?))

(defn combine-checkers-or
  "return function which combining checker functions with OR operator"
  [& fns] (fn [s] (not-nil? (some #(% s) fns))))
(defn combine-checkers-and
  "return function whick combining checker functions with AND operator"
  [& fns] (fn [s] (every? #(% s) fns)))
(def ^{:doc "same as checker-combine-and" :arglists '([& fns])}
  combine-checkers combine-checkers-and)