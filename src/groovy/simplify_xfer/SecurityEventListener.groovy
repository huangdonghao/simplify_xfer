package simplify_xfer

import javax.servlet.http.*
import org.apache.commons.logging.LogFactory
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AbstractAuthenticationEvent
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutHandler
import twitter4j.auth.AccessToken

class SecurityEventListener implements ApplicationListener<AbstractAuthenticationEvent>, LogoutHandler {

	def twitterCommandService
	private static final log = LogFactory.getLog(this)

	void onApplicationEvent(AbstractAuthenticationEvent event) {
		event.authentication.with {
			def username = principal.hasProperty('username')?.getProperty(principal) ?: principal
			log.info "event=${event.class.simpleName} username=${username} "

			if (event instanceof InteractiveAuthenticationSuccessEvent) {
				twitterCommandService.startListening(it)
			}
		}
	}

	void logout(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		authentication.with {
			def username = principal.hasProperty('username')?.getProperty(principal) ?: principal
			log.info "event=Logout username=${username} "
		}
	}
}