package org.com.dao;

import org.com.model.Pojo;
import org.com.security.HashPassword;
import org.com.security.UserCreater;
import org.com.util.DBConnection;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ExamPortal {
    static UserCreater uc = new UserCreater();


    //polymorphism
    public static int addData(String qus, String op1, String op2, String op3, String op4, String ans, String topic) throws SQLException {

        String query = "insert into mcq(question,option1,option2,option3,option4,answer,topic) values(?,?,?,?,?,?,?)";

        try (Connection con = DBConnection.getDbConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {

            preparedStatement.setString(1, qus);
            preparedStatement.setString(2, op1);
            preparedStatement.setString(3, op2);
            preparedStatement.setString(4, op3);
            preparedStatement.setString(5, op4);
            preparedStatement.setString(6, ans);
            preparedStatement.setString(7, topic);

            return preparedStatement.executeUpdate();

        }

    }

    public static ArrayList<Pojo> showQus() throws SQLException {
        ArrayList<Pojo> list = new ArrayList<>();
        String query = "select q_id,question from mcq";

        try (Connection con = DBConnection.getDbConnection();
             Statement statement = con.createStatement()) {
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("q_id");
                String qus = rs.getString("question");
                Pojo pojo = new Pojo(id, qus);
                list.add(pojo);

            }
            return list;
        }

    }

    public static boolean delQus(int id) throws SQLException {

        String query = "delete from mcq where q_id=?";
        try (Connection con = DBConnection.getDbConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        }
    }

    public static boolean checkUser(String uname) throws SQLException {

        String query = "select username from users";
        try (Connection con = DBConnection.getDbConnection();
             Statement statement = con.createStatement()) {

            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                if (rs.getString("username").equals(uname)) {
                    return false;
                }
            }
            return true;

        }

    }

    //polymorphism
    public static boolean addData(String user) throws SQLException {

        String query = "insert into users(username,password) values (?,?)";

        try (Connection con = DBConnection.getDbConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {

            preparedStatement.setString(1, user);
            String pass = HashPassword.hashPassword(UserCreater.createUser(user));
            preparedStatement.setString(2, pass);

            return preparedStatement.executeUpdate() > 0;
        }
    }

    public String checkAuth(String user) throws SQLException {
        String query = "select password from users where username=?";

        try (Connection con = DBConnection.getDbConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {

            preparedStatement.setString(1, user);
            ResultSet rs = preparedStatement.executeQuery();


            if (rs.next()) {
                return rs.getString("password");
            }
            return "";
        }
    }

    /*public static void writeExam(Scanner scan, String userName,String topic) throws SQLException {
        String query="select * from mcq where topic=?";
        try(Connection con=DBConnection.getDbConnection();
            PreparedStatement preparedStatement=con.prepareStatement(query)) {
            int mark = 0;

            preparedStatement.setString(1,topic);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("question"));
                System.out.println("a  " + rs.getString("option1"));
                System.out.println("b  " + rs.getString("option2"));
                System.out.println("c  " + rs.getString("option3"));
                System.out.println("d  " + rs.getString("option4"));

                System.out.println();
                System.out.println("Enter your answer");
                String answer = rs.getString("answer");
                String ans = scan.nextLine();


                if (ans.equals(answer)) {
                    System.out.println("current mark " + ++mark);
                    System.out.println("The answer is " + answer);
                    hisAdd(userName,rs.getInt("q_id"), ans, "correct answer");
                }else{
                    System.out.println("current mark " + mark);
                    System.out.println("The answer is " + answer);
                    hisAdd(userName,rs.getInt("q_id"), ans, "wrong answer");

                }


            }
        }
    }*/
    public static void hisAdd(String userName, int q_id, String answer, String status) throws SQLException {
        String query = "insert into history (username, qid, answer, status) values(?,?,?,?)";
        try (Connection con = DBConnection.getDbConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {

            preparedStatement.setString(1, userName);
            preparedStatement.setInt(2, q_id);
            preparedStatement.setString(3, answer);
            preparedStatement.setString(4, status);

            preparedStatement.executeUpdate();


        }
    }

    public List<Pojo> getTopics() throws SQLException {
        String query = "select distinct topic from mcq";
        List<Pojo> list = new ArrayList<>();

        try (Connection con = DBConnection.getDbConnection();
             Statement statement = con.createStatement()) {

            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                String topic = rs.getString("topic");
                Pojo pojo = new Pojo(topic);
                list.add(pojo);

            }
            return list;

        }

    }

    public List<Pojo> getQuestions(String topic) throws SQLException {
        String query = "Select qid,question,option1,option2,option3,option4,option5 from mcq where topic=?";

        try (Connection con = DBConnection.getDbConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            List<Pojo> list = new ArrayList<>();

            preparedStatement.setString(1, topic);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("qid");
                String question = rs.getString("question");
                String option1 = rs.getString("option1");
                String option2 = rs.getString("option2");
                String option3 = rs.getString("option3");
                String option4 = rs.getString("option4");
                String option5 = rs.getString("option5");
                Pojo pojo = new Pojo(id, question, option1, option2, option3, option4, option5);
                list.add(pojo);

            }
            return list;
        }

    }

    public boolean checkAdmin(String user) throws SQLException {
        String query = "select username from users where type='admin' and username=?";

        try (Connection con = DBConnection.getDbConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {

            preparedStatement.setString(1, user);

            ResultSet rs = preparedStatement.executeQuery();

            return rs.next();

        }

    }

    public boolean changePassword(String user, String password) throws SQLException {
        String query = "update users set password=? where username=? ";
        try (Connection con = DBConnection.getDbConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {

            preparedStatement.setString(1, password);
            preparedStatement.setString(2, user);


            return preparedStatement.executeUpdate() > 0;

        }
    }

    public String submitAnswer(int qid) throws SQLException {
        String query = "select answer from mcq where qid=?";

        try (Connection con = DBConnection.getDbConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, qid);
            ResultSet rs = preparedStatement.executeQuery();
            String answer = "qid is invalid";
            if (rs.next()) {
                answer = rs.getString("answer");
            }
            return answer;
        }
    }

}