(ns todomvc.components.title
  (:require [rum.core :as rum]
            [todomvc.spec :as spec]
            [clojure.spec.alpha :as s]
            [todomvc.query :as query]
            [todomvc.env :refer [disable-pbt?]]))

(s/fdef component
  :args (s/cat :state ::spec/state)
  :ret ::spec/view
  :fn #(or @disable-pbt?
           (query/view-has? % :h1 {:content "todos"})))

(rum/defc component [_]
  [:h1 "todos"])
