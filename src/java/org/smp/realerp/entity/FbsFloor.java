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
@Table(name = "fbs_floor")
@NamedQueries({
    @NamedQuery(name = "FbsFloor.findAll", query = "SELECT f FROM FbsFloor f"),
    @NamedQuery(name = "FbsFloor.findByFloorId", query = "SELECT f FROM FbsFloor f WHERE f.floorId = :floorId"),
    @NamedQuery(name = "FbsFloor.findByFloorNo", query = "SELECT f FROM FbsFloor f WHERE f.floorNo = :floorNo"),
    @NamedQuery(name = "FbsFloor.findMaxFloorNo", query = "SELECT MAX(f.floorNo) FROM FbsFloor f where f.fbsBlock = :fbsBlock")})
public class FbsFloor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "FLOOR_ID")
    private Integer floorId;
    @Column(name = "FLOOR_NO")
    private Integer floorNo;
    @JoinColumn(name = "FK_BLOCK_ID", referencedColumnName = "BLOCK_ID")
    @ManyToOne(optional = false)
    private FbsBlock fbsBlock;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsFloor")
    private Collection<FbsFlat> fbsFlatCollection;

    public FbsFloor() {
    }

    public FbsFloor(Integer floorId) {
        this.floorId = floorId;
    }

    public Integer getFloorId() {
        return floorId;
    }

    public void setFloorId(Integer floorId) {
        this.floorId = floorId;
    }

    public Integer getFloorNo() {
        return floorNo;
    }

    public void setFloorNo(Integer floorNo) {
        this.floorNo = floorNo;
    }

    public FbsBlock getFbsBlock() {
        return fbsBlock;
    }

    public void setFbsBlock(FbsBlock fbsBlock) {
        this.fbsBlock = fbsBlock;
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
        hash += (floorId != null ? floorId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsFloor)) {
            return false;
        }
        FbsFloor other = (FbsFloor) object;
        if ((this.floorId == null && other.floorId != null) || (this.floorId != null && !this.floorId.equals(other.floorId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsFloor[floorId=" + floorId + "]";
    }
}
