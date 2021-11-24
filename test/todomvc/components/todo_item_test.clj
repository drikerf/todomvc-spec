(ns todomvc.components.todo-item-test
  (:require [clojure.test :refer [is deftest]]
            [todomvc.test-utils :refer [speccheck]]
            [todomvc.components.todo-item :refer [component]]))

(deftest test-todo-item
  (is (speccheck `component)))
