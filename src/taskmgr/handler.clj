(ns taskmgr.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [database.core :as db]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [taskview.view :as taskview]
            [view.weekly-report :as weekly-report]
            [view.task :as task]
            [view.task-create :as task-create]
            [view.milestone :as milestone]))

(defroutes app-routes
  (GET "/" [] (task/page 0))
  
  (GET "/task" [id] (task/page (Integer/parseInt id)))
  (GET "/add_task" [id] (task-create/page id))
  (POST "/add_task" [id title owner due description] 
    (do
      (db/add-task {:pid (Integer/parseInt id) :title title :owner owner :due due :description description})
      (ring.util.response/redirect (str "/task?id=" id))))
  (GET "/edit_task" [id] (task-create/page id))
  (GET "/delete_task" [id])
  
  (GET "/add_status" [] (db/add-status {:tid 9 :complete 100 :status "Green" :description "Initial version has done."}))
  (GET "/add_comment" [] (db/add-comment {:tid 4 :type 0 :content "Initial version."}))
  (GET "/read_task_status" [] (db/read-task-status 4 "2018-01-01"))
  
  (GET "/report" [id] (weekly-report/page (IntegerInt id) "2018-01-01"))
  (GET "/milestone" [] (milestone/page))

  ;Test
  (GET "/test000" [] (str (db/read-ancestor-tasks 8)))

  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
