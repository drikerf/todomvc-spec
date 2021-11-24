(ns todomvc.test-utils
  (:require [clojure.spec.test.alpha :as stest]
            [clojure.test :refer [run-all-tests]]
            [expound.alpha :as expound]
            [clojure.spec.alpha :as s]
            [clojure.tools.namespace.repl :refer [disable-reload!]]
            [todomvc.env :refer [disable-pbt?]]))

(disable-reload!)

(defonce def-opts
  (atom {:num-tests (if-let [s (System/getenv "N_TESTS")]
                      (Integer/parseInt s)
                      1)
         :max-size 10}))

(defn speccheck [fn-to-check]
  (binding [s/*explain-out* expound/printer]
    (let [results (stest/check [fn-to-check] {:clojure.spec.test.check/opts @def-opts})]
      (if (some :failure results)
        (do
          (println "\nFailed specs:")
          (doseq [result results
                  :when (:failure result)]
            (println (:sym result))
            (expound/explain-results results)))
        true))))

(defn do-tests [n include-pbt?]
  (swap! def-opts assoc :num-tests n)
  ;; Works but wont print in fdef since it returns early when true.
  (swap! disable-pbt? (fn [_] (not include-pbt?)))
  (run-all-tests #"todomvc.*"))
