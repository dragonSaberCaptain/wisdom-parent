package com.wisdom.tools.algorithm;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * Copyright © 2022 dragonSaberCaptain. All rights reserved.
 * <p>
 * 排序算法工具类
 *
 * @author captain
 * @version 1.0
 * @datetime 2022/7/14 10:03 星期四
 */
@Data
@Slf4j
@Accessors(chain = true)
public class SortUtil {
    public static void main(String[] args) {
        bubbleSort(new int[]{2, 1, 3, 1, 2, 2, 5, 4, 8, 9, 8, 8, 9, 0, 6, 4, 2, 1, 2, 4, 5, 6, 7, 8, 9, 0, 0, 9, 8, 7, 6, 5, 4, 3, 1, 1});
    }

    /**
     * 冒泡排序
     */
    public static void bubbleSort(int[] array) {
        if (array == null || array.length < 2) { //数组大小 小于2的直接返回
            return;
        }

        int tmp;
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    tmp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = tmp;
                }
            }
        }
    }
}
