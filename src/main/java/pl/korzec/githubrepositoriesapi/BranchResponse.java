package pl.korzec.githubrepositoriesapi;

public record BranchResponse(
            String name,
            String lastCommitSha
) {
}
