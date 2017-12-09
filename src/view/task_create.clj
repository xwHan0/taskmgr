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

(defn- type-field [type]
  [:div#field
    [:span "Type: "] [:input#type {:type "text" :name "type" :size 10}]])

(defn- class-field [class]
  [:div#field
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
      [:form#task_cnxt {:method "post" :action (str "/add_task?id=" tid)}
        (title-field "")
        [:div (owner-field "") (due-field "")]
        [:div (type-field "") (class-field "")]
        [:textarea#descriptin {:name "description"}]
        [:input {:type "submit"}]
        (anti-forgery-field)
      ]
      )))