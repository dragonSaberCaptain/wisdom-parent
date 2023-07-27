package com.wisdom.tools.algorithm;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * 按位与( & )    同1为1,否则为0  1011 & 1111 = 1011
 * 按位或( | )    有1为1,否则为0  1011 & 1111 = 1111
 * 按位异或 ( ^ ) 相同为0,不同为1  1011 & 1111 = 0100
 * 左移( << ) 左移一位相当于乘以2的1次方，左移n位就相当于乘以2的n次方。
 * 右移( >> ) 右移一位相当于除以2的1次方，右移n位就相当于除以2的n次方。
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/11/1 17:16 星期一
 */
@Data
@Slf4j
@Accessors(chain = true)
public class MathUtil {
    /**
     * 算法题1：java 底层 加法 使用异或高效率运算
     * <p>
     * The sum of a and b
     *
     * @author captain
     * @datetime 2021-11-01 17:17:45
     */
    public int aplusb(int a, int b) {
        while (b != 0) {
            int tt = a & b;
            a = a ^ b;
            b = tt << 1;
        }
        return a;
    }

    /**
     * 算法题2：给定一个整数n，计算出n!(乘阶) 中尾部零的个数。
     * <p>
     * 素数中只有5*2可以产生0，因此只需要求5的个数即可得到尾数0的个数
     * <p>
     * An integer, denote the number of trailing zeros in n!
     *
     * @author captain
     * @datetime 2021-11-02 09:54:38
     */
    public long trailingZeros(long n) {
        long res = 0;
        while (n != 0) {
            res += n / 5;
            n /= 5;
        }
        return res;
    }

    /**
     * 算法题3：给定一个数字 k，计算 k 在 0 到 n 中出现的次数，k 可能是 0 到 9 的一个值。
     * An integer denote the count of digit k in 1..n
     */
    public int digitCounts(int k, int n) {
        int num = 1;
        for (int w = 10; w <= n; w++) {
            int x = w;
            while (x != 0) {
                int i = x % 10;
                if (k == i) {
                    num++;
                }
                x /= 10;
            }
        }
        return num;
    }

    /**
     * 贪心法：贪心算法（又称贪婪算法）是指，在对问题求解时，总是做出在当前看来是最好的选择。也就是说，不从整体最优上加以考虑，算法得到的是在某种意义上的局部最优解
     * <p>
     * 题:给定一个数字，在数字的任意位置插入一个5，使得插入后的这个数字最大。 应分正数和负数来考虑
     * <p>
     * 如果a是正数，我们要想尽办法让数字大，所以我们把5插入第一次遇见的比五小的数前面。
     * 如果a是负数，我们要想尽办法让去掉负号后的数字小，所以我们把5插入到第一次遇见的比五大的数前面。
     *
     * @param a A number
     * @return int Returns the maximum number after insertion
     * @author captain
     * @datetime 2021-12-21 14:55:10
     */
    public int insertFive(int a) {
        String strNum = String.valueOf(a);
        int index = 0;
        while (index < strNum.length()) {
            if (a >= 0 && strNum.charAt(index) < '5') break;
            else if (index != 0 && strNum.charAt(index) > '5') break;
            index += 1;
        }
        return Integer.parseInt(strNum.substring(0, index) + '5' + strNum.substring(index));
    }

