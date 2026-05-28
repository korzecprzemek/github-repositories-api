package pl.korzec.githubrepositoriesapi;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepositoryService {
    private final GitHubClient gitHubClient;

    RepositoryService(GitHubClient gitHubClient){
        this.gitHubClient = gitHubClient;
    }

    List<RepositoryResponse> getRepositories(String username){
          return gitHubClient.getUserRepositories(username).stream()
                  .filter(repository -> !repository.fork())
                  .map(repository -> new RepositoryResponse(
                          repository.name(),
                          repository.owner().login(),
                          getBranches(repository)
                  ))
                  .toList();
    }
    private List<BranchResponse> getBranches(GitHubRepositoryDto repository) {
        return gitHubClient.getRepositoryBranches(repository.owner().login(), repository.name()).stream()
                .map(branch -> new BranchResponse(
                        branch.name(),
                        branch.commit().sha()
                ))
                .toList();
    }
}
