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
    sql (str "SELECT x.id, x.title, x.owner, y.content, y.finish 
              FROM tasks x left outer join descriptions y on x.id=y.tid 
              WHERE x.id = " tid)
    items (query db [sql])
    ]
    (first items)))

(defn update-task [{:keys [tid title owner]}]
  (execute! db ["UPDATE tasks SET title=?, owner=? WHERE id=?" title owner tid])
  "Update Success!")

(defn read-task-status [tid & date]
  (let [
    date (first date)
    sql (str "SELECT x.complete, x.status, y.content, y.owner, y.start, y.finish FROM status x, descriptions y WHERE x.cid = y.id and x.tid = " tid " ")
    sql (str sql (if date (str "and datetime(y.finish)<=datetime('" date "') ") " "))
    sql (str sql "order by y.finish ASC")
    items (query db [sql])]
    (-> items last)))

(defn read-task-statuses [tid]
  (let [
    sql (str "SELECT x.complete, x.status, y.content, y.owner, y.start, y.finish FROM status x, descriptions y WHERE x.cid = y.id and x.tid = " tid " ")
    sql (str sql "order by y.finish ASC")
    items (query db [sql])]
    items))


(defn read-sub-tasks [tid]
  (let [
    ; sql (str "SELECT x.id,x.title,y.owner
    ;           FROM relations z,tasks x left outer join descriptions y on x.cid=y.id
    ;           WHERE z.tid=x.id z.pid=" tid)
    sql (str "SELECT x.id,x.title,x.owner
              FROM tasks x, relations z 
              WHERE x.id=z.tid and z.pid=" tid)
    ]
    (query db [sql])))

(defn read-ancestor-tasks [tid]
  (if (zero? tid)
    []
    (let [{:keys [title pid]} (first (query db ["SELECT x.title,y.pid 
                                                 FROM tasks x, relations y 
                                                 WHERE x.id=y.tid and x.id=?" tid]))]
      (if (zero? pid)
        [{:title title :id tid}]
        (conj (read-ancestor-tasks pid) {:title title :id tid})))))

(defn read-descriptions [& {:keys [tid cid]}]
  (let [
    sql (str "SELECT x.id,x.start,x.finish,x.owner,x.content,y.complete,y.status 
              FROM descriptions x left outer join status y on x.id=y.cid 
              WHERE " 
              (if tid (str "x.tid=" tid " ")) 
              (if cid (str "x.id=" cid " ")) 
              " ORDER by x.finish")
  ]
    (if (and (nil? tid) (nil? cid))
      {}
      (query db [sql]))))



(defn add-task [{:keys [pid due title owner] :as task}]
  (let [tid (insert-db db :tasks {:title title :owner owner})]
    (insert-db db :relations {:tid tid :pid pid :type "subtask"})
    "Add success!"))

(defn delete-task [tid pid]
  (let []
    (execute! db ["DELETE FROM tasks WHERE id=?" tid])
    (execute! db ["DELETE FROM relations WHERE tid=?" tid])
    (execute! db ["UPDATE relations SET pid=?" pid " WHERE pid=?" tid])
    ))
 
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

(defn add-milestone [{:keys [tid title type class start finish description]}]
  (let [did (insert-db db :descriptions {:tid tid :start start :finish finish :content description})]
    (insert-db db :milestones {:tid tid :cid did :title title :type type :class class})
    "Add milestone success!"))

(defn read-milestones [tid]
  (let [sql (str "SELECT x.title,x.type,x.class,y.start,y.finish,y.content 
          FROM milestones x, descriptions y 
          WHERE x.tid=" tid " and x.cid=y.id 
          ORDER by y.finish")]
    (query db [sql])))
    
(defn update-description [& {:keys [id owner start finish description]}]
  (let [sql (str "UPDATE descriptions SET "
         "owner=" owner ","
         "content=" description " "
         (when start (str ", start=" start))
         (when finish (str ", finish=" finish))
         " WHERE id=" id)]
    (execute! db [sql])
    "Update description success!"))
    
(defn delete-description [id]
  (let [sql (str "DELETE FROM descriptions WHERE id=" id)]
    (execute! db ["DELETE FROM status WHERE cid=?" id])
    (execute! db [sql])))