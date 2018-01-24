(ns view.status-add
  (:require
    [clojure.core.ex :refer :all]
    [hiccup.page :refer :all]
    [ring.util.anti-forgery :refer :all]
    [database.core :as db]
    [util.command :as cmd]
    [debux.core :refer :all]
    [view.util :as util]))

(defn- title-field [title]
  [:div#field
    [:span "Task Title: "] [:input#title {:type "text" :name "title" :size 80 :value title}]])

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
    
(defn page [{:keys [tid cid status? date? owner? content? title? href cmd edit?]}]
  (let [
    {:keys [title owner complete status start finish content]} (dbgn (cmd/read cmd tid))
  ]
    (util/page
      [
        "js/jquery-3.1.1.min.js" 
        "js/tinymce/tinymce.min.js" 
        "js/taskview.js"
      ]
      ["css/taskadd.css"]
      [:form#task_cnxt {:method "post" :action href}
        (when (if edit? title title?) (title-field title))
        (when owner? (owner-field owner))
        (when status? (status-field complete status))
        (when date? (date-field start finish))
        (when content? [:textarea#descriptin {:name "description"} content])
        [:input {:type "submit" :value "submit"}]
        (anti-forgery-field)
      ]
      )))