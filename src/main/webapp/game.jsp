<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List,com.tanha.mathwhiz.model.Question" %>
<%
    String player = (String) session.getAttribute("playerName");
    List<Question> questions = (List<Question>) session.getAttribute("questions");
    if (player == null || questions == null) {
        response.sendRedirect(request.getContextPath() + "/");
        return;
    }
%>
<!DOCTYPE html><html lang="en"><head>
<meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Game | TANHA – MATH WHIZ</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head><body><main class="shell">
    <header class="topbar"><a href="${pageContext.request.contextPath}/" class="brand">TANHA – MATH WHIZ</a><span class="pill">NORMAL</span></header>
    <section class="page-heading"><div class="eyebrow">PLAYER: <%= player.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\\\"", "&quot;") %></div><h1>Let’s solve this!</h1><p>Answer all four questions. Trigonometry answers may be rounded to 2 decimal places.</p></section>
    <form action="${pageContext.request.contextPath}/app/submit" method="post" class="questions">
        <% for (int i = 0; i < questions.size(); i++) { Question q = questions.get(i); %>
        <article class="question-card">
            <div class="question-meta"><span>QUESTION 0<%= i + 1 %></span><strong><%= q.getMarks() %> MARKS</strong></div>
            <h2><%= q.getPrompt() %></h2>
            <label for="answer<%= i + 1 %>">Your answer</label>
            <input id="answer<%= i + 1 %>" name="answer<%= i + 1 %>" required
                   type="<%= "trig".equals(q.getType()) ? "number" : "number" %>"
                   step="<%= "trig".equals(q.getType()) ? "any" : "1" %>"
                   placeholder="<%= "trig".equals(q.getType()) ? "e.g. 0.50" : "Enter an integer" %>">
        </article>
        <% } %>
        <button type="submit" class="wide">Submit Answers <span>→</span></button>
    </form>
</main></body></html>
