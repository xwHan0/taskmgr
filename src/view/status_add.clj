(ns view.status-add
  (:require
    [clojure.core.ex :refer :all]
    [hiccup.page :refer :all]
    [ring.util.anti-forgery :refer :all]
    [database.core :as db]
    [view.util :as util]))


(defn- status-field [status]
  [:div#field
    [:span "Status: "] [:input#status {:type "text" :name "status" :size 10 :value status}]])

(defn- owner-field [owner]
  [:div#field
    [:span "Owner: "] [:input#owner {:type "text" :name "owner" :size 10 :value owner}]])

(defn- complete-field [complete]
  [:div#field
    [:span "Complete(%): "] [:input#complete {:type "number" :name "complete" :size 10 :value complete}]])

(defn page [tid]
  (let [
    {:keys [status owner complete content]} (db/read-task-status tid)
  ]
    (util/page
      [
        "js/jquery-3.1.1.min.js" 
        "js/tinymce/tinymce.min.js" 
        "js/taskview.js"
      ]
      ["css/taskadd.css"]
      [:form#task_cnxt {:method "post" :action (str "/add_status?id=" tid)}
        (status-field status)
        (owner-field owner)
        (complete-field complete)
        [:textarea#descriptin {:name "description"} content]
        [:input {:type "submit"}]
        (anti-forgery-field)
      ]
      )))