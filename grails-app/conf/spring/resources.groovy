// Place your Spring DSL code here
beans = {
	securityEventListener(simplify_xfer.SecurityEventListener) {
		twitterCommandService = ref("twitterCommandService")
	}
}
