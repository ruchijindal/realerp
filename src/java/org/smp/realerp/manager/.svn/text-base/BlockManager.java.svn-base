/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.manager;


import java.util.*;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.smp.realerp.entity.*;
import org.smp.realerp.session.*;

/**
 *
 * @author smp
 */
@Stateless
@LocalBean
public class BlockManager {

    @EJB
    FbsProjectFacade fbsProjectFacade;
    @EJB
    FbsBlockFacade fbsBlockFacade;
    @EJB
    FbsFloorFacade fbsFloorFacade;
    @EJB
    FbsFlatFacade fbsFlatFacade;
    @EJB
    FbsFlatTypeFacade fbsFlatTypeFacade;
    @EJB
    FbsPlcFacade fbsPlcFacade;
    @EJB
    FbsPlcAllotFacade fbsPlcAllotFacade;
    @PersistenceContext(unitName = "RealErpPU")
    EntityManager em;
    FbsFlat fbsFlat = new FbsFlat();
    FbsFloor fbsFloor = new FbsFloor();
    FbsPlcAllot fbsPlcAllot = new FbsPlcAllot();
    FbsPlc fbsPlc = new FbsPlc();
    List<FbsPlcAllot> fbsPlcAllotList = new ArrayList<FbsPlcAllot>();

    public void addBlock(FbsBlock fbsBlock, int noOfFloor, int noOfFlat) {
        fbsBlockFacade.create(fbsBlock);
        String floorNo = "";
        String flatNo = "";
        for (int i = 0; i < noOfFloor; i++) {
            fbsFloor = new FbsFloor();
            fbsFloor.setFbsBlock(fbsBlock);
            fbsFloor.setFloorNo(i);
            fbsFloorFacade.create(fbsFloor);
            for (int k = 1; k <= noOfFlat; k++) {
                fbsFlat = new FbsFlat();
                fbsFlat.setStatus("unbooked");
                floorNo = "";
                if (String.valueOf(fbsFloor.getFloorNo()).length() == 1) {
                    floorNo = "0" + fbsFloor.getFloorNo();
                } else {
                    floorNo = "" + fbsFloor.getFloorNo();
                }
                flatNo = "";
                if (k >= 1 && k <= 9) {
                    flatNo = floorNo + "0" + k;
                } else {
                    flatNo = floorNo + k;
                }
                fbsFlat.setFlatNo(flatNo);
                fbsFlat.setFbsFloor(fbsFloor);
                fbsFlat.setLockStatus("unlocked");
                fbsFlatFacade.create(fbsFlat);
            }
        }
    }

    public void editBlock(FbsBlock fbsBlock) {
        try {
            fbsBlockFacade.edit(fbsBlock);
        } catch (Exception ex) {
            System.out.println("Exception in edit block :" + ex);
        }
    }

    public void deleteBlock(FbsBlock fbsBlock) {
        try {
            fbsBlockFacade.remove(fbsBlock);
        } catch (Exception ex) {
            System.out.println("Exception in deleting Block" + ex);
        }
    }

    public FbsBlock populateFbsBlock(int blockId) {
        return fbsBlockFacade.find(blockId);
    }

    public FbsProject populateFbsProject(int projectId) {
        return fbsProjectFacade.find(projectId);
    }


    public void addFlatSpecification(FbsFlat[] selectFlatList, FbsFlatType fbsFlatType) {
        try {
            int unitCode = 0;
            for (int i = 0; i < selectFlatList.length; i++) {
                unitCode = selectFlatList[i].getUnitCode();
                fbsFlat = new FbsFlat();
                fbsFlat = fbsFlatFacade.find(unitCode);
                fbsFlat.setFbsFlatType(fbsFlatType);
                fbsFlatFacade.edit(fbsFlat);
            }
        } catch (Exception ex) {
            System.out.println("Exception in adding flat specification:" + ex);

        }
    }

   

