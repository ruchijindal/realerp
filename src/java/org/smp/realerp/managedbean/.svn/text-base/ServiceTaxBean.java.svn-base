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
import org.smp.realerp.entity.FbsServicetax;
import org.smp.realerp.manager.CompanyManager;
import org.smp.realerp.session.FbsCompanyFacade;

/**
 *
 * @author smp
 */
@ManagedBean(name = "serviceTaxBean")
@ViewScoped
public class ServiceTaxBean {

    @EJB
    CompanyManager companyManager;
    @EJB
    FbsCompanyFacade fbsCompanyFacade;
    FbsServicetax fbsServicetax = new FbsServicetax();
    List<FbsServicetax> fbsServiceTaxList = new ArrayList<FbsServicetax>();
    FbsServicetax editFbsServiceTax = new FbsServicetax();
    FbsServicetax delFbsServicetax = new FbsServicetax();
    boolean renderDialog;
    FbsCompany fbsCompany = new FbsCompany();

    @PostConstruct
    public void populate() {
        fbsServiceTaxList.clear();
        fbsCompany=new FbsCompany();
        fbsCompany=fbsCompanyFacade.find(LoginBean.fbsLogin.getFbsCompany().getCompanyId());
        fbsServiceTaxList = (List<FbsServicetax>) fbsCompany.getFbsServicetaxCollection();

    }

    public void addServiceTax() {
        fbsServicetax.setFbsCompany(fbsCompany);
        companyManager.addServiceTax(fbsServicetax);
        fbsServicetax = new FbsServicetax();
        populate();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, " Service Tax  Added", ""));
    }

    public void edit(FbsServicetax fbsServicetax) {
        editFbsServiceTax = new FbsServicetax();
        editFbsServiceTax = fbsServicetax;
        renderDialog = true;
    }

    public void editServiceTax() {
        System.out.println("inside edit");
        companyManager.editServiceTax(editFbsServiceTax);
        populate();
        renderDialog = false;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, " Service Tax  Updated", ""));

    }

    public void confirmDeleteServiceTax(FbsServicetax fbsServicetax) {
        delFbsServicetax = new FbsServicetax();
        delFbsServicetax = fbsServicetax;
    }

    public void deleteServiceTax() {
        companyManager.deleteServiceTax(delFbsServicetax);
        delFbsServicetax = new FbsServicetax();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Service Tax Deleted", ""));
        populate();
    }

    public List<FbsServicetax> getFbsServiceTaxList() {
        return fbsServiceTaxList;
    }

    public void setFbsServiceTaxList(List<FbsServicetax> fbsServiceTaxList) {
        this.fbsServiceTaxList = fbsServiceTaxList;
    }

    public FbsServicetax getFbsServicetax() {
        return fbsServicetax;
    }

    public void setFbsServicetax(FbsServicetax fbsServicetax) {
        this.fbsServicetax = fbsServicetax;
    }

    public FbsServicetax getEditFbsServiceTax() {
        return editFbsServiceTax;
    }

    public void setEditFbsServiceTax(FbsServicetax editFbsServiceTax) {
        this.editFbsServiceTax = editFbsServiceTax;
    }

    public boolean isRenderDialog() {
        return renderDialog;
    }

    public void setRenderDialog(boolean renderDialog) {
        this.renderDialog = renderDialog;
    }
}
