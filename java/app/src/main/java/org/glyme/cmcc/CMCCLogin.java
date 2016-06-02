package org.glyme.cmcc;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by dell on 2016/4/12.
 */
public class CMCCLogin {
    final static String url_login = "http://218.200.239.185:8888/portalserver/user/unionlogin.do?brasip=221.182.42.11&braslogoutip=221.182.42.11&area=union&wlanuserip=null&redirectUrl=example/cnunion/cnunion&domain=@chinamobile";
    final static String url_yzm = "http://218.200.239.185:8888/portalserver/user/randomimage";

//    public static void main(String[] args) throws IOException {
//        String s = getCookie();
//        ByteBuffer buf = getVerifyCode(s);
//        FileChannel fc = FileChannel.open(Paths.get("d:/a.jpg"), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
//        fc.write(buf);
//        fc.close();
//        if (s != null) {
//            System.out.println(s);
//        }
//    }

    public static String getCookie() {
        try {
            URL url = new URL("http://218.200.239.185:8888/portalserver/scuniondzkdqs.jsp");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Host", "218.200.239.185:8888");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv,33.0) Gecko/20100101 Firefox/33.0");
            conn.setRequestProperty("Referer", "http,//218.200.239.185:8888/portalserver/scuniondzkdqs.jsp");
            conn.setRequestProperty("Connection", "keep-alive");

            conn.setRequestMethod("GET");

            conn.connect();

            if (conn.getResponseCode() != 200)
                return null;

            url = new URL("http://218.200.239.185:8888/portalserver/user/close.do");
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Host", "218.200.239.185:8888");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv,33.0) Gecko/20100101 Firefox/33.0");
            conn.setRequestProperty("Referer", "http,//218.200.239.185:8888/portalserver/scuniondzkdqs.jsp");
            conn.setRequestProperty("Connection", "keep-alive");

            conn.setRequestMethod("GET");

            conn.connect();

            if (conn.getResponseCode() != 200)
                return null;

            url = new URL(url_login);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Host", "218.200.239.185:8888");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv,33.0) Gecko/20100101 Firefox/33.0");
            conn.setRequestProperty("Referer", "http,//218.200.239.185:8888/portalserver/scuniondzkdqs.jsp");
            conn.setRequestProperty("Connection", "keep-alive");

            conn.setRequestMethod("GET");

            conn.connect();

            if (conn.getResponseCode() != 200)
                return null;

            String headerName = null;
            for (int i = 1; (headerName = conn.getHeaderFieldKey(i)) != null; i++) {
//                System.out.println(conn.getHeaderField(i));
                if (headerName.equals("Set-Cookie")) {
                    return conn.getHeaderField(i);
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ByteBuffer getVerifyCode(String cookie) {
        try {
            URL url = new URL(url_yzm);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Host", "218.200.239.185:8888");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv,33.0) Gecko/20100101 Firefox/33.0");
            conn.setRequestProperty("Referer", "http,//218.200.239.185:8888/portalserver/scuniondzkdqs.jsp");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Cookie", cookie);

            conn.setRequestMethod("GET");

            conn.connect();

            if (conn.getResponseCode() != 200)
                return null;

            ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
            ReadableByteChannel resp_chann = Channels.newChannel(conn.getInputStream());
            resp_chann.read(byteBuffer);
            byteBuffer.flip();
            return byteBuffer;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean login(String username, String password, String verifyCode, String cookie) {
        try {
            URL url = new URL(url_login);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Host", "218.200.239.185:8888");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv,33.0) Gecko/20100101 Firefox/33.0");
            conn.setRequestProperty("Referer", "http,//218.200.239.185:8888/portalserver/scuniondzkdqs.jsp");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Cookie", cookie);

            conn.setRequestMethod("POST");

            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(String.format("name=%s&pass=%s&psNum=%s", username, password, verifyCode).getBytes("Unicode"));
            os.flush();
            os.close();

            conn.connect();

            if (conn.getResponseCode() != 200)
                return false;

            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.indexOf("登陆成功") != -1) {
                    br.close();
                    return true;
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
