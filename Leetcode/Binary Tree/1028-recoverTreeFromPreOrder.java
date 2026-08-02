//https://leetcode.com/problems/recover-a-tree-from-preorder-traversal/description/

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public TreeNode recoverFromPreorder(String traversal) {
        int len = traversal.length();
        Stack<TreeNode> st = new Stack();
        int i=0;
        TreeNode root=null, node;
        while(i<len){
            int val=0,lvl=0;
            while(traversal.charAt(i)=='-'){
                i++;
                lvl++;
            }
            
            while(i<len && traversal.charAt(i)!='-') val = val*10+traversal.charAt(i++)-'0';
            
            node = new TreeNode(val);
            
            if(root==null) root = node;
            
            while(st.size()>lvl) st.pop();
            
            if(!st.empty()){
                TreeNode cur = st.peek();
                if(cur.left==null) cur.left = node;
                else cur.right = node;
            }
            st.push(node);
        }
        
        return root;
    }
}
