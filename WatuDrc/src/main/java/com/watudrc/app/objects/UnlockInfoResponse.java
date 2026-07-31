package com.watudrc.app.objects;

public class UnlockInfoResponse {
    private boolean confirmedByLoanStatus;
    private boolean confirmedByRequestCount;
    private byte dailyLimit;
    private byte dailyRemaining;
    private Long dailyGlobalMax;

    public boolean isConfirmedByLoanStatus() {
        return confirmedByLoanStatus;
    }

    public boolean isConfirmedByRequestCount() {
        return confirmedByRequestCount;
    }

    public byte getDailyLimit() {
        return dailyLimit;
    }

    public byte getDailyRemaining() {
        return dailyRemaining;
    }

    public Long getDailyGlobalMax() {
        return dailyGlobalMax;
    }
}
