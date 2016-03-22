(ns people-clojure.core
  (:require [clojure.string :as str] ;this brings in a new namespace
            [clojure.walk :as walk]) ;brings in walk lib 
  (:gen-class))

(defn -main []
  (let [people (slurp "people.csv") ;slurp reads in a file
        people (str/split-lines people) ;calls split-lines from the clojure.string lib
        people (map (fn [line]
                      (str/split line #","))
                    people)
        header (first people)
        people (rest people)
        people (map (fn [line]
                      (apply hash-map (interleave header line)))
                    people)
        people (walk/keywordize-keys people)
        people (filter (fn [line]
                         (= (:country line) "Brazil"))
                 people)]
    (spit "filtered_people.edn" (pr-str people))   ;this is a file writer
    people))
