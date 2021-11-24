(ns todomvc.query
  (:require [clojure.string :as string]
            [todomvc.spec :as spec]
            [clojure.spec.alpha :as s]))

(defn- indices
  "Indexes matching predicate."
  [pred coll]
  (keep-indexed #(when (pred %2) %1) coll))

(defn- nth?
  "Nth, but returns nil if out of bounds."
  [xs n]
  (if (< (dec (count xs)) n)
    nil
    (nth xs n)))

(defn q
  "Query structure for some selector `k` matching some query `q`.
  `:content` is a special key to match the content of a node.
  Returns the matched node without child nodes."
  ([xs k q]
   (let [qm (dissoc q :content)
         qc (:content q)
         flatlist (flatten xs)
         c-matcher (fn [x]
                     (if (qualified-keyword? qc)
                       (s/valid? qc x)
                       (= x qc)))
         m-matcher (fn [x]
                     (reduce-kv (fn [match? k v]
                                  (and match?
                                       (if (set? v)
                                         (contains? v (k x))
                                         (if (qualified-keyword? v)
                                           (s/valid? v (k x))
                                           (= v (k x))))))
                                true qm))
         match (->> flatlist
                    (indices #(= k %))
                    (filter (fn [idx]
                              (cond
                                (and (seq qm) (seq qc))
                                (and (m-matcher (nth? flatlist (+ idx 1)))
                                     (c-matcher (nth? flatlist (+ idx 2))))
                                (seq qm)
                                (or (m-matcher (nth? flatlist (+ idx 1)))
                                    (m-matcher (nth? flatlist (+ idx 2))))
                                ;; Can be a number (convert to string).
                                (seq (str qc))
                                (or (c-matcher (nth? flatlist (+ idx 1)))
                                    (c-matcher (nth? flatlist (+ idx 2))))
                                ;; Is the correct?
                                ;; Do we allow {:content nil} to always be true?
                                :else true)))
                    (map (fn [idx]
                           (->> flatlist
                                (drop (inc idx))
                                (take-while #(not (keyword? %)))
                                (cons k)
                                (vec))))
                    (vec))]
     match))
  ([xs k]
   (q xs k {})))

(defn has?
  "Checks if element is in datastructure."
  [& args]
  (boolean (seq (apply q args))))

(defn view-has?
  "Checks if view has element."
  [xs & args]
  (apply has? (s/unform ::spec/view (:ret xs)) args))

(defn n-of
  "Counts number of specified element in view."
  [& args]
  (count (apply q args)))

(defn view-has-n-of?
  "Checks if view has `n` elements of argument."
  [n xs & args]
  (= n (apply n-of (s/unform ::spec/view (:ret xs)) args)))

(defn substr?
  "Checks for substring somewhere in data structure."
  [xs s]
  (-> xs
      (vector)
      (str)
      (string/includes? s)))
