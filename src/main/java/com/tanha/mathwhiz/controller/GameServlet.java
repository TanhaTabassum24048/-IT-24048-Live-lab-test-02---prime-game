package com.tanha.mathwhiz.controller;

import com.tanha.mathwhiz.dao.ScoreDAO;
import com.tanha.mathwhiz.model.GameScore;
import com.tanha.mathwhiz.model.Question;
import com.tanha.mathwhiz.service.MathService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebServlet(name = "GameServlet", urlPatterns = {"/app/*"})
public class GameServlet extends HttpServlet {
    private final ScoreDAO scoreDAO = new ScoreDAO();
    private static final String DIFFICULTY = "NORMAL";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path == null || "/".equals(path) || "/home".equals(path)) {
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } else if ("/game".equals(path)) {
            request.getRequestDispatcher("/game.jsp").forward(request, response);
        } else if ("/leaderboard".equals(path)) {
            try {
                request.setAttribute("scores", scoreDAO.getTopScores());
                request.getRequestDispatcher("/leaderboard.jsp").forward(request, response);
            } catch (SQLException e) {
                throw new ServletException("Could not load leaderboard. Check database configuration.", e);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String path = request.getPathInfo();

        if ("/start".equals(path)) {
            String playerName = request.getParameter("playerName");
            if (playerName == null || playerName.trim().isEmpty() || playerName.trim().length() > 100) {
                request.setAttribute("error", "Please enter a name (maximum 100 characters).");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                return;
            }
            HttpSession session = request.getSession();
            session.setAttribute("playerName", playerName.trim());
            session.setAttribute("questions", createQuestions());
            response.sendRedirect(request.getContextPath() + "/app/game");
            return;
        }

        if ("/submit".equals(path)) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("questions") == null
                    || session.getAttribute("playerName") == null) {
                response.sendRedirect(request.getContextPath() + "/");
                return;
            }
            @SuppressWarnings("unchecked")
            List<Question> questions = (List<Question>) session.getAttribute("questions");
            int score = 0;
            int[] marks = {2, 2, 3, 3};
            boolean[] correct = new boolean[4];
            String[] answers = new String[4];

            for (int i = 0; i < 4; i++) {
                answers[i] = request.getParameter("answer" + (i + 1));
                correct[i] = checkAnswer(questions.get(i), answers[i]);
                if (correct[i]) score += marks[i];
            }

            String playerName = (String) session.getAttribute("playerName");
            try {
                scoreDAO.saveScore(playerName, score, DIFFICULTY);
            } catch (SQLException e) {
                request.setAttribute("databaseError",
                        "Your score was calculated, but it could not be saved. Check MySQL settings and try again.");
            }

            request.setAttribute("playerName", playerName);
            request.setAttribute("score", score);
            request.setAttribute("correct", correct);
            request.setAttribute("questions", questions);
            request.setAttribute("answers", answers);
            request.setAttribute("maxScore", 10);
            session.removeAttribute("questions");
            request.getRequestDispatcher("/result.jsp").forward(request, response);
            return;
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private List<Question> createQuestions() {
        Random random = new Random();
        int primeCandidate = 10 + random.nextInt(40);
        int a = 10 + random.nextInt(90);
        int b = 2 + random.nextInt(12);
        String[] operations = {"sin", "cos", "tan"};
        String op1 = operations[random.nextInt(operations.length)];
        String op2 = operations[random.nextInt(operations.length)];
        // Angles are selected to avoid undefined tangent values.
        int[] safeAngles = {0, 15, 30, 45, 60, 75, 90, 120, 135, 150, 180, 210, 225, 240, 270, 300, 315, 330};
        int angle1 = safeAngles[random.nextInt(safeAngles.length)];
        int angle2 = safeAngles[random.nextInt(safeAngles.length)];
        if ("tan".equals(op1) && angle1 % 180 == 90) angle1 = 45;
        if ("tan".equals(op2) && angle2 % 180 == 90) angle2 = 60;

        List<Question> list = new ArrayList<>();
        list.add(new Question("prime", "Is " + primeCandidate + " a prime number? Enter 1 for Yes or 0 for No.",
                2, primeCandidate, 0, "", 0));
        list.add(new Question("mod", "Calculate the remainder: " + a + " % " + b,
                2, a, b, "", 0));
        list.add(new Question("trig", "Calculate " + op1.toUpperCase(Locale.ROOT) + "(" + angle1 + "°).",
                3, 0, 0, op1, angle1));
        list.add(new Question("trig", "Calculate " + op2.toUpperCase(Locale.ROOT) + "(" + angle2 + "°).",
                3, 0, 0, op2, angle2));
        return list;
    }

    private boolean checkAnswer(Question question, String answer) {
        if (answer == null || answer.isBlank()) return false;
        try {
            switch (question.getType()) {
                case "prime":
                    return Integer.parseInt(answer.trim()) == MathService.isPrime(question.getFirst());
                case "mod":
                    return Integer.parseInt(answer.trim()) ==
                            MathService.calculateMod(question.getFirst(), question.getSecond());
                case "trig":
                    double submitted = Double.parseDouble(answer.trim());
                    double expected;
                    switch (question.getOperation()) {
                        case "sin": expected = MathService.calculateSin(question.getAngle()); break;
                        case "cos": expected = MathService.calculateCos(question.getAngle()); break;
                        case "tan": expected = MathService.calculateTan(question.getAngle()); break;
                        default: return false;
                    }
                    return MathService.nearlyEqual(submitted, expected);
                default:
                    return false;
            }
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
