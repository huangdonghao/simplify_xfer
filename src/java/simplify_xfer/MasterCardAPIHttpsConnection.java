package simplify_xfer;

import com.google.gdata.client.authn.oauth.OAuthException;
import com.google.gdata.client.authn.oauth.OAuthParameters;
import com.google.gdata.client.authn.oauth.OAuthRsaSha1Signer;
import com.google.gdata.client.authn.oauth.OAuthUtil;
import com.google.gdata.util.common.util.Base64;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import javax.security.cert.CertificateException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

/**
 * Created by yonghow on 26/2/15.
 */
public class MasterCardAPIHttpsConnection {

    private String consumerKey = "ApyjTbI5xPE7ak73xcoTAK1a5dYWUXY3FbRrdQO-e26d23b0!414e6b414738467564786e7863394435482b436b6856383d";

    /**
     * Establish an OAuth connection to a MasterCard API over HTTPS.
     * @param httpsURL The full URL to call, including any querystring parameters.
     * @param body The body to include.  If this has a body, an HTTP POST will be established,
     * 			   this content will be used to generate the oauth_body_hash and the contents passed
     * 			   as the body of the request.  If the body parameter is null, an HTTP GET
     *             will be established.
     */
    public HttpsURLConnection createOpenAPIConnection(String httpsURL, String body, String method) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, OAuthException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException, UnrecoverableEntryException {
        HttpsURLConnection con = null;
        PrivateKey privKey = getPrivateKey();
        if (privKey != null) {
            OAuthRsaSha1Signer rsaSigner = new OAuthRsaSha1Signer();
            OAuthParameters params = new OAuthParameters();
            params.setOAuthConsumerKey(consumerKey);
            params.setOAuthNonce(OAuthUtil.getNonce());
            params.setOAuthTimestamp(OAuthUtil.getTimestamp());
            params.setOAuthSignatureMethod("RSA-SHA1");
            params.setOAuthType(OAuthParameters.OAuthType.TWO_LEGGED_OAUTH);
            params.addCustomBaseParameter("oauth_version", "1.0");
            rsaSigner.setPrivateKey(privKey);

            if (body != null) {
                MessageDigest digest = MessageDigest.getInstance("SHA-1");
                digest.reset();
                byte[] hash = digest.digest(body.getBytes("UTF-8"));
                String encodedHash = Base64.encode(hash);

                params.addCustomBaseParameter("oauth_body_hash", encodedHash);
            }

            String baseString = OAuthUtil.getSignatureBaseString(httpsURL, method, params.getBaseParameters());
            System.out.println(baseString);

            String signature = rsaSigner.getSignature(baseString, params);

            params.addCustomBaseParameter("oauth_signature", signature);

            URL url = new URL(httpsURL);
            con = (HttpsURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod(method);
            con.setSSLSocketFactory((SSLSocketFactory)SSLSocketFactory.getDefault());
            con.addRequestProperty("Authorization",	buildAuthHeaderString(params));

            System.out.println(buildAuthHeaderString(params));

            if (body != null) {
                con.addRequestProperty("content-type", "application/xml; charset=UTF-8");
                con.addRequestProperty("content-length", Integer.toString(body.length()));
            }
            con.connect();

            if (body != null) {
                CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
                OutputStreamWriter request = new OutputStreamWriter(con.getOutputStream(), encoder);
                request.append(body);
                request.flush();
                request.close();
            }


        }

        return con;
    }

    /**
     * Get PrivateKey
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws UnrecoverableEntryException
     */
    protected PrivateKey getPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, KeyStoreException, CertificateException, UnrecoverableEntryException {

        try {
            String kspw = "MyKeystorePassword";
            String privKeyFile = "keys/MCOpenAPI.p12";
            String keyAlias = "MyKeyAlias";

            KeyStore ks = KeyStore.getInstance("PKCS12");



            // get user password and file input stream
            ClassLoader cl = this.getClass().getClassLoader();
            InputStream stream = cl.getResourceAsStream(privKeyFile);
            ks.load(stream, kspw.toCharArray());
            Key key = ks.getKey(keyAlias, kspw.toCharArray());

            return (PrivateKey) key;
        } catch(java.security.cert.CertificateException ce) {
            System.out.println("Unable to generate private key!");
            return null;
        }
    }


    private String buildAuthHeaderString(OAuthParameters params) {
        StringBuffer buffer = new StringBuffer();
        int cnt = 0;
        buffer.append("OAuth ");
        Map<String, String> paramMap = params.getBaseParameters();
        Object[] paramNames = paramMap.keySet().toArray();
        for (Object paramName : paramNames) {
            String value = paramMap.get((String) paramName);
            buffer.append(paramName + "=\"" + OAuthUtil.encode(value) + "\"");
            cnt++;
            if (paramNames.length > cnt) {
                buffer.append(",");
            }

        }
        return buffer.toString();
    }
}
