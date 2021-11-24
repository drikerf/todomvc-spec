(ns todomvc.components.todos-filters
  (:require [rum.core :as rum]
            [clojure.spec.alpha :as s]
            [todomvc.components.todos-filter :as todos-filter]
            [todomvc.spec :as spec]
            [todomvc.env :refer [disable-pbt?]]
            [todomvc.query :as query]))

(s/fdef component
  :args (s/cat :state ::spec/state)
  :ret ::spec/view
  :fn #(or @disable-pbt?
           (and
            (query/view-has? % :ul.filters)
            (query/view-has-n-of? 3 % :a
                                  {:content ::spec/display-status}))))

(rum/defc component [state]
  [:ul.filters
   (map
    #(rum/with-key (todos-filter/component state %) %)
    ["all" "active" "completed"])])
