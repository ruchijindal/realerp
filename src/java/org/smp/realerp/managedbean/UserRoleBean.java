/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.managedbean;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.PersistenceContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.smp.realerp.entity.FbsApplicant;
import org.smp.realerp.entity.FbsCompany;
import org.smp.realerp.entity.FbsUser;
import org.smp.realerp.manager.CompanyManager;
import org.smp.realerp.manager.UserManager;
import org.smp.realerp.session.FbsUserFacade;

/**
 *
 * @author smp
 */
@ManagedBean(name = "userRoleBean")
@ViewScoped
public class UserRoleBean {

    @PersistenceContext(unitName = "RealErpPU")
    @EJB
    FbsUserFacade fbsUserFacade;
    @EJB
    UserManager userManager;
    @EJB
    CompanyManager companyManager;
    FbsUser fbsUser = new FbsUser();
    List<String> commandlist = new ArrayList<String>();
    public List<FbsUser> userRoleList = new ArrayList();
    private TreeNode companySideBar, editCompanySideBar;
    private TreeNode[] selectedCompanyNode;
    private TreeNode[] editselectedNode;
    private FbsUser processFbsUser;
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
    FbsCompany fbsCompany = new FbsCompany();

    @PostConstruct
    public void populate() {
        commandlist.clear();
        fbsCompany = new FbsCompany();
        fbsCompany = companyManager.populateFbsCompany(LoginBean.fbsLogin.getFbsCompany().getCompanyId());
        userRoleList.clear();
        userRoleList = (List<FbsUser>) fbsCompany.getFbsUserCollection();
        generateCompanyTree();


    }

    public void addUserRole() {
        FbsCompany fbsCompany = LoginBean.fbsLogin.getFbsCompany();//(FbsCompany) session.getAttribute("company");
        System.out.println("fbscompany id==>" + fbsCompany.getCompanyId());

        String xml = userManager.generateXMLForUserRole(selectedCompanyNode);
        System.out.println("xML readed is " + xml);
        fbsUser.setXmlFile(xml);
        fbsUser.setFbsCompany(fbsCompany);
        try {
            if (checkUserRole(fbsUser)) {
                fbsUserFacade.create(fbsUser);
                fbsUser = new FbsUser();
                populate();

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, " User Role Added", ""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User Role Already Exist", ""));
            }

        } catch (Exception e) {
            System.out.println("Unable to create user role ");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in addding the User Role", " "));
        }
        //FacesContext context = FacesContext.getCurrentInstance();
        //context.addMessage(null, new FacesMessage("Congrates! User Successfully Added"));
//        populate();
    }

    void generateCompanyTree() {
        companySideBar = new DefaultTreeNode("Root", null);
        TreeNode companySettring = new DefaultTreeNode(COMPANY_SETTING, companySideBar);
        TreeNode booking = new DefaultTreeNode(BOOKING, companySideBar);

        TreeNode addBooking = new DefaultTreeNode(ADD_BOOKING, booking);
        TreeNode viewBooking = new DefaultTreeNode(VIEW_BOOKING, booking);
        TreeNode authirizeBooking = new DefaultTreeNode(AUTHORIZE_BOOKING, booking);

        TreeNode payment = new DefaultTreeNode(PAYMNET, companySideBar);

        TreeNode addPayment = new DefaultTreeNode(ADD_PAYMENT, payment);
        TreeNode viewPayment = new DefaultTreeNode(VIEW_PAYMENT, payment);
        TreeNode authirizePayment = new DefaultTreeNode(AUTHORIZE_PAYMENT, payment);

        TreeNode broker = new DefaultTreeNode(BROKER, companySideBar);
        TreeNode addBrokerPaymnet = new DefaultTreeNode(ADD_BROKER_PAYMENT, broker);
        TreeNode viewBrokerPayment = new DefaultTreeNode(VIEW_BROKER_PAYMENT, broker);
        TreeNode authorizeBrokerPayment = new DefaultTreeNode(AUTHORIZE_BROKER_PAYMENT, broker);

        TreeNode complain = new DefaultTreeNode(COMPLAIN, companySideBar);
        TreeNode addComplain = new DefaultTreeNode(ADD_COMPPLAIN, complain);
        TreeNode viewComplain = new DefaultTreeNode(VIEW_COMPLAIN, complain);
        TreeNode resolveComplain = new DefaultTreeNode(RESOLVE_COMPLAIN, complain);


        TreeNode projectSetting = new DefaultTreeNode(PROJECT, companySideBar);


        TreeNode addProject = new DefaultTreeNode(ADD_PROJECT, projectSetting);
        TreeNode viewProject = new DefaultTreeNode(VIEW_PROJECT, projectSetting);
        TreeNode authorizeProject = new DefaultTreeNode(AUTHORIZE_PROJECT, projectSetting);



    }

