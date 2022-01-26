package com.wisdom.tools.algorithm;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

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
	 *
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
	 *
	 * 素数中只有5*2可以产生0，因此只需要求5的个数即可得到尾数0的个数
	 *
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


	public static void main(String[] args) {
		MathUtil mathUtil = new MathUtil();
//		System.out.println(mathUtil.aplusb(234, 432));
//		System.out.println(mathUtil.insertFive(234));
//		System.out.println(mathUtil.test(5, 109));
//		System.out.println(mathUtil.digitCounts(5, 100));

		Scanner sc = new Scanner(System.in); //相似度匹配
	}
}
