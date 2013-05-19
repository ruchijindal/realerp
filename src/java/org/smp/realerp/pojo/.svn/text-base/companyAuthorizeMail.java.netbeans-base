package org.smp.realerp.pojo;
/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

import java.util.Date;
import java.util.Properties;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.smp.realerp.entity.FbsCompany;
import org.smp.realerp.entity.FbsLogin;
import org.smp.realerp.managedbean.LoginBean;
import org.smp.realerp.manager.LoginManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author smp
 */
public class companyAuthorizeMail implements Runnable {

    @PersistenceContext(unitName = "RealErpPU")
    EntityManager em;
    @EJB
    LoginManager loginManager;
    String email = null;
    FbsCompany fbsCompany = new FbsCompany();
    FbsLogin fbsLogin = new FbsLogin();
    String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
    String xmlFile = path + "/resources/XML/mailSettings.xml";

    public companyAuthorizeMail(String email, FbsCompany fbsCompany, FbsLogin fbsLogin) {
        this.email = email;
        this.fbsCompany = fbsCompany;
        this.fbsLogin = fbsLogin;
    }

    @Override
    public void run() {
        try {
            String from = "";
            int port = 0;
            String user = "";
            String password = "";
            String host = "";
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document docxml = docBuilder.parse(xmlFile);
            docxml.getDocumentElement().normalize();
            NodeList p = docxml.getElementsByTagName("mail_detail");
            Node p1 = p.item(0);
            if (p1.getNodeType() == Node.ELEMENT_NODE) {

                org.w3c.dom.Element pathElement = (org.w3c.dom.Element) p1;

                NodeList path_list = pathElement.getElementsByTagName("from");
                Node rep_path = path_list.item(0);
                Element rep_path_element = (Element) rep_path;
                from = rep_path_element.getTextContent().trim();
                System.out.println("from==>" + from);

                path_list = pathElement.getElementsByTagName("port");
                rep_path = path_list.item(0);
                rep_path_element = (Element) rep_path;
                port = Integer.parseInt(rep_path_element.getTextContent().trim());
                System.out.println("port==>" + port);

                path_list = pathElement.getElementsByTagName("user");
                rep_path = path_list.item(0);
                rep_path_element = (Element) rep_path;
                user = rep_path_element.getTextContent().trim();
                System.out.println("user==>" + user);

                path_list = pathElement.getElementsByTagName("password");
                rep_path = path_list.item(0);
                rep_path_element = (Element) rep_path;
                password = rep_path_element.getTextContent().trim();
                System.out.println("password==>" + password);

                path_list = pathElement.getElementsByTagName("host");
                rep_path = path_list.item(0);
                rep_path_element = (Element) rep_path;
                host = rep_path_element.getTextContent().trim();
                System.out.println("host=>" + host);
            }


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
            msg.setSubject("Confirmation Mail From SMP Technolgies Pvt Ltd");
            msg.setSentDate(new Date());
            Multipart multipart = new MimeMultipart();

            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = "";
            if (fbsLogin.getCreatedBy() != null) {
                htmlText = "<b><u>Confirmation Details</u>:-</b><br/><br/>Company name - <b>" + this.fbsCompany.getCompanyName() + "</b><br/>User Id - <b>" + this.fbsLogin.getUserId() + "</b><br/>Password - <b>" + this.fbsLogin.getPassword() + "</b><br/>Your Account has been activated.   &nbsp;<br/><br/>This is the confirmation mail from SMP Technologies that your account has been activated to access Real Estate ERP";
            } else {
                htmlText = "<b><u>Confirmation Details</u>:-</b><br/><br/>Company name - <b>" + this.fbsCompany.getCompanyName() + "</b><br/>User Id - <b>" + this.fbsLogin.getUserId() + "</b><br/>Your Account has been activated.   &nbsp;<br/><br/>This is the confirmation mail from SMP Technologies that your account has been activated to access Real Estate ERP";

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
