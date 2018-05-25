package java8;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yangnx
 * @date 2018/3/19
 */
public class LamadaTest {

	@Test
	public void test0() {
//		List<Apple> apples = Arrays.asList(new Apple("red", 1), new Apple("blue", 3), new Apple("red", 4),
//				new Apple("blue", 5));
//		apples.stream().peek(Apple::getColor);
		List<Integer> nums = Lists.newArrayList(1,1,null,2,3,4,null,5,6,7,8,9,10);
		System.out.println("sum is:" + nums.stream().filter(Objects::nonNull).distinct().mapToInt(num -> num * 2)
				.peek(System.out::println).skip(2).limit(4).sum());


	}

	public interface Filter<T> {
		boolean filter(T t);
	}

	public static void getApple(List<Apple> apples, Filter<Apple> filter) {
		for (Apple apple : apples) {
			if (filter != null && filter.filter(apple)) {
				System.out.println(apple);
			}
		}
	}

}

@Data
@AllArgsConstructor
class Apple {
	private String color;
	private int weight;
}
