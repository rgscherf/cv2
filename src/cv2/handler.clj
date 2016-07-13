(ns cv2.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [environ "1.0.0"]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
