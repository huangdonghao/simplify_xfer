package simplify_xfer

import grails.transaction.Transactional
import groovy.xml.MarkupBuilder

import javax.net.ssl.HttpsURLConnection

@Transactional
class MasterCardOpenAPIService {

    def sandBoxURL = "https://sandbox.api.mastercard.com/moneysend/v2/mapping/card?Format=XML"

    def createCardMapping() {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.CreateMappingRequest() {
            SubscriberId('13147449999')
            SubscriberType('PHONE_NUMBER')
            AccountUsage('RECEIVING')
            DefaultIndicator('T')
            Alias('My Debit Card')
            ICA('009674')
            AccountNumber('5184680430000006')
            ExpiryDate('201401')
            CardholderFullName() {
                CardholderFirstName('John')
                CardholderMiddleName('Q')
                CardholderLastName('Public')
                CartholderFullName()
            }
            Address() {
                Line1('123 Main Street')
                Line2('#5A')
                City('Somewhere')
                CountrySubdivision('MO')
                PostalCode('69999')
                Country('USA')
            }
            DateOfBirth('DateOfBirth')
        }

        def xmlString = writer.buffer.toString()
        sendAPIRequest(xmlString)
    }

    def sendAPIRequest(String bodyContent) {
        def masterCardAPIHttpsConnection = new MasterCardAPIHttpsConnection();
        def HttpsURLConnection httpsURLConnection = masterCardAPIHttpsConnection.createOpenAPIConnection(sandBoxURL, bodyContent);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = bufferedReader.readLine()) != null) {
            response.append(inputLine);
        }
        bufferedReader.close();

        System.out.println(response.toString());
    }
}
