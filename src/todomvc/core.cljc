(ns todomvc.core
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as string]
            [todomvc.spec :as spec]
            [todomvc.env :refer [disable-pbt?]]))

(s/fdef next-id
  :args (s/cat :state ::spec/state)
  :ret ::spec/id
  :fn #(or @disable-pbt?
           (let [todos (-> % :args :state :todos)
                 id (s/unform ::spec/id (:ret %))]
             (and
              (<= 0 id)
              (if (empty? todos)
                (= id 0)
                (= id (->> todos
                           (map :id)
                           (apply max)
                           (inc))))))))

(defn next-id [{:keys [todos]}]
  (if (seq todos)
    (if-let [curr (->> todos
                       (map :id)
                       (apply max))]
      (inc curr)
      0)
    0))

(s/fdef update-input
  :args (s/cat :state ::spec/state :value ::spec/title)
  :ret ::spec/state
  :fn #(or @disable-pbt?
           (let [iv (-> % :args :value)
                 state (s/unform ::spec/state (:ret %))]
             (if (nil? iv)
               (= "" (get-in state [:input :value]))
               (= iv (get-in state [:input :value]))))))

(defn update-input [state s]
  (assoc-in state [:input :value] s))

(s/fdef create-todo!
  :args (s/cat :state ::spec/state)
  :ret ::spec/state
  :fn #(or @disable-pbt?
           (let [before (-> % :args :state)
                 after (s/unform ::spec/state (:ret %))
                 last-todo (last (:todos after))]
             (if (and (seq (get-in before [:input :value]))
                      (seq (string/trim (get-in before [:input :value]))))
               (and
                (= "" (get-in after [:input :value]))
                (= (count (:todos after)) (inc (count (:todos before))))
                (= (string/trim (get-in before [:input :value]))
                   (:title last-todo))
                (not (:completed? last-todo))
                (not (:editing? last-todo)))
               (= (:todos after) (:todos before))))))

(defn create-todo! [{:keys [input] :as state}]
  (if (and (seq (:value input))
           (seq (string/trim (:value input))))
    (let [todo {:id (next-id state)
                :title (string/trim (:value input))
                :completed? false}]
      (-> state
          (update :todos #(concat % [todo]))
          (assoc-in [:input :value] "")))
    state))

(s/fdef remove-todo!
  :args (s/cat :state ::spec/state :id ::spec/id)
  :ret ::spec/state
  :fn #(or @disable-pbt?
           (let [before (-> % :args :state)
                 id (-> % :args :id)
                 after (s/unform ::spec/state (:ret %))]
             (and
              (if (as-> (:todos before) $
                    (map :id $)
                    (set $)
                    (contains? $ id))
                (= (count (:todos after)) (dec (count (:todos before))))
                (= (:todos after) (:todos before)))
              (as-> (:todos after) $
                (map :id $)
                (set $)
                (not (contains? $ id)))))))

