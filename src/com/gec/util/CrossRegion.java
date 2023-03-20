package com.gec.util;

import javax.servlet.http.HttpServletResponse;

public class CrossRegion {

    public static void setCross(HttpServletResponse response) {
        String allowHeaders = "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With, Authorization,token";
        response.addHeader("Access-Control-Allow-Headers", allowHeaders);

        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Methods","*");
        response.setHeader("Access-Control-Max-Age","3600");
        response.setHeader("Access-Control-Max-Headers","*");
        response.setHeader("Access-Control-Allow-Credentials","true");
        response.setHeader("Access-Control-Expose-Headers", "token");

        response.setContentType("application/json");
    }
}
