(ns todomvc.components.todo-input-test
  (:require [clojure.test :refer [is deftest]]
            [todomvc.test-utils :refer [speccheck]]
            [todomvc.components.todo-input :refer [component]]))

(deftest test-todo-input
  (is (speccheck `component)))
