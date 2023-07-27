package com.wisdom.tools.algorithm;

import cn.hutool.core.date.StopWatch;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Copyright © 2022 dragonSaberCaptain. All rights reserved.
 * <p>
 * 排序算法工具类
 * <p>
 * 排序法	    最好情形	    平均时间	    最差情形	    稳定度	空间复杂度
 * 冒泡排序	    O(n)	    O(n^2^)	    O(n^2^)	    稳定	O(1)
 * 快速排序	    O(nlogn)	O(nlogn)	O(n^2^)	    不稳定	O(nlogn)
 * 直接插入排序	O(n)	    O(n^2^)	    O(n^2^)	    稳定	O(1)
 * 希尔排序				                            不稳定	O(1)
 * 直接选择排序	O(n^2^)	    O(n^2^)	    O(n^2^)	    不稳定	O(1)
 * 堆排序	    O(nlogn)	O(nlogn)	O(nlogn)	不稳定	O(1)
 * 归并排序	    O(nlogn)	O(nlogn)	O(nlogn)	稳定	O(n + logn)
 *
 * @author captain
 * @version 1.0
 * @datetime 2022/7/14 10:03 星期四
 */
@Data
@Slf4j
@Accessors(chain = true)
public class SortUtil {
    /**
     * 交换排序-冒泡排序
     *
     * @param arr 待排序数组
     * @author captain
     * @datetime 2023-01-12 16:23:27
     */
    public static void bubbleSort(int[] arr) {
        if (arr == null || arr.length < 2) { //数组大小 小于2的直接返回
            return;
        }

        int tmp;
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                }
            }
        }

    }

    /**
     * 交换排序-冒泡排序 优化1
     * 说明:在某次交换排序后 集合是有序的(没有在发生交换)
     *
     * @param arr 待排序数组
     * @author captain
     * @datetime 2023-01-12 16:23:27
     */
    public static void bubbleSort1(int[] arr) {
        if (arr == null || arr.length < 2) { //数组大小 小于2的直接返回
            return;
        }

        int tmp;
        for (int i = 0; i < arr.length - 1; i++) {
            int flag = 1;
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                    flag = 0;
                }
            }
            // 没有发生交换，排序已经完成
            if (flag == 1) {
                return;
            }
        }
        return;
    }

    /**
     * 交换排序-冒泡排序 优化2
     * 说明:假设一次循环后数组第 i 个元素后所有元素已经有序，优化目标就是下次循环不再花费时间遍历已经有序的部分。
     * 比如 3、4、2、1、6、7、8 这个数组，第一次循环后，变为 3、2、1、4、6、7、8 的顺序
     *
     * @param arr 待排序数组
     * @author captain
     * @datetime 2023-01-12 16:23:27
     */
    public static void bubbleSort2(int[] arr) {
        if (arr == null || arr.length < 2) { //数组大小 小于2的直接返回
            return;
        }

        int temp;
        int len = arr.length - 1;
        while (len > 1) {
            //记录最后一次交换位置
            int lastChange = 0;
            for (int j = 0; j < len; j++) {
                if (arr[j] > arr[j + 1]) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    // 每交换一次更新一次
                    lastChange = j;
                }
            }
            // 没有发生交换，排序已经完成
            if (lastChange == 0) {
                return;
            }
            len = lastChange + 1;
        }
    }

    /**
     * 交换排序-快速排序
     *
     * @param arr        待排序数组
     * @param startIndex 开始下标
     * @param endIndex   结束下标
     * @author captain
     * @datetime 2023-01-12 17:45:36
     */
    public static void quickSort(int[] arr, int startIndex, int endIndex) {
        if (arr == null || arr.length < 2) { //数组大小 小于2的直接返回
            return;
        }
        if (startIndex < endIndex) {
            // 记录需要排序的下标
            int leftIndex = startIndex;
            int rightIndex = endIndex;
            int pivot = arr[leftIndex]; // 把数组中的首位数字作为基准数

            while (leftIndex <= rightIndex) {
                while (leftIndex <= rightIndex && arr[leftIndex] < pivot) { // 左边的数字比基准数小
                    leftIndex++;
                }

                while (leftIndex <= rightIndex && arr[rightIndex] > pivot) { // 右边的数字比基准数大
                    rightIndex--;
                }

                if (leftIndex <= rightIndex) {
                    int tmp = arr[leftIndex];
                    arr[leftIndex] = arr[rightIndex];
                    arr[rightIndex] = tmp;
                    leftIndex++;
                    rightIndex--;
                }
            }
            quickSort(arr, startIndex, rightIndex);
            quickSort(arr, leftIndex, endIndex);
        }
    }

    /**
     * 插入排序-直接插入排序
     *
     * @param arr 待排序数组
     * @author captain
     * @datetime 2023-01-12 17:45:25
     */
    public static void insertSort(int[] arr) {
        // 遍历所有数字
        for (int i = 1; i < arr.length - 1; i++) {
            // 当前数字比前一个数字小
            if (arr[i] < arr[i - 1]) {
                int j;
                // 把当前遍历的数字保存起来
                int temp = arr[i];
                for (j = i - 1; j >= 0 && arr[j] > temp; j--) {
                    // 前一个数字赋给后一个数字
                    arr[j + 1] = arr[j];
                }
                // 把临时变量赋给不满足条件的后一个元素
                arr[j + 1] = temp;
            }
        }
    }

    /**
     * 插入排序-希尔排序(缩小增量排序)
     *
     * @param arr 待排序数组
     * @author captain
     * @datetime 2023-01-12 17:48:39
     */
    public static void shellSort(int[] arr) {
        // gap 为步长，每次减为原来的一半。
        for (int gap = arr.length / 2; gap > 0; gap /= 2) {
            // 对每一组都执行直接插入排序
            for (int i = 0; i < gap; i++) {
                // 对本组数据执行直接插入排序
                for (int j = i + gap; j < arr.length; j += gap) {
                    // 如果 a[j] < a[j-gap]，则寻找 a[j] 位置，并将后面数据的位置都后移
                    if (arr[j] < arr[j - gap]) {
                        int k;
                        int temp = arr[j];
                        for (k = j - gap; k >= 0 && arr[k] > temp; k -= gap) {
                            arr[k + gap] = arr[k];
                        }
                        arr[k + gap] = temp;
                    }
                }
            }
        }
    }

    /**
     * 选择排序-简单选择排序
     *
     * @param arr 待排序数组
     * @author captain
     * @datetime 2023-01-12 17:49:52
     */
    public static void selectSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int minIndex = i;
            // 把当前遍历的数和后面所有的数进行比较，并记录下最小的数的下标
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIndex]) {
                    // 记录最小的数的下标
                    minIndex = j;
                }
            }
            // 如果最小的数和当前遍历的下标不一致，则交换
            if (i != minIndex) {
                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
            }
        }
    }

    /**
     * 选择排序-堆排序
     *
     * @param arr 待排序数组
     * @author captain
     * @datetime 2023-01-12 17:51:09
     */
    public static void heapSort(int[] arr) {
        // 开始位置是最后一个非叶子结点，即最后一个结点的父结点
        int start = (arr.length - 1) / 2;
        // 调整为大顶堆
        for (int i = start; i >= 0; i--) {
            maxHeap(arr, arr.length, i);
        }
        // 先把数组中第 0 个位置的数和堆中最后一个数交换位置，再把前面的处理为大顶堆
        for (int i = arr.length - 1; i > 0; i--) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            maxHeap(arr, i, 0);
        }

    }

    /**
     * 归并排序
     *
     * @param arr        待排序数组
     * @param startIndex 开始下标
     * @param endIndex   结束下标
     * @author captain
     * @datetime 2023-01-12 17:55:13
     */
    public static void mergeSort(int[] arr, int startIndex, int endIndex) {
        int middle = (endIndex + startIndex) / 2;
        if (startIndex < endIndex) {
            // 处理左边数组
            mergeSort(arr, startIndex, middle);
            // 处理右边数组
            mergeSort(arr, middle + 1, endIndex);
            // 归并
            merge(arr, startIndex, middle, endIndex);
        }

    }

    /**
     * 基数排序
     *
     * @param arr 待排序数组
     * @author captain
     * @datetime 2023-01-12 17:57:32
     */
    public static void radixSort(int[] arr) {
        // 存放数组中的最大数字
        int max = Integer.MIN_VALUE;
        for (int value : arr) {
            if (value > max) {
                max = value;
            }
        }
        // 计算最大数字是几位数
        int maxLength = (max + "").length();
        // 用于临时存储数据
        int[][] temp = new int[10][arr.length];
        // 用于记录在 temp 中相应的下标存放数字的数量
        int[] counts = new int[10];
        // 根据最大长度的数决定比较次数
        for (int i = 0, n = 1; i < maxLength; i++, n *= 10) {
            // 每一个数字分别计算余数
            for (int j = 0; j < arr.length; j++) {
                // 计算余数
                int remainder = arr[j] / n % 10;
                // 把当前遍历的数据放到指定的数组中
                temp[remainder][counts[remainder]] = arr[j];
                // 记录数量
                counts[remainder]++;
            }
            // 记录取的元素需要放的位置
            int index = 0;
            // 把数字取出来
            for (int k = 0; k < counts.length; k++) {
                // 记录数量的数组中当前余数记录的数量不为 0
                if (counts[k] != 0) {
                    // 循环取出元素
                    for (int l = 0; l < counts[k]; l++) {
                        arr[index] = temp[k][l];
                        // 记录下一个位置
                        index++;
                    }
                    // 把数量置空
                    counts[k] = 0;
                }
            }
        }
    }

    /**
     * 转化为大顶堆
     *
     * @param arr   待转化数组
     * @param size  待调整的区间长度
     * @param index 结点下标
     * @author captain
     * @datetime 2023-01-12 18:11:38
     */
    public static void maxHeap(int[] arr, int size, int index) {
        // 左子结点
        int leftNode = 2 * index + 1;
        // 右子结点
        int rightNode = 2 * index + 2;
        int max = index;
        // 和两个子结点分别对比，找出最大的结点
        if (leftNode < size && arr[leftNode] > arr[max]) {
            max = leftNode;
        }
        if (rightNode < size && arr[rightNode] > arr[max]) {
            max = rightNode;
        }
        // 交换位置
        if (max != index) {
            int temp = arr[index];
            arr[index] = arr[max];
            arr[max] = temp;
            // 因为交换位置后有可能使子树不满足大顶堆条件，所以要对子树进行调整
            maxHeap(arr, size, max);
        }
    }

    /**
     * 合并数组
     *
     * @param arr    待合并数组
     * @param low
     * @param middle
     * @param high
     * @author captain
     * @datetime 2023-01-12 18:13:30
     */
    public static void merge(int[] arr, int low, int middle, int high) {
        // 用于存储归并后的临时数组
        int[] temp = new int[high - low + 1];
        // 记录第一个数组中需要遍历的下标
        int i = low;
        // 记录第二个数组中需要遍历的下标
        int j = middle + 1;
        // 记录在临时数组中存放的下标
        int index = 0;
        // 遍历两个数组，取出小的数字，放入临时数组中
        while (i <= middle && j <= high) {
            // 第一个数组的数据更小
            if (arr[i] <= arr[j]) {
                // 把更小的数据放入临时数组中
                temp[index] = arr[i];
                // 下标向后移动一位
                i++;
            } else {
                temp[index] = arr[j];
                j++;
            }
            index++;
        }
        // 处理剩余未比较的数据
        while (i <= middle) {
            temp[index] = arr[i];
            i++;
            index++;
        }
        while (j <= high) {
            temp[index] = arr[j];
            j++;
            index++;
        }
        // 把临时数组中的数据重新放入原数组
        for (int k = 0; k < temp.length; k++) {
            arr[k + low] = temp[k];
        }
    }

    public static void quickSort(int[] arr) {
        quickSort(arr, 0, arr.length - 1);
    }

    public static void mergeSort(int[] arr) {
        mergeSort(arr, 0, arr.length - 1);
    }

    public static void main(String[] args) throws Exception {
        int size = 1000;
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = new Random().nextInt(size);
        }
        System.out.println("待排序数据:" + Arrays.toString(array));
        int[] arr0 = array.clone();
        int[] arr1 = array.clone();
        int[] arr2 = array.clone();
        int[] arr3 = array.clone();
        int[] arr4 = array.clone();
        int[] arr5 = array.clone();
        int[] arr6 = array.clone();
        int[] arr7 = array.clone();
        int[] arr8 = array.clone();
        int[] arr9 = array.clone();

        StopWatch sw = new StopWatch();

        sw.start("交换排序-冒泡排序");
        bubbleSort(arr0);
        sw.stop();

        sw.start("交换排序-冒泡排序优化1");
        bubbleSort1(arr1);
        sw.stop();

        sw.start("交换排序-冒泡排序优化2");
        bubbleSort2(arr2);
        sw.stop();

        sw.start("交换排序-快速排序");
        quickSort(arr3);
        sw.stop();

        sw.start("插入排序-直接插入排序");
        insertSort(arr4);
        sw.stop();

        sw.start("插入排序-希尔排序(缩小增量排序)");
        shellSort(arr5);
        sw.stop();

        sw.start("选择排序-简单选择排序");
        selectSort(arr6);
        sw.stop();

        sw.start("选择排序-堆排序");
        heapSort(arr7);
        sw.stop();

        sw.start("归并排序");
        mergeSort(arr8);
        sw.stop();

        sw.start("基数排序");
        radixSort(arr9);
        sw.stop();

        System.out.println(sw.prettyPrint(TimeUnit.MICROSECONDS));
    }
}
