(defproject taskmgr "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.1"]
                 [ring/ring-defaults "0.2.1"]
                 [org.clojure/core.ex "0.1.0"]
                 [hiccup/hiccup "1.0.5"]
                 ;For database
                 [org.clojure/java.jdbc "0.7.3"]
                 [org.xerial/sqlite-jdbc "3.7.2"]
                 ;for clojurescript
                 [org.clojure/tools.reader "1.0.0-beta3"]
                 [org.clojure/clojurescript "1.9.229"]
                 [reagent "0.6.0"]]
    :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]
  
  :plugins [[lein-ring "0.9.7"]
            [lein-cljsbuild "1.1.5"]]
  :ring {:handler taskmgr.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}}
  :cljsbuild {
     :builds [
        {:id "dev"
         :source-paths ["src-cljs"] 
         :compiler {
                    :output-dir "resources/public/js/out"
                    :output-to "resources/public/js/taskview_cljs.js"
                    :pretty-print true
      }
      }]})
