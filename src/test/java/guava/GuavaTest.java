package guava;

import static com.google.common.base.Preconditions.*;
import org.junit.Test;

import java.util.Optional;

public class GuavaTest {

	@Test
	public void testNull() {
		Optional<Integer> possible = Optional.ofNullable(null);
		System.out.println(possible.orElse(10));
	}

	@Test
	public void testPrecondition(){
//		checkArgument(2>3, "%s 和 %s",2 , 3);
//		checkNotNull(null, "为空");
//		checkPositionIndex()
	}

}
