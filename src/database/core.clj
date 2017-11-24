(ns database.core
  (:require [clojure.java.jdbc :refer :all]))

;
(def generated-key (keyword "last_insert_rowid()"))

; (def db {:connection-uri "jdbc:sqlite:resources/database/tmgr.sqlite"})

(def db
  {
    :classname   "org.sqlite.JDBC"
    :subprotocol "sqlite"
    :subname     "resources/database/tmgr.sqlite"
  })  

(defn read-task [tid]
  (let [items (query db ["SELECT * FROM tasks WHERE id = ?" tid])]
    (first items)))

(defn read-task-status [tid]
  (let [items (query db ["SELECT * FROM descriptions WHERE tid = ?" tid])]
    (-> items last :content)))

(defn read-sub-tasks [tid]
  (query db ["SELECT * FROM tasks WHERE pid = ?" tid]))

(defn add-task [{:keys [pid status due title] :as task}]
  (insert! db :tasks task))

(defn add-comment [{:keys [tid type owner content duration] :as desc}]
  (let [description (insert! db :descriptions desc)
        did (generated-key (first description))
        comment (insert! db :comments {:tid tid :cid did :type 0})
        cid (generated-key (first comment))]
    ; (update! db :tasks {:cid cid} ["id = ?" tid])
    ))