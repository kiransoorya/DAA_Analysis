public class RodCutting {

    public static void main(String[] args) {
        int[] prices = {1, 5, 8, 9, 10, 17, 17, 20}; // Example prices for rod lengths 1 to 8
        int n = prices.length;

        int[] revenue = new int[n + 1];
        int[] cutIndices = new int[n + 1];

        rodCutting(prices, n, revenue, cutIndices);

        System.out.println("Maximum revenue: " + revenue[n]);
        System.out.print("Cut indices: ");
        printCutIndices(n, cutIndices);
    }

    private static void rodCutting(int[] prices, int n, int[] revenue, int[] cutIndices) {
        revenue[0] = 0;

        for (int i = 1; i <= n; i++) {
            int maxRevenue = Integer.MIN_VALUE;
            for (int j = 1; j <= i; j++) {
                if (maxRevenue < prices[j - 1] + revenue[i - j]) {
                    maxRevenue = prices[j - 1] + revenue[i - j];
                    cutIndices[i] = j;
                }
            }
            revenue[i] = maxRevenue;
        }
    }

    private static void printCutIndices(int n, int[] cutIndices) {
        while (n > 0) {
            System.out.print(cutIndices[n] + " ");
            n = n - cutIndices[n];
        }
        System.out.println();
    }
}
