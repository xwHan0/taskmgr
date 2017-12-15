(ns taskmgr.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [database.core :as db]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [taskview.view :as taskview]
            [view.report.weekly :as weekly-report]
            [view.report.issue :as issue]
            [view.task :as task]
            [view.task-create :as task-create]
            [view.status-add :as status-add]
            [view.milestone-add :as milestone-add]
            [view.milestone :as milestone]))

(defroutes app-routes
  (GET "/" [] (task/page 0))
  
  (GET "/task" [id] (task/page (Integer/parseInt id)))
  (GET "/add_task" [id] (task-create/page (Integer/parseInt id)))
  (POST "/add_task" [id title owner due description] 
    (do
      (db/add-task {:pid (Integer/parseInt id) :title title :owner owner :due due :description description})
      (ring.util.response/redirect (str "/task?id=" id))))
  (GET "/edit_task" [id] (task-create/page (Integer/parseInt id)))
  (GET "/delete_task" [id])
  
  (GET "/add_status" [id] (status-add/page :tid (Integer/parseInt id) :status? true :href (str "add_status?id=" id)))
  (POST "/add_status" [id start finish status owner complete description]
    (db/add-status {:tid (Integer/parseInt id) :start start :finish finish :complete complete :status status :description description :owner owner}))

  (GET "/add_record" [id] (status-add/page :date? true :href (str "add_record?id=" id)))
  (GET "/add_record" [] (status-add/page :date? true :href "add_record"))
  (POST "/add_record" [id start finish owner description]
    (if-not (= "" id)
      (db/add-status {:id (Integer/parseInt id) :start start :finish finish :description description :owner owner})
      (db/add-status {:start start :finish finish :description description :owner owner})))

  (GET "/add_comment" [id] (status-add/page :tid id :href (str "add_comment?id=" id)))
  (POST "/add_comment" [id start finish owner description]
    (db/add-status {:tid (Integer/parseInt id) :start start :finish finish :description description :owner owner}))
  (GET "/edit_comment" [id] (status-add/page :cid id :href (str "edit_comment?id=" id)))
  (POST "/edit_comment" [id owner description]
    (db/update-description :id (Integer/parseInt id) :owner owner :description description))
  (GET "/delete_comment" [id] (db/delete-description id))

  
  (GET "/report_plan" [id date] (weekly-report/page (Integer/parseInt id) date))
  (GET "/report_issue" [id date] (issue/page (Integer/parseInt id) date))

  (GET "/add_milestone" [id] (milestone-add/page (Integer/parseInt id)))
  (POST "/add_milestone" [id title type class start finish description] 
        (db/add-milestone {:tid (Integer/parseInt id) :title title :type type :class class :start start :finish finish :description description}))
  (GET "/milestone" [id] (milestone/page (Integer/parseInt id)))

  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
