/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smp.realerp.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author smp
 */
@Entity
@Table(name = "fbs_project")
@NamedQueries({
    @NamedQuery(name = "FbsProject.findAll", query = "SELECT f FROM FbsProject f"),
    @NamedQuery(name = "FbsProject.findByCompany", query = "SELECT f FROM FbsProject f WHERE f.fbsCompany= :fbsCompany"),
    @NamedQuery(name = "FbsProject.findByProjId", query = "SELECT f FROM FbsProject f WHERE f.projId = :projId"),
    @NamedQuery(name = "FbsProject.findByProjCode", query = "SELECT f FROM FbsProject f WHERE f.projCode = :projCode"),
    @NamedQuery(name = "FbsProject.findByProjName", query = "SELECT f FROM FbsProject f WHERE f.projName = :projName"),
    @NamedQuery(name = "FbsProject.findByProjType", query = "SELECT f FROM FbsProject f WHERE f.projType = :projType"),
    @NamedQuery(name = "FbsProject.findByAddress", query = "SELECT f FROM FbsProject f WHERE f.address = :address"),
    @NamedQuery(name = "FbsProject.findByState", query = "SELECT f FROM FbsProject f WHERE f.state = :state"),
    @NamedQuery(name = "FbsProject.findByCity", query = "SELECT f FROM FbsProject f WHERE f.city = :city"),
    @NamedQuery(name = "FbsProject.findByStartDt", query = "SELECT f FROM FbsProject f WHERE f.startDt = :startDt"),
    @NamedQuery(name = "FbsProject.findByEndDt", query = "SELECT f FROM FbsProject f WHERE f.endDt = :endDt"),
    @NamedQuery(name = "FbsProject.findByImagePath", query = "SELECT f FROM FbsProject f WHERE f.imagePath = :imagePath"),
    @NamedQuery(name = "FbsProject.findMaxProjId", query = "SELECT MAX(f.projId) FROM FbsProject f")})
public class FbsProject implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PROJ_ID")
    private Integer projId;
    @Column(name = "PROJ_CODE")
    private String projCode;
    @Column(name = "PROJ_NAME")
    private String projName;
    @Column(name = "PROJ_TYPE")
    private String projType;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "STATE")
    private String state;
    @Column(name = "CITY")
    private String city;
    @Column(name = "START_DT")
    @Temporal(TemporalType.DATE)
    private Date startDt;
    @Column(name = "END_DT")
    @Temporal(TemporalType.DATE)
    private Date endDt;
    @Column(name = "IMAGE_PATH")
    private String imagePath;
    @Column(name = "PROJ_ABVR")
    private String projAbvr;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsProject")
    private Collection<FbsPlc> fbsPlcCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsProject")
    private Collection<FbsParkingType> fbsParkingTypeCollection;
    @JoinColumn(name = "FK_COMPANY_ID", referencedColumnName = "COMPANY_ID")
    @ManyToOne(optional = false)
    private FbsCompany fbsCompany;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsProject")
    private Collection<FbsFlatType> fbsFlatTypeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsProject")
    private Collection<FbsPlanname> fbsPlannameCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsProject")
    private Collection<FbsCharge> fbsChargeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsProject")
    private Collection<FbsBlock> fbsBlockCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsProject")
    private Collection<FbsComplaint> fbsComplaintCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsProject")
    private Collection<FbsFacility> fbsFacilityCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsProject")
    private Collection<FbsProjectAllot> fbsProjectAllotCollection;

    public FbsProject() {
    }

    public FbsProject(String str) {
        this.projName = str;
    }

    public FbsProject(Integer projId) {
        this.projId = projId;
    }

    public Integer getProjId() {
        return projId;
    }

    public void setProjId(Integer projId) {
        this.projId = projId;
    }

    public String getProjCode() {
        return projCode;
    }

    public void setProjCode(String projCode) {
        this.projCode = projCode;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getProjType() {
        return projType;
    }

    public void setProjType(String projType) {
        this.projType = projType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getStartDt() {
        return startDt;
    }

    public void setStartDt(Date startDt) {
        this.startDt = startDt;
    }

    public Date getEndDt() {
        return endDt;
    }

    public void setEndDt(Date endDt) {
        this.endDt = endDt;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getProjAbvr() {
        return projAbvr;
    }

    public void setProjAbvr(String projAbvr) {
        this.projAbvr = projAbvr;
    }
    

    public Collection<FbsPlc> getFbsPlcCollection() {
        return fbsPlcCollection;
    }

    public void setFbsPlcCollection(Collection<FbsPlc> fbsPlcCollection) {
        this.fbsPlcCollection = fbsPlcCollection;
    }

    public Collection<FbsParkingType> getFbsParkingTypeCollection() {
        return fbsParkingTypeCollection;
    }

    public void setFbsParkingTypeCollection(Collection<FbsParkingType> fbsParkingTypeCollection) {
        this.fbsParkingTypeCollection = fbsParkingTypeCollection;
    }

    public FbsCompany getFbsCompany() {
        return fbsCompany;
    }

    public void setFbsCompany(FbsCompany fbsCompany) {
        this.fbsCompany = fbsCompany;
    }

    public Collection<FbsFlatType> getFbsFlatTypeCollection() {
        return fbsFlatTypeCollection;
    }

    public void setFbsFlatTypeCollection(Collection<FbsFlatType> fbsFlatTypeCollection) {
        this.fbsFlatTypeCollection = fbsFlatTypeCollection;
    }

    public Collection<FbsPlanname> getFbsPlannameCollection() {
        return fbsPlannameCollection;
    }

    public void setFbsPlannameCollection(Collection<FbsPlanname> fbsPlannameCollection) {
        this.fbsPlannameCollection = fbsPlannameCollection;
    }

    public Collection<FbsCharge> getFbsChargeCollection() {
        return fbsChargeCollection;
    }

    public void setFbsChargeCollection(Collection<FbsCharge> fbsChargeCollection) {
        this.fbsChargeCollection = fbsChargeCollection;
    }

    public Collection<FbsBlock> getFbsBlockCollection() {
        return fbsBlockCollection;
    }

    public void setFbsBlockCollection(Collection<FbsBlock> fbsBlockCollection) {
        this.fbsBlockCollection = fbsBlockCollection;
    }

    public Collection<FbsComplaint> getFbsComplaintCollection() {
        return fbsComplaintCollection;
    }

    public void setFbsComplaintCollection(Collection<FbsComplaint> fbsComplaintCollection) {
        this.fbsComplaintCollection = fbsComplaintCollection;
    }

    public Collection<FbsFacility> getFbsFacilityCollection() {
        return fbsFacilityCollection;
    }

    public void setFbsFacilityCollection(Collection<FbsFacility> fbsFacilityCollection) {
        this.fbsFacilityCollection = fbsFacilityCollection;
    }

    public Collection<FbsProjectAllot> getFbsProjectAllotCollection() {
        return fbsProjectAllotCollection;
    }

    public void setFbsProjectAllotCollection(Collection<FbsProjectAllot> fbsProjectAllotCollection) {
        this.fbsProjectAllotCollection = fbsProjectAllotCollection;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (projId != null ? projId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsProject)) {
            return false;
        }
        FbsProject other = (FbsProject) object;
        if ((this.projId == null && other.projId != null) || (this.projId != null && !this.projId.equals(other.projId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsProject[projId=" + projId + "]";
    }

}
