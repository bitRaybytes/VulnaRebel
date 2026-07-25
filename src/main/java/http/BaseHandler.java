package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.BaseHandlerException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Base implementation for all HTTP request handlers in VulnaRebel.
 * <p>
 * This class implements {@link HttpHandler} and provides a common
 * request dispatching mechanism for {@code GET} and {@code POST}
 * requests, as well as utility methods for reading resources,
 * parsing request bodies, and sending HTTP responses.
 * </p>
 * <p>
 * Challenge-specific handlers should extend this class and override
 * {@link #doGet(HttpExchange)} and/or {@link #doPost(HttpExchange)}
 * depending on the supported HTTP methods.
 * </p>
 */
public abstract class BaseHandler implements HttpHandler {

    private static final Logger LOGGER = Logger.getLogger(BaseHandler.class.getName());

    protected static final String TEXT_HTML = "text/html; charset=utf-8";
    protected static final String TEXT_PLAIN = "text/plain; charset=utf-8";

    /**
     * Dispatch of incoming HTTP requests to the corresponding handler
     * method based on its request method.
     * <p>
     *     Supported methods are:
     * </p>
     * <ul>
     *      <li>{@code GET} -> {@link #doGet(HttpExchange)}</li>
     *      <li>{@code POST} -> {@link #doPost(HttpExchange)}</li>
     * </ul>
     * <p>
     *      Unsupported methods receive a {@code 405 Method Not Allowed} response.<br>
     *      Any other result in a {@code 500 Internal Server Error} response.
     * </p>
     *
     * @param exchange the exchange containing the request from the
     *                 client and used to send the response
     * @throws IOException if an I/O error occurs while communicating with client
     */
    @Override
    public final void handle(HttpExchange exchange) throws IOException {
        try {
            if ("GET".equals(exchange.getRequestMethod())) {
                doGet(exchange);
            } else if ("POST".equals(exchange.getRequestMethod())) {
                doPost(exchange);
            } else {
                sendResponse(exchange, 405, TEXT_HTML,"Method Not Allowed");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Request handling failed.", e);
            sendResponse(exchange, 500, TEXT_HTML,"Internal Server Error");
        }
    }

    /**
     * Handles HTTP {@code GET} requests.
     * <p>
     *     The default implementation responds with
     *     {@code 405 Method Now Allowed}.
     *     Subclasses should override this method if
     *     they support {@code GET} requests.
     * </p>
     * @param exchange the current HTTP request and response exchange
     * @throws IOException if an I/O error occurs while sending the response
     */
    protected void doGet(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 405, TEXT_HTML,"Method Not Allowed");
    }

    /**
     * Handles HTTP {@code POST} requests.
     * <p>
     *     The default implementation responds with
     *     {@code 405 Method Now Allowed}.
     *     Subclasses should override this method if
     *     they support {@code POST} requests.
     * </p>
     * @param exchange the current HTTP request and response exchange
     * @throws IOException if an I/O error occurs while sending the response
     */
    protected void doPost(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 405, TEXT_HTML,"Method Not Allowed");
    }

    /**
     * Sends a HTTP whose body is already available as a {@link String}.
     *
     * @param exchange the current HTTP request and response exchange
     * @param statusCode on which status code to serve
     * @param contentType the type of content to serve
     * @param body the body to read the html file
     * @throws IOException if an error occurs
     */
    protected void sendResponse(HttpExchange exchange,
                                int statusCode,
                                String contentType,
                                String body) throws IOException {
        validateExchange(exchange);
        validateStatuscode(statusCode);
        validateContentType(contentType);
        validateBody(body);

        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    /**
     * Sends an HTTP response whose body is already available as
     * a byte array.
     * <p>
     *     Unlike the string-based overload, this method avoids an
     *     additional UTF-8 conversion and is intended for serving
     *     binary resources or pre-rendered content.
     * </p>
     *
     * @param exchange the current HTTP request and response exchange
     * @param statusCode the HTTP status code to send
     * @param contentType the response content type
     * @param body the response body
     * @throws IOException if an I/O error occurs while sending the response
     */
    protected void sendResponse(HttpExchange exchange,
                                int statusCode,
                                String contentType,
                                byte[] body) throws IOException {
        validateExchange(exchange);
        validateStatuscode(statusCode);
        validateContentType(contentType);
        validateBody(body);

        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(statusCode, body.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body);
        }
    }

    /**
     * Reads the complete request body from the given HTTP exchange.
     *
     * @param exchange the current HTTP request and response exchange
     * @return the request body as a UTF-8 encoded string
     * @throws IOException if an I/O error occurs while reading
     *                     the request body
     */
    protected String readBody(HttpExchange exchange) throws IOException {
        validateExchange(exchange);
        try (InputStream is = exchange.getRequestBody()){
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    /**
     * Reads a resource from the application's classpath.
     * <p>
     * The supplied path must be absolute within the classpath,
     * for example:
     * </p>
     *
     * <pre>{@code
     * /static/index.html
     * }</pre>
     *
     * @param classpathPath the absolute classpath resource path
     * @return the resource contents as a byte array
     * @throws IOException if an I/O error occurs while reading
     *                     the resource
     * @throws BaseHandlerException if the resource does not exist
     */
    protected byte[] readResource(String classpathPath) throws IOException {
        validateClassPath(classpathPath);
        try (InputStream is = BaseHandler.class.getResourceAsStream(classpathPath)) {
            if (is == null) {
                throw new BaseHandlerException(
                        getClass().getName() +
                                ": Resource not found: " + classpathPath);
            }
            return is.readAllBytes();
        }
    }

    /**
     * Parses an {@code application/x-www-form-urlencoded} request body
     * into key-value pairs.
     * <p>
     * Both keys and values are URL-decoded using UTF-8.
     * Malformed input that does not contain valid key-value pairs
     * results in a {@link BaseHandlerException}.
     * </p>
     *
     * @param body the URL-encoded request body
     * @return a map containing the decoded form parameters
     * @throws BaseHandlerException if the body is null, blank,
     *                              malformed, or contains no
     *                              valid key-value pairs
     */
    protected Map<String,String> parseUrlEncodedData(String body){
        validateBody(body);
        Map<String, String> form = new HashMap<>();

        String[] pairs = body.split("&");
        for (String pair: pairs){
            String[] parts = pair.split("=",2);

            // edge case for malformed pairs with no "="
            if (parts.length == 2){
                String key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
                String value = URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
                form.put(key,value);
            }else {
                throw new BaseHandlerException(BaseHandler.class.getName()
                        + ": There is no pair for: "+ Arrays.toString(parts));
            }
        }

        if (!form.isEmpty()) return form;

        throw new BaseHandlerException(BaseHandler.class.getName() + ": Map cannot be null. A body needs to be given.");
    }

    private void validateBody(String body) {
        if (body == null || body.isBlank()){
            throw new BaseHandlerException(
                    BaseHandler.class.getName() +
                            "Body cannot be empty: "+body);
        }
    }

    private void validateExchange(HttpExchange exchange){
        if (exchange == null){
            throw new BaseHandlerException(
                    getClass().getName() + ": Given HttpExchange cannot be null."
            );
        }
    }

    private void validateStatuscode(int statusCode){
        if (statusCode < 100 || statusCode > 599){
            throw new BaseHandlerException(
                    getClass().getName() +
                            ": Http Statuscode is not defined or out of range. Check Http Statuscode."
            );
        }
    }

    private void validateContentType(String contentType){
        if (contentType == null || contentType.isBlank()){
            throw new BaseHandlerException(
                    getClass().getName() +
                            ": String cannot be null or empty."
            );
        }
    }

    private void validateClassPath(String classpathPath){
        if (classpathPath == null || classpathPath.isBlank()) {
            throw new BaseHandlerException(
                    getClass().getName() +
                            ": Path to classpath cannot be null or empty."
            );
        }
    }

    private void validateBody(byte[] body){
        if (body == null){
            throw new BaseHandlerException(
                    getClass().getName() +
                            ": Body cannot be null."
            );
        }
    }
}
