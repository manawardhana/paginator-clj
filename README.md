# paginator-clj
A utility for pagination logic in Clojure and ClojureScript.

[![Clojars Project](https://img.shields.io/clojars/v/paginator-clj.svg)](https://clojars.org/paginator-clj)

Usage
-----
Pass a map of following structure to the paginate function:
```
; :records        : Total number of records
; :per-page       : Items shown per page
; :max-pages      : Maximum number of pagination links appear
; :current        : Current page number
; :biased         : :left or :right, if the number of pages shown is even, 
;                   current page should either sit in left half of right half
; :link-tpl       : template to use for individual links
; :list-tpl       : tempate to use for entire list
```
E.g.
```
(paginate {:records 300 :per-page 10 :max-pages 10 :current 28 :biased :left})
=> {:pages [21 22 23 24 25 26 27 28 29 30] 
    :current 28}
```
