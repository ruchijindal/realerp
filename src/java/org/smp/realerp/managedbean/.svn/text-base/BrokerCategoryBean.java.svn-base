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
import org.smp.realerp.entity.FbsBrokerCat;
import org.smp.realerp.entity.FbsCompany;
import org.smp.realerp.manager.BrokerManager;
import org.smp.realerp.manager.CompanyManager;
import org.smp.realerp.session.FbsBrokerCatFacade;

/**
 *
 * @author smp
 */
@ManagedBean(name = "brokerCategoryBean")
@ViewScoped
public class BrokerCategoryBean {

    @EJB
    BrokerManager brokerManager;
    @EJB
    CompanyManager companyManager;
    @EJB
    FbsBrokerCatFacade fbsBrokerCatFacade;
    List<FbsBrokerCat> fbsBrokerCatList = new ArrayList<FbsBrokerCat>();
    FbsBrokerCat fbsBrokerCat = new FbsBrokerCat();
    public static FbsBrokerCat fbsBrokerCategory = null;
    FbsBrokerCat delFbsBrokerCat = new FbsBrokerCat();
    FbsBrokerCat ediFbsBrokerCat = new FbsBrokerCat();
    FbsCompany fbsCompany = new FbsCompany();
    FbsBrokerCat editFbsBrokerCat = new FbsBrokerCat();
    boolean renderDialog;

    @PostConstruct
    public void populate() {
        fbsBrokerCatList.clear();
        fbsCompany = new FbsCompany();
        fbsCompany = companyManager.populateFbsCompany(LoginBean.fbsLogin.getFbsCompany().getCompanyId());
        fbsBrokerCatList = (List<FbsBrokerCat>) fbsCompany.getFbsBrokerCatCollection();
    }

    public void addBrokerCategory() {
        fbsBrokerCat.setFbsCompany(LoginBean.fbsLogin.getFbsCompany());
        brokerManager.createBrokerCategory(fbsBrokerCat);
        fbsBrokerCat = new FbsBrokerCat();
        populate();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, " Broker Category  Added", ""));

    }

    public void confirmDeleteBrokerCategory(FbsBrokerCat fbsBrokerCat) {
        delFbsBrokerCat = new FbsBrokerCat();
        delFbsBrokerCat = fbsBrokerCat;
    }

    public void deleteBrokerCategory() {
        brokerManager.deleteBrokerCategory(delFbsBrokerCat);
        delFbsBrokerCat = new FbsBrokerCat();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Broker Category Deleted", ""));
        populate();

    }

    public void edit(FbsBrokerCat fbsBrokerCat) {
        editFbsBrokerCat = new FbsBrokerCat();
        editFbsBrokerCat = fbsBrokerCat;
        renderDialog = true;
    }

    public void editBrokerCategory() {
        //System.out.println("inside edit"+fbsBroker.getFbsBrokerCat().getCategoryId());         
        brokerManager.editBrokerCategory(editFbsBrokerCat);
        populate();
        renderDialog = false;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, " Broker Category  Updated", ""));

    }

    public FbsBrokerCat getFbsBrokerCat() {
        return fbsBrokerCat;
    }

    public void setFbsBrokerCat(FbsBrokerCat fbsBrokerCat) {
        this.fbsBrokerCat = fbsBrokerCat;
    }

    public List<FbsBrokerCat> getFbsBrokerCatList() {
        return fbsBrokerCatList;
    }

    public void setFbsBrokerCatList(List<FbsBrokerCat> fbsBrokerCatList) {
        this.fbsBrokerCatList = fbsBrokerCatList;
    }

    public FbsBrokerCat getEditFbsBrokerCat() {
        return editFbsBrokerCat;
    }

    public void setEditFbsBrokerCat(FbsBrokerCat editFbsBrokerCat) {
        this.editFbsBrokerCat = editFbsBrokerCat;
    }

    public static FbsBrokerCat getFbsBrokerCategory() {
        return fbsBrokerCategory;
    }

    public static void setFbsBrokerCategory(FbsBrokerCat fbsBrokerCategory) {
        BrokerCategoryBean.fbsBrokerCategory = fbsBrokerCategory;
    }

    public FbsBrokerCat getEdiFbsBrokerCat() {
        return ediFbsBrokerCat;
    }

    public void setEdiFbsBrokerCat(FbsBrokerCat ediFbsBrokerCat) {
        this.ediFbsBrokerCat = ediFbsBrokerCat;
    }

    public boolean isRenderDialog() {
        return renderDialog;
    }

    public void setRenderDialog(boolean renderDialog) {
        this.renderDialog = renderDialog;
    }
}
