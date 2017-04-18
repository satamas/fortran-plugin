forall (i=1:10:2)
   forall (j=2:20:4, i=1:10:2, j /= i) a = 2
 1 forall (i=1:10:2)
 2    v=1
 3 endforall
   where (l < 0) b = 1
   where (k<0)
      c = 4
   elsewhere (k<3)
      c=5
   elsewhere
      c=6
   endwhere
endforall
endprogram