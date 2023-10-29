package enums;

public enum TestCaseStatuses {

    PASSED("passed"),
    FAILED("failed"),
    BLOCKED("blocked"),
    SKIPPED("skipped");

    private final String status;

    TestCaseStatuses(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
