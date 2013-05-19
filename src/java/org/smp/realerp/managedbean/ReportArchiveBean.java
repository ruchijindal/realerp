/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.managedbean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.DateSelectEvent;
import org.smp.realerp.entity.FbsCompany;
import org.smp.realerp.entity.FbsProject;
import org.smp.realerp.entity.FbsReport;
import org.smp.realerp.pojo.RerpUtil;
import org.smp.realerp.session.FbsCompanyFacade;

/**
 *
 * @author smp
 */
@ManagedBean(name = "reportArchiveBean")
@ViewScoped
public class ReportArchiveBean {

    @EJB
    FbsCompanyFacade fbsCompanyFacade;
    @EJB
    RerpUtil utility;
    FbsCompany fbsCompany = new FbsCompany();
    List<FbsReport> fbsReportList = new ArrayList<FbsReport>();
    List<FbsReport> filteredFbsReportList = new ArrayList<FbsReport>();
    Date dateFrom;
    Date dateTo;
    Integer fbsRegNum;

    @PostConstruct
    public void populate() {
        fbsCompany = new FbsCompany();
        fbsCompany = fbsCompanyFacade.find(LoginBean.fbsLogin.getFbsCompany().getCompanyId());
        fbsReportList.clear();
        fbsReportList = (List<FbsReport>) fbsCompany.getFbsReportCollection();
        filteredFbsReportList.clear();
        filteredFbsReportList.addAll(fbsReportList);
    }

    public String roundOfUptoTwoDecimal(Double value) {
        return utility.indianFormat(value);
    }

    public void handleDateSelectForFromDate(DateSelectEvent event) {
        dateFrom = event.getDate();
        filterReportArchiveList();

    }

    public void handleDateSelectForToDate(DateSelectEvent event) {
        dateTo = event.getDate();
        filterReportArchiveList();

    }

    public void filterReportArchiveList() {
        filteredFbsReportList.clear();
        int regNum;
        Date archiveDate;
        Date to;
        for (int i = 0; i < fbsReportList.size(); i++) {
            regNum=fbsReportList.get(i).getFbsBooking().getRegNumber();
            archiveDate=fbsReportList.get(i).getDate();
            if((fbsRegNum == null || String.valueOf(regNum).contains(String.valueOf(fbsRegNum)))
                    && ((dateFrom == null) || ((archiveDate != null) && ((archiveDate.after(dateFrom)) || (archiveDate.equals(dateFrom)))))
                    && ((dateTo == null) || ((archiveDate != null) && ((archiveDate.before(dateTo)) || (archiveDate.equals(dateTo))))))
            {
                filteredFbsReportList.add(fbsReportList.get(i));
            }
        }

    }
     public void dueLetterReport(FbsReport fbsReport) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/dueLetterReport?registrationNumber=" + fbsReport.getFbsBooking().getRegNumber());


    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public Integer getFbsRegNum() {
        return fbsRegNum;
    }

    public void setFbsRegNum(Integer fbsRegNum) {
        this.fbsRegNum = fbsRegNum;
    }

    public List<FbsReport> getFilteredFbsReportList() {
        return filteredFbsReportList;
    }

    public void setFilteredFbsReportList(List<FbsReport> filteredFbsReportList) {
        this.filteredFbsReportList = filteredFbsReportList;
    }
}
