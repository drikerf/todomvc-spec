(ns todomvc.mutations
  (:require [clojure.string :as string]
            [clojure.java.shell :refer [sh]]
            [clojure.java.io :as io]
            [clojure.tools.namespace.repl :refer [refresh]]
            [todomvc.test-utils :refer [do-tests]]
            clojure.pprint))

(def debug? false)

(defn log
  [& args]
  (when debug?
    (apply println args)))

(def num-tests
  (cons 1 (range 10 110 10)))

(def pbt
  [true false])

(defn mutation-id
  [m]
  (-> (string/split m #"/")
      (last)
      (string/split #"\.")
      (first)))

(defn- bool->int
  [b]
  (if b 1 0))

(defn- header
  []
  (->> num-tests
       (string/join ",")
       (str "id,pbt,")
       (list)
       (vec)))

(defn- report!
  [fn report]
  (let [s (->> report
               (map (partial string/join ","))
               (string/join "\n"))
        out (str "out/" fn ".csv")]
    (println "Saving report to: out/" out)
    (spit out (str s "\n"))))

(defn- get-mutations
  []
  (->> (file-seq (io/file "mutations"))
       (filter #(not (.isDirectory %)))
       (map #(.getPath %))))

(defn- apply-mutation!
  [m]
  (log "BEFORE APPLY DIFF:" m ":" (sh "git" "diff" "src/"))
  (if (= m "/base_00.patch")
    true
    (let [res (sh "git" "apply" m)]
      (if (= 0 (:exit res))
        (do (log "AFTER APPLY DIFF:" m ":" (sh "git" "diff" "src/")) true)
        (throw (Exception. (str "Couldn't apply mutation: " m ">" res)))))))

(defn- restore-repo!
  []
  (log "BEFORE RESTORE DIFF:" (sh "git" "diff" "src/"))
  (if (= 0 (:exit (sh "git" "restore" "src/todomvc")))
    (do (log "AFTER RESTORE DIFF:" (sh "git" "diff" "src/")) true)
    (throw (Exception. "Couldn't restore repo"))))

(defn setup!
  [m]
  (do (refresh)
      (apply-mutation! m)
      (refresh)))

(defn teardown!
  []
  (restore-repo!))

(defn- test-mutation!
  [m]
  (let [id (mutation-id m)
        _ (teardown!)]
    (->> pbt
         (map (fn [include-pbt?]
                (let [_ (setup! m)
                      xs (->> num-tests
                              (map (fn [n]
                                     (let [tr (do-tests n include-pbt?)]
                                       {:fail (bool->int
                                               (or (< 0 (:fail tr))
                                                   (< 0 (:error tr))))})))
                              (reduce (fn [row {:keys [fail]}]
                                        (conj row fail))
                                      [id include-pbt?]))
                      _ (teardown!)]
                  xs))))))

(defn test-all!
  [filename & args]
  (println "Running mutation tests...")
  (let [mutations (cons "/base_00.patch" (get-mutations))]
    (->> mutations
         (map test-mutation!)
         (reduce #(conj %1 (first %2) (last %2)) [(header)])
         (report! (or filename "report")))))

(defn test!
  ([filename & args]
   (apply test-all! filename args)
   (System/exit 0))
  ([]
   (test! "report")))

(comment
  (test-all! "report")
  (test-mutation! "/base_00.patch"))
