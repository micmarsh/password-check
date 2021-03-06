(ns password-check.util)

(def not-nil? (comp not nil?))

(defn check [fail-message cond? pw]
  (if (cond? pw)
    {:status :pass}
    {:status :fail :message fail-message}))

(def checker #(partial check %1 %2))

(defn- return-failed [password checker]
  (let [result (checker password)]
    (when (= :fail (:status result))
      result)))

(defn first-failure
  [& checkers]
  (fn [password]
    (some (partial return-failed password) checkers)))

(defn- combine-messages [failures]
  (if (empty? failures)
      {:status :pass}
      {:status :fail
       :message
       (->> failures
            (map :message)
            (interpose \newline)
            (apply str))}))

(defn each-failure
  [& checkers]
  (fn [password]
    (->> checkers
         (map (partial return-failed password))
         (remove nil?)
         (combine-messages))))

(defn substrings [string amount]
  (let [bound (- (count string) amount)]
    (if (zero? bound)
      [(apply str string)]
      (cons (apply str (take amount string))
            (lazy-seq (substrings (rest string) amount))))))

(defn all-substrings
  ([string]
    (all-substrings string 1))
  ([string start]
    (mapcat (partial substrings string)
            (range start (count string)))))

