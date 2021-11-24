(ns todomvc.components.todo-item
  (:require [rum.core :as rum]
            [clojure.spec.alpha :as s]
            [todomvc.spec :as spec]
            [todomvc.utils :as utils]
            [todomvc.events :refer [dispatch]]
            [todomvc.components.todo-toggle :as todo-toggle]
            [todomvc.components.todo-edit :as todo-edit]
            [todomvc.components.todo-destroy :as todo-destroy]
            [todomvc.env :refer [disable-pbt?]]
            [todomvc.query :as query]
            [clojure.string :as string]))

(s/fdef component
  :args (s/cat :state ::spec/state :todo ::spec/todo)
  :ret ::spec/view
  :fn #(or @disable-pbt?
           (and
            (query/view-has? % :li
                             {:class (let [{:keys [editing? completed?]}
                                           (-> % :args :todo)]
                                       (cond-> ""
                                         completed? (str " completed")
                                         editing? (str " editing")
                                         true (string/trim)
                                         true (utils/purge-str)))})
            (query/view-has? % :.view
                             {:on-double-click ::spec/event-handler}))))

(rum/defc component [state {:keys [id title completed? editing?] :as todo}]
  [:li
   {:class (utils/classnames
            {:completed completed?
             :editing editing?})}
   [:.view
    {:on-double-click #(dispatch :edit-todo id)}
    (todo-toggle/component state todo)
    [:label title]
    (todo-destroy/component state todo)]
   (todo-edit/component state todo)])
