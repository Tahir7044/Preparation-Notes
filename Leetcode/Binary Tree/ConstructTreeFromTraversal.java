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
    
    int pos=0;
    // BST from Pre order
    private TreeNode buildTree(int[] ar, int low, int high){
        if(pos>=ar.length || ar[pos]<low || ar[pos]>high) {
            return null;
        }
        TreeNode node = new TreeNode(ar[pos++]);
        node.left = buildTree(ar, low, node.val);
        node.right = buildTree(ar, node.val, high);
        return node;
    }

    public TreeNode bstFromPreorder(int[] preorder) {
        pos=0;
        return buildTree(preorder, -1000000, 1000000);
    }

  private TreeNode PostInorder(int []postorder, Map<Integer,Integer> mp, int []pos, int l, int h){
        if(pos[0]<0 || l>h) return null;
        int root = postorder[pos[0]];
        TreeNode node = new TreeNode(root);
        int mid = mp.get(root);
        pos[0]--;
        node.right = solve(postorder, mp, pos, mid+1, h);
        node.left = solve(postorder, mp, pos, l, mid-1);
        return node;
    }

  // Post and In order
    public TreeNode buildTree(int[] inorder, int[] postorder) {
        Map<Integer,Integer> mp = new HashMap();
        int n=postorder.length;
        int pos[] = new int[1];
        pos[0]=n-1;
        for(int i=0;i<n;i++){
            mp.put(inorder[i],i);
        }
        return PostInorder(postorder, mp, pos, 0, n-1);
    }
  
   //----------------------------------------------------------------------------------------------
    private TreeNode PreInorder(int []pre, Map<Integer,Integer> mp, int []pos, int l, int h){
        if(pos[0]>=pre.length || l>h){
            return null;
        }
        int root = pre[pos[0]];
        TreeNode node = new TreeNode(root);
        int mid = mp.get(root);
        pos[0]++;
        node.left = solve(pre, mp, pos, l, mid-1);
        node.right = solve(pre, mp, pos, mid+1, h);
        return node;
    }

    // Pre and In order
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        Map<Integer,Integer> mp = new HashMap();
        int n=preorder.length;
        int pos[] = new int[1];
        for(int i=0;i<n;i++){
            mp.put(inorder[i],i);
        }
        return PreInorder(preorder, mp, pos, 0, n-1);
    }
  
    int preInd = 0, postInd = 0;
    // from post and pre order
    public TreeNode constructFromPrePost(int[] preorder, int[] postorder) {
        TreeNode root = new TreeNode(preorder[preInd++]);
        if(root.val!=postorder[postInd]) root.left = constructFromPrePost(preorder, postorder);
        if(root.val!=postorder[postInd]) root.right = constructFromPrePost(preorder, postorder);
        postInd++;
        return root;
    }
}
