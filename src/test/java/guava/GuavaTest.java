package guava;

import static com.google.common.base.Preconditions.*;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.sun.org.apache.xpath.internal.SourceTree;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class GuavaTest {

	@Test
	public void testNull() {
		Optional<Integer> possible = Optional.ofNullable(null);
		System.out.println(possible.orElse(10));
	}

	@Test
	public void testPrecondition(){
		checkArgument(3>2, "%s 和 %s",2 , 3);
		checkNotNull(1, "为空");
		checkPositionIndex(1, 3);
	}

	@Test
	public void testObject() {
		System.out.println(Objects.equal(null, null));
		System.out.println(ComparisonChain.start().compare(1,2).result());
	}

	@Test
	public void testComparator() {
		List<Foo> list = new ArrayList<>();
		list.add(new Foo("e", 5));
		list.add(new Foo("b", 2));
		list.add(new Foo("c", 3));
		list.add(new Foo("d", 4));
		list.add(new Foo(null, 4));

		Ordering<Foo> ordering = Ordering.natural().reverse().nullsFirst().onResultOf(input -> input.getName());
		ordering.greatestOf(list.iterator(), list.size()).forEach(item -> System.out.println(item.getAge()));
	}
	@Test
	public void testComparator2() {
		List<Foo> list = new ArrayList<>();
		list.add(new Foo("e", 5));
		list.add(new Foo("b", 2));
		list.add(new Foo("c", 3));
		list.add(new Foo("d", 4));
		list.add(new Foo(null, 4));
		Ordering<Foo> ordering = new Ordering<Foo>() {
			@Override
			public int compare(Foo left, Foo right) {
				return left.getAge() - right.getAge();
			}
		};
		ordering.reverse().greatestOf(list.iterator(), list.size()).forEach(item -> System.out.println(item.getAge()));
	}

}
