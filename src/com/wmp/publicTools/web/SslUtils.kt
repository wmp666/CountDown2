package com.wmp.publicTools.web

import com.wmp.publicTools.printLog.Log
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.cert.X509Certificate
import javax.net.ssl.*

object SslUtils {
    private fun trustAllHttpsCertificates() {
        try {
            val trustAllCerts = arrayOfNulls<TrustManager>(1)
            val tm: TrustManager = miTM()
            trustAllCerts[0] = tm
            val sc = SSLContext.getInstance("SSL")
            sc.init(null, trustAllCerts, null)
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory())
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: KeyManagementException) {
            throw RuntimeException(e)
        }
    }

    /**
     * 忽略HTTPS请求的SSL证书，必须在openConnection之前调用
     * 
     */
    @JvmStatic
    fun ignoreSsl() {
        val hv = HostnameVerifier { urlHostName: String?, session: SSLSession? ->
            Log.info.print("SslUtils", "Warning: URL Host: " + urlHostName + " vs. " + session!!.getPeerHost())
            true
        }
        trustAllHttpsCertificates()
        HttpsURLConnection.setDefaultHostnameVerifier(hv)
    }

    internal class miTM : TrustManager, X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate?>? {
            return null
        }

        fun isServerTrusted(certs: Array<X509Certificate?>?): Boolean {
            return true
        }

        fun isClientTrusted(certs: Array<X509Certificate?>?): Boolean {
            return true
        }

        override fun checkServerTrusted(certs: Array<X509Certificate?>?, authType: String?) {
        }

        override fun checkClientTrusted(certs: Array<X509Certificate?>?, authType: String?) {
        }
    }
}
