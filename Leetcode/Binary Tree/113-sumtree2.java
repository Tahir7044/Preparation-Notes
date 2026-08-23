class Solution {
    private void solve(TreeNode root, int targetSum, List<List<Integer>> ans, List<Integer> path){
        if(root==null) return;
        path.add(root.val);
        if(root.left==null && root.right==null && root.val==targetSum) ans.add(new ArrayList(path));
        solve(root.left, targetSum-root.val, ans, path);
        solve(root.right, targetSum-root.val, ans, path);
        path.remove(path.size()-1);
    }
    public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
        List<List<Integer>> ans = new ArrayList();
        List<Integer> path = new ArrayList();
        if(root==null) return ans;
        solve(root, targetSum, ans, path);
        return ans;
    }
}
