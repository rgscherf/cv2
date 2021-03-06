(defproject cv2 "0.1.4-static-files-SNAPSHOT"
  :description "Source for http://scherf.works"
  :url "https://github.com/rgscherf/cv2"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [cheshire "5.4.0"]
                 [spyscope "0.1.5"]
                 [clj-time "0.12.0"]
                 [clj-http "2.2.0"]
                 [hiccup "1.0.5"]
                 [compojure "1.5.1"]
                 [ring/ring-defaults "0.2.1"]]
  :plugins [[lein-beanstalk "0.2.7"]
            [lein-ring "0.9.7"]]
  :ring {:handler cv2.handler/app}
  :main cv2.handler
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}})
