package org.com.servlet;

import org.com.dao.ExamPortal;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

public class AptiAdminServlet extends HttpServlet
{
    ExamPortal ep=new ExamPortal();

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        JSONObject jsonResponse=new JSONObject();

        String result="";
        String path = request.getPathInfo();

        try {
            StringBuilder sb=new StringBuilder();
            String line;
            BufferedReader reader=request.getReader();
            while((line=reader.readLine())!=null){
                sb.append(line);
            }
            JSONObject jsonRequest=new JSONObject(sb.toString());
            reader.close();

            if(path.equals("/insert")) {
                String question=jsonRequest.getString("question");
                String option1=jsonRequest.getString("option1");
                String option2=jsonRequest.getString("option2");
                String option3=jsonRequest.getString("option3");
                String option4=jsonRequest.getString("option4");

                String answer=jsonRequest.getString("answer");
                String topic=jsonRequest.getString("topic");
                int num = ep.addData(question, option1, option2, option3,option4, answer,topic);
                if (num > 0) {
                    result = "Successfully added";
                } else {
                    result = "Failed to add";
                }
            }else if(path.equals("/login")){
                String user=jsonRequest.getString("user");
                String password=jsonRequest.getString("password");
                String checkpwd=ep.checkAuth(user);


                if(checkpwd.equals(password)){
                    if(ep.checkAdmin(user)){
                        result="Login Succesfully";

                    }else{
                        result="only admins can access";
                    }

                }else{
                    result="Register and retry";
                }


            }
        } catch (Exception e) {
            result = Arrays.toString(e.getStackTrace());
        }
        jsonResponse.put("success",true);
        jsonResponse.put("message",result);
        response.getWriter().write(jsonResponse.toString());

    }

}