    public void processUpdate(FbsUser fbsUser) {
        this.fbsUser = fbsUser;
        System.out.println("Fbs User " + fbsUser.getRoleName());

        editCompanySideBar = new DefaultTreeNode("Root", null);
        TreeNode companySettring = new DefaultTreeNode(COMPANY_SETTING, editCompanySideBar);
        TreeNode booking = new DefaultTreeNode(BOOKING, editCompanySideBar);

        TreeNode addBooking = new DefaultTreeNode(ADD_BOOKING, booking);
        TreeNode viewBooking = new DefaultTreeNode(VIEW_BOOKING, booking);
        TreeNode authirizeBooking = new DefaultTreeNode(AUTHORIZE_BOOKING, booking);

        TreeNode payment = new DefaultTreeNode(PAYMNET, editCompanySideBar);

        TreeNode addPayment = new DefaultTreeNode(ADD_PAYMENT, payment);
        TreeNode viewPayment = new DefaultTreeNode(VIEW_PAYMENT, payment);
        TreeNode authirizePayment = new DefaultTreeNode(AUTHORIZE_PAYMENT, payment);

        TreeNode broker = new DefaultTreeNode(BROKER, editCompanySideBar);

        TreeNode addBrokerPaymnet = new DefaultTreeNode(ADD_BROKER_PAYMENT, broker);
        TreeNode viewBrokerPayment = new DefaultTreeNode(VIEW_BROKER_PAYMENT, broker);
        TreeNode authorizeBrokerPayment = new DefaultTreeNode(AUTHORIZE_BROKER_PAYMENT, broker);

        TreeNode complain = new DefaultTreeNode(COMPLAIN, editCompanySideBar);
        TreeNode addComplain = new DefaultTreeNode(ADD_COMPPLAIN, complain);
        TreeNode viewComplain = new DefaultTreeNode(VIEW_COMPLAIN, complain);
        TreeNode resolveComplain = new DefaultTreeNode(RESOLVE_COMPLAIN, complain);


        TreeNode projectSetting = new DefaultTreeNode(PROJECT, editCompanySideBar);


        TreeNode addProject = new DefaultTreeNode(ADD_PROJECT, projectSetting);
        TreeNode viewProject = new DefaultTreeNode(VIEW_PROJECT, projectSetting);
        TreeNode authorizeProject = new DefaultTreeNode(AUTHORIZE_PROJECT, projectSetting);



        String xmldata = userManager.readUserXml(fbsUser.getXmlFile());

        // System.out.println(xmldata);


        if (xmldata.contains(ADD_BOOKING)) {
            addBooking.setExpanded(true);

            addBooking.setSelected(true);


        }
        if (xmldata.contains(VIEW_BOOKING)) {

            viewBooking.setSelected(true);
            viewBooking.setExpanded(true);

        }
        if (xmldata.contains(AUTHORIZE_BOOKING)) {
            authirizeBooking.setSelected(true);
            authirizeBooking.setExpanded(true);
            booking.setSelected(true);
        }
        if (xmldata.contains(ADD_PAYMENT)) {
            System.out.println("add payment is true ");
            addPayment.setExpanded(true);
            addPayment.setSelected(true);

        }

        if (xmldata.contains(VIEW_PAYMENT)) {
            viewPayment.setSelected(true);
            viewPayment.setExpanded(true);

        }
        if (xmldata.contains(AUTHORIZE_PAYMENT)) {
            authirizePayment.setSelected(true);
            authirizePayment.setExpanded(true);
            payment.setSelected(true);

        }
        if (xmldata.contains(ADD_BROKER_PAYMENT)) {

            addBrokerPaymnet.setSelected(true);
            addBrokerPaymnet.setExpanded(true);
        }
        if (xmldata.contains(VIEW_BROKER_PAYMENT)) {
            viewBrokerPayment.setSelected(true);
            viewBrokerPayment.setExpanded(true);

        }
        if (xmldata.contains(AUTHORIZE_BROKER_PAYMENT)) {
            authorizeBrokerPayment.setSelected(true);
            authorizeBrokerPayment.setExpanded(true);
            broker.setSelected(true);

        }


        if (xmldata.contains(COMPANY_SETTING)) {
            companySettring.setSelected(true);

        }

        if (xmldata.contains(ADD_PROJECT)) {
            addProject.setSelected(true);
            addProject.setExpanded(true);
        }
        if (xmldata.contains(VIEW_PROJECT)) {
            viewProject.setSelected(true);
            viewProject.setExpanded(true);
        }
        if (xmldata.contains(AUTHORIZE_PROJECT)) {
            authorizeProject.setSelected(true);
            authorizeProject.setExpanded(true);
            projectSetting.setSelected(true);
        }
        if (xmldata.contains(ADD_COMPPLAIN)) {
            addComplain.setSelected(true);
            addComplain.setExpanded(true);
        }
        if (xmldata.contains(VIEW_COMPLAIN)) {
            viewComplain.setSelected(true);
            viewComplain.setExpanded(true);
        }
        if (xmldata.contains(RESOLVE_COMPLAIN)) {
            resolveComplain.setSelected(true);
            resolveComplain.setExpanded(true);
        }
    }

