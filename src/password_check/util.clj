(ns password-check.util)

(def not-nil? (comp not nil?))

(defn combine-checkers-or
  "return function which combining checker functions with OR operator"
  [& fns] (fn [s] (not-nil? (some #(% s) fns))))

(defn combine-checkers-and
  "return function whick combining checker functions with AND operator"
  [& fns] (fn [s] (every? #(% s) fns)))

(def combine-checkers combine-checkers-and)

(defn check [cond? fail-message pw]
  (if (cond? pw)
    {:status :pass}
    {:status :fail :message fail-message}))

(def checker #(partial check %1 %2))