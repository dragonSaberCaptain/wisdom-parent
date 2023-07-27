package com.wisdom.tools.blast;

import java.io.File;
import java.util.Scanner;

/**
 * @author captain
 * @version 1.0
 * @datetime 2023/1/29 16:31 星期日
 */
public class BlastDto {
    /**
     * 最小口令长度
     */
    long blastLenMin = 4;
    /**
     * 最大口令长度
     */
    long blastLenMax = 4;

    /**
     * 所有大写拉丁文(A-Z)
     */
    boolean capitalLatin = true;
    /**
     * 所有小写拉丁文(a-z)
     */
    boolean smallLatin = true;

    /**
     * 所有数字(0-9)
     */
    boolean allNumbers = true;

    /**
     * 所有特殊符号(!@...)
     */
    boolean allSpecialSymbols = false;
    /**
     * 空格
     */
    boolean space = false;

    /**
     * 所有可打印字符
     */
    boolean allChar = false;

    /**
     * 文件绝对路径
     */
    File srcFile;

    /**
     * 字典绝对路径
     */
    String dictPath;


    public BlastDto(Scanner sc) {
        System.out.println("是否启用自定义配置 y/n:");
        String isDef = sc.nextLine();
        if ("y".equalsIgnoreCase(isDef)) {
            customConfig(sc);
        }
        fileConfig(sc);
    }

    public void customConfig(Scanner sc) {
        System.out.println("是否启用所有大写拉丁文(A-Z) y/n:");
        this.capitalLatin = "y".equalsIgnoreCase(sc.nextLine());

        System.out.println("是否启用所有小写拉丁文(a-z) y/n:");
        this.smallLatin = "y".equalsIgnoreCase(sc.nextLine());

        System.out.println("是否启用所有数字(0-9) y/n:");
        this.allNumbers = "y".equalsIgnoreCase(sc.nextLine());

        System.out.println("是否启用所有特殊符号(!@...) y/n:");
        this.allSpecialSymbols = "y".equalsIgnoreCase(sc.nextLine());

        System.out.println("是否启用空格 y/n:");
        this.space = "y".equalsIgnoreCase(sc.nextLine());

        System.out.println("是否启用所有可打印字符 y/n:");
        this.allChar = "y".equalsIgnoreCase(sc.nextLine());

        System.out.println("请输入最小口令长度:");
        this.blastLenMin = sc.nextLong();

        System.out.println("请输入最大口令长度:");
        this.blastLenMax = sc.nextLong();

        System.out.println("配置完成！！！");
    }

    public void fileConfig(Scanner sc) {
        File srcFile;
        while (true) {
            System.out.println("请输入文件路径:");
            String filePath = sc.nextLine();
            if (null == filePath || "".equals(filePath)) {
                System.out.println("请输入正确的文件路径！");
            } else {
                //获取当前压缩文件
                srcFile = new File(filePath);
                // 判断源文件是否存在
                if (!srcFile.exists()) {
                    System.out.println(srcFile.getPath() + "所指文件不存在,请重新输入");
                } else {
                    this.srcFile = srcFile;
                    break;
                }
            }
        }
    }

    public long getBlastLenMin() {
        return blastLenMin;
    }

    public void setBlastLenMin(long blastLenMin) {
        this.blastLenMin = blastLenMin;
    }

    public long getBlastLenMax() {
        return blastLenMax;
    }

    public void setBlastLenMax(long blastLenMax) {
        this.blastLenMax = blastLenMax;
    }

    public boolean isCapitalLatin() {
        return capitalLatin;
    }

    public void setCapitalLatin(boolean capitalLatin) {
        this.capitalLatin = capitalLatin;
    }

    public boolean isSmallLatin() {
        return smallLatin;
    }

    public void setSmallLatin(boolean smallLatin) {
        this.smallLatin = smallLatin;
    }

    public boolean isAllNumbers() {
        return allNumbers;
    }

    public void setAllNumbers(boolean allNumbers) {
        this.allNumbers = allNumbers;
    }

    public boolean isAllSpecialSymbols() {
        return allSpecialSymbols;
    }

    public void setAllSpecialSymbols(boolean allSpecialSymbols) {
        this.allSpecialSymbols = allSpecialSymbols;
    }

    public boolean isSpace() {
        return space;
    }

    public void setSpace(boolean space) {
        this.space = space;
    }

    public boolean isAllChar() {
        return allChar;
    }

    public void setAllChar(boolean allChar) {
        this.allChar = allChar;
    }

    public File getSrcFile() {
        return srcFile;
    }

    public void setSrcFile(File srcFile) {
        this.srcFile = srcFile;
    }

    public String getDictPath() {
        return dictPath;
    }

    public void setDictPath(String dictPath) {
        this.dictPath = dictPath;
    }
}
