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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author smp
 */
@Entity
@Table(name = "fbs_company")
@NamedQueries({
    @NamedQuery(name = "FbsCompany.findAll", query = "SELECT f FROM FbsCompany f"),
    @NamedQuery(name = "FbsCompany.findByCompanyId", query = "SELECT f FROM FbsCompany f WHERE f.companyId = :companyId"),
    @NamedQuery(name = "FbsCompany.findByCompanyName", query = "SELECT f FROM FbsCompany f WHERE f.companyName = :companyName"),
    @NamedQuery(name = "FbsCompany.findByCompanyAbrv", query = "SELECT f FROM FbsCompany f WHERE f.companyAbrv = :companyAbrv"),
    @NamedQuery(name = "FbsCompany.findByAddress", query = "SELECT f FROM FbsCompany f WHERE f.address = :address"),
    @NamedQuery(name = "FbsCompany.findByTelNumber", query = "SELECT f FROM FbsCompany f WHERE f.telNumber = :telNumber"),
    @NamedQuery(name = "FbsCompany.findByMobile", query = "SELECT f FROM FbsCompany f WHERE f.mobile = :mobile"),
    @NamedQuery(name = "FbsCompany.findByEmail", query = "SELECT f FROM FbsCompany f WHERE f.email = :email"),
    @NamedQuery(name = "FbsCompany.findByWebsite", query = "SELECT f FROM FbsCompany f WHERE f.website = :website"),
    @NamedQuery(name = "FbsCompany.findByStatus", query = "SELECT f FROM FbsCompany f WHERE f.status = :status")})
public class FbsCompany implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "COMPANY_ID")
    private Integer companyId;
    @Basic(optional = false)
    @Column(name = "COMPANY_NAME")
    private String companyName;
    @Column(name = "COMPANY_ABRV")
    private String companyAbrv;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "TEL_NUMBER")
    private String telNumber;
    @Column(name = "MOBILE")
    private String mobile;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "WEBSITE")
    private String website;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "MAX_PROJECTS")
    private Integer maxProjects;
    @Column(name = "MAX_FLATS")
    private Integer maxFlats;
    @Column(name = "COPYRIGHT")
    private String copyRight;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsCompany")
    private Collection<FbsBroker> fbsBrokerCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsCompany")
    private Collection<FbsDiscount> fbsDiscountCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsCompany")
    private Collection<FbsLogin> fbsLoginCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsCompany")
    private Collection<FbsProject> fbsProjectCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsCompany")
    private Collection<FbsInterest> fbsInterestCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsCompany")
    private Collection<FbsBank> fbsBankCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsCompany")
    private Collection<FbsUser> fbsUserCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsCompany")
    private Collection<FbsBrokerCat> fbsBrokerCatCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsCompany")
    private Collection<FbsReport> fbsReportCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsCompany")
    private Collection<FbsServicetax> fbsServicetaxCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsCompany")
    private Collection<FbsMailsetting> fbsMailSettingCollection;

    public FbsCompany() {
    }

    public FbsCompany(Integer companyId) {
        this.companyId = companyId;
    }

    public FbsCompany(Integer companyId, String companyName) {
        this.companyId = companyId;
        this.companyName = companyName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAbrv() {
        return companyAbrv;
    }

    public void setCompanyAbrv(String companyAbrv) {
        this.companyAbrv = companyAbrv;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Collection<FbsBroker> getFbsBrokerCollection() {
        return fbsBrokerCollection;
    }

    public void setFbsBrokerCollection(Collection<FbsBroker> fbsBrokerCollection) {
        this.fbsBrokerCollection = fbsBrokerCollection;
    }

    public Collection<FbsDiscount> getFbsDiscountCollection() {
        return fbsDiscountCollection;
    }

    public void setFbsDiscountCollection(Collection<FbsDiscount> fbsDiscountCollection) {
        this.fbsDiscountCollection = fbsDiscountCollection;
    }

    public Collection<FbsLogin> getFbsLoginCollection() {
        return fbsLoginCollection;
    }

    public void setFbsLoginCollection(Collection<FbsLogin> fbsLoginCollection) {
        this.fbsLoginCollection = fbsLoginCollection;
    }

    public Collection<FbsProject> getFbsProjectCollection() {
        return fbsProjectCollection;
    }

    public void setFbsProjectCollection(Collection<FbsProject> fbsProjectCollection) {
        this.fbsProjectCollection = fbsProjectCollection;
    }

    public Collection<FbsInterest> getFbsInterestCollection() {
        return fbsInterestCollection;
    }

    public void setFbsInterestCollection(Collection<FbsInterest> fbsInterestCollection) {
        this.fbsInterestCollection = fbsInterestCollection;
    }

    public Collection<FbsBank> getFbsBankCollection() {
        return fbsBankCollection;
    }

    public void setFbsBankCollection(Collection<FbsBank> fbsBankCollection) {
        this.fbsBankCollection = fbsBankCollection;
    }

    public Collection<FbsUser> getFbsUserCollection() {
        return fbsUserCollection;
    }

    public void setFbsUserCollection(Collection<FbsUser> fbsUserCollection) {
        this.fbsUserCollection = fbsUserCollection;
    }

    public Collection<FbsBrokerCat> getFbsBrokerCatCollection() {
        return fbsBrokerCatCollection;
    }

    public void setFbsBrokerCatCollection(Collection<FbsBrokerCat> fbsBrokerCatCollection) {
        this.fbsBrokerCatCollection = fbsBrokerCatCollection;
    }

    public Collection<FbsReport> getFbsReportCollection() {
        return fbsReportCollection;
    }

    public void setFbsReportCollection(Collection<FbsReport> fbsReportCollection) {
        this.fbsReportCollection = fbsReportCollection;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getMaxFlats() {
        return maxFlats;
    }

    public void setMaxFlats(Integer maxFlats) {
        this.maxFlats = maxFlats;
    }

    public Integer getMaxProjects() {
        return maxProjects;
    }

    public void setMaxProjects(Integer maxProjects) {
        this.maxProjects = maxProjects;
    }

    public String getCopyRight() {
        return copyRight;
    }

    public void setCopyRight(String copyRight) {
        this.copyRight = copyRight;
    }
    

    public Collection<FbsServicetax> getFbsServicetaxCollection() {
        return fbsServicetaxCollection;
    }

    public void setFbsServicetaxCollection(Collection<FbsServicetax> fbsServicetaxCollection) {
        this.fbsServicetaxCollection = fbsServicetaxCollection;
    }

    public Collection<FbsMailsetting> getFbsMailSettingCollection() {
        return fbsMailSettingCollection;
    }

    public void setFbsMailSettingCollection(Collection<FbsMailsetting> fbsMailSettingCollection) {
        this.fbsMailSettingCollection = fbsMailSettingCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (companyId != null ? companyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsCompany)) {
            return false;
        }
        FbsCompany other = (FbsCompany) object;
        if ((this.companyId == null && other.companyId != null) || (this.companyId != null && !this.companyId.equals(other.companyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsCompany[companyId=" + companyId + "]";
    }
}
