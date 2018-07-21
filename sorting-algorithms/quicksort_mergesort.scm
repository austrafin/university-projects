(define partition (lambda (pivot vals)
		(addToList pivot (cdr vals) '() '())))

(define addToList (lambda (pivot vals left right)
	(cond
		((null? vals) (list left right))
		((< (car vals) pivot) (addToList pivot (cdr vals) (cons (car vals) left) right))
		((> (car vals) pivot) (addToList pivot (cdr vals) left (cons (car vals) right)))
		(else (addToList pivot (cdr vals) left right))))

(define prepend-reversed (lambda (first second) 
	(cond
	((null? first) second)
	((prepend-reversed (cdr first) (cons (car first) second))))))
	
(define quicksort (lambda (vals)
	(cond
		((> (length vals) 1) (let ((left_and_right (partition (car vals) vals)))
								(let ((left (car left_and_right)) (right (cadr left_and_right)))
								(prepend-reversed (reverse (quicksort left)) (cons (car vals) (quicksort right))))))
		(else vals))))
	
(define zip (lambda (list_1 list_2)
	(cond
		((null? list_1) list_2)
		((null? list_2) list_1)
		((< (car list_1) (car list_2)) (cons (car list_1) (zip (cdr list_1) list_2)))
		(else (cons (car list_2) (zip (cdr list_2) list_1))))))	
		

	
(define odds (lambda (L)
	(cond
		((null? L) '())
		((null? (cdr L)) (list (car L)))
		(else (cons (car L) (odds (cddr L))))))) ; take the first element (odd) and recursively insert into other odd index elements
		
(define evens (lambda (L)
	(cond
		((null? L) '())
		((null? (cdr L)) '())
		(else (cons (cadr L) (evens (cddr L))))))) ; take the second element (even) and recursively insert into other even index elements

; splits into two lists
(define split (lambda (L)
	(cons (odds L) (cons (evens L) '()))))

(define mergesort (lambda (L)
	(cond
		((null? L) L)
		((null? (cdr L)) L)
		(else (zip (mergesort (car (split L))) (mergesort (cadr (split L))))))))
	
(define random-list (lambda (ls count)
	(cond
		((> count 0) (random-list (cons (random 10000) ls) (- count 1)))
		(else ls))))

(define main (lambda (runs size total)
	(let ((start (runtime)))
		(mergesort (random-list '() size))
		(let ((end (runtime)))
			(cond
			((> runs 0) (main (- runs 1) size (+ (* 1000000000 (* (- end start) (/ 524288 size))) total)))
			(else (/ total 10)))))))