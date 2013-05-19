/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.managedbean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import org.smp.realerp.entity.FbsApplicant;
import org.smp.realerp.entity.FbsBooking;
import org.smp.realerp.manager.BlockManager;
import org.smp.realerp.manager.BookingManager;
import org.smp.realerp.session.FbsBookingFacade;

/**
 *
 * @author smp
 */
@ManagedBean(name = "transferBookingBean")
@ViewScoped
public class TransferBookingBean {

    @PersistenceContext(unitName = "RealErpPU")
    private EntityManager em;
    @EJB
    BookingManager bookingManager;
    @EJB
    FbsBookingFacade fbsBookingFacade;
    FbsBooking fbsBooking = new FbsBooking();
    FbsApplicant fbsApplicant = new FbsApplicant();
    FbsApplicant fbsCoApplicant = new FbsApplicant();
    @EJB
    BlockManager blockManager;
    String rFlag;
    String renderFlag;
    List<FbsApplicant> fbsapplicantlist = new ArrayList<FbsApplicant>();
    boolean renderDetail;

    @PostConstruct
    public void populate() {

        int regNum = 0;
        fbsBooking = new FbsBooking();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (request.getParameter("regNum") != null) {
            regNum = Integer.parseInt(request.getParameter("regNum"));
        }
        if (request.getParameter("rFlag") != null) {
            rFlag = request.getParameter("rFlag");
        }
        if (request.getParameter("renderFlag") != null) {
            renderFlag = request.getParameter("renderFlag");
        }
        fbsBooking = fbsBookingFacade.find(regNum);
        if (renderFlag.equals("2")) {
            renderDetail = true;
            //  @NamedQuery(name = "FbsApplicant.findByFbsFlat&Status", query = "SELECT f FROM FbsApplicant f where f.fbsFlat= :fbsFlat AND f.status = :status"),
            fbsapplicantlist.clear();
            fbsapplicantlist = em.createNamedQuery("FbsApplicant.findByFbsFlat&Status").setParameter("fbsFlat", fbsBooking.getFbsFlat()).setParameter("status", "transfered").getResultList();

        } else {
            renderDetail = false;
        }
    }

    public String convertFloorNo(int floorNo) {
        return blockManager.convertFloorNo(floorNo);
    }

    public void transferBooking() throws IOException {
        bookingManager.transferBooking(fbsBooking.getFbsFlat(), fbsApplicant, fbsCoApplicant);
        fbsApplicant = new FbsApplicant();
        fbsCoApplicant = new FbsApplicant();
        redirectBooking();
    }

    public void redirectBooking() throws IOException {
        if (rFlag.equals("1")) {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/jsfpages/Booking/bookingList.xhtml");
        } else {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/jsfpages/Booking/bookingDetail.xhtml?unitCode=" + fbsBooking.getFbsFlat().getUnitCode() + "&redirectFlag=bookingList&name=Booking List&url=/faces/jsfpages/Booking/bookingList.xhtml");
        }

        populate();

    }

    public String returnUrl() {
        String url;
        if (fbsBooking != null && fbsBooking.getRegNumber() != null) {
            url = "/faces/jsfpages/Booking/bookingDetail.xhtml?unitCode=" + fbsBooking.getFbsFlat().getUnitCode() + "&redirectFlag=bookingList&name=Booking List&url=/faces/jsfpages/Booking/bookingList.xhtml";

        } else {
            url = "#";
        }
        return url;
    }

    public void transferDetail(FbsApplicant transferApplicant) {
        fbsApplicant = new FbsApplicant();
        fbsApplicant = transferApplicant;
    }

    public FbsApplicant getFbsApplicant() {
        return fbsApplicant;
    }

    public void setFbsApplicant(FbsApplicant fbsApplicant) {
        this.fbsApplicant = fbsApplicant;
    }

    public FbsApplicant getFbsCoApplicant() {
        return fbsCoApplicant;
    }

    public void setFbsCoApplicant(FbsApplicant fbsCoApplicant) {
        this.fbsCoApplicant = fbsCoApplicant;
    }

    public FbsBooking getFbsBooking() {
        return fbsBooking;
    }

    public void setFbsBooking(FbsBooking fbsBooking) {
        this.fbsBooking = fbsBooking;
    }

    public List<FbsApplicant> getFbsapplicantlist() {
        return fbsapplicantlist;
    }

    public void setFbsapplicantlist(List<FbsApplicant> fbsapplicantlist) {
        this.fbsapplicantlist = fbsapplicantlist;
    }

    public boolean isRenderDetail() {
        return renderDetail;
    }

    public void setRenderDetail(boolean renderDetail) {
        this.renderDetail = renderDetail;
    }
}
