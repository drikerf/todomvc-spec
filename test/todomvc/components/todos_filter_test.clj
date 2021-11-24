(ns todomvc.components.todos-filter-test
  (:require [clojure.test :refer [is deftest]]
            [todomvc.test-utils :refer [speccheck]]
            [todomvc.components.todos-filter :refer [component]]))

(deftest test-todos-filter
  (is (speccheck `component)))
