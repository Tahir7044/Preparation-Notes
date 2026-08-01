//https://leetcode.com/problems/binary-tree-inorder-traversal/description/

class Two {
    public TreeNode node;
    public int count;

    Two(TreeNode node, int count) {
        this.node = node;
        this.count = count;
    }
}

class Solution {
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
