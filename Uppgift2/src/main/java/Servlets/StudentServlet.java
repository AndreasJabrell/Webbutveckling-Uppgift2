package Servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


@WebServlet(urlPatterns = "/students") //mapping
public class StudentServlet extends HttpServlet {
    //basic html med internal och inline CSS
    String top = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "    <head>\n" +
            "        <title>Studenter på Grit Academy</title>\n" +
            "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=\"ISO-8859-1\">" +
            "<style>" +
            "body {" +
            "  background-image: linear-gradient(red, white);\n" +
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

            "form {" +
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
            "    <a href=http://localhost:9090/courses title=Courses> Courses</a>\n" +
            "    <a href=http://localhost:9090/attendance title=Attendance> Attendance</a>\n" +
            "    <a href=http://localhost:9090/newStudent title=NewStudent> New student</a>\n" +
            "    <a href=http://localhost:9090/newCourse title=NewCourse> add new Course</a>\n" +
            "</div>";

    String form =
            "<form style=text-align:center; action=\"/students\" method=\"POST\">\n" +
                    "    <label for=\"fname\">First name:</label><br>\n" +
                    "    <input type=\"text\" id=\"fname\" name=\"fname\" required><br>" +
                    "    <label for=\"lname\">Last name:</label><br>\n" +
                    "    <input type=\"text\" id=\"lname\" name=\"lname\" required><br><br>\n" +
                    "    <input type=\"submit\" value=\"Submit\">\n" +
                    "</form>";
    String tableHeader = "<table style=\"text-align:center;\">\n" +
            "  <tr>\n" +
            "   \t<th style=\"text-decoration: underline;\">Namn</th>\n" +
            "   \t<th style=\"text-decoration: underline;\">Kurs</th>\n" +
            "  </tr>\n";
    String bottom = "</table>" +
            "    </body>\n" +
            "       <footer>\n" +
            "        <p>Author: Andreas Jabrell</p>\n" +
            "        <p><a href=\"http://localhost:9090/\" title=\"TILLBAKA TILL MAIN\"> TILLBAKA TILL MAIN</a></p>\n" +
            "       </footer>" +
            "</html>";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            //skapar connection mot databasen
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3387/gritacademy2",
                    "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from students");
            PrintWriter out = resp.getWriter();

            out.println(top);
            out.println("<h1>Vänligen skriv in namn på student</h1>\";");
            out.println(form);
            out.println("<table style=\"text-align:center;\">\n" +
                    "  <tr>\n" +
                    "   \t<th style=\"text-decoration: underline;\">Elever</th>\n" +
                    "  </tr>\n");
            while (rs.next()) {
                String fname = rs.getString(2);
                String lname = rs.getString(3);

                out.println(
                        "<tr>\n" +
                                "\t<td>" + fname + " " + lname + "</td>\n" +
                                "  </tr>\n");
            }
            out.println(bottom);
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, NullPointerException {
        //hämtar in de inskickade variablerna
        String fname = req.getParameter("fname");
        String lname = req.getParameter("lname");
        PrintWriter out = resp.getWriter();
        out.println(top);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3387/gritacademy2",
                    "root", "");
            Statement stmt = con.createStatement();
            System.out.println(fname);
            System.out.println(lname);
            //SQL sträng för att få fram önskad info om vad för kursen den går
            String sql = "SELECT s.fname, s.lname AS \"Student\", c.name AS \"Course\", c.description AS \"Course description\" " +
                    "FROM attendance a LEFT JOIN students s ON s.id = a.student_id INNER JOIN courses c ON c.id = a.course_id " +
                    "WHERE s.fname = '" + fname + "'";
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println(sql);
            out.println("<h1>Här har du info som önskat</h1>\";");
            out.println(tableHeader);
            while (rs.next()) {
                fname = rs.getString(1);
                lname = rs.getString(2);
                String course = rs.getString(3);
                out.println("<tr>\n" +
                        "\t<th>" + fname + " " + lname + "</th>\n" +
                        "\t<th>" + course + "</th>\n" +
                        "  </tr>\n");
            }
                //denna för att få tillbaka grundinfo när man klickar på tillbakaknappen
            while (rs.next()) {
                fname = rs.getString(2);
                lname = rs.getString(3);
                out.println(
                        "<tr>\n" +
                                "\t<td>" + fname + " " + lname + "</td>\n" +
                                "  </tr>\n");
            }
            out.println(
                    "</table>" +
                            "<a style=text-align:center;\" href=\"http://localhost:9090/students\">\n" +
                            "    <button>Tillbaka</button>\n" +
                            "</a>" +
                            "    </body>\n" +
                            "<footer>\n" +
                            "  <p>Author: Andreas Jabrell</p>\n" +
                            "  <p><a href=\"http://localhost:9090/\" title=\"TILLBAKA TILL MAIN\"> TILLBAKA TILL MAIN</a></p>\n" +
                            "</footer>" +
                            "</html>");
            con.close();
            out.println(bottom);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}