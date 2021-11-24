(ns todomvc.components.info-test
  (:require [clojure.test :refer [is deftest]]
            [todomvc.test-utils :refer [speccheck]]
            [todomvc.components.info :refer [component]]))

(deftest test-info
  (is (speccheck `component)))
