(ns todomvc.components.todos-count-test
  (:require [clojure.test :refer [is deftest]]
            [todomvc.test-utils :refer [speccheck]]
            [todomvc.components.todos-count :refer [component]]))

(deftest test-todos-count
  (is (speccheck `component)))
