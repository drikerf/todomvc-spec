(ns todomvc.components.todos-clear
  (:require [rum.core :as rum]
            [clojure.spec.alpha :as s]
            [todomvc.core :as core]
            [todomvc.spec :as spec]
            [todomvc.events :refer [dispatch]]
            [todomvc.env :refer [disable-pbt?]]
            [todomvc.query :as query]))

(s/fdef component
  :args (s/cat :state ::spec/state)
  :ret ::spec/view
  :fn #(or @disable-pbt?
           (if (-> % :args :state core/completed-todos seq)
             (query/view-has? % :button.clear-completed
                              {:on-click ::spec/event-handler})
             (and (not (query/view-has? % :button.clear-completed))
                  (query/view-has? % :rum/nothing)))))

(rum/defc component [state]
  (when (seq (core/completed-todos state))
    [:button.clear-completed
     {:on-click #(dispatch :clear-completed!)}
     "Clear completed"]))
