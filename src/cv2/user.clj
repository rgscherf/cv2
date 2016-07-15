(ns cv2.user
  (:require [clj-http.client :as http]
            [cheshire.core :as c]))

(defn user-url [user] (str "https://api.github.com/users/" user))
(defn commit-url [user] (str "https://api.github.com/users/" user "/events"))
(def api-options {:headers {"Authorization" "token 68622003dbbabc59e105f357be2390c3ab418087"
                            "User-Agent" "rgscherf/cv2"}})

(defn get-response
  "Get response from API. Returns string body"
  [url-string]
  (let [response (http/get url-string)]
    (if (= (:status response) 200)
      (:body response)
      nil)))

(defn parse-response 
  "Convert string body into map"
  [resp-body] 
  (if (nil? resp-body)
    nil
    (c/parse-string resp-body true)))

(defn make-response-map
  "Convenience function: URL string -> response map."
  [url]
  (-> url
      (get-response)
      (parse-response)))

(defn basic-user-info
  "Github user name -> map of relevant user fields"
  [user-string]
  (if-let [user (make-response-map (user-url user-string))]
    {:avatar_url (:avatar_url user)
     :html_url (:html_url user)
     :login (:login user)
     :name (:name user)
     :public_repos (:public_repos user)}
    nil))

(defn shape-single-commit
  "Pull relevant fields from map of single commit"
  [c]
  ; TODO: repo_name
  ; TODO: repo_url
  ; TODO: timestamp from commit
  ; TODO: pretty timestamps
  {:message (:message c)
   :repo_url (:url c)
   :commit_url (:url c)
   :sha (apply str (take 7 (:sha c)))
   :timestamp_raw (:created_at c)})

(defn make-commit-map
  "Github user name -> sorted list of commits"
  [user]
  (->> (make-response-map (commit-url user))
       (filter #(= (:type %) "PushEvent"))
       (map #(get-in % [:payload :commits]))
       (flatten)
       (map shape-single-commit)))
      ; ... then sort by date

(defn add-commits
  "Intermediary function to assoc commits to user map"
  [user-map]
  (if (nil? user-map)
    nil
    (assoc user-map :commits (make-commit-map (:login user-map)))))

(defn assemble-user-map
  "Github user name -> user + commits map"
  [user-name]
  (-> user-name
      (basic-user-info)
      (add-commits)))