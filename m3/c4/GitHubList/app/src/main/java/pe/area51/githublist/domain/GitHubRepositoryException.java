package pe.area51.githublist.domain;

public class GitHubRepositoryException extends Exception {

    public final static int ERROR_UNKNOWN_USER = 1;
    public final static int ERROR_IO = 2;
    public final static int ERROR_OTHER = 3;

    private final int errorCode;

    public GitHubRepositoryException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
