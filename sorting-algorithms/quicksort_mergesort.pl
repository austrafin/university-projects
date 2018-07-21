ordered([]).
ordered([_]).
ordered([Head,Next|Tail]) :- Head =< Next, ordered([Next|Tail]).

perm([],[]).
perm([Head|Tail], List) :- delete(Head, List, Rest), perm(Tail, Rest).

delete(Item, [Item|List], List).
delete(Item, [Head|Long], [Head|Short]) :- delete(Item, Long, Short).

psort(L, S) :- perm(S, L), ordered(S).

zip([], L, L).
zip(L, [], L).
zip([Left_head|Left_tail], [Right_head|Right_tail], [Left_head|Merged])  :- Left_head < Right_head, zip(Left_tail, [Right_head|Right_tail], Merged).
zip([Left_head|Left_tail], [Right_head|Right_tail], [Right_head|Merged]) :- Left_head > Right_head, zip([Left_head|Left_tail], Right_tail, Merged).
zip([Left_head|Left_tail], [Right_head|Right_tail], [Right_head|Merged]) :- Left_head = Right_head, zip(Left_tail, Right_tail, Merged). % ignore equals

% split by odd and even indecies
split([], [], []).
split([H|T], [H|Odd], Even):- split(T, Even, Odd). % toggle

mergesort([X],[X]).
mergesort([],[]).
mergesort(Unsorted, Sorted) :-
	split(Unsorted, Left, Right), mergesort(Left, Left_sorted), mergesort(Right, Right_sorted), zip(Left_sorted, Right_sorted, Sorted).
	
insertionsort(List, Sorted) :- isort_with_acc(List, [], Sorted).
isort_with_acc([], A, A).
isort_with_acc([H|T], A, Sorted) :- insert(H, A, Inserted), isort_with_acc(T, Inserted, Sorted).
   
insert(Key, [H|T], [H|Sorted]) :- Key  > H, insert(Key, T, Sorted).
insert(Key, [H|T], [Key,H|T])  :- Key =< H.
insert(X, [], [X]).
  
% If the Head is less than pivot, insert into the 'less' list and move on to the next item in the list.
% Else insert Head into the 'greater than' list.
partition(_,[],[],[]).
partition(Pivot, [H|T], [H|Less], Greater) :- H < Pivot, partition(Pivot, T, Less, Greater).
partition(Pivot, [H|T], Less, [H|Greater]) :- H > Pivot, partition(Pivot, T, Less, Greater).
partition(Pivot, [H|T], Less, Greater) :- H = Pivot, partition(Pivot, T, Less, Greater).

quicksort_with_append([], []).
quicksort_with_append([H|T], S) :- % First element in the list is the pivot.
	partition(H, T, Left, Right),
	quicksort_with_append(Left, Left_sorted),
	quicksort_with_append(Right, Right_sorted),
	append(Left_sorted, [H|Right_sorted], S).

qsortap([], A, A).
qsortap([Pivot|Rest], A, Sorted) :- % First element in the list is the pivot.
	partition(Pivot, Rest, Left, Right),
	qsortap(Right, A, Right_sorted),
	qsortap(Left, [Pivot|Right_sorted], Sorted).

quicksort(List, Sorted) :- qsortap(List, [], Sorted).

random_list(0, []).
random_list(Count, List) :-
	Count > 0,
	New_count is Count - 1,
	random(1, 1000000, R),
	List = [R|T],
	random_list(New_count, T).

main(N, X) :-
	use_module(library(random)),
	random_list(N, L),
	statistics(runtime, [Start|_]),	
	% quicksort(L, X),
	% quicksort_with_append(L, X),
	% psort(L, X),
	% mergesort(L, X),
	insertionsort(L, X),
	statistics(runtime, [Finish|_]), 
	Total is Finish - Start,
	write('Execution time: '), write(Total), write(' ms.'), nl.