(defn remove-todo! [state id]
  (-> state
      (update :todos (fn [xs]
                       (filter #(not= id (:id %)) xs)))))

(s/fdef edit-todo
  :args (s/cat :state ::spec/state :id ::spec/id)
  :ret ::spec/state
  :fn #(or @disable-pbt?
           (let [id (-> % :args :id)
                 after (s/unform ::spec/state (:ret %))
                 todo (->> (:todos after)
                           (filter (fn [x] (= (:id x) id)))
                           (first))]
             (if todo
               (:editing? todo)
               (every? (comp not :editing?) (:todos after))))))

(defn edit-todo [state id]
  (-> state
      (update :todos (fn [xs]
                       (map #(if (= id (:id %))
                               (assoc % :editing? true)
                               (dissoc % :editing?))
                            xs)))))

(s/fdef save-todo
  :args (s/cat :state ::spec/state)
  :ret ::spec/state
  :fn #(or @disable-pbt?
           (let [after (s/unform ::spec/state (:ret %))
                 id (->> (-> % :args :state :todos)
                         (filter :editing?)
                         (first)
                         (:id))
                 saved-todo (->> (-> after :todos)
                                 (filter (fn [t] (= (:id t) id)))
                                 (first))]
             (and
              (every? (comp not :editing?) (:todos after))
              (or (not (seq (:title saved-todo)))
                  (not (string/ends-with? (:title saved-todo) " ")))))))

(defn save-todo [state]
  (-> state
      (update :todos (fn [xs]
                       (->> xs
                            (map #(dissoc % :editing?))
                            (map #(update % :title string/trim)))))))

(s/fdef update-todo
  :args (s/cat :state ::spec/state :title ::spec/title)
  :ret ::spec/state
  :fn #(or @disable-pbt?
           (let [before (-> % :args :state)
                 title (-> % :args :title)
                 after (s/unform ::spec/state (:ret %))
                 todo (->> (:todos after)
                           (filter :editing?)
                           (first))]
             (if todo
               (= (:title todo) title)
               (= before after)))))

(defn update-todo [state s]
  (-> state
      (update :todos (fn [xs]
                       (map #(if (:editing? %)
                               (assoc % :title s) %)
                            xs)))))

(s/fdef toggle-todo
  :args (s/cat :state ::spec/state :id ::spec/id)
  :ret ::spec/state
  :fn #(or @disable-pbt?
           (let [before (-> % :args :state)
                 id (-> % :args :id)
                 after (s/unform ::spec/state (:ret %))
                 todo-before (->> (:todos before)
                                  (filter (fn [x] (= id (:id x))))
                                  (first)) 
                 todo-after (->> (:todos after)
                                 (filter (fn [x] (= id (:id x))))
                                 (first))]
             (if todo-before
               (= (:completed? todo-before) (not (:completed? todo-after)))
               (= before after)))))

(defn toggle-todo [state id]
  (-> state
      (update :todos (fn [xs]
                       (map #(if (= id (:id %))
                               (update % :completed? not) %)
                            xs)))))

(s/fdef toggle-all
  :args (s/cat :state ::spec/state)
  :ret ::spec/state
  :fn #(or @disable-pbt?
           (let [before (-> % :args :state)
                 after (s/unform ::spec/state (:ret %))]
             (if (every? :completed? (:todos before))
               (every? (complement :completed?) (:todos after))
               (every? :completed? (:todos after))))))

(defn toggle-all [{:keys [todos] :as state}]
  (let [completed? (not (every? :completed? todos))]
    (-> state
        (update :todos (fn [xs]
                         (map #(assoc % :completed? completed?) xs))))))

(s/fdef set-filter
  :args (s/cat :state ::spec/state :path ::spec/url-path)
  :ret ::spec/state
  :fn #(or @disable-pbt?
           (let [path (-> % :args :path)
                 before (-> % :args :state)
                 after (s/unform ::spec/state (:ret %))]
             (case path
               "" (= "all" (-> after :filter :status))
               "#/all" (= "all" (-> after :filter :status))
               "#/active" (= "active" (-> after :filter :status))
               "#/completed" (= "completed" (-> after :filter :status))
               (= before after)))))

(defn set-filter [state path]
  (if (s/valid? ::spec/status-link path)
    (let [status (last (clojure.string/split path #"/" 2))]
      (assoc-in state [:filter :status]
                (if (seq status) status "all")))
    state))

(s/fdef clear-completed!
  :args (s/cat :state ::spec/state)
  :ret ::spec/state
  :fn #(or @disable-pbt?
           (let [after (s/unform ::spec/state (:ret %))]
             (not-any? :completed? (:todos after)))))

(defn clear-completed! [state]
  (-> state
      (update :todos (fn [xs]
                       (filter (comp not :completed?) xs)))))

(s/fdef completed-todos
  :args (s/cat :state ::spec/state)
  :ret ::spec/todos
  :fn #(or @disable-pbt?
           (let [state (-> % :args :state)
                 todos (s/unform ::spec/todos (:ret %))]
             (= todos
                (->> (:todos state)
                     (filter :completed?))))))

(defn completed-todos [state]
  (->> state
       (:todos)
       (filter :completed?)))

(s/fdef visible-todos
  :args (s/cat :state ::spec/state)
  :ret ::spec/todos
  :fn #(or false
           (let [state (-> % :args :state)
                 todos (s/unform ::spec/todos (:ret %))]
             (case (get-in state [:filter :status])
               "all" (= (count todos) (count (:todos state)))
               "active" (every? (comp not :completed?) todos)
               "completed" (every? :completed? todos)))))

(defn visible-todos [{:keys [todos] {:keys [status]} :filter}]
  (->> todos
       (filter #(case status
                  "all" true
                  "active" (not (:completed? %))
                  "completed" (:completed? %)))))