    /**
     * 算法题2506:删除不匹配括号
     * A string which has been removed invalid parentheses
     *
     * @param s A string with lowercase letters and parentheses
     * @return java.lang.String
     * @author captain
     * @datetime 2022-07-28 14:29:32
     */
    public static String removeParentheses(String s) {
        if (s == null || (!s.contains("(") && !s.contains(")"))) return s;

        LinkedList<String> splitList = new LinkedList<>();

        boolean splitBl = false; //拆分标记
        int index = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ')' && !splitBl) {
                splitBl = true;
            } else if (s.charAt(i) == '(' && splitBl) {
                splitList.add(s.substring(index, i));
                index = i;
                splitBl = false;
            }
        }
        splitList.add(s.substring(index));

        StringBuilder sb = new StringBuilder(s.length());
        HashSet<Integer> set;
        LinkedList<Integer> list;
        for (String strSub : splitList) {
            set = new HashSet<>();//存储不配对的括号下标
            list = new LinkedList<>();//临时存储不配对下标
            for (int i = 0; i < strSub.length(); i++) {
                if (strSub.charAt(i) == '(') {
                    list.add(i);//记录存储左括号下标
                } else if (strSub.charAt(i) == ')') {
                    if (list.isEmpty()) {//list为空说明没有左括号下标存储
                        set.add(i);//存储不配对下标
                    } else {
                        list.remove(list.size() - 1);//括号配对匹配 进行配对抵消
                    }
                }
            }
            set.addAll(list);//添加不配对的下标
            for (int i = 0; i < strSub.length(); i++) {
                if (!set.contains(i)) {//判断进行字符串拼接操作
                    sb.append(strSub.charAt(i));
                }
            }
        }
        return sb.toString();
    }

    /**
     * 算法题1916：亮起时间最长的灯
     * The lamp has the longest liighting time.
     * 输入：
     * [[0,2],[1,5],[0,9],[2,15]]
     * 输出：
     * 'c'
     * 说明：
     * operation = `[[0, 2], [1, 5], [0, 9], [2, 15]]`
     * 在第0秒的时候，接收到指令`[0, 2]`，此时标号为 0 的灯亮起，第 2 秒的时候熄灭。此时 0号灯 的单次亮起时间为`2-0 = 2` 秒。
     * 在第2秒的时候，接收到指令`[1, 5]`，此时标号为 1 的灯亮起，第 5 秒的时候熄灭。此时 1号灯 的单次亮起时间为 `5-2 = 3` 秒。
     * 在第5秒的时候，接收到指令`[0, 9]`，此时标号为 0 的灯亮起，第 9 秒的时候熄灭。此时 0号灯 的单次亮起时间为 `9-5 = 4` 秒。
     * 在第9秒的时候，接收到指令`[2, 15]`，此时标号为 2 的灯亮起，第 15 秒的时候熄灭。此时 2号灯 的单次亮起时间为 `15-9 = 6` 秒。
     * 所以单次亮起的最长时间为 `max(2, 3, 4, 6) = 6` 秒，是标号为 `2` 的彩灯。
     * <p>
     * **你需要返回一个小写英文字母代表这个编号。`如 'a' 代表 0，'b' 代表 1，'c' 代表 2 ... 'z' 代表 25。`**
     * 所以你的答案应该是`'c'`
     *
     * @param operation: A list of operations.
     * @return char
     */
    public static char longestLightingTime(List<List<Integer>> operation) {
        int maxIdx = operation.get(0).get(0);
        int maxVal = operation.get(0).get(1);
        int preTime = 0;
        for (List<Integer> pair : operation) {
            int durtion = pair.get(1) - preTime;
            if (durtion > maxVal) {
                maxVal = durtion;
                maxIdx = pair.get(0);
            }
            preTime = pair.get(1);
        }

        return (char) ('a' + maxIdx);
    }

    /**
     * 算法题1914:聪明的销售
     * the result of the min item
     *
     * @param ids ID number of items
     * @param m   The largest number of items that can be remove
     * @return int
     * @author captain
     * @datetime 2022-07-28 14:31:21
     */
    public static int minItem(List<Integer> ids, int m) {
        Map<Integer, Integer> counts = new HashMap<>();
        for (int v : ids)
            counts.put(v, counts.getOrDefault(v, 0) + 1);
        List<Integer> clist = new ArrayList<>(counts.values());
        Collections.sort(clist);
        int rids = 0, roll = 0;
        for (int v : clist) {
            roll += v;
            if (roll > m) break;
            rids++;
        }
        return counts.size() - rids;
    }

    /**
     * 算法题1910 · 数组中出现次数最多的值
     * An interger.
     *
     * @param array: An array.
     * @return int
     * @author captain
     * @datetime 2022-07-28 14:32:23
     */
    public static int findNumber(int[] array) {
        int maxNum = 0; //最大出现次数
        int resultNum = 0; //返回的数
        Map<Integer, Integer> map = new HashMap<>();
        for (Integer tmp : array) {
            map.merge(tmp, 1, Integer::sum);
            Integer tmpNum = map.get(tmp);
            if (tmpNum == maxNum && tmp < resultNum) {
                resultNum = tmp;
            }
            if (tmpNum > maxNum) {
                maxNum = tmpNum;
                resultNum = tmp;
            }
        }
        return resultNum;
    }

    /**
     * 算法题1907 · 数组游戏
     * determine the number of moves to make all elements equals
     * 给定一个整数数组，请算出让所有元素相同的最小步数。每一步你可以选择一个元素，使得其他元素全部加1。
     * [3, 4, 6, 6, 3] -> [4, 5, 7, 6, 4] -> [5, 6, 7, 7, 5] -> [6, 7, 8, 7, 6] -> [7, 8, 8, 8, 7] -> [8, 9, 9, 8, 8] -> [9, 9, 10, 9, 9] -> [10, 10, 10, 10, 10]
     *
     * @param arr: the array
     * @return long
     * @author captain
     * @datetime 2022-07-28 14:33:16
     */
    public static long arrayGame(int[] arr) {
        int min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        long result = 0;
        for (int num : arr) {
            result += num - min;
        }
        return result;
    }

    /**
     * Returns the deleted string
     *
     * @param str: The first string given
     * @param sub: The given second string
     * @return java.lang.String
     * @author captain
     * @datetime 2022-07-28 14:34:14
     */
    public static String characterDeletion(String str, String sub) {
        Set<Character> subSet = new HashSet<>(); //去重
        for (char c : sub.toCharArray()) {
            subSet.add(c);
        }

        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (!subSet.contains(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 算法题1354 · 杨辉三角形II
     * the kth index row of the Pascal's triangle
     * 给定非负索引k，其中k≤33，返回杨辉三角形的第k个索引行  挑战：空间复杂度为O(k)
     * 提示：在杨辉三角中，每个数字是它上面两个数字的总和。
     * [1, 2, 1]
     *
     * @param rowIndex: a non-negative index
     * @return java.util.List<java.lang.Integer>
     * @author captain
     * @datetime 2022-07-28 14:35:03
     */
    public static List<Integer> getRow(int rowIndex) {
        List<Integer> row = new ArrayList<>();
        row.add(1);
        for (int i = 1; i <= rowIndex; ++i) {
            row.add(0);
            for (int j = i; j > 0; --j) {
                row.set(j, row.get(j) + row.get(j - 1));
            }
        }
        return row;
    }

    /**
     * 算法题1917 · 切割剩余金属
     * The function must return an integer that denotes the maximum possible profit.
     * (暴力尝试切割)
     *
     * @param costPerCut: integer cost to make a cut
     * @param salePrice:  integer per unit length sales price
     * @param lengths:    an array of integer rod lengths
     * @return int
     * @author captain
     * @datetime 2022-07-28 14:35:51
     */
    public static int maxProfit(int costPerCut, int salePrice, int[] lengths) {
        int profit = 0; //价格
        int maxLen = 0; //最大长度
        for (int j : lengths) {
            maxLen = Math.max(maxLen, j);
        }
        for (int length = 1; length <= maxLen; length++) {
            int cut = 0, pieces = 0;
            for (int j : lengths) {
                int curCut = (j - 1) / length;
                int curPiece = j / length;
                if (length * salePrice * curPiece - costPerCut * curCut > 0) {
                    cut += curCut;
                    pieces += curPiece;
                }
            }
            profit = Math.max(profit, length * salePrice * pieces - costPerCut * cut);
        }
        return profit;
    }

    /**
     * an integer denoting the maximum capacity of items that she can lift.
     *
     * @param weights:     An array of n integers, where the value of each element weights[i] is the weight of each plate i
     * @param maxCapacity: An integer, the capacity of the barbell
     * @return int
     * @author captain
     * @datetime 2022-07-28 14:36:34
     */
    public static int weightCapacity(int[] weights, int maxCapacity) {
        boolean[] dp = new boolean[maxCapacity + 1];
        dp[0] = true;
        int answer = 0;

        for (int weight : weights) {
            for (int j = maxCapacity; j >= weight; j--) {
                if (dp[j - weight]) {
                    dp[j] = true;
                    answer = Math.max(answer, j);
                }
            }
        }

        return answer;
    }

    //局部内部类
    int a = 0;

    /**
     * 2426 打印素数
     * 1≤n≤10000
     * 素数是除了 1 和它自身外，不能整除其他自然数的数
     *
     * @author captain
     * @datetime 2022-11-30 14:01:40
     */
    public static void printPrimeNumbers(int n) {
        for (int i = 1; i <= n; i += 2) { // 使用for循环遍历区间[2,n]
            int flag = 1; //flag为1的时候表明这个数是素数
            for (int a = 2; a <= Math.sqrt(i); a++) {
                if (0 == i % a) {
                    flag = 0;
                    break;
                }
            }
            if (flag == 1) {
                if (i == 1) {
                    System.out.println(2);
                } else {
                    System.out.println(i);
                }
            }
        }
    }

    /**
     * 1320 包含重复值
     *
     * @author captain
     * @datetime 2022-11-30 15:14:08
     */
    public static boolean containsDuplicate(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int x : nums) {
            if (!set.add(x)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 2168 · 打印三角九九乘法表
     *
     * @author captain
     * @datetime 2023-01-11 10:09:15
     */
    public static void multiplicationTable() {
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= i; j++) {
                System.out.print(j + "*" + i + "=" + (i * j) + " ");
            }
            System.out.println();
        }
    }

    /**
     * 输入: n = 100
     * 输出: 19
     * 解释:
     * 有8,18,28,38,48,58,68,78,80,81,82,83,84,85,86,87,88,89,98。
     *
     * @param n: count lucky numbers from 1 ~ n
     * @return the numbers of lucky number
     */
    public static int luckyNumber(int n) {
        int ans = 0;
        for (int i = 1; i <= n; i++) {
            int x = i;
            while (x != 0) {
                if (x % 10 == 8) {
                    ans++;
                    break;
                }
                x /= 10;
            }
        }
        return ans;
    }

    public static int luckyNumber2(int n) {
        int ans = 0;
        for (int i = 1; i <= n; i++) {
            if (Integer.toString(i).contains("8")) {
                ans += 1;
            }
        }
        return ans;
    }

    /**
     * @param trees: the positions of trees.
     * @param d:     the minimum beautiful interval.
     * @return the minimum number of trees to remove to make trees beautiful.
     * <p>
     * [1,2,3,5,6]
     */
    public static int treePlanning(int[] trees, int d) {
        int num = 0;
        int i = trees.length - 1;
        while (i > 0) {
            if (trees[i] - trees[i - 1] < d) {
                num += 1;
                i -= 2;
            } else {
                i -= 1;
            }
        }
        return num;
    }

    public static void main(String[] args) {
        MathUtil mathUtil = new MathUtil();
        //System.out.println(luckyNumber2(100));
        int[] trees = {1, 6, 11, 16, 21, 26};
        System.out.println(treePlanning(trees, 11));
    }

    public void method() {
        final int a = 20;
        class Inner {
            final int a1 = a;
            final int a2 = MathUtil.this.a;
        }
        Inner inner = new Inner();
        System.out.println(inner.a1);
        System.out.println(inner.a2);
    }
}
