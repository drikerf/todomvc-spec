(ns todomvc.components.todo-input
  (:require [rum.core :as rum]
            [clojure.spec.alpha :as s]
            [todomvc.spec :as spec]
            [todomvc.events :refer [dispatch]]
            [todomvc.env :refer [disable-pbt?]]
            [todomvc.query :as query]))

(def ENTER 13)

(s/fdef component
  :args (s/cat :state ::spec/state)
  :ret ::spec/view
  :fn #(or @disable-pbt?
           (query/view-has? % :input.new-todo
                            {:on-change ::spec/event-handler
                             :on-key-up ::spec/event-handler
                             :auto-focus true
                             :value (-> % :args :state :input :value)})))

(rum/defc component [{:keys [input]}]
  [:input.new-todo
   {:type "text"
    :auto-focus true
    :placeholder "What needs to be done?"
    :value (:value input)
    :on-change #(dispatch :update-input (-> % .-target .-value))
    :on-key-up #(when (= (-> % .-keyCode) ENTER)
                  (dispatch :create-todo!))}])
