package touch.baton.fixture.domain;

import touch.baton.domain.supporter.vo.Message;

public abstract class MessageFixture {

    private MessageFixture() {
    }

    public static Message message(final String value) {
        return new Message(value);
    }
}
