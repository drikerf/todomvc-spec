(ns todomvc.env
  #?(:clj (:require [clojure.tools.namespace.repl :refer [disable-reload!]])))

#?(:clj (disable-reload!))

(defonce disable-pbt?
  (atom (if #?(:clj (System/getenv "ENABLE_PBT")
               :cljs nil)
          false true)))

(comment
  ;; Set PBT in REPL session.
  (swap! disable-pbt? (fn [_] false)))
