package java8;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author yangnx
 * @date 2018/8/3
 */
public class TestFunction {

	public static void main(String[] args) {
		Function function = (i) -> {

			return 1;
		};
		Supplier supplier = () -> {return 1;};

		TestFunction.test1(integer -> System.out.println(integer));

	}

	public static void test1(Consumer<Integer> consumer) {
		consumer.accept(1);
	}

}
