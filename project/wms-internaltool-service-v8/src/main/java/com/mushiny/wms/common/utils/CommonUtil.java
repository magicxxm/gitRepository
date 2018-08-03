package com.mushiny.wms.common.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by preston on 2017/5/4.
 */
public class CommonUtil {
    public static int maxThreshold;
    public static void setMaxThreshold(int threshold){
        maxThreshold = threshold;
    }
    public static float IsSeem(String item1,String item2){
        return getSimilarityRatio(item1,item2);
    }

    /**
     * 根据集合获取行和列
     * @param list 要计算的集合
     * @param column 指定的列数
     * @return
     */
    public static Map<String,Integer> getRowAnColumn(List list,int column){
        Map<String,Integer> map = new HashMap<>();
        //行
        int row = list.size()/column;
        int col = list.size()%column;
        if(col!=0){
            row=row+1;
        }
       // column = list.size()/row;
        map.put("column",column);
        map.put("row",row);
        return map;
    }

    private static int compare(String str, String target) {
        int d[][]; // 矩阵
        int n = str.length();
        int m = target.length();
        int i; // 遍历str的
        int j; // 遍历target的
        char ch1; // str的
        char ch2; // target的
        int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1

        if (n == 0) {
            return m;
        }

        if (m == 0) {
            return n;
        }

        d = new int[n + 1][m + 1];

        for (i = 0; i <= n; i++) { // 初始化第一列
            d[i][0] = i;
        }

        for (j = 0; j <= m; j++) { // 初始化第一行
            d[0][j] = j;
        }

        for (i = 1; i <= n; i++) { // 遍历str
            ch1 = str.charAt(i - 1);
            // 去匹配target
            for (j = 1; j <= m; j++) {
                ch2 = target.charAt(j - 1);
                if (ch1 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }

                // 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }

        return d[n][m];
    }

    public static float semilar(String str,String target){
        List<Character>  d;// 矩阵
        List<Character>  ds;// 矩阵
        int n = str.length();
        int m = target.length();
        int i; // 遍历str的
        int j; // 遍历target的
        char ch1; // str的
        char ch2; // target的

        if (n == 0) {
            return 0;
        }

        if (m == 0) {
            return 0;
        }

        d = new ArrayList<>();
        ds = new ArrayList<>();
        if(str.length()>target.length()){
            for (i = 0; i < str.length(); i++) { // 初始化第一列
                d.add(str.charAt(i));
            }
            for (i = 0; i < target.length(); i++) { // 初始化第一列
                ds.add(target.charAt(i));
            }
            m = d.size();
            n = ds.size();
        }else{
            for (i = 0; i < target.length(); i++) { // 初始化第一列
                d.add(target.charAt(i));
            }
            for (i = 0; i < str.length(); i++) { // 初始化第一列
                ds.add(str.charAt(i));
            }
            m = d.size();
            n = ds.size();
        }
        int temp = 0;
        for (i = 0; i < d.size(); i++) { // 遍历str
            ch1 = d.get(i);
            boolean flag = false;
            // 去匹配target
            for (j = 0; j < ds.size(); j++) {
                if(ch1==ds.get(j)){
                    System.out.println("a-->"+ch1);
                    d.remove(d.get(i));
                    ds.remove(ds.get(j));
                    j--;
                    i--;
                    break;
                }
            }
        }
        temp = d.size()+ds.size();
        int maxlength = m+n;
        System.out.println("temp-->"+temp+"/maxlength-->"+maxlength);
        return (1-((float)temp/(float)maxlength));
    }

    private static int min(int one, int two, int three) {
        return (one = one < two ? one : two) < three ? one : three;
    }

    /**
     * 获取两字符串的相似度
     *
     * @param str
     * @param target
     *
     * @return
     */

    public static float getSimilarityRatio(String str, String target) {
        return 1 - (float) compare(str, target) / Math.max(str.length(), target.length());
    }
}
