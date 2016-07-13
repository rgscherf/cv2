(ns cv2.handler
  (:require [compojure.core :refer :all]
            [cheshire.core :as c]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn headerize
  "wrap HTTP response with status and headers.
  param package: [operation-successful message-to-send]"
  [package]
  (let [[err message] package
        head {"Content-Type" "application/json; charset=utf-8"}]
    (if (not err)
      {:status 400
       :headers head}
      {:status 200
       :headers head
       :body message})))

(defn jsonize
  "generate string from jsonizable message
  param message [operation-successful message-to-send]"
  [message]
  (-> message
      (headerize)
      (c/generate-string)))

(defroutes app-routes
  (GET "/tell" [] (jsonize [true "hello, how is everyone?"]))
  (GET "/" [] "Hello World")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
