import Prelude hiding (lookup)
import Data.Map hiding (map)
import Data.Maybe
import Char

mnemonics = fromList [ ('2', "ABC"), ('3', "DEF"), ('4', "GHI"), ('5', "JKL")
                     , ('6', "MNO"), ('7', "PQRS"), ('8', "TUV"), ('9', "WXYZ") ]

get key dict = fromJust (lookup key dict)

charCode = fromList [(letter, digit) | digit <- keys mnemonics, letter <- get digit mnemonics]

wordCode = map ((flip get) charCode . toUpper) 

wordsForNum (word:[]) = fromList [(wordCode word, [word])]
wordsForNum (word:words) = insertWith (++) (wordCode word) [word] (wordsForNum words)

encode wordList "" = [[]]
encode wordList code = [ [word] ++ rest | i <- [1.. length code]
                                        , let words = lookup (take i code) (wordsForNum wordList) 
                                        , words /= Nothing
                                        , word <- fromJust words 
                                        , rest <- encode wordList (drop i code) ]

-- *Main> encode ["nam", "man", "o", "war"] "6266927"
-- [["nam","o","war"],["man","o","war"]]
	