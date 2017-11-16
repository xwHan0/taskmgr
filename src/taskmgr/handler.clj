(ns taskmgr.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [database.core :as db]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [taskview.view :as taskview]))

(defroutes app-routes
  (GET "/" [] (taskview/task-view {:title "Task1" :start-date "1981/03/21" :finish-date "2099/12/12"
                                   :resume-time 23}))
  (GET "/test" [] (db/test))
  (GET "test2" [] (db/test2))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
