(ns view.task
  (:require
    [clojure.core.ex :refer :all]
    [hiccup.page :refer :all]
    [database.core :as db]
    [view.util :as util]))

(defn- title-componment-hirechy [parent-task-vector]
    "按照任务集成列表生成继承链接。parent-task-vector格式为：[{:title :id}]"
    (let [parent-comp-list (for [{:keys [title id]} parent-task-vector] 
                                [:a {:href (str "/task?id=" id)} title])]
        (->> parent-comp-list
            (interpose " / ")
            (apply vector :div [:a {:href "/task?id=0"} "TOP / "]))))

(defn- title-componment [{:keys [title]} parent-task-vector]
  "按照task-info的任务信息生产标题显示组件。"  
    [:div {:id "task_title_top"}
        [:div {:id "task_title_log"} [:img {:src "img/logo.jpg"}]]
        [:div {:id "task_title_info"}
        [:div {:id "task_title_title"} title]
        [:div {:id "task_title_hirechy"} (title-componment-hirechy parent-task-vector)]]])

(defn- attribute-componment [{:keys [date owner status complete finish-date resume-time]}]
  "按照task-info信息填充任务属性。"
  [:div#task_attr
    [:div#task_attr_line
      [:div "| Owner"] [:div (str ": " owner)]
      [:div "| Finish"] [:div (str ": " finish-date)]]
    [:div#task_attr_line
      [:div "| Complete"] [:div (str ": " complete "%")]
      [:div "| Status"] [:div (str ": " status)]]
    [:div#task_attr_line
      [:div "| Start"] [:div (str ": " date)]
      [:div "| Finish"] [:div (str ": " finish-date)]]
    [:div {:id "task_attr_line"}
      [:div "| Resume"] [:div {:id "resume_div"} (str ": " 22)]]])

(defn- subtask-componment [subtasks]
    "填充子任务组件。"
    [:div#task_subtask_top
        [:table {:id "task_subtask_tbl"}
        [:thead
        [:tr [:th "No."] [:th "Title"] [:th "Owner"] [:th "Date"]]]
        (apply vector :tbody
            (for [{:keys [id title owner date]} subtasks]
                [:tr [:td id] [:td [:a {:href (str "/task?id=" id)} title]] [:td owner] [:td date]]))]])
      

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
      [:div#task_cnxt
        (title-componment task-info (db/read-ancestor-tasks tid))
        (attribute-componment task-info)
        (subtask-componment (db/read-sub-tasks tid))
      ]
    ;   (include-js "js/taskview.js")
      )))