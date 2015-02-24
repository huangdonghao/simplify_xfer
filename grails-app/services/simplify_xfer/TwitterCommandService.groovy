package simplify_xfer

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.social.twitter.api.UserList

import twitter4j.ConnectionLifeCycleListener;
import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice
import twitter4j.TwitterStream
import twitter4j.TwitterStreamFactory
import twitter4j.User;
import twitter4j.UserStreamAdapter;
import twitter4j.UserStreamListener
import twitter4j.auth.AccessToken
import twitter4j.conf.ConfigurationBuilder
import grails.transaction.Transactional

@Transactional
class TwitterCommandService implements ApplicationContextAware, UserStreamListener {

	static transactional = true
	ApplicationContext applicationContext

	def twitter4jService // twitter4j.Twitter

	public void init() {
		def agentTwitterId = twitter4jService.id
		def agentTwitterName = twitter4jService.screenName

		String startupMessage = """
!------
! TwitterCommandService started:
!     I am [${agentTwitterName}] (${agentTwitterId})
!---------"""
		
		log.info startupMessage
	}
	
	public void startListening(def it) {
		log.info("Start listening: [${it.screenName}] (${it.userId})")
		
		TwitterStreamFactory twitterStreamFactory = new TwitterStreamFactory(twitter4jService.configuration);
		TwitterStream twitterStream = twitterStreamFactory.getInstance(new AccessToken(it.token, it.tokenSecret));
		twitterStream.addListener(this);
		
		twitterStream.user()
		
		//String[] track = ["pay", "ask", "approve", "decline"]
		//twitterStream.user(track)
	}


	@Override
	public void onException(Exception arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTrackLimitationNotice(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatus(Status status) {
		// TODO Auto-generated method stub
		System.out.println(status.getText());
	}

	@Override
	public void onScrubGeo(long arg0, long arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserProfileUpdate(User arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnfavorite(User arg0, User arg1, Status arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnblock(User arg0, User arg1) {
		// TODO Auto-generated method stub

	}



	@Override
	public void onFriendList(long[] arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFollow(User arg0, User arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFavorite(User arg0, User arg1, Status arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDirectMessage(DirectMessage message) {
		// TODO Auto-generated method stub
		log.debug("onDirectMessage: ${message}");
	}

	@Override
	public void onDeletionNotice(long arg0, long arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBlock(User arg0, User arg1) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onStallWarning(StallWarning arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onUnfollow(User arg0, User arg1) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onUserListCreation(User arg0, twitter4j.UserList arg1) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onUserListDeletion(User arg0, twitter4j.UserList arg1) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onUserListMemberAddition(User arg0, User arg1,
			twitter4j.UserList arg2) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onUserListMemberDeletion(User arg0, User arg1,
			twitter4j.UserList arg2) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onUserListSubscription(User arg0, User arg1,
			twitter4j.UserList arg2) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onUserListUnsubscription(User arg0, User arg1,
			twitter4j.UserList arg2) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onUserListUpdate(User arg0, twitter4j.UserList arg1) {
		// TODO Auto-generated method stub

	}
}