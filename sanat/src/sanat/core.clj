(ns sanat.core
  (:require [clojure.data.json :as json] 
             [me.raynes.conch.low-level :as sh]
             [clojure.string :as string]
             )
  (:import [org.puimula.libvoikko Voikko TokenType]))
           ;;[org.tartarus.snowball.ext finnishStemmer]))

;; brew install libvoikko
;; wget http://www.puimula.org/htp/testing/voikko-snapshot/dict.zip
;; unzip to ~/.voikko

(def sanat-frequencies (-> "sanat.json" slurp json/read-str))
(def freqs (into {} sanat-frequencies))
(def sanat (map first sanat-frequencies))


(def lang "fi")
(def voikko (Voikko. lang))

(defn spell [word]
  (.spell voikko word))

(defn suggest [word]
  (vec (.suggest voikko word)))

(defn to-token [token]
  {:text (.getText token)
   :type (case (-> token .getType .ordinal)
           0 :none
           1 :word
           2 :punctuation
           3 :whitespace
           4 :unknown)})

(defn tokens [text]
  (mapv to-token (vec (.tokens voikko text))))

(defn sentences [text]
  (vec (.sentences voikko text)))

(defn hyphenation-pattern [word]
  (.getHyphenationPattern voikko word))

(defn hyphenate [word]
  (.hyphenate voikko word))

(defn to-grammar-error [ge]
  {:error-code (.getErrorCode ge)
   :start-position (.getStartPos ge)
   :error-length (.getErrorLen ge)
   :suggestions (vec (.getSuggestions ge))
   :description (.shortDescription ge)})

(defn grammar-errors [text]
  (mapv to-grammar-error (vec (.grammarErrors voikko text))))

(defn sanat-str [] 
  (->> (take 1000 sanat) (interpose "\n") (apply str)))

(defn run-malaga []
  (let [malaga (sh/proc "malaga" "-m" "sukija/suomi.pro")]
    (future (do (sh/feed-from-string malaga (sanat-str))))
    malaga))

;; TODO: read perusmuodot from mala :out

;; Snowballstemmer

;(def stemmer (finnishStemmer.))

;;(defn stem [word] 
;;  (.setCurrent stemmer word)
;;  (.stem stemmer)
;;  (.getCurrent stemmer))
