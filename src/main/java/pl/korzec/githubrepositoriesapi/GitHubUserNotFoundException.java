package pl.korzec.githubrepositoriesapi;

public class GitHubUserNotFoundException extends RuntimeException {
    public GitHubUserNotFoundException(String username) {
        super("GitHub user not found: " + username);
    }
}
