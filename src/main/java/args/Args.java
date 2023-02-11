package args;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Args {
    public static <T> T parse(Class<T> optionsClass, String ...args) {
        try {
            List<String> arguments = Arrays.asList(args);
            Constructor<?> constructor = optionsClass.getDeclaredConstructors()[0];
            Object[] values = Arrays.stream(constructor.getParameters())
                .map(it -> parseOption(arguments, it)).toArray();
            return (T) constructor.newInstance(values);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Object parseOption(List<String> arguments, Parameter parameter) {
        return getOptionParser(parameter.getType())
            .parse(arguments, parameter.getAnnotation(Option.class));
    }

    static Map<Class<?>, OptionParser> PARSERS = Map.of(
        boolean.class, new BooleanOptionParser(),
        int.class, new SingleValueOptionParser<>(Integer::parseInt),
        String.class, new SingleValueOptionParser<>(String::valueOf)
    );

    private static OptionParser getOptionParser(Class<?> parameterType) {
        return PARSERS.get(parameterType);
    }
}
