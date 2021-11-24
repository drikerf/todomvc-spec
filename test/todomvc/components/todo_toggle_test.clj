(ns todomvc.components.todo-toggle-test
  (:require [clojure.test :refer [is deftest]]
            [todomvc.test-utils :refer :all]
            [todomvc.components.todo-toggle :refer :all]))

(deftest test-todo-toggle
  (is (speccheck `component)))
