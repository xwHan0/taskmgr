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

(defn- attribute-componment [{:keys [date owner due status complete finish-date resume-time]}]
  "按照task-info信息填充任务属性。"
  [:div#task_attr
    [:div#task_attr_line
      [:div "| Owner"] [:div (str ": " owner)]
      [:div "| Due"] [:div (str ": " due)]]
    [:div#task_attr_line
      [:div "| Complete"] [:div (str ": " complete "%")]
      [:div "| Status"] [:div (str ": " status)]]
    [:div#task_attr_line
      [:div "| Start"] [:div (str ": " date)]
      [:div "| Finish"] [:div (str ": " finish-date)]]
    [:div {:id "task_attr_line"}
      [:div "| Resume"] [:div {:id "resume_div"} (str ": " 22)]]])

(defn- task-commands [tid]
  [:div#task_command_bar
    [:div [:a {:href (str "/add_task?id=" tid)} "Create Sub Task"]]
    [:div " | "]
    [:div [:a {:href (str "/add_status?id=" tid)} "Add status"] ]])

(defn- subtask-componment [subtasks]
    "填充子任务组件。"
    [:div#task_subtask_top
        [:table {:id "task_subtask_tbl"}
        [:thead
        [:tr [:th "No."] [:th "Title"] [:th "Owner"] [:th "Status"]]]
        (apply vector :tbody
            (for [{:keys [id title owner status]} subtasks]
                [:tr [:td id] [:td [:a {:href (str "/task?id=" id)} title]] [:td owner] [:td status]]))]])
      

(defn page [tid]
  (let [
    task-info (into (db/read-task-status tid) (db/read-task tid))
    sub-tasks (db/read-sub-tasks tid)
    sub-tasks-status (->> sub-tasks (map :id) (map db/read-task-status) (map :status))
    sub-tasks-info (map #(assoc %1 :status %2) sub-tasks sub-tasks-status)
  ]
    ;(println "status is:" sub-tasks-info)
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
        (task-commands tid)
        (subtask-componment sub-tasks-info)
      ]
    ;   (include-js "js/taskview.js")
      )))