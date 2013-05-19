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
 * @author SMP Technologies
 */
@Entity
@Table(name = "fbs_discount")
@NamedQueries({
    @NamedQuery(name = "FbsDiscount.findAll", query = "SELECT f FROM FbsDiscount f"),
    @NamedQuery(name = "FbsDiscount.findByDiscountId", query = "SELECT f FROM FbsDiscount f WHERE f.discountId = :discountId"),
    @NamedQuery(name = "FbsDiscount.findByDiscountType", query = "SELECT f FROM FbsDiscount f WHERE f.discountType = :discountType"),
    @NamedQuery(name = "FbsDiscount.findByPercentage", query = "SELECT f FROM FbsDiscount f WHERE f.percentage = :percentage")})
public class FbsDiscount implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "DISCOUNT_ID")
    private Integer discountId;
    @Column(name = "DISCOUNT_TYPE")
    private String discountType;
    @Column(name = "PERCENTAGE")
    private Double percentage;
    @JoinColumn(name = "FK_COMPANY_ID", referencedColumnName = "COMPANY_ID")
    @ManyToOne(optional = false)
    private FbsCompany fbsCompany;
    @OneToMany(mappedBy = "fbsDiscount")
    private Collection<FbsBooking> fbsBookingCollection;

    public FbsDiscount() {
    }

    public FbsDiscount(Integer discountId) {
        this.discountId = discountId;
    }

    public Integer getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Integer discountId) {
        this.discountId = discountId;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public FbsCompany getFbsCompany() {
        return fbsCompany;
    }

    public void setFbsCompany(FbsCompany fbsCompany) {
        this.fbsCompany = fbsCompany;
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
        hash += (discountId != null ? discountId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsDiscount)) {
            return false;
        }
        FbsDiscount other = (FbsDiscount) object;
        if ((this.discountId == null && other.discountId != null) || (this.discountId != null && !this.discountId.equals(other.discountId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsDiscount[discountId=" + discountId + "]";
    }

}
