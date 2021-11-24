(ns todomvc.storage)

(defn save!
  "Persists state to localstorage."
  ([key state]
   (let [ls (.-localStorage js/window)]
     (->> (clj->js state)
          (.stringify js/JSON)
          (.setItem ls key))))
  ([state]
   (save! "todos-spec" state)))

(defn load
  "Loads state from localstorage."
  ([key]
   (let [ls (.-localStorage js/window)]
     (as-> (.getItem ls key) $
          (.parse js/JSON $)
          (js->clj $ :keywordize-keys true))))
  ([]
   (load "todos-spec")))
