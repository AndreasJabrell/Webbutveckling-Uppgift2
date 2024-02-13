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

@WebServlet(urlPatterns = "/newCourse") //mapping
public class NewCourse extends HttpServlet {
    //html top med internal CSS
    String top = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "    <head>\n" +
            "        <title>Lägg till ny Kurs</title>\n" +
            "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=\"ISO-8859-1\">" +
            "<style>" +
            "body {" +
            "  background-image: linear-gradient(#FA93A7, white);\n" +
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
            "    <a href=http://localhost:9090/newStudent title=NewStudent> add new Student</a>\n" +
            "</div>";

    String form = //formulär för ny kurs
            "<form style=text-align:center; action=\"/newCourse\" method=\"POST\">\n" +
                    "    <label for=\"name\">Skriv in kursens namn:</label><br>\n" +
                    "    <input type=\"text\" id=\"name\" name=\"name\" required><br>" +
                    "    <label for=\"yhp\">Skriv in Yrkeshögskolepoäng:</label><br>\n" +
                    "    <input type=\"text\" id=\"yhp\" name=\"yhp\" required><br>\n" +
                    "    <label for=\"description\">Vad innehåller kursen?</label><br>\n" +
                    "    <input type=\"text\" id=\"description\" name=\"description\" required><br>" +

                    "    <input type=\"submit\" value=\"Submit\">\n" +
                    "</form>";

    //rubriker för tabellen som skrivs ut
    String tableHeader = "<table style=\"text-align:center;\">\n" +
            "  <tr>\n" +
            "   \t<th style=\"text-decoration: underline;\">Kurs</th>\n" +
            "   \t<th style=\"text-decoration: underline;\">YHP</th>\n" +
            "   \t<th style=\"text-decoration: underline;\">Beskrivning</th>\n" +
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
        //skapar connection och skriver ut startsidan
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3387/gritacademy2",
                    "root", "");
            PrintWriter out = resp.getWriter();
            out.println(top);
            out.println("<h1>Följ instruktionerna för att lägga till en ny kurs</h1>");
            out.println(form);
            out.println(bottom);
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, NullPointerException {
        //hämtar in variabler
        String name = req.getParameter("name");
        int yhp = Integer.parseInt(req.getParameter("yhp"));
        String description = req.getParameter("description");
        PrintWriter out = resp.getWriter();
        out.println(top);
        out.println("        <h1>Kolla, bara att ansöka nu</h1>");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3387/gritacademy2",
                    "root", "");
            Statement stmt = con.createStatement();
                //sätter in hämtade variabler i SQL sträng
            String sql = "INSERT INTO courses (name, yhp, description) VALUES ('" + name + "', '" + yhp + "', '" + description + "')";
            System.out.println(sql);
            int rs = stmt.executeUpdate(sql);
            if (rs > 0) {
                System.out.println("YEAHBABY"); //printas i konsollen om allt funkar
            }
            String sql2 = "SELECT * FROM courses"; //hämtar fram och visar all aktuell info, inklusive ny kurs
            ResultSet rs2 = stmt.executeQuery(sql2);
            System.out.println(sql);
            out.println(tableHeader);
            while (rs2.next()) {

                name = rs2.getString(2);
                yhp = rs2.getInt(3);
                description = rs2.getString(4);
                out.println("<tr>\n" +
                        "\t<th>" + name + "</th>\n" +
                        "\t<th>" + yhp + "</th>\n" +
                        "\t<th>" + description + "</th>\n" +
                        "  </tr>\n");
            }

            out.println(
                    "</table>" +
                            //skapar en knapp för att komma tillbaka till start
                            "<a style=text-align:center;\" href=\"http://localhost:9090/newCourse\">\n" +
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

