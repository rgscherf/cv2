(ns cv2.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.pprint :refer [pprint]]
            [clojure.java.io :as io]
            [cheshire.core :as c]
            [clj-http.client :as http]
            [cv2.user :refer [assemble-user-map]]
            [cv2.index :refer [render-page]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn user-data
  []
  (-> "data.json"
      (io/resource)
      (slurp)
      (c/parse-string true)))

(defn commit-data
  []
  (->> "rgscherf"
    (assemble-user-map)
    (:commits)
    (take 6)
    (apply vector)))

(defn create-payload
  "retrieve user data for template"
  []
  (assoc (user-data) :commits (commit-data)))

(defroutes app-routes
  (GET "/usermap" [] (c/generate-string (assemble-user-map "rgscherf")))
  (GET "/" [] (render-page (create-payload)))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
