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
import org.smp.realerp.entity.FbsCharge;
import org.smp.realerp.entity.FbsProject;
import org.smp.realerp.manager.ProjectManager;

/**
 *
 * @author smp
 */
@ManagedBean(name = "chargeBean")
@ViewScoped
public class ChargeBean {

    @EJB
    ProjectManager projectManager;
    FbsCharge fbsCharge = new FbsCharge();
    List<FbsCharge> chargeList = new ArrayList<FbsCharge>();
    FbsCharge editFbsCharge = new FbsCharge();
    static FbsCharge delFbsCharge = new FbsCharge();
    FbsProject fbsProject = new FbsProject();
    boolean renderDialog;

    @PostConstruct
    public void populate() {
        chargeList.clear();
        fbsProject = new FbsProject();
        fbsProject = projectManager.populateFbsProject(LoginBean.fbsProject.getProjId());
        chargeList = (List<FbsCharge>) fbsProject.getFbsChargeCollection();
    }

    public void addCharges() {
        boolean flag = projectManager.checkChargeName(fbsCharge.getChargeName());
        if (flag = true) {
            fbsCharge.setFbsProject(LoginBean.fbsProject);
            projectManager.addCharges(fbsCharge);
            fbsCharge = new FbsCharge();
            populate();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, " Charges  Added", ""));

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure:", " Charge Name already exist"));
        }
    }

    public void confirmDeleteCharges(FbsCharge fbsCharge) {
        delFbsCharge = new FbsCharge();
        delFbsCharge = fbsCharge;
    }

    public void deleteChagres() {
        projectManager.deleteCharges(delFbsCharge);
        delFbsCharge = new FbsCharge();
        populate();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success:", "Charge Deleted"));

    }

    public void edit(FbsCharge fbsCharge) {
        editFbsCharge = new FbsCharge();
        editFbsCharge = fbsCharge;
        renderDialog = true;
    }

    public void editCharges() {
        projectManager.editCharges(editFbsCharge);
        renderDialog = false;
        populate();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, " Charges  Updated", ""));
    }

    public List<FbsCharge> getChargeList() {
        return chargeList;
    }

    public void setChargeList(List<FbsCharge> chargeList) {
        this.chargeList = chargeList;
    }

    public static FbsCharge getDelFbsCharge() {
        return delFbsCharge;
    }

    public static void setDelFbsCharge(FbsCharge delFbsCharge) {
        ChargeBean.delFbsCharge = delFbsCharge;
    }

    public FbsCharge getFbsCharge() {
        return fbsCharge;
    }

    public void setFbsCharge(FbsCharge fbsCharge) {
        this.fbsCharge = fbsCharge;
    }

    public FbsCharge getEditFbsCharge() {
        return editFbsCharge;
    }

    public void setEditFbsCharge(FbsCharge editFbsCharge) {
        this.editFbsCharge = editFbsCharge;
    }

    public boolean isRenderDialog() {
        return renderDialog;
    }

    public void setRenderDialog(boolean renderDialog) {
        this.renderDialog = renderDialog;
    }
}
