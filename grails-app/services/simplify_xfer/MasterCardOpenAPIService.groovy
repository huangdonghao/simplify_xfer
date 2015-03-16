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
    def transferURL = "https://sandbox.api.mastercard.com/moneysend/v2/transfer"

    def createCardMapping() {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.CreateMappingRequest() {
            SubscriberId('yonghow@gmail.com')
            SubscriberType('EMAIL_ADDRESS')
            AccountUsage('SENDING')
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

    def fundingRequest() {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)

        xml.FundingRequest() {
            LocalDate('0312')
            LocalTime('180000')
            TransactionReference('0999999034810154901') // TODO: Auto generate random number.
            FundingMapped() {
                SubscriberId('')
                SubscriberType('')
                SubscriberAlias('')
            }
            FundingUCAF('')
            FundingMasterCardAssignedId('')
            FundingAmount() {
                Value('15000')
                Currency('840')
            }
            ReceiverName('')
            ReceiverAddress() {
                Line1('')
                Line2('')
                City('')
                CountrySubdivision('')
                PostalCode('')
                Country('')
            }
            ReceiverPhone('')
            Channel('')
            UCAFSupport('')
            ICA('')
            ProcessorId('')
            RoutingAndTransitNumber('')
            CardAcceptor() {
                Name('')
                City('')
                State('')
                PostalCode('')
                Country('')
            }
            TransactionDesc('')
            MerchantId('')

        }

        def xmlString = writer.buffer.toString()
        return sendAPIRequest(cardEligibilityURL, xmlString, "PUT")
    }

    def transferRequest() {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)

        xml.TransferRequest() {
            LocalDate('0316')
            LocalTime('161222')
            TransactionReference('0999999034810154966')
            SenderName('John Doe')
            SenderAddress() {
                Line1('123 Main Street')
                Line2('#5A')
                City('Arlington')
                CountrySubdivision('VA')
                PostalCode('22207')
                Country('USA')
            }
            FundingCard() {
                AccountNumber('5184680430000006')
                ExpiryMonth('11')
                ExpiryYear('2018')
            }
            FundingAmount() {
                Value('15000')
                Currency('840')
            }
            ReceiverName('Jose Lopez')
            ReceiverAddress() {
                Line1('Pueblo Street')
                Line2('PO BOX 12')
                City('El PASO')
                CountrySubdivision('TX')
                PostalCode('79906')
                Country('USA')
            }
            ReceiverPhone('1800639426')
            ReceivingCard() {
                AccountNumber('5184680430000006')
            }
            ReceivingAmount() {
                Value('182206')
                Currency('484')
            }
            Channel('W')
            ICA('009674')
            ProcessorId('9000000442')
            UCAFSupport('true')
            RoutingAndTransitNumber('990442082')
            CardAcceptor(){
                Name('My Local Bank')
                City('Saint Louis')
                State('MO')
                PostalCode('63101')
                Country('USA')
            }
            TransactionDesc('P2P')
        }


        def xmlString = writer.buffer.toString()
        Log.info('TransferRequest:')
        Log.info(xmlString)
        return sendAPIRequest(transferURL, xmlString, "POST")
    }

    def sendAPIRequest(String url, String bodyContent, String method) {
        def masterCardAPIHttpsConnection = new MasterCardAPIHttpsConnection();
        def HttpsURLConnection httpsURLConnection = masterCardAPIHttpsConnection.createOpenAPIConnection(url, bodyContent, method);

        BufferedReader bufferedReader
        String inputLine;
        StringBuffer response = new StringBuffer();

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
        } catch (Exception e) {
            bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getErrorStream()));
        }

        while ((inputLine = bufferedReader.readLine()) != null) {
            response.append(inputLine);
        }
        bufferedReader.close();

        return response.toString();
    }
}
