(ns todomvc.components.todos-toggle-test
  (:require [clojure.test :refer [is deftest]]
            [todomvc.test-utils :refer [speccheck]]
            [todomvc.components.todos-toggle :refer [component]]))

(deftest test-todos-toggle
  (is (speccheck `component)))
