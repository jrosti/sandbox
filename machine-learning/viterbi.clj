(def transitions 
  [[0.3 0.5 0.8] 
   [0.1 0.8 0.1] 
   [0.1 0.7 0.2]])

(defn observation-fn
  [observation state]
  0.3333)

(def states 3)

(def initial-state 
  [ [ [0] [1] [2]] [-1 -1 -1] ])

(defn apply-viterbi 
  [[path viterbi] observation]
  (loop [j 0 new-viterbi (transient []) new-path (transient [])]
    (if (= j states)
      [(persistent! new-path) (persistent! new-viterbi)]
      (let [candidates (for [i (range states)]
                         [i (+ (viterbi i) 
                               (Math/log (get-in transitions [i j]))
                               (Math/log (observation-fn observation j)))])
            survivor (apply max-key second candidates)]
        (recur (inc j) 
               (conj! new-viterbi (second survivor))
               (conj! new-path 
                     (conj (path (first survivor)) j)))))))

(defn run []
  (reduce apply-viterbi initial-state 
          (take 100 (interpose 2 (repeat 0)))))
