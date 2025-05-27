

-------------------------
-------- PART A --------- 
-------------------------
import Data.List

type Var = String

data Term =
    Variable Var
  | Lambda   Var  Term
  | Apply    Term Term
  --deriving Show

instance Show Term where
  show = pretty

example :: Term
example = Lambda "a" (Lambda "x" (Apply (Apply (Lambda "y" (Apply (Variable "a") (Variable "c"))) (Variable "x")) (Variable "b")))

pretty :: Term -> String
pretty = f 0
    where
      f i (Variable x) = x
      f i (Lambda x m) = if i /= 0 then "(" ++ s ++ ")" else s where s = "\\" ++ x ++ ". " ++ f 0 m 
      f i (Apply  n m) = if i == 2 then "(" ++ s ++ ")" else s where s = f 1 n ++ " " ++ f 2 m


factorial :: Int -> Int
factorial 0 = 1
factorial x = x * factorial (x-1) 

------------------------- Assignment 1

numeral :: Int -> Term 
numeral i = Lambda "f" (Lambda "x" (subNumeral i)) -- Base Lf.Lx.i
    where
      subNumeral i
        | i <= 0 = Variable "x" -- at zero simply .x
        | otherwise = Apply (Variable "f") (subNumeral (i-1)) --above 0 apply i*(fx)


-------------------------

merge :: Ord a => [a] -> [a] -> [a]
merge xs [] = xs
merge [] ys = ys
merge (x:xs) (y:ys)
    | x == y    = x : merge xs ys
    | x <= y    = x : merge xs (y:ys)
    | otherwise = y : merge (x:xs) ys


------------------------- Assignment 2

variables :: [Var]
variables = map (:[]) ['a'..'z'] ++ [ x:show y | y <- [1..] ,x <- ['a'..'z']]

filterVariables :: [Var] -> [Var] -> [Var]
filterVariables xs []     = xs 
filterVariables xs (y:ys) = filter (/=y) (filterVariables xs ys)

fresh :: [Var] -> Var
fresh = head . filterVariables variables

used :: Term -> [Var]
used (Variable x) = [x]
used (Lambda x n) = merge [x] (used n)
used (Apply  n m) = merge (used n) (used m)


------------------------- Assignment 3


rename :: Var -> Var -> Term -> Term
rename x y (Variable z)
    | z == x    = Variable y
    | otherwise = Variable z
rename x y (Lambda z n)
    | z == x    = Lambda z n
    | otherwise = Lambda z (rename x y n)
rename x y (Apply n m) = Apply (rename x y n) (rename x y m)


substitute :: Var -> Term -> Term -> Term
substitute x n (Variable y)
    | x == y    = n
    | otherwise = Variable y
substitute x n (Lambda y m)
    | x == y    = Lambda y m
    | otherwise = Lambda z (substitute x n (rename y z m))
    where z = fresh (used n `merge` used m `merge` [x,y])
substitute x n (Apply m p) = Apply (substitute x n m) (substitute x n p)

------------------------- Assignment 4

