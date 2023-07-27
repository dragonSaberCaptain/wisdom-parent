package com.wisdom.tools.Thread;

import java.util.Arrays;
import java.util.List;

/**
 * TODO (用一句话描述该类的作用)
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/1/31 14:57 星期二
 */
public class ListFactory {
    private final List<String> data;
    private final int pageSize = 20;
    private int curPage = 0;

    public ListFactory() {
        String[] arr = new String[]{"9", "1", "2", "1", "3", "5", "8", "3", "9", "6", "8", "7", "5", "5", "1", "1", "5", "4", "9", "4", "2", "7", "8", "8", "5", "1", "4", "3", "8", "1", "2", "4", "2", "4", "4", "8", "8", "4", "4", "5", "3", "3", "6", "4", "7", "6", "5", "4", "1", "8", "4", "3", "6", "7", "7", "4", "5", "3", "9", "9", "5", "5", "6", "1", "4", "1", "9", "3", "5", "1", "1", "7", "9", "4", "9", "2", "8", "7", "6", "6", "4", "1", "6", "6", "9", "1", "5", "2", "1", "1", "8", "1", "6", "3", "1", "8", "4"};
        this.data = Arrays.asList(arr);
        System.out.println("全部数据 " + this.data);
    }

    public static void main(String[] args) {
        ListFactory listFactory = new ListFactory();
        for (int i = 0; i < 10; i++) {
            List<String> data = listFactory.getNext();
            if (null == data) {
                break;
            } else {
                System.out.println(data);
            }
        }
    }

    public List<String> getNext() {
        int start = curPage * pageSize;

        //数据取完了
        if (start > data.size()) {
            return null;
        }

        int end = Math.min((start + pageSize), data.size());
        curPage += 1; //下一页
        return this.data.subList(start, end);
    }

    public int getSize() {
        return this.data.size();
    }

    public int getCurPage() {
        return this.curPage;
    }
}