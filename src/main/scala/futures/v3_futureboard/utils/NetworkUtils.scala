package futures.v3_futureboard.utils

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.params.HttpConnectionParams

import scala.util.Try

object NetworkUtils {

    /**
      * Returns the text (content) from a REST URL as a String.
      * Returns a blank String if there was a problem.
      * This function will also throw exceptions if there are problems trying
      * to connect to the url:
      *
      *     java.net.UnknownHostException
      *     org.apache.http.conn.ConnectTimeoutException
      *     java.net.SocketTimeoutException
      *
      * @param url A complete URL, such as "http://foo.com/bar"
      * @param connectionTimeout The connection timeout, in ms.
      * @param socketTimeout The socket timeout, in ms.
      */
    @throws(classOf[java.net.UnknownHostException])
    @throws(classOf[java.net.SocketTimeoutException])
    @throws(classOf[org.apache.http.conn.ConnectTimeoutException])
    def get(
        url: String,
        connectionTimeout: Int = 5000 ,
        socketTimeout: Int = 5000): String =
    {
        val httpClient = buildHttpClient(connectionTimeout, socketTimeout)
        val httpResponse = httpClient.execute(new HttpGet(url))
        val entity = httpResponse.getEntity
        var content = ""
        if (entity != null) {
            val inputStream = entity.getContent
            content = io.Source.fromInputStream(inputStream).getLines.mkString
            inputStream.close
        }
        httpClient.getConnectionManager.shutdown
        content
    }


    /**
      * Returns the text (content) from a REST URL as a String.
      * Returns a blank String if there was a problem.
      * This function will also throw exceptions if there are problems trying
      * to connect to the url.
      *
      * @param url A complete URL, such as "http://foo.com/bar"
      * @param connectionTimeout The connection timeout, in ms.
      * @param socketTimeout The socket timeout, in ms.
      */
    def getAsTry(
        url: String,
        connectionTimeout: Int = 5000 ,
        socketTimeout: Int = 5000): Try[String] =
    Try {
        val httpClient = buildHttpClient(connectionTimeout, socketTimeout)
        val httpResponse = httpClient.execute(new HttpGet(url))
        val entity = httpResponse.getEntity
        var content = ""
        if (entity != null) {
            val inputStream = entity.getContent
            content = io.Source.fromInputStream(inputStream).getLines.mkString
            inputStream.close
        }
        httpClient.getConnectionManager.shutdown
        content
    }

    // TODO update code to use HttpClientBuilder instead of DefaultHttpClient
    private def buildHttpClient(connectionTimeout: Int, socketTimeout: Int): DefaultHttpClient = {
        val httpClient = new DefaultHttpClient
        val httpParams = httpClient.getParams
        HttpConnectionParams.setConnectionTimeout(httpParams, connectionTimeout)
        HttpConnectionParams.setSoTimeout(httpParams, socketTimeout)
        httpClient.setParams(httpParams)
        httpClient
    }


}
