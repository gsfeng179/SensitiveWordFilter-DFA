package com.gsfeng.wordfilter.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * 敏感词过滤器
 *
 * @author gsfeng
 * @date 2018/7/31 17:06
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SensitivewordFilter {
    /**
     * 敏感词库
     */
    @SuppressWarnings("rawtypes")
    private static Map sensitiveWordMap;
    /**
     * 最小匹配规则
     */
    public static int minMatchType = 1;
    /**
     * 最大匹配规则
     */
    public static int maxMatchType = 2;
    /**
     * 私有静态变量
     */
    private static SensitivewordFilter instance;

    static {
        sensitiveWordMap = new SensitiveWordInit().initKeyWord();
    }


    /**
     * 获取实例（双重检测锁）
     *
     * @return SensitivewordFilter
     */
    public static SensitivewordFilter getInstance() {
        if (instance == null) {
            synchronized (SensitivewordFilter.class) {
                if (instance == null) {
                    instance = new SensitivewordFilter();
                }
            }
        }
        return instance;
    }

    /**
     * 获取文字中的敏感词
     *
     * @param txt       文字
     * @param matchType 匹配规则。1：最小匹配规则，2：最大匹配规则
     * @return Set
     */
    public Set<String> getSensitiveWord(String txt, int matchType) {
        return getSensitiveWordSets(txt, matchType);
    }

    /**
     * 替换敏感字字符
     *
     * @param txt         文本
     * @param matchType   匹配规则
     * @param replaceChar 替换字符，默认*
     * @return String
     */
    public String replaceSensitiveWord(String txt, int matchType, String replaceChar) {
        String resultTxt = txt;
        // 获取所有的敏感词
        Set<String> sets = getSensitiveWord(txt, matchType);
        for (String str : sets) {
            String replaceString = getReplaceChars(replaceChar, str.length());
            resultTxt = resultTxt.replaceAll(str, replaceString);
        }
        return resultTxt;
    }

    /**
     * 获取替换字符串
     *
     * @param replaceChar 替换符
     * @param length      长度
     * @return String
     */
    private String getReplaceChars(String replaceChar, int length) {
        StringBuilder resultReplace = new StringBuilder(replaceChar);
        for (int i = 1; i < length; i++) {
            resultReplace.append(replaceChar);
        }
        return resultReplace.toString();
    }

    /**
     * 检查文字中是否包含敏感字符，检查规则如下
     *
     * @param txt        文本
     * @param beginIndex 开始位置
     * @param matchType  匹配规则
     * @return int 如果存在，则返回敏感词字符的长度，不存在返回0
     */
    public int checkSensitiveWord(String txt, int beginIndex, int matchType) {
        Set<String> sets = getSensitiveWordSets(txt, matchType);
        return sets.size();
    }

    private Set<String> getSensitiveWordSets(String txt, int matchType) {
        Set<String> sensitiveWordSets = new HashSet<>();
        for (int n = 0; n < txt.length(); n++) {
            // 判断是否包含敏感字符
            int length = judgeSensitiveWithIndex(txt, n, matchType);
            if (length > 0) {
                // 存在,加入list中
                sensitiveWordSets.add(txt.substring(n, n + length));
                // 减1的原因，是因为for会自增
                n = n + length - 1;
            }
        }
        return sensitiveWordSets;
    }

    /**
     * 根据指定位置是否是敏感词的开始
     *
     * @param txt        文本
     * @param beginIndex 开始位置
     * @param matchType  匹配规则
     * @return int
     */
    private int judgeSensitiveWithIndex(String txt, int beginIndex, int matchType) {
        // 匹配标识数默认为0
        int matchFlag = 0;
        char word;
        Map nowMap = sensitiveWordMap;
        for (int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            // 获取指定key
            nowMap = (Map) nowMap.get(word);
            // 存在，则判断是否为最后一个
            if (nowMap != null) {
                // 找到相应key，匹配标识+1
                matchFlag++;
                if ("1".equals(nowMap.get("isEnd"))) {
                    // 如果为最后一个匹配规则,结束循环，返回匹配标识数
                    break;
                }
            } else {
                // 不存在，直接返回
                break;
            }
        }
        // 长度必须大于等于1，为词
        if (matchFlag < 2) {
            matchFlag = 0;
        }
        return matchFlag;
    }
}
