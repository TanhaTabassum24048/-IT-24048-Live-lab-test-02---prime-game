<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List,com.tanha.mathwhiz.model.Question" %>
<%
    Boolean[] unused = null;
    boolean[] correct = (boolean[]) request.getAttribute("correct");
    List<Question> questions = (List<Question>) request.getAttribute("questions");
    String[] answers = (String[]) request.getAttribute("answers");
    if (correct == null || questions == null) { response.sendRedirect(request.getContextPath() + "/"); return; }
%>
<!DOCTYPE html><html lang="en"><head>
<meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Your Result | TANHA – MATH WHIZ</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head><body><main class="shell">
    <header class="topbar"><a href="${pageContext.request.contextPath}/" class="brand">TANHA – MATH WHIZ</a><span class="pill">RESULT</span></header>
    <section class="result-hero">
        <div class="eyebrow">GAME COMPLETED</div><h1>Well played, <%= String.valueOf(request.getAttribute("playerName")).replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;") %>!</h1>
        <div class="score"><strong><%= request.getAttribute("score") %></strong><span>/ 10</span></div>
        <p>You earned <%= request.getAttribute("score") %> out of 10 marks.</p>
    </section>
    <% if (request.getAttribute("databaseError") != null) { %><p class="alert"><%= request.getAttribute("databaseError") %></p><% } %>
    <section class="panel"><h2>Answer review</h2>
        <% for (int i = 0; i < questions.size(); i++) { %>
        <div class="review-row">
            <div><strong>Q<%= i + 1 %> · <%= questions.get(i).getMarks() %> marks</strong><p><%= questions.get(i).getPrompt() %></p>
            <small>Your answer: <%= (answers[i] == null || answers[i].isBlank()) ? "Not answered" : answers[i] %></small></div>
            <span class="<%= correct[i] ? "status-good" : "status-bad" %>"><%= correct[i] ? "Correct" : "Incorrect" %></span>
        </div>
        <% } %>
    </section>
    <div class="actions"><a class="button" href="${pageContext.request.contextPath}/">Play Again</a><a class="button secondary" href="${pageContext.request.contextPath}/app/leaderboard">Leaderboard</a></div>
</main></body></html>
