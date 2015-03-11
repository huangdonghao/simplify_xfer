package simplify_xfer

import grails.transaction.Transactional
import groovy.xml.MarkupBuilder
import jline.internal.Log

import javax.net.ssl.HttpsURLConnection

@Transactional
class MasterCardOpenAPIService {

    def createCardMappingURL = "https://sandbox.api.mastercard.com/moneysend/v2/mapping/card?Format=XML"
    def inquiryCardMappingURL = "https://sandbox.api.mastercard.com/moneysend/v2/mapping/card?Format=XML"
    def cardEligibilityURL = "https://sandbox.api.mastercard.com/moneysend/v2/eligibility/pan"

    def createCardMapping() {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.CreateMappingRequest() {
            SubscriberId('yonghow@gmail.com')
            SubscriberType('EMAIL_ADDRESS')
            AccountUsage('RECEIVING')
            DefaultIndicator('T')
            Alias('My Debit Card 3')
            ICA('009674')
            AccountNumber('5184680430000006')
            ExpiryDate('201908')
            CardholderFullName() {
                CardholderFirstName('John')
                CardholderMiddleInitial('Q')
                CardholderLastName('Public')
            }
            Address() {
                Line1('123 Main Street')
                Line2('#5A')
                City('Somewhere')
                CountrySubdivision('MO')
                PostalCode('69999')
                Country('USA')
            }
            DateOfBirth('19840102')
        }

        def xmlString = writer.buffer.toString()
        Log.info('Create Card XML: ')
        Log.info(xmlString)
        return sendAPIRequest(createCardMappingURL, xmlString, "POST")
    }

    def inquireCardMapping() {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)

        xml.InquireMappingRequest() {
            SubscriberId('yonghow@gmail.com')
            SubscriberType('EMAIL_ADDRESS')
        }

        def xmlString = writer.buffer.toString()
        Log.info('Inquire Card XML: ')
        Log.info(xmlString)
        return sendAPIRequest(inquiryCardMappingURL, xmlString, "PUT")
    }

    def checkCardEligible() {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)

        xml.PanEligibilityRequest() {
           SendingAccountNumber('5184680430000006')
        }

        def xmlString = writer.buffer.toString()
        return sendAPIRequest(cardEligibilityURL, xmlString, "PUT")
    }

    def sendAPIRequest(String url, String bodyContent, String method) {
        def masterCardAPIHttpsConnection = new MasterCardAPIHttpsConnection();
        def HttpsURLConnection httpsURLConnection = masterCardAPIHttpsConnection.createOpenAPIConnection(url, bodyContent, method);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = bufferedReader.readLine()) != null) {
            response.append(inputLine);
        }
        bufferedReader.close();

        return response.toString();
    }
}