    public void addPlc(FbsFlat[] selectFlatList, List<String> selectPlcList) {
        try {

            boolean flag = false;
            for (int i = 0; i < selectPlcList.size(); i++) {
                if (selectPlcList.get(i).equals("0")) {
                    flag = true;
                    break;
                }
            }

            if (flag == false) {
                for (int i = 0; i < selectFlatList.length; i++) {
                    for (int k = 0; k < selectPlcList.size(); k++) {
                        fbsPlc = new FbsPlc();
                        int plcId;
                        plcId = Integer.parseInt(selectPlcList.get(k));
                        fbsPlc = fbsPlcFacade.find(plcId);
                        fbsPlcAllot = new FbsPlcAllot();
                        fbsPlcAllot.setFbsPlc(fbsPlc);
                        fbsPlcAllot.setFbsFlat(selectFlatList[i]);
                        if (!fbsPlcAllotFacade.findAll().contains(fbsPlcAllot)) {
                            fbsPlcAllotFacade.create(fbsPlcAllot);
                        }

                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Exception in add PLC: " + ex);
        }
    }

    public void deleteAllotedPlc(FbsFlat fbsFlat) {
        try {
            fbsPlcAllotList.clear();
            fbsPlcAllotList = (List<FbsPlcAllot>) fbsFlat.getFbsPlcAllotCollection();
            System.out.println("fbsPlcAllotList.size " + fbsPlcAllotList.size());
            for (int i = 0; i < fbsPlcAllotList.size(); i++) {
                fbsPlcAllotFacade.remove(fbsPlcAllotList.get(i));
            }
        } catch (Exception ex) {
            System.out.println("Exception in deleting alloted Plc" + ex);
        }

    }

    public Collection<FbsPlcAllot> populateFlatPlcList(FbsFlat fbsFlat) {
        return fbsFlat.getFbsPlcAllotCollection();
    }

    public FbsFlat populateFbsFlat(int unitCode) {
        return fbsFlatFacade.find(unitCode);
    }

    public int lockFbsFlat(FbsFlat fbsFlat) {

        if (fbsFlat.getLockStatus().equals("unlocked")) {
            fbsFlat.setLockStatus("locked");
            fbsFlatFacade.edit(fbsFlat);
            return 1;
        } else {
            fbsFlat.setLockStatus("unlocked");
            fbsFlatFacade.edit(fbsFlat);
            return 2;
        }

    }

    public void addFlat(FbsBlock fbsBlock, int floorId, int noOfFlat) {
        try {
            String floorNo = "";
            String flatNo = "";
            if (floorId == 0) {
                //@NamedQuery(name = "FbsFloor.findMaxFloorNo", query = "SELECT MAX(f.floorNo) FROM FbsFloor f where f.fbsBlock = :fbsBlock")
                Integer maxFloorNo = (Integer) em.createNamedQuery("FbsFloor.findMaxFloorNo").setParameter("fbsBlock", fbsBlock).getResultList().get(0);
                fbsFloor = new FbsFloor();
                fbsFloor.setFbsBlock(fbsBlock);
                if (maxFloorNo == null) {
                    fbsFloor.setFloorNo(0);
                } else {
                    fbsFloor.setFloorNo(maxFloorNo + 1);
                }
                fbsFloorFacade.create(fbsFloor);
                for (int k = 1; k <= noOfFlat; k++) {
                    fbsFlat = new FbsFlat();
                    fbsFlat.setStatus("unbooked");
                    floorNo = "";
                    if (String.valueOf(fbsFloor.getFloorNo()).length() == 1) {
                        floorNo = "0" + fbsFloor.getFloorNo();
                    } else {
                        floorNo = "" + fbsFloor.getFloorNo();
                    }
                    flatNo = "";
                    if (k >= 1 && k <= 9) {
                        flatNo = floorNo + "0" + k;
                    } else {
                        flatNo = floorNo + k;
                    }
                    fbsFlat.setFlatNo(flatNo);
                    fbsFlat.setFbsFloor(fbsFloor);
                    fbsFlat.setLockStatus("unlocked");
                    fbsFlatFacade.create(fbsFlat);
                }
            } else {
                fbsFloor = new FbsFloor();
                fbsFloor = fbsFloorFacade.find(floorId);
                floorNo = "";

                if (String.valueOf(fbsFloor.getFloorNo()).length() == 1) {
                    floorNo = "0" + fbsFloor.getFloorNo();
                } else {
                    floorNo = "" + fbsFloor.getFloorNo();
                }
                for (int k = 0; k < noOfFlat; k++) {
                    flatNo = "";
                    fbsFlat = new FbsFlat();
                    fbsFlat.setStatus("unbooked");
                    //@NamedQuery(name = "FbsFloor.findMaxFlatNo", query = "SELECT MAX(f.flatNo) FROM FbsFlat f where f.fbsFloor = :fbsFloor")
                    String maxFlatNo = (String) em.createNamedQuery("FbsFloor.findMaxFlatNo").setParameter("fbsFloor", fbsFloor).getResultList().get(0);
                    if (maxFlatNo == null) {
                        flatNo = floorNo + "0" + 1;
                    } else {
                        maxFlatNo = maxFlatNo.substring(2);
                        System.out.println("maxFlatNo+++ " + maxFlatNo);
                        if ((Integer.parseInt(maxFlatNo) >= 1) && (Integer.parseInt(maxFlatNo) < 9)) {
                            flatNo = floorNo + "0" + (Integer.parseInt(maxFlatNo) + 1);
                        } else {
                            flatNo = floorNo + (Integer.parseInt(maxFlatNo) + 1);
                        }

                    }
                    fbsFlat.setFlatNo(flatNo);
                    fbsFlat.setFbsFloor(fbsFloor);
                    fbsFlat.setLockStatus("unlocked");
                    fbsFlatFacade.create(fbsFlat);
                }

            }
        } catch (Exception ex) {
            System.out.println("Exception in adding flat" + ex);
        }
    }
    
    
    public void deleteFlat(FbsFlat fbsFlat)
    {
        try{
            fbsFlatFacade.remove(fbsFlat);
        }
        catch(Exception ex)
        {
            System.out .println("Exception in deleting flat: "+ex);
        }
    }

    public List<FbsFlat> populateFbsFlatList(int blockId) {
        List<FbsFlat> fbsFlatList = new ArrayList<FbsFlat>();
        FbsBlock fbsBlock = new FbsBlock();
        fbsBlock = fbsBlockFacade.find(blockId);
        List<FbsFloor> showfbsFloorList = new ArrayList<FbsFloor>();
        showfbsFloorList = (List<FbsFloor>) fbsBlock.getFbsFloorCollection();
        for (int i = 0; i < showfbsFloorList.size(); i++) {
            fbsFlatList.addAll(showfbsFloorList.get(i).getFbsFlatCollection());
        }
        Collections.sort(fbsFlatList, new Comparator() {

            public int compare(Object o1, Object o2) {
                FbsFlat f1 = (FbsFlat) o1;
                FbsFlat f2 = (FbsFlat) o2;
                return f1.getFlatNo().compareTo(f2.getFlatNo());
            }
        });
        return fbsFlatList;

    }
    public List<FbsFlat> sortFlatList(List<FbsFlat> fbsFlatList)
    {
        Collections.sort(fbsFlatList, new Comparator() {

            public int compare(Object o1, Object o2) {
                FbsFlat f1 = (FbsFlat) o1;
                FbsFlat f2 = (FbsFlat) o2;
                return f1.getFlatNo().compareTo(f2.getFlatNo());
            }
        });
        return fbsFlatList;
    }
    
    public List<FbsFloor> sortFloorList(List<FbsFloor> fbsFloorList)
    {
        Collections.sort(fbsFloorList, new Comparator() {

            public int compare(Object o1, Object o2) {
                FbsFloor f1 = (FbsFloor) o1;
                FbsFloor f2 = (FbsFloor) o2;
                return f1.getFloorNo().compareTo(f2.getFloorNo());
            }
        });
        return fbsFloorList;
    }
   

    public String convertFloorNo(int floorNo) {
        if (floorNo == 0) {
            return "Ground";
        }
        if (floorNo == 1) {
            return "1st";
        }
        if (floorNo == 2) {
            return "2nd";
        }
        if (floorNo == 3) {
            return "3rd";
        } else {
            return floorNo + "th";
        }

    }
}
