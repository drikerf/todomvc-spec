(ns todomvc.components.todo-destroy-test
  (:require [clojure.test :refer [is deftest]]
            [todomvc.test-utils :refer [speccheck]]
            [todomvc.components.todo-destroy :refer [component]]))

(deftest test-todo-destroy
  (is (speccheck `component)))
