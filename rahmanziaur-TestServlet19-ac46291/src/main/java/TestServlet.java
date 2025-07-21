import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("studentName");
        String email = request.getParameter("studentEmail");
        String action = request.getParameter("action");

        ServiceClass service = new ServiceClass();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head><style>");
        out.println("table { border-collapse: collapse; width: 50%; }");
        out.println("th, td { border: 1px solid #333; padding: 8px; text-align: left; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println("</style></head><body>");

        out.println("<h2>Action: " + action + "</h2>");

        switch (action) {
            case "Insert":
                if (service.insertDB(name, email)) {
                    out.println("<p>Inserted successfully.</p>");
                } else {
                    out.println("<p>Insertion failed.</p>");
                }
                break;

            case "View":
                List<String> students = service.viewDB();
                if (students.isEmpty()) {
                    out.println("<p>No students found.</p>");
                } else {
                    out.println("<table>");
                    out.println("<tr><th>Name</th><th>Email</th></tr>");
                    for (String student : students) {
                        String[] parts = student.split(", Email: ");
                        String studentName = parts[0].replace("Name: ", "");
                        String studentEmail = parts.length > 1 ? parts[1] : "";
                        out.println("<tr><td>" + studentName + "</td><td>" + studentEmail + "</td></tr>");
                    }
                    out.println("</table>");
                }
                break;

            case "Update":
                if (service.updateDB(name, email)) {
                    out.println("<p>Updated successfully.</p>");
                } else {
                    out.println("<p>Update failed. Name not found?</p>");
                }
                break;

            case "Delete":
                if (service.deleteDB(name)) {
                    out.println("<p>Deleted successfully.</p>");
                } else {
                    out.println("<p>Delete failed. Name not found?</p>");
                }
                break;

            default:
                out.println("<p>Unknown action.</p>");
        }

        out.println("</body></html>");
    }
}
