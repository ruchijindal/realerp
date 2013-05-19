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
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.smp.realerp.entity.*;
import org.smp.realerp.manager.CompanyManager;
import org.smp.realerp.manager.LoginManager;
import org.smp.realerp.manager.UserManager;
import org.smp.realerp.session.FbsLoginFacade;
import org.smp.realerp.session.FbsProjectAllotFacade;
import org.smp.realerp.session.FbsUserFacade;

/**
 *
 * @author smp
 */
@ManagedBean(name = "userBean")
@ViewScoped
public class UserBean {

    @EJB
    UserManager userManager;
    @EJB
    FbsLoginFacade fbsLoginFacade;
    @EJB
    FbsUserFacade fbsUserFacade;
    @EJB
    CompanyManager companyManager;
    @EJB
    FbsProjectAllotFacade fbsProjectAllotFacade;
    @EJB
    LoginManager loginManager;
    FbsLogin fbsLogin = new FbsLogin();
    FbsUser fbsUser = new FbsUser();
    FbsCompany fbsCompany = new FbsCompany();
    List<FbsUser> fbsUserList = new ArrayList<FbsUser>();
    List<FbsLogin> showUserList = new ArrayList();
    static FbsLogin delFbsLogin = new FbsLogin();
    FbsLogin editFbsLogin = new FbsLogin();
    boolean renderDialog;
    private TreeNode root;
    private TreeNode[] selectedProjects;

    @PostConstruct
    public void populate() {
        fbsCompany = new FbsCompany();
        fbsCompany = companyManager.populateFbsCompany(LoginBean.fbsLogin.getFbsCompany().getCompanyId());
        showUserList.clear();
        showUserList = (List<FbsLogin>) fbsCompany.getFbsLoginCollection();
        fbsUserList.clear();
        fbsUserList = (List<FbsUser>) fbsCompany.getFbsUserCollection();
        fbsLogin = new FbsLogin();
        List<FbsProject> fbsProjectList = (List<FbsProject>) fbsCompany.getFbsProjectCollection();
        root = new DefaultTreeNode("Root", null);
        for (int i = 0; i < fbsProjectList.size(); i++) {
            TreeNode node = new DefaultTreeNode(fbsProjectList.get(i), root);
        }

    }

    public void addUser() {
        fbsUser = fbsUserFacade.find(fbsUser.getRollId());
        fbsLogin.setFbsCompany(LoginBean.fbsLogin.getFbsCompany());
        fbsLogin.setFbsUser(fbsUser);
        fbsLogin.setCreatedBy(LoginBean.fbsLogin.getUserId());
        fbsLogin.setPassword(loginManager.convertPassword(fbsLogin.getPassword()));
        fbsLoginFacade.create(fbsLogin);
        System.out.println("fbslogin==> " + fbsLogin.getLoginId());
        FbsProjectAllot fbsProjectAllot = new FbsProjectAllot();
        for (int i = 0; i < selectedProjects.length; i++) {
            fbsProjectAllot = new FbsProjectAllot();
            fbsProjectAllot.setFbsProject((FbsProject) selectedProjects[i].getData());
            fbsProjectAllot.setFbsLogin(fbsLogin);
            fbsProjectAllotFacade.create(fbsProjectAllot);

        }
        fbsLogin = new FbsLogin();
        fbsUser = new FbsUser();
        populate();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User Added", ""));
    }

    public void edit(FbsLogin fbsLogin) {
        editFbsLogin = new FbsLogin();
        editFbsLogin = fbsLogin;
        renderDialog = true;
        fbsUser = new FbsUser();
        fbsUser = fbsUserFacade.find(editFbsLogin.getFbsUser().getRollId());
        System.out.println("inside edit fbsuser is..." + fbsUser.getRoleName());
        List<FbsProjectAllot> projectAllotList = (List<FbsProjectAllot>) editFbsLogin.getFbsProjectAllotCollection();
      
        for (int i = 0; i < projectAllotList.size(); i++) {
          TreeNode node=(TreeNode) projectAllotList.get(i).getFbsProject();
          node.setSelected(true);
        }
    }

    public void editUser() {

        userManager.editUser(editFbsLogin, fbsUser);
        populate();
        renderDialog = false;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User Updated", ""));
    }

    public void confirmDeleteUser(FbsLogin fbsLogin) {
        delFbsLogin = new FbsLogin();
        delFbsLogin = fbsLogin;
    }

    public void deleteUser() {
        userManager.deleteUser(delFbsLogin);
        delFbsLogin = new FbsLogin();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User Deleted", ""));
        populate();
    }

    public FbsUser getFbsUser() {
        return fbsUser;
    }

    public void setFbsUser(FbsUser fbsUser) {
        this.fbsUser = fbsUser;
    }

    public List<FbsUser> getFbsUserList() {
        return fbsUserList;
    }

    public void setFbsUserList(List<FbsUser> fbsUserList) {
        this.fbsUserList = fbsUserList;
    }

    public FbsLogin getFbsLogin() {
        return fbsLogin;
    }

    public void setFbsLogin(FbsLogin fbsLogin) {
        this.fbsLogin = fbsLogin;
    }

    public List<FbsLogin> getShowUserList() {
        return showUserList;
    }

    public void setShowUserList(List<FbsLogin> showUserList) {
        this.showUserList = showUserList;
    }

    public boolean isRenderDialog() {
        return renderDialog;
    }

    public void setRenderDialog(boolean renderDialog) {
        this.renderDialog = renderDialog;
    }

    public FbsLogin getEditFbsLogin() {
        return editFbsLogin;
    }

    public void setEditFbsLogin(FbsLogin editFbsLogin) {
        this.editFbsLogin = editFbsLogin;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode[] getSelectedProjects() {
        return selectedProjects;
    }

    public void setSelectedProjects(TreeNode[] selectedProjects) {
        this.selectedProjects = selectedProjects;
    }
}