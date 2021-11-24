(ns todomvc.components.info
  (:require [rum.core :as rum]
            [clojure.spec.alpha :as s]
            [todomvc.spec :as spec]
            [todomvc.env :refer [disable-pbt?]]
            [todomvc.query :as query]))

(s/fdef component
  :args (s/cat :state ::spec/state)
  :ret ::spec/view
  :fn #(or @disable-pbt?
           (query/view-has? % :a {:href "https://todomvc.com"})))

(rum/defc component [_]
  [:footer.info
   [:p "Double-click to edit a todo"]
   [:p "Implementation of "
    [:a {:href "https://todomvc.com"}
     "TodoMVC"]
    "."]])
