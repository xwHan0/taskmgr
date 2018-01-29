(ns util.command)
(require '[database.core :as db])

(defn command-parameter [cmd id]
  (let []
    (conj {:cmd cmd}
      (cond 
        (= cmd :ADD-TASK) 
            {:tid id :status? false :date? false :owner? true  :content? false :title? true :href (str "/add_task?id=" id) }
        (= cmd :EDT-TASK) 
            {:tid id :status? false :date? false :owner? true  :content? false :title? true  :href (str "/edit_task?id=" id)}
        (= cmd :ADD-COMT) 
            {:tid id :status? true  :date? true  :owner? true  :content? true  :title? false :href (str "/add_comment?id=" id)}
        (= cmd :EDT-COMT) 
            {:tid id :status? true  :date? true  :owner? true  :content? true  :title? false :href (str "/edit_comment?id=" id)}
        (= cmd :ADD-STAT) 
            {:tid id :status? true  :date? false :owner? false :content? true  :title? false :href (str "/add_status?id=" id)}
        (= cmd :ADD-RECD) 
            {:tid id :status? false :date? true  :owner? true  :content? true  :title? false :href (str "/add_record?id=" id)}
        (= cmd :ADD-TASK) 
            {:tid id :status? false :date? false :owner? false :content? false :title? false :href (str "/add_task?id=" id)}      
        ))))

(defn command-button [cmd id label]
  (let [{:keys [href]} (command-parameter cmd id)]
    [:a {:href (str "javascript: description_layer(" id ", '" href "')")} label]))

(defn read [cmd id]
  (cond 
    (= cmd :EDT-TASK) (db/read-task id)
    (= cmd :EDT-COMT) (db/read-description id)
    :else {:content "ERROR!"}))