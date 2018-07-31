# Wordfilter
基于DFA算法实现的敏感词过滤


# QuickStart
下载编译
```
git clone https://github.com/gsfeng2015/Wordfilter.git
cd Wordfilter
mvn clean install -Dmaven.test.skip -Denv=release
```

# 使用
添加依赖
```
<dependency>
    <groupId>com.gsfeng.wordfilter</groupId>
     <artifactId>wordfilter-core</artifactId>
    <version>${bulid.version}</version>
</dependency>
```

# 使用
具体使用见wordfilter-example


#FAQ
1、阅读源码时为什么会出现编译错误?
> 回答：  
   wordfilter-core使用lombok实现极简代码。关于更多使用和安装细节，请参考[lombok官网](https://projectlombok.org/download.html)。

# 算法--DFA
[对于DFA、NFA理解](https://blog.csdn.net/little_nai/article/details/52528294)

