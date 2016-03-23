(ns people-clojure.core
  (:require [clojure.string :as str] ;this brings in a new lib
            [clojure.walk :as walk] ;brings in walk lib 
            [compojure.core :as c]
            [ring.adapter.jetty :as j]
            [ring.middleware.params :as p]
            [hiccup.core :as h])
  (:gen-class))

(defn read-people []
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
        people (walk/keywordize-keys people)]
    ;(spit "filtered_people.edn" (pr-str people))   ;this is a file writer
    people))

(defn countries-html [people]
  (let [all-countries (map :country people)
        unique-countries (set all-countries)
        sorted-countries (sort unique-countries)]
    [:div
     (map (fn [country]
           [:span
            [:a {:href (str "/?country=" country)} country]
            " "])
      sorted-countries)]))


(defn people-html [people]
  [:ol
   (map (fn [person]
          [:li (str (:first_name person) " " (:last_name person))])
     people)])
                 

(c/defroutes app    ;define routes here. The app part names the routes for the server to use
  (c/GET "/" request
    (let [params (:params request)
          country (get params "country")
          country (or country "Brazil")
          people (read-people)
          filtered-people (filter (fn [person]
                                    (= (:country person) country))
                            people)]
      (h/html [:html 
               [:body 
                (countries-html people)
                (people-html filtered-people)]]))))

(defn -main []
  (j/run-jetty (p/wrap-params app) {:port 3000}))   ;params for the web server
  
  