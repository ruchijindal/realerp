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
import javax.persistence.Lob;
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
@Table(name = "fbs_applicant")
@NamedQueries({
    @NamedQuery(name = "FbsApplicant.findByStatus&ApplicantFlag", query = "SELECT f FROM FbsApplicant f where f.status = :status AND f.applicantFlag = :applicantFlag"),
    @NamedQuery(name = "FbsApplicant.findByFbsFlat&Status&ApplicantFlag", query = "SELECT f FROM FbsApplicant f where f.fbsFlat= :fbsFlat AND f.status = :status AND f.applicantFlag = :applicantFlag"),
    @NamedQuery(name = "FbsApplicant.findByApplicant&Status&ApplicantFlag", query = "SELECT f FROM FbsApplicant f where f.applicantName LIKE :applicantName AND f.status = :status AND f.applicantFlag = :applicantFlag"),    
    @NamedQuery(name = "FbsApplicant.findByFbsFlat&Status", query = "SELECT f FROM FbsApplicant f where f.fbsFlat= :fbsFlat AND f.status = :status"),
    @NamedQuery(name = "FbsApplicant.findAll", query = "SELECT f FROM FbsApplicant f"),
    @NamedQuery(name = "FbsApplicant.findByApplicantId", query = "SELECT f FROM FbsApplicant f WHERE f.applicantId = :applicantId"),
    @NamedQuery(name = "FbsApplicant.findByApplicantName", query = "SELECT f FROM FbsApplicant f WHERE f.applicantName = :applicantName"),
    @NamedQuery(name = "FbsApplicant.findBySWD", query = "SELECT f FROM FbsApplicant f WHERE f.sWD = :sWD"),
    @NamedQuery(name = "FbsApplicant.findByNationality", query = "SELECT f FROM FbsApplicant f WHERE f.nationality = :nationality"),
    @NamedQuery(name = "FbsApplicant.findByDob", query = "SELECT f FROM FbsApplicant f WHERE f.dob = :dob"),
    @NamedQuery(name = "FbsApplicant.findByProfession", query = "SELECT f FROM FbsApplicant f WHERE f.profession = :profession"),
    @NamedQuery(name = "FbsApplicant.findByPanNo", query = "SELECT f FROM FbsApplicant f WHERE f.panNo = :panNo"),
    @NamedQuery(name = "FbsApplicant.findByDlNo", query = "SELECT f FROM FbsApplicant f WHERE f.dlNo = :dlNo"),
    @NamedQuery(name = "FbsApplicant.findByPassportNo", query = "SELECT f FROM FbsApplicant f WHERE f.passportNo = :passportNo"),
    @NamedQuery(name = "FbsApplicant.findByResAdd", query = "SELECT f FROM FbsApplicant f WHERE f.resAdd = :resAdd"),
    @NamedQuery(name = "FbsApplicant.findByOffAdd", query = "SELECT f FROM FbsApplicant f WHERE f.offAdd = :offAdd"),
    @NamedQuery(name = "FbsApplicant.findByTelephone", query = "SELECT f FROM FbsApplicant f WHERE f.telephone = :telephone"),
    @NamedQuery(name = "FbsApplicant.findByMobile", query = "SELECT f FROM FbsApplicant f WHERE f.mobile = :mobile"),
    @NamedQuery(name = "FbsApplicant.findByFax", query = "SELECT f FROM FbsApplicant f WHERE f.fax = :fax"),
    @NamedQuery(name = "FbsApplicant.findByEmail", query = "SELECT f FROM FbsApplicant f WHERE f.email = :email"),
    @NamedQuery(name = "FbsApplicant.findByApplicantFlag", query = "SELECT f FROM FbsApplicant f WHERE f.applicantFlag = :applicantFlag"),
    @NamedQuery(name = "FbsApplicant.findByDocImage", query = "SELECT f FROM FbsApplicant f WHERE f.docImage = :docImage"),
    @NamedQuery(name = "FbsApplicant.findByStatus", query = "SELECT f FROM FbsApplicant f WHERE f.status = :status"),
    @NamedQuery(name = "FbsApplicant.findByTransferDate", query = "SELECT f FROM FbsApplicant f WHERE f.transferDate = :transferDate")})
public class FbsApplicant implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "APPLICANT_ID")
    private Integer applicantId;
    @Column(name = "APPLICANT_NAME")
    private String applicantName;
    @Column(name = "S_W_D")
    private String sWD;
    @Column(name = "NATIONALITY")
    private String nationality;
    @Column(name = "DOB")
    @Temporal(TemporalType.DATE)
    private Date dob;
    @Column(name = "PROFESSION")
    private String profession;
    @Column(name = "PAN_NO")
    private String panNo;
    @Column(name = "DL_NO")
    private String dlNo;
    @Column(name = "PASSPORT_NO")
    private String passportNo;
    @Column(name = "RES_ADD")
    private String resAdd;
    @Column(name = "OFF_ADD")
    private String offAdd;
    @Column(name = "TELEPHONE")
    private String telephone;
    @Column(name = "MOBILE")
    private String mobile;
    @Column(name = "FAX")
    private String fax;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "APPLICANT_FLAG")
    private Integer applicantFlag;
    @Lob
    @Column(name = "IMAGE1_PATH")
    private byte[] image1Path;
    @Column(name = "DOC_IMAGE")
    private String docImage;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "TRANSFER_DATE")
    @Temporal(TemporalType.DATE)
    private Date transferDate;
    @JoinColumn(name = "FK_UNIT_CODE", referencedColumnName = "UNIT_CODE")
    @ManyToOne(optional = false)
    private FbsFlat fbsFlat;

    public FbsApplicant() {
    }

    public FbsApplicant(Integer applicantId) {
        this.applicantId = applicantId;
    }

    public Integer getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(Integer applicantId) {
        this.applicantId = applicantId;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getSWD() {
        return sWD;
    }

    public void setSWD(String sWD) {
        this.sWD = sWD;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getDlNo() {
        return dlNo;
    }

    public void setDlNo(String dlNo) {
        this.dlNo = dlNo;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getResAdd() {
        return resAdd;
    }

    public void setResAdd(String resAdd) {
        this.resAdd = resAdd;
    }

    public String getOffAdd() {
        return offAdd;
    }

    public void setOffAdd(String offAdd) {
        this.offAdd = offAdd;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getApplicantFlag() {
        return applicantFlag;
    }

    public void setApplicantFlag(Integer applicantFlag) {
        this.applicantFlag = applicantFlag;
    }

    public byte[] getImage1Path() {
        return image1Path;
    }

    public void setImage1Path(byte[] image1Path) {
        this.image1Path = image1Path;
    }

    public String getDocImage() {
        return docImage;
    }

    public void setDocImage(String docImage) {
        this.docImage = docImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
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
        hash += (applicantId != null ? applicantId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsApplicant)) {
            return false;
        }
        FbsApplicant other = (FbsApplicant) object;
        if ((this.applicantId == null && other.applicantId != null) || (this.applicantId != null && !this.applicantId.equals(other.applicantId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsApplicant[applicantId=" + applicantId + "]";
    }

}
