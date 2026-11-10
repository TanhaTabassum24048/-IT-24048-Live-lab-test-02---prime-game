<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List,com.tanha.mathwhiz.model.GameScore" %>
<%
    List<GameScore> scores = (List<GameScore>) request.getAttribute("scores");
%>
<!DOCTYPE html><html lang="en"><head>
<meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Leaderboard | TANHA – MATH WHIZ</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head><body><main class="shell">
    <header class="topbar"><a href="${pageContext.request.contextPath}/" class="brand">TANHA – MATH WHIZ</a><span class="pill">TOP 10</span></header>
    <section class="page-heading"><div class="eyebrow">THE HALL OF FAME</div><h1>Leaderboard</h1><p>Highest scores appear first. Earlier attempts rank higher when scores are tied.</p></section>
    <section class="panel table-wrap">
        <table><thead><tr><th>#</th><th>Player</th><th>Score</th><th>Difficulty</th><th>Played at</th></tr></thead><tbody>
        <% if (scores == null || scores.isEmpty()) { %><tr><td colspan="5" class="empty">No scores yet. Be the first to play!</td></tr>
        <% } else { int rank = 1; for (GameScore s : scores) { %>
        <tr><td><%= rank++ %></td><td><%= s.getPlayerName().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\\\"", "&quot;") %></td>
        <td><strong><%= s.getScore() %>/10</strong></td><td><%= s.getDifficulty() %></td><td><%= s.getPlayedAt() %></td></tr>
        <% } } %>
        </tbody></table>
    </section>
    <a class="button" href="${pageContext.request.contextPath}/">Back to Home</a>
</main></body></html>
