import cn.cuteoo.interview.FileUtil;
import cn.cuteoo.interview.HashCode;
import cn.cuteoo.interview.RateInfo;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

/**
 * @Description: 测试HashCode为什么使用31做乘数
 *
 * Why does Java's hashCode() in String use 31 as a multiplier?
 * s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
 *
 * 之所以使用 31， 是因为他是一个奇素数。如果乘数是偶数，并且乘法溢出的话，信息就会丢失，因为与2相乘等价于移位运算（低位补0）。
 * 使用素数的好处并不很明显，但是习惯上使用素数来计算散列结果。
 * 31 有个很好的性能，即用移位和减法来代替乘法，可以得到更好的性能： 31 * i == (i << 5）- i，
 * 现代的 JVM 可以自动完成这种优化。这个公式可以很简单的推导出来。
 *
 * https://stackoverflow.com/questions/299304/why-does-javas-hashcode-in-string-use-31-as-a-multiplier
 *
 * @Author: Song Pengbo
 * @Create: 2022/3/29 12:31
 */
public class ApiTest {

    private Set<String> words;

    @Before
    public void before() {

        int abcHashCode = "abc".hashCode();
        System.out.println("abc字符串的hashCode值为: " + abcHashCode);

        // 读取文件，103976个英语单词库.txt
        words = FileUtil.readWordList("E:\\DevProject\\MyProject\\Interview-Manual\\Interview-bugstack\\interview-01\\src\\main\\resources\\103976个英语单词库.txt");
    }

    @Test
    public void test_collisionRate() {
        System.out.println("单词数量：" + words.size());
        List<RateInfo> rateInfoList = HashCode.collisionRateList(words, 2, 3, 5, 7, 17, 31, 32, 33, 39, 41, 199);
        for (RateInfo rate : rateInfoList) {
            System.out.println(String.format("乘数 = %4d, 最小Hash = %11d, 最大Hash = %10d, 碰撞数量 =%6d, 碰撞概率 = %.4f%%", rate.getMultiplier(), rate.getMinHash(), rate.getMaxHash(), rate.getCollisionCount(), rate.getCollisionRate() * 100));
        }
    }

    @Test
    public void test_hashArea() {
        System.out.println(HashCode.hashArea(words, 2).values());
        System.out.println(HashCode.hashArea(words, 7).values());
        System.out.println(HashCode.hashArea(words, 31).values());
        System.out.println(HashCode.hashArea(words, 32).values());
        // 乘数199的数据是更加分散的，但因为数据区间问题会有数据丢失问题，所以不能选择
        System.out.println(HashCode.hashArea(words, 199).values());
    }

}
