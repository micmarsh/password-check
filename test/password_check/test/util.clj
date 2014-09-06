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

(def long-with-upper? (each-failure has-upper? length?))

(def nil-msg "no nils allowed")
(def str-msg "must be a string")
(def sequential-checks
  (first-failure
   (checker nil-msg not-nil?)
   (checker str-msg  string?)
   has-upper?
   length?))

(deftest checker-combinators
  (let [fail (long-with-upper? "blah")
        {:keys [message status]} fail]
    (is (= status :fail))
    (is (.contains message length-fail))
    (is (.contains message upper-fail)))
  (let [nil-fail (sequential-checks nil)
        str-fail (sequential-checks 5)
        too-lower (sequential-checks "thebestthing10.1")
        too-short (sequential-checks "LOL")
        fails [nil-fail str-fail too-short too-lower]]
    (doseq [fail fails]
      (is (= (:status fail) :fail)))
    (doseq [[message expected] (map vector
                                    (map :message fails)
                                    [nil-msg str-msg length-fail upper-fail])]
      (is (= message expected))))
)
