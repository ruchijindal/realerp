/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smp.realerp.entity;

import java.io.Serializable;
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

/**
 *
 * @author smp
 */
@Entity
@Table(name = "fbs_bank")
@NamedQueries({
    @NamedQuery(name = "FbsBank.findAll", query = "SELECT f FROM FbsBank f"),
    @NamedQuery(name = "FbsBank.findByBankId", query = "SELECT f FROM FbsBank f WHERE f.bankId = :bankId"),
    @NamedQuery(name = "FbsBank.findByBankName", query = "SELECT f FROM FbsBank f WHERE f.bankName = :bankName"),
    @NamedQuery(name = "FbsBank.findByAccountNo", query = "SELECT f FROM FbsBank f WHERE f.accountNo = :accountNo"),
    @NamedQuery(name = "FbsBank.findByAddress", query = "SELECT f FROM FbsBank f WHERE f.address = :address")})
public class FbsBank implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "BANK_ID")
    private Integer bankId;
    @Column(name = "BANK_NAME")
    private String bankName;
    @Column(name = "ACCOUNT_NO")
    private String accountNo;
    @Column(name = "ADDRESS")
    private String address;
    @JoinColumn(name = "FK_COMPANY_ID", referencedColumnName = "COMPANY_ID")
    @ManyToOne(optional = false)
    private FbsCompany fbsCompany;

    public FbsBank() {
    }

    public FbsBank(Integer bankId) {
        this.bankId = bankId;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
        hash += (bankId != null ? bankId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsBank)) {
            return false;
        }
        FbsBank other = (FbsBank) object;
        if ((this.bankId == null && other.bankId != null) || (this.bankId != null && !this.bankId.equals(other.bankId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsBank[bankId=" + bankId + "]";
    }

}
