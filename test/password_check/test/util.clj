(ns password-check.test.util
  (:use password-check.util
        password-check.core
        clojure.test)
  (:require [password-check.test :refer (equal-pairs)]))

(deftest check-function
  (equal-pairs
   {:status :pass} (check "Needs to be a number" number?  8)
   "failure" (:message (check "failure"  nil? 5))
   "nope" (:message (check "nope" (partial some #{\s}) "foo"))))

(def upper-fail "Needs to include at least one uppercase letter")
(def has-upper? (checker upper-fail contains-uppercase?))

(def length-fail "Needs to be at least 10 characters")
(def length? (checker length-fail (length-range 10)))

(deftest make-checkers
  (equal-pairs
   {:status :pass} (has-upper? "aaaaAaaa")
   upper-fail (:message (has-upper? "fooooo"))
   length-fail (:message (length? "too short"))))

(deftest checker-combinators
  )
