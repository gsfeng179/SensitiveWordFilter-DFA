package com.gsfeng.wordfilter.example;

import com.gsfeng.wordfilter.core.SensitivewordFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WordfilterExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(WordfilterExampleApplication.class, args);

        String comments = "我爱五星红旗，你说法轮功害不害人啊";
        // 敏感词过滤器
        SensitivewordFilter filter = SensitivewordFilter.getInstance();

        // 是否存在敏感词
        System.out.println(filter.checkSensitiveWord(comments, 0, SensitivewordFilter.minMatchType));
        // 获取敏感词
        System.out.println(filter.getSensitiveWord(comments, SensitivewordFilter.minMatchType));
        // 敏感词替换
        System.out.println(filter.replaceSensitiveWord(comments, SensitivewordFilter.minMatchType, "*"));
    }
}
