from pyyoutube import SearchResult as ApiResult


class SearchResult:
    def __init__(self, result: ApiResult):
        self.id = result.id.videoId
        self.title = result.snippet.title
        self.description = result.snippet.description
        self.thumbnail = result.snippet.thumbnails.high.url
        self.channelID = result.snippet.channelId

    def __str__(self) -> str:
        return f"""Result:
        ID: {self.id}
        Title: {self.title}
        Thumbnail: {self.thumbnail}
        Channel ID: {self.channelID}"""

    def __repr__(self) -> str:
        return self.id
