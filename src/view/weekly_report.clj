(ns view.weekly-report
  (:require [clojure.core.ex :refer :all] 
            [hiccup.core :refer :all]
            [hiccup.page :refer :all]))

(defn- page
  "把componments填入页面主框架，并调用html函数形成页面。"
  [& componments]
  (html5
     [:head
      (include-css "css/taskview.css")
      (include-js "js/jquery-3.1.1.min.js")
      
      ]
     (apply vector :body componments)))


;--------------------------------------------------------------------------------------
(defn- detail-tbl [dates task-status]
  "按照任务集成列表生成继承链接。parent-task-vector格式为：[{:title :id}]"
  (let []
    [:table
      [:thead [:tr [:th "Title"] (for [d dates] [:td d])]]
      [:tbody
        (for [{:keys [title content]} task-status]
          [:tr [:td title]
          (for [cnxt content]
            [:td cnxt])])]]))

(defn- title-componment
  "按照task-info的任务信息生产标题显示组件。"
  [{:keys [title]} parent-task-vector]
  [:div {:id "task_title_top"}
   [:div {:id "task_title_log"} [:img {:src "img/logo.jpg"}]]
   [:div {:id "task_title_info"}
    [:div {:id "task_title_title"} title]
    [:div {:id "task_title_hirechy"} (title-componment-hirechy parent-task-vector)]]])

;--------------------------------------------------------------------------------------
(defn- attribute-componment
  "按照task-info信息填充任务属性。"
  [{:keys [start-date finish-date resume-time]}]
  [:div {:id "task_attr"}
   [:div {:id "task_attr_line"}
    [:div "| Start"] [:div (str ": " start-date)]
    [:div "| Finish"] [:div (str ": " finish-date)]]
   [:div {:id "task_attr_line"}
    [:div "| Resume"] [:div {:id "resume_div"} (str ": " 22)]]])

;--------------------------------------------------------------------------------------
(defn- subtask-componment
  "填充子任务组件。"
  [subtasks]
  [:div {:id "task_subtask_top"}
   [:table {:id "task_subtask_tbl"}
    [:thead
     [:tr [:th "No."] [:th "Title"]]]
    (apply vector :tbody
           (for [{:keys [no title]} subtasks]
             [:tr [:td no] [:td title]]))]])

;--------------------------------------------------------------------------------------
(defn- comment-componment-history
  "按照comments显示历史comment信息"
  [{:keys [date comment]}]
  [:div {:id "task_comment_one"}
   [:div {:id "task_comment_ctrl"} 
    date " Edit | Delete" ]
   [:div {:id "task_comment_cnxt"} comment]])

(defn- comment-componment
  "按照comments信息填充注释组件。"
  [comments]
  [:div {:id "task_comment_top"}
   (apply vector :div 
          {:id "task_comment_history"}
          (map comment-componment-history comments))
   [:div {:id "task_comment_new"}
    [:textarea {:id "comment" :name "comment"}]
    [:br]
    [:input {:type "button" :id "comment" :name "comment" :value "comment"}]]])

;--------------------------------------------------------------------------------------

(defn task-view
  "任务显示页面定义。"
  [{:keys [title description start-date finish-date resume-time] :as task-info}]
  (task-view-page
    (title-componment task-info [{:title "sup1" :id 1} {:title "sup2" :id 2}])
    [:div {:id "task_cnxt"}
     (attribute-componment task-info)
     (subtask-componment [{:no 0 :title "subtask1"}
                          {:no 1 :title "subtask2"}])
     (comment-componment [{:date "2016/12/26" :comment "Test"} 
                         {:date "2016/12/25" :comment "Haha"}])]
    (include-js "js/taskview.js")))