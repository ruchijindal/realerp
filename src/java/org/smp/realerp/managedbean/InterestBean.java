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
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.smp.realerp.entity.FbsCompany;
import org.smp.realerp.entity.FbsInterest;
import org.smp.realerp.manager.CompanyManager;

/**
 *
 * @author smp
 */
@ManagedBean(name = "interestBean")
@ViewScoped
public class InterestBean {
    
    @EJB
    CompanyManager companyManager;
    FbsInterest fbsInterest = new FbsInterest();
    List<FbsInterest> fbsInterestList = new ArrayList<FbsInterest>();
    FbsInterest editFbsInterest = new FbsInterest();
    FbsInterest delFbsInterest = new FbsInterest();
    FbsCompany fbsCompany = new FbsCompany();
    boolean renderDialog;
    
    @PostConstruct
    public void populate() {
        fbsInterestList.clear();
        fbsCompany = new FbsCompany();
        fbsCompany = companyManager.populateFbsCompany(LoginBean.fbsLogin.getFbsCompany().getCompanyId());
        fbsInterestList = (List<FbsInterest>) fbsCompany.getFbsInterestCollection();
        
    }
    
    public void addInterest() {
        fbsInterest.setFbsCompany(LoginBean.fbsLogin.getFbsCompany());
        companyManager.addInterest(fbsInterest);
        fbsInterest = new FbsInterest();
        populate();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Interest  Added", ""));
    }
    
    public void edit(FbsInterest fbsInterest) {
        editFbsInterest = new FbsInterest();
        editFbsInterest = fbsInterest;
        renderDialog = true;
    }
    
    public void editInterest() {        
        System.out.println("inside edit");
        companyManager.editInterest(editFbsInterest);
        populate();
        renderDialog = false;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, " Interest Updated", ""));
        
    }
    
    public void confirmDeleteInterest(FbsInterest fbsInterest) {
        delFbsInterest = new FbsInterest();
        delFbsInterest = fbsInterest;
    }
    
    public void deleteInterest() {
        companyManager.deleteInterest(delFbsInterest);
        delFbsInterest = new FbsInterest();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Interest Deleted", ""));
        populate();
    }
    
    public FbsInterest getFbsInterest() {
        return fbsInterest;
    }
    
    public void setFbsInterest(FbsInterest fbsInterest) {
        this.fbsInterest = fbsInterest;
    }
    
    public List<FbsInterest> getFbsInterestList() {
        return fbsInterestList;
    }
    
    public void setFbsInterestList(List<FbsInterest> fbsInterestList) {
        this.fbsInterestList = fbsInterestList;
    }
    
    public FbsInterest getEditFbsInterest() {
        return editFbsInterest;
    }
    
    public void setEditFbsInterest(FbsInterest editFbsInterest) {
        this.editFbsInterest = editFbsInterest;
    }
    
    public boolean isRenderDialog() {
        return renderDialog;
    }
    
    public void setRenderDialog(boolean renderDialog) {
        this.renderDialog = renderDialog;
    }
}
