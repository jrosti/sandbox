import java.util.Stack;

public class Continuation {

	interface Function<T, K> {
		T apply(K v);
	}

	interface BinOp<T> {
		T apply(T a, T b);
	}
	
	static Function<Function<Integer, Integer>, Integer> op(final BinOp<Integer> op) {
		
		return new Function<Function<Integer, Integer>, Integer>() {
			
			@Override
			public Function<Integer, Integer> apply(final Integer left) {
				return new Function<Integer, Integer>() {
					
					@Override
					public Integer apply(Integer right) {
						return op.apply(left, right);
					}
				};
			}
		};
	}
	
	static Function<Function<Integer, Integer>, Integer> times() {
		return op(new BinOp<Integer>() {
			
			@Override
			public Integer apply(Integer a, Integer b) {
				return a*b;
			}
		});
	}

	static <T> Function<T, T> id(T a) {
		return new Function<T, T>() {
			@Override
			public T apply(T v) {
				return v;
			}
		}; 
	}
	
	public static Integer xyzcps(Function<Integer, Integer> continuation, Integer n) {
		if (n == 0) return continuation.apply(1);
		return xyzcps(times().apply(continuation.apply(n)), n-1);
	}
	
	public static Integer xyz(Integer a) {
		return xyzcps(id(a) , a);
	}
	
	public static void main(String[] args) {
		System.out.println(curry(times(), 3, 4, 5));
		// 
		System.out.println(xyz(5));
	}

	public static Integer curry(Function<Function<Integer, Integer>, Integer> op, Integer ... args) {
		Stack<Integer> argList = new Stack<Integer>();
		for  (Integer arg : args) {
			argList.push(arg);
		}
		Integer res = argList.pop();
		do {
			Function<Integer, Integer> f = op.apply(res);
			res = f.apply(argList.pop());
		} while(! argList.isEmpty());
		return res; 
	}
}



