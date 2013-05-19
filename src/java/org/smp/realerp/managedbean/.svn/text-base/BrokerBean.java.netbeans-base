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
import org.smp.realerp.entity.FbsBroker;
import org.smp.realerp.entity.FbsBrokerCat;
import org.smp.realerp.entity.FbsCompany;
import org.smp.realerp.entity.FbsUser;
import org.smp.realerp.manager.BrokerManager;
import org.smp.realerp.manager.CompanyManager;
import org.smp.realerp.session.FbsBrokerCatFacade;

/**
 *
 * @author smp
 */
@ManagedBean(name = "brokerBean")
@ViewScoped
public class BrokerBean {

    @EJB
    BrokerManager brokerManager;
    @EJB
    CompanyManager companyManager;
    @EJB
    FbsBrokerCatFacade fbsBrokerCatFacade;
    FbsUser fbsUser;
    FbsBroker fbsBroker = new FbsBroker();
    FbsBrokerCat fbsBrokerCat = new FbsBrokerCat();
    List<FbsBroker> brokerList = new ArrayList();
    List<FbsBrokerCat> brokerCatList = new ArrayList();
    FbsBroker editFbsBroker = new FbsBroker();
    FbsBroker delFbsBroker = new FbsBroker();
    FbsCompany fbsCompany = new FbsCompany();
    boolean renderDialog;

    //****For Populating the Broker List****//
    @PostConstruct
    public void populate() {
        brokerList.clear();
        brokerCatList.clear();
        fbsCompany = new FbsCompany();
        fbsCompany = companyManager.populateFbsCompany(LoginBean.fbsLogin.getFbsCompany().getCompanyId());
        brokerList = (List<FbsBroker>) fbsCompany.getFbsBrokerCollection();
        brokerCatList = (List<FbsBrokerCat>) fbsCompany.getFbsBrokerCatCollection();

    }

    //*****For adding a new Broker***//
    public void addBroker() {
        // System.out.println("company id id>>>>>>"+LoginBean.fbsLogin.getFbsCompany());
        fbsBroker.setFbsCompany(LoginBean.fbsLogin.getFbsCompany());
        // fbsBroker.setFbsBrokerCat(BrokerCategoryBean.);
        fbsBroker.setFbsBrokerCat(fbsBrokerCatFacade.find(fbsBrokerCat.getCategoryId()));
        brokerManager.addBroker(fbsBroker);
        fbsBroker = new FbsBroker();
        fbsBrokerCat = new FbsBrokerCat();
        populate();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success:", "Congrates! Borker Successfully Added"));

    }

    //****For deleting the Broker***//
    public void confirmDeleteBroker(FbsBroker fbsBroker) {
        delFbsBroker = new FbsBroker();
        delFbsBroker = fbsBroker;
    }

    public void deleteBroker() {
        brokerManager.deleteBroker(delFbsBroker);
        delFbsBroker = new FbsBroker();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success:", "Broker Deleted"));
        populate();

    }

    public void edit(FbsBroker fbsBroker) {
        editFbsBroker = new FbsBroker();
        editFbsBroker = fbsBroker;
        renderDialog = true;
        fbsBrokerCat = new FbsBrokerCat();
        fbsBrokerCat = fbsBrokerCatFacade.find(fbsBroker.getFbsBrokerCat().getCategoryId());
    }

    public void editBroker() {
        System.out.println("inside edit.........");
        fbsBrokerCat = fbsBrokerCatFacade.find(fbsBrokerCat.getCategoryId());
        System.out.println("valus inside edit is....." + fbsBrokerCat.getCategoryName());
        editFbsBroker.setFbsBrokerCat(fbsBrokerCat);
        brokerManager.editBroker(editFbsBroker);
        populate();
        renderDialog = false;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Congrates! Broker  Updated", ""));

    }

    public FbsBroker getFbsBroker() {
        return fbsBroker;
    }

    public void setFbsBroker(FbsBroker fbsBroker) {
        this.fbsBroker = fbsBroker;
    }

    public FbsBrokerCat getFbsBrokerCat() {
        return fbsBrokerCat;
    }

    public void setFbsBrokerCat(FbsBrokerCat fbsBrokerCat) {
        this.fbsBrokerCat = fbsBrokerCat;
    }

    public List<FbsBroker> getBrokerList() {
        return brokerList;
    }

    public void setBrokerList(List<FbsBroker> brokerList) {
        this.brokerList = brokerList;
    }

    public List<FbsBrokerCat> getBrokerCatList() {
        return brokerCatList;
    }

    public void setBrokerCatList(List<FbsBrokerCat> brokerCatList) {
        this.brokerCatList = brokerCatList;
    }

    public FbsBroker getEditFbsBroker() {
        return editFbsBroker;
    }

    public void setEditFbsBroker(FbsBroker editFbsBroker) {
        this.editFbsBroker = editFbsBroker;
    }

    public boolean isRenderDialog() {
        return renderDialog;
    }

    public void setRenderDialog(boolean renderDialog) {
        this.renderDialog = renderDialog;
    }
}
