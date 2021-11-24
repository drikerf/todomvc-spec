(ns todomvc.components.todo-edit
  (:require [rum.core :as rum]
            [clojure.spec.alpha :as s]
            [todomvc.spec :as spec]
            [todomvc.events :refer [dispatch]]
            [todomvc.env :refer [disable-pbt?]]
            [todomvc.query :as query]))

(def ENTER 13)

(s/fdef component
  :args (s/cat :state ::spec/state :todo ::spec/todo)
  :ret ::spec/view
  :fn #(or @disable-pbt?
           (query/view-has? % :input.edit#todo-edit
                            {:on-change ::spec/event-handler
                             :on-key-up ::spec/event-handler
                             :ref ::spec/ref
                             :on-blur ::spec/event-handler
                             :value (-> % :args :todo :title)})))

(rum/defc component [_ {:keys [title editing?]}]
  (let [ref (rum/use-ref nil)]
    (rum/use-effect!
     #(when editing? (.focus (rum/deref ref))))
    [:input.edit#todo-edit
     {:type "text"
      :ref ref
      :value title
      :on-change #(dispatch :update-todo (-> % .-target .-value))
      :on-blur #(dispatch :save-todo)
      :on-key-up #(when (= (-> % .-keyCode) ENTER)
                    (dispatch :save-todo))}]))
