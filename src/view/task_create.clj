(ns view.task-create
  (:require
    [clojure.core.ex :refer :all]
    [hiccup.page :refer :all]
    [database.core :as db]
    [view.util :as util]))




(defn page [tid]
  (let [task-info (into (db/read-task-status tid) (db/read-task tid))]
    (util/page
      [
        "js/jquery-3.1.1.min.js" 
        "dataTables/jquery.dataTables.min.js" 
        "js/tinymce/tinymce.min.js" 
        "js/taskview.js"
      ]
      ["css/taskview.css" "dataTables/jquery.dataTables.min.css"]
      [:form#task {:method "post" :action (str "/create_task?id=" tid)}
        [:input#title {:type "text" :name "title"}]
        [:input#due {:type "date" :name "due"}]
        [:input#owner {:type "text" :name "owner"}]
        [:textarea#descriptin {:name "description"}]
        [:input {:type "submit"}]
      ]
      )))