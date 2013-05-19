/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.managedbean;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultUploadedFile;
import org.smp.realerp.entity.*;
import org.smp.realerp.manager.CompanyManager;
import org.smp.realerp.manager.LoginManager;
import org.smp.realerp.manager.UserManager;
import org.smp.realerp.pojo.sendReplyMail;
import org.smp.realerp.session.FbsLoginFacade;
import org.smp.realerp.session.FbsMailsettingFacade;

/**
 *
 * @author smp
 */
@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    @PersistenceContext(unitName = "RealErpPU")
    EntityManager em;
    @EJB
    LoginManager loginManager;
    @EJB
    CompanyManager companyManager;
    @EJB
    UserManager userManager;
    @EJB
    FbsMailsettingFacade fbsMailsettingFacade;
    @EJB
    FbsLoginFacade fbsLoginFacade;
    public static FbsLogin fbsLogin = new FbsLogin();
    public FbsLogin login = new FbsLogin();
    public static FbsLogin adminLogin = new FbsLogin();
    FbsLogin fbsLogin1 = new FbsLogin();
    FacesMessage msg = null;
    String email = "";
    String userId = "";
    String currentPassword = "";
    String newPassword = "";
    static List<FbsProject> fbsProjectList = new ArrayList<FbsProject>();
    boolean addBooking = false, viewBoking = false, authorizeBooking = false;
    boolean addPayment = false, viewPayment = false, authorizePayment = false;
    boolean addBrokerPayment = false, viewBrokerPayment = false, authorizeBrokerPayment = false;
    //boolean cCollectionReport = false, cReportGeneration = false, CReportArchive = false;
    boolean addProject = false, viewProject = false, authorizeProject = false;
    boolean addComplain = false, viewComplain = false, resolveComplain = false;
    boolean companySetting = false;
    Integer menuIndex = 0;
    Integer adminMenuIndex = 0;
    Integer dashboardIndex = 0;
    FbsCompany editFbsCompany = new FbsCompany();
    static List<FbsCompany> fbsCompanyList = new ArrayList<FbsCompany>();
    FbsCompany selectedCompany = new FbsCompany();
    FbsMailsetting fbsMailsetting = new FbsMailsetting();
    //Link
    boolean lDetail = false, lConsumenrReport = false, lTransfer = false, ldueLetterReport = false, lCancel = false;
    /*
     * for rendering in side bar constructor in FBSProject
     *
     * public FbsProject(String str) { this.projName = str; }
     */
    static FbsProject fbsProject = new FbsProject("Projects");
    static FbsProject enquiryFbsProject = new FbsProject();
    static boolean renderProjectMenu = false;
    public static final String PAYMNET = "Payment";
    public static final String ADD_PAYMENT = "Add Payment";
    public static final String VIEW_PAYMENT = "View Paymnet";
    public static final String AUTHORIZE_PAYMENT = "Authorize Payment";
    public static final String BOOKING = "Booking";
    public static final String ADD_BOOKING = "Add Booking";
    public static final String VIEW_BOOKING = "View Booking";
    public static final String AUTHORIZE_BOOKING = "Authorize Booking";
    public static final String BROKER = "Broker";
    public static final String ADD_BROKER_PAYMENT = "Add Broker Payment";
    public static final String VIEW_BROKER_PAYMENT = "View Broker Payment";
    public static final String AUTHORIZE_BROKER_PAYMENT = "Authorize Broker Payemnt";
    public static final String COMPANY_SETTING = "Company Setting";
    public static final String COMPLAIN = "Complain";
    public static final String ADD_COMPPLAIN = "Add Complain";
    public static final String VIEW_COMPLAIN = "View Complain";
    public static final String RESOLVE_COMPLAIN = "Resolve Complain";
    public static final String PROJECT = "Project";
    public static final String ADD_PROJECT = "Add Project";
    public static final String VIEW_PROJECT = "View Project";
    public static final String AUTHORIZE_PROJECT = "Authorize Project";
    String copyRightMessage;

    @PostConstruct
    public void populate() {
        fbsCompanyList.clear();
        fbsCompanyList = em.createNamedQuery("FbsCompany.findByStatus").setParameter("status", "activate").getResultList();
        showCopyRightMessage();
    }

    public void adminLoginValidate() throws IOException {

        // @NamedQuery(name = "FbsLogin.findByUserId&Password", query = "SELECT f FROM FbsLogin f WHERE f.userId = :userId AND f.password = :password"),
        List<FbsLogin> fbsloginList = em.createNamedQuery("FbsLogin.findByUserId&Password").setParameter("userId", adminLogin.getUserId()).setParameter("password", loginManager.convertPassword(adminLogin.getPassword())).getResultList();

        if (fbsloginList.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Invalid credentials", ""));
        } else {
            if (fbsloginList.get(0).getFbsUser().getRoleName().equalsIgnoreCase("Smp Admin")) {
                LoginBean.adminLogin = fbsloginList.get(0);
                redirectCompanySettings("admin");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Access Denied", ""));
            }
        }

    }

    public void validateUser() {
        fbsLogin = loginManager.validateUser(selectedCompany, login.getUserId(), login.getPassword());
        login = fbsLogin;
        if (fbsLogin != null) {
            try {
                fbsProjectList.clear();
                fbsProjectList = (List<FbsProject>) fbsLogin.getFbsCompany().getFbsProjectCollection();
                editFbsCompany = new FbsCompany();
                editFbsCompany = fbsLogin.getFbsCompany();
                fbsMailsetting = new FbsMailsetting();
                if (!editFbsCompany.getFbsMailSettingCollection().isEmpty()) {
                    fbsMailsetting = ((List<FbsMailsetting>) editFbsCompany.getFbsMailSettingCollection()).get(0);
                }
                LoginBean.renderProjectMenu = false;
                updateSideBar();
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/jsfpages/Dashboard/sampleChart.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            fbsLogin = new FbsLogin();
            login = new FbsLogin();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Invalid credentials", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        class eshort implements Comparator<Integer> {

            public int compare(Integer i1, Integer i2) {
                if (i1 == i2) {
                    return 0;
                } else {
                    return 1;
                }
            }
        }

        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(1);
        list.add(3);
        list.add(4);
        list.add(5);
        Collections.sort(list, new eshort());
        System.out.println("list..........=" + list);

    }

    public void activeMenu(Integer menuIndex, String url) throws IOException {
        System.out.println("dklfjklsklfj");
        this.menuIndex = menuIndex;
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + url);
    }

    public void adminActiveMenu(Integer adminMenuIndex, String url) throws IOException {
        System.out.println("dklfjklsklfj");
        this.adminMenuIndex = adminMenuIndex;
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + url);
    }

    void updateSideBar() {
        FbsUser fbsUser = login.getFbsUser();
        String xmldata = userManager.readUserXml(fbsUser.getXmlFile());

        // System.out.println(xmldata);

        if (xmldata.contains(ADD_BOOKING)) {
            viewBoking = true;
            addBooking = true;
            lTransfer = true;
            lConsumenrReport = true;
            lDetail = true;
            lCancel = true;
            ldueLetterReport = true;
        }
        if (xmldata.contains(VIEW_BOOKING)) {
            viewBoking = true;
            lConsumenrReport = true;
            ldueLetterReport = true;
            lDetail = true;


        }
        if (xmldata.contains(AUTHORIZE_BOOKING)) {
            viewBoking = true;
            addBooking = true;
            lTransfer = true;
            lConsumenrReport = true;
            lDetail = true;
            lCancel = true;
            ldueLetterReport = true;
            authorizeBooking = true;
        }
        if (xmldata.contains(ADD_PAYMENT)) {

            addPayment = true;
            viewPayment = true;

        }

        if (xmldata.contains(VIEW_PAYMENT)) {
            viewPayment = true;

        }
        if (xmldata.contains(AUTHORIZE_PAYMENT)) {

            viewPayment = true;
            addPayment = true;
            authorizePayment = true;
        }
        if (xmldata.contains(ADD_BROKER_PAYMENT)) {

            addBrokerPayment = true;
            viewBrokerPayment = true;
        }
        if (xmldata.contains(VIEW_BROKER_PAYMENT)) {
            viewBrokerPayment = true;

        }
        if (xmldata.contains(AUTHORIZE_BROKER_PAYMENT)) {
            viewBrokerPayment = true;
            addBrokerPayment = true;
            authorizeBrokerPayment = true;
        }


        if (xmldata.contains(COMPANY_SETTING)) {
            companySetting = true;
        }

        if (xmldata.contains(ADD_PROJECT)) {
            addProject = true;
            viewProject = true;
        }
        if (xmldata.contains(VIEW_PROJECT)) {
            viewProject = true;
        }
        if (xmldata.contains(AUTHORIZE_PROJECT)) {
            addProject = true;
            viewProject = true;
            authorizeProject = true;
        }
        if (xmldata.contains(ADD_COMPPLAIN)) {
            addComplain = true;
            viewComplain = true;
        }
        if (xmldata.contains(VIEW_COMPLAIN)) {
            viewComplain = true;
        }
        if (xmldata.contains(RESOLVE_COMPLAIN)) {
            resolveComplain = true;
            addComplain = true;
            viewComplain = true;
        }




    }

    public void logout() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            session.invalidate();
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/index.xhtml");
            LoginBean.fbsLogin = new FbsLogin();
            LoginBean.fbsProject = new FbsProject("Projects");

        } catch (IOException ex) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void adminLogout() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            session.invalidate();
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/index.xhtml");
            LoginBean.adminLogin = new FbsLogin();
            LoginBean.fbsProject = new FbsProject("Projects");

        } catch (IOException ex) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void redirectIndex() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/index.xhtml");


    }

    public void recoverPassword() throws IOException {
        System.out.println("recoverPassword");
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/recoverPassword.xhtml");
    }

    public void changePassword() throws IOException, NoSuchAlgorithmException {
        currentPassword = loginManager.convertPassword(currentPassword);
        newPassword = loginManager.convertPassword(newPassword);
        if (newPassword.equals(fbsLogin.getPassword())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password Already Exist", ""));

        } else {
            if (fbsLogin.getPassword().equals(currentPassword)) {
                FbsLogin newFbsLogin = loginManager.changePassword(fbsLogin, newPassword);
                if ((newFbsLogin != null) && (newFbsLogin.getLoginId() != null)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password successfully changed", ""));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password cannot be changed", ""));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Current Password not exist", ""));
            }
        }
    }

    public void showPassword() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/jsfpages/PasswordRecovery/changePassword.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void forwardIndex() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/index.xhtml");
    }

    public void checkMailExists() {
        int flag = 0;

        flag = loginManager.checkMailExists(userId, email);
        System.out.println("flag++++ " + flag);
        if (flag == 1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid User Id", ""));
        }
        if (flag == 2) {
            if (LoginBean.fbsLogin.getFbsCompany().getFbsMailSettingCollection().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Server Settings Not Found!", ""));
            } else {

                String randomPassword = loginManager.getAlphaNumeric(6);
                System.out.println("random Password is+++++" + randomPassword);

                fbsLogin1 = (FbsLogin) em.createNamedQuery("FbsLogin.findByUserId").setParameter("userId", userId).getResultList().get(0);
                loginManager.editRandomPassword(userId, randomPassword);
                sendReplyMail sendMail = new sendReplyMail(email, randomPassword, fbsLogin1.getUserName());
                Thread thread = new Thread(sendMail);
                thread.start();
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/showMessage.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (flag == 3) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid EmailId", ""));
        }
    }

    public void handleFileUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        System.out.println("klsdfkskl");
        DefaultUploadedFile inputFile = (DefaultUploadedFile) event.getFile();
        System.out.println("file name = " + FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "resources/images/" + inputFile.getFileName());
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        path = path.replaceAll("build.*", "");
        path = path + "/web/resources/";
        System.out.println("path " + path);
        File outputFile = new File(path + inputFile.getFileName());
        byte byteArray[] = inputFile.getContents();
        OutputStream fileOutputStream = new FileOutputStream(outputFile);
        fileOutputStream.write(byteArray);
        fileOutputStream.close();

    }

    public void redirectAdminLoginForm() throws IOException {
        LoginBean.adminLogin = new FbsLogin();
        this.menuIndex = 0;
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/adminIndex.xhtml");
    }

    public void redirectCompanySettings(String requestType) throws IOException {
        this.menuIndex = 0;
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/registerCompany.xhtml?requestType=" + requestType);
    }

    public void redirectCompanyDashboard() throws IOException {
        fbsProject = new FbsProject();
        fbsProject.setProjName("Projects");
        LoginBean.renderProjectMenu = false;
        this.menuIndex = 0;
        this.dashboardIndex = 1;
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/jsfpages/Dashboard/sampleChart.xhtml");

    }

    public void onSelectionSetProject(FbsProject fbsProject) throws IOException {
        LoginBean.fbsProject = fbsProject;
        LoginBean.renderProjectMenu = true;
        this.dashboardIndex = 2;
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/jsfpages/Dashboard/sampleChart.xhtml");

    }

    public void editCompanyInformation() {
        companyManager.editCompanyInfo(editFbsCompany);
        fbsLogin.setFbsCompany(editFbsCompany);
        login.setFbsCompany(editFbsCompany);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Company Updated", ""));
    }

    public void redirectCompanyWebsite() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("http://" + login.getFbsCompany().getWebsite());
    }

    public void editUserDetails() {
        loginManager.editUserDetails(login);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User Updated", ""));
    }

    public void showCopyRightMessage() {
        copyRightMessage = "";
        copyRightMessage = loginManager.copyRightMesasge();
    }

    public void editCoyRightMessage() {
        loginManager.editCopyRightMessage(copyRightMessage);
        showCopyRightMessage();
        LoginBean.adminLogin = fbsLoginFacade.find(LoginBean.adminLogin.getLoginId());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "CopyRight Message Updated", ""));
    }

    public void addCompanyMailSetting() {
        if (editFbsCompany.getFbsMailSettingCollection().isEmpty()) {
            fbsMailsetting.setFbsCompany(LoginBean.fbsLogin.getFbsCompany());
            fbsMailsettingFacade.create(fbsMailsetting);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Added Mail Settings", ""));
        } else {
            fbsMailsettingFacade.edit(fbsMailsetting);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mail Settings Updated", ""));
        }
        fbsLogin.setFbsCompany(editFbsCompany);
        login.setFbsCompany(editFbsCompany);
    }

    public void testPingToserver() {
        System.out.println("ping 2 server..");
        boolean ping = loginManager.testPingToServer(fbsMailsetting.getHost());
        if (ping == true) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Faliure", ""));
        }
    }

    public FbsLogin getLogin() {
        return login;
    }

    public void setLogin(FbsLogin login) {
        this.login = login;
    }

    public FbsProject getFbsProject() {
        return fbsProject;
    }

    public void setFbsProject(FbsProject fbsProject) {
        this.fbsProject = fbsProject;
    }

    public List<FbsProject> getFbsProjectList() {
        return fbsProjectList;
    }

    public void setFbsProjectList(List<FbsProject> fbsProjectList) {
        this.fbsProjectList = fbsProjectList;
    }

    public boolean isRenderProjectMenu() {
        return renderProjectMenu;
    }

    public void setRenderProjectMenu(boolean renderProjectMenu) {
        this.renderProjectMenu = renderProjectMenu;
    }

    public FbsLogin getFbsLogin1() {
        return fbsLogin1;
    }

    public void setFbsLogin1(FbsLogin fbsLogin1) {
        this.fbsLogin1 = fbsLogin1;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public CompanyManager getCompanyManager() {
        return companyManager;
    }

    public void setCompanyManager(CompanyManager companyManager) {
        this.companyManager = companyManager;
    }

    public boolean islConsumenrReport() {
        return lConsumenrReport;
    }

    public void setlConsumenrReport(boolean lConsumenrReport) {
        this.lConsumenrReport = lConsumenrReport;
    }

    public boolean islDetail() {
        return lDetail;
    }

    public void setlDetail(boolean lDetail) {
        this.lDetail = lDetail;
    }

    public boolean islTransfer() {
        return lTransfer;
    }

    public void setlTransfer(boolean lTransfer) {
        this.lTransfer = lTransfer;
    }

    public boolean isLdueLetterReport() {
        return ldueLetterReport;
    }

    public void setLdueLetterReport(boolean ldueLetterReport) {
        this.ldueLetterReport = ldueLetterReport;
    }

    public boolean isAddBooking() {
        return addBooking;
    }

    public void setAddBooking(boolean addBooking) {
        this.addBooking = addBooking;
    }

    public boolean isAddBrokerPayment() {
        return addBrokerPayment;
    }

    public void setAddBrokerPayment(boolean addBrokerPayment) {
        this.addBrokerPayment = addBrokerPayment;
    }

    public boolean isAddComplain() {
        return addComplain;
    }

    public void setAddComplain(boolean addComplain) {
        this.addComplain = addComplain;
    }

    public boolean isAddPayment() {
        return addPayment;
    }

    public void setAddPayment(boolean addPayment) {
        this.addPayment = addPayment;
    }

    public boolean isAddProject() {
        return addProject;
    }

    public void setAddProject(boolean addProject) {
        this.addProject = addProject;
    }

    public boolean isAuthorizeBooking() {
        return authorizeBooking;
    }

    public void setAuthorizeBooking(boolean authorizeBooking) {
        this.authorizeBooking = authorizeBooking;
    }

    public boolean isAuthorizeBrokerPayment() {
        return authorizeBrokerPayment;
    }

    public void setAuthorizeBrokerPayment(boolean authorizeBrokerPayment) {
        this.authorizeBrokerPayment = authorizeBrokerPayment;
    }

    public boolean isAuthorizePayment() {
        return authorizePayment;
    }

    public void setAuthorizePayment(boolean authorizePayment) {
        this.authorizePayment = authorizePayment;
    }

    public boolean isAuthorizeProject() {
        return authorizeProject;
    }

    public void setAuthorizeProject(boolean authorizeProject) {
        this.authorizeProject = authorizeProject;
    }

    public boolean isCompanySetting() {
        return companySetting;
    }

    public void setCompanySetting(boolean companySetting) {
        this.companySetting = companySetting;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public boolean isResolveComplain() {
        return resolveComplain;
    }

    public void setResolveComplain(boolean resolveComplain) {
        this.resolveComplain = resolveComplain;
    }

    public boolean isViewBoking() {
        return viewBoking;
    }

    public void setViewBoking(boolean viewBoking) {
        this.viewBoking = viewBoking;
    }

    public boolean isViewBrokerPayment() {
        return viewBrokerPayment;
    }

    public void setViewBrokerPayment(boolean viewBrokerPayment) {
        this.viewBrokerPayment = viewBrokerPayment;
    }

    public boolean isViewComplain() {
        return viewComplain;
    }

    public void setViewComplain(boolean viewComplain) {
        this.viewComplain = viewComplain;
    }

    public boolean isViewPayment() {
        return viewPayment;
    }

    public void setViewPayment(boolean viewPayment) {
        this.viewPayment = viewPayment;
    }

    public boolean isViewProject() {
        return viewProject;
    }

    public void setViewProject(boolean viewProject) {
        this.viewProject = viewProject;
    }

    public boolean islCancel() {
        return lCancel;
    }

    public void setlCancel(boolean lCancel) {
        this.lCancel = lCancel;
    }

    //***********
    public Integer getMenuIndex() {
        return menuIndex;
    }

    public void setMenuIndex(Integer menuIndex) {
        this.menuIndex = menuIndex;
    }

    public Integer getDashboardIndex() {
        return dashboardIndex;
    }

    public void setDashboardIndex(Integer dashboardIndex) {
        this.dashboardIndex = dashboardIndex;
    }

    public FbsLogin getAdminLogin() {
        return adminLogin;
    }

    public void setAdminLogin(FbsLogin adminLogin) {
        this.adminLogin = adminLogin;
    }

    public FbsCompany getEditFbsCompany() {
        return editFbsCompany;
    }

    public void setEditFbsCompany(FbsCompany editFbsCompany) {
        this.editFbsCompany = editFbsCompany;
    }

    public List<FbsCompany> getFbsCompanyList() {
        return fbsCompanyList;
    }

    public void setFbsCompanyList(List<FbsCompany> fbsCompanyList) {
        this.fbsCompanyList = fbsCompanyList;
    }

    public FbsCompany getSelectedCompany() {
        return selectedCompany;
    }

    public void setSelectedCompany(FbsCompany selectedCompany) {
        this.selectedCompany = selectedCompany;
    }

    public Integer getAdminMenuIndex() {
        return adminMenuIndex;
    }

    public void setAdminMenuIndex(Integer adminMenuIndex) {
        this.adminMenuIndex = adminMenuIndex;
    }

    public FbsMailsetting getFbsMailsetting() {
        return fbsMailsetting;
    }

    public void setFbsMailsetting(FbsMailsetting fbsMailsetting) {
        this.fbsMailsetting = fbsMailsetting;
    }

    public String getCopyRightMessage() {
        return copyRightMessage;
    }

    public void setCopyRightMessage(String copyRightMessage) {
        this.copyRightMessage = copyRightMessage;
    }
}
