(defproject people-clojure "0.0.1-SNAPSHOT"
  :description "Project to work with parsing files and the web"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring "1.4.0"]
                 [compojure "1.5.0"]
                 [hiccup "1.0.5"]]
  :javac-options ["-target" "1.6" "-source" "1.6" "-Xlint:-options"]
  :aot [people-clojure.core]
  :main people-clojure.core)
