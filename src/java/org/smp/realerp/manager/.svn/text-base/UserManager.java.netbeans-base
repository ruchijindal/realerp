/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.manager;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.primefaces.model.TreeNode;
import org.smp.realerp.entity.FbsLogin;
import org.smp.realerp.entity.FbsUser;
import org.smp.realerp.session.FbsLoginFacade;
import org.smp.realerp.session.FbsUserFacade;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author smp
 */
@Stateless
@LocalBean
public class UserManager {

    @PersistenceContext(unitName = "RealErpPU")
    EntityManager em;
    @EJB
    FbsUserFacade fbsUserFacade;
    @EJB
    FbsLoginFacade fbsLoginFacade;
    @EJB
    LoginManager loginManager;
    FbsLogin fbsLogin = new FbsLogin();
    static FbsUser fbsUserRole = new FbsUser();
    List<FbsUser> userRoleList = new ArrayList();

    public void createUserRole(FbsUser fbsUser) {
        try {
            System.out.println("inside user manager");
            fbsUserFacade.create(fbsUser);
        } catch (Exception e) {
            System.out.println("Exception in add user role" + e);
        }
    }

    public String generateUserRoleXml(List<String> commandlist) {
        String xml_file = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?> ";
        xml_file += "<role_authorize>" + "\n";
        for (int i = 0; i < commandlist.size(); i++) {
            System.out.println(commandlist.get(i));
            xml_file += "<right>" + commandlist.get(i) + "</right>" + "\n";
        }
        xml_file += "</role_authorize>";
        //   System.out.println(xml_file);
        return xml_file;
    }

    public String readUserXml(String xmlFile1) {
        String rightList = "";
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            System.out.println("XML1 IS \n " + xmlFile1);

            Document doc = (Document) db.parse(new InputSource(new StringReader(xmlFile1)));
            NodeList rightCategoryList = doc.getElementsByTagName("role_authorize");
            int categoryLength = rightCategoryList.getLength();
            System.out.println("Category Length" + categoryLength);

            for (int s = 0; s < rightCategoryList.getLength(); s++) {

                Node categoryNode = rightCategoryList.item(s);
                if (categoryNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element categoryElement = (Element) categoryNode;
                    rightList = getRightFromNodeList(categoryElement, "company");




                    //  Element firstRightElement = (Element) rightList.item(0);
                    // NodeList textFNList = firstRightElement.getChildNodes();

                    //  al.add(s, textFNList);
                }
            }
        } catch (Exception e) {
            System.out.println("excetion");

            e.printStackTrace();
            System.out.println("excetion End");

        }
        return rightList;
    }

    String getRightFromNodeList(Element categoryElement, String listName) {
        String list = "";
        NodeList rightList = categoryElement.getElementsByTagName(listName);
        for (int r = 0; r < rightList.getLength(); r++) {
            Node rightNode = rightList.item(r);

            if (rightNode.getNodeType() == Node.ELEMENT_NODE) {
                Element rightElement = (Element) rightNode;

                NodeList RightList = rightElement.getElementsByTagName("right");
                for (int i = 0; i < RightList.getLength(); i++) {
                    Node node = RightList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        String data = RightList.item(i).getTextContent();
                        //System.out.println("date " + data);
                        list += data + "$";
                    }
                }
            }
        }
        return  list;
    }

    public String generateXMLForUserRole(TreeNode[] companyNodeList) {
        String xml_file = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?> ";
        xml_file += "<role_authorize>";
        xml_file += "<company>";
        for (TreeNode node : companyNodeList) {
            //  System.out.println(node.getData().toString());
            xml_file += "<right>" + node.getData().toString() + "</right>";
        }
        xml_file += "</company>";
        xml_file += "</role_authorize>";

        return xml_file;
    }

    //*****For user details
    public List<FbsLogin> showUserList() {
        return fbsLoginFacade.findAll();
    }

    public FbsLogin createUser(FbsLogin fbsLogin) {
        try {
            System.out.println("inside user manager");
            fbsLogin.setPassword(loginManager.convertPassword(fbsLogin.getPassword()));
            fbsLoginFacade.create(fbsLogin);
            


        } catch (Exception e) {
            System.out.println("Exception in add user" + e);
        }
        return fbsLogin;
    }

    public void editUser(FbsLogin fbsLogin,FbsUser fbsUser) {
        try {
             fbsUser = fbsUserFacade.find(fbsUser.getRollId());
             fbsLogin.setFbsUser(fbsUser);
            fbsLoginFacade.edit(fbsLogin);
        } catch (Exception ex) {
            System.out.println("Exception in user editing " + ex);
        }
    }

    public void deleteUser(FbsLogin fbsLogin) {
        try {
            fbsLoginFacade.remove(fbsLogin);
        } catch (Exception ex) {
            System.out.println("Exception in delete user " + ex);
        }
    }
    public boolean chechUniqueUserId(FbsLogin fbsLogin)
    {
       List<FbsLogin> fbsLogins=em.createNamedQuery("FbsLogin.findByUserId").setParameter("userId", fbsLogin.getUserId()).getResultList();
       if(fbsLogins.size() > 0)
           return  true;
       else
           return false;
    }
}
