package org.com.security;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.security.SecureRandom;
import java.util.Properties;
import java.util.Random;

public class UserCreater {
    private static final String DIGITS = "0123456789";
    private static final SecureRandom random = new SecureRandom();

    static final String SENDER ="a.rajagopalan415@gmail.com";

    static final String APPPWD ="hzcn ugoa mnrv mfrt";
    private static Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");


        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER, APPPWD);
            }
        });
    }




    public static String createUser(String user){


        String password=generatePassword(8);

        String subject="Your mcq exam port login credentials";

        String body="Hi, \n\n Your account has been created in ZSGS mcq. \n\n"+"Username: "+ user +"\n" +"Password: "+password+"\n\n"+"Have a nice day\n\n"+"Regards,\n HR Admin";


        try{
            Session session=getSession();
            Message message=new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER));

            message.setRecipients(
                    Message.RecipientType.TO,InternetAddress.parse(user)
            );
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


        return password;
    }
    public static String generatePassword(int len){

        String chars="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890!@#$%^&*()_+{}";

        StringBuilder sb=new StringBuilder();
        Random random=new SecureRandom();

        for(int i=0;i<len;i++){
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
    public static int sendOtp(String user) throws MessagingException {

        String subject="ZSGS OTP";
        int OTP=generateOTP();

        String body="Hello "+user+" user this code to get new password";


            Session session = getSession();
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(user));

            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);

            return OTP;


    }
    public static int generateOTP(){
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<7;i++){
            sb.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        }
    return Integer.parseInt(sb.toString());
    }
}
