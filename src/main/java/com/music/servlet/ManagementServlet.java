
package com.music.servlet;

import com.music.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/manage")
public class ManagementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String type = request.getParameter("type");
        if (!"artists".equals(type)) {
            type = "customers";
        }

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        pageStart(out, "customers".equals(type)
                ? "Customer Management" : "Artist Management");

        out.println("<p><a href='management.html'>Back to Management Home</a></p>");
        out.println("<p><a href='manage?type=customers'>Customers</a> | "
                + "<a href='manage?type=artists'>Artists</a></p>");

        String message = request.getParameter("msg");
        if ("added".equals(message)) {
            out.println("<p>Record added successfully!</p>");
        } else if ("deleted".equals(message)) {
            out.println("<p>Record deleted successfully!</p>");
        }

        if ("customers".equals(type)) {
            showCustomerForm(out);
            listCustomers(out, response);
        } else {
            showArtistForm(out);
            listArtists(out, response);
        }

        pageEnd(out);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String type = request.getParameter("type");
        String action = request.getParameter("action");

        try (Connection conn = DBConnection.getConnection()) {

            if ("customers".equals(type)) {

                if ("add".equals(action)) {
                    String sql = "INSERT INTO customer "
                            + "(C_ID, First_Name, Last_Name, Birth_Date, "
                            + "Age, City, Gender, Email) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, Integer.parseInt(request.getParameter("id")));
                        ps.setString(2, request.getParameter("firstName"));
                        ps.setString(3, request.getParameter("lastName"));
                        ps.setDate(4, Date.valueOf(request.getParameter("birthDate")));
                        ps.setInt(5, Integer.parseInt(request.getParameter("age")));
                        ps.setString(6, request.getParameter("city"));
                        ps.setString(7, request.getParameter("gender"));
                        ps.setString(8, request.getParameter("email"));
                        ps.executeUpdate();
                    }

                } else if ("delete".equals(action)) {
                    String sql = "DELETE FROM customer WHERE C_ID = ?";

                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, Integer.parseInt(request.getParameter("id")));
                        int rows = ps.executeUpdate();

                        if (rows == 0) {
                            throw new SQLException("No customer found with that ID.");
                        }
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

            } else if ("artists".equals(type)) {

                if ("add".equals(action)) {
                    String sql = "INSERT INTO artist "
                            + "(A_ID, Name, Genre, City, Fee_Per_Show, Contact_Number) "
                            + "VALUES (?, ?, ?, ?, ?, ?)";

                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, Integer.parseInt(request.getParameter("id")));
                        ps.setString(2, request.getParameter("name"));
                        ps.setString(3, request.getParameter("genre"));
                        ps.setString(4, request.getParameter("city"));
                        ps.setBigDecimal(5, new java.math.BigDecimal(
                                request.getParameter("fee")));
                        ps.setString(6, request.getParameter("contact"));
                        ps.executeUpdate();
                    }

                } else if ("delete".equals(action)) {
                    String sql = "DELETE FROM artist WHERE A_ID = ?";

                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, Integer.parseInt(request.getParameter("id")));
                        int rows = ps.executeUpdate();

                        if (rows == 0) {
                            throw new SQLException("No artist found with that ID.");
                        }
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            response.sendRedirect("manage?type=" + type + "&msg="
                    + ("add".equals(action) ? "added" : "deleted"));

        } catch (SQLException | IllegalArgumentException e) {
            response.setContentType("text/html;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            pageStart(out, "Operation Failed");
            out.println("<p>The operation could not be completed. "
                    + "Check that the ID is unique, the fields are valid, "
                    + "and the record is not linked to another record.</p>");
            out.println("<p><a href='manage?type="
                    + ("artists".equals(type) ? "artists" : "customers")
                    + "'>Return to management</a></p>");
            pageEnd(out);
            getServletContext().log("Management operation failed", e);
        }
    }

    private void showCustomerForm(PrintWriter out) {
        out.println("<h2>Add Customer</h2>");
        out.println("<form method='post' action='manage?type=customers'>");
        out.println("<input type='hidden' name='type' value='customers'>");
        out.println("<input type='hidden' name='action' value='add'>");
        out.println("Customer ID: <input type='number' name='id' required><br>");
        out.println("First name: <input name='firstName' required><br>");
        out.println("Last name: <input name='lastName' required><br>");
        out.println("Birth date: <input type='date' name='birthDate' required><br>");
        out.println("Age: <input type='number' name='age' min='0' required><br>");
        out.println("City: <input name='city' required><br>");
        out.println("Gender: <input name='gender' required><br>");
        out.println("Email: <input type='email' name='email' required><br>");
        out.println("<button type='submit'>Add Customer</button></form>");

        out.println("<h2>Delete Customer</h2>");
        out.println("<form method='post' action='manage?type=customers'>");
        out.println("<input type='hidden' name='type' value='customers'>");
        out.println("<input type='hidden' name='action' value='delete'>");
        out.println("Customer ID: <input type='number' name='id' required>");
        out.println("<button type='submit'>Delete Customer</button></form>");
    }

    private void showArtistForm(PrintWriter out) {
        out.println("<h2>Add Artist</h2>");
        out.println("<form method='post' action='manage?type=artists'>");
        out.println("<input type='hidden' name='type' value='artists'>");
        out.println("<input type='hidden' name='action' value='add'>");
        out.println("Artist ID: <input type='number' name='id' required><br>");
        out.println("Name: <input name='name' required><br>");
        out.println("Genre: <input name='genre' required><br>");
        out.println("City: <input name='city' required><br>");
        out.println("Fee per show: <input type='number' name='fee' min='0' step='0.01' required><br>");
        out.println("Contact number: <input name='contact' required><br>");
        out.println("<button type='submit'>Add Artist</button></form>");

        out.println("<h2>Delete Artist</h2>");
        out.println("<form method='post' action='manage?type=artists'>");
        out.println("<input type='hidden' name='type' value='artists'>");
        out.println("<input type='hidden' name='action' value='delete'>");
        out.println("Artist ID: <input type='number' name='id' required>");
        out.println("<button type='submit'>Delete Artist</button></form>");
    }

    private void listCustomers(PrintWriter out, HttpServletResponse response)
            throws IOException {
        out.println("<h2>Existing Customers</h2>");
        out.println("<table border='1' cellpadding='6'><tr>"
                + "<th>ID</th><th>First Name</th><th>Last Name</th>"
                + "<th>Birth Date</th><th>Age</th><th>City</th>"
                + "<th>Gender</th><th>Email</th></tr>");

        String sql = "SELECT C_ID, First_Name, Last_Name, Birth_Date, "
                + "Age, City, Gender, Email FROM customer ORDER BY C_ID";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                out.println("<tr><td>" + rs.getInt("C_ID") + "</td><td>"
                        + esc(rs.getString("First_Name")) + "</td><td>"
                        + esc(rs.getString("Last_Name")) + "</td><td>"
                        + esc(String.valueOf(rs.getDate("Birth_Date"))) + "</td><td>"
                        + rs.getInt("Age") + "</td><td>"
                        + esc(rs.getString("City")) + "</td><td>"
                        + esc(rs.getString("Gender")) + "</td><td>"
                        + esc(rs.getString("Email")) + "</td></tr>");
            }

        } catch (SQLException e) {
            getServletContext().log("Could not list customers", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("<tr><td colspan='8'>Could not load customers. "
                    + "Check the Tomcat logs.</td></tr>");
        }

        out.println("</table>");
    }

    private void listArtists(PrintWriter out, HttpServletResponse response)
            throws IOException {
        out.println("<h2>Existing Artists</h2>");
        out.println("<table border='1' cellpadding='6'><tr>"
                + "<th>ID</th><th>Name</th><th>Genre</th><th>City</th>"
                + "<th>Fee per Show</th><th>Contact Number</th></tr>");

        String sql = "SELECT A_ID, Name, Genre, City, Fee_Per_Show, "
                + "Contact_Number FROM artist ORDER BY A_ID";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                out.println("<tr><td>" + rs.getInt("A_ID") + "</td><td>"
                        + esc(rs.getString("Name")) + "</td><td>"
                        + esc(rs.getString("Genre")) + "</td><td>"
                        + esc(rs.getString("City")) + "</td><td>"
                        + rs.getBigDecimal("Fee_Per_Show") + "</td><td>"
                        + esc(rs.getString("Contact_Number")) + "</td></tr>");
            }

        } catch (SQLException e) {
            getServletContext().log("Could not list artists", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("<tr><td colspan='6'>Could not load artists. "
                    + "Check the Tomcat logs.</td></tr>");
        }

        out.println("</table>");
    }

    private void pageStart(PrintWriter out, String title) {
        out.println("<!DOCTYPE html><html lang='en'><head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");
        out.println("<title>" + esc(title) + " - MusicVerse</title>");
        out.println("<style>body{font-family:Arial,sans-serif;max-width:1000px;"
                + "margin:30px auto;padding:0 16px}input{margin:6px;padding:6px}"
                + "button{margin:8px;padding:8px 12px;cursor:pointer}"
                + "table{border-collapse:collapse;width:100%;margin:16px 0}"
                + "th,td{padding:8px;text-align:left;overflow-wrap:anywhere}"
                + "th{background:#eee}form{margin-bottom:24px}</style>");
        out.println("</head><body><h1>MusicVerse</h1><h2>"
                + esc(title) + "</h2>");
    }

    private void pageEnd(PrintWriter out) {
        out.println("</body></html>");
    }

    private String esc(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
