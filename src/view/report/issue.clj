(ns view.report.issue
  (:require [clojure.core.ex :refer :all] 
            [hiccup.core :refer :all]
            [hiccup.page :refer :all]
            [clj-time.core :as t]
            [clj-time.format :as tf]
            [view.util :as util]
            [database.core :as db]))

(defn- detail-tbl [tid]
  "按照任务集成列表生成继承链接。parent-task-vector格式为：[{:title :id}]
  task-status ::= [{title owner status}...]
  status ::= [{complete status content}]
  "
  (let [
    sub-tasks (db/read-sub-tasks tid)
    sub-tasks-id (map :id sub-tasks)
    sub-tasks-status
      (for [tid sub-tasks-id]
        (-> tid db/read-task-statuses reverse))
    task-status (map #(assoc %1 :status %2) sub-tasks sub-tasks-status)
  ]
    [:table 
      [:thead [:tr [:th "Issue"] [:th "Owner"] [:th "Description"] [:th "Status"]]]
      [:tbody
        (for [{:keys [title owner status]} task-status]
          [:tr [:td title] [:td owner]
            [:td (for [{:keys [content finish] :or {}} status]
              (when (and finish content) [:div "[" finish "]" content]))]
            [:td (-> status last :status)]])]]))

(defn page [tid & date]
  (let [
    custom-format (tf/formatter "yyyy-MM-dd")
    date 
      (if (first date)
        (first date)
        (tf/unparse custom-format (t/to-time-zone (t/now) (t/default-time-zone))))
    current (t/plus (tf/parse custom-format date) (t/days 1))
    intervals (map #(t/weeks %) (reverse (range 4)))
    dates (map #(t/minus current %) intervals)
    dates (map #(tf/unparse custom-format %) dates)
    {:keys [status content]} (db/read-task-status tid date)
    ]
    (util/page
      ;JS
      []
      ;CSS
      ["css/table.css" "css/report.css"]
      ;Component
      [:h2 (->> tid db/read-task :title) [:img {:src (str "img/" status ".png")}]]
      [:p content]
      (detail-tbl tid))))