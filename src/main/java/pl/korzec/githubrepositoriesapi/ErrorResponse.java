package pl.korzec.githubrepositoriesapi;

public record ErrorResponse(
        int status,
        String message
) {
}
