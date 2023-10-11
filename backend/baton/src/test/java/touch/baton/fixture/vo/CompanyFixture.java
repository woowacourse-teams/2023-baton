package touch.baton.fixture.vo;

import touch.baton.domain.member.command.vo.Company;

public abstract class CompanyFixture {

    private CompanyFixture() {
    }

    public static Company company(final String value) {
        return new Company(value);
    }
}
