package org.smp.realerp.pojo;
/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.ejb.EJB;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.smp.realerp.entity.FbsApplicant;
import org.smp.realerp.entity.FbsMailsetting;
import org.smp.realerp.managedbean.LoginBean;
import org.smp.realerp.manager.LoginManager;

/**
 *
 * @author smp
 */
public class SendAttachmentMail implements Runnable {

    @PersistenceContext(unitName = "RealErpPU")
    EntityManager em;
    @EJB
    LoginManager loginManager;
    FbsApplicant fbsApplicant = new FbsApplicant();
    String filename = null;
    FbsMailsetting fbsMailsetting = new FbsMailsetting();
    String attachmentType = null;

    public SendAttachmentMail(FbsApplicant fbsApplicant, String filename, String attachmentType) {
        this.fbsApplicant = fbsApplicant;
        this.filename = filename;
        this.attachmentType = attachmentType;
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
            Address address = new InternetAddress(fbsApplicant.getEmail());
            msg.setRecipient(Message.RecipientType.TO, address);
            msg.setSubject("Consumer Details....");
            msg.setSentDate(new Date());
            Multipart multipart = new MimeMultipart();

            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = "<b> Dear " + this.fbsApplicant.getApplicantName() + ",</b><br/><br/>We are sending you " + attachmentType + " documents as attachment .Please check the attachment";

            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
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
        } finally {
            File outputFile = new File(filename);
            outputFile.delete();
        }
    }
}
