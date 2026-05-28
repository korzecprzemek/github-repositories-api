package pl.korzec.githubrepositoriesapi;

public record GitHubRepositoryDto(
        String name,
        GitHubOwnerDto owner,
        boolean fork
) {
}
