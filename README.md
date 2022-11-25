## 화일_처리 AVL 코드
- 언어 : JAVA
- 2022년 11월에 만듦. (2학년 2학기)
- 회전알고리즘을 증명하고 코드까지 짜는데 3일동안 25시간 투자함. 머리아팠지만 재밌었닿
 
 </br>
🚀 코드 위치  </br>

-  코드는 **src > Node.java** 에 있습니다.
</br></br></br></br>



# 🔔 AVL 트리 회전 알고리즘 증명
</br></br>

## 📌LL법칙 (삽입) </br>
<image src="https://user-images.githubusercontent.com/84231143/203920795-3827576a-b23b-468b-8953-ac78f606e1f9.png" width="48%"> <image src="https://user-images.githubusercontent.com/84231143/203920820-797599f3-d265-4aeb-9fa3-8bf6f689ebd8.png" width="48%"> </br>
    
</br></br>
## 📌 LL법칙 (삭제) </br>
<image src="https://user-images.githubusercontent.com/84231143/203920909-c4b16bb0-5c3f-43cd-9d05-c178495c736d.png" width="48%"> <image src="https://user-images.githubusercontent.com/84231143/203923234-3c941cf7-84da-4ba7-b24f-5377bec2dd3a.png" width="48%"> </br>
    
</br></br>
## 📌 LR법칙 (삽입/삭제) </br>
<image src="https://user-images.githubusercontent.com/84231143/203923341-c7505083-4e3f-4bf0-8b61-8ba064613a84.png" width="48%"> <image src="https://user-images.githubusercontent.com/84231143/203923389-0cc37e55-3959-40e9-8eee-a9af67b1f6bd.png" width="48%"> </br></br></br>
 


</br></br></br></br>
# 🔔 rotateAvl() 코드 구현
</br></br> 
### LL 법칙, RR법칙 (삽입 시)
> a : 불균형이 처음 발생한 노드 </br>
b:  a의 왼쪽(오른쪽) 자식 </br>
회전 후 r = a.right.height (r = a.left.height) 로 두면, 아래가 자명하다. (단, a가 루트면 r = -1) </br>
a.h = r+1,   a.bf = 0 </br>
b.h = r+2,   b.bf = 0 </br>


### LL법칙 (삭제 시)
> a : 불균형이 처음 발생한 노드 </br>
b:  a의 왼쪽 자식 </br>
회전 후 r = a.left.height 로 두면, 아래가 자명하다. (단, a가 루트면 r = -1) </br>
a.h = r+1 </br> 
b.h = r+2 </br>
if) b.bf == 0 then </br>
    a.bf = 1 </br>
    b.bf = -1 </br>
if) b.bf == 1 then </br>
    a.bf = 0 </br>
    b.bf = 0 </br>
    
    
### RR법칙 (삭제 시)
> a : 불균형이 처음 발생한 노드 </br>
b: a의 오른쪽 자식 </br>
회전 후 r = a.right.height 로 두면, 아래가 자명하다. (단, a가 루트면 r = -1) </br>
a.h = r+1 </br>
b.h = r+2 </br>
if) b.bf == 0  then </br>
a.bf = -1 </br>
b.bf = 1 </br>
if)b.bf == -1  then </br> 
a.bf = 0 </br>
b.bf = 0 </br>


### RL법칙 (삽입, 삭제 시) 
> a : 불균형이 처음 발생한 노드 </br>
b:  a의 오른쪽 자식 </br>
c:  b의 왼쪽 자식 </br>
회전 후 r = b.right.height 로 두면, 아래가 자명하다. (단, a가 루트면 r = -1) </br>
a.h = r+1 </br>
b.h = r+1 </br>
c.h = r+2 </br>
if) c.bf == 1 then </br>
a.bf = 0 </br>
b.bf = -1 </br>
c.bf = 0 </br>
if) c.bf ==0 then </br>
a.bf = 0 </br>
b.bf = 0 </br>
c.bf = 0 </br>
if) c.bf == 1 then </br>
a.bf = 1 </br>
b.bf = 0 </br>
c.bf = 0 </br>


### LR법칙 (삽입, 삭제 시)
> a : 불균형이 처음 발생한 노드 </br>
b:  a의 왼쪽 자식 </br>
c:  b의 오른쪽 자식 </br>
회전 후 r = b.left.height 로 두면, 아래가 자명하다. (단, a가 루트면 r = -1) </br>
a.h = r+1 </br>
b.h = r+1 </br>
c.h = r+2 </br>
if) c.bf == 1 then </br>
a.bf = -1  </br>
b.bf = 0 </br>
c.bf = 0 </br>
if) c.bf == 0 then </br>
a.bf = 0 </br>
b.bf = 0 </br>
c.bf = 0 </br>
if) c.bf == 1 then </br>
a.bf = 0 </br>
b.bf = 1 </br>
c.bf = 0 </br>
