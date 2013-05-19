/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author smp
 */
@Entity
@Table(name = "fbs_broker")
@NamedQueries({
    @NamedQuery(name = "FbsBroker.findAll", query = "SELECT f FROM FbsBroker f"),
    @NamedQuery(name = "FbsBroker.findByBrokerId", query = "SELECT f FROM FbsBroker f WHERE f.brokerId = :brokerId"),
    @NamedQuery(name = "FbsBroker.findByBrName", query = "SELECT f FROM FbsBroker f WHERE f.brName = :brName"),
    @NamedQuery(name = "FbsBroker.findByBrAdd", query = "SELECT f FROM FbsBroker f WHERE f.brAdd = :brAdd"),
    @NamedQuery(name = "FbsBroker.findByBrMail", query = "SELECT f FROM FbsBroker f WHERE f.brMail = :brMail"),
    @NamedQuery(name = "FbsBroker.findByBrMobile", query = "SELECT f FROM FbsBroker f WHERE f.brMobile = :brMobile"),
    @NamedQuery(name = "FbsBroker.findByBrPhone", query = "SELECT f FROM FbsBroker f WHERE f.brPhone = :brPhone"),
    @NamedQuery(name = "FbsBroker.findBySite", query = "SELECT f FROM FbsBroker f WHERE f.site = :site")})
public class FbsBroker implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "BROKER_ID")
    private Integer brokerId;
    @Column(name = "BR_NAME")
    private String brName;
    @Column(name = "BR_ADD")
    private String brAdd;
    @Column(name = "BR_MAIL")
    private String brMail;
    @Column(name = "BR_MOBILE")
    private String brMobile;
    @Column(name = "BR_PHONE")
    private String brPhone;
    @Column(name = "SITE")
    private String site;
    @JoinColumn(name = "FK_COMPANY_ID", referencedColumnName = "COMPANY_ID")
    @ManyToOne(optional = false)
    private FbsCompany fbsCompany;
    @JoinColumn(name = "FK_BROKER_CATEGORY_ID", referencedColumnName = "CATEGORY_ID")
    @ManyToOne(optional = false)
    private FbsBrokerCat fbsBrokerCat;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsBroker")
    private Collection<FbsBrPayment> fbsBrPaymentCollection;
    @OneToMany(mappedBy = "fbsBroker")
    private Collection<FbsBooking> fbsBookingCollection;

    public FbsBroker() {
    }

    public FbsBroker(Integer brokerId) {
        this.brokerId = brokerId;
    }

    public Integer getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(Integer brokerId) {
        this.brokerId = brokerId;
    }

    public String getBrName() {
        return brName;
    }

    public void setBrName(String brName) {
        this.brName = brName;
    }

    public String getBrAdd() {
        return brAdd;
    }

    public void setBrAdd(String brAdd) {
        this.brAdd = brAdd;
    }

    public String getBrMail() {
        return brMail;
    }

    public void setBrMail(String brMail) {
        this.brMail = brMail;
    }

    public String getBrMobile() {
        return brMobile;
    }

    public void setBrMobile(String brMobile) {
        this.brMobile = brMobile;
    }

    public String getBrPhone() {
        return brPhone;
    }

    public void setBrPhone(String brPhone) {
        this.brPhone = brPhone;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public FbsCompany getFbsCompany() {
        return fbsCompany;
    }

    public void setFbsCompany(FbsCompany fbsCompany) {
        this.fbsCompany = fbsCompany;
    }

    public FbsBrokerCat getFbsBrokerCat() {
        return fbsBrokerCat;
    }

    public void setFbsBrokerCat(FbsBrokerCat fbsBrokerCat) {
        this.fbsBrokerCat = fbsBrokerCat;
    }

    public Collection<FbsBrPayment> getFbsBrPaymentCollection() {
        return fbsBrPaymentCollection;
    }

    public void setFbsBrPaymentCollection(Collection<FbsBrPayment> fbsBrPaymentCollection) {
        this.fbsBrPaymentCollection = fbsBrPaymentCollection;
    }

    public Collection<FbsBooking> getFbsBookingCollection() {
        return fbsBookingCollection;
    }

    public void setFbsBookingCollection(Collection<FbsBooking> fbsBookingCollection) {
        this.fbsBookingCollection = fbsBookingCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (brokerId != null ? brokerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsBroker)) {
            return false;
        }
        FbsBroker other = (FbsBroker) object;
        if ((this.brokerId == null && other.brokerId != null) || (this.brokerId != null && !this.brokerId.equals(other.brokerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsBroker[brokerId=" + brokerId + "]";
    }
}