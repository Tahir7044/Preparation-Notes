class Main {
    
    private static int solve(int pos, int m, int n, int[][] count, int[][][] dp){
        if(pos>=count.length) return 0;
        int count_z = count[pos][0];
        int count_o = count[pos][1];
        if(dp[pos][m][n]!=-1) return dp[pos][m][n];
        int ans = solve(pos+1, m,n,count, dp);
        if(m>=count_z && n>=count_o){
            ans = Math.max(ans, solve(pos+1, m-count_z, n-count_o, count, dp)+1);
        }
        
        return dp[pos][m][n] = ans;
        
    }

    private static int findMaxSubSet(String[] str, int m, int n){
    
        int len = str.length;
        int[][] count = new int[len][2];
        int[][][] dp = new int[len][m+2][n+2];
        
        for(int i=0;i<=n;i++){
            for(int j=0;j<=m;j++){
                for(int k=0;k<len;k++)
                dp[k][j][i]=-1;
            }
        }
        
        for(int i=0;i<len;i++) {
            String st = str[i];
            for(int j=0;j<st.length();j++){
                if(st.charAt(j)=='0'){
                    count[i][0]++;
                } else {
                    count[i][1]++;
                }
            }
        }
        
        return solve(0, m,n, count, dp);
    
    }


    public static void main(String[] args) {
        String[] str = new String[]{"000", "111","10","10", "10"};
        int m=3,n=3;
        System.out.println(findMaxSubSet(str, m,n));
    }

}
