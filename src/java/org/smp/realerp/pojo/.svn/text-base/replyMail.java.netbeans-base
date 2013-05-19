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
import org.smp.realerp.entity.FbsComplaint;
import org.smp.realerp.entity.FbsComplaintReply;
import org.smp.realerp.entity.FbsMailsetting;
import org.smp.realerp.managedbean.LoginBean;
import org.smp.realerp.manager.ComplaintManager;

/**
 *
 * @author smp
 */
public class replyMail implements Runnable {

    @PersistenceContext(unitName = "RealErpPU")
    EntityManager em;
    @EJB
    ComplaintManager complaintManager;
    String email = null;
    String name = null;
    boolean complaint = false, ack = false;
    // String subject,complaintDetail;
    // int complaintId;
    //String reply;
    FbsComplaint fbsComplaint;
    FbsComplaintReply fbsComplaintReply;
//    String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
//    String xmlFile = path + "/resources/XML/mailSettings.xml";
    FbsMailsetting fbsMailsetting = new FbsMailsetting();

    public replyMail(String email, String name) {
        this.email = email;
        this.name = name;
        complaint = false;
    }

//    public replyMail(String email, String name,String subject,String complaintDetail,String reply,int complaintId, boolean complain,boolean ack) {
//        System.out.println(email);
//        this.email = email;
//        this.name = name;
//        complaint = complain;
//       // this.subject=subject;
//       // this.ack=ack;
//       // this.reply=reply;
//       // this.complaintDetail=complaintDetail;
//        //this.complaintId=complaintId;
//    }
    public replyMail(FbsComplaint fbsComplaint, FbsComplaintReply fbsComplaintReply, boolean complain, boolean ack) {
        //throw new UnsupportedOperationException("Not yet implemented");
        this.fbsComplaint = fbsComplaint;
        this.fbsComplaintReply = fbsComplaintReply;
        this.ack = ack;
        complaint = complain;
        email = fbsComplaint.getEmail();
        name = fbsComplaint.getName();


    }

    public replyMail(FbsComplaint fbsComplaint, boolean complain, boolean ack) {
        email = fbsComplaint.getEmail();
        name = fbsComplaint.getName();
        this.fbsComplaint = fbsComplaint;
        complaint = complain;
        this.ack = ack;
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

            msg.setSentDate(new Date());
            Multipart multipart = new MimeMultipart();

            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = "";
            if (!complaint) {
                //password recovery
                msg.setSubject("Complaint Details....");
                htmlText = "<b><u>Login Details</u>:-</b><br/><br/>Your Username - <b>" + this.name + "</b><br/><br/>Your Complaint is resolved.";
            } else {
                if (ack) {
                    //complaint ACK
                    msg.setSubject("Acknowledgement For " + fbsComplaint.getSubject());
                    htmlText = "<p> Dear Applicant,</p>";
                    htmlText += "<br/> <p> Thank You for contacting us.<br/> We acknowledge that your complaint registered successfully.</p>";
                    htmlText += "<br/><br/>  You complaint id is  <b>" + fbsComplaint.getComplaintId() + "</b>.";
                    htmlText += "<p>Your comaplaint subject- <b> " + fbsComplaint.getSubject() + " </b> .</p>";
                    htmlText += "<p> <br/> <b>Complaint:-</b> " + fbsComplaint.getComplaint() + "</p>";
                    htmlText += "<br/><br/><p>Warm Regards<b><br/>" + LoginBean.fbsLogin.getFbsCompany().getCompanyName() + "</b></p>";
                    htmlText += "<br/><br/><p>If you have any query regarding this Please call Us at <b>" + LoginBean.fbsLogin.getFbsCompany().getTelNumber() + "</b>  or mail at <b>" + LoginBean.fbsLogin.getFbsCompany().getEmail() + "</b></p>";
                    htmlText += "<p>We will contact you ASAP.</p>";
                } else {
                    //complaint reply
                    // System.out.println("Repliedddddddddddd");
                    msg.setSubject("RE: " + fbsComplaint.getSubject() + " [ Complaint Id  " + fbsComplaint.getComplaintId() + " ]");
                    htmlText = "<p>" + fbsComplaintReply.getMessage() + "</p>";
                    htmlText += "<br/><br/><p> Complaint Status <b>" + fbsComplaintReply.getStatus() + "</b></p>";
                    htmlText += "<br/><br/><p>You can Call us at <b>" + LoginBean.fbsLogin.getFbsCompany().getMobile() + "</b> or mail us <b>" + LoginBean.fbsLogin.getFbsCompany().getEmail() + "</b></p>";
                }
            }

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
