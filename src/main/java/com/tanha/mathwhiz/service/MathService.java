package com.tanha.mathwhiz.service;

public class MathService {
    public static int isPrime(int number) {
        if (number < 2) return 0;
        if (number == 2) return 1;
        if (number % 2 == 0) return 0;
        for (int i = 3; i <= number / i; i += 2) {
            if (number % i == 0) return 0;
        }
        return 1;
    }

    public static int calculateMod(int a, int b) {
        if (b == 0) throw new IllegalArgumentException("The divisor cannot be zero.");
        return a % b;
    }

    public static double calculateSin(double degree) {
        return Math.sin(Math.toRadians(degree));
    }

    public static double calculateCos(double degree) {
        return Math.cos(Math.toRadians(degree));
    }

    public static double calculateTan(double degree) {
        double cosine = calculateCos(degree);
        if (Math.abs(cosine) < 1.0e-10) {
            throw new IllegalArgumentException("Tangent is undefined at this angle.");
        }
        return Math.tan(Math.toRadians(degree));
    }

    public static boolean nearlyEqual(double actual, double expected) {
        return Double.isFinite(actual) && Math.abs(actual - expected) <= 0.01;
    }
}
