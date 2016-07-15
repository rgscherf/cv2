(ns cv2.handler
  (:require [compojure.core :refer :all]
            [cv2.user :refer [assemble-user-map]]
            [cv2.index :refer [render-page]]
            [clojure.pprint :refer [pprint]]
            [cheshire.core :as c]
            [compojure.route :as route]
            [clj-http.client :as http]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn user-commits
  "retrieve user's recent commits from external API"
  [as-string]
  (let [res (-> "http://twitbackend.herokuapp.com/user?user=rgscherf"
              (http/get)
              (:body)
              (c/parse-string true)
              (:commits))]
    (if as-string
      (c/generate-string res)
      res )))

(defn create-payload
  "retrieve user data for template"
  [debug]
  (let [data (-> "resources/data.json"
                 (slurp)
                 (c/parse-string true))
        commits (take 6 (user-commits false))
        full-data (assoc data :commits commits)]
    (if debug
      (c/generate-string full-data)
      full-data)))

(defroutes app-routes
  (GET "/usermap" [] (c/generate-string (assemble-user-map "rgscherf")))
  (GET "/tell" [] (create-payload true))
  (GET "/" [] (render-page (create-payload false)))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
