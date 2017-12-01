(ns view.task-create
  (:require
    [clojure.core.ex :refer :all]
    [hiccup.page :refer :all]
    [ring.util.anti-forgery :refer :all]
    [database.core :as db]
    [view.util :as util]))


(defn- title-field [title]
  [:div#field
    [:span "Task Title: "] [:input#title {:type "text" :name "title" :size 80}]])

(defn- owner-field [owner]
  [:div#field
    [:span "Owner: "] [:input#owner {:type "text" :name "owner" :size 10}]])

(defn- due-field [due]
  [:div#field
    [:span "Due Date: "] [:input#date {:type "date" :name "due" :size 10}]])

(defn page [tid]
  (let [task-info (into (db/read-task-status tid) (db/read-task tid))]
    (util/page
      [
        "js/jquery-3.1.1.min.js" 
        "js/tinymce/tinymce.min.js" 
        "js/taskview.js"
      ]
      ["css/taskadd.css"]
      [:form#task_cnxt {:method "post" :action (str "/add_task?id=" tid)}
        (title-field "")
        (owner-field "")
        (due-field "")
        ; [:input#due {:type "date" :name "due"}]
        ; [:input#owner {:type "text" :name "owner"}]
        [:textarea#descriptin {:name "description"}]
        [:input {:type "submit"}]
        (anti-forgery-field)
      ]
      )))