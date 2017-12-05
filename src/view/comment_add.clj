(ns view.comment-add
  (:require
    [clojure.core.ex :refer :all]
    [database.core :as db]))

(defn comment-add-component [tid {:keys [owner status complete start end content]}]
  "按照任务集成列表生成继承链接。parent-task-vector格式为[{:title :id}]"
  (let []
    [:form {:method "post" :action (str "/add_comment?id=" tid)}
      [:div
        "Status: "
        [input#status {:type "text" :name "status" :value status}]
        "Complete: "
        [input#complete {:type "number" :name "complete" :value complete}]
        "Owner: "
        [input#owner {:type "text" :name "owner" :value owner}]]
      [:div
        "Start: "
        [input#start {:type "datetime" :name "start" :value start}]
        "Complete: "
        [input#end {:type "datetime" :name "end" :value end}]]
      [:div#task_comment_new
        [:textarea#content {:name "content"} content]]
      [:input {:type "submit" :value "comment"}]))

