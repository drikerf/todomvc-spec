(ns todomvc.components.app-test
  (:require [clojure.test :refer [is deftest]]
            [todomvc.test-utils :refer [speccheck]]
            [todomvc.components.app :refer [component]]))

(deftest test-app
  (is (speccheck `component)))
