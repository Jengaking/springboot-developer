import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class JUnitQuiz {
    @Test
    public void junitTest() {
        String name1 = "홍길동";
        String name2 = "홍길동";
        String name3 = "홍길은";
        Assertions.assertThat(name1).isNotNull();
        Assertions.assertThat(name2).isNotNull();
        Assertions.assertThat(name3).isNotNull();
        Assertions.assertThat(name1).isEqualTo(name2);
        Assertions.assertThat(name1).isNotEqualTo(name3);
    }

    @Test
    public void junitTest2() {
        int number1 = 15;
        int number2 = 0;
        int number3 = -5;

        Assertions.assertThat(number1).isPositive();
        Assertions.assertThat(number2).isZero();
        Assertions.assertThat(number3).isNegative();

        Assertions.assertThat(number1).isGreaterThan(number2);
        Assertions.assertThat(number3).isLessThan(number2);
    }
}
