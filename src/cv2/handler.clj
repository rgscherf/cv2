(ns cv2.handler
  (:require [compojure.core :refer :all]
            [clojure.pprint :refer [pprint]]
            [cheshire.core :as c]
            [compojure.route :as route]
            [selmer.parser :refer [render-file]]
            [clj-http.client :as http]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

; (defn headerize
;   "wrap HTTP response with status and headers.
;   param package: [operation-successful message-to-send]"
;   [package]
;   (let [[err message] package
;         head {"Content-Type" "application/json; charset=utf-8"}]
;     (if (not err)
;       {:status 400
;        :headers head}
;       {:status 200
;        :headers head
;        :body message})))

; (defn jsonize
;   "generate string from jsonizable message
;   param message [operation-successful message-to-send]"
;   [message]
;   (-> message
;       (headerize)
;       (c/generate-string)))

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
  (let [data (-> "data.json"
                 (slurp)
                 (c/parse-string true))
        commits (user-commits false)
        full-data (assoc data :commits commits)]
    (if debug
      (c/generate-string full-data)
      full-data)))

(defroutes app-routes
  (GET "/tell" [] (create-payload true))
  (GET "/" [] "Hello World")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
