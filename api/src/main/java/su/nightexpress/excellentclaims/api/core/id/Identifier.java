package su.nightexpress.excellentclaims.api.core.id;

import java.util.Optional;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.util.LowerCase;

@NullMarked
public record Identifier(String value) {

    public Identifier {
        if (!isValidName(value)) {
            throw new IllegalArgumentException("Name is empty or invalid");
        }
    }

    public static Identifier of(String input) throws IllegalArgumentException {
        return new Identifier(input);
    }

    public static Identifier ofSanitized(String input) throws IllegalArgumentException {
        return sanitize(input).map(Identifier::new)
            .orElseThrow(() -> new IllegalArgumentException("Unable to sanitize '" + input + "'"));
    }

    public static Optional<Identifier> parse(String input) {
        try {
            Identifier id = new Identifier(input);
            return Optional.of(id);
        }
        catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }

    public static Optional<Identifier> parseSanitized(String input) {
        return sanitize(input).map(Identifier::new);
    }

    public static boolean isAllowedInName(char c) {
        return Character.isLetterOrDigit(c) || c == '_' || c == '-';
    }

    public static boolean isValidName(String name) {
        int length = name.length();
        if (length == 0) return false;

        for (int index = 0; index < length; index++) {
            if (!isAllowedInName(name.charAt(index))) {
                return false;
            }
        }

        return true;
    }

    public static Optional<String> sanitize(String input) {
        char[] chars = LowerCase.internal(input).toCharArray();

        StringBuilder builder = new StringBuilder();
        for (char letter : chars) {
            if (Character.isWhitespace(letter)) {
                builder.append("_");
                continue;
            }
            if (!isAllowedInName(letter)) {
                continue;
            }
            builder.append(Character.toLowerCase(letter));
        }

        String result = builder.toString();
        return result.isBlank() ? Optional.empty() : Optional.of(result);
    }

    @Override
    public String toString() {
        return this.value();
    }
}
