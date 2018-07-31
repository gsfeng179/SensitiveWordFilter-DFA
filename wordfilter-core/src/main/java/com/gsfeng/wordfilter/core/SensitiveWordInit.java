package com.gsfeng.wordfilter.core;

import com.gsfeng.wordfilter.core.resources.LoadResources;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by gsfeng on 2017/6/21.
 */
@NoArgsConstructor(access = AccessLevel.MODULE)
public class SensitiveWordInit {
    /**
     * 字符编码
     */
    private static final String ENCODING = "utf8";
    /**
     * 敏感词库
     */
    @SuppressWarnings("rawtypes")
    private HashMap sensitiveWordMap;

    @SuppressWarnings("rawtypes")
    Map initKeyWord() {
        try {
            // 读取敏感词库
            Set<String> keyWordSet = readSensitiveWordFile();
            // 将敏感词库加入到HashMap中
            addSensitiveWordToHashMap(keyWordSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sensitiveWordMap;
    }

    /**
     * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：<br>
     * 中 = { isEnd = 0 国 = {<br>
     * isEnd = 1 人 = {isEnd = 0 民 = {isEnd = 1} } 男 = { isEnd = 0 人 = { isEnd =
     * 1 } } } } 五 = { isEnd = 0 星 = { isEnd = 0 红 = { isEnd = 0 旗 = { isEnd = 1
     * } } } }
     *
     * @param keyWordSet 敏感词库
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
        // 初始化敏感词容器，减少扩容操作
        sensitiveWordMap = new HashMap(keyWordSet.size());
        String key;
        Map nowMap;
        Map<String, String> newWordMap;
        // 迭代keyWordSet
        for (String aKeyWordSet : keyWordSet) {
            // 关键字
            key = aKeyWordSet;
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                // 转换成char型
                char keyChar = key.charAt(i);
                // 65279是一个空格
                if ((int) keyChar == 65279) {
                    continue;
                }
                // 获取
                Object wordMap = nowMap.get(keyChar);

                if (wordMap != null) {
                    // 如果存在该key，直接赋值
                    nowMap = (Map) wordMap;
                } else {
                    // 不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWordMap = new HashMap<>();
                    // 不是最后一个
                    newWordMap.put("isEnd", "0");
                    nowMap.put(keyChar, newWordMap);
                    nowMap = newWordMap;
                }

                if (i == key.length() - 1) {
                    // 最后一个
                    nowMap.put("isEnd", "1");
                }
            }
        }
    }

    /**
     * 读取敏感词库中的内容，将内容添加到set集合中
     *
     * @return Set
     * @throws Exception Exception
     */
    private Set<String> readSensitiveWordFile() throws Exception {
        File file = LoadResources.getFilterResource();

        InputStreamReader read = new InputStreamReader(new FileInputStream(file), ENCODING);
        try {
            Set<String> set = new HashSet<>();
            BufferedReader bufferedReader = new BufferedReader(read);
            String txt;
            while ((txt = bufferedReader.readLine()) != null) {
                set.add(txt);
            }
            return set;
        } finally {
            read.close();
        }
    }
}
