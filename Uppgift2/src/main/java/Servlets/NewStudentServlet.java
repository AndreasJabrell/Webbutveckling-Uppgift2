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

@WebServlet(urlPatterns = "/newStudent") //mapping
public class NewStudentServlet extends HttpServlet {
    //basic html med internal och inline CSS
    String top = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "    <head>\n" +
            "        <title>Lägg till ny Student</title>\n" +
            "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=\"ISO-8859-1\">" +
            "<style>" +
            "body {" +
            "  background-image: linear-gradient(grey, white);\n" +
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
            "    <a href=http://localhost:9090/students title=Students> Students</a>\n" +
            "    <a href=http://localhost:9090/newCourse title=NewCourse> add new Course</a>\n" +
            "</div>" +
            "        <h1>Följ instruktionerna för att lägga till en ny student</h1>";

        //formulär för att lägga till student, required på allt som är NOT NULL
    String form =
            "<form style=text-align:center; action=\"/newStudent\" method=\"POST\">\n" +
                    "    <label for=\"fname\">Skriv in förnamn:</label><br>\n" +
                    "    <input type=\"text\" id=\"fname\" name=\"fname\" required><br>" +
                    "    <label for=\"lname\">Skriv in efternamn:</label><br>\n" +
                    "    <input type=\"text\" id=\"lname\" name=\"lname\" required><br>\n" +
                    "    <label for=\"town\">Vart bor studenten:</label><br>\n" +
                    "    <input type=\"text\" id=\"town\" name=\"town\" required><br>" +
                    "    <label for=\"hobby\">Skriv in Hobby:</label><br>\n" +
                    "    <input type=\"text\" id=\"hobby\" name=\"hobby\" required><br>" +
                    "    <input type=\"submit\" value=\"Submit\">\n" +
                    "</form>";
    String tableHeader = "<table style=\"text-align:center;\">\n" +
            "  <tr>\n" +
            "   \t<th style=\"text-decoration: underline;\">Namn</th>\n" +
            "   \t<th style=\"text-decoration: underline;\">Stad</th>\n" +
            "   \t<th style=\"text-decoration: underline;\">Hobby</th>\n" +
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
        //Skriver ut hemsidan med formulär
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3387/gritacademy2",
                    "root", "");
            PrintWriter out = resp.getWriter();
            out.println(top);
            out.println(form);
            out.println(bottom);
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, NullPointerException {
        //tar in alla variabler
        String fname = req.getParameter("fname");
        String lname = req.getParameter("lname");
        String town = req.getParameter("town");
        String hobby = req.getParameter("hobby");
        PrintWriter out = resp.getWriter();
        out.println(top);
            //börjar med en insert till databasen, lägg till student med hämtade värden
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3387/gritacademy2",
                    "root", "");
            Statement stmt = con.createStatement();
            String sql = "INSERT INTO students (fname, lname, town, hobby) VALUES ('" + fname + "', '" + lname + "', '" + town + "', '" + hobby + "')";
            System.out.println(sql);
            int rs = stmt.executeUpdate(sql);
            if (rs > 0) {
                System.out.println("YEAHBABY"); //jippii det funkade
            }
            String sql2 = "SELECT * FROM students";
            ResultSet rs2 = stmt.executeQuery(sql2);
            System.out.println(sql);
            out.println(tableHeader);
            while (rs2.next()) {
                    //hämtar ut den nya infon för att skriva ut alla studenter
                fname = rs2.getString(2);
                lname = rs2.getString(3);
                town = rs2.getString(4);
                hobby = rs2.getString(5);
                out.println("<tr>\n" +
                        "\t<th>" + fname + " " + lname + "</th>\n" +
                        "\t<th>" + town + "</th>\n" +
                        "\t<th>" + hobby + "</th>\n" +
                        "  </tr>\n");
            }
            out.println(
                    "</table>" +
                            "<a style=text-align:center;\" href=\"http://localhost:9090/newStudent\">\n" +
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

