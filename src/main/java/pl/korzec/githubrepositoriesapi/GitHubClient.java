package pl.korzec.githubrepositoriesapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

@Component
public class GitHubClient {

    private final RestClient restClient;

    GitHubClient(@Value("${github.api.url}") String githubApiUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(githubApiUrl)
                .build();
    }

    List<GitHubRepositoryDto> getUserRepositories(String username) {
        return restClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .onStatus(
                        status -> status.value() == 404,
                        (request, response) -> {
                            throw new GitHubUserNotFoundException(username);
                        }
                )
                .body(new ParameterizedTypeReference<>() {
                });
    }
    List<GitHubBranchDto> getRepositoryBranches(String owner, String repositoryName) {
        return restClient.get()
                .uri("/repos/{owner}/{repositoryName}/branches", owner, repositoryName)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
