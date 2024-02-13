package Servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;

@WebServlet(urlPatterns = "/attendance") //mapping
public class AttendanceServlet extends HttpServlet {
    //html och internal CSS
    String top = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "    <head>\n" +
            "        <title>Närvaro</title>\n" +
            "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=\"ISO-8859-1\">" +
            "<style>" +
            "body {" +
            "  background-image: linear-gradient(orange, white);\n" +
            "}" +
            "}" +

            "h1 {" +
            "text-align: center;" +
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

            "footer {" +
            "margin-top: 175px;" +
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
            "    <a href=http://localhost:9090/courses title=Courses> Courses</a>\n" +
            "    <a href=http://localhost:9090/newStudent title=NewStudent> New student</a>\n" +
            "    <a href=http://localhost:9090/newCourse title=NewCourse> add new Course</a>\n" +
            "</div>";


    String tableheader = "<table style=\"text-align:center;\">\n" +
            "  <tr>\n" +
            "   \t<th style=\"text-decoration: underline;\">Namn</th>\n" +
            "   \t<th style=\"text-decoration: underline;\">Kurs</th>\n" +
            "  </tr>\n";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println(top);
        out.print("        <h1 style=\"text-align: center;\">Lägg till student på en ny kurs</h1>" +
                "        <h3 style=\"text-align: center;\">Välj en student i listan</h3>");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3387/gritacademy2",
                    "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from students");
            while (rs.next()) {
                int studentId = rs.getInt("id");
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                System.out.println(fname);
                System.out.println(lname);
                    //tar fram studenterna på skolan
                out.println(
                        "<form style=text-align:center;\" action=\"/attendance\" method=\"POST\">" +
                                "<input type=\"radio\" name=\"fname\" id=\"fname\" value=\"" + fname + "\">\n" + "   " +
                                "<input type=\"radio\" name=\"studentId\" id=\"studentId\" value=\" " + studentId + "\">\n" +
                                "        <label for=\"fname\">" + fname + " " + lname + "</label><br>"
                );
            }

            out.println("<h3 style=\"text-align: center;\">Välj vilken kurs studenten ska ta</h3>");

            ResultSet rs2 = stmt.executeQuery("select * from courses");
            while (rs2.next()) {
                int courseId = rs2.getInt("id");
                String course = rs2.getString("name");
                System.out.println(course);
                    //tar fram kurserna man kan gå på skolan
                out.println(
                        "<form style=text-align:center;\" action=\"/attendance\" method=\"POST\">" +
                                "<input type=\"radio\" name=\"courseId\" id=\"courseId\" value=\" " + courseId + "\">\n" +
                                "<input type=\"radio\" name=\"name\" id=\"fname\" value=\"" + course + "\">\n" + "   " +
                                "        <label for=\"fname\">" + course + "</label><br>"
                );
            } //knapp för att skicka in all info om elev + kurser
            out.println("<input type=\"submit\" value=\"Submit\"><br>\n");
            out.println(
                    "    </body>\n" +
                            "<footer>\n" +
                            "  <p>Author: Andreas Jabrell</p>\n" +
                            "  <p><a href=\"http://localhost:9090/\" title=\"TILLBAKA TILL MAIN\"> TILLBAKA TILL MAIN</a></p>\n" +
                            "</footer>" +
                            "</html>");
            con.close();


        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String studentId = (req.getParameter("studentId"));
        String fname = req.getParameter("fname");
        String course = req.getParameter("name");
        String courseId = (req.getParameter("courseId"));
        PrintWriter out = resp.getWriter();
        out.println(top);
        out.println("        <h1 style=\"text-align: center;\">Tillagd</h1>" +
                "        <h3 style=\"text-align: center;\">Snyggt va?</h3>");
        System.out.println(studentId + " " + fname + " " + courseId + " " + course);


            //påbörjad kod för att visa http respons kod och sätta ut rätt info mot användaren
        URL url = new URL("http://google.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        int code = connection.getResponseCode();
        System.out.println("Response code of the object is " + code);
        if (code == 200) {
            System.out.println("OK");
        } else if (code == 500) {
            System.out.println("något gick fel");
            out.println(top);
            out.println("        <h1 style=\"text-align: center;\">NÅGOT GICK FEL, KLICKA TILLBAKA</h1>");
        }


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3387/gritacademy2",
                    "root", "");
            Statement stmt = con.createStatement();
                //hämtar in önskad info från användaren och sätter in i SQL sträng
            String sql = "INSERT INTO attendance (student_id, course_id) VALUES (" + studentId + ", " + courseId + ");";
            System.out.println(sql);
            int rs = stmt.executeUpdate(sql);
            if (rs > 0) {
                System.out.println("YEAHBABY");
            }
            //SQL för att visa all info om vilka kurser en specifik student går
            String sql2 = "SELECT s.fname, s.lname AS Student, c.name AS Course FROM attendance a LEFT JOIN " +
                    "students s ON s.id = a.student_id INNER JOIN courses c ON c.id = a.course_id WHERE s.fname " +
                    "= '" + fname + "'";
            System.out.println(sql2);
            ResultSet rs2 = stmt.executeQuery(sql2);
            System.out.println(sql);
            out.println(tableheader);
            while (rs2.next()) {

                fname = rs2.getString(1);
                String lname = rs2.getString(2);
                course = rs2.getString(3);
                //System.out.print("name: " + fname + " " + lname + ", course: " + course + ", Beskrivning: " + description + "\n");

                out.println("<tr>\n" +
                        "\t<th>" + fname + " " + lname + "</th>\n" +
                        "\t<th>" + course + "</th>\n" +
                        "  </tr>\n");
            }

            out.println("</table>" +
                    "<a style=text-align:center;\" href=\"http://localhost:9090/attendance\">\n" +
                    "    <button>Tillbaka</button>\n" +
                    "</a>" +
                    "    </body>\n" +
                    "<footer>\n" +
                    "  <p>Author: Andreas Jabrell</p>\n" +
                    "  <p><a href=\"http://localhost:9090/\" title=\"TILLBAKA TILL MAIN\"> TILLBAKA TILL MAIN</a></p>\n" +
                    "</footer>" +
                    "</html>");
            con.close();
        } catch (ClassNotFoundException | SQLException e) {
                out.println("<p>" + e.getMessage() + "</p>");
            //throw new RuntimeException(e);
        }
    }
            //tänkte lägga Http Respons som en egen metod att kalla på vid tillfälle. Just nu körs den direkt i koden
    private void checkHttpResponse() throws IOException {
        {

            URL url = new URL("http://google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();
            System.out.println("Response code of the object is " + code);
            if (code == 200) {
                System.out.println("OK");
            }
        }
    }
}
