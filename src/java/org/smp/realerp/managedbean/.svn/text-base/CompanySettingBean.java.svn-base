/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.managedbean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.smp.realerp.entity.FbsCompany;
import org.smp.realerp.entity.FbsLoan;
import org.smp.realerp.entity.FbsLogin;
import org.smp.realerp.manager.CompanyManager;
import org.smp.realerp.manager.LoginManager;
import org.smp.realerp.manager.UserManager;
import org.smp.realerp.pojo.companyAuthorizeMail;
import org.smp.realerp.session.FbsCompanyFacade;

/**
 *
 * @author smp
 */
@ManagedBean(name = "companySettingBean")
@ViewScoped
public class CompanySettingBean {

    @PersistenceContext(unitName = "RealErpPU")
    EntityManager em;
    @EJB
    CompanyManager companyManager;
    @EJB
    UserManager userManager;
    @EJB
    FbsCompanyFacade fbsCompanyFacade;
    @EJB
    LoginManager loginManager;
    FbsCompany fbsCompany = new FbsCompany();
    FbsLogin fbsLogin = new FbsLogin();
    List<FbsCompany> fbsCompanyList = new ArrayList<FbsCompany>();
    boolean renderDialog;
    FbsCompany delFbsCompany = new FbsCompany();
    boolean rendetDataTable;
    Integer maxProjects;
    Integer maxUnits;
    FbsLogin adminLogin = new FbsLogin();
    boolean renderPassword;

