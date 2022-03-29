package cn.cuteoo.interview;

import java.util.*;

public class HashCode {

    /**
     * @Description: Hash计算函数，与原Hash函数对比只是替换了可变参数，用于统计不同乘积数的计算结果
     * @param str: 单个单词
     * @param multiplier: 乘积数
     *
     * @Author: Song Pengbo
     * @Date: 2022/3/29 12:39
     * @return: java.lang.Integer
     */
    public static Integer hashCode(String str, Integer multiplier) {
        int hash = 0;
        for (int i = 0; i < str.length(); i++) {
            // charAt() 方法用于返回指定索引处的字符。索引范围为从 0 到 length() - 1。
            hash = multiplier * hash + str.charAt(i);
        }
        return hash;
    }

    /**
     * @Description: 计算Hash碰撞概率
     * @param multiplier: 乘积数
     * @param hashCodeList: 所有单词的HashCode列表
     *
     * @Author: Song Pengbo
     * @Date: 2022/3/29 12:45
     * @return: cn.cuteoo.interview.RateInfo
     */
    private static RateInfo hashCollisionRate(Integer multiplier, List<Integer> hashCodeList) {
        // Java Stream 通过min()和max()获取列表最小值和最大值
        // 官网对双冒号的解释是方法引用,也就是引用一个方法的意思,英文名称 Method References
        int maxHash = hashCodeList.stream().max(Integer::compareTo).get();
        int minHash = hashCodeList.stream().min(Integer::compareTo).get();

        // distinct() 返回由该流的不同元素组成的流，用于列表去重
        int collisionCount = (int) (hashCodeList.size() - hashCodeList.stream().distinct().count());
        double collisionRate = (collisionCount * 1.0) / hashCodeList.size();

        return new RateInfo(maxHash, minHash, multiplier, collisionCount, collisionRate);
    }

    /**
     * @Description: 计算不同乘积值下hashcode的碰撞比例列表
     * @param strList: 单词列表
     * @param multipliers: 成绩值
     *
     * 类型后面三个点(String...)，是从Java 5开始，Java语言对方法参数支持一种新写法，叫可变长度参数列表，其语法就是类型后跟...，表示此处接受的参数为0到多个Object类型的对象，或者是一个Object[]
     * https://blog.csdn.net/guoquanyou/article/details/8571156
     *
     * @Author: Song Pengbo
     * @Date: 2022/3/29 15:46
     * @return: java.util.List<cn.cuteoo.interview.RateInfo>
     */
    public static List<RateInfo> collisionRateList(Set<String> strList, Integer... multipliers) {
        List<RateInfo> rateInfoList = new ArrayList<>();
        for (Integer multiplier : multipliers) {
            List<Integer> hashCodeList = new ArrayList<>();
            for (String str : strList) {
                Integer hashCode = hashCode(str, multiplier);
                hashCodeList.add(hashCode);
            }
            rateInfoList.add(hashCollisionRate(multiplier, hashCodeList));
        }
        return rateInfoList;
    }

    /**
     * @Description: 统计int取值范围内，每个哈希值存放到不同格子里的数量
     * @param hashCodeList: 每个单词对应的哈希值列表
     *
     * @Author: Song Pengbo
     * @Date: 2022/3/29 16:05
     * @return: java.util.Map<java.lang.Integer,java.lang.Integer>
     */
    public static Map<Integer, Integer> hashArea(List<Integer> hashCodeList) {
        Map<Integer, Integer> statistics = new LinkedHashMap<>();
        int start = 0;
        // 给int类型赋值的话，0X7FFFFFFF代表最大值，0X80000000代表最小值
        // int类型范围：-2,147,483,648 到 2,147,483,647
        for (long i = 0x80000000; i <= 0x7fffffff; i += 67108864) {
            long min = i;
            long max = min + 67108864;
            // 筛选出每个格子里的哈希值数量，java8流统计；https://bugstack.cn/itstack-demo-any/2019/12/10/%E6%9C%89%E7%82%B9%E5%B9%B2%E8%B4%A7-Jdk1.8%E6%96%B0%E7%89%B9%E6%80%A7%E5%AE%9E%E6%88%98%E7%AF%87(41%E4%B8%AA%E6%A1%88%E4%BE%8B).html
            // 使用stream().filter()来过滤一个List对象，查找符合条件的对象集合
            int num = (int) hashCodeList.parallelStream().filter(x -> x >= min && x < max).count();
            statistics.put(start++, num);
        }
        return statistics;
    }

    /**
     * @Description: 计算不同乘积数下，Hash值的散列分布
     * @param strList: 单词列表
     * @param multiplier: 乘积数
     *
     * @Author: Song Pengbo
     * @Date: 2022/3/29 16:12
     * @return: java.util.Map<java.lang.Integer,java.lang.Integer>
     */
    public static Map<Integer, Integer> hashArea(Set<String> strList, Integer multiplier){
        List<Integer> hashCodeList = new ArrayList<>();
        for (String str : strList) {
            Integer hashCode = hashCode(str, multiplier);
            hashCodeList.add(hashCode);
        }
        return hashArea(hashCodeList);
    }

}
