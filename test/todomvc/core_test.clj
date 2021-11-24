(ns todomvc.core-test
  (:require [clojure.test :refer [is deftest]]
            [todomvc.test-utils :refer [speccheck]]
            [todomvc.core :refer :all]))

(deftest test-next-id (is (speccheck `next-id)))

(deftest test-update-input (is (speccheck `update-input)))

(deftest test-create-todo! (is (speccheck `create-todo!)))

(deftest test-remove-todo! (is (speccheck `remove-todo!)))

(deftest test-edit-todo (is (speccheck `edit-todo)))

(deftest test-save-todo (is (speccheck `save-todo)))

(deftest test-update-todo (is (speccheck `update-todo)))

(deftest test-toggle-todo (is (speccheck `toggle-todo)))

(deftest test-toggle-all (is (speccheck `toggle-all)))

(deftest test-set-filter (is (speccheck `set-filter)))

(deftest test-clear-completed! (is (speccheck `clear-completed!)))

(deftest test-completed-todos (is (speccheck `completed-todos)))

(deftest test-visible-todos (is (speccheck `visible-todos)))
