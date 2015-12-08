#!/usr/bin/python

import tweepy
from HashTagListener import HashTagListener

consumer_key = 'Ss4TQn7x9tEMkrhQAWqbZ2x0j' 
consumer_secret = '9gxwZKfSwWdqvpgrK8wUKQ2OxmxoglNOC47pr8hvC9ltGHAL9q'
access_token = '4314262877-rBWOrLhX3IVp0NBk5cS30crM6Q6PoD4YIHLewYP'
access_toke_secret = 'thJhh46P0im2tQIPlYXyhMQ4xD71bB8pzPXFj1GofKYfu'

listener = None

def authenticate():
	auth = tweepy.OAuthHandler(consumer_key,consumer_secret)
	auth.set_access_token(access_token, access_toke_secret)
	
	return tweepy.API(auth)
	

def main():
	
	api = authenticate();
	
	if(api == None):
		print "Failed to Authenticate"
	else:
		print  "Authenticated Successfully!"
		
		listener = HashTagListener()
		stream = tweepy.Stream(api.auth, listener)
		stream.filter(track=['#SharpR0cks'])
main()
