# API proxy for GitHub repositories

## Requirements:
- Java 25
- Gradle

## How to run:
``` ./gradlew bootRun ```

## How to run tests:
``` ./gradlew test ```

## Example request:

``` GET /repositories/octocat ```

## Example response:

```
[
    {
        "repositoryName": "hello-world",
        "ownerLogin": "octocat",
        "branches": [
            {
                "name": "main",
                "lastCommitSha": "abc123"
            }
        ]
    }
]
```

## 404 example:

```
{
    "status": 404,
    "message": GitHub user not found: unknown-user"
}
```