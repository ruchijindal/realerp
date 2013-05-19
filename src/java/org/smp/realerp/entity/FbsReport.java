/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smp.realerp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author smp
 */
@Entity
@Table(name = "fbs_report")
@NamedQueries({
    @NamedQuery(name = "FbsReport.findAll", query = "SELECT f FROM FbsReport f"),
    @NamedQuery(name = "FbsReport.findById", query = "SELECT f FROM FbsReport f WHERE f.id = :id"),   
    @NamedQuery(name = "FbsReport.findByDate", query = "SELECT f FROM FbsReport f WHERE f.date = :date"),
    @NamedQuery(name = "FbsReport.findByRecievedAmt", query = "SELECT f FROM FbsReport f WHERE f.recievedAmt = :recievedAmt"),
    @NamedQuery(name = "FbsReport.findByCurInstallment", query = "SELECT f FROM FbsReport f WHERE f.curInstallment = :curInstallment"),
    @NamedQuery(name = "FbsReport.findByOutCredit", query = "SELECT f FROM FbsReport f WHERE f.outCredit = :outCredit"),
    @NamedQuery(name = "FbsReport.findByAmountPayable", query = "SELECT f FROM FbsReport f WHERE f.amountPayable = :amountPayable"),
    @NamedQuery(name = "FbsReport.findByServiceTax", query = "SELECT f FROM FbsReport f WHERE f.serviceTax = :serviceTax"),
    @NamedQuery(name = "FbsReport.findByTotalCost", query = "SELECT f FROM FbsReport f WHERE f.totalCost = :totalCost"),
    @NamedQuery(name = "FbsReport.findByRemainingAmt", query = "SELECT f FROM FbsReport f WHERE f.remainingAmt = :remainingAmt")})
public class FbsReport implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @JoinColumn(name = "FK_REG_NUMBER", referencedColumnName = "REG_NUMBER")
    @ManyToOne(optional = false)
    private FbsBooking fbsBooking;
    @Column(name = "DATE")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Column(name = "RECIEVED_AMT")
    private Double recievedAmt;
    @Column(name = "CUR_INSTALLMENT")
    private Double curInstallment;
    @Column(name = "OUT_CREDIT")
    private Double outCredit;
    @Column(name = "AMOUNT_PAYABLE")
    private Double amountPayable;
    @Column(name = "SERVICE_TAX")
    private Double serviceTax;
    @Column(name = "TOTAL_COST")
    private Double totalCost;
    @Column(name = "REMAINING_AMT")
    private Double remainingAmt;
    @JoinColumn(name = "FK_COMPANY_ID", referencedColumnName = "COMPANY_ID")
    @ManyToOne(optional = false)
    private FbsCompany fbsCompany;

    public FbsReport() {
    }

    public FbsReport(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FbsBooking getFbsBooking() {
        return fbsBooking;
    }

    public void setFbsBooking(FbsBooking fbsBooking) {
        this.fbsBooking = fbsBooking;
    }

   

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getRecievedAmt() {
        return recievedAmt;
    }

    public void setRecievedAmt(Double recievedAmt) {
        this.recievedAmt = recievedAmt;
    }

    public Double getCurInstallment() {
        return curInstallment;
    }

    public void setCurInstallment(Double curInstallment) {
        this.curInstallment = curInstallment;
    }

    public Double getOutCredit() {
        return outCredit;
    }

    public void setOutCredit(Double outCredit) {
        this.outCredit = outCredit;
    }

    public Double getAmountPayable() {
        return amountPayable;
    }

    public void setAmountPayable(Double amountPayable) {
        this.amountPayable = amountPayable;
    }

    public Double getServiceTax() {
        return serviceTax;
    }

    public void setServiceTax(Double serviceTax) {
        this.serviceTax = serviceTax;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Double getRemainingAmt() {
        return remainingAmt;
    }

    public void setRemainingAmt(Double remainingAmt) {
        this.remainingAmt = remainingAmt;
    }

    public FbsCompany getFbsCompany() {
        return fbsCompany;
    }

    public void setFbsCompany(FbsCompany fbsCompany) {
        this.fbsCompany = fbsCompany;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsReport)) {
            return false;
        }
        FbsReport other = (FbsReport) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsReport[id=" + id + "]";
    }

}