    @PostConstruct
    public void populate() throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        httpServletResponse.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        httpServletResponse.setDateHeader("Expires", 0); // Proxies.
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (request.getParameter("requestType").equalsIgnoreCase("admin")) {
            renderPassword = false;
            if (LoginBean.adminLogin == null || LoginBean.adminLogin.getLoginId() == null) {
                System.out.println("insde if");
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/index.xhtml");
            } else {
                System.out.println("insde else");
                fbsCompanyList.clear();
                fbsCompanyList = fbsCompanyFacade.findAll();
                rendetDataTable = true;
                adminLogin = LoginBean.adminLogin;
            }
        } else {
            renderPassword = true;
        }
    }

    public void registerCompany() {
        if (userManager.chechUniqueUserId(fbsLogin) == false) {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//        String ipAdress = httpServletRequest.getRemoteAddr();
            String randomPassword = "";
            if (renderPassword == false) {
                randomPassword = loginManager.getAlphaNumeric(8);
                fbsLogin.setPassword(loginManager.convertPassword(randomPassword));
            } else {
                fbsLogin.setPassword(loginManager.convertPassword(fbsLogin.getPassword()));
            }
            fbsLogin.setCreatedBy(LoginBean.adminLogin.getUserId());
            companyManager.createCompany(fbsCompany, fbsLogin);
            if (LoginBean.adminLogin != null && LoginBean.adminLogin.getLoginId() != null) {
                fbsLogin.setPassword(randomPassword);
                companyAuthorizeMail sendMail = new companyAuthorizeMail(fbsCompany.getEmail(), fbsCompany, fbsLogin);
                Thread thread = new Thread(sendMail);
                thread.start();
            }
            fbsCompany = new FbsCompany();
            fbsLogin = new FbsLogin();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Company Added", ""));
            fbsCompanyList.clear();
            fbsCompanyList = fbsCompanyFacade.findAll();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User Id already exist", ""));
        }
    }

    public void confirmStatusChange(FbsCompany editFbsCompany) {
        fbsCompany = new FbsCompany();
        fbsCompany = editFbsCompany;

    }

    public void changeStatusOfCompany() throws IOException {
        if (fbsCompany.getStatus().equalsIgnoreCase("deactivate")) {
            fbsCompany.setStatus("activate");
            List<FbsLogin> fbsLoginList = new ArrayList<FbsLogin>();
            fbsLoginList = (List<FbsLogin>) fbsCompany.getFbsLoginCollection();
            FbsLogin fbsLogin = new FbsLogin();
            for (int i = 0; i < fbsLoginList.size(); i++) {
                if (fbsLoginList.get(i).getFbsUser().getRoleName().equalsIgnoreCase("Super Admin")) {
                    fbsLogin = fbsLoginList.get(i);
                    break;
                }
            }

            companyAuthorizeMail sendMail = new companyAuthorizeMail(fbsCompany.getEmail(), fbsCompany, fbsLogin);
            Thread thread = new Thread(sendMail);
            thread.start();

        } else if (fbsCompany.getStatus().equalsIgnoreCase("activate")) {
            fbsCompany.setStatus("deactivate");
        }
        fbsCompanyFacade.edit(fbsCompany);
        fbsCompanyList.clear();
        fbsCompanyList = fbsCompanyFacade.findAll();
        LoginBean.fbsCompanyList.clear();
        LoginBean.fbsCompanyList = em.createNamedQuery("FbsCompany.findByStatus").setParameter("status", "activate").getResultList();

    }

    public void confirmEditCompanyInformation(FbsCompany editFbsCompany) {

        fbsCompany = new FbsCompany();
        fbsCompany = editFbsCompany;
        renderDialog = true;
    }

    public void editCompanyInformation() throws IOException {
        companyManager.editCompanyInfo(fbsCompany);
        renderDialog = false;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Company  Updated", ""));
        fbsCompanyList.clear();
        fbsCompanyList = fbsCompanyFacade.findAll();
    }

    public void confirmDeleteCompany(FbsCompany fbsCompany) {
        delFbsCompany = new FbsCompany();
        delFbsCompany = fbsCompany;
    }

    public void deleteCompany() throws IOException {
        fbsCompanyFacade.remove(delFbsCompany);
        delFbsCompany = new FbsCompany();
        fbsCompanyList.clear();
        fbsCompanyList = fbsCompanyFacade.findAll();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Company  Deleted", ""));

    }

    public void reset() {
        fbsCompany = new FbsCompany();
        fbsCompanyList.clear();
        fbsCompanyList = fbsCompanyFacade.findAll();
        renderDialog = false;

    }

    public void addCompanySettings() {
        fbsCompany = fbsCompanyFacade.find(fbsCompany.getCompanyId());
        fbsCompany.setMaxProjects(maxProjects);
        fbsCompany.setMaxFlats(maxUnits);
        fbsCompanyFacade.edit(fbsCompany);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Company Updated", ""));
        resetSettings();

    }

    public void resetSettings() {
        fbsCompany = new FbsCompany();
        fbsCompanyList.clear();
        fbsCompanyList = fbsCompanyFacade.findAll();
        renderDialog = false;
        maxProjects = null;
        maxUnits = null;
    }

    public void confirmEditCompanySetting(FbsCompany editFbsCompany) {

        fbsCompany = new FbsCompany();
        fbsCompany = editFbsCompany;
        renderDialog = true;
    }

    public void editCompanySetting() throws IOException {
        fbsCompanyFacade.edit(fbsCompany);
        renderDialog = false;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Company  Updated", ""));
        resetSettings();
    }

    public void renderUserName() {
        fbsLogin.setUserName(fbsCompany.getCompanyName());
    }

    public FbsCompany getFbsCompany() {
        return fbsCompany;
    }

    public void setFbsCompany(FbsCompany fbsCompany) {
        this.fbsCompany = fbsCompany;
    }

    public FbsLogin getFbsLogin() {
        return fbsLogin;
    }

    public void setFbsLogin(FbsLogin fbsLogin) {
        this.fbsLogin = fbsLogin;
    }

    public List<FbsCompany> getFbsCompanyList() {
        return fbsCompanyList;
    }

    public void setFbsCompanyList(List<FbsCompany> fbsCompanyList) {
        this.fbsCompanyList = fbsCompanyList;
    }

    public boolean isRenderDialog() {
        return renderDialog;
    }

    public void setRenderDialog(boolean renderDialog) {
        this.renderDialog = renderDialog;
    }

    public boolean isRendetDataTable() {
        return rendetDataTable;
    }

    public void setRendetDataTable(boolean rendetDataTable) {
        this.rendetDataTable = rendetDataTable;
    }

    public Integer getMaxProjects() {
        return maxProjects;
    }

    public void setMaxProjects(Integer maxProjects) {
        this.maxProjects = maxProjects;
    }

    public Integer getMaxUnits() {
        return maxUnits;
    }

    public void setMaxUnits(Integer maxUnits) {
        this.maxUnits = maxUnits;
    }

    public FbsLogin getAdminLogin() {
        return adminLogin;
    }

    public void setAdminLogin(FbsLogin adminLogin) {
        this.adminLogin = adminLogin;
    }

    public boolean isRenderPassword() {
        return renderPassword;
    }

    public void setRenderPassword(boolean renderPassword) {
        this.renderPassword = renderPassword;
    }
}
