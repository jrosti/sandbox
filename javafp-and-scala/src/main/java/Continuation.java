public class Continuation {

    interface Function<T, K> {
        T apply(K v);
    }

    interface BinOp<T> {
        T apply(T a, T b);
    }

    interface CurriedFunction<T> extends Function<Function<T, T>, T> {}

    static <T> CurriedFunction<T> op(final BinOp<T> op) {

        return new CurriedFunction<T>() {

            @Override
            public Function<T, T> apply(final T left) {
                return new Function<T, T>() {

                    @Override
                    public T apply(T right) {
                        return op.apply(left, right);
                    }
                };
            }
        };
    }

    static <T> Function<T, T> partial(CurriedFunction<T> op, T arg) {
        return op.apply(arg);
    }

    public static <T> T curry(CurriedFunction<T> op, T... args) {
        T result = null;
        Function<T, T> f = partial(op, args[0]);
        for (int i = 1; i < args.length; i++) {
            result = f.apply(args[i]);
            f = partial(op, result);
        }
        ;
        return result;
    }

    static CurriedFunction<Integer> times() {
        return op(new BinOp<Integer>() {

            @Override
            public Integer apply(Integer a, Integer b) {
                return a * b;
            }
        });
    }

    static CurriedFunction<Integer> minus() {
        return op(new BinOp<Integer>() {
            @Override
            public Integer apply(Integer a, Integer b) {
                return a - b;
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
        return xyzcps(partial(times(), continuation.apply(n)), curry(minus(), n, 1));
    }

    public static Integer xyz(Integer a) {
        return xyzcps(id(a), a);
    }

    public static void main(String[] args) {
        System.out.println(curry(minus(), 3, 1, 2, 3));
        //
        System.out.println(xyz(5));
    }
}



