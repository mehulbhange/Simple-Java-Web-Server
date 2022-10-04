package com.bridgelabz;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.net.URLDecoder;
import java.util.*;

public class Handlers {
    public static class RootHandler implements HttpHandler{

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response = "<h1>Server start sucess if you see this message</h1>"+"<h1> Port : "+ SimpleHttpServer.port + "</h1>";
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    public static class EchoHeaderHandler implements HttpHandler{

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            Headers headers = httpExchange.getRequestHeaders();
            Set<Map.Entry<String, List<String>>> entries = headers.entrySet();
            String response = "";
            for (Map.Entry<String, List<String>> entry : entries){
                response += entry.toString() + "\n";
            }
            httpExchange.sendResponseHeaders(200,response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    public static class EchoGetHandler implements HttpHandler{

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            Map<String, Object> parameters = new HashMap<>();
            URI requestedUri = httpExchange.getRequestURI();
            String query = requestedUri.getRawQuery();
            parseQuery(query, parameters);
            String response = "";
            for (String key : parameters.keySet())
                response += key + " = " + parameters.get(key) + "\n";

            httpExchange.sendResponseHeaders(200,response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    public static class EchoPostHandler implements HttpHandler{

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            Map<String, Object> parameters = new HashMap<>();
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String query = br.readLine();
            parseQuery(query, parameters);
            String response = "";
            for (String key : parameters.keySet())
                response += key + " = "+ parameters.get(key)+"\n";
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    private static void parseQuery(String query, Map<String, Object> parameters) throws UnsupportedEncodingException{
        if (query != null){
            String[] pairs = query.split("[&]");
            for (String pair : pairs){
                String[] param = pair.split("[=]");
                String key = null;
                String value = null;
                if (param.length > 0)
                    key = URLDecoder.decode(param[0], System.getProperty("file.encoding"));

                if (param.length > 1)
                    value = URLDecoder.decode(param[1], System.getProperty("file.encoding"));

                parameters.put(key, value);
            }
        }
    }
}
