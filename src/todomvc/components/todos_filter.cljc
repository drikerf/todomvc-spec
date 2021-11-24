(ns todomvc.components.todos-filter
  (:require [rum.core :as rum]
            [clojure.spec.alpha :as s]
            [todomvc.spec :as spec]
            [todomvc.utils :as utils]
            [todomvc.env :refer [disable-pbt?]]
            [todomvc.query :as query]
            [clojure.string :as string]))

(s/fdef component
  :args (s/cat :state ::spec/state :status-filter ::spec/status)
  :ret ::spec/view
  :fn #(or @disable-pbt?
           (and
            (query/view-has? % :li)
            (query/view-has? % :a
                             {:href ::spec/status-link
                              :content (string/capitalize
                                        (-> % :args :status-filter))}))))

(rum/defc component
  [{{:keys [status]} :filter} status-filter]
  [:li
   [:a {:href (str "#/" status-filter)
        :class (utils/classnames
                {:selected (= status status-filter)})}
    (clojure.string/capitalize status-filter)]])
