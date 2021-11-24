(ns todomvc.components.todo-toggle
  (:require [rum.core :as rum]
            [clojure.spec.alpha :as s]
            [todomvc.spec :as spec]
            [todomvc.events :refer [dispatch]]
            [todomvc.env :refer [disable-pbt?]]
            [todomvc.query :as query]))

(s/fdef component
  :args (s/cat :state ::spec/state :todo ::spec/todo)
  :ret ::spec/view
  :fn #(or @disable-pbt?
           (query/view-has? % :input.toggle
                            {:on-change ::spec/event-handler
                             :checked (-> % :args :todo :completed?)})))

(rum/defc component [_ {:keys [id completed?]}]
  [:input.toggle
   {:type "checkbox"
    :checked completed?
    :on-change #(dispatch :toggle-todo id)}])
