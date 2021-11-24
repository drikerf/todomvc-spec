(ns todomvc.utils
  (:require [clojure.string :as string]))

(defn classnames
  "Will return a string of classnames who's values are truthy."
  [coll]
  (-> (reduce-kv
       (fn [s key val]
         (if val
           (str s " " (name key))
           s))
       "" coll)
      (string/trim)))

(defn purge-str
  "Transforms empty string to nil."
  [s]
  (if (seq s)
    s
    nil))
