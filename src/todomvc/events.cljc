(ns todomvc.events
  (:require [todomvc.core :as core]
            [todomvc.db :refer [state-atom]]))

(def DEBUG? false)

(defmulti dispatch (fn [event & args]
                     (when DEBUG?
                       (println event)
                       (apply println args))
                     event))

(defmethod dispatch :update-input [_ s]
  (swap! state-atom core/update-input s))

(defmethod dispatch :create-todo! [_]
  (swap! state-atom core/create-todo!))

(defmethod dispatch :remove-todo! [_ id]
  (swap! state-atom core/remove-todo! id))

(defmethod dispatch :edit-todo [_ id]
  (swap! state-atom core/edit-todo id))

(defmethod dispatch :save-todo [_]
  (swap! state-atom core/save-todo))

(defmethod dispatch :update-todo [_ s]
  (swap! state-atom core/update-todo s))

(defmethod dispatch :toggle-todo [_ id]
  (swap! state-atom core/toggle-todo id))

(defmethod dispatch :toggle-all [_]
  (swap! state-atom core/toggle-all))

(defmethod dispatch :set-filter [_ path]
  (swap! state-atom core/set-filter path))

(defmethod dispatch :clear-completed! []
  (swap! state-atom core/clear-completed!))
