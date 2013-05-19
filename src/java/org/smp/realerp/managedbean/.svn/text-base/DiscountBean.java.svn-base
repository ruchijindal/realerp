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
import org.smp.realerp.entity.FbsDiscount;
import org.smp.realerp.manager.CompanyManager;

/**
 *
 * @author smp
 */
@ManagedBean(name = "discountBean")
@ViewScoped
public class DiscountBean {

    @EJB
    CompanyManager companyManager;
    FbsDiscount fbsDiscount = new FbsDiscount();
    List<FbsDiscount> fbsDiscountList = new ArrayList<FbsDiscount>();
    FbsDiscount editFbsDiscount = new FbsDiscount();
    FbsDiscount delFbsDiscount = new FbsDiscount();
    FbsCompany fbsCompany = new FbsCompany();
    boolean renderDialog;

    @PostConstruct
    public void populate() {
        fbsDiscountList.clear();
        fbsCompany = new FbsCompany();
        fbsCompany = companyManager.populateFbsCompany(LoginBean.fbsLogin.getFbsCompany().getCompanyId());
        fbsDiscountList = (List<FbsDiscount>) fbsCompany.getFbsDiscountCollection();

    }

    public void addDiscount() {
        boolean flag = companyManager.checkDiscountType(fbsDiscount.getDiscountType());
        if (flag = true) {
            fbsDiscount.setFbsCompany(LoginBean.fbsLogin.getFbsCompany());
            companyManager.setDiscount(fbsDiscount);
            fbsDiscount = new FbsDiscount();
            populate();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Discount  Added", ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Discount already exist", ""));
        }

    }

    public void edit(FbsDiscount fbsDiscount) {
        editFbsDiscount = new FbsDiscount();
        editFbsDiscount = fbsDiscount;
        renderDialog = true;
    }

    public void editDiscount() {
        System.out.println("inside edit");
        companyManager.editDiscount(editFbsDiscount);
        populate();
        renderDialog = false;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Discount  Updated", ""));

    }

    public void confirmDeleteDiscount(FbsDiscount fbsDiscount) {
        delFbsDiscount = new FbsDiscount();
        delFbsDiscount = fbsDiscount;
    }

    public void deleteDiscount() {
        companyManager.deleteDiscount(delFbsDiscount);
        delFbsDiscount = new FbsDiscount();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Discount Deleted", ""));
        populate();
    }

    public FbsDiscount getFbsDiscount() {
        return fbsDiscount;
    }

    public void setFbsDiscount(FbsDiscount fbsDiscount) {
        this.fbsDiscount = fbsDiscount;
    }

    public List<FbsDiscount> getFbsDiscountList() {
        return fbsDiscountList;
    }

    public void setFbsDiscountList(List<FbsDiscount> fbsDiscountList) {
        this.fbsDiscountList = fbsDiscountList;
    }

    public FbsDiscount getEditFbsDiscount() {
        return editFbsDiscount;
    }

    public void setEditFbsDiscount(FbsDiscount editFbsDiscount) {
        this.editFbsDiscount = editFbsDiscount;
    }

    public boolean isRenderDialog() {
        return renderDialog;
    }

    public void setRenderDialog(boolean renderDialog) {
        this.renderDialog = renderDialog;
    }
}
