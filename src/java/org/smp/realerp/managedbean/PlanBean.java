/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.managedbean;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.smp.realerp.entity.FbsPlanname;
import org.smp.realerp.entity.FbsProject;
import org.smp.realerp.manager.ProjectManager;
import org.smp.realerp.session.FbsPlannameFacade;
import org.smp.realerp.session.FbsProjectFacade;

/**
 *
 * @author smp
 */
@Stateless
@LocalBean
@ManagedBean(name = "planBean")
@ViewScoped
public class PlanBean {

    @EJB
    ProjectManager projectManager;
    @EJB
    FbsProjectFacade fbsProjectFacade;
    @EJB
    FbsPlannameFacade fbsPlannameFacade;
    FbsPlanname fbsPlanname = new FbsPlanname();
    List<FbsPlanname> fbsPlannamesList = new ArrayList<FbsPlanname>();
    FbsPlanname editFbsPlanname = new FbsPlanname();
    static FbsPlanname delFbsPlanname = new FbsPlanname();
    FbsProject fbsProject = new FbsProject();
    boolean renderDialog;

    @PostConstruct
    public void populate() {
        fbsPlannamesList.clear();
        fbsProject = new FbsProject();
        fbsProject = projectManager.populateFbsProject(LoginBean.fbsProject.getProjId());
        fbsPlannamesList = (List<FbsPlanname>) fbsProject.getFbsPlannameCollection();

    }

    public void addPlanname() {
        System.out.println("");
        fbsPlanname.setFbsProject(LoginBean.fbsProject);

        projectManager.createPlanname(fbsPlanname);
        fbsPlanname = new FbsPlanname();
        populate();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success:", "Congrates! Plan Successfully Added"));
    }

    public void edit(FbsPlanname fbsPlanname) {
        editFbsPlanname = new FbsPlanname();
        editFbsPlanname = fbsPlanname;
        renderDialog = true;
    }

    public void editPlanname() {
        projectManager.editPlanname(editFbsPlanname);
        renderDialog = false;
        populate();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success:", "Congrates! Plan Successfully Updated"));

    }

    public void confimDeletePlanname(FbsPlanname fbsPlanname) {
        delFbsPlanname = new FbsPlanname();
        delFbsPlanname = fbsPlanname;
    }

    public void deletePlanname() {
        projectManager.deletePlanname(delFbsPlanname);
        delFbsPlanname = new FbsPlanname();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success:", "Plan Name Deleted"));
        populate();
    }

    public FbsPlanname getFbsPlanname() {
        return fbsPlanname;
    }

    public static void setDelfFbsPlanname(FbsPlanname delfFbsPlanname) {
        PlanBean.delFbsPlanname = delfFbsPlanname;
    }

    public List<FbsPlanname> getFbsPlannamesList() {
        return fbsPlannamesList;
    }

    public void setFbsPlannamesList(List<FbsPlanname> fbsPlannamesList) {
        this.fbsPlannamesList = fbsPlannamesList;
    }

    public FbsPlanname getEditFbsPlanname() {
        return editFbsPlanname;
    }

    public void setEditFbsPlanname(FbsPlanname editFbsPlanname) {
        this.editFbsPlanname = editFbsPlanname;
    }

    public boolean isRenderDialog() {
        return renderDialog;
    }

    public void setRenderDialog(boolean renderDialog) {
        this.renderDialog = renderDialog;
    }
}
