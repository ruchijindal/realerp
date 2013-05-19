package org.smp.realerp.pojo;
/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.ejb.EJB;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.smp.realerp.entity.FbsMailsetting;
import org.smp.realerp.managedbean.LoginBean;
import org.smp.realerp.manager.LoginManager;

/**
 *
 * @author smp
 */
public class sendReplyMail implements Runnable {

    @PersistenceContext(unitName = "RealErpPU")
    EntityManager em;
    @EJB
    LoginManager loginManager;
    String email = null;
    String randomPassword = null;
    String userName = null;
//    String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
//    String xmlFile = path + "/resources/XML/mailSettings.xml";
    FbsMailsetting fbsMailsetting = new FbsMailsetting();

    public sendReplyMail(String email, String randomPassword, String userName) {
        this.email = email;
        this.randomPassword = randomPassword;
        this.userName = userName;
    }

    @Override
    public void run() {
        try {

            String from = "";
            int port = 0;
            String user = "";
            String password = "";
            String host = "";
            fbsMailsetting = ((List<FbsMailsetting>) LoginBean.fbsLogin.getFbsCompany().getFbsMailSettingCollection()).get(0);
            from = fbsMailsetting.getReplyId();
            port = fbsMailsetting.getPort();
            user = fbsMailsetting.getUser();
            password = fbsMailsetting.getPassword();
            host = fbsMailsetting.getHost();

            // Create properties, get Session
            Properties props = new Properties();
            // If using static Transport.send(),
            // need to specify which host to send it to
            props.put("mail.smtp.host", host);
            // props.put("mail.pop3.host","pop3.gmail.com");
            // To see what is going on behind the scene
            props.put("mail.debug", "true");
            props.put("mail.smtp.auth", "true");
            // props.put("mail.smtp.ssl.enable","true");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.host", host);
            Authenticator auth = new PasswordAuth();
            javax.mail.Session session1 = javax.mail.Session.getInstance(props, auth);
            // Instantiatee a message
            Message msg = new MimeMessage(session1);
            msg.setContent("Hello", "text/plain");
            //Set message attributes
            msg.setFrom(new InternetAddress(from, "RealErp"));
            Address address = new InternetAddress(this.email);
            msg.setRecipient(Message.RecipientType.TO, address);
            msg.setSubject("Login Details....");
            msg.setSentDate(new Date());
            Multipart multipart = new MimeMultipart();

            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = "<b><u>Login Details</u>:-</b><br/><br/>Your Username - <b>" + this.userName + "</b><br/>New Password   &nbsp;- <b>" + randomPassword + "</b><br/><br/>This is system generated password, please change your password from profile settings.";

            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);

            msg.setContent(multipart);

            //msg.setText("Your New Password is - " + randomPassword + " For UserName - " + this.userName);
            //Send the message
            // com.sun.mail.smtp.SMTPSSLTransport.send(msg);
            //SMTPSSLTransport transport=(SMTPSSLTransport)session1.getTransport("smtps");
            Transport transport = session1.getTransport("smtp");
            transport.connect(host, port, user, password);
            transport.send(msg);
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
