/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smp.realerp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author smp
 */
@Entity
@Table(name = "fbs_loan")
@NamedQueries({
    @NamedQuery(name = "FbsLoan.findAll", query = "SELECT f FROM FbsLoan f"),
    @NamedQuery(name = "FbsLoan.findByLoanId", query = "SELECT f FROM FbsLoan f WHERE f.loanId = :loanId"),
    @NamedQuery(name = "FbsLoan.findByLoanAppnumber", query = "SELECT f FROM FbsLoan f WHERE f.loanAppnumber = :loanAppnumber"),
    @NamedQuery(name = "FbsLoan.findByLoanDate", query = "SELECT f FROM FbsLoan f WHERE f.loanDate = :loanDate"),
    @NamedQuery(name = "FbsLoan.findByStartFrom", query = "SELECT f FROM FbsLoan f WHERE f.startFrom = :startFrom"),
    @NamedQuery(name = "FbsLoan.findByBankName", query = "SELECT f FROM FbsLoan f WHERE f.bankName = :bankName"),
    @NamedQuery(name = "FbsLoan.findByBankAddress", query = "SELECT f FROM FbsLoan f WHERE f.bankAddress = :bankAddress")})
public class FbsLoan implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "LOAN_ID")
    private Integer loanId;
    @Column(name = "LOAN_APPNUMBER")
    private String loanAppnumber;
    @Column(name = "LOAN_DATE")
    @Temporal(TemporalType.DATE)
    private Date loanDate;
    @Column(name = "START_FROM")
    @Temporal(TemporalType.DATE)
    private Date startFrom;
    @Column(name = "BANK_NAME")
    private String bankName;
    @Column(name = "BANK_ADDRESS")
    private String bankAddress;
    @JoinColumn(name = "fbs_flat_unit_code", referencedColumnName = "UNIT_CODE")
    @ManyToOne(optional = false)
    private FbsFlat fbsFlat;

    public FbsLoan() {
    }

    public FbsLoan(Integer loanId) {
        this.loanId = loanId;
    }

    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }

    public String getLoanAppnumber() {
        return loanAppnumber;
    }

    public void setLoanAppnumber(String loanAppnumber) {
        this.loanAppnumber = loanAppnumber;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public Date getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(Date startFrom) {
        this.startFrom = startFrom;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public FbsFlat getFbsFlat() {
        return fbsFlat;
    }

    public void setFbsFlat(FbsFlat fbsFlat) {
        this.fbsFlat = fbsFlat;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (loanId != null ? loanId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsLoan)) {
            return false;
        }
        FbsLoan other = (FbsLoan) object;
        if ((this.loanId == null && other.loanId != null) || (this.loanId != null && !this.loanId.equals(other.loanId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsLoan[loanId=" + loanId + "]";
    }

}
