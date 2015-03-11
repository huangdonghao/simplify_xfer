package simplify_xfer

import grails.converters.JSON
import grails.converters.XML

class TestController {

    def masterCardOpenAPIService;

    def index() {
        render "Test Controller"
    }

    def test() {
        render "Hello World"
    }

    def createCardMapping() {
        render(text: masterCardOpenAPIService.createCardMapping(), contentType: "text/xml", encoding: "UTF-8")
    }

    def inquireCardMapping() {
        render(text: masterCardOpenAPIService.inquireCardMapping(), contentType: "text/xml", encoding: "UTF-8")
    }

    def checkCard() {
        render(text: masterCardOpenAPIService.checkCardEligible(), contentType: "text/xml", encoding: "UTF-8")
    }
}
