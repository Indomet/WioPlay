# importing packages
import math
from pytube import YouTube
import os

END_OF_LINE = "\n"

class Mp3Downloader:
    #set class variables
    #140 default itag for 128kbps mp3 format
    #Refer to this for more information on youtube itags: https://gist.github.com/sidneys/7095afe4da4ae58694d128b1034e01e2

    duration = None
    title = None
    description = None #Not used yet
    author = None
    thumbnail = None
    itag = 140
    youtubeObject = None
    abr = None #Average bit rate

    def __init__(self):
        pass

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

    def getFileStream(self): #returns all filtered file streams retreived
        streams = self.youtubeObject.streams.filter(only_audio=True)
        list = ""
        for stream in streams:
            list += f"Itag : {stream.itag}, Quality : {stream.abr}, VCodec : {stream.codecs[0]}" + END_OF_LINE
        return list

