(ns todomvc.components.todo-destroy
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
           (query/view-has? % :button.destroy
                            {:on-click ::spec/event-handler})))

(rum/defc component [_ {:keys [id]}]
  [:button.destroy
   {:on-click #(dispatch :remove-todo! id)}])

