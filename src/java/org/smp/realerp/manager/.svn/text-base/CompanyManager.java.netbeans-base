/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.manager;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.smp.realerp.entity.*;
import org.smp.realerp.managedbean.LoginBean;
import org.smp.realerp.session.*;

/**
 *
 * @author smp
 */
@Stateless
public class CompanyManager {

    @PersistenceContext(unitName = "RealErpPU")
    EntityManager em;
    @EJB
    FbsCompanyFacade fbsCompanyFacade;
    @EJB
    FbsUserFacade fbsUserFacade;
    @EJB
    FbsLoginFacade fbsLoginFacade;
    @EJB
    FbsBankFacade fbsBankFacade;
    @EJB
    FbsDiscountFacade fbsDiscountFacade;
    @EJB
    FbsInterestFacade fbsInterestFacade;
    @EJB
    FbsServicetaxFacade fbsServicetaxFacade;
    @EJB
    FbsProjectFacade fbsProjectFacade;

    /**
     * ******For Register a new Company**********
     */
    public void createCompany(FbsCompany fbsCompany, FbsLogin fbsLogin) {
        try {
            String userRoleXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?> <role_authorize><company><right>Company Setting</right><right>Booking</right><right>Add Booking</right><right>View Booking</right><right>Authorize Booking</right><right>Payment</right><right>Add Payment</right><right>View Paymnet</right><right>Authorize Payment</right><right>Broker</right><right>Add Broker Payment</right><right>View Broker Payment</right><right>Authorize Broker Payemnt</right><right>Project</right><right>Add Project</right><right>View Project</right><right>Authorize Project</right><right>Complain</right><right>Add Complain</right><right>View Complain</right><right>Resolve Complain</right></company></role_authorize>";
            System.out.println("inside company manager");
            if (LoginBean.adminLogin != null && LoginBean.adminLogin.getLoginId() != null) {
                fbsCompany.setStatus("activate");
            } else {
                fbsCompany.setStatus("deactivate");
            }
            fbsCompany.setMaxFlats(0);
            fbsCompany.setMaxProjects(0);
            fbsCompanyFacade.create(fbsCompany);
            fbsLogin.setTelPhone(fbsCompany.getTelNumber());
            fbsLogin.setMobile(fbsCompany.getMobile());
            fbsLogin.setAddress(fbsCompany.getAddress());
            fbsLogin.setFbsCompany(fbsCompany);
            fbsLogin.setEmail(fbsCompany.getEmail());
            fbsLogin.setWebsite(fbsCompany.getWebsite());
            FbsUser fbsUser = new FbsUser();
            fbsUser.setFbsCompany(fbsCompany);
            fbsUser.setRoleName("Super Admin");
            fbsUser.setRoleArv("SA");
            fbsUser.setXmlFile(userRoleXML);
            fbsUserFacade.create(fbsUser);
            fbsLogin.setFbsUser(fbsUser);
            fbsLoginFacade.create(fbsLogin);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FbsCompany populateFbsCompany(int companyId) {
        return fbsCompanyFacade.find(companyId);
    }

    public void editCompanyInfo(FbsCompany fbsCompany) {
        try {
            fbsCompanyFacade.edit(fbsCompany);
        } catch (Exception ex) {
            System.out.println("Exception in editing company info " + ex);
        }
    }

    /**
     * ******For bank settings***********
     */
    public void createBank(FbsBank fbsBank) {
        try {
//            fbsBank.setFbsCompany((FbsCompany)session.getAttribute("company"));
            fbsBankFacade.create(fbsBank);
        } catch (Exception ex) {
            System.out.println("Exception in create bank " + ex);
        }

    }

    public void editBank(FbsBank fbsBank) {
        try {
            fbsBankFacade.edit(fbsBank);
        } catch (Exception ex) {
            System.out.println("Exception in bank editing " + ex);
        }
    }

    public void deleteBank(FbsBank fbsBank) {
        try {
            fbsBankFacade.remove(fbsBank);
        } catch (Exception ex) {
            System.out.println("Exception in delete bank " + ex);
        }
    }

    public boolean checkBankName(String bankName) {
        List<FbsBank> fbsBanks = em.createNamedQuery("FbsBank.findByBankName").setParameter("bankName", bankName).getResultList();
        if (fbsBanks.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ******For Discount settings***********
     */
    public void setDiscount(FbsDiscount fbsDiscount) {
        try {

            fbsDiscountFacade.create(fbsDiscount);
        } catch (Exception ex) {
            System.out.println("Exception insert discount " + ex);
        }

    }

    public void editDiscount(FbsDiscount fbsDiscount) {
        try {
            fbsDiscountFacade.edit(fbsDiscount);
        } catch (Exception ex) {
            System.out.println("Exception in discount editing " + ex);
        }
    }

    public void deleteDiscount(FbsDiscount fbsDiscount) {
        try {
            fbsDiscountFacade.remove(fbsDiscount);
        } catch (Exception ex) {
            System.out.println("Exception in delete discount " + ex);
        }
    }

    public boolean checkDiscountType(String discountType) {
        List<FbsDiscount> fbsDiscounts = em.createNamedQuery("FbsDiscount.findByDiscountType").setParameter("discountType", discountType).getResultList();
        if (fbsDiscounts.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ******For Interest settings***********
     */
    public void addInterest(FbsInterest fbsInterest) {
        try {
            fbsInterestFacade.create(fbsInterest);
        } catch (Exception ex) {
            System.out.println("Exception insert interest " + ex);
        }

    }

    public void editInterest(FbsInterest fbsInterest) {
        try {
            fbsInterestFacade.edit(fbsInterest);
        } catch (Exception ex) {
            System.out.println("Exception in interest editing " + ex);
        }
    }

    public void deleteInterest(FbsInterest fbsInterest) {
        try {
            fbsInterestFacade.remove(fbsInterest);
        } catch (Exception ex) {
            System.out.println("Exception in delete discount " + ex);
        }
    }

    /**
     * ******For Service Tax settings***********
     */
    public void addServiceTax(FbsServicetax fbsServicetax) {
        try {
            fbsServicetaxFacade.create(fbsServicetax);
        } catch (Exception ex) {
            System.out.println("Exception insert service tax " + ex);
        }

    }

    public void editServiceTax(FbsServicetax fbsServicetax) {
        try {
            fbsServicetaxFacade.edit(fbsServicetax);
        } catch (Exception ex) {
            System.out.println("Exception in service tax editing " + ex);
        }
    }

    public void deleteServiceTax(FbsServicetax fbsServicetax) {
        try {
            fbsServicetaxFacade.remove(fbsServicetax);
        } catch (Exception ex) {
            System.out.println("Exception in delete service tax " + ex);
        }
    }

    /**
     * ***For Project Setting*****
     */
    public void addProject(FbsProject fbsProject) {
        try {
            fbsProjectFacade.create(fbsProject);
            //@NamedQuery(name = "FbsProject.findMaxProjId", query = "SELECT MAX(f.projId) FROM FbsProject f")
            Object projId = em.createNamedQuery("FbsProject.findMaxProjId").getResultList().get(0);
            fbsProject.setProjCode(LoginBean.fbsLogin.getFbsCompany().getCompanyId().toString() + projId);
            fbsProjectFacade.edit(fbsProject);

        } catch (Exception ex) {
            System.out.println("Exception insert project " + ex);
        }

    }

    public void editProject(FbsProject fbsProject) {
        try {
            fbsProjectFacade.edit(fbsProject);
        } catch (Exception ex) {
            System.out.println("Exception in project editing " + ex);
        }
    }

    public void deleteProject(FbsProject fbsProject) {
        try {
            fbsProjectFacade.remove(fbsProject);
        } catch (Exception ex) {
            System.out.println("Exception in delete project " + ex);
        }
    }

    public boolean checkProjectName(String projectName) {
        List<FbsProject> fbsProjects = em.createNamedQuery("FbsProject.findByProjName").setParameter("projName", projectName).getResultList();
        if (fbsProjects.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkProjectForDeletion(FbsProject fbsProject) {
        boolean flag = true;
        int projectId = 0;
        List<FbsBooking> fbsBookingList = em.createNamedQuery("FbsBooking.findByStatus").setParameter("status", "booked").getResultList();
        for (int i = 0; i < fbsBookingList.size(); i++) {
            projectId = fbsBookingList.get(i).getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getProjId();
            if (projectId == fbsProject.getProjId()) {
                flag = false;
                break;
            }
        }
        return flag;
    }
}
