package args;

import args.exceptions.IllegalValueException;
import args.exceptions.InsufficientArgumentsException;
import args.exceptions.TooManyArgumentsException;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

class OptionParsers {
    public static OptionParser<Boolean> bool() {
        return (arguments, option) -> values(arguments, option, 0).isPresent();
    }

    public static <T> OptionParser<T> unary(T defaultValue, Function<String, T> valueParser) {
        return (arguments, option) -> values(arguments, option, 1)
            .map(it -> parseValue(option, it.get(0), valueParser))
            .orElse(defaultValue);
    }

    public static <T> OptionParser<T[]> list(IntFunction<T[]> generator, Function<String, T> valueParser) {
        return (arguments, option) -> values(arguments, option)
            .map(it -> it.stream().map(value -> parseValue(option, value, valueParser))
                .toArray(generator))
            .orElse(generator.apply(0));
    }

    private static Optional<List<String>> values(List<String> arguments, Option option) {
        int index = arguments.indexOf("-" + option.value());
        if (index == -1) return Optional.empty();
        return Optional.of(values(arguments, index + 1));
    }

    private static Optional<List<String>> values(List<String> arguments, Option option, int expectedSize) {
        int index = arguments.indexOf("-" + option.value());
        if (index == -1) return Optional.empty();

        List<String> values = values(arguments, index + 1);

        if (values.size() < expectedSize) throw new InsufficientArgumentsException(option.value());
        if (values.size() > expectedSize) throw new TooManyArgumentsException(option.value());

        return Optional.of(values);
    }

    private static List<String> values(List<String> arguments, int valueIndex) {
        int followingFlagIndex = IntStream.range(valueIndex, arguments.size())
            .filter(it -> arguments.get(it).startsWith("-"))
            .findFirst().orElse(arguments.size());
        return arguments.subList(valueIndex, followingFlagIndex);
    }

    private static <T> T parseValue(Option option, String value, Function<String, T> valueParser) {
        try {
            return valueParser.apply(value);
        } catch (Exception e) {
            throw new IllegalValueException(option.value(), value);
        }
    }
}
