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


    // 235. Lowest Common Ancestor of a Binary Search Tree
    // https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/description
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if(root==null) return null;
        if(root.val==p.val || root.val==q.val) return root;
        TreeNode left = null, right=null;
        if(root.val > Math.max(p.val, q.val)) left = lowestCommonAncestor(root.left, p, q);
        else if(root.val < Math.min(p.val, q.val)) right = lowestCommonAncestor(root.right, p, q);
        else {
            left = lowestCommonAncestor(root.left, p, q);
            right = lowestCommonAncestor(root.right, p, q);
        }
        if(left!=null && right!=null) return root;
        return left!=null? left: right;
    }

  
    private TreeNode lca(TreeNode root, TreeNode a, TreeNode b){
        if(root==null) return root;
        if(root.val==a.val || root.val==b.val) return root;
        TreeNode left = lca(root.left, a,b);
        TreeNode right = lca(root.right, a,b);
        if(left!=null && right!=null) return root;
        return left!=null? left : right;
    }

  // 1123. Lowest Common Ancestor of Deepest Leaves
  // 236. Lowest Common Ancestor of a Binary Tree
  // https://leetcode.com/problems/lowest-common-ancestor-of-deepest-leaves/description
    public TreeNode lcaDeepestLeaves(TreeNode root) {
        Queue<TreeNode> q = new LinkedList();
        TreeNode a=null, b=null;
        q.add(root);
        while(!q.isEmpty()){
            List<TreeNode> par = new ArrayList();
            int len = q.size();
            for(int i=0;i<len;i++){
                TreeNode cur = q.poll();
                par.add(cur);
                if(cur.left!=null) q.add(cur.left);
                if(cur.right!=null) q.add(cur.right);
            }

            if(q.isEmpty())
            for(TreeNode x: par){
                if(a==null) a=x;
                b=x;
            }
        }

        return lca(root,a,b);
    }

    private int FindMax(TreeNode root, int min, int max) {
        if(root==null) return max-min;
        min = Math.min(root.val, min);
        max = Math.max(root.val, max);
        return Math.max(solve(root.left, min, max), solve(root.right, min, max));

    }

    // 1026. Maximum Difference Between Node and Ancestor
    public int maxAncestorDiff(TreeNode root) {
        return FindMax(root, root.val , root.val);
    }
}
