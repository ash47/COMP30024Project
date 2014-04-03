COMP30024 Artificial Intelligence
==============

###Nothing much here###
 - Some change

###Test Cases###


Black wins loop
```
5
BB---
B-B---
-B-B---
--BB----
------W--
-WW--W--
--WW---
---W--
--W--
```

Black wins both
```
5
BBBBB
BBBBBB
BBBBBBB
BBBBBBBB
BBB-BBBBB
BBBBBBBB
BBBBBBB
BBBBBB
BBBBB
```

Draw, black loop, white tripod
```
5
    B B - - - 
   B - B - - - 
  - B - B - - W 
 - - B B - - W - 
- - - - - - W - - 
 W W - - W W - - 
  - W W W - - - 
   - - W - - - 
    - W - - - 
```

Black wins loop
```
5
    B B - - - 
   B - B - - - 
  - B - B - - - 
 - - B B - - - - 
- - - - - - W - - 
 W W - - W W - - 
  - W W - - W - 
   - - W - - W 
    - W - - - 

```

No winner
```
5
    B B - - - 
   B - B - - - 
  - - - B - - - 
 - - B B - - - - 
- - - - - - W - - 
 W W - - W - - - 
  - W W - - - - 
   - - W - - - 
    - W - - - 

```

No winner
```
5
    B B - - - 
   B - B - - - 
  - - - B - - - 
 - - B B - - - - 
- - - - - - W - - 
 W W - - W - - - 
  - W - - - - - 
   - - - - - - 
    - W - - - 

```

Invalid input (line 5 is wrong length, got length 10, expected 9)
```
5
    B B - - - 
   B - B - - - 
  - - - B - - - 
 - - B B - - - - 
- - - - - - W - - -
 W W - - W - - - 
  - W - - - - - 
   - - - - - - 
    - W - - - 
```

Invalid input (line 1 is wrong length, got length 6, expected 1)
```
5
    B B - - - -
   B - B - - - -
  - - - B - - - -
 - - B B - - - - -
- - - - - - W - - -
 W W - - W - - - -
  - W - - - - - -
   - - - - - - -
    - W - - - -
```

Invalid input (Unknown player token on line 1 (Got C))
```
5
    B B C - - 
   B - B - - - 
  - - - B - - - 
 - - B B - - - - 
- - - - - - W - - 
 W W - - W - - - 
  - W - - - - - 
   - - - - - - 
    - W - - - 
```

White tripod
```
5
    - - - - - 
   - - - - - - 
  - - - - - - - 
 - - - - - - - - 
- - - - - - - - - 
 - - - - - - - - 
  - - - - - - - 
   W - - - - W
    W W W W W
```

None
```
6
------
    - - - - - --
   - - - - - - --
  - - - - - - - --
 - - - - - - - - --
- - - - - - - - -- -
- - - - - - - - -- 
-  - - - - - - -- 
-   - - - - - --
 -   W W W W W-
 ------
```

None
```
1
-
```

None
```
2
--
---
--
```

White loop
```
2
WW
W-W
WW
```

None
```
2
WW
WWW
WW
```

White loop
```
6
     W W W W W W
    - W - - - W -
   - - W - - W - -
  - - - W - W - - -
 - - - - W W - - - -
- - - - - W - - - - -
 - - - - W W - - - -
  - - - W - W - - -
   - - W - - W - -
    - W - - - W -
     W W W W W W
```
