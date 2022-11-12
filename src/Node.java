import java.io.*;
import java.util.Stack;


//두개 이상 리턴 값을 보내야 하는 상황에 Container 객체 사용할 예정.
class Container {
    Node fbb, fbbp; //fbb : first balance broke  fbbp: fbb의 부모
    String rotation; //회전 방향
    boolean success; //삽입 또는 삭제 성공
    boolean thisis_fbb; //현재 노드가 fbb 인지.
}

public class Node {
    Node left; //왼쪽 자식 (작음)
    int key; //현재 노드의 값
    Node right; //오른쪽 자식 (큼)
    int height; //얘를 root로 가지는 subtree의 height
    int bf; // 1 0 -1 순으로
    boolean emptyTree; //빈트리인지 확인 (쩔수없이 추가)
    String rotationType; //회전방향


    //생성자 (루트 만들때 사용할 예정)
    public Node() {
        this.left = null;
        this.right = null;
        this.emptyTree = true;
    }

    //생성자 (새 노드 만들때 사용할 예정)
    public Node(int newKey) {
        this.key = newKey;
        this.height = 0;
        this.left = null;
        this.right = null;
        this.emptyTree = false;
    }

    //생성자 (깊은 복사할 때 사용할 예정)
    public Node(Node left, int key, Node right, int height, int bf) {
        this.left = left;
        this.key = key;
        this.right = right;
        this.height = height;
        this.bf = bf;
        this.emptyTree = false;
    }

    //출력
    void printAVL(String rotationType) {
        System.out.print(rotationType + " ");
        inorderAVL(this);
        System.out.println();
    }

    //inorder 알고리즘
    static void inorderAVL(Node T) { //왼쪽 > 중간 > 오른쪽
        if (T.left != null) {
            inorderAVL(T.left);
        }
        //빈트리인 경우엔 출력하면 안 되므로.(빈트리가 되어도 내가 key값 null로 못 만들어서)
        if (T.emptyTree == false) System.out.print("(" + T.key + ", " + T.bf + ") ");
        if (T.right != null) {
            inorderAVL(T.right);
        }
    }


    //높이 갱신 알고리즘
    int calHeight() {
        Node l = this.left;
        Node r = this.right;

        if (l == null && r == null) return -1;
        else if (l == null) return r.height;
        else if (r == null) return l.height;

        if (l.emptyTree && r.emptyTree) return -1;
        else if (l.emptyTree) return r.height;
        else if (r.emptyTree) return l.height;
        return Math.max(l.height, r.height);
    }

    //bf 갱신 알고리즘
    int calc_bf(Node q) {

        int curr_left, curr_right;
        //왼쪽 자식의 높이
        if (q.left == null || q.left.emptyTree) curr_left = 0;
        else curr_left = q.left.height + 1;

        //오른쪽 자식의 높이
        if (q.right == null || q.right.emptyTree) curr_right = 0;
        else curr_right = q.right.height + 1;

        //현 노드의 bf
        return curr_left - curr_right;
    }



    //어떤 회전 해야 하는지 check 할 알고리즘
    void checkBalance(Container con, Stack<Node> arr) {

        //현 노드의 thisis_fbb 초기화
        con.thisis_fbb = false;

        Node q = arr.pop();

        //회전 방향
        String rotation;

        //현 노드의 height 갱신
        q.height = 1 + q.calHeight();

        //현 노드의 bf 갱신
        q.bf = calc_bf(q);


        //현 노드의 bf가 범위를 벗어난 첫번째 노드라면, 어떤 회전을 해야할지 결정함.
        if (con.fbb == null && (q.bf < -1 || q.bf > 1)) {
            con.fbb = q;

            //arr에서 fbb의 부모 보기.
            if (arr.size() > 0) con.fbbp = arr.peek();
            //arr에서 꺼낼게 없다면 fbb가 루트노드임을 의미. (즉, fbbp == null)


            // 회전 방향 결정하기
            //불균형의 bf가 왼쪽에서 더 컸던 거라면, (L-)
            if (1 < con.fbb.bf) {
                //그 왼쪽 자식이 오른쪽이 더 크다면 (LR)
                if (con.fbb.left.bf < 0) rotation = "LR";
                else rotation = "LL";
            }

            //불균형의 bf가 오른쪽에서 더 컸던 거라면, (R-)
            else {
                if (con.fbb.right.bf > 0) rotation = "RL";
                else rotation = "RR";
            }
            con.rotation = rotation;

            //현재 노드가 첫 불균형 노드임을 표시.
            con.thisis_fbb = true;
        }

        // 불군형 노드가 아닌 경우 (fbb가 무조건 갱신되지 않음.)
        else if (con.fbb == null){
            con.rotation = "NO";
        }

        //이미 불균형노드의 회전방향이 결정된 경우는 rotation 값을 수정하면 안 된다.
    }

