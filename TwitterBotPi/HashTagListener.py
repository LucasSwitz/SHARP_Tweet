#!/usr/bin/python

import tweepy
class HashTagListener(tweepy.StreamListener):

    def on_status(self, status):
	print(status.text)
        return True
 
    def on_error(self, status_code):
        print('Got an error with status code: ' + str(status_code))
        return True
 
    def on_timeout(self):
        print('Timeout...')
        return True
