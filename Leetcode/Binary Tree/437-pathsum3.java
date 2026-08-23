class Solution {
    // Long one=1, zero=0;
    private int solve(Map<Long, Integer> mp, TreeNode root, int targetSum, Long sum){
        if(root==null) return 0;
        sum+=root.val;
        int ans=mp.getOrDefault(sum-targetSum, 0);
        mp.put(sum,mp.getOrDefault(sum, 0)+1);
        ans+=solve(mp, root.left, targetSum, sum);
        ans+=solve(mp, root.right, targetSum, sum);
        mp.put(sum,mp.getOrDefault(sum, 0)-1);
        return ans;
    }
    public int pathSum(TreeNode root, int targetSum) {
        Map<Long, Integer> mp = new HashMap();
        mp.put(0L, 1);
        return solve(mp, root, targetSum, 0L);
    }
}
