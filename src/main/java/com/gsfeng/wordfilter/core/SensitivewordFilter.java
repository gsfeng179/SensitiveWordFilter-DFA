package com.gsfeng.wordfilter.core;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by gsfeng on 2017/6/21.
 */
public class SensitivewordFilter {
    @SuppressWarnings("rawtypes")
    private static Map sensitiveWordMap = null;
    public static int minMatchTYpe = 1; // 最小匹配规则
    public static int maxMatchType = 2; // 最大匹配规则

    /**
     *
     * <p>
     * <b>方法描述：</b>初始化敏感词库
     * </p>
     *
     * @since
     * @return
     */
    static {
        sensitiveWordMap = new SensitiveWordInit().initKeyWord();
    }

    // 1.一个私有的指向自己的静态变量
    private static SensitivewordFilter instance;

    // 2.私有的构造方法,保证不能从外部创建对象
    private SensitivewordFilter() {
    }

    // 3.公开的静态工厂方法,返回该类的唯一实例(当发现没有实例没有初始化的时候才初始化)
    public static SensitivewordFilter getInstance() {
        if (instance == null) {
            instance = new SensitivewordFilter();
        }
        return instance;
    }

    /**
     * 获取文字中的敏感词
     *
     * @param txt       文字
     * @param matchType 匹配规则&nbsp;1：最小匹配规则，2：最大匹配规则
     * @return
     * @version 1.0
     */
    public Set<String> getSensitiveWord(String txt, int matchType) {
        Set<String> sensitiveWordList = new HashSet<String>();

        for (int i = 0; i < txt.length(); i++) {
            int length = CheckSensitiveWord(txt, i, matchType); // 判断是否包含敏感字符
            if (length > 0) { // 存在,加入list中
                sensitiveWordList.add(txt.substring(i, i + length));
                i = i + length - 1; // 减1的原因，是因为for会自增
            }
        }

        return sensitiveWordList;
    }

    /**
     * 替换敏感字字符
     *
     * @param txt
     * @param matchType
     * @param replaceChar 替换字符，默认*
     * @version 1.0
     */
    public String replaceSensitiveWord(String txt, int matchType, String replaceChar) {
        String resultTxt = txt;
        Set<String> set = getSensitiveWord(txt, matchType); // 获取所有的敏感词
        Iterator<String> iterator = set.iterator();
        String word;
        String replaceString;
        while (iterator.hasNext()) {
            word = iterator.next();
            replaceString = getReplaceChars(replaceChar, word.length());
            resultTxt = resultTxt.replaceAll(word, replaceString);
        }

        return resultTxt;
    }

    /**
     * 获取替换字符串
     *
     * @param replaceChar
     * @param length
     * @return
     * @version 1.0 aaaaa
     */
    private String getReplaceChars(String replaceChar, int length) {
        String resultReplace = replaceChar;
        for (int i = 1; i < length; i++) {
            resultReplace += replaceChar;
        }

        return resultReplace;
    }

    /**
     * 检查文字中是否包含敏感字符，检查规则如下：<br>
     *
     * @param txt
     * @param beginIndex
     * @param matchType
     * @return，如果存在，则返回敏感词字符的长度，不存在返回0
     * @version 1.0
     */
    @SuppressWarnings({"rawtypes"})
    public int CheckSensitiveWord(String txt, int beginIndex, int matchType) {
        int matchFlag = 0; // 匹配标识数默认为0
        char word;
        Map nowMap = sensitiveWordMap;
        for (int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word); // 获取指定key
            if (nowMap != null) { // 存在，则判断是否为最后一个
                matchFlag++; // 找到相应key，匹配标识+1
                if ("1".equals(nowMap.get("isEnd"))) { // 如果为最后一个匹配规则,结束循环，返回匹配标识数
                    break;
                }
            } else { // 不存在，直接返回
                break;
            }
        }
        if (matchFlag < 2) { // 长度必须大于等于1，为词
            matchFlag = 0;
        }
        return matchFlag;
    }
}
