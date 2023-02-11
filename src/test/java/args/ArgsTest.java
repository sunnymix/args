package args;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArgsTest {
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
