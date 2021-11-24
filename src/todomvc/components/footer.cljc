(ns todomvc.components.footer
  (:require [rum.core :as rum]
            [clojure.spec.alpha :as s]
            [todomvc.spec :as spec]
            [todomvc.components.todos-count :as todos-count]
            [todomvc.components.todos-filters :as todos-filters]
            [todomvc.components.todos-clear :as todos-clear]
            [todomvc.env :refer [disable-pbt?]]
            [todomvc.query :as query]))

(s/fdef component
  :args (s/cat :state ::spec/state)
  :ret ::spec/view
  :fn #(or @disable-pbt?
           (query/view-has? % :footer.footer)))

(rum/defc component [state]
  [:footer.footer
   (todos-count/component state)
   (todos-filters/component state)
   (todos-clear/component state)])
