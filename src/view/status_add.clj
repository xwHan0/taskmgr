(ns view.status-add
  (:require
    [clojure.core.ex :refer :all]
    [hiccup.page :refer :all]
    [ring.util.anti-forgery :refer :all]
    [database.core :as db]
    [view.util :as util]))


(defn- owner-field [owner]
  [:div#field
    [:span "Owner: "] [:input#owner {:type "text" :name "owner" :size 10 :value owner}]])

(defn- status-field [complete status]
  [:div#field
    [:span "Status: "] [:input#status {:type "text" :name "status" :size 10 :value status}]
    [:span "Complete(%): "] [:input#complete {:type "number" :name "complete" :size 10 :value complete}]])

(defn- date-field [start finish]
  [:div#field
    [:span "Start: "] [:input#start {:type "datetime-local" :name "start" }]
    [:span "Finish: "] [:input#finish {:type "datetime-local" :name "finish" }]])
    
(defn page [tid & cid]
  (let [
    cid (first cid)
        {:keys [status owner complete content start finish]} (last (db/read-descriptions tid cid))
  ]
    (util/page
      [
        "js/jquery-3.1.1.min.js" 
        "js/tinymce/tinymce.min.js" 
        "js/taskview.js"
      ]
      ["css/taskadd.css"]
      [:form#task_cnxt {:method "post" :action (str "/add_status?id=" tid)}
        (owner-field owner)
        (status-field complete status)
        (date-field start finish)
        [:textarea#descriptin {:name "description"} content]
        [:input {:type "submit" :value "submit"}]
        (anti-forgery-field)
      ]
      )))