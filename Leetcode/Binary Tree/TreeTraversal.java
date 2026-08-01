//https://leetcode.com/problems/binary-tree-inorder-traversal/description/

class Two {
    public TreeNode node;
    public int count;

    Two(TreeNode node, int count) {
        this.node = node;
        this.count = count;
    }
}

 class Cord{
    int val;
    int col;
    Cord(int val, int col){
        this.col=col;
        this.val=val;
    }
 }

class Solution {

    private void solve(TreeNode root, SortedMap<Integer,PriorityQueue<Cord>> mp, int col, int row){
        if(root==null) return;
        if(!mp.containsKey(col)){
            mp.put(col, new PriorityQueue<Cord>((a,b)-> {
                if(a.col==b.col) return a.val-b.val;
                return a.col-b.col;
            }) );
        }
        mp.get(col).add(new Cord(root.val, row));
        solve(root.left, mp, col-1, row+1);
        solve(root.right, mp, col+1, row+1);
    }

    // Verticle order traversal
    public List<List<Integer>> verticalTraversal(TreeNode root) {
        SortedMap<Integer,PriorityQueue<Cord>> mp = new TreeMap();
        solve(root,mp,0,0);
        List<List<Integer>> ans = new ArrayList();
        for(int key: mp.keySet()){
            List<Integer> temp = new ArrayList();
            PriorityQueue<Cord> curr = mp.get(key);
            while(!curr.isEmpty())
            temp.add(curr.poll().val);
            ans.add(temp);
        }
        return ans;
    }

    // Level oder traversal
    public List<List<Integer>> levelOrder(TreeNode root) {
        Queue<TreeNode> pq = new LinkedList();
        List<List<Integer>> ans = new ArrayList();
        if(root==null) return ans;
        pq.add(root);
        while(pq.size()!=0){
            List<Integer> lvl = new ArrayList();
            int n = pq.size();
            for(int i=0;i<n;i++){
                TreeNode cur = pq.poll();
                lvl.add(cur.val);
                if(cur.left!=null){
                    pq.add(cur.left);
                }
                if(cur.right!=null){
                    pq.add(cur.right);
                }
            }
            ans.add(lvl);
        }
        return ans;
    }

    // Post order traversal
    public List<Integer> postorderTraversal(TreeNode root) {
        Stack<Two> tree = new Stack();
        List<Integer> ans = new ArrayList();

        if(root==null) return ans;
        tree.push(new Two(root, 1));
        while(tree.size()!=0){
            Two cur = tree.pop();
            if(cur.count==2){
                ans.add(cur.node.val);
            }
            if(cur.count==1){
                cur.count++;
                tree.push(cur);
                if(cur.node.right!=null) tree.push(new Two(cur.node.right, 1));
                if(cur.node.left!=null) tree.push(new Two(cur.node.left, 1));
            }
        }
        return ans;
    }
    // Pre order traversal
    public List<Integer> preorderTraversal(TreeNode root) {
        Stack<Two> st = new Stack<Two>();
        List<Integer> ans = new ArrayList();
        if(root==null) return ans;
        st.push(new Two(root,1));
        while(!st.empty()){
            Two cur = st.pop();
            if(cur.count==1){
                ans.add(cur.node.val);
                if(cur.node.right!=null) st.push(new Two(cur.node.right,1));
                if(cur.node.left!=null) st.push(new Two(cur.node.left,1));
            }
        }
        return ans;
    }

    // In order traversal
    public List<Integer> inorderTraversal(TreeNode root) {
        Stack<Two> st = new Stack<Two>();
        List<Integer> ans = new ArrayList();
        if (root == null)
            return ans;
        st.push(new Two(root, 1));
        while (!st.empty()) {
            Two cur = st.pop();
            if (cur.count == 2) {
                ans.add(cur.node.val);
            } else {
                if (cur.node.right != null) {
                    st.push(new Two(cur.node.right, 1));
                }
                cur.count++;
                st.push(cur);
                if (cur.node.left != null) {
                    st.push(new Two(cur.node.left, 1));
                }
            }
        }
        return ans;
    }
}
