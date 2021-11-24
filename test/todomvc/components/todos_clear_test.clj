(ns todomvc.components.todos-clear-test
  (:require [clojure.test :refer [is deftest]]
            [todomvc.test-utils :refer :all]
            [todomvc.components.todos-clear :refer :all]))

(deftest test-todos-clear
  (is (speccheck `component)))
