(require '[clojure.data.json :as json])
(require '[me.raynes.conch.low-level :as sh])
(require '[clojure.string :as string])

(def sanat-frequencies (-> "sanat.json" slurp json/read-str))
(def freqs (into {} sanat-frequencies))
(def sanat (mapv first sanat-frequencies))

;; {:user {:plugins [[lein-exec "0.3.4"]]
;;         :dependencies [[org.puimula.voikko/libvoikko "3.6.1"]
;;                        [me.raynes/conch "0.8.0"]]
;; brew install libvoikko
;; wget http://www.puimula.org/htp/testing/voikko-snapshot/dict.zip
;; unzip to ~/.voikko

(import '[org.puimula.libvoikko Voikko TokenType])

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
        
