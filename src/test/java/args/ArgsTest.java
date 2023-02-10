package args;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
1. Input:
    -l -p 8080 -d /usr/logs (String)

2. API design:
    Options options = Args.parse(Options.class, "-l", "-p", "8080", "-d", "/usr/logs")
    options.logging()
    options.port()

3. Implementation:
    parse the origin array by index, eg, [-l], [-p, 8080], [-d, /usr/logs]

4. Tasks:
    1. Single options:
        - TODO bool: -l
        - TODO int: -p 8080
        - TODO string: -d /usr/logs
    2. TODO: Multi options: -l -p 8080 -d /usr/logs
    3. Sad path:
        - TODO bool: -l t , -l t f
        - TODO int: -p , -p 8080 8081
        - TODO string: -d , -d /usr/logs /usr/vars
    4. Default value:
        - TODO bool: false
        - TODO int: 0
        - TODO string: ""
Each _TODO should map to a Test.

5. Disable Test Too Big to implement in a short time. eg, example_1, example_2.

 */

public class ArgsTest {
    @Test
    public void should_set_boolean_option_to_true_if_flag_present() {
        BooleanOption option = Args.parse(BooleanOption.class, "-l");
        assertTrue(option.logging());
    }

    @Test
    public void should_set_boolean_option_to_false_if_flag_not_present() {
        BooleanOption option = Args.parse(BooleanOption.class);
        assertFalse(option.logging());
    }

    static record BooleanOption(@Option("l") boolean logging) {}

    @Test
    public void should_set_int_option_to_int_if_flag_present() {
        IntOption option = Args.parse(IntOption.class, "-p", "8080");
        assertEquals(8080, option.port());
    }

    static record IntOption(@Option("p") int port) {}

    @Test
    public void should_set_string_option_to_string_if_flag_present() {
        StringOption option = Args.parse(StringOption.class, "-d", "/usr/logs");
        assertEquals("/usr/logs", option.directory());
    }

    static record StringOption(@Option("d") String directory) {}

    /**
     * Too Big to implement, should use the @Disabled at the start.
     */
    @Test
    public void should_set_multi_options() {
        MultiOptions options = Args.parse(MultiOptions.class, "-l", "-p", "8080", "-d", "/usr/logs");
        assertTrue(options.logging());
        assertEquals(8080, options.port());
        assertEquals("/usr/logs", options.directory());
    }

    static record MultiOptions(
        @Option("l") boolean logging,
        @Option("p") int port,
        @Option("d") String directory
    ) {}

    /**
     * Too Big to implement, should use the @Disabled at the start.
     */
    @Test
    @Disabled
    public void should_example_2() {
       ListOptions options = Args.parse(ListOptions.class, "-g", "this", "is", "a", "list", "-d", "1", "2", "-3", "5");
       assertArrayEquals(new String[]{"this", "is", "a", "list"}, options.groups());
       assertArrayEquals(new int[]{1, 2, -3, 5}, options.decimals());
    }

    static record ListOptions(
        @Option("g") String[] groups,
        @Option("d") int[] decimals
    ) {}
}
