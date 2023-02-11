package args;

import args.exceptions.TooManyArgumentsException;

import java.util.List;

import static args.SingleValueOptionParser.values;

class BooleanOptionParser implements OptionParser<Boolean> {
    @Override
    public Boolean parse(List<String> arguments, Option option) {
        return values(arguments, option, 0).isPresent();
    }
}
