package com.wisdom.tools.algorithm;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

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
public class MathUtil {
	/**
	 * java 底层 加法 使用异或高效率运算
	 *
	 * @author captain
	 * @datetime 2021-11-01 17:17:45
	 */
	public int add(int a, int b) {
		while (b != 0) {
			int _a = a ^ b;
			int _b = (a & b) << 1;
			a = _a;
			b = _b;
		}
		return a;
	}

	/**
	 * 给定一个整数n，计算出n!中尾部零的个数。
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
	 * 给定一个数字 k，计算 k 在 0 到 n 中出现的次数，k 可能是 0 到 9 的一个值。
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

	public int test(int num, int n) {
		int resNum = 0;

		int abs = 0;

		List<Integer> digitList = new ArrayList<>();
		do {
			int m = n % 10;
			if (digitList.size() == 0 && m >= num) {
				resNum = 1;
			}
			n = n / 10;
			if (n != 0) {
				digitList.add(m);
			} else {
				break;
			}
		} while (true);

		if (digitList.size() == 3) {
			abs = 20;
		}
		if (digitList.size() == 4) {
			abs = 300;
		}
		for (int i = 0; i < digitList.size(); i++) {
			Integer tmpNum = digitList.get(i);
			if (i == 0 && tmpNum >= 5) {
				resNum = resNum + 1;
			}

			if (i == 1 && tmpNum >= 5) {
				resNum = resNum + 1;
			}
		}
		return resNum;
	}

	public static void main(String[] args) {
		MathUtil mathUtil = new MathUtil();
		// 二位数 5 * 2^2
		// 三位数 5*2^3
//		System.out.println(mathUtil.test(5, 14));
		System.out.println(mathUtil.digitCounts(5, 11));
		System.out.println(mathUtil.digitCounts(5, 21));
		System.out.println(mathUtil.digitCounts(5, 31));
		System.out.println(mathUtil.digitCounts(5, 41));
		System.out.println(mathUtil.digitCounts(5, 51));
		System.out.println(mathUtil.digitCounts(5, 61));
		System.out.println(mathUtil.digitCounts(5, 71));
		System.out.println(mathUtil.digitCounts(5, 81));
		System.out.println(mathUtil.digitCounts(5, 91));
		System.out.println("--------------------5----------------------------------------");
		System.out.println(mathUtil.digitCounts(5, 19));
		System.out.println(mathUtil.digitCounts(5, 29));
		System.out.println(mathUtil.digitCounts(5, 39));
		System.out.println(mathUtil.digitCounts(5, 49));
		System.out.println(mathUtil.digitCounts(5, 59));
		System.out.println(mathUtil.digitCounts(5, 69));
		System.out.println(mathUtil.digitCounts(5, 79));
		System.out.println(mathUtil.digitCounts(5, 89));
		System.out.println(mathUtil.digitCounts(5, 99));
		System.out.println("--------------------5----------------------------------------");
		System.out.println(mathUtil.digitCounts(5, 111));
		System.out.println(mathUtil.digitCounts(5, 211));
		System.out.println(mathUtil.digitCounts(5, 311));
		System.out.println(mathUtil.digitCounts(5, 411));
		System.out.println(mathUtil.digitCounts(5, 511));
		System.out.println(mathUtil.digitCounts(5, 611));
		System.out.println(mathUtil.digitCounts(5, 711));
		System.out.println(mathUtil.digitCounts(5, 811));
		System.out.println(mathUtil.digitCounts(5, 911));
		System.out.println("--------------------5----------------------------------------");
		System.out.println(mathUtil.digitCounts(5, 199));
		System.out.println(mathUtil.digitCounts(5, 299));
		System.out.println(mathUtil.digitCounts(5, 399));
		System.out.println(mathUtil.digitCounts(5, 499));
		System.out.println(mathUtil.digitCounts(5, 599));
		System.out.println(mathUtil.digitCounts(5, 699));
		System.out.println(mathUtil.digitCounts(5, 799));
		System.out.println(mathUtil.digitCounts(5, 899));
		System.out.println(mathUtil.digitCounts(5, 999));

		System.out.println("--------------------5----------------------------------------");
		System.out.println(mathUtil.digitCounts(5, 1999));
		System.out.println(mathUtil.digitCounts(5, 2999));
		System.out.println(mathUtil.digitCounts(5, 3999));
		System.out.println(mathUtil.digitCounts(5, 4999));
		System.out.println(mathUtil.digitCounts(5, 5999));
		System.out.println(mathUtil.digitCounts(5, 6999));
		System.out.println(mathUtil.digitCounts(5, 7999));
		System.out.println(mathUtil.digitCounts(5, 8999));
		System.out.println(mathUtil.digitCounts(5, 9999));
		// 1 2 3 4 (10 9 8 7 6) 16 17 18 19
		// 1 2 3 4 5 6 7 8 (18 17 16 15 14 13 12 11 10)

		// 2 3 4 5 (12 13 14 15 16) 17 18 19 20
		// 2 3 4 5 6 7 8 9 20
		//公式：5 * 2 + bitNum - 5
		//公式：5 * 2 + bitNum - (5 - 2)
		//公式：9 * 2 + 9 - (9 - 2)
	}
}
