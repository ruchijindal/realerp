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
@Table(name = "fbs_br_payment")
@NamedQueries({
    @NamedQuery(name = "FbsBrPayment.findAll", query = "SELECT f FROM FbsBrPayment f"),
    @NamedQuery(name = "FbsBrPayment.findByPaymentId", query = "SELECT f FROM FbsBrPayment f WHERE f.paymentId = :paymentId"),
    @NamedQuery(name = "FbsBrPayment.findByClearingDt", query = "SELECT f FROM FbsBrPayment f WHERE f.clearingDt = :clearingDt"),
    @NamedQuery(name = "FbsBrPayment.findByPaymentDate", query = "SELECT f FROM FbsBrPayment f WHERE f.paymentDate = :paymentDate"),
    @NamedQuery(name = "FbsBrPayment.findByPaymentMode", query = "SELECT f FROM FbsBrPayment f WHERE f.paymentMode = :paymentMode"),
    @NamedQuery(name = "FbsBrPayment.findByAmount", query = "SELECT f FROM FbsBrPayment f WHERE f.amount = :amount"),
    @NamedQuery(name = "FbsBrPayment.findByChequeDate", query = "SELECT f FROM FbsBrPayment f WHERE f.chequeDate = :chequeDate"),
    @NamedQuery(name = "FbsBrPayment.findByChequeNo", query = "SELECT f FROM FbsBrPayment f WHERE f.chequeNo = :chequeNo"),
    @NamedQuery(name = "FbsBrPayment.findByDrawnOn", query = "SELECT f FROM FbsBrPayment f WHERE f.drawnOn = :drawnOn"),
    @NamedQuery(name = "FbsBrPayment.findByStatus", query = "SELECT f FROM FbsBrPayment f WHERE f.status = :status"),   
    @NamedQuery(name = "FbsBrPayment.findByAuthorizedBy", query = "SELECT f FROM FbsBrPayment f WHERE f.authorizedBy = :authorizedBy"),
    @NamedQuery(name = "FbsBrPayment.findByRemark", query = "SELECT f FROM FbsBrPayment f WHERE f.remark = :remark"),
    @NamedQuery(name = "FbsBrPayment.findByUserId", query = "SELECT f FROM FbsBrPayment f WHERE f.userId = :userId"),
    @NamedQuery(name = "FbsBrPayment.findByClearingBank", query = "SELECT f FROM FbsBrPayment f WHERE f.clearingBank = :clearingBank"),
    @NamedQuery(name = "FbsBrPayment.findByTransactionId", query = "SELECT f FROM FbsBrPayment f WHERE f.transactionId = :transactionId")})
public class FbsBrPayment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PAYMENT_ID")
    private Integer paymentId;
    @Column(name = "CLEARING_DT")
    @Temporal(TemporalType.DATE)
    private Date clearingDt;
    @Column(name = "PAYMENT_DATE")
    @Temporal(TemporalType.DATE)
    private Date paymentDate;
    @Column(name = "PAYMENT_MODE")
    private String paymentMode;
    @Column(name = "AMOUNT")
    private Double amount;
    @Column(name = "CHEQUE_DATE")
    @Temporal(TemporalType.DATE)
    private Date chequeDate;
    @Column(name = "CHEQUE_NO")
    private String chequeNo;
    @Column(name = "DRAWN_ON")
    private String drawnOn;
    @Column(name = "STATUS")
    private String status;   
    @Column(name = "AUTHORIZED_BY")
    private String authorizedBy;
    @Column(name = "REMARK")
    private String remark;
    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "CLEARING_BANK")
    private String clearingBank;
    @Column(name = "TRANSACTION_ID")
    private String transactionId;
    @JoinColumn(name = "FK_REG_NUMBER", referencedColumnName = "REG_NUMBER")
    @ManyToOne(optional = false)
    private FbsBooking fbsBooking;
    @JoinColumn(name = "FK_BROKER_ID", referencedColumnName = "BROKER_ID")
    @ManyToOne(optional = false)
    private FbsBroker fbsBroker;

    public FbsBrPayment() {
    }

    public FbsBrPayment(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Date getClearingDt() {
        return clearingDt;
    }

    public void setClearingDt(Date clearingDt) {
        this.clearingDt = clearingDt;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(Date chequeDate) {
        this.chequeDate = chequeDate;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public String getDrawnOn() {
        return drawnOn;
    }

    public void setDrawnOn(String drawnOn) {
        this.drawnOn = drawnOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClearingBank() {
        return clearingBank;
    }

    public void setClearingBank(String clearingBank) {
        this.clearingBank = clearingBank;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public FbsBooking getFbsBooking() {
        return fbsBooking;
    }

    public void setFbsBooking(FbsBooking fbsBooking) {
        this.fbsBooking = fbsBooking;
    }

   

    public FbsBroker getFbsBroker() {
        return fbsBroker;
    }

    public void setFbsBroker(FbsBroker fbsBroker) {
        this.fbsBroker = fbsBroker;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paymentId != null ? paymentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsBrPayment)) {
            return false;
        }
        FbsBrPayment other = (FbsBrPayment) object;
        if ((this.paymentId == null && other.paymentId != null) || (this.paymentId != null && !this.paymentId.equals(other.paymentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsBrPayment[paymentId=" + paymentId + "]";
    }

}
