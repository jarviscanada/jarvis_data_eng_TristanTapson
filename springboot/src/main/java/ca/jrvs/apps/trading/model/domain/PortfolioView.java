package ca.jrvs.apps.trading.model.domain;

import java.util.Arrays;

public class PortfolioView {

    private SecurityRow [] securityRows;

    // getters and setters

    public SecurityRow[] getSecurityRows() {
        return securityRows;
    }

    public void setSecurityRows(SecurityRow[] securityRows) {
        this.securityRows = securityRows;
    }

    // toString for testing...
    @Override
    public String toString() {
        return "PortfolioView{" +
                "securityRows=" + Arrays.toString(securityRows) +
                '}';
    }
}
