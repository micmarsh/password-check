(ns password-check.test
  (:require [clojure.test :refer (are)]))

(defmacro equal-pairs [& body]
  `(are [x y] (= x y)
    ~@body))
