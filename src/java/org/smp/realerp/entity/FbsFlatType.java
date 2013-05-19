/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smp.realerp.entity;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author smp
 */
@Entity
@Table(name = "fbs_flat_type")
@NamedQueries({
    @NamedQuery(name = "FbsFlatType.findAll", query = "SELECT f FROM FbsFlatType f"),
    @NamedQuery(name = "FbsFlatType.findByFlatTypeId", query = "SELECT f FROM FbsFlatType f WHERE f.flatTypeId = :flatTypeId"),
    @NamedQuery(name = "FbsFlatType.findByFlatType", query = "SELECT f FROM FbsFlatType f WHERE f.flatType = :flatType"),
    @NamedQuery(name = "FbsFlatType.findByFlatSpecification", query = "SELECT f FROM FbsFlatType f WHERE f.flatSpecification = :flatSpecification"),
    @NamedQuery(name = "FbsFlatType.findByFlatSba", query = "SELECT f FROM FbsFlatType f WHERE f.flatSba = :flatSba"),
    @NamedQuery(name = "FbsFlatType.findByFlatBa", query = "SELECT f FROM FbsFlatType f WHERE f.flatBa = :flatBa"),
    @NamedQuery(name = "FbsFlatType.findByFlatCa", query = "SELECT f FROM FbsFlatType f WHERE f.flatCa = :flatCa"),
    @NamedQuery(name = "FbsFlatType.findByFlatBsp", query = "SELECT f FROM FbsFlatType f WHERE f.flatBsp = :flatBsp")})
public class FbsFlatType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "FLAT_TYPE_ID")
    private Integer flatTypeId;
    @Column(name = "FLAT_TYPE")
    private String flatType;
    @Column(name = "FLAT_SPECIFICATION")
    private String flatSpecification;
    @Column(name = "FLAT_SBA")
    private Double flatSba;
    @Column(name = "FLAT_BA")
    private Double flatBa;
    @Column(name = "FLAT_CA")
    private Double flatCa;
    @Column(name = "FLAT_BSP")
    private Double flatBsp;
    @JoinColumn(name = "FK_PROJ_ID", referencedColumnName = "PROJ_ID")
    @ManyToOne(optional = false)
    private FbsProject fbsProject;
    @OneToMany(mappedBy = "fbsFlatType")
    private Collection<FbsFlat> fbsFlatCollection;

    public FbsFlatType() {
    }

    public FbsFlatType(Integer flatTypeId) {
        this.flatTypeId = flatTypeId;
    }

    public Integer getFlatTypeId() {
        return flatTypeId;
    }

    public void setFlatTypeId(Integer flatTypeId) {
        this.flatTypeId = flatTypeId;
    }

    public String getFlatType() {
        return flatType;
    }

    public void setFlatType(String flatType) {
        this.flatType = flatType;
    }

    public String getFlatSpecification() {
        return flatSpecification;
    }

    public void setFlatSpecification(String flatSpecification) {
        this.flatSpecification = flatSpecification;
    }

    public Double getFlatSba() {
        return flatSba;
    }

    public void setFlatSba(Double flatSba) {
        this.flatSba = flatSba;
    }

    public Double getFlatBa() {
        return flatBa;
    }

    public void setFlatBa(Double flatBa) {
        this.flatBa = flatBa;
    }

    public Double getFlatCa() {
        return flatCa;
    }

    public void setFlatCa(Double flatCa) {
        this.flatCa = flatCa;
    }

    public Double getFlatBsp() {
        return flatBsp;
    }

    public void setFlatBsp(Double flatBsp) {
        this.flatBsp = flatBsp;
    }

    public FbsProject getFbsProject() {
        return fbsProject;
    }

    public void setFbsProject(FbsProject fbsProject) {
        this.fbsProject = fbsProject;
    }

    public Collection<FbsFlat> getFbsFlatCollection() {
        return fbsFlatCollection;
    }

    public void setFbsFlatCollection(Collection<FbsFlat> fbsFlatCollection) {
        this.fbsFlatCollection = fbsFlatCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flatTypeId != null ? flatTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsFlatType)) {
            return false;
        }
        FbsFlatType other = (FbsFlatType) object;
        if ((this.flatTypeId == null && other.flatTypeId != null) || (this.flatTypeId != null && !this.flatTypeId.equals(other.flatTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsFlatType[flatTypeId=" + flatTypeId + "]";
    }

}
