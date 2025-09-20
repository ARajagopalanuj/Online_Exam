package org.com.servlet;

import org.com.dao.ExamPortal;
import org.com.model.Pojo;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.com.security.HashPassword;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class AptiUserServlet extends HttpServlet {
    ExamPortal ep=new ExamPortal();
    private static String user;
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");



        JSONObject jsonResponse=new JSONObject();

        try{
            StringBuilder sb=new StringBuilder();
            BufferedReader reader= request.getReader();
            String line;
            while((line=reader.readLine())!=null){
                sb.append(line);
            }

            JSONObject jsonRequest=new JSONObject(sb.toString());
            reader.close();


            String path=request.getPathInfo();

            if("/register".equals(path)){
               user=jsonRequest.getString("user");
                boolean result=false;
                if (ep.checkUser(user)) {
                    result= ep.addData(user);
                }


                if(result){
                    jsonResponse.put("success", true);
                    jsonResponse.put("message","Succesfully added");
                }else{
                    jsonResponse.put("success",false);
                    jsonResponse.put("message","Failed to add");
                }

            }else if("/login".equals(path)){
                user=jsonRequest.getString("user");
                String password=jsonRequest.getString("password");
                if(!ep.checkUser(user)){
                    if(HashPassword.checkPassword(password,ep.checkAuth(user))){
                        jsonResponse.put("success",true);
                        jsonResponse.put("message","login succesfully");
                        List<Pojo> topics=ep.getTopics();
                        jsonResponse.put("topics",topics);
                    }else{
                        jsonResponse.put("success",false);
                        jsonResponse.put("message","login failed");

                    }

                }else{
                    jsonResponse.put("success",false);
                    jsonResponse.put("message","user not found");

                }


            }else if(path.equals("/writeExam")){
                JSONArray arr=jsonRequest.getJSONArray("questions");
                System.out.println("inside write Exam");
                    int marks=0;

                    for (int i=0;i<arr.length();i++) {



                        JSONObject temp=arr.getJSONObject(i);
                        int qid = temp.getInt("qid");
                        String answer=temp.getString("answer");
                        if(ep.submitAnswer(qid).equals(answer)){
                            ep.hisAdd(user,qid,answer,"correct Answer");
                            ++marks;
                        }else {
                            ep.hisAdd(user, qid, answer, "wrong Answer");
                        }
                    }
                    jsonResponse.put("success",true);
                    jsonResponse.put("message",marks);


            }else{
                jsonResponse.put("success",false);
                jsonResponse.put("message",path);
            }
        }catch(Exception e){
            jsonResponse.put("success",false);
            jsonResponse.put("error",e.getMessage());

        }

        PrintWriter out=response.getWriter();
        out.write(jsonResponse.toString());
        out.flush();
        out.close();
    }
    public void doGet(HttpServletRequest request,HttpServletResponse response){
        response.setContentType("application/json");


        String path=request.getPathInfo();
        JSONObject jsonResponse=new JSONObject();



        try {
            PrintWriter writer=response.getWriter();


            if (path.equals("/problemsonages")) {
                List<Pojo>list=ep.getQuestions("problems on ages");
                jsonResponse.put("success",true);
                jsonResponse.put("list",list);

            }else if(path.equals("/timedistancespeed")){
                List<Pojo>list=ep.getQuestions("TimeDistanceSpeed");
                jsonResponse.put("success",true);
                jsonResponse.put("list",list);
            }else if(path.equals(("/problemsontrain"))){
                List<Pojo>list=ep.getQuestions("problems on train");
                jsonResponse.put("success",true);
                jsonResponse.put("list",list);
            }else{
                jsonResponse.put("success",false);
                jsonResponse.put("message","path not found");
            }
            writer.print(jsonResponse.toString());
            writer.flush();
            writer.close();
        }catch(Exception e){
            jsonResponse.put("success",false);
            jsonResponse.put("error",e.getMessage());
        }

    }
    protected void doPut(HttpServletRequest request,HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        String path=request.getPathInfo();


        JSONObject jsonResponse=new JSONObject();

            StringBuilder sb=new StringBuilder();
            BufferedReader reader=request.getReader();
            String line;
            while((line=reader.readLine())!=null){
                sb.append(line);
            }
            JSONObject jsonRequest=new JSONObject(sb.toString());
            reader.close();
    try {
        if ("/changePassword".equals(path)) {
            String user = jsonRequest.getString("user");
            String oldPassword = jsonRequest.getString("oldPassword");
            String newPassword = jsonRequest.getString("newPassword");

            if (!ep.checkUser(user)) {
                if(ep.checkAuth(user).equals(oldPassword)){
                    if(ep.changePassword(user,newPassword)) {
                        jsonResponse.put("success", true);
                        jsonResponse.put("message", "password changes succesfully");
                    }else{
                        jsonResponse.put("success", false);
                        jsonResponse.put("message", "password changes failed");
                    }
                }else{
                    jsonResponse.put("success",false);
                    jsonResponse.put("message","incorrect password");
                }
            }else{
                jsonResponse.put("success",false);
                jsonResponse.put("message","user not found");
            }

        } else {
            jsonResponse.put("success",false);
            jsonResponse.put("message","path not found");

        }
    } catch (SQLException e) {
        jsonResponse.put("success",false);
        jsonResponse.put("message",e.getMessage());
    }
        PrintWriter out=response.getWriter();
        out.write(jsonResponse.toString());
        out.flush();

    }

}
