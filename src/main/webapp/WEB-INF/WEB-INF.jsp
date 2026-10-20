
<%@ page import="java.util.List" %>
<%@ page import="com.music.model.Event" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    List<Event> events = (List<Event>) request.getAttribute("events");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Explore Events | MusicVerse</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/style.css">
    <style>
        .page { padding: 45px 8%; }
        .event-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
            gap: 22px;
            margin-top: 30px;
        }
        .event-card {
            padding: 25px;
            background: #1e1830;
            border: 1px solid #3b2d50;
            border-radius: 14px;
        }
        .event-card h2 { color: #d9a7ff; }
        .muted { color: #c0b8d1; line-height: 1.7; }
        .event-date { color: #e6c9ff; }
    </style>
</head>
<body>
    <nav class="navbar">
        <a class="logo" href="<%= request.getContextPath() %>/index.html">
            MusicVerse
        </a>
        <a class="nav-link" href="<%= request.getContextPath() %>/events">
            Refresh Events
        </a>
    </nav>

    <main class="page">
        <p class="eyebrow">LIVE. LOUD. UNFORGETTABLE.</p>
        <h1>Explore Events</h1>
        <p class="muted">Events loaded from your MySQL database.</p>

        <div class="event-grid">
        <% if (events != null && !events.isEmpty()) {
               for (Event event : events) { %>

            <article class="event-card">
                <p class="event-date"><%= event.getDate() %></p>
                <h2><%= event.getName() %></h2>
                <p class="muted"><%= event.getDescription() %></p>
                <p>📍 <%= event.getCity() %></p>
                <p class="muted">
                    <%= event.getStartTime() %> – <%= event.getEndTime() %>
                </p>
                <p class="muted">
                    Venue capacity: <%= event.getCapacity() %>
                </p>
            </article>

        <%     }
           } else { %>
            <p>No events found in the database.</p>
        <% } %>
        </div>
    </main>
</body>
</html>
