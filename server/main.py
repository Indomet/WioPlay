from src.Server import Server

server = Server()
term = input("Enter a search term: ")
results = server.getResults(term)

# printing the results
for result in results.values():
    print(result)
