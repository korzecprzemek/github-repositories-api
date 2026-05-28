package pl.korzec.githubrepositoriesapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RepositoryController {
    private final RepositoryService repositoryService;

    RepositoryController(RepositoryService repositoryService){
        this.repositoryService = repositoryService;
    }
    @GetMapping("/repositories/{username}")
    List<Object> getRepositories(@PathVariable String username) {
        return repositoryService.getRepositories(username);
    }
}
