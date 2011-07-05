package com.humanet.elasticsearch;

/**
 * User: Hugo Marcelino
 * Date: 29/6/11
 */

public class Status {

    private boolean isOk;
    private String errorMsg;

    public static Status success() {
        Status status = new Status();
        status.isOk = true;
        return status;
    }

    public static Status failure(String errorMsg) {
        Status status = new Status();
        status.isOk = false;
        status.errorMsg = errorMsg;
        return status;
    }

    public boolean ok() {
        return isOk;
    }

    public boolean notOk() {
        return !isOk;
    }

    public String getErrorMsg(String errorMsg) {
        return this.errorMsg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Status status = (Status) o;

        if (isOk != status.isOk) return false;
        if (errorMsg != null ? !errorMsg.equals(status.errorMsg) : status.errorMsg != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (isOk ? 1 : 0);
        result = 31 * result + (errorMsg != null ? errorMsg.hashCode() : 0);
        return result;
    }
}
