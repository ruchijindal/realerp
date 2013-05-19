/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.managedbean;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultUploadedFile;
import org.smp.realerp.entity.*;
import org.smp.realerp.manager.BlockManager;
import org.smp.realerp.session.FbsBlockFacade;
import org.smp.realerp.session.FbsFlatFacade;
import org.smp.realerp.session.FbsProjectFacade;

/**
 *
 * @author smp
 */
@ManagedBean(name = "blockBean")
@ViewScoped
public class BlockBean {

    @EJB
    FbsBlockFacade fbsBlockFacade;
    @EJB
    BlockManager blockManager;
    @EJB
    FbsProjectFacade fbsProjectFacade;
    @EJB
    FbsFlatFacade fbsFlatFacade;
    static FbsProject fbsProject = new FbsProject();
    FbsBlock fbsBlock = new FbsBlock();
    FbsFloor fbsFloor = new FbsFloor();
    List<FbsBlock> fbsBlockList = new ArrayList<FbsBlock>();
    FbsBlock editFbsBlock = new FbsBlock();
    static FbsBlock delFbsBlock = new FbsBlock();
    int noOfFloor;
    int noOfFlat;
    static FbsBlock selectedFbsBlock = new FbsBlock();
    static List<FbsFlat> fbsFlatList = new ArrayList<FbsFlat>();
    FbsFlat[] selectFlatList;
    static FlatDataModelBean modelFlatList;
    FbsFlatType fbsFlatType = new FbsFlatType();
    static List<FbsPlc> fbsPlcList = new ArrayList<FbsPlc>();
    List<String> selectPlcList = new ArrayList<String>();
    // private Map<Integer,FbsPlc> plcMap = new HashMap<Integer,FbsPlc>();
    List<FbsPlcAllot> plcAllotList = new ArrayList<FbsPlcAllot>();
    static FbsFlat editFbsFlat = new FbsFlat();
    static List<FbsFloor> fbsFloorList = new ArrayList<FbsFloor>();
    static List<FbsFlatType> flatTypeList = new ArrayList<FbsFlatType>();
    static FbsFlat delFbsFlat = new FbsFlat();
    boolean renderGraphicalView[] = new boolean[3];
    List<FbsProject> fbsProjectList = new ArrayList<FbsProject>();
    List<FbsFloor> desfbsFloorList = new ArrayList<FbsFloor>();
    boolean renderProject;
    boolean renderDialog;
    boolean renderDetail = true;
    boolean renderDialog1;

    @PostConstruct
    public void populate() throws IOException {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (request.getParameter("chkRender") == null) {
            if (LoginBean.fbsProject.getProjName().equals("Projects")) {
                fbsProject = new FbsProject();
                renderGraphicalView[0] = true;
                renderGraphicalView[1] = false;
                renderGraphicalView[2] = false;
                fbsProjectList.clear();
                fbsProjectList = LoginBean.fbsProjectList;

            } else {
                fbsProject = new FbsProject();
                fbsProject = blockManager.populateFbsProject(LoginBean.fbsProject.getProjId());
                populateBlock(fbsProject);

            }
        } else {
            System.out.println("projId+++" + fbsProject.getProjId());
            if (request.getParameter("chkRender").equals("0")) {
                fbsProject = new FbsProject();
                renderProject = renderProjectBreadCrum();
                fbsProjectList.clear();
                fbsProjectList = LoginBean.fbsProjectList;
                renderDataGrid(request.getParameter("chkRender"));
            }
            if (request.getParameter("chkRender").equals("1") && !LoginBean.fbsProject.getProjName().equals("Projects")) {
                renderProject = renderProjectBreadCrum();
                fbsProject = new FbsProject();
                fbsProject = blockManager.populateFbsProject(LoginBean.fbsProject.getProjId());
                populateBlock(fbsProject);
                renderDataGrid(request.getParameter("chkRender"));
            }
            if (request.getParameter("chkRender").equals("1") && LoginBean.fbsProject.getProjName().equals("Projects") && fbsProject.getProjId() != null) {
                System.out.println("inside if+++" + fbsProject.getProjId());
                populateBlock(fbsProject);
                renderProject = renderProjectBreadCrum();
                renderDataGrid(request.getParameter("chkRender"));
            }
            if (request.getParameter("chkRender").equals("2")) {
                showBlockDetails(selectedFbsBlock, "g");
                renderProject = renderProjectBreadCrum();
                renderDataGrid(request.getParameter("chkRender"));
            }
        }
    }

