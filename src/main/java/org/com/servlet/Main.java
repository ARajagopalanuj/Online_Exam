package org.com.servlet;

import org.com.dao.ExamPortal;
import org.com.model.Pojo;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        Scanner scan=new Scanner(System.in);


        System.out.println("Enter the admin path enter 1");
        System.out.println();

        System.out.println("Enter the user path enter 2");
        System.out.println();

        System.out.println("any other key its exit");
        System.out.println("Enter your choice");
        int n=scan.nextInt();
        scan.nextLine();
        ExamPortal ep=new ExamPortal();




        try{

            System.out.println(ep.checkAuth("rajagopalan"));
            boolean checks=true;
            while(checks) {
                switch (n) {
                    case 1:

                        boolean check = true;

                        while (check) {
                            System.out.println("Enter 1 to add question");
                            System.out.println();

                            System.out.println("Enter 2 remove question ");
                            System.out.println();

                            System.out.println("Enter other key to exit...");
                            int n1 = scan.nextInt();

                            scan.nextLine();
                            switch (n1) {

                                case 1:
                                    String topic="";
                                    while (true) {
                                        System.out.println("Enter the topic");
                                        topic = scan.nextLine();
                                        if(!topic.equals("")){
                                            break;
                                        }

                                    }

                                    System.out.println("Enter the question");
                                    String qus = scan.nextLine();

                                    System.out.println("Enter the option 1");
                                    String op1 = scan.nextLine();

                                    System.out.println("Enter the option 2");
                                    String op2 = scan.nextLine();

                                    System.out.println("Enter the option 3");
                                    String op3 = scan.nextLine();

                                    System.out.println("Enter the option 4");
                                    String op4= scan.nextLine();


                                    String ans = "";
                                    while (true) {

                                        System.out.println("Enter the correct answer");
                                        ans = scan.nextLine();
                                        if (ans.equals(op1) || ans.equals(op2) || ans.equals(op3)||ans.equals(op4)) {
                                            break;

                                        } else {
                                            System.out.println("please Enter the correct Answer");
                                        }

                                    }

                                    if (ep.addData(qus, op1, op2, op3,op4, ans,topic) > 0) {
                                        System.out.println("question added succesfully");
                                    }

                                    break;

                                case 2:
                                    ArrayList<Pojo> list = ep.showQus();

                                    for (Pojo p : list) {
                                        System.out.print(p.getId());
                                        System.out.print(p.getQuestion());
                                        System.out.println();

                                    }

                                    System.out.println("Enter the question id");
                                    int id = scan.nextInt();
                                    scan.nextLine();

                                    if (ep.delQus(id)) {
                                        System.out.println("Deletion succesfully");
                                    }

                                    break;


                                default:
                                    check = false;
                                    System.out.println("Exit....");
                                    break;


                            }


                        }
                        break;
                    case 2:
                        System.out.println("Enter a new Student enter 1 \n or enter 0");

                        int chkvalue=scan.nextInt();
                        scan.nextLine();
                        String s=new String("java");

                        if(chkvalue==1)
                        {
                            System.out.println("Enter the user mail");
                            String username=scan.nextLine();
                            while(true) {

                                if (ep.checkUser(username)) {
                                    System.out.println("Enter your password");
                                    String password = scan.nextLine();


                                    if (ep.addData(username)) {
                                        System.out.println("User added succesfully");
                                    }
                                    break;
                                } else {
                                    System.out.println("username doeesn't exists");
                                }
                            }
                        }else{
                            System.out.println("Enter the username");
                            String username=scan.nextLine();


                            if(!ep.checkUser(username)){
                                System.out.println("Enter your password");
                                String password=scan.nextLine();

                                String temp=ep.checkAuth(username);
                                if(temp.equals(password)){
                                    System.out.println("Exam Starts");
                                    System.out.println("select the topic");
                                    String topic=scan.nextLine();
                                    //ep.writeExam(scan, username,topic);

                                }
                            }

                        }


                }
            }
        }catch(Exception e){
            System.out.println("Something happens " + e);

        }

    }
}
