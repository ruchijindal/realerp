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
@Table(name = "fbs_payment")
@NamedQueries({
    @NamedQuery(name = "FbsPayment.findByStatus", query = "SELECT f FROM FbsPayment f  WHERE  f.status = :status"),
    @NamedQuery(name = "FbsPayment.findByCurrentDate", query = "SELECT f FROM FbsPayment f  WHERE  f.paymentDate >= :beforeDate"),
    @NamedQuery(name = "FbsPayment.findByDate", query = "SELECT f FROM FbsPayment f WHERE f.paymentDate >= :fromDate AND f.paymentDate <= :toDate"),
    @NamedQuery(name = "FbsPayment.findByChqDate", query = "SELECT f FROM FbsPayment f WHERE f.chequeDate >= :fromDate AND f.chequeDate <= :toDate"),
    @NamedQuery(name = "FbsPayment.findByClrDate", query = "SELECT f FROM FbsPayment f WHERE f.clearingDt >= :fromDate AND f.clearingDt <= :toDate"),
    @NamedQuery(name = "FbsPayment.findByFbsChequeNo||TransactionId", query = "SELECT f FROM FbsPayment f WHERE f.chequeNo LIKE :chequeNo OR f.transactionId LIKE :transactionId"),
    @NamedQuery(name = "FbsPayment.findByFbsBooking", query = "SELECT f FROM FbsPayment f WHERE f.fbsBooking = :fbsBooking"),
    @NamedQuery(name = "FbsPayment.findByFbsBookingAndStatus", query = "SELECT f FROM FbsPayment f WHERE f.fbsBooking = :fbsBooking AND f.status = :status"),
    @NamedQuery(name = "FbsPayment.findAll", query = "SELECT f FROM FbsPayment f"),
    @NamedQuery(name = "FbsPayment.findAllReverseOrderPaymentDate", query = "SELECT f FROM FbsPayment f ORDER BY f.paymentDate DESC "),
    @NamedQuery(name = "FbsPayment.findByPaymentId", query = "SELECT f FROM FbsPayment f WHERE f.paymentId = :paymentId"),
    @NamedQuery(name = "FbsPayment.findByPaidAmount", query = "SELECT f FROM FbsPayment f WHERE f.paidAmount LIKE :paidAmount"),
    @NamedQuery(name = "FbsPayment.findByServiceTax", query = "SELECT f FROM FbsPayment f WHERE f.serviceTax = :serviceTax"),
    @NamedQuery(name = "FbsPayment.findByChequeNo", query = "SELECT f FROM FbsPayment f WHERE f.chequeNo = :chequeNo"),
    @NamedQuery(name = "FbsPayment.findByChequeDate", query = "SELECT f FROM FbsPayment f WHERE f.chequeDate = :chequeDate"),
    @NamedQuery(name = "FbsPayment.findByChequeAmt", query = "SELECT f FROM FbsPayment f WHERE f.chequeAmt = :chequeAmt"),
    @NamedQuery(name = "FbsPayment.findByDrawnOn", query = "SELECT f FROM FbsPayment f WHERE f.drawnOn LIKE :drawnOn"),
    @NamedQuery(name = "FbsPayment.findByChequeStatus", query = "SELECT f FROM FbsPayment f WHERE f.status = :status"),
    @NamedQuery(name = "FbsPayment.findByPaymentMode", query = "SELECT f FROM FbsPayment f WHERE f.paymentMode = :paymentMode"),
    @NamedQuery(name = "FbsPayment.findByPaymentDate", query = "SELECT f FROM FbsPayment f WHERE f.paymentDate = :paymentDate"),
    @NamedQuery(name = "FbsPayment.findByClearingDt", query = "SELECT f FROM FbsPayment f WHERE f.clearingDt = :clearingDt"),
    @NamedQuery(name = "FbsPayment.findByClearingBank", query = "SELECT f FROM FbsPayment f WHERE f.clearingBank = :clearingBank"),
    @NamedQuery(name = "FbsPayment.findByAuthorizedBy", query = "SELECT f FROM FbsPayment f WHERE f.authorizedBy LIKE :authorizedBy"),
    @NamedQuery(name = "FbsPayment.findByRemark", query = "SELECT f FROM FbsPayment f WHERE f.remark = :remark"),
    @NamedQuery(name = "FbsPayment.findByUserId", query = "SELECT f FROM FbsPayment f WHERE f.userId = :userId"),
    @NamedQuery(name = "FbsPayment.findByTransactionId", query = "SELECT f FROM FbsPayment f WHERE f.transactionId = :transactionId")})
public class FbsPayment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PAYMENT_ID")
    private Integer paymentId;
    @Column(name = "PAID_AMOUNT")
    private Double paidAmount;
    @Column(name = "SERVICE_TAX")
    private Double serviceTax;
    @Column(name = "CHEQUE_NO")
    private String chequeNo;
    @Column(name = "CHEQUE_DATE")
    @Temporal(TemporalType.DATE)
    private Date chequeDate;
    @Column(name = "CHEQUE_AMT")
    private Double chequeAmt;
    @Column(name = "DRAWN_ON")
    private String drawnOn;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "PAYMENT_MODE")
    private String paymentMode;
    @Column(name = "PAYMENT_DATE")
    @Temporal(TemporalType.DATE)
    private Date paymentDate;
    @Column(name = "CLEARING_DT")
    @Temporal(TemporalType.DATE)
    private Date clearingDt;
    @Column(name = "CLEARING_BANK")
    private String clearingBank;
    @Column(name = "AUTHORIZED_BY")
    private String authorizedBy;
    @Column(name = "REMARK")
    private String remark;
    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "TRANSACTION_ID")
    private String transactionId;
    @JoinColumn(name = "FK_REG_NUMBER", referencedColumnName = "REG_NUMBER")
    @ManyToOne(optional = false)
    private FbsBooking fbsBooking;

    public FbsPayment() {
    }

    public FbsPayment(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Double getServiceTax() {
        return serviceTax;
    }

    public void setServiceTax(Double serviceTax) {
        this.serviceTax = serviceTax;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public Date getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(Date chequeDate) {
        this.chequeDate = chequeDate;
    }

    public Double getChequeAmt() {
        return chequeAmt;
    }

    public void setChequeAmt(Double chequeAmt) {
        this.chequeAmt = chequeAmt;
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

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Date getClearingDt() {
        return clearingDt;
    }

    public void setClearingDt(Date clearingDt) {
        this.clearingDt = clearingDt;
    }

    public String getClearingBank() {
        return clearingBank;
    }

    public void setClearingBank(String clearingBank) {
        this.clearingBank = clearingBank;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paymentId != null ? paymentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsPayment)) {
            return false;
        }
        FbsPayment other = (FbsPayment) object;
        if ((this.paymentId == null && other.paymentId != null) || (this.paymentId != null && !this.paymentId.equals(other.paymentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsPayment[paymentId=" + paymentId + "]";
    }
}
