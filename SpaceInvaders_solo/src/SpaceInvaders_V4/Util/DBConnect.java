/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceInvaders_V4.Util;

import SpaceInvaders_V4.Users.User;
import java.sql.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;

public class DBConnect {

    //class server
    static final String DB_URL = "jdbc:mysql://209.129.8.4:3306/42029";
    static final String DB_USER = "42029";
    static final String DB_PASS = "42029csc18b";
//    static final String DB_URL = "jdbc:mysql://lovalhost:3306/invaders";
//    static final String DB_USER = "root";
//    static final String DB_PASS = "";

    /**
     * check for existing email
     *
     * @param email to check
     * @return number in rows including email
     */
    public static int checkEmail(String email) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int numRows = 0;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            stmt = conn.prepareStatement("SELECT count(*) FROM `spaceinvaders_entity_player` WHERE `email` = ?;");

            stmt.setString(1, email.trim());
            rs = stmt.executeQuery();

            while (rs.next()) {
                numRows = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnections(rs, stmt, conn);
        }

        return numRows;
    }

    /**
     * add user information to database
     *
     * @param fName User First Name
     * @param lName User Last Name
     * @param userName user name
     * @param email User email
     * @param pass User password
     * @return number of updated records
     */
    public static int register(String fName, String lName, String userName, String email, String pass) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int numRows = 0;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            stmt = conn.prepareStatement("INSERT INTO `spaceinvaders_entity_player` (`first_name`, `last_name`, `email`, `password`,`user_name`, date_created) VALUES(?, ?, ?, ?, ?, now());");

            stmt.setString(1, fName.trim());
            stmt.setString(2, lName.trim());
            stmt.setString(3, email.trim());
            stmt.setString(4, DigestUtils.sha1Hex(pass.trim()));
            stmt.setString(5, userName.trim());

            numRows = stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnections(rs, stmt, conn);
        }
        return numRows;
    }

    /**
     * check login credentials, login if match
     *
     * @param email User email
     * @param pass User password
     * @return return true if login valid
     */
    public static boolean login(String email, String pass) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int numRows = 0;
        int id = 1;
        String userName = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            stmt = conn.prepareStatement("SELECT `player_id`, `user_name` FROM `spaceinvaders_entity_player` WHERE `email` = ? AND `password` =  ?;");
            stmt.setString(1, email.trim());
            stmt.setString(2, DigestUtils.sha1Hex(pass.trim()));

            rs = stmt.executeQuery();

            while (rs.next()) {
                numRows++;
                id = rs.getInt("player_id");
                userName = rs.getString("user_name");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnections(rs, stmt, conn);
        }
        if (numRows == 1) {
            User.login(id, userName);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Submit score to database
     *
     * @param userID user playerID
     * @param score game score
     * @param kills number of kill this game
     * @param powerUps number of powerUps collected
     * @param deaths number of deaths this game
     */
    public static void submitScore(int userID, int score, int kills, int powerUps, int deaths) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int scoreID;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            stmt = conn.prepareStatement("INSERT INTO `spaceinvaders_entity_score` (score, kills, power_ups, deaths) VALUES (?,?,?,?);", Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, score);
            stmt.setInt(2, kills);
            stmt.setInt(3, powerUps);
            stmt.setInt(4, deaths);

            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            rs.next();
            scoreID = rs.getInt(1);

            stmt = conn.prepareStatement("INSERT INTO `spaceinvaders_xref_player_scores` (player_id, score_id) VALUES (?,?)");
            stmt.setInt(1, userID);
            stmt.setInt(2, scoreID);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } finally {
            closeConnections(rs, stmt, conn);
        }

    }

    /**
     * Get game state of player
     *
     * @param playerID the player identification number
     * @return hashmap containing player statistics int "games", int "score",
     * int "kills" , int "power_ups" , int "deaths" , int "max_score", int
     * "max_kills", int "max_power_ups", int "min_deaths", double "avg_score",
     * double "avg_kills", double "avg_power_ups", double "avg_deaths".
     */
    public static HashMap getPlayerStats(int playerID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap stats = new HashMap();

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            stmt = conn.prepareStatement("SELECT SUM( `spaceinvaders_entity_player`.`player_id` ) AS `games`, SUM( `spaceinvaders_entity_score`.`score` ) AS `score`, SUM( `spaceinvaders_entity_score`.`kills` ) AS `kills`, SUM( `spaceinvaders_entity_score`.`power_ups` ) AS `power_ups`, SUM( `spaceinvaders_entity_score`.`deaths` ) AS `deaths`, MAX( `spaceinvaders_entity_score`.`score` ) AS `max_score`, MAX( `spaceinvaders_entity_score`.`kills` ) AS `max_kills`, MAX( `spaceinvaders_entity_score`.`power_ups` ) AS `max_power_ups`, MIN( `spaceinvaders_entity_score`.`deaths` ) AS `min_deaths`, AVG( `spaceinvaders_entity_score`.`score` ) AS `avg_score`, AVG( `spaceinvaders_entity_score`.`kills` ) AS `avg_kills`, AVG( `spaceinvaders_entity_score`.`power_ups` ) AS `avg_power_ups`, AVG( `spaceinvaders_entity_score`.`deaths` ) AS `avg_deaths` FROM `42029`.`spaceinvaders_xref_player_scores` AS `spaceinvaders_xref_player_scores`, `42029`.`spaceinvaders_entity_score` AS `spaceinvaders_entity_score`, `42029`.`spaceinvaders_entity_player` AS `spaceinvaders_entity_player` WHERE `spaceinvaders_xref_player_scores`.`score_id` = `spaceinvaders_entity_score`.`score_id` AND `spaceinvaders_xref_player_scores`.`player_id` = `spaceinvaders_entity_player`.`player_id` AND `spaceinvaders_entity_player`.`player_id` = ?;");

            stmt.setInt(1, playerID);

            rs = stmt.executeQuery();

            while (rs.next()) {
                stats.put("games", rs.getDouble("games"));
                stats.put("score", rs.getDouble("score"));
                stats.put("kills", rs.getDouble("kills"));
                stats.put("power_ups", rs.getDouble("power_ups"));
                stats.put("deaths", rs.getDouble("deaths"));
                stats.put("max_score", rs.getInt("max_score"));
                stats.put("max_kills", rs.getInt("max_kills"));
                stats.put("max_power_ups", rs.getInt("max_power_ups"));
                stats.put("min_deaths", rs.getInt("min_deaths"));
                stats.put("avg_score", rs.getDouble("avg_score"));
                stats.put("avg_kills", rs.getDouble("avg_kills"));
                stats.put("avg_power_ups", rs.getDouble("avg_power_ups"));
                stats.put("avg_deaths", rs.getDouble("avg_deaths"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnections(rs, stmt, conn);
        }

        return stats;
    }

    /**
     * close database connection variables
     *
     * @param rs MySql ResultSet
     * @param stmt MySql Statement
     * @param conn MySql connection
     */
    private static void closeConnections(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
