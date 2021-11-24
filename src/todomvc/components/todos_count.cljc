(ns todomvc.components.todos-count
  (:require [rum.core :as rum]
            [todomvc.spec :as spec]
            [clojure.spec.alpha :as s]
            [todomvc.env :refer [disable-pbt?]]
            [todomvc.query :as query]
            [todomvc.core :as core]))

(s/fdef component
  :args (s/cat :state ::spec/state)
  :ret ::spec/view
  :fn #(or @disable-pbt?
           (query/view-has? % :strong
                            {:content (-> % :args :state core/visible-todos count)})))

(rum/defc component [state]
  (let [todo-count (count (core/visible-todos state))]
    [:span.todo-count
     [:strong todo-count]
     [:span
      (if (or (< 1 todo-count) (= 0 todo-count))
        " items left" " item left")]]))
