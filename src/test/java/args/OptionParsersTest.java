package args;

import args.exceptions.InsufficientArgumentsException;
import args.exceptions.TooManyArgumentsException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static args.OptionParsersTest.BooleanOptionParserTest.option;
import static org.junit.jupiter.api.Assertions.*;

class OptionParsersTest {

    @Nested
    class BooleanOptionParserTest {
        @Test // Sad path
        public void should_not_accept_extra_argument_for_boolean_option() {
            TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
                OptionParsers.bool().parse(Arrays.asList("-l", "t"), option("l"));
            });
            assertEquals("l", e.getOption());
        }

        @Test // Default value
        public void should_set_default_value_to_false_if_option_not_present() {
            assertFalse(OptionParsers.bool().parse(List.of(), option("l")));
        }

        @Test // Happy path
        public void should_set_boolean_option_to_true_if_flag_present() {
            assertTrue(OptionParsers.bool().parse(List.of("-l"), option("l")));
        }

        static Option option(String value) {
            return new Option() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return Option.class;
                }

                @Override
                public String value() {
                    return value;
                }
            };
        }
    }

    @Nested
    class UnaryOptionParserTest {
        @Test // Sad path
        public void should_not_accept_extra_argument_for_single_value_option() {
            TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
                OptionParsers.unary(0, Integer::parseInt).parse(Arrays.asList("-p", "8080", "8081"), option("p"));
            });
            assertEquals("p", e.getOption());
        }

        @ParameterizedTest // Sad path
        @ValueSource(strings = {"-p -l", "-p"})
        public void should_not_accept_insufficient_argument_for_single_value_option(String arguments) {
            InsufficientArgumentsException e = assertThrows(InsufficientArgumentsException.class, () -> {
                OptionParsers.unary(0, Integer::parseInt).parse(List.of(arguments.split(" ")), option("p"));
            });
            assertEquals("p", e.getOption());
        }

        @Test // Default value
        public void should_set_default_value_to_0_for_int_option() {
            Function<String, Object> whatever = (it) -> null;
            Object defaultValue = new Object();
            assertSame(defaultValue, OptionParsers.unary(defaultValue, whatever).parse(List.of(), option("p")));
        }

        @Test // Happy path
        public void should_parse_value_if_flag_present() {
            Object parsedValue = new Object();
            Function<String, Object> parse = (it) -> parsedValue;
            Object whatever = new Object();
            assertSame(parsedValue, OptionParsers.unary(whatever, parse).parse(List.of("-p", "8080"), option("p")));
        }
    }
}
