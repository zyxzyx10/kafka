package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public int drippingRain(int[] args) {
        int index_slow = 0;
//        int index_fast = 1;
        int result = 0;
        for (int i = 0; i < args.length; i++) {
            if (args[index_slow] <= args[i]) {
                for (int ii = index_slow; ii < i; ii++) {
                    result += args[index_slow] - args[ii];
                }
                index_slow = i;
            }
        }
        return result;
    }

    int drippingRain2(int[] args) {
        int water = 0;
        int leftIndex = 0;
        int rightIndex = args.length - 1;
        int maxLeft = args[leftIndex];
        int maxRight = args[rightIndex];
        while (leftIndex < rightIndex) {
            if (maxLeft < maxRight) {
                leftIndex++;
                maxLeft = Math.max(maxLeft, args[leftIndex]);
                water += maxLeft - args[leftIndex];
            } else {
                rightIndex--;
                maxRight = Math.max(maxRight, args[rightIndex]);
                water += maxRight - args[rightIndex];
            }
        }
        return water;
    }

    int drippingRain3(int[] args) {
        int left = 0;
        int right = args.length - 1;
        int maxleft = args[left];
        int maxright = args[right];
        int water = 0;

        while(left < right) {
            if(args[left] < args[right]) {
                left++;
                maxleft = Math.max(maxleft, args[left]);
                water += maxleft - args[left];
            } else {
                right--;
                maxright = Math.max(maxright, args[right]);
                water += maxright - args[right];
            }
        }
        return water;
    }

    public static void main(String[] args) {
        System.out.println(new Main().drippingRain(new int[]{4,2,0,3,2,5}));
        System.out.println(new Main().drippingRain(new int[]{0,1,0,2,1,0,1,3,2,1,2,1}));
        System.out.println(new Main().drippingRain2(new int[]{4,2,0,3,2,5}));
        System.out.println(new Main().drippingRain2(new int[]{0,1,0,2,1,0,1,3,2,1,2,1}));
        System.out.println(new Main().drippingRain3(new int[]{4,2,0,3,2,5}));
        System.out.println(new Main().drippingRain3(new int[]{0,1,0,2,1,0,1,3,2,1,2,1}));
    }
}