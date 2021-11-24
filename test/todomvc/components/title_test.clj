(ns todomvc.components.title-test
  (:require [clojure.test :refer [is deftest]]
            [todomvc.test-utils :refer [speccheck]]
            [todomvc.components.title :refer [component]]))

(deftest test-title
  (is (speccheck `component)))
