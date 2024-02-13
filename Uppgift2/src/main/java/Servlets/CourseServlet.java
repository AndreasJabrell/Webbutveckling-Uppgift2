package Servlets;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "/coursesservlet", urlPatterns = "/courses") //mapping
public class CourseServlet extends HttpServlet {
    String top = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "    <head>\n" +
            "        <title>Kurser på Grit Academy</title>\n" +
            "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=\"ISO-8859-1\">" +
            "<style>" +
            "body {" +
            "  background-image: linear-gradient(brown, white);\n" +
            "}" +

            "h1 {" +
            "text-align:center;" +
            "color: black;" +
            "}" +

            "table {" +
            "padding-top: 50px;" +
            "padding-right: 30px;" +
            "padding-bottom: 50px;" +
            "padding-left: 80px;" +
            "margin-left: auto;" +
            "margin-right: auto;" +
            "}" +

            "button {" +
            "margin-left: auto;" +
            "margin-right: auto;" +
            "}" +

            "footer {" +
            "margin-top: 280px;" +
            "border-style: dotted;\n" +
            "border-width: thick;" +
            "text-align:center;" +
            "}" +
            "}" +

            "</style>" +

            "</head>\n" +
            "    <body>\n" +
            "<div style=text-align:center;>\n" +
            "    <a href=http://localhost:9090/students title=Students> Students</a>\n" +
            "    <a href=http://localhost:9090/attendance title=Attendance> Attendance</a>\n" +
            "    <a href=http://localhost:9090/newStudent title=NewStudent> New student</a>\n" +
            "    <a href=http://localhost:9090/newCourse title=NewCourse> add new Course</a>\n" +
            "</div>" +
            "        <h1>Här har ni alla kurser</h1>";



    String tableHeader = "<table style=\"text-align:center;\">\n" +
            "  <tr>\n" +
            "   \t<th style=\"text-decoration: underline;\">Kursnamn</th>\n" +
            "   \t<th style=\"text-decoration: underline;\">YHP</th>\n" +
            "   \t<th style=\"text-decoration: underline;\">Beskrivning</th>\n" +
            "  </tr>\n";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //PORT and DbName should be changed

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3387/gritacademy2",
                    "root", "");
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("select * from courses");
            PrintWriter out = resp.getWriter();
            out.println(top);
            out.println("<p style=text-align:center;> Klicka på en kurs för att få veta mer</p>");
            while (rs.next()) {
                String course = rs.getString("name");
                out.println(
                        "<form style=text-align:center;\" action=\"/courses\" method=\"POST\">" +
                        "<input type=\"radio\" name=\"name\" id=\"name\" value=\""+ course + "\">\n" + "   " +
                                "        <label for=\"name\">" + course + "</label><br>"
                );
            }
            out.println("<input type=\"submit\" value=\"Submit\"><br>\n" +
                    "    </body>\n" +
                    "<footer>\n" +
                    "  <p>Author: Andreas Jabrell</p>\n" +
                    "  <p><a href=\"http://localhost:9090/\" title=\"TILLBAKA TILL MAIN\"> TILLBAKA TILL MAIN</a></p>\n" +
                    "</footer>" +
                    "</html>");
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }


    }

    //doPost ör vidarebefordrat från HomeServlet
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String course = req.getParameter("name");
        System.out.println(req.getParameter("name"));

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //PORT and DbName should be changed

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3387/gritacademy2",
                    "root", "");
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("select * from courses WHERE name = '" + course + "'");
            PrintWriter out = resp.getWriter();
            out.println(top);
            out.println(tableHeader);
            out.println();
            while (rs.next()) {
                //String course = rs.getString("name");
                //System.out.print("Kurs: " + course);


                String name = rs.getString(2);
                int yhp = rs.getInt(3);
                String description = rs.getString(4);
                //System.out.print(", First name: " + fname + ", Last name: " + lname + ", town: " + town + ", Hobby: " + hobby + "\n");

                out.println(
                        "<p style=text-align:center;> Här har du mer info om " + name + "</p>" +
                        "<tr>\n" +
                                "\t<td>" + name + "</td>\n" +
                                "\t<td>" + yhp + "</td>\n" +
                                "\t<td>" + description + "</td>\n" +
                                "  </tr>\n");
            }
            out.println("</table>");
            while (rs.next()) {
                course = rs.getString("name");
                out.println("<form style=text-align:center;\" action=\"/courses\" method=\"POST\">" +
                                "<input type=\"radio\" name=\"name\" id=\"name\" value=\""+ course + "\">\n" + "   " +
                                "        <label for=\"name\">" + course + "</label><br>");
            }


            out.println(                "<a style=text-align:center;\" href=\"http://localhost:9090/courses\">\n" +
                            "  <button>Tillbaka</button>\n" +
                            "</a>" +
                    "    </body>\n" +
                    "<footer>\n" +
                    "  <p>Author: Andreas Jabrell</p>\n" +
                    "  <p><a href=\"http://localhost:9090/\" title=\"TILLBAKA TILL MAIN\"> TILLBAKA TILL MAIN</a></p>\n" +
                    "</footer>" +
                    "</html>");
            con.close();

        /*System.out.println(req.getParameter("fname"));
        System.out.println(req.getParameter("lname"));
        resp.sendRedirect("/students");*/
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}