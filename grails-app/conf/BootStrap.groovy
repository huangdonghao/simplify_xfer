import com.the6hours.example.Role

class BootStrap {

	def twitterCommandService
	
    def init = { servletContext ->
        new Role(authority: 'ROLE_USER').save(failOnError: true, flush: true)
        new Role(authority: 'ROLE_TWITTER').save(failOnError: true, flush: true)
        new Role(authority: 'ROLE_EXAMPLE').save(failOnError: true, flush: true)
        new Role(authority: 'ROLE_SOCIAL').save(failOnError: true, flush: true)
		
		twitterCommandService.init()
    }
	
    def destroy = {
    }
}
