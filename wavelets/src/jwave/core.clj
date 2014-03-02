(ns jwave.core
  [:import 
   [math.jwave.transforms.wavelets 
    Haar02 Coif06 Daub02 Daub03 Daub04 Haar02Orthogonal Lege02 Lege04 Lege06]
   [math.jwave Transform]
   [math.jwave.transforms FastWaveletTransform WaveletPacketTransform]
   [javax.imageio ImageIO]
   [java.io File]
   [java.awt.image BufferedImage DataBufferByte]
   [java.awt Color Dimension Graphics]
   [javax.swing JFrame JPanel]
   ])

(set! *warn-on-reflection* true)

(defn ftw [transform] 
  (Transform. (FastWaveletTransform. transform)))

(defn wpt [transform]
  (Transform. (WaveletPacketTransform. transform)))

(def haar-ftw
  (ftw (Haar02.)))

(def haar-wpt
  (wpt (Haar02.)))

(defn forward [ftw double-arr]
  (.forward ftw double-arr))

(defn forward-wpt [wpt double-arr]
  (.forward wpt double-arr))

(def haar-forward
  (partial forward haar-ftw))

(def haar-wpt-forward 
  (partial forward-wpt haar-wpt))

(defn read-buffered-image [^String filename]
  (ImageIO/read (File. filename)))

(defn rgb-to-double [rgb]
  (let [r (bit-and (bit-shift-right rgb 16) 255)
        g (bit-and (bit-shift-right rgb 8) 255)
        b (bit-and rgb 255)]
    (mapv (fn [v] (/ (double v) 255.0)) [r g b])))

(defn intensity [^BufferedImage buffered-image]
  (let [h (.getHeight buffered-image)
        w (.getWidth buffered-image)
        mk #(make-array Double/TYPE w h)
        arr-r (mk) arr-g (mk) arr-b (mk)]
    (dotimes [y h]
      (dotimes [x w]
        (let [[r g b] (rgb-to-double (.getRGB buffered-image x y))]
          (aset arr-r x y r)
          (aset arr-g x y g)
          (aset arr-b x y b))))
    [arr-r arr-g arr-b]))

(defn construct-frame [width height panel]
  (let [frame (JFrame.)]
      (.setPreferredSize panel (Dimension. width height))
      (doto frame
        (.add panel)
        .pack
        (.setLocationRelativeTo nil)
        .show)))

(defn create-image [width height]
  (BufferedImage. width height (BufferedImage/TYPE_INT_RGB)))

(defn get-panel [img]
  (proxy [JPanel] [] (paint [g] (.drawImage g img 0 0 (Color/red) nil))))

(defn amaxmin [arr w h]
  (let [maxmin (double-array [Integer/MIN_VALUE Integer/MAX_VALUE 0.0])]
    (dotimes [y h]
      (dotimes [x w]
        (if (> (aget arr x y) (aget maxmin 0)) (aset maxmin 0 (aget arr x y))) 
        (if (< (aget arr x y) (aget maxmin 1)) (aset maxmin 1 (aget arr x y)))
        (aset maxmin 2 (+ (aget maxmin 2) (aget arr x y)))))
    (aset maxmin 2 (/ (aget maxmin 2) (* w h)))
    (vec maxmin)))

(defn hello [x]
  (prn x))
 
(defn draw-result [output-panel wr w h transform]
  (let [[max min mean] (amaxmin transform w h)
        mm (double (- min))
        rr (double (+ max mm))]
    (prn "transform max min" max min mm rr mean)
    (dotimes [y h]
      (dotimes [x w]
        (let [val (aget transform x y)
              g (int (if (< val -0.01) 255 0))
              r (if (> val 0.005) 255 0)
              b (if (and (< val 0.005) (> val -0.01)) 255 0)]
          (.setPixel wr x y (int-array [r g b]))))
        (.repaint output-panel))))

(defn -main [& args]
  (let [input-image (read-buffered-image (first args))
        width (.getWidth input-image)
        height (.getHeight input-image)
        output-images (map (fn [_] (create-image width height)) (range 3))
        output-panels (map get-panel output-images)
        input-panel (get-panel input-image)
        rasters (map (fn [img] (.getRaster img)) output-images)]
    (mapv (partial construct-frame width height) output-panels)
    (construct-frame width height input-panel)
    (let [intensities (intensity input-image)
          waves (pmap haar-forward intensities)
          draw (fn [f t] (draw-result 
                          (f output-panels) 
                          (f rasters) width height t))]
      (doall (pmap draw [first second last] waves))          
      (prn "done"))))


