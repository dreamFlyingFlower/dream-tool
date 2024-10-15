package dream.flying.flower.http;

import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dream.flying.flower.collection.CollectionHelper;

/**
 * 信任所有HTTPS证书
 *
 * @author 飞花梦影
 * @date 2024-07-19 16:16:18
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class HttpsTrust {

	/**
	 * 信任所有证书
	 */
	public static void trustAllCerts() {
		try {
			trustHttpsCertificates();
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

				@Override
				public boolean verify(String hostname, SSLSession session) {
					// 若直接返回true,表示信任所有证书
					return true;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 信任指定证书
	 * 
	 * @param trustedCerts 信任证书列表
	 */
	public static void trustCerts(List<String> trustedCerts) {
		try {
			trustHttpsCertificates();
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

				@Override
				public boolean verify(String hostname, SSLSession session) {
					if ("localhost".equals(hostname) || "127.0.0.1".equals(hostname)) {
						return true;
					}
					if (CollectionHelper.isEmpty(trustedCerts)) {
						return false;
					}
					if (trustedCerts.contains(hostname)) {
						return true;
					}
					return false;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void trustHttpsCertificates() throws Exception {
		TrustManager[] trustManagers = new TrustManager[1];
		TrustManager httpTrustManager = new HttpsTrustManager();
		trustManagers[0] = httpTrustManager;
		SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
		sc.init(null, trustManagers, null);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	static class HttpsTrustManager implements TrustManager, X509TrustManager {

		@Override
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		@Override
		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}

		@Override
		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}
	}
}