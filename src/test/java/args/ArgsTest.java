package args;

import args.exceptions.IllegalOptionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArgsTest {
    @Test
    public void should_set_multi_options() {
        // SUT Args.parse

        // Exercise
        MultiOptions options = Args.parse(MultiOptions.class, "-l", "-p", "8080", "-d", "/usr/logs");

        // Verify
        assertTrue(options.logging());
        assertEquals(8080, options.port());
        assertEquals("/usr/logs", options.directory());

        // Teardown
    }

    // SetUp
    static record MultiOptions(
        @Option("l") boolean logging,
        @Option("p") int port,
        @Option("d") String directory
    ) {
    }

    @Test
    public void should_throw_illegal_option_exception_if_annotation_not_present() {
        IllegalOptionException e = assertThrows(IllegalOptionException.class, () ->
            // Exercise
            Args.parse(MultiOptionsWithoutAnnotation.class, "-l", "-p", "8080", "-d", "/usr/logs"));

        // Verify
        assertEquals("port", e.getParameter());

        // Teardown
    }

    // SetUp
    static record MultiOptionsWithoutAnnotation(
        @Option("l") boolean logging,
        int port,
        @Option("d") String directory
    ) {
    }

    @Test
    public void should_parse_list_options() {
        // Exercise
        ListOptions options = Args.parse(ListOptions.class, "-g", "this", "is", "a", "list", "-d", "1", "2", "-3", "5");

        // Verify
        assertArrayEquals(new String[]{"this", "is", "a", "list"}, options.groups());
        assertArrayEquals(new Integer[]{1, 2, -3, 5}, options.decimals());

        // Teardown
    }

    // SetUp
    static record ListOptions(
        @Option("g") String[] groups,
        @Option("d") Integer[] decimals
    ) {
    }
}
