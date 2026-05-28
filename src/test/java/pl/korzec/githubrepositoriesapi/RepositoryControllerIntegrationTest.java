package pl.korzec.githubrepositoriesapi;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "github.api.url=http://localhost:8089"
)
public class RepositoryControllerIntegrationTest {
    private static WireMockServer wireMockServer;

    @LocalServerPort
    private int port;

    private final RestClient restClient = RestClient.create();

    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(options().port(8089));
        wireMockServer.start();
    }

    @AfterAll
    static void stopWireMock(){
        wireMockServer.stop();
    }

    @Test
    void shouldReturnNonForkRepositoriesWithBranches() {
        wireMockServer.stubFor(get(urlEqualTo("/users/octocat/repos"))
                .willReturn(okJson("""
                        [
                            {
                                "name": "hello-world",
                                "owner": {
                                    "login": "octocat"
                                },
                                "fork": false
                            },
                            {
                                "name": "forked-repo",
                                "owner": {
                                    "login": "octocat"
                                },
                                "fork": true
                            }
                        ]
                        """)));
        wireMockServer.stubFor(get(urlEqualTo("/repos/octocat/hello-world/branches"))
                .willReturn(okJson("""
                        [
                          {
                            "name": "main",
                            "commit": {
                              "sha": "abc123"
                            }
                          },
                          {
                            "name": "develop",
                            "commit": {
                              "sha": "def456"
                            }
                          }
                        ]
                        """)));

        String response = restClient.get()
                .uri("http://localhost:{port}/repositories/octocat", port)
                .retrieve()
                .body(String.class);

        assertThat(response).isEqualToIgnoringWhitespace("""
                [
                  {
                    "repositoryName": "hello-world",
                    "ownerLogin": "octocat",
                    "branches": [
                      {
                        "name": "main",
                        "lastCommitSha": "abc123"
                      },
                      {
                        "name": "develop",
                        "lastCommitSha": "def456"
                      }
                    ]
                  }
                ]
                """);
    }

    @Test
    void shouldReturn404WhenUserDoesNotExist() {
        wireMockServer.stubFor(get(urlEqualTo("/users/unknown-user/repos"))
                .willReturn(aResponse().withStatus(404)));

        String response = restClient.get()
                .uri("http://localhost:{port}/repositories/unknown-user", port)
                .retrieve()
                .onStatus(
                        status -> status.value() == 404,
                        (request, clientResponse) -> {
                        }
                )
                .body(String.class);
        assertThat(response)
                .contains("\"status\":404")
                .contains("\"message\":\"GitHub user not found: unknown-user\"");

    }
}
