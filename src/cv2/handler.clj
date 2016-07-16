(ns cv2.handler
  (:require [compojure.core :refer :all]
            [spyscope.core :refer :all]
            [cv2.user :refer [assemble-user-map]]
            [cv2.index :refer [render-page]]
            [clojure.pprint :refer [pprint]]
            [cheshire.core :as c]
            [compojure.route :as route]
            [clj-http.client :as http]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn user-data
  []
  (-> "resources/data.json"
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