    //회전 알고리즘
    void rotateAVL(Node p, Node q, String type, boolean now_insert) {
        //p: 불균형이 일어난 노드 / q: p의 부모 / T: fbb가 루트였을 때, 부모 역할 대신. (fbb : first_balance_broke)


        //불균형노드의 부모와 불균형노드를 연결할 때, 필요함.
        boolean p_is_right_child = false;

        //p의 부모가 있다면,
        if (q != null) {
            //p가 q의 오른쪽 자식이면,
            if (q.right.key == p.key) p_is_right_child = true;
            else p_is_right_child = false;
        }
        //p가 루트인 경우, 무한 포인터 반복이 일어나므로 해당 루트 노드를 깊은 복사한다.
        else {
            Node root_p = new Node(p.left, p.key, p.right, p.height, p.bf);
            p = root_p; //깊은 복사한 p
        }

        Node a, b;
        Node c = null; //RR,LL이면 초기화가 안 되므로 기본값 null 넣기

        if (type.equals("LL")) {
            a = p;
            b = p.left;
            a.left = b.right;
            b.right = a;

            //삽입 - LL 법칙
            if (now_insert) {
                a.bf = 0;
                b.bf = 0;

                //높이 갱신
                int r;
                //자식이 없는경우엔 +1 하면 안 됨.
                if (a.left == null) r = -1;
                else r = a.right.height;

                a.height = r + 1;
                b.height = r + 2;
            }

            //삭제 - LL 법칙
            else {
                if (b.bf == 0) {
                    a.bf = 1;
                    b.bf = -1;
                } else if (b.bf == 1) {
                    a.bf = 0;
                    b.bf = 0;
                }

                //높이 갱신
                int r;
                //자식이 없는 경우
                if (a.left == null) r = -1;
                else r = a.left.height;

                a.height = r + 1;
                b.height = r + 2;
            }


            this.match_parent_child(b, c, q, p_is_right_child);
        } else if (type.equals("RR")) {
            a = p;
            b = p.right;
            a.right = b.left;
            b.left = a;

            //삽입 - RR 법칙
            if (now_insert) {
                a.bf = 0;
                b.bf = 0;

                //높이 갱신
                int r;
                //자식이 없는경우엔 +1 하면 안 됨.
                if (a.left == null) r = -1;
                else r = a.left.height;

                a.height = r + 1;
                b.height = r + 2;
            }

            //삭제 - RR 법칙
            else {
                if (b.bf == 0) {
                    a.bf = -1;
                    b.bf = 1;
                } else if (b.bf == -1) {
                    a.bf = 0;
                    b.bf = 0;
                }

                //높이 갱신
                int r;
                //자식이 없는 경우
                if (a.right == null) r = -1;
                else r = a.right.height;

                a.height = r + 1;
                b.height = r + 2;
            }


            this.match_parent_child(b, c, q, p_is_right_child);
        } else if (type.equals("RL")) {
            a = p;
            b = p.right;
            c = b.left;

            b.left = c.right;
            a.right = c.left;
            c.right = b;
            c.left = a;

            //아래는 자명한 사실임.
            //기준 높이 (r)
            int r;

            //자식이 없는경우엔 +1 하면 안 됨.
            if (b.right == null) r = -1;
            else r = b.right.height;

            b.height = r + 1;
            a.height = r + 1;
            c.height = r + 2;

            if (c.bf == 1) {
                b.bf = -1;
                a.bf = 0;
                c.bf = 0;
            } else if (c.bf == 0) {
                b.bf = 0;
                a.bf = 0;
                c.bf = 0;
            } else {
                b.bf = 0;
                a.bf = 1;
                c.bf = 0;
            }
            this.match_parent_child(b, c, q, p_is_right_child);
        }

        //LR
        else {
            a = p;
            b = p.left;
            c = p.left.right;

            b.right = c.left;
            a.left = c.right;
            c.left = b;
            c.right = a;


            //아래는 자명한 사실임.
            //기준 높이 (r)
            int r;

            //자식이 없는경우엔 +1 하면 안 됨.
            if (b.left == null) r = -1;
            else r = b.left.height;

            b.height = r + 1;
            a.height = r + 1;
            c.height = r + 2;

            if (c.bf == 1) {
                b.bf = 0;
                a.bf = -1;
                c.bf = 0;
            } else if (c.bf == 0) {
                b.bf = 0;
                a.bf = 0;
                c.bf = 0;
            } else {
                b.bf = 1;
                a.bf = 0;
                c.bf = 0;
            }

            this.match_parent_child(b, c, q, p_is_right_child);
        }
    }

