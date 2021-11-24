(ns todomvc.components.todos-list
  (:require [rum.core :as rum]
            [clojure.spec.alpha :as s]
            [todomvc.spec :as spec]
            [todomvc.components.todo-item :as todo-item]
            [todomvc.env :refer [disable-pbt?]]
            [todomvc.query :as query]
            [todomvc.core :as core]))

(s/fdef component
  :args (s/cat :state ::spec/state)
  :ret ::spec/view
  :fn #(or @disable-pbt?
           (and
            (query/view-has? % :ul.todo-list)
            (query/view-has-n-of?
             (let [{:keys [todos] {:keys [status]} :filter}
                   (-> % :args :state)]
               (case status
                 "all" (count todos)
                 "active" (->> todos (filter (complement :completed?)) count)
                 "completed" (->> todos (filter :completed?) count)))
             % :.view))))

(rum/defc component [state]
  [:ul.todo-list
   (->> (core/visible-todos state)
        (map
         #(rum/with-key (todo-item/component state %) (:id %))))])
