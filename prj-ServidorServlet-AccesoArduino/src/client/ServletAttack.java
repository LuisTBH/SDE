package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

class ServletAttack {
	public String getData(String address, String queryString) throws MalformedURLException, IOException {
		String result = null;
		String urlStr = address;
		if (queryString != null && queryString.length() > 0)
			urlStr += "?" + queryString;
		HttpURLConnection urlc = (HttpURLConnection) (new URL(urlStr)).openConnection();
		urlc.setRequestMethod("GET");

		// Get the response
		BufferedReader rd = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
		StringBuilder data = new StringBuilder(150);
		String line;
		while ((line = rd.readLine()) != null)
			data.append(line);
		rd.close();
		result = data.toString();
		urlc.disconnect();

		return result;
	}

	public String postData(String address, String dataToBePosted)
			throws MalformedURLException, IOException, ProtocolException {

		HttpURLConnection urlc = (HttpURLConnection) (new URL(address)).openConnection();
		;
		urlc.setRequestMethod("POST");

		urlc.setDoOutput(true);
		urlc.setDoInput(true);
		urlc.setUseCaches(false);
		urlc.setAllowUserInteraction(false);
		urlc.setRequestProperty("Content-type", "text/xml; charset=" + "UTF-8");
		urlc.setRequestProperty("Cookie", "name=MyName;DNI=12345678");

		// Post the data
		OutputStream out = null;
		out = urlc.getOutputStream();
		Writer writer = new OutputStreamWriter(out, "UTF-8");
		writer.write(dataToBePosted);
		writer.close();
		out.close();

		// read the response back from the posted data
		BufferedReader bfreader = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
		StringBuilder builder = new StringBuilder(100);
		String line = "";
		while ((line = bfreader.readLine()) != null) {
			builder.append(line);
		}
		bfreader.close();

		/** return the response back from the POST */
		return builder.toString();
	}

	public static void main(String args[]) {
		ServletAttack sa = new ServletAttack();
		String retval = "";
		while (true) {
			try {
				Thread.sleep(1000);
				retval = sa.getData("http://localhost:8080/prj-ServidorServlet-AccesoArduino/ServletAccesoArduino",
						"id=TODOS");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			System.out.println(retval);
		}
	}
}