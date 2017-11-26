(ns taskmgr.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [database.core :as db]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [taskview.view :as taskview]
            [view.weekly-report :as weekly-report]
            [view.milestone :as milestone]))

(defroutes app-routes
  (GET "/" [] (taskview/task-view {:title "Task1" :start-date "1981/03/21" :finish-date "2099/12/12"
                                   :resume-time 23}))
  (GET "/add_task" [] (db/add-task {:pid 4 :title "CCS FS" :owner "欧阳帆"}))
  (GET "/add_status" [] (db/add-status {:tid 9 :complete 100 :status "Green" :description "Initial version has done."}))
  (GET "/add_comment" [] (db/add-comment {:tid 4 :type 0 :content "Initial version."}))
  (GET "/read_task_status" [] (db/read-task-status 4 "2018-01-01"))
  (GET "/report" [] (weekly-report/page 4 "2018-01-01"))
  (GET "/milestone" [] (milestone/page))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
