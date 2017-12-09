(ns database.core
  (:require 
    [clojure.java.jdbc :refer :all]
    [clj-time.core :as t]
    [clj-time.format :as tf]
    ))

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
    sql (str "SELECT x.id, x.title, x.due, y.owner, y.content, y.finish 
              FROM tasks x left outer join descriptions y on x.id=y.tid 
              WHERE x.id = " tid)
    items (query db [sql])
    ]
    (first items)))

(defn read-task-status [tid & date]
  (let [
    date (first date)
    sql (str "SELECT x.complete, x.status, y.content, y.owner, y.start, y.finish FROM status x, descriptions y WHERE x.cid = y.id and x.tid = " tid " ")
    sql (str sql (if date (str "and datetime(y.finish)<=datetime('" date "') ") " "))
    sql (str sql "order by y.finish")
    items (query db [sql])]
    (-> items last)))

(defn read-sub-tasks [tid]
  (let [
    sql (str "SELECT x.id,x.title,x.due,y.owner
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

(defn read-descriptions [tid & cid]
  (let [
    cid (first cid)
    sql (str "SELECT x.start,x.finish,x.owner,x.content,y.complete,y.status 
              FROM descriptions x left outer join status y on x.id=y.cid 
              WHERE x.tid=" tid (if cid (str " cid=" cid) "") " "
              " ORDER by x.finish")
  ]
    (if (and (nil? tid) (nil? cid))
      {}
      (query db [sql]))))

(defn add-task [{:keys [pid type due title owner start finish description] :as task}]
  (let [tid (insert-db db :tasks {:due due :type type :title title :pid pid})]
    (when (or owner description)
      (let [did (insert-db db :descriptions {:tid tid :owner owner :start start :finish finish :content description})]
        (update! db :tasks {:cid did} ["id = ?" tid])))
    "Add success!"))
 
(defn add-comment [{:keys [tid owner status complete start end content] :as desc}]
  (let [did (insert-db db :descriptions desc)]
    (when (or status complete)
      (insert-db db :status {:tid tid :cid did :status status :complete complete}))))
    
(defn add-status [{:keys [tid start finish owner complete status description] :as in}]
  (let [
    tid (cond (nil? tid) nil (zero? tid) nil :else tid)
    finish (if finish finish 
      (tf/unparse (tf/formatter "yyyy-MM-dd") (t/to-time-zone (t/now) (t/default-time-zone))))
    did (insert-db db :descriptions {:tid tid :start start :finish finish :owner owner :content description})
    ]
    (cond
      (and tid (or complete status))
        (do (insert-db db :status {:tid tid :cid did :complete complete :status status}) "Add status success!")
      :else "Add record success!")))

