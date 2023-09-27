package touch.baton.fixture.vo;

import touch.baton.domain.member.command.vo.Message;

public abstract class MessageFixture {

    private MessageFixture() {
    }

    public static Message message(final String value) {
        return new Message(value);
    }
}
