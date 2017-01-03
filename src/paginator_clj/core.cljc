(ns paginator-clj.core
  (:require [clojure.string :as cl-str]))

;Parameters
; :records        : Total number of records
; :per-page       : Items shown per page
; :max-pages      : Maximum number of pagination links appear
; :current        : Current page number
; :biased         : :left or :right, if the number of pages shown is even, 
;                   current page should either sit in left half of right half
; :link-tpl       : template to use for individual links
; :list-tpl       : tempate to use for entire list

(defn paginate
  "Returns data requred to render paginator."
  [{:keys [records per-page max-pages current biased] :or {per-page 10 max-pages 10 current 1 biased :left}}]

  (let [total-pages (int (Math/ceil (/ records per-page)))
        half (Math/floor (/ max-pages 2))
        left-half  (int (if (= biased :left) (- half (if (odd? max-pages) 0 1)) half))
        right-half (int (if (= biased :right) (- half (if (odd? max-pages) 0 1)) half))
        virtual-start (- current left-half);can be a minus
        virtual-end (+ current right-half); can be exceeding than available page limit
        start (max 1 (- virtual-start (if (> virtual-end total-pages) (- virtual-end total-pages) 0)))
        end (inc (min total-pages (+ current (+ right-half (if (< virtual-start 1) (Math/abs (dec virtual-start)) 0)))))]
    {:current current :pages (range start end)}))

; todo: visible page number adjustment

; list template vars
; link template vars  
; :link-tpl
; :list-tpl
; :show-first?
; :show-last?
; :first-text
; :last-text
(defn html-paginator [{:keys [link-tpl list-tpl show-first? show-last? first-text last-text] 
                       :or {link-tpl "<li class=\"page-{{page-number}}-link\"><a href=\"{{location}}\">{{page-number}}</a></li>"
                            list-tpl "<ul class=\"paginator\">{{page-links}}</ul>"
                            show-first? true
                            show-last? true
                            first-text "&lt;&lt;"
                            last-text "&gt;&gt;"} :as all}] 
  (let [{:keys [current pages]} (paginate all)]
    (cl-str/replace list-tpl "{{page-links}}"
                    (reduce str (for [x pages] (cl-str/replace link-tpl "{{page-number}}" (str x)))))))
(html-paginator {:records 100 :per-page 10 :max-pages 10 :current 8 :biased :left})
