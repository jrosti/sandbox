import Control.Parallel

type Inputs = [Double]
type Weights = [Double]
type TrainingData = (Inputs, Int)

alpha :: Double
alpha = 0.0001

defaultBias :: Double
defaultBias = 0.5

trainingData :: [TrainingData]
trainingData = [ ([1.0, 1.0], 1)
			   , ([0.0, 1.0], 0)
			   , ([1.0, 0.0], 0)
			   , ([0.0, 0.0], 0)
			   , ([0.0, 1.0], 0)
			
			   , ([1.0, 0.0], 0)
			   , ([0.0, 0.0], 0)
			   , ([0.0, 1.0], 0)
			   , ([1.0, 0.0], 0)
			   , ([0.0, 0.0], 0)
			
			   , ([0.0, 1.0], 0)
			   , ([1.0, 0.0], 0)
			   , ([0.0, 0.0], 0)
			   , ([0.0, 1.0], 0)
			   , ([1.0, 0.0], 0)
			   , ([0.0, 0.0], 0)
			   , ([1.0, 0.0], 0)
			   , ([0.0, 0.0], 0)
			   , ([0.0, 1.0], 0)
			   , ([1.0, 0.0], 0)
			   , ([0.0, 0.0], 0)
			
			   , ([0.0, 1.0], 0)
			   , ([1.0, 0.0], 0)
			   , ([0.0, 0.0], 0)
			   , ([0.0, 1.0], 0)
			   , ([1.0, 0.0], 0)
			   , ([0.0, 0.0], 0), ([1.0, 0.0], 0)
			   , ([0.0, 0.0], 0)
			   , ([0.0, 1.0], 0)
			   , ([1.0, 0.0], 0)
			   , ([0.0, 0.0], 0)
			
			   , ([0.0, 1.0], 0)
			   , ([1.0, 0.0], 0)
			   , ([0.0, 0.0], 0)
			   , ([0.0, 1.0], 0)
			   , ([1.0, 0.0], 0)
			   , ([0.0, 0.0], 0), ([1.0, 0.0], 0)
			   , ([0.0, 0.0], 0)
			   , ([0.0, 1.0], 0)
			   , ([1.0, 0.0], 0)
			   , ([0.0, 0.0], 0)
			
			   , ([0.0, 1.0], 0)
			   , ([1.0, 0.0], 0)
			   , ([0.0, 0.0], 0)
			   , ([0.0, 1.0], 0)
			   , ([1.0, 0.0], 0), ([1.0, 0.0], 0)
			   , ([0.0, 0.0], 0)
			   , ([0.0, 1.0], 0)
			   , ([1.0, 0.0], 0)
			   , ([0.0, 0.0], 0)
			
			   , ([0.0, 1.0], 0)
			   , ([1.0, 0.0], 0)
			   , ([0.0, 0.0], 0)
			   , ([0.0, 1.0], 0)
			   , ([1.0, 0.0], 0)
			   , ([0.0, 0.0], 0)
			   , ([0.0, 0.0], 0)			
               ]

perceptron :: Weights -> Double -> Inputs -> Double
perceptron weights bias input = (sum $ zipWith (*) weights input) - bias

neuronOutput :: (Weights -> Double) -> Inputs -> Int
neuronOutput perceptron input
  | perceptron input > 0.0 = 1
  | otherwise = 0

allOutputs :: Weights -> [Int]
allOutputs weights = map (\(input, _) -> neuronOutput (perceptron weights defaultBias) input) trainingData

trainWeights :: Int -> [TrainingData] -> Weights -> Weights
trainWeights 0 [] acc = acc
trainWeights t [] acc = trainWeights (t - 1) trainingData acc
trainWeights t (trData:trDatas) acc = trainWeights t trDatas (zipWith (+) acc $ map adjustWeight inputs) 
	where 
		adjustWeight = \input -> alpha * ( fromIntegral (expectedOutput - actualOutput) ) * input  
		expectedOutput = snd trData
		actualOutput = neuronOutput (perceptron acc defaultBias) $ fst trData
		inputs = fst trData

output t =  allOutputs (trainWeights t trainingData [-0.1, 0.2])

errorNorm :: Int -> Int
errorNorm t = foldl (+) 0 $ zipWith (\a b -> abs (a-b)) (map snd trainingData) (output t)


main = do 
	putStrLn $ show $ errorNorm 10000



