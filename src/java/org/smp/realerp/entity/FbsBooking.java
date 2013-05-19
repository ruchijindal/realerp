/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author smp
 */
@Entity
@Table(name = "fbs_booking")
@NamedQueries({
    @NamedQuery(name = "FbsBooking.findByCurrentDate", query = "SELECT f FROM FbsBooking f  WHERE  f.bookingDt >= :beforeDate"),                                                                                
    @NamedQuery(name = "FbsBooking.findByAuthorizeStatus", query = "SELECT f FROM FbsBooking f WHERE f.authorizeStatus = :authorizeStatus"),
    @NamedQuery(name = "FbsBooking.findByFbsBroker&Status", query = "SELECT f FROM FbsBooking f WHERE f.fbsBroker IS NOT NULL AND f.status = :status"),
    @NamedQuery(name = "FbsBooking.findByFbsFlat&Status", query = "SELECT f FROM FbsBooking f WHERE f.fbsFlat = :fbsFlat AND f.status = :status"),
    @NamedQuery(name = "FbsBooking.findByDate&Status", query = "SELECT f FROM FbsBooking f WHERE f.bookingDt >= :fromDate AND f.bookingDt <= :toDate AND f.status = :status"),
    @NamedQuery(name = "FbsBooking.findAll", query = "SELECT f FROM FbsBooking f"),
    @NamedQuery(name = "FbsBooking.findAllReverseOrderDate", query = "SELECT f FROM FbsBooking f ORDER BY f.bookingDt DESC "),
    @NamedQuery(name = "FbsBooking.findByRegNumber", query = "SELECT f FROM FbsBooking f WHERE f.regNumber = :regNumber"),
    @NamedQuery(name = "FbsBooking.findByBookingDt", query = "SELECT f FROM FbsBooking f WHERE f.bookingDt = :bookingDt"),
    @NamedQuery(name = "FbsBooking.findByStatus", query = "SELECT f FROM FbsBooking f WHERE f.status = :status"),
    @NamedQuery(name = "FbsBooking.findByUserId", query = "SELECT f FROM FbsBooking f WHERE f.userId = :userId"),
    @NamedQuery(name = "FbsBooking.findByRemark", query = "SELECT f FROM FbsBooking f WHERE f.remark = :remark")})
public class FbsBooking implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "REG_NUMBER")
    private Integer regNumber;
    @Column(name = "BOOKING_DT")
    @Temporal(TemporalType.DATE)
    private Date bookingDt;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "REMARK")
    private String remark;
    @Column(name = "AUTHORIZE_STATUS")
    private String authorizeStatus;
    @Column(name = "AUTHORIZE_BY")
    private String authorizeBy;
    @Column(name = "AUTHORIZE_DATE")
    @Temporal(TemporalType.DATE)
    private Date authorizeDate;
    @OneToMany(mappedBy = "fbsBooking")
    private Collection<FbsParking> fbsParkingCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsBooking")
    private Collection<FbsPayment> fbsPaymentCollection;
    @JoinColumn(name = "FK_UNIT_CODE", referencedColumnName = "UNIT_CODE")
    @ManyToOne(optional = false)
    private FbsFlat fbsFlat;
    @JoinColumn(name = "FK_BROKER_ID", referencedColumnName = "BROKER_ID")
    @ManyToOne
    private FbsBroker fbsBroker;
    @JoinColumn(name = "FK_DISCOUNT_ID", referencedColumnName = "DISCOUNT_ID")
    @ManyToOne
    private FbsDiscount fbsDiscount;
    @JoinColumn(name = "FK_PLANNAME_ID", referencedColumnName = "PLAN_ID")
    @ManyToOne
    private FbsPlanname fbsPlanname;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsBooking")
    private Collection<FbsBrPayment> fbsBrPaymentCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsBooking")
    private Collection<FbsReport> fbsReportCollection;

    public FbsBooking() {
    }

    public FbsBooking(Integer regNumber) {
        this.regNumber = regNumber;
    }

    public Integer getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(Integer regNumber) {
        this.regNumber = regNumber;
    }

    public Date getBookingDt() {
        return bookingDt;
    }

    public void setBookingDt(Date bookingDt) {
        this.bookingDt = bookingDt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Collection<FbsParking> getFbsParkingCollection() {
        return fbsParkingCollection;
    }

    public void setFbsParkingCollection(Collection<FbsParking> fbsParkingCollection) {
        this.fbsParkingCollection = fbsParkingCollection;
    }

    public Collection<FbsPayment> getFbsPaymentCollection() {
        return fbsPaymentCollection;
    }

    public void setFbsPaymentCollection(Collection<FbsPayment> fbsPaymentCollection) {
        this.fbsPaymentCollection = fbsPaymentCollection;
    }

    public FbsFlat getFbsFlat() {
        return fbsFlat;
    }

    public void setFbsFlat(FbsFlat fbsFlat) {
        this.fbsFlat = fbsFlat;
    }

    public FbsBroker getFbsBroker() {
        return fbsBroker;
    }

    public void setFbsBroker(FbsBroker fbsBroker) {
        this.fbsBroker = fbsBroker;
    }

    public FbsDiscount getFbsDiscount() {
        return fbsDiscount;
    }

    public void setFbsDiscount(FbsDiscount fbsDiscount) {
        this.fbsDiscount = fbsDiscount;
    }

    public FbsPlanname getFbsPlanname() {
        return fbsPlanname;
    }

    public void setFbsPlanname(FbsPlanname fbsPlanname) {
        this.fbsPlanname = fbsPlanname;
    }

    public Collection<FbsBrPayment> getFbsBrPaymentCollection() {
        return fbsBrPaymentCollection;
    }

    public void setFbsBrPaymentCollection(Collection<FbsBrPayment> fbsBrPaymentCollection) {
        this.fbsBrPaymentCollection = fbsBrPaymentCollection;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public Date getAuthorizeDate() {
        return authorizeDate;
    }

    public void setAuthorizeDate(Date authorizeDate) {
        this.authorizeDate = authorizeDate;
    }

    public Collection<FbsReport> getFbsReportCollection() {
        return fbsReportCollection;
    }

    public void setFbsReportCollection(Collection<FbsReport> fbsReportCollection) {
        this.fbsReportCollection = fbsReportCollection;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (regNumber != null ? regNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsBooking)) {
            return false;
        }
        FbsBooking other = (FbsBooking) object;
        if ((this.regNumber == null && other.regNumber != null) || (this.regNumber != null && !this.regNumber.equals(other.regNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsBooking[regNumber=" + regNumber + "]";
    }
}
