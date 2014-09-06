(ns password-check.test.util
  (:use password-check.util
        password-check.core
        clojure.test)
  (:require [password-check.test :refer (equal-pairs)]))

(deftest check-function
  (equal-pairs
   {:status :pass} (check "Needs to be a number" number?  8)
   "failure" (:message (check "failure"  nil? 5))))
