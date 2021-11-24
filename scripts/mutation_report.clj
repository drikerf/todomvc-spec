(ns mutation-report
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.string :as string]))

(defn csv-data->maps [csv-data]
  (map zipmap
       (->> (first csv-data)
            (map keyword)
            repeat)
       (rest csv-data)))

(defn make-report []
  (let [reader (io/reader "report_1_to_10.csv")
        mapped (csv-data->maps (csv/read-csv reader))]
    (->> mapped
         (map (fn [m]
                (update m :id (fn [s]
                                (first (string/split s #"_"))))))
         (filter #(not= (:id %) "base"))
         (group-by (juxt :id :pbt)))))

(comment
  (make-report))
