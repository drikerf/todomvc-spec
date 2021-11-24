(ns todomvc.components.footer-test
  (:require [clojure.test :refer [is deftest]]
            [todomvc.test-utils :refer [speccheck]]
            [todomvc.components.footer :refer [component]]))

(deftest test-footer (is (speccheck `component)))
