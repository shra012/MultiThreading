package com.shra012.threads;

import lombok.extern.log4j.Log4j2;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Simple server example with socket.
 *
 * This class depends on log42 and lombok libraries.
 */
@Log4j2
public class Server {

    /**
     * Server request class with is a runnable and
     * acts as a static server which servers the resources
     * in the src/main/resources.
     */
    static class ServeRequests implements Runnable {
        private Socket socket;

        public ServeRequests(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                InputStream in = socket.getInputStream();
                byte[] request = new byte[1024];
                int length = in.read(request);
                if (length == -1) {
                    log.info("Received empty request.... skipping...");
                    return;
                }
                String requestString = new String(request, 0, length);
                HTTPServerRequest webRequestData = getWebRequestData(requestString);
                log.info("Received request {}", webRequestData);
                InputStream is = getClass().getClassLoader().getResourceAsStream(webRequestData.getRoute().replace("/", ""));
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeBytes("HTTP/1.1 200 OK\r\n");
                out.writeBytes("Content-Type: text/html\r\n\r\n");
                if (Objects.isNull(is)) {
                    out.writeBytes("<html><head></head><body><h4>Resource Not Found</h4></body></html>");
                } else {
                    out.write(is.readAllBytes());
                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Main method to start the socket server.
     *
     * @param args - empty args
     */
    @SuppressWarnings("java:S2189")
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            log.info("Waiting For New Clients...");
            while (true) {
                Socket sock = serverSocket.accept();
                log.info("Connected to a client...");
                executorService.execute(new ServeRequests(sock));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Builds the request into a class.
     *
     * @param request- String format of the http request
     * @return a {@link HTTPServerRequest HTTPServerRequest} class.
     */
    public static HTTPServerRequest getWebRequestData(String request) {
        String[] requestSplit = request.split("\n");
        List<String> requestParts = (List<String>) Arrays.asList(requestSplit);
        String first = requestParts.get(0);
        String[] firstParts = first.split(" ");
        String second = requestParts.get(1);
        String[] secondParts = second.split(":");

        return HTTPServerRequest.builder().method(firstParts[0])
                .route(firstParts[1]).httpVersion(firstParts[2].strip())
                .host(secondParts[1].trim()).build();
    }
}
