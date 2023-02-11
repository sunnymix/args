package args;

import args.exceptions.InsufficientArgumentsException;
import args.exceptions.TooManyArgumentsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static args.BooleanOptionParserTest.option;
import static org.junit.jupiter.api.Assertions.*;

class SingleValueOptionParserTest {
    @Test // Sad path
    public void should_not_accept_extra_argument_for_single_value_option() {
        TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
            new SingleValueOptionParser<>(0, Integer::parseInt).parse(Arrays.asList("-p", "8080", "8081"), option("p"));
        });
        assertEquals("p", e.getOption());
    }

    @ParameterizedTest // Sad path
    @ValueSource(strings = {"-p -l", "-p"})
    public void should_not_accept_insufficient_argument_for_single_value_option(String arguments) {
        InsufficientArgumentsException e = assertThrows(InsufficientArgumentsException.class, () -> {
            new SingleValueOptionParser<>(0, Integer::parseInt).parse(List.of(arguments.split(" ")), option("p"));
        });
        assertEquals("p", e.getOption());
    }

    @Test // Default value
    public void should_set_default_value_to_0_for_int_option() {
        Function<String, Object> whatever = (it) -> null;
        Object defaultValue = new Object();
        assertSame(defaultValue, new SingleValueOptionParser<>(defaultValue, whatever).parse(List.of(), option("p")));
    }

    @Test // Happy path
    public void should_parse_value_if_flag_present() {
        Object parsedValue = new Object();
        Function<String, Object> parse = (it) -> parsedValue;
        Object whatever = new Object();
        assertSame(parsedValue, new SingleValueOptionParser<>(whatever, parse).parse(List.of("-p", "8080"), option("p")));
    }
}
