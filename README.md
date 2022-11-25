언어 : java
2022년 11월에 만듦
코드는 src > Node.java 에 있습니다.
회전알고리즘을 증명하고 코드 짜는데 3일동안 25시간 투자한 것 같습니다...



rotateAvl() 메소드의 알고리즘의 증명입니다.
LL법칙 (삽입)
![image](https://user-images.githubusercontent.com/84231143/203920795-3827576a-b23b-468b-8953-ac78f606e1f9.png)
![image](https://user-images.githubusercontent.com/84231143/203920820-797599f3-d265-4aeb-9fa3-8bf6f689ebd8.png)

LL법칙 (삭제)
![image](https://user-images.githubusercontent.com/84231143/203920909-c4b16bb0-5c3f-43cd-9d05-c178495c736d.png)
![image](https://user-images.githubusercontent.com/84231143/203923234-3c941cf7-84da-4ba7-b24f-5377bec2dd3a.png)

LR법칙 (삽입/삭제)
![image](https://user-images.githubusercontent.com/84231143/203923341-c7505083-4e3f-4bf0-8b61-8ba064613a84.png)
![image](https://user-images.githubusercontent.com/84231143/203923389-0cc37e55-3959-40e9-8eee-a9af67b1f6bd.png)




<회전 알고리즘의 법칙>

LL 법칙, RR법칙 (삽입 시)
> a : 불균형이 처음 발생한 노드
b:  a의 왼쪽(오른쪽) 자식
회전 후 r = a.right.height (r = a.left.height) 로 두면, 아래가 자명하다. (단, a가 루트면 r = -1)
a.h = r+1,   a.bf = 0
b.h = r+2,   b.bf = 0


LL법칙 (삭제 시)
> a : 불균형이 처음 발생한 노드
b:  a의 왼쪽 자식
회전 후 r = a.left.height 로 두면, 아래가 자명하다. (단, a가 루트면 r = -1)
a.h = r+1
b.h = r+2
if) b.bf == 0 then
    a.bf = 1
    b.bf = -1
if) b.bf == 1 then
    a.bf = 0
    b.bf = 0
    
    
> RR법칙 (삭제 시)
a : 불균형이 처음 발생한 노드
b: a의 오른쪽 자식
회전 후 r = a.right.height 로 두면, 아래가 자명하다. (단, a가 루트면 r = -1)
a.h = r+1
b.h = r+2
if) b.bf == 0  then
a.bf = -1
b.bf = 1
if)b.bf == -1  then
a.bf = 0
b.bf = 0


> RL법칙 (삽입, 삭제 시)
a : 불균형이 처음 발생한 노드
b:  a의 오른쪽 자식
c:  b의 왼쪽 자식
회전 후 r = b.right.height 로 두면, 아래가 자명하다. (단, a가 루트면 r = -1)
a.h = r+1
b.h = r+1
c.h = r+2
if) c.bf == 1 then
a.bf = 0
b.bf = -1
c.bf = 0
if) c.bf ==0 then
a.bf = 0
b.bf = 0
c.bf = 0
if) c.bf == 1 then
a.bf = 1
b.bf = 0
c.bf = 0


> LR법칙 (삽입, 삭제 시)
a : 불균형이 처음 발생한 노드
b:  a의 왼쪽 자식
c:  b의 오른쪽 자식
회전 후 r = b.left.height 로 두면, 아래가 자명하다. (단, a가 루트면 r = -1)
a.h = r+1
b.h = r+1
c.h = r+2
if) c.bf == 1 then
a.bf = -1
b.bf = 0
c.bf = 0
if) c.bf == 0 then
a.bf = 0
b.bf = 0
c.bf = 0
if) c.bf == 1 then
a.bf = 0
b.bf = 1
c.bf = 0
