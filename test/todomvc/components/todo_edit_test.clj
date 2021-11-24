(ns todomvc.components.todo-edit-test
  (:require [clojure.test :refer [is deftest]]
            [todomvc.test-utils :refer [speccheck]]
            [todomvc.components.todo-edit :refer [component]]))

(deftest test-todo-edit
  (is (speccheck `component)))
