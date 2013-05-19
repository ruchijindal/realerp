/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;

/**
 *
 * @author SMP Technologies
 */
@Entity
@Table(name = "fbs_login")
@NamedQueries({
    @NamedQuery(name = "FbsLogin.findByUserId&Password", query = "SELECT f FROM FbsLogin f WHERE f.userId = :userId AND f.password = :password"),
    @NamedQuery(name = "FbsLogin.findAll", query = "SELECT f FROM FbsLogin f"),
    @NamedQuery(name = "FbsLogin.findByLoginId", query = "SELECT f FROM FbsLogin f WHERE f.loginId = :loginId"),
    @NamedQuery(name = "FbsLogin.findByUserId", query = "SELECT f FROM FbsLogin f WHERE f.userId = :userId"),
    @NamedQuery(name = "FbsLogin.findByUserName", query = "SELECT f FROM FbsLogin f WHERE f.userName = :userName"),
    @NamedQuery(name = "FbsLogin.findByCreatedBy", query = "SELECT f FROM FbsLogin f WHERE f.createdBy = :createdBy"),
    @NamedQuery(name = "FbsLogin.findByTelPhone", query = "SELECT f FROM FbsLogin f WHERE f.telPhone = :telPhone"),
    @NamedQuery(name = "FbsLogin.findByMobile", query = "SELECT f FROM FbsLogin f WHERE f.mobile = :mobile"),
    @NamedQuery(name = "FbsLogin.findByAddress", query = "SELECT f FROM FbsLogin f WHERE f.address = :address"),
    @NamedQuery(name = "FbsLogin.findByCity", query = "SELECT f FROM FbsLogin f WHERE f.city = :city"),
    @NamedQuery(name = "FbsLogin.findByState", query = "SELECT f FROM FbsLogin f WHERE f.state = :state"),
    @NamedQuery(name = "FbsLogin.findByEmail", query = "SELECT f FROM FbsLogin f WHERE f.email = :email"),
    @NamedQuery(name = "FbsLogin.findByWebsite", query = "SELECT f FROM FbsLogin f WHERE f.website = :website"),
    @NamedQuery(name = "FbsLogin.findByPassword", query = "SELECT f FROM FbsLogin f WHERE f.password = :password"),
    @NamedQuery(name = "FbsLogin.findBySessionId", query = "SELECT f FROM FbsLogin f WHERE f.sessionId = :sessionId"),
    @NamedQuery(name = "FbsLogin.findByOldPassword", query = "SELECT f FROM FbsLogin f WHERE f.oldPassword = :oldPassword")})
public class FbsLogin implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "LOGIN_ID")
    private Integer loginId;
    @Basic(optional = false)
    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "USER_NAME")
    private String userName;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "TEL_PHONE")
    private String telPhone;
    @Column(name = "MOBILE")
    private String mobile;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "CITY")
    private String city;
    @Column(name = "STATE")
    private String state;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "WEBSITE")
    private String website;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "SESSION_ID")
    private String sessionId;
    @Column(name = "OLD_PASSWORD")
    private String oldPassword;
    @JoinColumn(name = "FK_ROLL_ID", referencedColumnName = "ROLL_ID")
    @ManyToOne(optional = false)
    private FbsUser fbsUser;
    @JoinColumn(name = "FK_COMPANY_ID", referencedColumnName = "COMPANY_ID")
    @ManyToOne(optional = false)
    private FbsCompany fbsCompany;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsLogin")
    private Collection<FbsProjectAllot> fbsProjectAllotCollection;

    public FbsLogin() {
    }

    public FbsLogin(Integer loginId) {
        this.loginId = loginId;
    }

    public FbsLogin(Integer loginId, String userId) {
        this.loginId = loginId;
        this.userId = userId;
    }

    public Integer getLoginId() {
        return loginId;
    }

    public void setLoginId(Integer loginId) {
        this.loginId = loginId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public FbsUser getFbsUser() {
        return fbsUser;
    }

    public void setFbsUser(FbsUser fbsUser) {
        this.fbsUser = fbsUser;
    }

    public FbsCompany getFbsCompany() {
        return fbsCompany;
    }

    public void setFbsCompany(FbsCompany fbsCompany) {
        this.fbsCompany = fbsCompany;
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
        hash += (loginId != null ? loginId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsLogin)) {
            return false;
        }
        FbsLogin other = (FbsLogin) object;
        if ((this.loginId == null && other.loginId != null) || (this.loginId != null && !this.loginId.equals(other.loginId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsLogin[loginId=" + loginId + "]";
    }
}
