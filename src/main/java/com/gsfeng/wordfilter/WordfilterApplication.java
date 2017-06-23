package com.gsfeng.wordfilter;

import com.gsfeng.wordfilter.core.SensitivewordFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WordfilterApplication {

    public static void main(String[] args) {
        SpringApplication.run(WordfilterApplication.class, args);


        String comments = "我爱五星红旗，你说法轮功还不害人啊";
        /**
         * 过滤敏感词
         */
        String text = SensitivewordFilter.getInstance().replaceSensitiveWord(
                comments, SensitivewordFilter.minMatchTYpe, "*");// 替换后的字段

        System.out.println(text);
    }
}
