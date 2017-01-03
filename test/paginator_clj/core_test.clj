; Author - Tharaka Manawardhana
; E-mail: manawardhana@bcs.org
; (c) Tharaka Manawardhana

(ns paginator-clj.core-test
  (:require [clojure.test :refer :all]
            [paginator-clj.core :refer :all]))

;Parameters
; :records        : Total number of records
; :per-page       : Items shown per page
; :max-pages      : Maximum number of pagination links appear
; :current        : Current page number
; :biased         : :left or :right, if the number of pages shown is even, 
;                   current page should either sit in left half of right half
; :link-tpl       : template to use for individual links
; :list-tpl       : tempate to use for entire list


(deftest pagination
;_________________________________
; Happy Case 
;---------------------------------
  (testing "Happy case - first page selected"
    (do
      (def data1 (paginate {:records 100 :per-page 10 :max-pages 10 :current 1 :biased :left}))
      (is (= (data1 :current), 1))
      (is (data1 :pages) (range 1 11)))) ;[1 2 3 4 5 6 7 8 9 10]

  (testing "Happy case - last page selected"
    (do
      (def data2 (paginate {:records 100 :per-page 10 :max-pages 10 :current 10 :biased :left}))
      (is (= (data2 :current), 10))
      (is (data2 :pages) (range 1 11)))) ;[1 2 3 4 5 6 7 8 9 10]

  (testing "Happy case - middle left biased random page selected"
    (do
      (def data3 (paginate {:records 100 :per-page 10 :max-pages 10 :current 3 :biased :left}))
      (is (= (data3 :current), 3))
      (is (data3 :pages) (range 1 11)))) ;[1 2 3 4 5 6 7 8 9 10]

  (testing "Happy case - middle right biases random page selected"
    (do
      (def data4 (paginate {:records 100 :per-page 10 :max-pages 10 :current 8 :biased :left}))
      (is (= (data4 :current), 8))
      (is (data4 :pages) (range 1 11)))) ;[1 2 3 4 5 6 7 8 9 10]

;_________________________________
; Less Pages
;---------------------------------
  (testing "Less pages case - 1st page selected"
    (do
      (def data5 (paginate {:records 51 :per-page 10 :max-pages 10 :current 1 :biased :left}))
      (is (= (data5 :current), 1))
      (is (data5 :pages) (range 1 7)))) ;[1 2 3 4 5 6]

  (testing "Less pages case - last page selected"
    (do
      (def data6 (paginate {:records 57 :per-page 10 :max-pages 10 :current 6 :biased :left}))
      (is (= (data6 :current), 6))
      (is (data6 :pages) (range 1 7)))) ;[1 2 3 4 5 6]

  (testing "Less pages case - left biased middle page selected"
    (do
      (def data7 (paginate {:records 65 :per-page 10 :max-pages 10 :current 3 :biased :left}))
      (is (= (data7 :current), 3))
      (is (data7 :pages) (range 1 8)))) ;[1 2 3 4 5 6 7]

  (testing "Less pages case - right biased middle page selected"
    (do
      (def data8 (paginate {:records 66 :per-page 10 :max-pages 10 :current 5 :biased :left}))
      (is (= (data8 :current), 5))
      (is (data8 :pages) (range 1 8)))) ;[1 2 3 4 5 6 7] 

;_________________________________
; More Pages
;---------------------------------
  (testing "More pages case - 1st page selected"
    (do
      (def data9 (paginate {:records 300 :per-page 10 :max-pages 10 :current 1 :biased :left}))
      (is (= (data9 :current), 1))
      (is (data9 :pages) (range 1 11)))) ;[1 2 3 4 5 6 7 8 9 10]

  (testing "More pages case - middle page selected - left biased"
    (do
      (def data10 (paginate {:records 300 :per-page 10 :max-pages 10 :current 15 :biased :left}))
      (is (= (data10 :current), 15))
      (is (data10 :pages) (range 11 21)))) ;[11 12 13 14 15 16 17 18 19 20]

  (testing "More pages case - middle page selected - right biased"
    (do
      (def data11 (paginate {:records 300 :per-page 10 :max-pages 10 :current 15 :biased :right}))
      (is (= (data11 :current), 15))
      (is (data11 :pages) (range 10 20))));[10 11 12 13 14 15 16 17 18 19]

  (testing "More pages case - middle page selected - left biased - odd number of pages"
    (do
      (def data12 (paginate {:records 300 :per-page 10 :max-pages 11 :current 15 :biased :left}))
      (is (= (data12 :current), 15))
      (is (data12 :pages) (range 10 21))));[10 11 12 13 14 15 16 17 18 19 20]

  (testing "More pages case - middle page selected - right biased - odd number of pages"
    (do
      (def data13 (paginate {:records 300 :per-page 10 :max-pages 11 :current 15 :biased :right}))
      (is (= (data13 :current), 15))
      (is (data13 :pages) (range 10 21))));[10 11 12 13 14 15 16 17 18 19 20]

  (testing "More pages case - left biased last page selected"
    (do
      (def data14 (paginate {:records 300 :per-page 10 :max-pages 10 :current 30 :biased :left}))
      (is (= (data14 :current), 30))
      (is (data14 :pages) (range 21 31))));[21 22 23 24 25 26 27 28 29 30]

  (testing "More pages case - right biased last page selected"
    (do
      (def data15 (paginate {:records 300 :per-page 10 :max-pages 10 :current 30 :biased :right}))
      (is (= (data15 :current), 30))
      (is (data15 :pages) (range 21 31))));[21 22 23 24 25 26 27 28 29 30]

  (testing "More pages case - right biased random page selected from last set"
    (do
      (def data16 (paginate {:records 300 :per-page 10 :max-pages 10 :current 28 :biased :left}))
      (is (= (data16 :current), 28))
      (is (data16 :pages) (range 21 31))));[21 22 23 24 25 26 27 28 29 30]

  (testing "No pagination needed - items fit to a single page"
    (do
      (def data17 (paginate {:records 5 :per-page 10 :max-pages 10 :current 1 :biased :left}))
      (is (= (data17 :current), 1))
      (is (data17 :pages) (range 1 2)))));[1]
