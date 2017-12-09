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
  
  (GET "/add_status" [id] (status-add/page :tid (Integer/parseInt id) :status? true :href (str "add_status?id=" id)))
  (POST "/add_status" [id start finish status owner complete description]
    (db/add-status {:tid (Integer/parseInt id) :start start :finish finish :complete complete :status status :description description :owner owner}))

  (GET "/add_record" [id] (status-add/page :date? true :href (str "add_record?id=" id)))
  (GET "/add_record" [] (status-add/page :date? true :href "add_record"))
  (POST "/add_record" [id start finish owner description]
    (if id
      (db/add-status {:id (Integer/parseInt id) :start start :finish finish :description description :owner owner})
      (db/add-status {:start start :finish finish :description description :owner owner})))

  (GET "/add_comment" [id] (status-add/page :tid id :href (str "add_comment?id=" id)))
  (POST "/add_comment" [id start finish owner description]
    (db/add-status {:tid (Integer/parseInt id) :start start :finish finish :description description :owner owner}))


  ;(GET "/add_comment" [] (db/add-comment {:tid 4 :type 0 :content "Initial version."}))
  (GET "/read_task_status" [] (db/read-task-status 4 "2018-01-01"))
  
  (GET "/report" [id date] (weekly-report/page (Integer/parseInt id) date))
  (GET "/milestone" [id] (milestone/page (Integer/parseInt id)))

  ;Test
  (GET "/test000" [] (str (db/read-ancestor-tasks 8)))

  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
