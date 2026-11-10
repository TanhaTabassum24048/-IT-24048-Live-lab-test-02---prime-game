package com.tanha.mathwhiz.dao;

import com.tanha.mathwhiz.model.GameScore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreDAO {

    private String envOrDefault(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? fallback : value;
    }

    private Connection getConnection() throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found!", e);
        }

        String url = envOrDefault(
                "MATHWHIZ_DB_URL",
                "jdbc:mysql://localhost:3306/math_whiz_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
        );

        String user = envOrDefault("MATHWHIZ_DB_USER", "root");

        String password = envOrDefault("MATHWHIZ_DB_PASSWORD", "");

        return DriverManager.getConnection(url, user, password);
    }

    // Save player's score
    public void saveScore(String playerName, int score, String difficulty)
            throws SQLException {

        String sql = "INSERT INTO game_scores " +
                "(player_name, score, difficulty, played_at) " +
                "VALUES (?, ?, ?, CURRENT_TIMESTAMP)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, playerName);
            statement.setInt(2, score);
            statement.setString(3, difficulty);

            statement.executeUpdate();
        }
    }

    // Retrieve Top 10 scores
    public List<GameScore> getTopScores() throws SQLException {

        String sql = "SELECT player_name, score, difficulty, played_at " +
                "FROM game_scores " +
                "ORDER BY score DESC, played_at ASC LIMIT 10";

        List<GameScore> scores = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                scores.add(new GameScore(
                        result.getString("player_name"),
                        result.getInt("score"),
                        result.getString("difficulty"),
                        result.getTimestamp("played_at")
                ));
            }
        }

        return scores;
    }
}