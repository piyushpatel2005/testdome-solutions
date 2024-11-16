package com.github.piyushpatel2005;

public class MegaStore {
    public enum DiscountType {
        Standard,
        Seasonal,
        Weight;
    }

    public static double getDiscountedPrice(double cartWeight,
                                            double totalPrice,
                                            DiscountType discountType) {
        double discountPrice = 0;
        switch (discountType) {
            case Standard:
                discountPrice = totalPrice * (1 - 0.06);
                break;
            case Seasonal:
                discountPrice = totalPrice * (1 - 0.12);
                break;
            case Weight:
                discountPrice = cartWeight > 10 ? totalPrice * (1 - 0.18) : totalPrice * (1 - 0.06);
                break;
            default:
                discountPrice = totalPrice;
        }
        return discountPrice;
    }

    public static void main(String[] args) {
        System.out.println(getDiscountedPrice(12, 100, DiscountType.Weight));
    }
}
