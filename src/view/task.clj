(ns view.task
  (:require
    [clojure.core.ex :refer :all]
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

(defn- attribute-componment [{:keys [start finish owner due status complete resume-time]}]
  "按照task-info信息填充任务属性。"
  [:div#task_attr
    [:div#task_attr_line
      [:div "| Owner"] [:div (str ": " owner)]
      [:div "| Due"] [:div (str ": " due)]]
    [:div#task_attr_line
      [:div "| Complete"] [:div (str ": " complete "%")]
      [:div "| Status"] [:div (str ": " status)]]
    [:div#task_attr_line
      [:div "| Start"] [:div (str ": " start)]
      [:div "| Finish"] [:div (str ": " finish)]]
    [:div {:id "task_attr_line"}
      [:div "| Resume"] [:div {:id "resume_div"} (str ": " 22)]]])

(defn- task-commands [tid]
  [:div#task_command_bar
    [:ul#nav
      [:li [:a {:href (str "#" tid)} "Task"]
        [:ul 
          [:li [:a {:href (str "/add_task?id=" tid)} "Add Sub Task"]]
          [:li
            [:a {:href (str "javascript:")} "Edit Task"]]
          [:li [:a {:href (str "/add_status?id=" tid)} "Delete Task"]]]]
      [:li [:a {:href "#"} "Comment"]
        [:ul
        [:li [:a {:href (str "javascript: description_layer(" tid ", '/add_comment?id=" tid "')")} "Add Comment"]]
        [:li [:a {:href (str "javascript: description_layer(" tid ", '/add_status?id=" tid "')")} "Add Status"]]
        [:li "--------------------------"]
        [:li [:a {:href (str "javascript: description_layer(0, '/add_record')")} "Add Global Record"]]
        ]]
      [:li [:a {:href (str "/report?id=" tid)}  "Report"]]
      [:li [:a {:href (str "/milestone")}  "Milestone"]]
      ]])

(defn- subtask-componment [subtasks]
    "填充子任务组件。"
    [:div#task_subtask_top
        [:table {:id "task_subtask_tbl"}
        [:thead
        [:tr [:th "No."] [:th "Title"] [:th "Owner"] [:th "Status"]]]
        (apply vector :tbody
            (for [{:keys [id title owner status]} subtasks]
                [:tr [:td id] [:td [:a {:href (str "/task?id=" id)} title]] [:td owner] [:td status]]))]])
      

(defn- comment-componment-history
  "按照comments显示历史comment信息"
  [{:keys [date content owner status complete]}]
  [:div {:id "task_comment_one"}
    [:div {:id "task_comment_ctrl"} 
      [:div date " by " owner]  [:div " Edit | Delete"] ]
    (when status
      [:div#task_comment_ctrl
        [:div "Status: " status " and complete: " complete "%"]])
    [:div {:id (if status "task_status_cnxt" "task_comment_cnxt")} content]])

(defn- comment-componment
  "按照comments信息填充注释组件。"
  [comments]
  [:div {:id "task_comment_top"}
    (apply vector :div 
          {:id "task_comment_history"}
          (map comment-componment-history comments))
    ; [:div {:id "task_comment_new"}
    ; [:textarea {:id "comment" :name "comment"}]
    ; [:br]
    ; [:input {:type "button" :id "comment" :name "comment" :value "comment"}]]
    ])

(defn page [tid]
  (let [
    task-info (into (db/read-task-status tid) (db/read-task tid))
    sub-tasks (db/read-sub-tasks tid)
    sub-tasks-status (->> sub-tasks (map :id) (map db/read-task-status) (map :status))
    sub-tasks-info (map #(assoc %1 :status %2) sub-tasks sub-tasks-status)
  ]
    (util/page
      [
        "js/jquery-3.2.1.min.js" 
        "dataTables/jquery.dataTables.min.js" 
        "js/tinymce/tinymce.min.js" 
        "layer/layer.js"
        "js/taskview.js"
      ]
      [
        "css/taskview.css" 
        "dataTables/jquery.dataTables.min.css"
        "css/nav_menu3.css"
      ]
      [:div#task_cnxt
        (title-componment task-info (db/read-ancestor-tasks tid))
        (task-commands tid)
        (attribute-componment task-info)
        (when (not-empty sub-tasks-info)
          (subtask-componment sub-tasks-info))
        (comment-componment (db/read-descriptions tid))
      ]
      )))