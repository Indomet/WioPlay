from src.SearchResult import SearchResult
from src.Server import Server


def test_resultLength():
    # result length must be at most equal to the limit specified
    results = Server.getResults("cat")
    assert len(results) <= Server.RESULT_LIMIT


def test_isResult():
    # result elements must be of type SearchResult
    results = list(Server.getResults("dog").values())
    assert isinstance(results.pop(), SearchResult)
