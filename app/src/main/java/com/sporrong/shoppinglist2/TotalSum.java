package com.sporrong.shoppinglist2;

public class TotalSum {
    private static int totalSum;

    public int getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(int totalSum) {
        this.totalSum = totalSum;
    }

    public void addTototalSum(int newPrice) {
        totalSum = totalSum + newPrice;
    }

    public void drawFromTotalSum(int price) {
        totalSum = totalSum - price;
    }
}
