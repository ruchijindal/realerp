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
@Table(name = "fbs_broker_cat")
@NamedQueries({
    @NamedQuery(name = "FbsBrokerCat.findAll", query = "SELECT f FROM FbsBrokerCat f"),
    @NamedQuery(name = "FbsBrokerCat.findByCategoryId", query = "SELECT f FROM FbsBrokerCat f WHERE f.categoryId = :categoryId"),
    @NamedQuery(name = "FbsBrokerCat.findByCategoryName", query = "SELECT f FROM FbsBrokerCat f WHERE f.categoryName = :categoryName"),
    @NamedQuery(name = "FbsBrokerCat.findByCommission", query = "SELECT f FROM FbsBrokerCat f WHERE f.commission = :commission"),
    @NamedQuery(name = "FbsBrokerCat.findByBspPercent", query = "SELECT f FROM FbsBrokerCat f WHERE f.bspPercent = :bspPercent"),
    @NamedQuery(name = "FbsBrokerCat.findByBrokerPercent", query = "SELECT f FROM FbsBrokerCat f WHERE f.brokerPercent = :brokerPercent")})
public class FbsBrokerCat implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CATEGORY_ID")
    private Integer categoryId;
    @Column(name = "CATEGORY_NAME")
    private String categoryName;
    @Column(name = "COMMISSION")
    private Double commission;
    @Column(name = "BSP_PERCENT")
    private Double bspPercent;
    @Column(name = "BROKER_PERCENT")
    private Double brokerPercent;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsBrokerCat")
    private Collection<FbsBroker> fbsBrokerCollection;
    @JoinColumn(name = "FK_COMPANY_ID", referencedColumnName = "COMPANY_ID")
    @ManyToOne(optional = false)
    private FbsCompany fbsCompany;

    public FbsBrokerCat() {
    }

    public FbsBrokerCat(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Double getBspPercent() {
        return bspPercent;
    }

    public void setBspPercent(Double bspPercent) {
        this.bspPercent = bspPercent;
    }

    public Double getBrokerPercent() {
        return brokerPercent;
    }

    public void setBrokerPercent(Double brokerPercent) {
        this.brokerPercent = brokerPercent;
    }

    public Collection<FbsBroker> getFbsBrokerCollection() {
        return fbsBrokerCollection;
    }

    public void setFbsBrokerCollection(Collection<FbsBroker> fbsBrokerCollection) {
        this.fbsBrokerCollection = fbsBrokerCollection;
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
        hash += (categoryId != null ? categoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsBrokerCat)) {
            return false;
        }
        FbsBrokerCat other = (FbsBrokerCat) object;
        if ((this.categoryId == null && other.categoryId != null) || (this.categoryId != null && !this.categoryId.equals(other.categoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsBrokerCat[categoryId=" + categoryId + "]";
    }

}
