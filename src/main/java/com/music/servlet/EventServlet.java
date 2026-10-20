
package com.music.servlet;

import com.music.model.Event;
import com.music.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/events")
public class EventServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        List<Event> events = new ArrayList<>();

        String sql = "SELECT E_ID, Name, Description, Date, " +
                     "S_Time, E_Time, Capacity, City FROM event";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                events.add(new Event(
                    rs.getInt("E_ID"),
                    rs.getString("Name"),
                    rs.getString("Description"),
                    rs.getDate("Date"),
                    rs.getTime("S_Time"),
                    rs.getTime("E_Time"),
                    rs.getInt("Capacity"),
                    rs.getString("City")
                ));
            }

        } catch (SQLException e) {
            throw new ServletException(
                "Unable to retrieve events from the database.", e);
        }

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' " +
                        "content='width=device-width, initial-scale=1.0'>");
            out.println("<title>MusicVerse | Events</title>");

            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; " +
                        "background: #10101a; color: #f5f5f5; " +
                        "margin: 0; padding: 30px; }");
            out.println("h1 { color: #d8a7ff; }");
            out.println(".events { display: grid; " +
                        "grid-template-columns: repeat(auto-fit, " +
                        "minmax(260px, 1fr)); gap: 20px; }");
            out.println(".card { background: #202033; padding: 20px; " +
                        "border-radius: 12px; }");
            out.println(".card h2 { color: #d8a7ff; }");
            out.println(".muted { color: #c4c4d4; }");
            out.println("a { color: #d8a7ff; }");
            out.println("</style>");

            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Upcoming Music Events</h1>");
            out.println("<p><a href='index.html'>&larr; Home</a></p>");

            if (events.isEmpty()) {
                out.println("<p>No events found in the database.</p>");
            } else {
                out.println("<div class='events'>");

                for (Event event : events) {
                    out.println("<article class='card'>");
                    out.println("<h2>" + escapeHtml(event.getName())
                                + "</h2>");
                    out.println("<p>" +
                                escapeHtml(event.getDescription()) +
                                "</p>");
                    out.println("<p class='muted'>Date: " +
                                event.getDate() + "</p>");
                    out.println("<p class='muted'>Start: " +
                                event.getStartTime() + "</p>");
                    out.println("<p class='muted'>End: " +
                                event.getEndTime() + "</p>");
                    out.println("<p class='muted'>City: " +
                                escapeHtml(event.getCity()) + "</p>");
                    out.println("<p class='muted'>Capacity: " +
                                event.getCapacity() + "</p>");
                    out.println("</article>");
                }

                out.println("</div>");
            }

            out.println("</body>");
            out.println("</html>");
        }
    }

    private String escapeHtml(String value) {
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
