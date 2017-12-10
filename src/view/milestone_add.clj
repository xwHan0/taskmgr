(ns view.milestone-add
  (:require
    [clojure.core.ex :refer :all]
    [hiccup.page :refer :all]
    [ring.util.anti-forgery :refer :all]
    [database.core :as db]
    [view.util :as util]))


(defn- title-field [title]
  [:div#field
    [:span "Task Title: "] [:input#title {:type "text" :name "title" :size 80}]])

(defn- date-field [start finish]
  [:div#field
    [:span "Start Date: "] [:input#start {:type "date" :name "start" :size 10}]
    [:span "Finish Date: "] [:input#finish {:type "date" :name "finish" :size 10}]
    ])

(defn- type-field [type class]
  [:div#field
    [:span "Type: "] [:input#type {:type "text" :name "type" :size 10}]
    [:span "class: "] [:input#class {:type "text" :name "class" :size 10}]])

(defn page [tid]
  (let [task-info (into (db/read-task-status tid) (db/read-task tid))]
    (util/page
      [
        "js/jquery-3.1.1.min.js" 
        "js/tinymce/tinymce.min.js" 
        "js/taskview.js"
      ]
      ["css/taskadd.css"]
      [:form#task_cnxt {:method "post" :action (str "/add_milestone?id=" tid)}
        (title-field "")
        (date-field "" "")
        (type-field "" "")
        [:textarea#descriptin {:name "description"}]
        [:input {:type "submit"}]
        (anti-forgery-field)
      ]
      )))