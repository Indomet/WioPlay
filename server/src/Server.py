from pyyoutube import Api, SearchListResponse
from src.config import API_KEYS
from src.SearchResult import SearchResult
api = Api(api_key=API_KEYS.youtube)


class Server:

    # results length will be 15 at most
    RESULT_LIMIT = 15

    @staticmethod
    def getResults(searchTerm: str):
        result = api.search_by_keywords(
            q=searchTerm,
            search_type=["video"],
            count=Server.RESULT_LIMIT,
            limit=Server.RESULT_LIMIT
        )
        return Server.formatResults(result)

    @staticmethod
    def formatResults(result: SearchListResponse):
        searchResults = {}
        for item in result.items:
            searchResult = SearchResult(item)
            searchResults[searchResult.id] = searchResult
        return searchResults
