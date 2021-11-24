(ns ^:figwheel-hooks todomvc.main
  (:require [goog.dom :as gdom]
            [rum.core :as rum]
            [todomvc.events :refer [dispatch]]
            [todomvc.db :as db]
            [todomvc.storage :as storage]
            [todomvc.components.app :as app]))

(def with-persistance? true)

(defn init-state [use-storage?]
  (if-let [state (and use-storage? (storage/load))]
    state
    (db/create-state)))

(rum/defc app [state]
  [:div (app/component state)])

(defn get-app-element []
  (gdom/getElement "app"))

(defn mount [el state]
  (rum/mount (app state) el))

(defn mount-app-element [state]
  (when-let [el (get-app-element)]
    (mount el state)))

(when (nil? (deref db/state-atom))
  (add-watch db/state-atom :app-loop
             (fn [_ _ _ new-state]
               (when with-persistance?
                 (storage/save! new-state))
               (mount-app-element new-state)))
  (reset! db/state-atom (init-state with-persistance?))
  (.addEventListener js/window "hashchange"
                     #(dispatch :set-filter (-> js/window .-location .-hash)))
  (dispatch :set-filter (-> js/window .-location .-hash))
  (mount-app-element (deref db/state-atom)))

(defn ^:after-load on-reload []
  (mount-app-element (deref db/state-atom)))
