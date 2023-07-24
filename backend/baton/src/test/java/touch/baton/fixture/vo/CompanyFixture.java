package touch.baton.fixture.vo;

import touch.baton.domain.member.vo.Company;

public abstract class CompanyFixture {

    private CompanyFixture() {
    }

    public static Company company(final String company) {
        return new Company(company);
    }
}
