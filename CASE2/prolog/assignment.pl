/* Predictates dealing with road - the basis of this program */
road(northbog, westhome, 3).
road(oldtown, westhome, 4).
road(oldtown, poshville, 5).
road(villanua, oldtown, 2).
road(eastwick, poshville, 2).
road(poshville, northbog, 3).
road(westpark, villanua, 2).
road(westpark, southfork, 7).
road(southfork, eastwick, 2).
road(eastwick, villanua, 8).

/* Fiddly bits to make everything else work */
true(X):- X is X.
makefalse(X):- \+ true(X).

issmaller(B, C, N, X, Y, R, A, D) :- B < C, N is B, R = [X, Y, A], makefalse(D).
issmaller(B, C, N, X, Y, R, A, D) :- C < B, N is B, R = [X, Y, D], makefalse(A).

/* The actual required predicates
route(X,Y,R,N) is true if a route R of length N exists between town X and town Y */
route(X, Y, R, N) :- road(X, Y, N), R = [X, Y].
route(X, Y, R, N) :- road(X, Z, N1), route(Z, Y, O, N2), N is N1 + N2, R = [X | O].

/* shortest(X,Y,R,N) is true if the route R of length N between town X and town Y is the shortest route between X and Y */
isSmaller(A, B, N) :-  A <= B, N is A.
isSmaller(A, B, N) :- B <= A, N is B.
% isCloser :- .

shortest(X, Y, R, N) :-  road(X, Y, N), R = [X, Y].
% shortest(X, Y, R, N) :- .
shortest(X, Y, R, N) :- road(X, Z, N1), shortest(Z, Y, O, N2), isSmaller(N1, N2, N), R = [X | O].

/*
shortest(X,Y,R,N):-road(X,Y,N),R=[X,Y].
shortest(X,Y,R,N):-road(X,A,B),route(A,Y,P,C),N is B+C,R=[X|P].
shortest(X,Y,R,N):-route(X,Y,A,B),route(X,Y,D,C),N is B+C,issmaller(B,C,N,X,Y,R,A,D).
*/
