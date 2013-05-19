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
import javax.faces.model.SelectItem;
import org.smp.realerp.entity.FbsPayplan;
import org.smp.realerp.entity.FbsPlanname;
import org.smp.realerp.entity.FbsProject;
import org.smp.realerp.manager.ProjectManager;
import org.smp.realerp.session.FbsPayplanFacade;
import org.smp.realerp.session.FbsPlannameFacade;

/**
 *
 * @author smp
 */
@ManagedBean(name = "payPlanBean")
@ViewScoped
public class PayPlanBean {

    @EJB
    ProjectManager projectManager;
    @EJB
    FbsPlannameFacade fbsPlannameFacade;
    @EJB
    FbsPayplanFacade fbsPayplanFacade;
    FbsPayplan fbsPayplan = new FbsPayplan();
    FbsPlanname fbsPlanname = new FbsPlanname();
    List<FbsPayplan> fbsPayPlanList = new ArrayList<FbsPayplan>();
    FbsPayplan ediFbsPayplan = new FbsPayplan();
    List<FbsPlanname> planNameList = new ArrayList<FbsPlanname>();
    FbsPayplan delFbsPayplan = new FbsPayplan();
    FbsProject fbsProject = new FbsProject();
    boolean renderDialog;

    @PostConstruct
    public void populate() {
        fbsPayPlanList.clear();
        planNameList.clear();
        fbsProject = new FbsProject();
        fbsProject = projectManager.populateFbsProject(LoginBean.fbsProject.getProjId());
        planNameList = (List<FbsPlanname>) fbsProject.getFbsPlannameCollection();
        for (int i = 0; i < planNameList.size(); i++) {
            List<FbsPayplan> payPlanList = (List<FbsPayplan>) planNameList.get(i).getFbsPayplanCollection();
            fbsPayPlanList.addAll(payPlanList);
        }


    }

    public void addPaymentPlan() {
        fbsPlanname.setFbsProject(LoginBean.fbsProject);
        fbsPayplan.setFbsPlanname(fbsPlanname);
        projectManager.addPaymentPlan(fbsPayplan);
        fbsPayplan = new FbsPayplan();
        populate();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, " Payment Plan  Added", ""));

    }

    public void edit(FbsPayplan fbsPayplan) {
        ediFbsPayplan = new FbsPayplan();
        ediFbsPayplan = fbsPayplan;
        renderDialog = true;
        fbsPlanname = new FbsPlanname();
        fbsPlanname = fbsPlannameFacade.find(fbsPayplan.getFbsPlanname().getPlanId());
    }

    public void editPaymentPlan() {
        fbsPlanname = fbsPlannameFacade.find(fbsPlanname.getPlanId());
        System.out.println("planname id is........" + fbsPlanname.getPlanId());
        ediFbsPayplan.setFbsPlanname(fbsPlanname);
        projectManager.editPaymentPlane(ediFbsPayplan);
        populate();
        renderDialog = false;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, " Payment Plan  Updated", ""));
    }

    public void confirmDeletePaymentPlan(FbsPayplan fbsPayplan) {
        delFbsPayplan = new FbsPayplan();
        delFbsPayplan = fbsPayplan;
    }

    public void deletePaymentPlan() {
        projectManager.deletePaymentPlan(delFbsPayplan);
        delFbsPayplan = new FbsPayplan();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment Plan Deleted", ""));
        populate();
    }

    public List<FbsPayplan> getFbsPayPlanList() {
        return fbsPayPlanList;
    }

    public void setFbsPayPlanList(List<FbsPayplan> fbsPayPlanList) {
        this.fbsPayPlanList = fbsPayPlanList;
    }

    public FbsPayplan getFbsPayplan() {
        return fbsPayplan;
    }

    public void setFbsPayplan(FbsPayplan fbsPayplan) {
        this.fbsPayplan = fbsPayplan;
    }

    public FbsPlanname getFbsPlanname() {
        return fbsPlanname;
    }

    public void setFbsPlanname(FbsPlanname fbsPlanname) {
        this.fbsPlanname = fbsPlanname;
    }

    public List<FbsPlanname> getPlanNameList() {
        return planNameList;
    }

    public void setPlanNameList(List<FbsPlanname> planNameList) {
        this.planNameList = planNameList;
    }

    public FbsPayplan getEdiFbsPayplan() {
        return ediFbsPayplan;
    }

    public void setEdiFbsPayplan(FbsPayplan ediFbsPayplan) {
        this.ediFbsPayplan = ediFbsPayplan;
    }

    public boolean isRenderDialog() {
        return renderDialog;
    }

    public void setRenderDialog(boolean renderDialog) {
        this.renderDialog = renderDialog;
    }
}
