package com.bankedmatsvalue;

public class Product implements Comparable<Product> {
    int id;
    int profit;

    Product(int id, int profit) {this.id = id; this.profit = profit;}

    @Override
    public int compareTo(Product p) {
        if (profit < p.profit) return -1;
        else if (profit > p.profit) return 1;
        else return 0;
    }
}
