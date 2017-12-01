(ns database.core
  (:require [clojure.java.jdbc :refer :all]))

;
(def generated-key (keyword "last_insert_rowid()"))
(defn insert-db [db table content]
  (generated-key (first (insert! db table content))))

; (def db {:connection-uri "jdbc:sqlite:resources/database/tmgr.sqlite"})  ;For sqlite-jdbc "3.15.1"

(def db
  {
    :classname   "org.sqlite.JDBC"
    :subprotocol "sqlite"
    :subname     "resources/database/tmgr.sqlite"
  })  

(defn read-task [tid]
  (let [
    sql (str "SELECT x.id, x.title, x.due, y.owner, y.content, y.date 
              FROM tasks x left outer join descriptions y on x.id=y.tid 
              WHERE x.id = " tid)
    items (query db [sql])
    ]
    (first items)))

(defn read-task-status [tid & date]
  (let [
    ; date (if date date "('now')")
    sql (str "SELECT x.complete, x.status, y.content FROM status x, descriptions y WHERE x.cid = y.id and x.tid = " tid " ")
    sql (str sql (if date (str "and datetime(y.date)<=datetime" date " ") " "))
    sql (str sql "order by y.date")
    items (query db [sql])]
    (-> items last)))

(defn read-sub-tasks [tid]
  (let [
    sql (str "SELECT x.id,x.title,x.due,y.owner,y.date 
              FROM tasks x left outer join descriptions y on x.cid=y.id 
              WHERE x.pid=" tid)
    ]
    (query db [sql])))

(defn read-ancestor-tasks [tid]
  (if (zero? tid)
    []
    (let [{:keys [title pid]} (first (query db ["SELECT title,pid FROM tasks WHERE id=?" tid]))]
      (if (zero? pid)
        [{:title title :id tid}]
        (conj (read-ancestor-tasks pid) {:title title :id tid})))))

(defn add-task [{:keys [pid owner due title description] :as task}]
  (let [tid (insert-db db :tasks {:due due :title title :pid pid})]
    (when (or owner description)
      (let [did (insert-db db :descriptions {:tid tid :owner owner :content description})]
        (update! db :tasks {:cid did} ["id = ?" tid])))
    "Add success!"))
 
(defn add-comment [{:keys [tid type owner content duration] :as desc}]
  (let [description (insert! db :descriptions desc)
        did (generated-key (first description))
        comment (insert! db :comments {:tid tid :cid did :type 0})
        cid (generated-key (first comment))]
    ; (update! db :tasks {:cid cid} ["id = ?" tid])
    ))
    
(defn add-status [{:keys [tid complete status description]}]
  (let [did (insert-db db :descriptions {:tid tid :content description})
        sid (insert-db db :status {:tid tid :cid did :complete complete :status status})]
    "Add success!"))