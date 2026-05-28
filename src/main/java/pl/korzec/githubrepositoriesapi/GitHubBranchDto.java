package pl.korzec.githubrepositoriesapi;

public record GitHubBranchDto(
        String name,
        GitHubCommitDto commit
) {
}
