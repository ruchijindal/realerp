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
import org.smp.realerp.entity.FbsBank;
import org.smp.realerp.entity.FbsCompany;
import org.smp.realerp.manager.CompanyManager;

/**
 *
 * @author smp
 */
@ManagedBean(name = "bankBean")
@ViewScoped
public class BankBean {

    @EJB
    CompanyManager companyManager;
    FbsBank fbsBank = new FbsBank();
    List<FbsBank> fbsBankList = new ArrayList<FbsBank>();
    FbsBank editFbsBank = new FbsBank();
    FbsBank delFbsBank = new FbsBank();
    FbsCompany fbsCompany = new FbsCompany();
    boolean renderDialog;

    @PostConstruct
    public void populate() {
        fbsBankList.clear();
        fbsCompany = new FbsCompany();
        fbsCompany = companyManager.populateFbsCompany(LoginBean.fbsLogin.getFbsCompany().getCompanyId());
        fbsBankList = (List<FbsBank>) fbsCompany.getFbsBankCollection();
        boolean renderDialog;

    }

    public void addBank() {
        boolean flag = companyManager.checkBankName(fbsBank.getBankName());
        if (flag == true) {
            fbsBank.setFbsCompany(LoginBean.fbsLogin.getFbsCompany());
            companyManager.createBank(fbsBank);
            fbsBank = new FbsBank();
            populate();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bank  Added", ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bank name already exit", ""));
        }
    }

    public void edit(FbsBank fbsBank) {
        editFbsBank = new FbsBank();
        editFbsBank = fbsBank;
        renderDialog = true;
    }

    public void editBank() {
        System.out.println("inside edit");
        companyManager.editBank(editFbsBank);
        populate();
        renderDialog = false;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bank  Updated", ""));

    }

    public void confirmDeleteBank(FbsBank fbsBank) {
        delFbsBank = new FbsBank();
        delFbsBank = fbsBank;
    }

    public void deleteBank() {
        companyManager.deleteBank(delFbsBank);
        delFbsBank = new FbsBank();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bank Deleted", ""));
        populate();
    }

    public FbsBank getFbsBank() {
        return fbsBank;
    }

    public void setFbsBank(FbsBank fbsBank) {
        this.fbsBank = fbsBank;
    }

    public List<FbsBank> getFbsBankList() {
        return fbsBankList;
    }

    public void setFbsBankList(List<FbsBank> fbsBankList) {
        this.fbsBankList = fbsBankList;
    }

    public FbsBank getEditFbsBank() {
        return editFbsBank;
    }

    public void setEditFbsBank(FbsBank editFbsBank) {
        this.editFbsBank = editFbsBank;
    }

    public boolean isRenderDialog() {
        return renderDialog;
    }

    public void setRenderDialog(boolean renderDialog) {
        this.renderDialog = renderDialog;
    }
}