    public boolean renderProjectBreadCrum() {
        if (LoginBean.fbsProject.getProjName().equals("Projects")) {
            return true;
        } else {
            return false;
        }

    }

    public void populateBlock(FbsProject selectedFbsProject) {
        fbsProject = new FbsProject();
        fbsProject = blockManager.populateFbsProject(selectedFbsProject.getProjId());
        fbsBlockList.clear();
        fbsBlockList = (List<FbsBlock>) fbsProject.getFbsBlockCollection();
        renderGraphicalView[0] = false;
        renderGraphicalView[1] = true;
        renderGraphicalView[2] = false;
    }

    public void addBlock() throws IOException {
        fbsBlock.setFbsProject(LoginBean.fbsProject);
        if ((fbsProject.getFbsCompany().getMaxFlats() - countunits()) >= noOfFlat) {
            blockManager.addBlock(fbsBlock, noOfFloor, noOfFlat);
            fbsBlock = new FbsBlock();           
            populate();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, " Block Added", ""));
            noOfFlat = 0;
            noOfFloor = 0;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Permission Denied", ""));
        }


    }

    public void edit(FbsBlock fbsBlock) {
        editFbsBlock = new FbsBlock();
        editFbsBlock = fbsBlock;
        renderDialog1 = true;
    }

    public void editBlock() throws IOException {
        blockManager.editBlock(editFbsBlock);
        renderDialog1 = false;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, " Block  Updated", ""));
        populate();
    }

    public int countNoOfFloor(FbsBlock fbsBlock) {
        if (fbsBlock != null) {
            if (fbsBlock.getFbsFloorCollection().isEmpty()) {
                return 0;
            } else {
                return fbsBlock.getFbsFloorCollection().size();
            }
        } else {
            return 0;
        }
    }

    public int countNoOfFlat(FbsBlock fbsBlock) {
        noOfFlat = 0;
        if (fbsBlock != null) {
            for (int i = 0; i < fbsBlock.getFbsFloorCollection().size(); i++) {
                for (int j = 0; j < ((List<FbsFloor>) fbsBlock.getFbsFloorCollection()).get(i).getFbsFlatCollection().size(); j++) {
                    noOfFlat++;
                }
            }


        } else {
            return 0;
        }
        return noOfFlat;
    }

    public void confirmDelBlock(FbsBlock fbsBlock) {
        delFbsBlock = new FbsBlock();
        delFbsBlock = fbsBlock;
    }

    public void deleteBlock() throws IOException {
        blockManager.deleteBlock(delFbsBlock);
        delFbsBlock = new FbsBlock();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Block Deleted", ""));
        populate();
    }

    public void showBlockDetails(FbsBlock fbsBlock, String chkRedirect) throws IOException {
        reset();
        renderGraphicalView[0] = false;
        renderGraphicalView[1] = false;
        renderGraphicalView[2] = true;
        flatTypeList.clear();
        fbsProject = new FbsProject();
        fbsProject = blockManager.populateFbsProject(fbsBlock.getFbsProject().getProjId());
        flatTypeList = (List<FbsFlatType>) fbsProject.getFbsFlatTypeCollection();

        fbsPlcList.clear();
        fbsPlcList = (List<FbsPlc>) fbsProject.getFbsPlcCollection();

        selectedFbsBlock = new FbsBlock();
        selectedFbsBlock = blockManager.populateFbsBlock(fbsBlock.getBlockId());

        fbsFloorList.clear();
        fbsFloorList = (List<FbsFloor>) selectedFbsBlock.getFbsFloorCollection();
        fbsFloorList = blockManager.sortFloorList(fbsFloorList);
        for (int i = fbsFloorList.size() - 1; i >= 0; i--) {
            desfbsFloorList.add(fbsFloorList.get(i));
        }

        fbsFlatList.clear();
        fbsFlatList = blockManager.populateFbsFlatList(selectedFbsBlock.getBlockId());
        modelFlatList = new FlatDataModelBean(fbsFlatList);
        System.out.println("fbsflatList+++++++ " + fbsFlatList.size());
        if (chkRedirect.equals("ng")) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/RealErp/faces/jsfpages/ProjectSetting/blockDetailList.xhtml");
        }
    }

    public int checkFlatList(FbsFlatType fbsFlatType) {
        if (fbsFlatType == null) {
            return 0;
        } else {
            return fbsFlatType.getFlatTypeId();
        }
    }

    public void addFlatSpecification() {
        if (selectFlatList.length == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Flat Selected", ""));
        } else if (fbsFlatType.getFlatTypeId() == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Select Flat Specification", ""));
        } else {
            blockManager.addFlatSpecification(selectFlatList, fbsFlatType);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, " Flat Specification  Added", ""));
            fbsFlatList.clear();
            fbsFlatList = blockManager.populateFbsFlatList(selectedFbsBlock.getBlockId());
            modelFlatList = new FlatDataModelBean(fbsFlatList);
            reset();

        }
    }

    public void reset() {
        fbsFloor = new FbsFloor();
        noOfFlat = 0;
        selectFlatList = new FbsFlat[0];
        fbsFlatType = new FbsFlatType();
        selectPlcList = new ArrayList<String>();

    }

    public void addPlc() {
        if (selectFlatList.length == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Flat Selected", ""));
        } else if (selectPlcList.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Select PLC", ""));
        } else {
            System.out.println("selectFlatList+++" + selectFlatList.length);
            System.out.println("selectPlcList+++" + selectPlcList.size());
            for (int i = 0; i < selectFlatList.length; i++) {
                FbsFlat fbsFlat = new FbsFlat();
                fbsFlat = blockManager.populateFbsFlat(selectFlatList[i].getUnitCode());
                blockManager.deleteAllotedPlc(fbsFlat);
            }
            blockManager.addPlc(selectFlatList, selectPlcList);
            reset();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "  PLC  Added", ""));
        }
    }

    public String showPlc(FbsFlat fbsFlat) {
        editFbsFlat = new FbsFlat();
        editFbsFlat = blockManager.populateFbsFlat(fbsFlat.getUnitCode());
        plcAllotList.clear();
        plcAllotList = (List<FbsPlcAllot>) blockManager.populateFlatPlcList(editFbsFlat);
        System.out.println("plcallotlist++++++" + plcAllotList.size());
        String plcList = "";
        for (int i = 0; i < plcAllotList.size(); i++) {
            plcList = plcList + plcAllotList.get(i).getFbsPlc().getPlcName() + ",";
        }
        if (plcList.length() > 0) {
            plcList = plcList.substring(0, plcList.length() - 1);
        }
        return plcList;

    }

    public String lockString(FbsFlat fbsFlat) {
        if (fbsFlat == null) {
            return "ui-icon ui-icon-unlocked";
        }
        if (fbsFlat.getLockStatus().equals("locked") && fbsFlat.getStatus().equals("unbooked")) {
            return "ui-icon ui-icon-locked";
        }
        if (fbsFlat.getLockStatus().equals("locked") && fbsFlat.getStatus().equals("booked")) {
            return "ui-icon ui-icon-locked";
        }
        if (fbsFlat.getLockStatus().equals("unlocked") && fbsFlat.getStatus().equals("unbooked")) {
            return "ui-icon ui-icon-unlocked";
        } else {
            return "ui-icon ui-icon-unlocked";
        }
    }

    public void lockFlat(FbsFlat lockFbsFlat) {
        if (lockFbsFlat.getFbsFlatType() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Flat Specification Selected", ""));

        } else {
            int flag = blockManager.lockFbsFlat(lockFbsFlat);
            fbsFlatList.clear();
            fbsFlatList = blockManager.populateFbsFlatList(selectedFbsBlock.getBlockId());
            modelFlatList = new FlatDataModelBean(fbsFlatList);
            reset();
            if (flag == 1) {
                //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucess:", "Flat Sucessfully Locked!"));
            }
            if (flag == 2) {
                //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucess:", "Flat Sucessfully UnLocked!"));
            }

        }
    }

    public void addFlat() {
        System.out.println("inside addFlat++++" + selectedFbsBlock.getBlockName() + fbsFloor.getFloorId() + noOfFlat);
        if ((selectedFbsBlock.getFbsProject().getFbsCompany().getMaxFlats() - countunits()) >= noOfFlat) {
            blockManager.addFlat(selectedFbsBlock, fbsFloor.getFloorId(), noOfFlat);
            fbsFloor = new FbsFloor();
            noOfFlat = 0;
            fbsFlatList.clear();
            selectedFbsBlock = blockManager.populateFbsBlock(selectedFbsBlock.getBlockId());
            fbsFloorList.clear();
            fbsFloorList = (List<FbsFloor>) selectedFbsBlock.getFbsFloorCollection();
            fbsFloorList = blockManager.sortFloorList(fbsFloorList);
            fbsFlatList = blockManager.populateFbsFlatList(selectedFbsBlock.getBlockId());
            modelFlatList = new FlatDataModelBean(fbsFlatList);
            reset();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Block Updated", ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Permission Denied", ""));
        }
    }

    public void deleteFbsFlat(FbsFlat fbsFlat) {
        delFbsFlat = new FbsFlat();
        delFbsFlat = fbsFlat;
    }

    public void confirmDeleteFlat() {
        blockManager.deleteFlat(delFbsFlat);
        delFbsFlat = new FbsFlat();
        fbsFlatList.clear();
        selectedFbsBlock = blockManager.populateFbsBlock(selectedFbsBlock.getBlockId());
        fbsFlatList = blockManager.populateFbsFlatList(selectedFbsBlock.getBlockId());
        modelFlatList = new FlatDataModelBean(fbsFlatList);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "  Flats  Deleted", ""));

    }

    public void deleteSelectedFlat() {

        for (int i = 0; i < selectFlatList.length; i++) {
            blockManager.deleteFlat(selectFlatList[i]);
        }
        fbsFlatList.clear();
        selectedFbsBlock = blockManager.populateFbsBlock(selectedFbsBlock.getBlockId());
        fbsFlatList = blockManager.populateFbsFlatList(selectedFbsBlock.getBlockId());
        modelFlatList = new FlatDataModelBean(fbsFlatList);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, " Flats  Deleted", ""));


    }

    public void lockSelectedFlat(String lockStatus) {
        if (selectFlatList.length == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "No Flat Selected"));
        } else {
            boolean flag = false;
            for (int i = 0; i < selectFlatList.length; i++) {
                if (selectFlatList[i].getFbsFlatType() == null) {
                    flag = true;
                    break;
                }
            }
            if (flag == true) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Flat Specification Selected", ""));
            } else {

                for (int i = 0; i < selectFlatList.length; i++) {
                    blockManager.lockFbsFlat(selectFlatList[i]);
                }
                fbsFlatList.clear();
                selectedFbsBlock = blockManager.populateFbsBlock(selectedFbsBlock.getBlockId());
                fbsFlatList = blockManager.populateFbsFlatList(selectedFbsBlock.getBlockId());
                modelFlatList = new FlatDataModelBean(fbsFlatList);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Congrates!  Flats Successfully" + lockStatus, ""));
            }
            reset();
        }

    }

    public void checkSelectedFlatList() {
        if (selectFlatList.length == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Flat Selected", ""));
            renderDialog = false;
        } else {
            renderDialog = true;
        }
    }

    public void renderDataGrid(String chkRender) {
        System.out.println("inside renderGrid");
        if (chkRender.equals("0")) {
            System.out.println("inside renderGrid 0");
            renderGraphicalView[0] = true;
            renderGraphicalView[1] = false;
            renderGraphicalView[2] = false;
        }
        if (chkRender.equals("1")) {
            System.out.println("inside renderGrid 1");
            renderGraphicalView[0] = false;
            renderGraphicalView[1] = true;
            renderGraphicalView[2] = false;
        }
        if (chkRender.equals("2")) {
            System.out.println("inside renderGrid 2");
            renderGraphicalView[0] = false;
            renderGraphicalView[1] = false;
            renderGraphicalView[2] = true;
        }
    }

    public List<FbsFlat> sortFlatList(Collection<FbsFlat> fbsFlatCollection) {
        return blockManager.sortFlatList((List<FbsFlat>) fbsFlatCollection);
    }

    public String convertFloorNo(int floorNo) {
        return blockManager.convertFloorNo(floorNo);
    }

    public void uploadBlockImage(FileUploadEvent event) throws FileNotFoundException, IOException {
        DefaultUploadedFile inputFile = (DefaultUploadedFile) event.getFile();
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");

        path = path + "resources/documents/block_image/" + selectedFbsBlock.getBlockId() + ".png";
        File outputFile = new File(path);
        byte byteArray[] = inputFile.getContents();
        OutputStream fileOutputStream = new FileOutputStream(outputFile);
        fileOutputStream.write(byteArray);
        fileOutputStream.close();
        selectedFbsBlock.setImagePath("resources/documents/block_image/" + selectedFbsBlock.getBlockId() + ".png");
        fbsBlockFacade.edit(selectedFbsBlock);
        selectedFbsBlock = fbsBlockFacade.find(selectedFbsBlock.getBlockId());
        FacesContext.getCurrentInstance().getExternalContext().redirect("/RealErp/faces/jsfpages/ProjectSetting/blockDetailList.xhtml");
    }

    public boolean renderDelete(FbsBlock fbsBlock) throws IOException {
        boolean renderDelete = true;
        showBlockDetails(fbsBlock, "delete");
        for (int i = 0; i < fbsFlatList.size(); i++) {
            if (fbsFlatList.get(i).getStatus().equalsIgnoreCase("booked")) {
                renderDelete = false;
                break;
            }

        }
        return renderDelete;


    }

    public void bookingDetail(FbsFlat fbsFlat) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/jsfpages/Booking/bookingDetail.xhtml?unitCode=" + fbsFlat.getUnitCode() + "&redirectFlag=blockDetail&name=" + fbsFlat.getFbsFloor().getFbsBlock().getBlockName() + "&url=/faces/jsfpages/ProjectSetting/blockDetailList.xhtml");
    }

    public int countunits() {
        FbsProject countUnits = new FbsProject();
        countUnits = fbsProjectFacade.find(LoginBean.fbsProject.getProjId());
        int count = 0;
        for (int i = 0; i < fbsFlatFacade.findAll().size(); i++) {
            if (countUnits.getProjId() == fbsFlatFacade.findAll().get(i).getFbsFloor().getFbsBlock().getFbsProject().getProjId()) {
                count++;
            }
        }
        return count;
    }

    public FbsBlock getFbsBlock() {
        return fbsBlock;
    }

    public void setFbsBlock(FbsBlock fbsBlock) {
        this.fbsBlock = fbsBlock;
    }

    public int getNoOfFloor() {
        return noOfFloor;
    }

    public void setNoOfFloor(int noOfFloor) {
        this.noOfFloor = noOfFloor;
    }

    public List<FbsBlock> getFbsBlockList() {
        return fbsBlockList;
    }

    public void setFbsBlockList(List<FbsBlock> fbsBlockList) {
        this.fbsBlockList = fbsBlockList;
    }

    public int getNoOfFlat() {
        return noOfFlat;
    }

    public void setNoOfFlat(int noOfFlat) {
        this.noOfFlat = noOfFlat;
    }

    public FbsBlock getSelectedFbsBlock() {
        return selectedFbsBlock;
    }

    public void setSelectedFbsBlock(FbsBlock selectedFbsBlock) {
        this.selectedFbsBlock = selectedFbsBlock;
    }

    public List<FbsFlat> getFbsFlatList() {
        return fbsFlatList;
    }

    public void setFbsFlatList(List<FbsFlat> fbsFlatList) {
        this.fbsFlatList = fbsFlatList;
    }

    public FbsFlat[] getSelectFlatList() {
        return selectFlatList;
    }

    public void setSelectFlatList(FbsFlat[] selectFlatList) {
        this.selectFlatList = selectFlatList;
    }

    public FlatDataModelBean getModelFlatList() {
        return modelFlatList;
    }

    public void setModelFlatList(FlatDataModelBean modelFlatList) {
        this.modelFlatList = modelFlatList;
    }

    public FbsFlatType getFbsFlatType() {
        return fbsFlatType;
    }

    public void setFbsFlatType(FbsFlatType fbsFlatType) {
        this.fbsFlatType = fbsFlatType;
    }

    public List<String> getSelectPlcList() {
        return selectPlcList;
    }

    public void setSelectPlcList(List<String> selectPlcList) {
        this.selectPlcList = selectPlcList;
    }

    public FbsFloor getFbsFloor() {
        return fbsFloor;
    }

    public void setFbsFloor(FbsFloor fbsFloor) {
        this.fbsFloor = fbsFloor;
    }

    public List<FbsFlatType> getFlatTypeList() {
        return flatTypeList;
    }

    public void setFlatTypeList(List<FbsFlatType> flatTypeList) {
        this.flatTypeList = flatTypeList;
    }

    public List<FbsPlc> getFbsPlcList() {
        return fbsPlcList;
    }

    public void setFbsPlcList(List<FbsPlc> fbsPlcList) {
        this.fbsPlcList = fbsPlcList;
    }

    public List<FbsFloor> getFbsFloorList() {
        return fbsFloorList;
    }

    public void setFbsFloorList(List<FbsFloor> fbsFloorList) {
        this.fbsFloorList = fbsFloorList;
    }

    public boolean[] getRenderGraphicalView() {
        return renderGraphicalView;
    }

    public void setRenderGraphicalView(boolean[] renderGraphicalView) {
        this.renderGraphicalView = renderGraphicalView;
    }

    public List<FbsProject> getFbsProjectList() {
        return fbsProjectList;
    }

    public void setFbsProjectList(List<FbsProject> fbsProjectList) {
        this.fbsProjectList = fbsProjectList;
    }

    public List<FbsFloor> getDesfbsFloorList() {
        return desfbsFloorList;
    }

    public void setDesfbsFloorList(List<FbsFloor> desfbsFloorList) {
        this.desfbsFloorList = desfbsFloorList;
    }

    public boolean isRenderProject() {
        return renderProject;
    }

    public void setRenderProject(boolean renderProject) {
        this.renderProject = renderProject;
    }

    public boolean isRenderDialog() {
        return renderDialog;
    }

    public void setRenderDialog(boolean renderDialog) {
        this.renderDialog = renderDialog;
    }

    public FbsBlockFacade getFbsBlockFacade() {
        return fbsBlockFacade;
    }

    public void setFbsBlockFacade(FbsBlockFacade fbsBlockFacade) {
        this.fbsBlockFacade = fbsBlockFacade;
    }

    public boolean isRenderDetail() {
        return renderDetail;
    }

    public void setRenderDetail(boolean renderDetail) {
        this.renderDetail = renderDetail;
    }

    public FbsBlock getEditFbsBlock() {
        return editFbsBlock;
    }

    public void setEditFbsBlock(FbsBlock editFbsBlock) {
        this.editFbsBlock = editFbsBlock;
    }

    public boolean isRenderDialog1() {
        return renderDialog1;
    }

    public void setRenderDialog1(boolean renderDialog1) {
        this.renderDialog1 = renderDialog1;
    }
}
