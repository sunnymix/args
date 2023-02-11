package args;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

import static args.BooleanOptionParserTest.option;
import static org.junit.jupiter.api.Assertions.*;

class SingleValueOptionParserTest {
    @Test
    public void should_not_accept_extra_argument_for_single_value_option() {
        TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
            new SingleValueOptionParser<>(0, Integer::parseInt).parse(Arrays.asList("-p", "8080", "8081"), option("p"));
        });
        assertEquals("p", e.getOption());
    }

    @ParameterizedTest
    @ValueSource(strings = {"-p -l", "-p"})
    public void should_not_accept_insufficient_argument_for_single_value_option(String arguments) {
        InsufficientArgumentsException e = assertThrows(InsufficientArgumentsException.class, () -> {
            new SingleValueOptionParser<>(0, Integer::parseInt).parse(List.of(arguments.split(" ")), option("p"));
        });
        assertEquals("p", e.getOption());
    }

    @Test
    public void should_set_default_value_to_0_for_int_option() {
        assertEquals(0, new SingleValueOptionParser<>(0, Integer::parseInt).parse(List.of(), option("p")));
    }

    @Test
    public void should_set_default_value_to_empty_string_for_string_option() {
        assertEquals("", new SingleValueOptionParser<>("", String::valueOf).parse(List.of(), option("d")));
    }
}
