(ns view.weekly-report
  (:require [clojure.core.ex :refer :all] 
            [hiccup.core :refer :all]
            [hiccup.page :refer :all]
            [view.util :as util]
            [database.core :as db]))

(defn- detail-tbl [dates task-status]
  "按照任务集成列表生成继承链接。parent-task-vector格式为：[{:title :id}]"
  (let []
    [:table {:border "1em"}
      [:thead [:tr [:th "Title"] (for [d dates] [:td d])]]
      [:tbody
        (for [{:keys [title content]} task-status]
          [:tr [:td title]
          (for [cnxt content]
            [:td cnxt])])]]))

(defn page [tid]
  (let [
    sub-tasks (db/read-sub-tasks tid)
    sub-tasks-status (->> sub-tasks (map :id) (map db/read-task-status))
    sub-tasks-map (map #(hash-map :title (:title %1) :content [%2]) sub-tasks sub-tasks-status)
    ]
    (util/page
      ;JS
      []
      ;CSS
      ["css/taskview.css"]
      ;Component
      [:h2 (->> tid db/read-task :title)]
      [:p (db/read-task-status tid)]
      (detail-tbl ["2017-11-27"] sub-tasks-map))))