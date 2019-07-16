package DabEngine.Utils;

public interface MultiFunc<A, R> {
    R apply(A... args);
}