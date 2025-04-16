package util;

import java.net.URL;

public class BuildUrl {

    public static String build(URL url) {
        StringBuilder result = new StringBuilder()
                .append(url.getProtocol())
                .append("://")
                .append(url.getHost());

        if (url.getPort() != -1) {
            result.append(":").append(url.getPort());
        }
        return result.toString().toLowerCase();
    }

}
