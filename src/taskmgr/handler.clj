(ns taskmgr.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [database.core :as db]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [taskview.view :as taskview]
            [view.weekly-report :as weekly-report]
            [view.task :as task]
            [view.task-create :as task-create]
            [view.status-add :as status-add]
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
  
  (GET "/add_status" [id] (status-add/page (Integer/parseInt id)))
  (POST "/add_status" [id start finish status owner complete description]
    (do 
      (db/add-status {:tid (Integer/parseInt id) :start start :finish finish :complete complete :status status :description description :owner owner})
      #_(ring.util.response/redirect (str "/task?id=" id))))

  (GET "/add_comment" [] (db/add-comment {:tid 4 :type 0 :content "Initial version."}))
  (GET "/read_task_status" [] (db/read-task-status 4 "2018-01-01"))
  
  (GET "/report" [id date] (weekly-report/page (Integer/parseInt id) date))
  (GET "/milestone" [] (milestone/page))

  ;Test
  (GET "/test000" [] (str (db/read-ancestor-tasks 8)))

  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
