(ns todomvc.spec
  (:require [clojure.spec.alpha :as s]
            [clojure.test.check.generators]
            #_:clj-kondo/ignore
            [clojure.spec.gen.alpha :as gen]
            [com.gfredericks.test.chuck.generators :as cg]
            [clojure.string :as string]))

;; Todo
(s/def :todo/id nat-int?)
(s/def :todo/title (s/and string? (complement string/blank?)))
(s/def :todo/completed? boolean?)
(s/def :todo/editing? boolean?)
(s/def ::todo
  (s/keys :req-un [:todo/id :todo/title :todo/completed?]
          :opt-un [:todo/editing?]))
(s/def ::todos
  (s/and 
   (s/coll-of ::todo :distinct true :into [])
   #(if (empty? %) true (and (apply distinct? (mapv :id %))
                             (< (count (filterv :editing? %)) 2)))))

;; Input
(s/def :input/value
  #?(:clj (s/with-gen string?
            #(cg/string-from-regex #"[a-zA-Z0-9!? ]+"))
     :cljs string?))
(s/def ::input
  (s/keys :req-un [:input/value]))

;; Filters
(s/def :filter/status #{"all" "active" "completed"})
(s/def ::filter
  (s/keys :req-un [:filter/status]))

;; State
(s/def ::state
  (s/keys :req-un [::todos ::input ::filter]))

;; Simple types
(s/def ::id :todo/id)
(s/def ::title :todo/title)
(s/def ::url-path string?)
(s/def ::status :filter/status)
(s/def ::display-status #{"All" "Active" "Completed"})
(s/def ::status-link #{"#/all" "#/active" "#/completed" ""})

;; Known HTML elements
(def known-elements
  #{:div :checkbox :button :input :form :nav :footer :ul :li :a :span :p :h1 :h2 :label
    :em :strong :rum/nothing})

;; Tags can either be known elements, classes or ids
(s/def ::tag (s/or :known-el (s/and keyword? #(known-elements %))
                   :known-el-with-class-or-id
                   (s/and keyword?
                          #(some-> (name %)
                                   (clojure.string/split #"\.|#" 2)
                                   (first)
                                   (keyword)
                                   (known-elements)))
                   :class (s/and keyword? #(clojure.string/starts-with? (name %) "."))
                   :id (s/and keyword? #(clojure.string/starts-with? (name %) "#"))))
;; Valid Hiccup
(s/def ::hiccup
  (s/cat :tag ::tag
         ;; Would it be possible to define this more narrow.
         ;; Can we encode event handlers using regex match for `on-` etc?
         :attributes (s/? map?)
         :content (s/* (s/or :terminal string?
                             :terminal-number number?
                             :nil nil?
                             :el ::hiccup
                             :els (s/coll-of ::hiccup)))))

;; Dom tree
(s/def ::view ::hiccup)

;; Event handlers
(s/def ::event-handler fn?)

(s/def ::ref #?(:clj #(instance? clojure.lang.Atom %)
                :cljs any?))
