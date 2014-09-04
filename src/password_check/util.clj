(ns password-check.util)

(def not-nil? (comp not nil?))

(defn combine-checkers-or
  "return function which combining checker functions with OR operator"
  [& fns] (fn [s] (not-nil? (some #(% s) fns))))

(defn combine-checkers-and
  "return function whick combining checker functions with AND operator"
  [& fns] (fn [s] (every? #(% s) fns)))

(def combine-checkers combine-checkers-and)

(defn substrings [string amount]
  (let [bound (- (count string) amount)]
    (if (= bound 0)
      [(apply str string)]
      (cons (apply str (take amount string))
            (lazy-seq (substrings (rest string) amount))))))

(defn all-substrings
  ([string]
    (all-substrings string 1))
  ([string start]
    (mapcat (partial substrings string)
            (range start (count string)))))

(defn check [cond? fail-message pw]
  (if (cond? pw)
    {:status :pass}
    {:status :fail :message fail-message}))

(def checker #(partial check %1 %2))