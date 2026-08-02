//https://leetcode.com/problems/flip-binary-tree-to-match-preorder-traversal/description/

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
    List<Integer> ans = new ArrayList();
    int pos=0;
    private boolean solve(TreeNode root, int[] arr){
        if(root==null) return true;
        if(root.val!= arr[pos++]) return false;
        if(root.left!=null && root.left.val!=arr[pos]){
            ans.add(root.val);
            return solve(root.right, arr) && solve(root.left, arr); 
        }
        return solve(root.left, arr) && solve(root.right, arr);
    }

    public List<Integer> flipMatchVoyage(TreeNode root, int[] voyage) {
        return solve(root, voyage) ? ans : Arrays.asList(-1);
    }
}
