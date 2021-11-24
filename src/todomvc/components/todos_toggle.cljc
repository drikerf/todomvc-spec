(ns todomvc.components.todos-toggle
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
           (and
            (query/view-has? % :input.toggle-all#toggle-all
             {:on-change ::spec/event-handler
              :checked (-> % :args :state core/completed-todos count
                           (= (-> % :args :state :todos count)))})
            (query/view-has? % :label))))

(rum/defc component [state]
  [:span
   [:input.toggle-all#toggle-all
    {:type "checkbox"
     :on-change #(dispatch :toggle-all)
     :checked (= (count (core/completed-todos state))
                 (count (:todos state)))}]
   [:label
    {:for "toggle-all"}]])
