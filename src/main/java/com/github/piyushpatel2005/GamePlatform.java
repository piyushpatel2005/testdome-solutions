package com.github.piyushpatel2005;

public class GamePlatform {
    public static double calculateFinalSpeed(double initialSpeed, int[] inclinations) {
        for (int inclination: inclinations) {
            initialSpeed -= inclination;
            if (initialSpeed <= 0) {
                return 0.0;
            }
        }
        return initialSpeed;
    }

    public static void main(String[] args) {
        System.out.println(calculateFinalSpeed(60.0, new int[]{0, 30, 0, -45, 0}));
    }
}