    //회전 후, 불균형 노드와 그의 부모를 연결해 줄 메소드.
    void match_parent_child(Node b, Node c, Node q, boolean p_is_right_child) {

        //RR, LL
        if (c == null) {
            //불균형있던 애의 부모의 자식을 균형이 맞춰진 자식으로 다시 연결. (부모가 있다면)
            if (q != null) {
                if (p_is_right_child) q.right = b;
                else q.left = b;
            }
            //불균형 노드가 루트였다면,
            else {
                //루트 변경
                this.key = b.key;
                this.right = b.right;
                this.left = b.left;
                this.height = b.height;
                this.bf = b.bf;
            }
        }

        //RL, LR
        else {
            //불균형있던 애의 부모의 자식을 균형이 맞춰진 자식으로 다시 연결. (부모가 있다면)
            if (q != null) {
                if (p_is_right_child) q.right = c;
                else q.left = c;
            }
            //불균형 노드가 루트였다면,
            else {
                //루트 변경
                this.key = c.key;
                this.right = c.right;
                this.left = c.left;
                this.height = c.height;
                this.bf = c.bf;
            }
        }
    }

    //BST 삽입 알고리즘
    Stack<Node> insertBST(int newKey, Container con) {

        Node p = this;  //현 노드
        Node q = null;  //p의 부모
        Stack<Node> arr = new Stack<>(); //지나온 길 저장

        //BST 방법으로 일단 삽입
        while (p != null && !this.emptyTree) {
            //이미 존재하는 키.
            if (newKey == p.key) {
                System.out.println("i " + newKey + " : The key already exists");
                con.success = false;
                return null;
            }
            q = p;
            arr.push(q);
            if (newKey < p.key) p = p.left;
            else p = p.right;
        }


        // 삽입할 위치 찾음 !! //

        //새 노드 객체 생성.
        Node newNode = new Node(newKey);

        //새 노드 삽입하기.
        //빈 트리였다면, 현재 트리의 key에 newKey를 대입.
        if (this.emptyTree == true) {
            this.key = newKey;
            emptyTree = false;
        }

        //빈 트리가 아니면 새 노드를 q의 자식으로 넣어야함.
        else if (newKey < q.key) q.left = newNode; //부모보다 작으면 왼쪽 자식으로
        else q.right = newNode; //부모보다 크면 오른쪽 자식으로

        //삽입 성공
        con.success = true;

        return arr;
    }