beta :: Term -> [Term]
beta (Apply (Lambda x n) m) =
  [substitute x m n] ++
  [Apply (Lambda x n') m  | n' <- beta n] ++
  [Apply (Lambda x n)  m' | m' <- beta m]
beta (Apply n m) =
  [Apply n' m  | n' <- beta n] ++
  [Apply n  m' | m' <- beta m]
beta (Lambda x n) = [Lambda x n' | n' <- beta n]
beta (Variable _) = []


normalize :: Term -> Term
normalize n
  | null ns   = n
  | otherwise = normalize (head ns) 
  where ns = beta n

run :: Term -> IO ()
run n = do
  print n
  let ns = beta n
  if null ns then
    return ()
  else
    run (head ns)

 
-------------------------

suc    = Lambda "n" (Lambda "f" (Lambda "x" (Apply (Variable "f") (Apply (Apply (Variable "n") (Variable "f")) (Variable "x")))))
add    = Lambda "m" (Lambda "n" (Lambda "f" (Lambda "x" (Apply (Apply (Variable "m") (Variable "f")) (Apply (Apply (Variable "n") (Variable "f")) (Variable "x"))))))
mul    = Lambda "m" (Lambda "n" (Lambda "f" (Lambda "x" (Apply (Apply (Variable "m") (Apply (Variable "n") (Variable "f"))) (Variable "x")))))
dec    = Lambda "n" (Lambda "f" (Lambda "x" (Apply (Apply (Apply (Variable "n") (Lambda "g" (Lambda "h" (Apply (Variable "h") (Apply (Variable "g") (Variable "f")))))) (Lambda "u" (Variable "x"))) (Lambda "x" (Variable "x")))))
minus  = Lambda "n" (Lambda "m" (Apply (Apply (Variable "m") dec) (Variable "n")))

-------------------------
-------- PART B --------- 
-------------------------

------------------------- Assignment 5


exampletwo :: Term
exampletwo = Apply (Apply (Variable "x") (Variable "y")) (Variable "z")

free :: Term -> [Var]
free (Variable x) = [x] --if receive a single var, return as list
free (Lambda x n) = sort (filter (/=x) (free n)) --sort answer, filter bound vars to this lambda
free (Apply  n m) = merge (sort((free n))) (sort((free m)))  --find free for each term in application

abstractions :: Term -> [Var] -> Term 
abstractions t [] = t --if no more Vars left, add the 't' Term
abstractions t (x:xs) = Lambda x (abstractions t (xs)) --add the next variable 

applications :: Term -> [Term] -> Term
applications t [] = t --if not more terms left, simply apply this term
applications t (x:xs) = applications (Apply t x) xs --apply left-sided this term with next and following application 

lift :: Term -> Term --apply the free vars of term to abstractions of term and term
lift t = applications (abstractions t (free t)) (map (Variable) (free t))
    
super :: Term -> Term
super (Variable x) = Variable x -- singular variables stay as is
super (Apply n m)  = Apply (super n) (super m) --super both sub-terms
super (Lambda x n) = lift (Lambda x (super' n)) --lift a subterm of (\\x1...\\xn.N) with super N
  where  --auxillary function a term of form \\x1...\\xn.N is found
    super' (Lambda x n) = Lambda x (super' n) --if another lambda, recurse
    super' (Apply n m) = super (Apply n m) --return to super function otherwise
    super' (Variable x) = (Variable x) --no need to change singular variables
    

------------------------- Assignment 6

data Expr =
    V Var --V for variable
  | A Expr Expr --A for application

toTerm :: Expr -> Term
toTerm (V x) = Variable x --turn expression variable to lambda variable
toTerm (A n m) = Apply (toTerm n) (toTerm m) --turn expr application to \\ application

instance Show Expr where
  show = show . toTerm

type Inst = (Var, [Var], Expr) --{name, paramaters, expression}

data Prog = Prog [Inst]

instance Show Prog where
  show (Prog ls) = unlines (map showInst ks)
    where
      ks = map showParts ls
      n  = maximum (map (length . fst) ks)
      showParts (x,xs,e) = (x ++ " " ++ unwords xs , show e)
      showInst (s,t) = take n (s ++ repeat ' ') ++ " = " ++ t

names = ['$':show i | i <- [1..] ]

-------------------------

stripAbs :: Term -> ([Var],Term)
stripAbs t = stripAbs' [] t --pass list of abs and term to accumulator function
  where
    stripAbs' :: [Var] -> Term -> ([Var],Term) --take a list of abs and term
    stripAbs' acc (Lambda x n) = stripAbs' (acc ++ (x:[])) n --if abs, accumulate and repeat
    stripAbs' acc (Variable x) = (acc, Variable x) --else, return empty list and term
    stripAbs' acc (Apply n m)  = (acc, Apply n m)

takeAbs :: Term -> [Term]
takeAbs (Lambda x n) = [(Lambda x n)] --if the whole term is one abstraction, return as singleton
takeAbs (Apply n m) = (takeAbs n) ++ (takeAbs m)   --if term is application, return each term's abstraction(s)
takeAbs (Variable x) = [] --Vars are not abstractions

getVarsInExpr :: Expr -> [Var] --get the variables in an expression to filter them out 
getVarsInExpr (V x) = [x]
getVarsInExpr (A n m) = (getVarsInExpr n) ++ (getVarsInExpr m)

toExpr :: [Var] -> Term -> Expr
toExpr _ (Variable x) = V x --leave lone variables as is
toExpr vars (Lambda x n) = V (head vars) --for abstractions, insert first in list of fresh vars
toExpr vars (Apply n m) = A (toExpr vars n) (toExpr (filterVariables vars (getVarsInExpr(toExpr vars n))) m)--for applications, do both abstractions, removing used ones in n for m

toInst :: [Var] -> (Var,Term) -> (Inst,[(Var,Term)],[Var])
toInst vars (v,t) = toInst' vars (v,t) (stripAbs t) --reapply function, separating stripAbs of remainder term
  where --return tuple of (instruction, pair of instructions with their abs, remaining names)
    toInst' :: [Var] -> (Var,Term) -> ([Var],Term) -> (Inst,[(Var,Term)],[Var])
    toInst' vars (v,t) (sVars,lTerm) = ((v,sVars,toExpr vars lTerm),zip vars (takeAbs lTerm),filterVariables vars (getVarsInExpr(toExpr vars lTerm)))

prog :: Term -> Prog
prog t = Prog (aux names [("$main", super t)]) --return an instance of Prog 
  where
    aux :: [Var] -> [(Var,Term)] -> [Inst]
    aux vars [] = [] --with no terms left, stop
    aux vars ((v,t):lTerms) = subAux (toInst vars (v,t)) vars lTerms --recurse with next term
      where
        subAux :: (Inst,[(Var,Term)],[Var]) -> [Var] -> [(Var,Term)] -> [Inst]
        subAux (i,is,vars) _ lTerms = [i] ++ (aux vars (lTerms ++ is)) --return instance of current instruction and recurse with next

example2 = Apply (Variable "S") (Apply (Apply example (numeral 0)) (Variable "0"))
example3 = Apply (Apply add (numeral 1)) (Apply (Apply mul (numeral 2)) (numeral 3))
example4 = Apply (Apply example3 (Variable "S")) (Variable "0")

------------------------- Assignment 7

sub :: [(Var,Expr)] -> Expr -> Expr
sub ves (A n m) = A (sub ves n) (sub ves m) --if application, substitute in both Exprs
sub ves (V x) = sub' ves (V x) --if variable, if substitutable, do so, else leave it
  where
    sub' [] (V n) = (V n)
    sub' ((v,e):vs) (V n) = if (n == v) then e else (sub' vs (V n))

step :: [Inst] -> [Expr] -> IO [Expr]
step i ((A n m):es) = do return ([n] ++ [m] ++ es) --if application, push both to stack
step i ((V x):vs) = do  --if var
  let (v,e) = getVarsInInst i (V x) --find instances of var in instructions
  if (v == []) then return ([e] ++ vs)  --if no parameters, return the expression itself and stack
  else if ((head v) == "Empty") then do --if free variable, print name
    (putStr (x ++ " ")); (return vs)
  else if ((length v) > (length vs)) then error "step: insufficient arguments on stack" --error if more params than stack vars
  else --otherwise, do the substitutions and update stack
    return ([(sub (zip v vs) e)] ++ (drop (length v) vs))
    where --function to get occurances of var in instructions
      getVarsInInst :: [Inst] -> Expr -> ([Var],Expr)
      getVarsInInst [] (V x) = (["Empty"],(V x))
      getVarsInInst ((v,vars,e):is) (V x) = 
        if (v == x) then (vars,e) else (getVarsInInst is (V x))  

supernormalize :: Term -> IO ()
supernormalize t = do --start io process
  let (Prog p) = prog t --create an instacnce of program, using the given lambda-term
  supernormalize' p [V "$main"]
    where --repeat 'step' using list of insts (prog) and given stack 
      supernormalize' :: [Inst] -> [Expr] -> IO ()
      supernormalize' i s = do
      stack <- step i s --keep going until step returns empty stack (nothing)
      if (length stack) > 0 then supernormalize' i stack
      else putStrLn(""); return ();