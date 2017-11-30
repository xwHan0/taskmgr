(ns view.weekly-report
  (:require [clojure.core.ex :refer :all] 
            [hiccup.core :refer :all]
            [hiccup.page :refer :all]
            [view.util :as util]
            [database.core :as db]))

(defn- detail-tbl [dates tid]
  "按照任务集成列表生成继承链接。parent-task-vector格式为：[{:title :id}]
  task-status ::= [{title owner status}...]
  status ::= [{complete status content}]
  "
  (let [
    sub-tasks (db/read-sub-tasks tid)
    sub-tasks-id (map :id sub-tasks)
    sub-tasks-status
      (for [tid sub-tasks-id]
        (for [date dates]
          (db/read-task-status tid date)))
    task-status (map #(assoc %1 :status %2) sub-tasks sub-tasks-status)
  ]
    [:table 
      [:thead [:tr [:th "Task"] [:th "Due"] (for [d dates] [:th d])]]
      [:tbody
        (for [{:keys [title owner status due]} task-status]
          [:tr [:td.report_tasks_title [:div.task_title title] [:div#task_owner owner]]
            [:td due]
            (for [{:keys [complete status content] :or {complete 0 status "gray"}} status]
              [:td [:div.report_complete [:img {:src (str "img/" status ".png")}] complete "%" ] [:div content]])])]]))

(defn page [tid date]
  (let [
    ]
    (util/page
      ;JS
      []
      ;CSS
      ["css/table.css" "css/report.css"]
      ;Component
      [:h2 (->> tid db/read-task :title)]
      [:p (:content (db/read-task-status tid date))]
      (detail-tbl ["2017-11-27" "2018-01-01"] tid))))