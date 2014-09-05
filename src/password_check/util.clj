(ns password-check.util)

(def not-nil? (comp not nil?))

(defn- return-failed [checker]
  (let [result (checker password)]
    (when (= :fail (:status result))
      result)))

(defn first-checker
  [& checkers]
  (fn [password]
    (some return-failed checkers)))

(defn- combine-messages [failures]
  {:status :fail
   :message
   (->> failures
        (map :message)
        (interpose \newline)
        (apply str))})

(defn each-checker
  [& checkers]
  (fn [password]
    (->> checkers
         (map return-failed)
         (remove nil?)
         (combine-messages))))

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