    //AVL 삽입 알고리즘
    void insertAVL(int newKey) {
        Container con = new Container();
        Stack<Node> arr = insertBST(newKey, con);

        Node first_balance_broke = null;  //처음 불균형이 발생한 노드
        Node first_balance_broke_parent;  //x의 부모

        //stack에서 노드 빼면서 height, bf 갱신.
        while (arr != null && !arr.empty()) {

            checkBalance(con, arr);

            first_balance_broke = con.fbb;
            first_balance_broke_parent = con.fbbp;

            rotationType = con.rotation;

            // 첫 불균형 노드인 경우에만 아래를 실행하도록.
            if (con.thisis_fbb) {
                //회전 시키기.
                rotateAVL(first_balance_broke, first_balance_broke_parent, rotationType, true);
            }
        }

        // 최종 출력 //

        //삽입 실패했다면,
        if (!con.success) this.printAVL("");

        //(first_balance_broke==null)이란 뜻은 불균형이 일어난 노드가 없다.
        else if (first_balance_broke == null) this.printAVL("NO");

        //불균형이 일어난 노드가 있다면
        else this.printAVL(rotationType);

    }



    //minNode 알고리즘(오른쪽 자식의 트리 중 가장 작은 노드)
    static Node minNode(Node T, Stack<Node> arr) {
        Node temp = T;
        while (temp.left != null) {
            arr.push(temp);
            temp = temp.left;
        }
        return temp;
    }

    //maxNode 알고리즘(왼쪽 자식의 트리 중 가장 큰 노드)
    static Node maxNode(Node T, Stack<Node> arr) {
        Node temp = T;
        while (temp.right != null) {
            arr.push(temp);
            temp = temp.right;
        }
        return temp;
    }

    //noNdoe 알고리즘(트리의 총 노드의 수.)
    static int noNode(Node T, int cnt) {
        //null도 아니고, 빈 노드도 아닌 경우
        if (T.left != null) {
            if (!T.left.emptyTree) noNode(T.left, cnt++);
        }
        cnt++;
        if (T.right != null) {
            if (!T.right.emptyTree) noNode(T.right, cnt++);
        }
        return cnt;
    }

