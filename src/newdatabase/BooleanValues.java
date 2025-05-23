package newdatabase;

public class BooleanValues {
    public static enum WithBlank {
        TRUE("true"), FALSE("false"), BLANK("");

        private final String value;

        WithBlank(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        public static WithBlank fromString(String value) {
            for (WithBlank boolVal : WithBlank.values()) {
                if (boolVal.value.equalsIgnoreCase(value)) {
                    return boolVal;
                }
            }
            throw new IllegalArgumentException("Invalid boolean value: " + value);
        }
    }
    public static enum WithoutBlank {
        TRUE("true"), FALSE("false");

        private final String value;

        WithoutBlank(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        public static WithoutBlank fromString(String value) {
            for (WithoutBlank boolVal : WithoutBlank.values()) {
                if (boolVal.value.equalsIgnoreCase(value)) {
                    return boolVal;
                }
            }
            throw new IllegalArgumentException("Invalid boolean value: " + value);
        }
    }
}
