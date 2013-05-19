/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.managedbean;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;
import org.smp.realerp.entity.FbsFlat;


/**
 *
 * @author smp
 */
@ManagedBean
@RequestScoped
public class FlatDataModelBean extends ListDataModel<FbsFlat> implements SelectableDataModel<FbsFlat> {

   public FlatDataModelBean () {
    }

    public FlatDataModelBean (List<FbsFlat> data) {
        super(data);
    }
    
    @Override
    public FbsFlat getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data
        
        List<FbsFlat> FbsFlatList = (List<FbsFlat>) getWrappedData();
        
        for(FbsFlat flat : FbsFlatList) {
            if(flat.getUnitCode().toString().equals(rowKey))
                return flat;
        }
        
        return null;
    }

    @Override
    public Object getRowKey(FbsFlat fbsFlat) {
        return fbsFlat.getUnitCode();
    }
}
