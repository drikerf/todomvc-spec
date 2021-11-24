(ns todomvc.components.todos-list-test
  (:require [clojure.test :refer [is deftest]]
            [todomvc.test-utils :refer [speccheck]]
            [todomvc.components.todos-list :refer [component]]))

(deftest test-todos-list
  (is (speccheck `component)))
