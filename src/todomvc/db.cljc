(ns todomvc.db)

(defonce state-atom (atom nil))

(defn create-state []
  {:todos []
   :input {:value ""}
   :filter {:status "all"}})
