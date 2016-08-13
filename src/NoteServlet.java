
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Created by user on 7/2/16.
 * this is the view in McVVM
 */
public class NoteServlet extends HttpServlet {
    //lifecycle of jetty servlet
    //thread protection
    //Jetty may use multiple instances of NoteServlet to serve multiple remote client calls
    private static String userInput = "";
    private static String responseFromServer = "";
    private static final Object userInput_lock = new Object();
    private static final Object responseFromServer_lock = new Object();
    private String computeServerURL = "http://sisyphus.cs.rice.edu/api/";
    private String testServerURL = "http://localhost:9090/splicing";
    public NoteServlet()
    {
        System.out.println("constructor run");
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException,
            IOException {

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        synchronized (userInput_lock) {
            //response.getWriter().println(userInput);

        }
        synchronized (responseFromServer_lock) {
            System.out.println("in Get" + responseFromServer);

            responseFromServer.replaceAll("<", "removed '<'");
            System.out.println("after replacing <" + responseFromServer);

            response.getWriter().println("this is answer from testServer" + responseFromServer);

        }

    }
    //an echo server. getparameter is only for form data
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("in post");
        //must understand every line of a foreign code snippet before putting it into my program
        synchronized (userInput_lock) {
            userInput = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
//            //echo userInput sent in from the web interface
//            response.getWriter().println(userInput);
            System.out.println("sent to pliny: " + userInput);
        }

        //send userInput to dummy/pliny server here, get back and send back
        //need doPost, just do get here to test

        String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
        String param1 = "value1";
        String param2 = "value2";
//      //build a query
//            String query = String.format("param1=%s&param2=%s",
//                    URLEncoder.encode(param1, charset),
//                    URLEncoder.encode(param2, charset));
        //todo ask yanxin what query should I use to reach hello in pliny scarla

        URLConnection connection = new URL(testServerURL).openConnection();
        connection.setDoOutput(true); // Triggers POST.
        connection.setRequestProperty("Accept-Charset", charset);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

        try (OutputStream output = connection.getOutputStream()) {
            synchronized (userInput_lock) {
                output.write(userInput.getBytes(charset));
            }
        }
//can't write output after reading input
        InputStream serverResponse = connection.getInputStream();

        try (Scanner scanner = new Scanner(serverResponse)) {
            //TODO use sync lock for responseBody
            //Todo get upper case response from the server
            String responseBody = scanner.useDelimiter("\\A").next();

            synchronized (responseFromServer_lock) {
                responseFromServer = responseBody;
                //send responseFromServer to Dart app
                response.getWriter().println(responseFromServer);
                System.out.println("received from Pliny: " + responseFromServer);
            }

        }

        //should encapsulate send-connection to pliny server as an object?
        //**** Matt: create a Java client library that support Pliny-specific request based on TA4 tool API structure
        //**** similar to Amazon java SDK
        //**** to write a library for most popular languages?
        //Todo: send email to yanxin/in charge of sisphus abt CORS; modified interface Dart; java client library pliny
        //Jetty only receive http request & deal with that
        //sending out request need another library, Java ships with such a library


    }
}
