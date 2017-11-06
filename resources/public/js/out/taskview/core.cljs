(ns taskview.core
  (:require [reagent.core :as r]))

(defonce resume-time (r/atom 0))
(defonce resume-status (r/atom :pause))
(defonce resume-handle (r/atom 0))

(defn run-resume []
  (js/setInterval #(swap! resume-time inc) 1000))

(defn pause-resume []
  (js/clearInterval @resume-handle))

(defn resume-componment []
  [:div 
   {:on-click #(if (= :pause @resume-status)
                 (do 
                   (reset! resume-status :count)
                   (reset! resume-handle (run-resume)))
                 (do
                   (reset! resume-status :pause)
                   (pause-resume)))}
   (str ": " @resume-time)])

(r/render-component [resume-componment]
          (.getElementById js/document "resume_div"))