    public void updateUserRole() {
        String xml = userManager.generateXMLForUserRole(editselectedNode);
        System.out.println("xML readed is " + xml);
        fbsUser.setXmlFile(xml);

        try {
            if (!checkUserRole(fbsUser)) {
                fbsUserFacade.edit(fbsUser);
                fbsUser = new FbsUser();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User Role Updated", ""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User Role Already Exist", ""));
            }
        } catch (Exception e) {
            System.out.println("Unable to create user role ");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in Updating the User Role ", ""));
        }

    }

    boolean checkUserRole(FbsUser fbsUser) {
        boolean check = true;
        for (FbsUser f : userRoleList) {
            if (f.getRoleName().equalsIgnoreCase(fbsUser.getRoleName())) {
                check = false;
            }
        }
        return check;
    }

    public void confirmDeleteUserRole(FbsUser user) {
        processFbsUser = new FbsUser();
        processFbsUser = user;
    }

    public void deleteUserRole() {
        fbsUserFacade.remove(processFbsUser);
        processFbsUser = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User Role Deleted", ""));
        populate();
    }

    public FbsUser getFbsUser() {
        return fbsUser;
    }

    public void setFbsUser(FbsUser fbsUser) {
        this.fbsUser = fbsUser;
    }

    public List<FbsUser> getUserRoleList() {
        return userRoleList;
    }

    public void setUserRoleList(List<FbsUser> userRoleList) {
        this.userRoleList = userRoleList;
    }

    public TreeNode getCompanySideBar() {
        return companySideBar;
    }

    public void setCompanySideBar(TreeNode companySideBar) {
        this.companySideBar = companySideBar;
    }

    public TreeNode[] getSelectedCompanyNode() {
        return selectedCompanyNode;
    }

    public void setSelectedCompanyNode(TreeNode[] selectedCompanyNode) {
        this.selectedCompanyNode = selectedCompanyNode;
    }

    public FbsUser getProcessFbsUser() {
        return processFbsUser;
    }

    public void setProcessFbsUser(FbsUser processFbsUser) {
        this.processFbsUser = processFbsUser;
    }

    public TreeNode[] getEditselectedNode() {
        return editselectedNode;
    }

    public void setEditselectedNode(TreeNode[] editselectedNode) {
        this.editselectedNode = editselectedNode;
    }

    public TreeNode getEditCompanySideBar() {
        return editCompanySideBar;
    }

    public void setEditCompanySideBar(TreeNode editCompanySideBar) {
        this.editCompanySideBar = editCompanySideBar;
    }
}
