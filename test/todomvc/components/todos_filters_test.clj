(ns todomvc.components.todos-filters-test
  (:require [clojure.test :refer [is deftest]]
            [todomvc.test-utils :refer [speccheck]]
            [todomvc.components.todos-filters :refer [component]]))

(deftest test-todos-filters
  (is (speccheck `component)))
