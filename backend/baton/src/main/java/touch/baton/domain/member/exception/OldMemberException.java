package touch.baton.domain.member.exception;

public class OldMemberException extends RuntimeException {

    public OldMemberException(final String message) {
        super(message);
    }

    public static class NotNull extends OldMemberException {

        public NotNull(final String message) {
            super(message);
        }
    }
}