    //BST 삭제 알고리즘
    Stack<Node> deleteBST(int deleteKey, Container con) {
        Node p = this;
        Node q = null;
        Stack<Node> arr = new Stack<>(); //stack에 지나온 길 저장

        //삭제할 노드의 위치 찾기
        //탐색을 끝까지했거나, 찾는기가 현재키와 같지 않는동안
        while ((p != null) && (deleteKey != p.key)) {
            q = p; //1 level 내려옴
            arr.push(q);
            //찾는키가 현재키보다 작다면 왼쪽으로 이동. 크다면 오른쪽으로 이동.
            if (deleteKey < p.key) p = p.left;
            else p = p.right;
        }

        //while문을 나온이유. 1)삭제할노드가 없었음. 2)삭제할노드 찾음.
        //삭제할노드가 없었다면. 삭제 실패.
        if (p == null) {
            System.out.println("d " + deleteKey + " : The key does not exist");
            con.success = false;
            return null;
        }


        //삭제할 노드를 찾음
        //자식이 둘다 있는 노드였다면
        if (p.left != null && p.right != null) {
            arr.push(p);
            Node tempNode;
            int flag = -1; //1이 오른쪽
            //오른쪽 자식의 높이가 더 크다면, 오른쪽 자식을 루트로
            if (p.left.height < p.right.height) {
                tempNode = minNode(p.right, arr);
                flag = 1;
            }
            //왼쪽 자식의 높이가 더 크다면, 왼쪽 자식을 루트로
            else if (p.left.height > p.right.height) {
                tempNode = maxNode(p.left, arr);
                flag = 0;
            }
            //왼쪽과 오른쪽 자식의 높이가 같다면, 노드가 더 많은 트리로.
            else {
                if (noNode(p.left, 0) > noNode(p.right, 0)) {
                    tempNode = maxNode(p.left, arr);
                    flag = 0;
                } else {
                    tempNode = minNode(p.right, arr);
                    flag = 1;
                }
            }
            //자리바꾸기.
            p.key = tempNode.key;
            //삭제된 자리로 올라간 leaf 노드 삭제.
            if (flag == 0) {
                //p.left에서 삭제하려는 값이 루트노드라면 그냥 부모와 연을 끊으면 됨
                if (p.left.key == tempNode.key) {
                    p.left = tempNode.left;
                    //리턴값
                }
                //p.left에 삭제하려는 값 이 루트노드가 아니면, 타고 내려감
                else p.left.deleteBST(tempNode.key, con);
            } else {
                if (p.right.key == tempNode.key) {
                    p.right = tempNode.right;
                    //리턴값
                } else p.right.deleteBST(tempNode.key, con);
            }
        }

        //자식이 없는 노드였다면,
        else if (p.left == null && p.right == null) {
            //루트 노드이면 루트를 null로 만들기.
            if (q == null) {
                this.emptyTree = true;
                this.height = -1; //자식 중 하나는 emptytree이고, 하나는 null인 경우에 emptytree의 높이를 참조하므로
            }
            //루트노드가 아니고, 왼쪽 자식이였다면, 부모의 왼쪽자식 null
            else if (q.left == p) {
                q.left = null;
            }
            //루트노드가 아니고, 오른쪽 자식이였다면, 부모의 오른쪽자식 null
            else {
                q.right = null;
            }
        }

        //자식이 하나 있는 노드였다면,
        else {
            //왼쪽 자식이 있는노드
            if (p.left != null) {
                //현재노드가 루트노드였다면, 내 왼쪽자식을 루트로 갱신.
                if (q == null) {
//                    System.out.println(p);
                    arr.push(p);
                    this.key = this.left.key;
                    //height 갱신(this.right를 갱신하기전에 height를 손봐야함)
                    this.height = this.left.height;
                    //순서가 중요함. this.left가 매개니까 this.left는 나중에 수정해야함
                    this.right = this.left.right;
                    this.left = this.left.left;
                }
                //루트노드가 아니고, 왼쪽자식이였다면, 내 부모를 내자식과 연결.
                else if (q.left == p) q.left = p.left;
                    //루트노드가 아니고, 오른쪽자식이였다면, 내 부모를 내자식과 연결.
                else q.right = p.left;
            }
            //오른쪽 자식이 있는 노드 (위와 동일)
            else {
                //현재노드가 루트노드였다면, 내 오른쪽 자식을 루트로 갱신.
                if (q == null) {
                    arr.push(p);
                    this.key = this.right.key;
                    //height 갱신
                    this.height = this.right.height;
                    //순서가 중요함. this.right가 매개니까 this.right는 나중에 수정해야함
                    this.left = this.right.left;
                    this.right = this.right.right;
                } else if (q.left == p) q.left = p.right;
                else q.right = p.right;
            }
        }

        //삭제 성공
        con.success = true;

        return arr;
    }

    //AVL 삭제 알고리즘
    void deleteAVL(int deleteKey) {
        Container con = new Container();
        Stack<Node> arr = deleteBST(deleteKey, con);

        Node first_balance_broke = null;    //처음 불균형이 발생한 노드
        Node first_balance_broke_parent;    //x의 부모

        while (arr != null && !arr.empty()) {
            checkBalance(con, arr);

            first_balance_broke = con.fbb;
            first_balance_broke_parent = con.fbbp;

            rotationType = con.rotation;


            // 첫 불균형 노드인 경우에만 아래를 실행하도록.
            if (con.thisis_fbb) {
                //회전 시키기.
                rotateAVL(first_balance_broke, first_balance_broke_parent, rotationType, false);
            }
        }


        // 최종 출력 //

        //삭제 실패 했다면
        if (!con.success) this.printAVL("");

        //(first_balance_broke==null)이란 뜻은 불균형이 일어난 노드가 없다.
        else if (first_balance_broke == null) this.printAVL("NO");

        //불균형이 일어난 노드가 있다면
        else this.printAVL(rotationType);
    }


    public static void main(String[] args) throws IOException {
        Node root = new Node();

        File file = new File("C:\\Users\\hyeeu\\OneDrive\\바탕 화면\\AVL\\src\\input.txt");

        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line;
        String order;
        int value;
        while ((line = bufferedReader.readLine()) != null) {

            String[] arr = line.split(" ");
            order = arr[0]; //i 또는 d
            value = Integer.parseInt(arr[1]); //숫자

            //삽입
            if (order.equals("i")) root.insertAVL(value);

            //삭제
            else root.deleteAVL(value);

        }
    }
}
