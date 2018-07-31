package com.gsfeng.wordfilter.core.resources;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 加载资源敏感词资源
 *
 * @author gsfeng
 * @date 2018/7/31 17:06
 */
public class LoadResources {

    /**
     * 加载类路径下资源
     *
     * @return File
     * @throws FileNotFoundException FileNotFoundException
     */
    public static File getFilterResource() throws FileNotFoundException {
        File file;
        try {
            file = ResourceUtils.getFile("classpath:filter.txt");
        } catch (FileNotFoundException e) {
            System.out.println("资源不存在");
            throw e;
        }
        return file;
    }
}
