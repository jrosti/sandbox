(def transitions 
  [[0.3 0.5 0.8] 
   [0.1 0.8 0.1] 
   [0.1 0.7 0.2]])

(defn observation-fn
  [state token]
  0.3333)

(def states 3)

(defn viterbi [accumulator observation]
  (loop [next 0 new-viterbi [] new-path []]
    (if (= source states) 
      {:path new-path :viterbi new-viterbi}
      (let [candidates (map-indexed (fn [source]
                                        (* ((:viterbi accumulator) source) 
                                           (get-in transitions [source next])
                                           (observation-fn observation next)))
                                    (range states))
            survivor (apply min-key second candidates)]
        (recur (inc source) 
               (conj new-viterbi (second survivor))
               (conj new-path 
                     (conj ((:path accumulator) (first survivor)) next)))))))

