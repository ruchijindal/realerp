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
@Table(name = "fbs_block")
@NamedQueries({
    @NamedQuery(name = "FbsBlock.findAll", query = "SELECT f FROM FbsBlock f"),
    @NamedQuery(name = "FbsBlock.findByBlockId", query = "SELECT f FROM FbsBlock f WHERE f.blockId = :blockId"),
    @NamedQuery(name = "FbsBlock.findByBlockName", query = "SELECT f FROM FbsBlock f WHERE f.blockName = :blockName"),
    @NamedQuery(name = "FbsBlock.findByImagePath", query = "SELECT f FROM FbsBlock f WHERE f.imagePath = :imagePath"),
    @NamedQuery(name = "FbsBlock.findByStatus", query = "SELECT f FROM FbsBlock f WHERE f.status = :status")})
public class FbsBlock implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "BLOCK_ID")
    private Integer blockId;
    @Column(name = "BLOCK_NAME")
    private String blockName;
    @Column(name = "BLOCK_ABVR")
    private String blockAbvr;
    @Column(name = "IMAGE_PATH")
    private String imagePath;
    @Column(name = "STATUS")
    private String status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsBlock")
    private Collection<FbsFloor> fbsFloorCollection;
    @JoinColumn(name = "FK_PROJ_ID", referencedColumnName = "PROJ_ID")
    @ManyToOne(optional = false)
    private FbsProject fbsProject;

    public FbsBlock() {
    }

    public FbsBlock(Integer blockId) {
        this.blockId = blockId;
    }

    public Integer getBlockId() {
        return blockId;
    }

    public void setBlockId(Integer blockId) {
        this.blockId = blockId;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Collection<FbsFloor> getFbsFloorCollection() {
        return fbsFloorCollection;
    }

    public void setFbsFloorCollection(Collection<FbsFloor> fbsFloorCollection) {
        this.fbsFloorCollection = fbsFloorCollection;
    }

    public FbsProject getFbsProject() {
        return fbsProject;
    }

    public void setFbsProject(FbsProject fbsProject) {
        this.fbsProject = fbsProject;
    }

    public String getBlockAbvr() {
        return blockAbvr;
    }

    public void setBlockAbvr(String blockAbvr) {
        this.blockAbvr = blockAbvr;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (blockId != null ? blockId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsBlock)) {
            return false;
        }
        FbsBlock other = (FbsBlock) object;
        if ((this.blockId == null && other.blockId != null) || (this.blockId != null && !this.blockId.equals(other.blockId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsBlock[blockId=" + blockId + "]";
    }

}
