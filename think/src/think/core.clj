(ns think.core)

(defn gcd [a b]
  (if (== b 0) 
    a
    (gcd b (mod a b))))

(def lcd 
  (fn [& args] 
    (letfn [(gcd [a b]
              (if (== b 0) 
                a
                (gcd b (mod a b))))
            (lcd [a b] 
              (/ (* a b) (gcd a b)))]
      (reduce lcd args))))

(defn pascal-triangle 
  [row]
  (vec 
   (flatten 
    (reduce 
     (fn [[result memo] x]
       [(conj result (+ memo x)) x])
     [[] 0] 
     row))))

(defn lazy-pascal [fst]
  (cons fst (lazy-seq (lazy-pascal (pascal-triangle fst)))))

(def pascal-triangle2 (letfn [(pt [row]
                  (vec
                   (flatten 
                    (reduce 
                     (fn [[result memo] x]
                       [(conj result (+' memo x)) x])
                     [[] 0] 
                     row))))]
          (partial iterate pt)))

(def bt [1 [2 nil [3 [4 [5 nil nil] [6 nil nil]] nil]]
         [2 [3 nil [4 [6 nil nil] [5 nil nil]]] nil]])

(defn blt [a b]
  (if (and a b)
    (< (first a) (first b))
    (not a))

(def bt2 [1 [2 nil [3 [4 [5 nil nil] [6 nil nil]] nil]]
      [2 [3 nil [4 [6 nil nil] [5 nil nil]]] nil]])

(defn sort-binary-tree [tree]
  (let [[node left right] tree
        leftsort (if left sortbtree identity) 
        rightsort (if right sortbtree identity)]
    (if (blt left right)
      [node (leftsort left) (rightsort right)]
      [node (rightsort right) (leftsort left)])))

(defn binary-tree-sym? [tree] 
  (letfn [(blt [a b]
            (if (and a b)
              (< (first a) (first b))
              (if (not a)
                true
                false)))
          (sortbtree [tree]
            (let [[node left right] tree
                  leftsort (if left sortbtree identity) 
                  rightsort (if right sortbtree identity)]
              (if (blt left right)
                [node (leftsort left) (rightsort right)]
                [node (rightsort right) (leftsort left)])))]
    (let [[node left right] tree]
      (= (sort-binary-tree left) (sort-binary-tree right)))))
          
(defn mirror [tree]
  (let [[node left right] tree
        leftmirror (if left mirror identity) 
        rightmirror (if right mirror identity)]
    [node (rightmirror right) (leftmirror left)]))
    
(defn binary-tree-sym2? [tree] 
  (letfn [(mirror [tree]
            (let [[node left right] tree
                  leftmirror (if left mirror identity) 
                  rightmirror (if right mirror identity)]
                [node (rightmirror right) (leftmirror left)]))]
    (let [[node left right] tree]
      (= left (mirror right)))))

              
(def symmetric-tree-golf 
  (fn [[_ l r]]
    (= ((fn m [t] 
          (let [[n l r] t 
                lm (if l m identity) 
                rm (if r m identity)]
            [n (rm r) (lm l)])) l) 
       r)))

(def nm '{a {p 1, q 2}
          b {m 3, n 4}})

(defn rotate [n coll]
  (let [inf (reduce concat (repeat 100 coll))]
    (if (>= n 0)
      (take (count coll) (nthnext inf n))
      (rotate (+ (* (- n) (count coll)) n) coll))))

(defn indices [n length]
  (for [k (range n)] (range k length n)))

(defn reverse-interleave [coll n]
  (map #(map (vec coll) %) (indices n (count coll))))

(def rigolf 
(fn [c n] (map #(map (vec c) %) (for [k (range n)] (range k (count c) n)))))
       
(defn reverse-interleave2 [coll n] 
  (apply map vector (partition n coll)))

(defn reverse-interleave3 [coll n]  (map #(take-nth n %) (take n (iterate rest coll))))

(def problem-50 #(set (vals (group-by type %))))

(def fs0 frequencies)

(defn fs2 [coll]
  (reduce 
   (fn [freqs elem]
     (merge-with + freqs {elem 1}))
   {}
   coll))

(defn distinct1 [coll]
  (first 
   (reduce 
    (fn [[elems seen] elem]
      (if (seen elem)
        [elems seen]
        (map #(conj % elem) [elems seen])))
    [[] #{}]
    coll)))

(defn distinct2 [coll]
  (reduce 
   (fn [elems elem]
     (if (some #{elem} elems)
       elems
       (conj elems elem)))
   []
   coll))

(defn distinct3 [coll]
  (vec (set coll)))

(defn distinct4 [coll]
  (keys (into {} (for [k coll] [k nil]))))

(defn dropn [n]
  (fn [coll] (drop n coll)))

(def pt
  (fn [n coll]
    (take-while #(== n (count %)) (map #(take n %) (iterate (partial drop n) coll)))))

(defn isprime? [primes ^clojure.lang.BigInt n]
  (let [partitioned-primes (partition-all 1 (take-while #(< (* % %) n) primes))
        not-divisible? #(not= 0 (mod n %))] 
    (every? identity (map (fn [partition] (every? not-divisible? partition))
                           partitioned-primes))))

(defn all-primes []
  (reductions 
   (fn [primes number]
       (if (isprime? primes number)
         (conj primes number)
         primes))
   []
   (iterate inc 2N)))

(defn n-primes [n]
  (first (take 1 (drop-while #(not= (count %) n) (all-primes)))))


(defn all-primes []
  (drop 1 (reductions 
           (fn [primes number]
             (if (isprime? primes number)
               (conj primes number)
               primes))
           []
           (iterate inc 2N))))
  
(defn nprimes [n]
  (letfn [(all-primes [] 
            (reductions
             (fn [primes number]
               (letfn [(isprime? [n]
                         (let [test-primes-until (take-while #(< (* % %) n) primes)
                               not-divisible? #(not= 0 (mod n %))] 
                           (every? not-divisible? test-primes-until)))]
                 (if (isprime? number)
                   (conj primes number)
                   primes)))
             []
             (iterate inc 2N)))]
    (first (take 1 (drop-while #(not= (count %) n) (all-primes))))))

(defn isqrt-step [n [x_k _]]
  (let [x_k1 (* 0.5 (+ x_k (/ n x_k)))
        delta (* (- x_k1 x_k) (- x_k1 x_k))]
    [x_k1 delta]))

;; Integer square root using newton's method
(defn isqrt [n]
  (let [x0 (/ n 2)
        newton-seq (iterate (partial isqrt-step n) [x0 10])]
    ((comp int #(Math/floor ^Double %) first first (partial take 1)) 
     (drop-while #(> (second %) 1.0) newton-seq))))
    
 (defn isgolden? [n]
   (let [isqrtn (isqrt n)]
     (== (* isqrtn isqrtn) n)))

(defn problem-74 [^String s]
  (letfn [(isqrt-step [n [x_k _]]
            (let [x_k1 (* 0.5 (+ x_k (/ n x_k)))
                  delta (* (- x_k1 x_k) (- x_k1 x_k))]
              [x_k1 delta]))
          (isqrt [n]   
            (let [x0 (/ n 2)
                  newton-seq (iterate (partial isqrt-step n) [x0 10])]
              ((comp int #(Math/floor ^Double %) first first (partial take 1)) 
               (drop-while #(> (second %) 1.0) newton-seq))))
          (isgolden?  [n]
            (let [isqrtn (isqrt n)]
              (== (* isqrtn isqrtn) n)))]
    (apply 
     str 
     (interpose 
      "," 
      (filter 
       isgolden? 
       (map 
        #(Integer/parseInt ^String %) 
        (vec (.split s ","))))))))

(defn perfect [n]
  (letfn [(divisors [n]
            (let [candidates (range 1 n)]
              (filter #(and (== 0 (mod n %)) (not (== n %))) candidates)))]
    (== n
        (reduce + (divisors n)))))
                    
 (defn my-merge-with [f & maps]
   (reduce
    (fn [merged amap]
      (reduce 
       (fn [merged [k v]]
         (if (contains? merged k)
           (assoc merged k (f (merged k) v))
           (assoc merged k v)))
       merged amap))
    (first maps)
    (rest maps)))

(defn my-reductions 
  ([f coll]
     (my-reductions f (first coll) (rest coll)))
  ([f initial coll]
     (lazy-seq
      (if-let [elem (first coll)]
        (let [reduction (f initial elem)]
          (cons initial (my-reductions f reduction (rest coll))))
        (cons initial nil)))))

(defn to-camel[word]
  (letfn [(to-upper [word]
            (let [ws (vec word)]
              (apply str (Character/toUpperCase (first ws)) (rest ws))))]
    (let [words (vec (.split word "-"))]
      (apply str (first words) (map to-upper (rest words))))))

(defn totient [x]
  (letfn [(gcd [a b]
            (if (== b 0) 
              a
              (gcd b (mod a b))))]
    (if (== 1 x) 
      1
      (let [pints (range 1 x)]
        (count (filter #(== 1 (gcd x %)) pints))))))

(defn sqsum [n] (reduce + (map #(* % %) (map #(- (int %) (int \0)) (vec (str n))))))

(defn happy? [n]
  (loop [nset #{n} lastn n]
    (println (str nset))
    (let [newn (sqsum lastn)]
      (if (nset newn) 
        false
        (if (== 1 newn)
          true
          (recur (conj nset newn) newn))))))

(defn my-trampoline [f & args]
  (let [app (apply f args)]
    (loop [a app]
      (if (fn? a)
        (recur (a))
        a))))

(defn lsi [cs]
  (let [sqs (reduce 
             (fn [seqs val]
               (if (> val ((comp last last) seqs))
                 (conj (vec (butlast seqs)) (conj (last seqs) val))
                 (conj seqs [val])))
             [[(first cs)]]
             (rest cs))
        longest (first (second (apply max-key key (group-by count sqs))))]
    (if (== 1 (count longest))
      []
      longest)))

(defn balanced [n]
  (let [s (-> n str vec)
        l (count s)
        f #(subvec % 0 (quot l 2))]
    (apply = (map (comp frequencies f) [s (vec (reverse s))]))))

(defn balanced-chouser [n] 
  (let [s (map #(- (int %) 48) (str n))
        l (/ (count s) 2)
        [a b] (map #(apply + (take l %)) [s (into () s)])]
    (= a b)))
    
(def ttte [[:e :e :e]
           [:e :e :e]
           [:e :e :e]])


(def ttt2 [[:x :e :o]
           [:x :e :e]
           [:x :e :o]])

(defn transpose [[x y]]
  [y x])

(defn shift [q [x y]]
  [(+ x q) y])

(def shifts
  (mapv #(partial shift %) (range 3)))

(def test-funs 
  (for [tr [identity transpose] sh shifts]
    (comp tr sh)))

(defn get-in-t [m transform coords]
  (mapv #(get-in m (transform %)) coords))

(defn verify-ttt? [board]
  (let [transpose (fn [[x y]] [y x])
        shift (fn [q [x y]] [(+ x q) y])
        shifts (mapv #(partial shift %) (range 3))
        test-funs (for [tr [identity transpose] sh shifts]
                    (comp tr sh))
        get-in-t (fn [m transform coords]
                   (mapv #(get-in m (transform %)) coords))
        straights (set (map #(get-in-t board % [[0 0] [0 1] [0 2]]) test-funs))
        all (set (concat [(get-in-t board identity [[0 0] [1 1] [2 2]])]
                         [(get-in-t board identity [[0 2] [1 1] [2 0]])]
                         straights))]
    (cond (all [:x :x :x]) :x
          (all [:o :o :o]) :o)))

(defn kvs [in]
  (first
   (reduce 
    (fn [[res kw] val]
      (if (keyword? val)
        [(assoc res val []) val]
        [(merge-with conj res {kw val}) kw]))
    [{} nil]
    in)))

(defn ^clojure.lang.BigInt ipow[x n]
  (->> x repeat (take n) (apply *)))

(defn powers [base]
  (map 
   (partial ipow (* 1N base))
   (range)))

(defn up-to-powers [base n]
  (vec (reverse (take-while #(< % n) (powers base)))))

(defn bconv [n base]
  (if (== n base)
    [1 0]
    (if (== n 0) 
      [0]
      (let [ipow (fn [x n] (->> x repeat (take n) (apply *)))
            powers (fn [base] (map  (partial ipow (* 1N base)) (range)))
            take-powers (fn [base n] (vec (reverse (take-while #(< % n) (powers base)))))]
        (first 
         (reduce
          (fn [[result remainder] power]
            [(conj result (int (quot remainder power)))
           (rem remainder power)])
          [[] n]
        (take-powers base n)))))))
  
(defn seqprons [initial]
  (rest
   (iterate
    (fn prns [sq]
      (let [pbyid (partition-by identity sq)]
        (mapcat (fn [v] [(count v) (first v)]) pbyid)))
    initial)))

(defn oscilrate [value & funs]
  (let [fs (cycle funs)]
    (reductions
     (fn [res fun]
       (fun res))
     value
     fs)))

(defn flatten1
  [x]
  (filter (comp (complement sequential?) first)
          (rest (tree-seq (comp sequential? first) seq x))))

(defn gtake-while [n p s]
  (lazy-seq 
   (if-let [v (first s)]
     (let [newn (if (p v) (dec n) n)]
       (when (not= newn 0)
         (cons v (gtake-while newn p (rest s))))))))

(defn uncurry [f]
  (fn [& args]
    (reduce 
     (fn [res arg]
       (res arg))
     f
     args)))
    

(defn machine [formula]
  (letfn [(evalf [f env]
            (cond (number? f) f
                  (symbol? f) (env f)
                  (list? f) (apply ({'+ + '- - '* * '/ /} 
                                    (first f)) (map #(evalf % env) (rest f)))))]
    (partial evalf formula)))
    
  
(defn balanced-prime [n]
  (letfn [(all-primes [] 
            (reductions
             (fn [primes number]
               (letfn [(isprime? [n]
                         (let [test-primes-until (take-while #(< (* % %) n) primes)
                               not-divisible? #(not= 0 (mod n %))] 
                           (every? not-divisible? test-primes-until)))]
                 (if (isprime? number)
                   (conj primes number)
                   primes)))
             []
             (iterate inc 2N)))]))

(defn subsets [xs] 
  (letfn [(minusone [xs]
            (for [x xs] (disj xs x)))]
  (if (= 1 (count xs))
    #{#{}}
    (apply clojure.set/union #{xs} (map subsets (minusone xs))))))

;; empty set
;; all pairs
;; all triplets ...

(defn gen [all]
  (if (= 0 (count all))
    #{#{}}
    (loop [n 1 subsets #{#{}}]
      (if (== n (count all))
        (conj subsets all)
        (recur (inc n)
               (clojure.set/union
                subsets
                (set
                 (for [elem all subset subsets]
                   (conj subset elem)))))))))

(defn insert-between [predicate value coll]
  (filter identity (conj 
   (->> coll
        (partition 2 1)
        (map (fn [pair]
               (if (apply predicate pair)
                 [value (last pair)]
                 [(last pair)])))
          flatten)
   (first coll))))

(defn sergen [n x] (take-while #(< % n) 
                               (iterate (partial + 0N x) x)))

  (defn big-divide [n a b]
    (let [gen (fn [n x] (take-while #(< % n) 
                                    (iterate (partial + 0N x) x)))]
      (reduce + (clojure.set/union (set (gen n a)) (set (gen n b))))))

(defmacro machine2 
  [formula env]
  `((first '~formula) 
         (~env (second '~formula) (second '~formula)) 
         (~env (last '~formula) (last '~formula))))
