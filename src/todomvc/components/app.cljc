(ns todomvc.components.app
  (:require [rum.core :as rum]
            [clojure.spec.alpha :as s]
            [todomvc.spec :as spec]
            [todomvc.query :as query]
            [todomvc.components.title :as title]
            [todomvc.components.todo-input :as todo-input]
            [todomvc.components.todos-list :as todos-list]
            [todomvc.components.todos-toggle :as todos-toggle]
            [todomvc.components.footer :as footer]
            [todomvc.components.info :as info]
            [todomvc.env :refer [disable-pbt?]]))

(s/fdef component
  :args (s/cat :state ::spec/state)
  :ret ::spec/view
  :fn #(or @disable-pbt?
           (and
            (query/view-has? % :.app)
            (query/view-has? % :.todoapp)
            (if (-> % :args :state :todos seq)
              (and (query/view-has? % :.main)
                   (query/view-has? % :footer.footer))
              (not (or (query/view-has? % :.main)
                       (query/view-has? % :footer.footer)))))))

(rum/defc component [{:keys [todos] :as state}]
  [:.app
   [:.todoapp
    (title/component state)
    (todo-input/component state)
    (when (seq todos)
      [:.main
       (todos-toggle/component state)
       (todos-list/component state)])
    (when (seq todos)
      (footer/component state))]
   (info/component state)])
