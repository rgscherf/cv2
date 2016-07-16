(ns cv2.index
  (:require [hiccup.core :as h]))

(defn experience-snippet
  [experience]
  [:div.experienceCard
    [:div.fontAwesomeStack {:style "width:52px;text-align:center;"}
      [:i {:aria-hidden "true" :class (str "fa fa-2x experienceIcon " (:fontawesome_pointer experience))}]]
    [:div.experienceDescription
      [:div (:time experience)]
      [:div (:title experience)]
      [:div (:description experience)]]])

(defn skill-legend-snippet
  [[n title]]
  [:div
    [:div (repeat n [:i.fa.fa-cube.fa {:aria-hidden "true"}])]
    [:div.arvo title]])

(defn skill-snippet
  [width skill]
  [:div.skillSide 
    [:div.skillName {:style (str "width:" width "px;")} (:name skill)]
    [:div.skillLevel (repeat (:level skill) [:i.experienceIcon.fa.fa-cube.fa-2x {:aria-hidden "true"}])]])

(defn commit-snippet
  [commit]
  [:div.commitCard
    [:div.commitMessage (:message commit)]
    [:div.commitInfoBlock
      [:div
        [:a {:href (:commit_url commit)} (:sha commit)]
        " to "
        [:a.commitLink {:href (:repo_url commit)} (:repo_name commit)]]
      [:div (:timestamp_pretty commit)]]])

(defn contact-snippet
  [contact]
  [:div
    [:a {:href (:link contact)}
      [:i {:aria-hidden "true"
           :class (str "fa fa-3x experienceIcon fa-" (:fontawesome_pointer contact))}]]])

(defn render-page
  [data]
  (def *in data)
  (h/html
    [:head 
      [:title "rgscherf-cv"]
      [:link {:rel "icon" :type "image/png" :href "favicon-bw.png"}] 
      [:link {:rel "stylesheet" :href "style.css"}] 
      [:link {:rel "stylesheet" :href "https://fonts.googleapis.com/css?family=Arvo|Bungee+Shade|Lobster+Two|VT323|Open+Sans|Roboto+Mono"}]
      [:script {:type "text/javascript" :src "https://use.fontawesome.com/3214b7792e.js"}]]
    [:body
      [:div#contentWrapper
        [:div#headerWrapper
          [:div#headline (:name data)]
          [:div#subline (:blurb data)]]
        [:div#experience
          [:div.sectionHeader "Education & Experience"]
          [:div#experienceCards.sectionBody.basicFlex
            (map experience-snippet (:experience data))]]
        [:div#skills
          [:div.sectionHeader
            [:div#skillsHeaderWrapper
              [:div "Skills"]
              [:div
                [:div#skillsLegend
                  (map skill-legend-snippet [[4 "Expert"] [3 "Advanced"] [2 "Experienced"]])]]]]
          [:div.sectionBody
            [:div.skillWrapper {:style "margin-left:50px;"}
              [:div (map (partial skill-snippet 210) (:skillsLeft data) )]
              [:div (map (partial skill-snippet 110) (:skillsRight data) )]]]]
        [:div#projects
          [:div.sectionHeader {:style "margin-bottom: 20px;"} "Selected Projects"]
          [:div.sectionBody
            [:table#projectTable
              [:tr
                [:td 
                  [:a {:href "http://gainful.work"}
                    [:div#projectLogoGainful.projectLogoContainer
                      [:span#projectSpanGainful "Gainful"]]]]
                [:td.projectTableSpacer]
                [:td
                  [:div "Simple, sane aggregation for public service job postings."]
                  [:div "Django / BeautifulSoup / Elm"]]]
              [:tr
                [:td
                  [:a {:href "http://twitbackend.herokuapp.com"}
                    [:div#projectLogoTwit.projectLogoContainer
                      [:div#projectSpanTwit "TWIT"]]]]
                [:td.projectTableSpacer]
                [:td
                  [:div "Github commit timeline and explorer."]
                  [:div "ExpressJS / React"]]]
              [:tr
                [:td
                  [:a {:href "https://rgscherf.itch.io/potion-lord"}
                    [:div#projectLogoLord.projectLogoContainer
                      [:span#projectSpanLord "POTION LORD"]]]]
                [:td.projectTableSpacer]
                [:td
                  [:div "Award-winning retro action game made in one weekend."]
                  [:div "Unity / C#"]]]]]]
        [:div#commits
          [:div.sectionHeader "Latest Commits"]
          [:div.sectionBody.basicFlex {:style "align-items:flex-end;"}
            (map commit-snippet (:commits data))]]
        [:div#contact
          [:div.sectionHeader "Contact"]
          [:div#contactCards.sectionBody.basicFlex
            (map contact-snippet (:contact data))]]]]))

