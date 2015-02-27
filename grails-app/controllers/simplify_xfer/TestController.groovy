package simplify_xfer

class TestController {

    def masterCardOpenAPIService;

    def index() {
        render "Test Controller"
    }

    def test() {
        render "Hello World"
    }

    def createCardMapping() {
        masterCardOpenAPIService.createCardMapping();
    }
}
