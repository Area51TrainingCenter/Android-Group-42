package pe.area51.githublist.ui;

import android.support.annotation.Nullable;

public class Response<SuccessData> {

    @Nullable
    private final SuccessData successData;
    private final boolean isLoading;
    private final boolean wasSuccessful;
    private final int errorMessage;

    public Response(@Nullable SuccessData successData, boolean isLoading, boolean wasSuccessful, int errorMessage) {
        this.successData = successData;
        this.isLoading = isLoading;
        this.wasSuccessful = wasSuccessful;
        this.errorMessage = errorMessage;
    }

    @Nullable
    public SuccessData getSuccessData() {
        return successData;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean wasSuccessful() {
        return wasSuccessful;
    }

    public int getErrorMessage() {
        return errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response<?> response = (Response<?>) o;
        if (isLoading != response.isLoading) return false;
        if (wasSuccessful != response.wasSuccessful) return false;
        if (errorMessage != response.errorMessage) return false;
        return successData != null ? successData.equals(response.successData) : response.successData == null;
    }

    @Override
    public int hashCode() {
        int result = successData != null ? successData.hashCode() : 0;
        result = 31 * result + (isLoading ? 1 : 0);
        result = 31 * result + (wasSuccessful ? 1 : 0);
        result = 31 * result + errorMessage;
        return result;
    }

    @Override
    public String toString() {
        return "Response{" +
                "successData=" + successData +
                ", isLoading=" + isLoading +
                ", wasSuccessful=" + wasSuccessful +
                ", errorMessage=" + errorMessage +
                '}';
    }
}
