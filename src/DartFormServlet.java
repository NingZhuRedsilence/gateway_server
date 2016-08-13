import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by user on 7/2/16.
 * this is the view in McVVM
 */
public class DartFormServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException,
            IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<form method=\"get\">"); //action="action_page.php"
        response.getWriter().println("First name:<br>");
        response.getWriter().println("<input type=\"text\" name=\"firstname\" value=\"input\"><br>");
        response.getWriter().println("<input type=\"submit\" value=\"Submit\">");
        response.getWriter().println("</form>");

    }

//
//    Last name:<br>
//    <input type="text" name="lastname" value="Mouse"><br><br>
//    <input type="submit" value="Submit">
//    </form>
}
