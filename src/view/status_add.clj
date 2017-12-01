(ns view.status-add
  (:require
    [clojure.core.ex :refer :all]
    [hiccup.page :refer :all]
    [ring.util.anti-forgery :refer :all]
    [database.core :as db]
    [view.util :as util]))


(defn- status-field [title]
  [:div#field
    [:span "Status: "] [:input#status {:type "text" :name "status" :size 10}]])

(defn- owner-field [owner]
  [:div#field
    [:span "Owner: "] [:input#owner {:type "text" :name "owner" :size 10}]])

(defn- complete-field [due]
  [:div#field
    [:span "Complete(%): "] [:input#complete {:type "number" :name "complete" :size 10}]])

(defn page [tid]
  (let []
    (util/page
      [
        "js/jquery-3.1.1.min.js" 
        "js/tinymce/tinymce.min.js" 
        "js/taskview.js"
      ]
      ["css/taskadd.css"]
      [:form#task_cnxt {:method "post" :action (str "/add_status?id=" tid)}
        (status-field "")
        (owner-field "")
        (complete-field "")
        [:textarea#descriptin {:name "description"}]
        [:input {:type "submit"}]
        (anti-forgery-field)
      ]
      )))