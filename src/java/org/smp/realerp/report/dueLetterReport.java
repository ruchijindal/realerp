/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.report;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.ZapfDingbatsList;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.smp.realerp.entity.*;

import org.smp.realerp.managedbean.LoginBean;
import org.smp.realerp.manager.CalculationManager;
import org.smp.realerp.pojo.RerpUtil;
import org.smp.realerp.session.FbsBookingFacade;
import org.smp.realerp.session.FbsReportFacade;
import org.smp.realerp.session.FbsServicetaxFacade;

/**
 *
 * @author smp
 */
@WebServlet(name = "dueLetterReport", urlPatterns = {"/dueLetterReport"})
public class dueLetterReport extends HttpServlet {

    @EJB
    CalculationManager calculationManager;
    @EJB
    FbsServicetaxFacade fbsServicetaxFacade;
    @EJB
    FbsReportFacade fbsReportFacade;
    @EJB
    FbsBookingFacade fbsBookingFacade;
    String regNumber;
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    private static Font blackFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL, BaseColor.BLACK);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
    private static Font blackSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 6, Font.NORMAL, BaseColor.BLACK);
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private FbsBooking fbsBooking;
    private FbsFlat fbsFlat;
    private FbsApplicant firstFbsApplicant;
    private FbsApplicant secFbsApplicant;
    private FbsPlanname fbsPlanname;
    private FbsDiscount fbsDiscount;
    private FbsPayplan fbsPayplan;
    private FbsBroker fbsBroker;
    private FbsFlatType fbsFlatType;
    private FbsFloor fbsFloor;
    private FbsBlock fbsBlock;
    private FbsProject fbsProject;
    private int registrationnumber;
    private List<FbsParking> fbsParkingList;
    private List<FbsPlcAllot> fbsPlcAllotList;
    private List<FbsCharge> chargeList;
    private List<FbsPayment> fbsPaymentList;
    private List<FbsPayplan> fbsPayplanList;
    private List<FbsServicetax> fbsServicetaxList;
    private FbsServicetax fbsServicetax;
    private List<Integer> regList = new ArrayList<Integer>();
    private int registrationList;
    RerpUtil utility = new RerpUtil();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DocumentException {
        //  response.setContentType("text/html;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        try {

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 0, 0, 0, 0);
            System.out.println("-------------------------------------------------------------------");
            String requestType = request.getParameter("requestType");
            if (requestType.equals("email")) {
                String relativeWebPath = "/resources/documents/" + "DueLetterTemp.pdf";
                String absoluteDiskPath = getServletContext().getRealPath(relativeWebPath);
                File file = new File(absoluteDiskPath);
                FileOutputStream fileout = new FileOutputStream(file);
                PdfWriter.getInstance(document, fileout);
            } else {
                PdfWriter.getInstance(document, bos);
            }

            document.open();
            document.addTitle("Due Letter Report");
            document.addSubject("Using iText");
            document.addKeywords("Java, PDF, iText");

            try {
                regNumber = request.getParameter("registrationNumber");
                int registrationNumber = Integer.parseInt(regNumber);

                getDetail(registrationNumber);
                PdfPTable mainTable = new PdfPTable(1);
                addEmptyParagraphToDocument(document, 2);
                mainTable.addCell(setupCompanyDetail());
                mainTable.addCell(dueLetterTable());
                mainTable.addCell(applicantDetailCell());
                mainTable.addCell(dueLetterBodyCell());
                mainTable.addCell(dueTable());
                mainTable.addCell(letterEnd());
                document.add(mainTable);

            } catch (Exception e) {
                HttpSession session = request.getSession(false);
                ArrayList<Integer> list = (ArrayList<Integer>) session.getAttribute("registrationList");
                System.out.println("registrationList size " + list.size());

                for (int i = 0; i < list.size(); i++) {
                    getDetail(list.get(i));
                    PdfPTable mainTable = new PdfPTable(1);
                    addEmptyParagraphToDocument(document, 2);
                    mainTable.addCell(setupCompanyDetail());
                    mainTable.addCell(dueLetterTable());
                    mainTable.addCell(applicantDetailCell());
                    mainTable.addCell(dueLetterBodyCell());
                    mainTable.addCell(dueTable());
                    mainTable.addCell(letterEnd());
                    document.add(mainTable);
                    document.newPage();
                }

            }



            document.close();
            if (requestType.equals("nonEmail")) {
                response.setHeader("Expiresdocument.add(mainTable);", "0");
                response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
                response.setHeader("Pragma", "public");
                response.setContentType("application/pdf");
                bos.writeTo(out);
                out.flush();
            }
        } catch (Exception e) {
            System.out.println("Exception in dueLetterReport");
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    PdfPTable setupCompanyDetail() {

        PdfPTable companyDetailTable = new PdfPTable(1);
        Phrase companyPhrase = new Phrase(LoginBean.fbsLogin.getFbsCompany().getCompanyName(), smallBold);
        PdfPCell companyCell = new PdfPCell(companyPhrase);
        companyCell.setBorder(PdfPCell.NO_BORDER);
        companyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        companyDetailTable.addCell(companyCell);

        Phrase companyAddressPhrase = new Phrase(LoginBean.fbsLogin.getFbsCompany().getAddress(), smallBold);
        PdfPCell companyAddressCell = new PdfPCell(companyAddressPhrase);
        companyAddressCell.setBorder(PdfPCell.NO_BORDER);
        companyAddressCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        companyDetailTable.addCell(companyAddressCell);

        Phrase companyTelePhonePhrase = new Phrase("TelePhone : " + LoginBean.fbsLogin.getFbsCompany().getTelNumber(), smallBold);
        PdfPCell companyTelePhoneCell = new PdfPCell(companyTelePhonePhrase);
        companyTelePhoneCell.setBorder(PdfPCell.NO_BORDER);
        companyTelePhoneCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        companyDetailTable.addCell(companyTelePhoneCell);

        Phrase companyEmailPhrase = new Phrase("Email : " + LoginBean.fbsLogin.getFbsCompany().getEmail(), smallBold);
        PdfPCell companyEmailCell = new PdfPCell(companyEmailPhrase);
        companyEmailCell.setBorder(PdfPCell.NO_BORDER);
        companyEmailCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        companyDetailTable.addCell(companyEmailCell);
        return companyDetailTable;


    }

    PdfPTable dueLetterTable() {
        PdfPTable dueLetterTable = new PdfPTable(1);
        Phrase dueLetterPhrase = new Paragraph("Due Letter ", smallBold);



        PdfPCell dueLetterCell = new PdfPCell(dueLetterPhrase);
        dueLetterCell.setBorder(PdfPCell.NO_BORDER);
        dueLetterCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        dueLetterTable.addCell(dueLetterCell);


        return dueLetterTable;
    }

    PdfPTable applicantDetailCell() {
        PdfPTable applicantDetailCell = new PdfPTable(2);
        applicantDetailCell.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        PdfPCell unitCodeCell = new PdfPCell(new Phrase("Unit Code: " + fbsBlock.getFbsProject().getProjAbvr() + "-" + fbsBlock.getBlockAbvr() + "-" + fbsFlat.getFlatNo().toString(), smallBold));
        unitCodeCell.setBorder(PdfPCell.NO_BORDER);
        unitCodeCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        applicantDetailCell.addCell(unitCodeCell);


        PdfPCell dateCell = new PdfPCell(new Phrase("Date: " + dateFormat.format(new Date()), smallBold));
        dateCell.setBorder(PdfPCell.NO_BORDER);
        dateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        applicantDetailCell.addCell(dateCell);


        PdfPCell registrationNoCell = new PdfPCell(new Phrase("REGISTRATION NO:" + fbsBooking.getRegNumber().toString(), smallBold));
        registrationNoCell.setBorder(PdfPCell.NO_BORDER);
        registrationNoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        applicantDetailCell.addCell(registrationNoCell);




        PdfPCell bookingDateCell = new PdfPCell(new Phrase("BOOKING DATE:" + dateFormat.format(fbsBooking.getBookingDt()).toString(), smallBold));
        bookingDateCell.setBorder(PdfPCell.NO_BORDER);
        bookingDateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        applicantDetailCell.addCell(bookingDateCell);



        return applicantDetailCell;
    }

    PdfPCell dueLetterBodyCell() {

        PdfPCell letterBodyCell = new PdfPCell();

        Paragraph toParagraph = new Paragraph();
        Phrase toPhrase = new Phrase("To,\n", blackFont);
        toParagraph.add(toPhrase);

        Phrase applicantPhrase = new Phrase("   " + (calculationManager.getFbsApplicant(fbsFlat, 1).getApplicantName()) + "\n", smallBold);

        Phrase applicantAddressPhrase = new Phrase("   " + fbsProject.getAddress().toString() + "\n", smallBold);

        applicantPhrase.add(applicantAddressPhrase);
        toParagraph.add(applicantPhrase);

        Paragraph subjectParagraph = new Paragraph();
        Phrase subjectPhrase = new Phrase("Subject: ", smallBold);
        subjectParagraph.add(subjectPhrase);

        Phrase subjetBodyPhrase = new Phrase("Request for payment towards your", blackFont);

        Phrase unitNoPhrase = new Phrase(" Unit Number " + fbsFlat.getUnitCode().toString(), smallBold);

        subjectParagraph.add(subjetBodyPhrase);
        subjectParagraph.add(unitNoPhrase);

        toParagraph.add(subjectParagraph);

        //  letterBodyCell.addElement(toParagraph);

        Paragraph bodyParagraph = new Paragraph();
        Phrase bodyPart0Phrase = new Phrase(" You have been alloted a Unit No. ", blackFont);
        Phrase bodyPart1Phrase = new Phrase("" + fbsFlat.getUnitCode().toString(), smallBold);
        Phrase bodyPart2Phrase = new Phrase(" on", blackFont);
        Phrase bodyPart3Phrase = new Phrase(" in " + fbsBlock.getBlockName().toString(), smallBold);
        Phrase bodyPart4Phrase = new Phrase(" " + fbsProject.getProjName().toString(), smallBold);
        Phrase bodyPart5Phrase = new Phrase(" Group Housing Residential Complex being developed on", blackFont);
        Phrase bodyPart6Phrase = new Phrase(" " + fbsProject.getAddress().toString(), blackFont);
        Phrase bodyPart7Phrase = new Phrase(". You have booked the reffered unit in", blackFont);
        Phrase bodyPart8Phrase = new Phrase(" " + fbsProject.getProjName().toString(), smallBold);
        Phrase bodyPart9Phrase = new Phrase(" Group Housing Project under Flexi Payement Plan.The basic cost of the booked Unit is", blackFont);
        Phrase bodyPart10Phrase = new Phrase("  Rs " + (utility.indianFormat(calculationManager.calculateTotalCost(fbsFlatType, fbsPlanname, fbsDiscount, fbsParkingList, fbsPlcAllotList, chargeList))), smallBold);
        Phrase bodyPart11Phrase = new Phrase("/-(" + calculationManager.convertIntegerIntoWords((int) calculationManager.calculateTotalCost(fbsFlatType, fbsPlanname, fbsDiscount, fbsParkingList, fbsPlcAllotList, chargeList)) + " Only).", smallBold);
        //zapfDingbatsList.add(new ListItem("2. Helpline No."+fbsCompany.getTelNumber()+"/Email ID:"+fbsCompany.getEmail()+".\n", blackFont));
        bodyParagraph.add(bodyPart0Phrase);
        bodyParagraph.add(bodyPart1Phrase);
        bodyParagraph.add(bodyPart2Phrase);
        bodyParagraph.add(bodyPart3Phrase);
        bodyParagraph.add(bodyPart4Phrase);
        bodyParagraph.add(bodyPart5Phrase);
        bodyParagraph.add(bodyPart6Phrase);
        bodyParagraph.add(bodyPart7Phrase);
        bodyParagraph.add(bodyPart8Phrase);
        bodyParagraph.add(bodyPart9Phrase);
        bodyParagraph.add(bodyPart10Phrase);
        bodyParagraph.add(bodyPart11Phrase);

        toParagraph.add(bodyParagraph);

        letterBodyCell.addElement(toParagraph);
        //   letterBodyCell.addElement(bodyParagraph);

        return letterBodyCell;

    }

    PdfPTable dueTable() {
        float[] floats = {5f, 2f};
        PdfPTable dueTable = new PdfPTable(floats);

        Phrase totalCostPhrase = new Phrase("TOTAL COST(A)", blackFont);
        PdfPCell totalCostCell = new PdfPCell(totalCostPhrase);
        dueTable.addCell(totalCostCell);

        Phrase totalCostPhraseValue = new Phrase(" " + (utility.indianFormat(calculationManager.calculateTotalCost(fbsFlatType, fbsPlanname, fbsDiscount, fbsParkingList, fbsPlcAllotList, chargeList))), blackFont);
        PdfPCell totalCostCellValue = new PdfPCell(totalCostPhraseValue);
        totalCostCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        dueTable.addCell(totalCostCellValue);

        Phrase receivedPhrase = new Phrase("RECEIVED AMOUNT(B)", blackFont);
        PdfPCell receiveCell = new PdfPCell(receivedPhrase);
        dueTable.addCell(receiveCell);

        Phrase receivedPhraseValue = new Phrase(" " + (utility.indianFormat(calculationManager.calculatePaidAmountAfterServiceTaxStatusCleared(fbsPaymentList))), blackFont);
        PdfPCell receivedCellValue = new PdfPCell(receivedPhraseValue);
        receivedCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        dueTable.addCell(receivedCellValue);

        Phrase currentinstallmentPhrase = new Phrase("CURRENT INSTALLMENT(C)", blackFont);
        PdfPCell currentinstallmentCell = new PdfPCell(currentinstallmentPhrase);
        dueTable.addCell(currentinstallmentCell);

        double currentInstallment = calculateCurrentInstallment();
        Phrase currentinstallmentPhraseValue = new Phrase(String.valueOf(utility.indianFormat(currentInstallment)), blackFont);


        PdfPCell currentinstallmentCellValue = new PdfPCell(currentinstallmentPhraseValue);
        currentinstallmentCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        dueTable.addCell(currentinstallmentCellValue);

        Phrase outstandingPhrase = new Phrase("PREVIOUS OUTSTANDING(+)/PREVIOUS CREDIT(-)(D)", blackFont);
        PdfPCell outstandingCell = new PdfPCell(outstandingPhrase);
        dueTable.addCell(outstandingCell);

        Phrase outstandingPhraseValue = new Phrase(String.valueOf(utility.indianFormat(previousOutStandingOrCreditAmount())), blackFont);
        PdfPCell outstandingCellValue = new PdfPCell(outstandingPhraseValue);
        outstandingCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        dueTable.addCell(outstandingCellValue);


        Phrase amountpayablePhrase = new Phrase("AMOUNT PAYABLE(C+D)", blackFont);
        PdfPCell amountpayableCell = new PdfPCell(amountpayablePhrase);
        dueTable.addCell(amountpayableCell);



        Phrase amountpayablePhraseValue = new Phrase(String.valueOf(utility.indianFormat(amountPayable(currentInstallment))), blackFont);
        PdfPCell amountpayableCellValue = new PdfPCell(amountpayablePhraseValue);
        amountpayableCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        dueTable.addCell(amountpayableCellValue);


        Phrase servicetaxPhrase = new Phrase("SERVICE TAX(11%)(E)", blackFont);
        PdfPCell servicetaxCell = new PdfPCell(servicetaxPhrase);
        dueTable.addCell(servicetaxCell);



        Phrase servicetaxPhraseValue = new Phrase(String.valueOf(utility.indianFormat(calculateServiceTaxOnAmountPayable(amountPayable(currentInstallment)))), blackFont);
        PdfPCell servicetaxCellValue = new PdfPCell(servicetaxPhraseValue);
        servicetaxCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        dueTable.addCell(servicetaxCellValue);

        Phrase totalamountpayablePhrase = new Phrase("TOTAL AMOUNT PAYABLE(C+D+E)", blackFont);
        PdfPCell totalamountpayableCell = new PdfPCell(totalamountpayablePhrase);
        dueTable.addCell(totalamountpayableCell);

        Phrase totalamountpayablePhraseValue = new Phrase(String.valueOf(utility.indianFormat(calculateServiceTaxOnAmountPayable(amountPayable(currentInstallment)) + amountPayable(currentInstallment))), blackFont);
        PdfPCell totalamountpayableCellValue = new PdfPCell(totalamountpayablePhraseValue);
        totalamountpayableCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        dueTable.addCell(totalamountpayableCellValue);


        Phrase remainingamountPhrase = new Phrase("REMAINING AMOUNT[A-(B+C+D)](Excluding Service Tax)", blackFont);
        PdfPCell remainingamountCell = new PdfPCell(remainingamountPhrase);
        dueTable.addCell(remainingamountCell);

        Phrase remainingamountPhraseValue = new Phrase(String.valueOf(utility.indianFormat(remainingAmount(calculationManager.calculateTotalCost(fbsFlatType, fbsPlanname, fbsDiscount, fbsParkingList, fbsPlcAllotList, chargeList), amountPayable(currentInstallment)))), blackFont);
        PdfPCell remainingamountCellValue = new PdfPCell(remainingamountPhraseValue);
        remainingamountCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        dueTable.addCell(remainingamountCellValue);

        FbsReport fbsReport = new FbsReport();
        fbsReport.setFbsCompany(LoginBean.fbsLogin.getFbsCompany());
        fbsReport.setFbsBooking(fbsBooking);
        fbsReport.setDate(new Date());
        fbsReport.setRecievedAmt((calculationManager.calculatePaidAmountAfterServiceTaxStatusCleared(fbsPaymentList)));
        fbsReport.setCurInstallment(currentInstallment);
        fbsReport.setOutCredit(previousOutStandingOrCreditAmount());
        fbsReport.setAmountPayable(amountPayable(currentInstallment));
        fbsReport.setServiceTax(calculateServiceTaxOnAmountPayable(amountPayable(currentInstallment)));
        fbsReport.setTotalCost(calculationManager.calculateTotalCost(fbsFlatType, fbsPlanname, fbsDiscount, fbsParkingList, fbsPlcAllotList, chargeList));
        fbsReport.setRemainingAmt(remainingAmount(calculationManager.calculateTotalCost(fbsFlatType, fbsPlanname, fbsDiscount, fbsParkingList, fbsPlcAllotList, chargeList), amountPayable(currentInstallment)));
        fbsReportFacade.create(fbsReport);
        return dueTable;




    }

    double calculateCurrentInstallment() {
        fbsPayplan = calculationManager.getCurrentInstallmentNumber(fbsPayplanList);
        double currentInstallment = 0;
        if (fbsPayplan != null) {


            currentInstallment = calculationManager.calculateBSPInstallmentAmount(fbsPayplan, fbsFlatType, fbsPlanname, fbsDiscount) + calculationManager.calculatePLCInstallmentAmount(fbsPayplan, fbsPlcAllotList, fbsFlatType);
            if (fbsPayplan.getSerialNo() == fbsPayplanList.size() - 1) {
                currentInstallment += calculationManager.calculateOtherCharge(chargeList) + calculationManager.calculateParkingCharge(fbsParkingList);
            }

        } else {
            currentInstallment = 0;
        }
        return currentInstallment;
    }

    PdfPCell letterEnd() {

        PdfPCell letterEnd = new PdfPCell();

        Paragraph endBody = new Paragraph();
        Phrase p11 = new Phrase("It is requested to remit the amount with in 7 days. We communicate the payment schedule of the amount of Rs.", blackFont);
        endBody.add(p11);
        Phrase body = new Phrase(String.valueOf(utility.indianFormat(remainingAmount(calculationManager.calculateTotalCost(fbsFlatType, fbsPlanname, fbsDiscount, fbsParkingList, fbsPlcAllotList, chargeList), amountPayable(calculateCurrentInstallment())))), smallBold);
        endBody.add(body);
        Phrase p12 = new Phrase("/- payable by you in due course.\n", blackFont);
        endBody.add(p12);
        Phrase p13 = new Phrase("For ", blackFont);
        endBody.add(p13);
        Phrase p14 = new Phrase("" + fbsProject.getFbsCompany().getCompanyName().toString(), smallBold);
        endBody.add(p14);
        Phrase p15 = new Phrase(("\nNOTE:\n"), blackFont);
        ZapfDingbatsList zapfDingbatsList = new ZapfDingbatsList(42, 15);
        endBody.add(p15);
        zapfDingbatsList.add(new ListItem("1. For any delayed in payment interest will be charged ", blackFont));
        zapfDingbatsList.add(new ListItem("18% p.a.\n", blackFont));
        zapfDingbatsList.add(new ListItem("2. Helpline No:", blackFont));
        zapfDingbatsList.add(new ListItem(" " + LoginBean.fbsLogin.getFbsCompany().getTelNumber(), smallBold));
        zapfDingbatsList.add(new ListItem("/E-mail:", blackFont));
        zapfDingbatsList.add(new ListItem(" " + LoginBean.fbsLogin.getFbsCompany().getEmail() + "\n", smallBold));
        zapfDingbatsList.add(new ListItem("3. Please ignore this letter in case the payment has already been made.\n", blackFont));
        zapfDingbatsList.add(new ListItem("4. Service Tax is applicable as per Govt. Norms.\n", blackFont));
        zapfDingbatsList.add(new ListItem("5. This is computer generated sheet and does not require any signature.\n", blackFont));

        endBody.add(zapfDingbatsList);




        letterEnd.addElement(new Phrase(endBody));

        return letterEnd;

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (DocumentException ex) {
            ex.printStackTrace();
            Logger.getLogger(dueLetterReport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (DocumentException ex) {
            ex.printStackTrace();
            Logger.getLogger(dueLetterReport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    void addEmptyParagraphToDocument(Document document, int numberOfParagraph) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Paragraph("", blackFont));
        for (int i = 0; i < numberOfParagraph; i++) {
            paragraph.add(new Paragraph(" "));
        }
        document.add(paragraph);
    }

    void getDetail(int registrationNumber) {
        fbsBooking = calculationManager.getBooking(registrationNumber);
        fbsParkingList = (List<FbsParking>) fbsBooking.getFbsParkingCollection();
        fbsPlcAllotList = (List<FbsPlcAllot>) fbsBooking.getFbsFlat().getFbsPlcAllotCollection();
        // fbsServicetaxList = (List<FbsServicetax>) fbsServicetax.getEndDate();
        fbsFlat = fbsBooking.getFbsFlat();
        firstFbsApplicant = calculationManager.getFbsApplicant(fbsFlat, 1);
        secFbsApplicant = calculationManager.getFbsApplicant(fbsFlat, 2);
        fbsPlanname = fbsBooking.getFbsPlanname();
        fbsDiscount = fbsBooking.getFbsDiscount();
        fbsBroker = fbsBooking.getFbsBroker();
        fbsFlatType = fbsFlat.getFbsFlatType();
        fbsFloor = fbsFlat.getFbsFloor();
        fbsBlock = fbsFloor.getFbsBlock();
        fbsProject = fbsBlock.getFbsProject();
        chargeList = (List<FbsCharge>) fbsProject.getFbsChargeCollection();
        fbsPaymentList = (List<FbsPayment>) fbsBooking.getFbsPaymentCollection();
        System.out.println("Fbs pay plan " + fbsPlanname.getPlanId());
        fbsPayplanList = calculationManager.getFbsPayplanList(fbsPlanname);


    }

    double previousOutStandingOrCreditAmount() {
        double amount = 0;

        for (int i = 0; i < fbsPayplanList.size(); i++) {
            if (fbsPayplan.getSerialNo() > fbsPayplanList.get(i).getSerialNo()) {
                amount += calculationManager.calculateBSPInstallmentAmount(fbsPayplanList.get(i), fbsFlatType, fbsPlanname, fbsDiscount) + calculationManager.calculatePLCInstallmentAmount(fbsPayplanList.get(i), fbsPlcAllotList, fbsFlatType);
            }
        }
        amount -= calculationManager.calculatePaidAmountAfterServiceTaxStatusCleared(fbsPaymentList);
        return amount;
    }

    double amountPayable(double currentInstallment) {
        double amountPayable = 0;

        amountPayable = (currentInstallment + previousOutStandingOrCreditAmount());

        if (amountPayable < 0) {
            amountPayable = 0;
        }


        return amountPayable;
    }

    double calculateServiceTaxOnAmountPayable(double amount) {
        double serviceTax = 0;

        List<FbsServicetax> serviceTaxList = fbsServicetaxFacade.findAll();
        FbsServicetax fbsServicetax = new FbsServicetax();

        try {
            for (int i = 0; i < serviceTaxList.size(); i++) {
                System.out.println("service tax date critera " + serviceTaxList.get(i).getStDate() + "- " + serviceTaxList.get(i).getEndDate());
                if (((new Date().equals(serviceTaxList.get(i).getStDate()) || new Date().after(serviceTaxList.get(i).getStDate()))) && (new Date().equals(serviceTaxList.get(i).getEndDate()) || new Date().before(serviceTaxList.get(i).getEndDate()))) {

                    fbsServicetax = serviceTaxList.get(i);
                    break;
                }
            }

            System.out.println("service tax date critera " + fbsServicetax.getStDate() + "- " + fbsServicetax.getEndDate());


            serviceTax = amount * fbsServicetax.getServicetax() / 100;
        } catch (Exception e) {
            System.out.println("Service Tax not defined for this period ");
            serviceTax = 0;
        }
        return serviceTax;



    }

    double remainingAmount(double totalCost, double amountPayable) {

        double remainingAmount = 0;

        remainingAmount = (totalCost - amountPayable);
        return remainingAmount;
    }
}
