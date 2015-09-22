(declare-const OA1 Int)
(declare-const OB1 Int)
(declare-const OB2 Int)
(declare-const OA2 Int)
(declare-const OB4 Int)
(declare-const OB3 Int)
(assert 
(> OA2 OA1)
)

(assert 
(> OB2 OB1)
)

(assert 
(> OB3 OB2)
)

(assert 
(> OB4 OB3)
)

(assert 
(> OB4 OA1)
)

(assert 
(or (> OA1 OB2)
 (> OB2 OB4)
)
)

(assert 
(> OA2 OB1)
)

(assert 
(> OB3 OB2)
)

(assert 
(or (> OB2 OA1)
 (> OA1 OB3)
)
)

(check-sat)
(get-model)
(exit)
