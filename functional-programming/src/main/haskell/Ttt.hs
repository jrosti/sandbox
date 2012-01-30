import Data.List
import Control.Parallel

data Board = Board {
    crosses :: [Int],
    noughts :: [Int], 
    alternated :: Bool
} deriving Show

boardSize :: Int
boardSize = 3

ticsForWin :: Int
ticsForWin = 3

emptySlots :: Board -> [Int]
emptySlots board = filter (\position -> notElem position (noughts board ++ crosses board)) 
    $ [1  .. (boardSize * boardSize)]

addTic :: Board -> Int -> Board
addTic board cross = (Board (noughts board) (cross:(crosses board)) (not $ alternated board))

isLeftBorder :: Int -> Bool
isLeftBorder pos = if ( ((pos - 1) `mod` boardSize) == 0)
    then True
    else False

isRightBorder :: Int -> Bool
isRightBorder pos = isLeftBorder $ pos + 1

isBorder :: Int -> Bool
isBorder pos = isLeftBorder pos || isRightBorder pos

hasCross ::  Board -> Int -> Bool
hasCross board position =  position `elem` (crosses board)

from :: Board -> Int -> ( Int -> Int ) -> [Int]
from board pos move = if (isValid pos move && hasCross board (move pos) )
    then (move pos):(from board (move pos) move)
    else []

isValid :: Int -> (Int -> Int) -> Bool
isValid pos move = not (isBorder pos && isBorder (move pos))

horizontal =   ( \pos -> pos - 1            , \pos -> pos + 1 )
diagonalUp =   ( \pos -> pos - boardSize + 1, \pos -> pos + boardSize - 1)
diagonalDown = ( \pos -> pos - boardSize - 1, \pos -> pos + boardSize + 1)
vertical =     ( \pos -> pos - boardSize    , \pos -> pos + boardSize    )

isFull :: Board -> Bool
isFull board = emptySlots board == []

rows :: Board -> Int -> [[Int]]
rows board index = filter (\row -> length row > 1) $ 
    [from board index start ++ [index] ++ from board index end
    | (start, end) <- [ horizontal
                      , diagonalUp
                      , diagonalDown
                      , vertical
                      ]
    ]

hasWinningRow :: Board -> [Int] -> Bool
hasWinningRow board [] = False
hasWinningRow board (x:xs) = if (any (\row -> length row == ticsForWin) (rows board x))
    then True
    else hasWinningRow board xs

score board = let hasWin = hasWinningRow (alternate board) (crosses (alternate board)) in
    if (hasWin)
    then if (alternated board)
        then 1
        else - 1
    else 0
    where
        alternate = \board -> Board (noughts board) (crosses board) (not (alternated board))

data Tree a = Leaf a | Branch [Tree a] deriving Show

gameTree :: Board -> Tree Board
gameTree board = 
    if (isFull board || abs (score board) == 1)
    then Leaf board
    else Branch $ map (\emptySlot -> gameTree (addTic board emptySlot)) $ emptySlots board

parMap :: (a -> b) -> [a] -> [b]
parMap f [] = []
parMap f (x:xs) = fx `par` fxs `seq` (fx:fxs)
	where
		fx = f x
		fxs = parMap f xs

maximize :: Tree Board -> Int
maximize (Leaf board) = score board
maximize (Branch subGames) = maximum (parMap minimize subGames)

minimize :: Tree Board -> Int
minimize (Leaf board) = score board
minimize (Branch subGames) = minimum (parMap maximize subGames)

main = do 
	putStrLn $ show $ maximize $ gameTree $ Board [] [] False
