/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.smp.realerp.entity.FbsCompany;
import org.smp.realerp.entity.FbsLogin;
import org.smp.realerp.entity.FbsUser;
import org.smp.realerp.managedbean.LoginBean;
import org.smp.realerp.session.FbsCompanyFacade;
import org.smp.realerp.session.FbsLoginFacade;

/**
 *
 * @author smp
 */
@Stateless
@LocalBean
public class LoginManager {

    @PersistenceContext(unitName = "RealErpPU")
    EntityManager em;
    @EJB
    FbsLoginFacade fbsLoginFacade;
    @EJB
    FbsCompanyFacade fbsCompanyFacade;
    MessageDigest algorithm = null;
    FbsLogin fbsLogin = new FbsLogin();
    String encyptpass = "";
    private static final String ALPHA_NUM = "!1@2aA#Ss3Dd$Ff4gG%Hh5Jj@Kk6Ll&Zz7Xx*Cc8Vv(Bb9Nn)Mm0QqWwEeR-rTtYy+Uu}]IiOo{[Pp";

    public FbsLogin validateUser(FbsCompany fbsCompany, String userId, String password) {
        System.out.println("company id==> " + fbsCompany.getCompanyId());
        int flag = 0;
        List<FbsLogin> fbsLoginList = em.createNamedQuery("FbsLogin.findByUserId").setParameter("userId", userId).getResultList();
        if (fbsLoginList.isEmpty()) {
            return null;
        }
        password = convertPassword(password);

        if ((fbsLoginList.get(0).getPassword().equals(password))
                && (fbsLoginList.get(0).getFbsCompany().getCompanyId().intValue() == fbsCompany.getCompanyId().intValue())) {
            return fbsLoginList.get(0);
        } else {
            return null;
        }

    }

    public String convertPassword(String password) {
        try {
            algorithm = MessageDigest.getInstance("SHA-256");
            byte[] defaultBytes = password.getBytes();
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte[] messageDigest = algorithm.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            password = hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return password;
    }

    public int checkMailExists(String userId, String email) {
        int flag = 0;
        //fbsLogin = new FbsLogin();
        System.out.println("user id is........" + userId);
        List<FbsLogin> fbsLoginList1 = em.createNamedQuery("FbsLogin.findByUserId").setParameter("userId", userId).getResultList();
        System.out.println("size of list is....... " + fbsLoginList1.size());
        System.out.println("user name is........ " + fbsLoginList1.get(0).getUserName());
        if (fbsLoginList1.isEmpty()) {
            return flag = 1;
        }

        if (fbsLoginList1.get(0).getEmail().equals(email)) {
            return flag = 2;
        } else {
            return flag = 3;
        }


    }

    public String getAlphaNumeric(int len) {
        StringBuffer sb = new StringBuffer(len);
        for (int i = 0; i < len; i++) {
            int ndx = (int) (Math.random() * ALPHA_NUM.length());
            sb.append(ALPHA_NUM.charAt(ndx));
        }
        return sb.toString();
    }

    public void editRandomPassword(String userId, String randomPassword) {
        try {
            algorithm = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException nsae) {
            System.out.println("Cannot find digest algorithm" + nsae);
            System.exit(1);
        }

        byte[] defaultBytes = randomPassword.getBytes();
        algorithm.reset();
        algorithm.update(defaultBytes);
        byte messageDigest[] = algorithm.digest();
        StringBuffer hexString = new StringBuffer();

        for (int j = 0; j < messageDigest.length; j++) {
            String hex = Integer.toHexString(0xFF & messageDigest[j]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        encyptpass = hexString.toString();
        FbsLogin fbsLogin = new FbsLogin();
        List<FbsLogin> fbsLoginList1 = em.createNamedQuery("FbsLogin.findByUserId").setParameter("userId", userId).getResultList();
        fbsLoginList1.get(0).setPassword(encyptpass);
        fbsLoginFacade.edit(fbsLoginList1.get(0));
    }

    public FbsLogin changePassword(FbsLogin fbsLogin, String newPassword) {
        FbsLogin newFbsLogin = new FbsLogin();
        try {
            newFbsLogin = fbsLoginFacade.find(fbsLogin.getLoginId());
            newFbsLogin.setOldPassword(fbsLogin.getPassword());
            newFbsLogin.setPassword(newPassword);
            fbsLoginFacade.edit(newFbsLogin);

        } catch (Exception ex) {
            System.out.println("Exception in changing password: " + ex);
        }
        return newFbsLogin;
    }

    public void editUserDetails(FbsLogin fbsLogin) {
        try {
            fbsLoginFacade.edit(fbsLogin);
        } catch (Exception ex) {
            System.out.println("Exception in edit user Details" + ex);
        }
    }

    public String copyRightMesasge() {
        List<FbsUser> userList = em.createNamedQuery("FbsUser.findByRoleName").setParameter("roleName", "Smp Admin").getResultList();
        if (userList.isEmpty()) {
            return "";
        } else {
            return userList.get(0).getFbsCompany().getCopyRight();
        }
    }

    public void editCopyRightMessage(String newMessage) {
        FbsCompany fbsCompany = LoginBean.adminLogin.getFbsCompany();
        fbsCompany.setCopyRight(newMessage);
        fbsCompanyFacade.edit(fbsCompany);


    }

    public boolean testPingToServer(String ip) {

        String pingResult = "";
        boolean test = false;

        String pingCmd = "ping " +ip;

        try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(pingCmd);

            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String inputLine;
            if (in.readLine() == null) {
                test = false;
                System.out.println("no answer");
            } else {
                if (in.readLine().contains("ttl") || in.readLine().contains("TTL")) {
                    test = true;
                } else {
                    test = false;
                }
//                while ((inputLine = in.readLine()) != null) {
//                    System.out.println(inputLine);
//                    pingResult += inputLine;                    
//                }
            }
            in.close();

        }//try
        catch (IOException e) {
            System.out.println(e);
             test = false;
        }
        return test;
    }
}
