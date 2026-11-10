<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en"><head>
<meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>TANHA – MATH WHIZ</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head><body><main class="shell hero">
    <div class="eyebrow">JAVA SERVLET • JDBC • MATHEMATICS</div>
    <h1>TANHA <span>– MATH WHIZ</span></h1>
    <p class="subtitle">A little logic, a little math, and a whole lot of brain power.</p>
    <section class="panel">
        <h2>Ready to play?</h2>
        <p>Enter your name and solve four randomly generated challenges.</p>
        <% if (request.getAttribute("error") != null) { %>
            <p class="alert"><%= request.getAttribute("error") %></p>
        <% } %>
        <form action="${pageContext.request.contextPath}/app/start" method="post">
            <label for="playerName">Player name</label>
            <input id="playerName" name="playerName" type="text" maxlength="100" required placeholder="Enter your name">
            <button type="submit">Start Game <span>→</span></button>
        </form>
    </section>
    <a class="text-link" href="${pageContext.request.contextPath}/app/leaderboard">View Top 10 Leaderboard</a>
    <p class="footnote">NORMAL difficulty · 4 questions · 10 marks</p>
</main></body></html>
