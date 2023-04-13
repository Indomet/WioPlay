# importing packages
import math
from pytube import YouTube
import os

END_OF_LINE = "\n"

class Mp3Downloader:
    #set class variables
    #140 default itag for 128kbps mp3 format
    #Refer to this for more information on youtube itags: https://gist.github.com/sidneys/7095afe4da4ae58694d128b1034e01e2

    def __init__(self, duration = None, title = None, description = None, author = None, thumbnail = None, itag = 140, youtubeObject = None, abr = None ):

        self.duration = duration
        self.title = title
        self.description = description #Not used yet
        self.author = author
        self.thumbnail = thumbnail

        self.itag = itag
        self.youtubeObject = youtubeObject
        self.abr = abr #Average bit rate

    def getFileStream(self): #returns all filtered file streams retreived
        streams = self.youtubeObject.streams.filter(only_audio=True)
        list = ""
        for stream in streams:
            list += f"Itag : {stream.itag}, Quality : {stream.abr}, VCodec : {stream.codecs[0]}" + END_OF_LINE
        return list

    def setFormat(self, itag):
         self.itag = itag

    # download the file
    def download(self, link, destination):
        self.youtubeObject = YouTube(link)
        file = self.youtubeObject.streams.get_by_itag(self.itag)
        self.abr = file.abr
        out_file = file.download(output_path=destination)

        # save the file as .mp3 format
        fileName, extension = os.path.splitext(out_file)
        new_file = fileName + '.mp3'
        os.rename(out_file, new_file)

    #Output metadata
    #Can be simplified by without making class attributes at all
    def outMessage(self):
        message = self.youtubeObject.title + " " + self.abr + " has been downloaded." + END_OF_LINE + "Duration: " + str(math.floor(self.youtubeObject.length/60)) + ":" + str(self.youtubeObject.length%60) + END_OF_LINE + "Author: " + self.youtubeObject.author + END_OF_LINE + "Thumbnail: " + self.youtubeObject.thumbnail_url
        return message




#Old, ------------- Command Line tool version ------------------
def download_mp3():

    #URL from user input
    yt = YouTube(str(input("Enter the URL of the video you want to download: \n>> ")))

    # extract only audio
    streams = yt.streams.filter(only_audio=True)

    for stream in streams:
        print(f"Itag : {stream.itag}, Quality : {stream.abr}, VCodec : {stream.codecs[0]}")

    itag = input("Enter itag Value : ")
    file = yt.streams.get_by_itag(itag)

    # check for destination to save file
    print("Enter the destination (leave blank for current directory)")
    #relative path
    destination = str(input(">> ")) or "downloads/mp3"

    # download the file
    out_file = file.download(output_path=destination)

    # save the file

    base, ext = os.path.splitext(out_file)
    new_file = base + '.mp3'
    os.rename(out_file, new_file)

    # result of success
    duration = yt.length
    description = yt.description
    author = yt.author
    thumbnail = yt.thumbnail_url

    print(yt.title + " "+file.abr + " has been successfully downloaded.")
    print("thumbnail image: " + thumbnail)
    print("description: " + description)
    print("author: " + author)
    print("duration: " + str(duration))
    print("duration: " + str(math.floor(duration/60)) + ":" + str(duration%60))



def main():

    #receive a link from MQTT
    link = "https://www.youtube.com/watch?v=0GEm67c9WUg&list=PLH7pNzRrQXY3XQJ4K5zPih4Y1jfdgm1fN&index=9"
    destination = "downloads/mp3"

    downloader = Mp3Downloader()
    downloader.setFormat(140) #128kbps mp3, leave blank for default.
    downloader.download(link, destination)
    print(downloader.outMessage())

    print(downloader.getFileStream()) #outputs all available formats for download



if __name__== "__main__":
    main()
