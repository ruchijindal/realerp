/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.report;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.smp.realerp.entity.FbsApplicant;
import org.smp.realerp.entity.FbsBlock;
import org.smp.realerp.entity.FbsBooking;
import org.smp.realerp.entity.FbsBroker;
import org.smp.realerp.entity.FbsCharge;
import org.smp.realerp.entity.FbsDiscount;
import org.smp.realerp.entity.FbsFlat;
import org.smp.realerp.entity.FbsFlatType;
import org.smp.realerp.entity.FbsFloor;
import org.smp.realerp.entity.FbsParking;
import org.smp.realerp.entity.FbsPayment;
import org.smp.realerp.entity.FbsPayplan;
import org.smp.realerp.entity.FbsPlanname;
import org.smp.realerp.entity.FbsPlc;
import org.smp.realerp.entity.FbsPlcAllot;
import org.smp.realerp.entity.FbsProject;
import org.smp.realerp.manager.CalculationManager;
import org.smp.realerp.manager.PaymentManager;
import org.smp.realerp.session.FbsApplicantFacade;
import org.smp.realerp.session.FbsBankFacade;
import org.smp.realerp.session.FbsBlockFacade;
import org.smp.realerp.session.FbsBookingFacade;
import org.smp.realerp.session.FbsBrokerFacade;

/**
 *
 * @author SMP Technologies
 */
@WebServlet(name = "ConsumerReport", urlPatterns = {"/ConsumerReport"})
public class ConsumerReport extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @PersistenceContext(unitName = "RealErpPU")
    EntityManager em;
    @EJB
    FbsBookingFacade fbsBookingFacade;
    @EJB
    FbsApplicantFacade fbsApplicantFacade;
    @EJB
    FbsBlockFacade fbsBlockFacade;
    @EJB
    CalculationManager calculationManager;
    @EJB
    FbsBrokerFacade fbsBrokerFacade;
    @EJB
    PaymentManager paymentManager;
    @EJB
    FbsBankFacade fbsBankFacade;
    FbsBooking fbsBooking = new FbsBooking();
    FbsApplicant fbsApplicant = new FbsApplicant();
    FbsApplicant firstFbsApplicant = new FbsApplicant();
    FbsApplicant secFbsApplicant = new FbsApplicant();
    FbsFlat fbsFlat = new FbsFlat();                //---
    FbsPlanname fbsPlanname = new FbsPlanname();    //||
    FbsDiscount fbsDiscount = new FbsDiscount();    //
    FbsBroker fbsBroker = new FbsBroker();
    FbsFlatType fbsFlatType = new FbsFlatType();
    FbsFloor fbsFloor = new FbsFloor();
    FbsBlock fbsBlock = new FbsBlock();
    FbsProject fbsProject = new FbsProject();
    List<FbsApplicant> fbsApplicantList = new ArrayList<FbsApplicant>();
    List<FbsPlcAllot> fbsPlcAllots = new ArrayList<FbsPlcAllot>();
    List<FbsPlc> fbsPLCList = new ArrayList<FbsPlc>();
    List<FbsParking> fbsParkingList = new ArrayList<FbsParking>();
    List<FbsCharge> fbsChargeList = new ArrayList<FbsCharge>(); // other Charge
    List<FbsPayment> fbsPaymentList = new ArrayList<FbsPayment>();
    List<FbsPayplan> fbsPayplanList = new ArrayList<FbsPayplan>();
    String regNumber;
    DecimalFormat decimalFormat = new DecimalFormat("##,###.00");
    FontSelector fontselector = new FontSelector();
    FontSelector fontselectorhd = new FontSelector();  //for secondary headingReg_no
    FontSelector fontselectorhd2 = new FontSelector();
    FontSelector fontselectorhd3 = new FontSelector();
    FontSelector fontselectorDesc = new FontSelector();
    FontSelector fontselectorsale = new FontSelector();
    FontSelector fontselectordes = new FontSelector();
    FontSelector fontSelectorNormal = new FontSelector();
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
    double totalDueOrCreditAmount = 0;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            fontselector.addFont(new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD));
            fontselectorhd.addFont(new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
            fontselectorhd2.addFont(new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
            //for Description
            fontselectorDesc.addFont(new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
            //for Table total Row
            fontselectorhd3.addFont(new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD));
            fontselectorsale.addFont(new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));

            fontselectordes.addFont(new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.BOLD));// for payment schedule Dexceription
            fontSelectorNormal.addFont(new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.NORMAL));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 0, 0, 60, 30);
            int registrationNumber;
            try {
                System.out.println("-------------------------------------------------------------------");
                String requestType = request.getParameter("requestType");
                if (requestType.equals("email")) {
                    String relativeWebPath = "/resources/documents/" + "ConsumerReportTemp.pdf";
                    String absoluteDiskPath = getServletContext().getRealPath(relativeWebPath);
                    File file = new File(absoluteDiskPath);
                    FileOutputStream fileout = new FileOutputStream(file);
                    PdfWriter.getInstance(document, fileout);
                } else {
                    PdfWriter.getInstance(document, baos);
                }


                document.open();
                try {


                    regNumber = request.getParameter("registrationNumber");
                    registrationNumber = Integer.parseInt(regNumber);
                    getDetail(registrationNumber);
                    PdfPTable mainTable = new PdfPTable(2);//for first nested table
                    PdfPCell addSpace = new PdfPCell(new Paragraph(""));
                    addSpace.setBorder(Rectangle.NO_BORDER);
                    addSpace.setColspan(2);
                    System.out.println("second Applicant Name " + secFbsApplicant.getApplicantName());

//-----------------------------------------Table Start-------------------------------------------------------------------

                    mainTable.addCell(setUpTableHead(fbsProject.getProjName()));
                    addEmptyParagraph(mainTable, 4, 2);
                    //  Phrase unitCodePhrase = fontselector.process("UnitCode :" + fbsFlat.getFlatNo());
                    // PdfPCell unitCodeCell = new PdfPCell(new Paragraph(unitCodePhrase));
                    // unitCodeCell.setBorder(Rectangle.NO_BORDER);
                    // mainTable.addCell(unitCodeCell);
                    Phrase registrationNumberPhrase = fontselector.process("Registration No. :" + fbsBooking.getRegNumber());
                    PdfPCell registrationNumberCell = new PdfPCell(new Paragraph(registrationNumberPhrase));
                    registrationNumberCell.setBorder(Rectangle.NO_BORDER);
                    mainTable.addCell(registrationNumberCell);
                    String bookingDate = formatter.format(fbsBooking.getBookingDt());
                    Phrase bookingDatePhrase = fontselector.process("Booking Date :" + bookingDate);
                    PdfPCell bookingDateCell = new PdfPCell(new Paragraph(bookingDatePhrase));
                    bookingDateCell.setBorder(Rectangle.NO_BORDER);
                    bookingDateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    mainTable.addCell(bookingDateCell);
                    addEmptyParagraph(mainTable, 1, 2);

                    addEmptyParagraph(mainTable, 1, 2);
                    Phrase applicantDetailPhrase = fontselectorDesc.process("Applicant Details");
                    PdfPCell applicantDetailCell = new PdfPCell(new Paragraph(applicantDetailPhrase));
                    mainTable.addCell(applicantDetailCell);
                    Phrase coApplicantDetailPhrase = fontselectorDesc.process("Co-Applicant Details");
                    PdfPCell coApplicantDetailCell = new PdfPCell(new Paragraph(coApplicantDetailPhrase));
                    mainTable.addCell(coApplicantDetailCell);

                    mainTable.addCell(getApplicantDetail(firstFbsApplicant));
                    mainTable.addCell(getApplicantDetail(secFbsApplicant));
                    mainTable.addCell(getFlatDetail());
                    mainTable.addCell(getPLCChargeDetail());
                    mainTable.addCell(getflatCalculationTable());
                    mainTable.addCell(getOtherChargeDetail());
                    document.add(mainTable);

//--------------------------NEW PAGE START---------------------------------------

                    document.newPage();

                    float[] paymentScheduleTableWidth = {1.4f, 5.5f, 1.8f, 2.0f, 2.0f, 2.0f, 2.0f, 1.8f, 1.5f, 2.0f};
                    PdfPTable paymentScheduleTable = new PdfPTable(paymentScheduleTableWidth);

                    setUpPaymentScheduleTable(paymentScheduleTable);
                    document.add(paymentScheduleTable);


                    float[] paymentHistoryTableWidth = {2.0f, 2.0f, 2.0f, 2.0f, 2.0f, 2.0f, 4.0f, 4.0f, 2.0f, 2.0f, 2.5f};
                    PdfPTable paymentHistoryTable = new PdfPTable(paymentHistoryTableWidth);
                    paymentDetailHistory(paymentHistoryTable);
                    document.add(paymentHistoryTable);
                } catch (Exception e) {

                    HttpSession session = request.getSession(false);
                    ArrayList<Integer> list = (ArrayList<Integer>) session.getAttribute("registrationList");
                    System.out.println("registrationList size " + list.size());

                    for (int i = 0; i < list.size(); i++) {


                        getDetail(list.get(i));
                        PdfPTable mainTable = new PdfPTable(2);//for first nested table
                        PdfPCell addSpace = new PdfPCell(new Paragraph(""));
                        addSpace.setBorder(Rectangle.NO_BORDER);
                        addSpace.setColspan(2);
                        System.out.println("second Applicant Name " + secFbsApplicant.getApplicantName());

//-----------------------------------------Table Start-------------------------------------------------------------------

                        mainTable.addCell(setUpTableHead(fbsProject.getProjName()));
                        addEmptyParagraph(mainTable, 4, 2);
                        //  Phrase unitCodePhrase = fontselector.process("UnitCode :" + fbsFlat.getFlatNo());
                        // PdfPCell unitCodeCell = new PdfPCell(new Paragraph(unitCodePhrase));
                        // unitCodeCell.setBorder(Rectangle.NO_BORDER);
                        // mainTable.addCell(unitCodeCell);
                        Phrase registrationNumberPhrase = fontselector.process("Registration No. :" + fbsBooking.getRegNumber());
                        PdfPCell registrationNumberCell = new PdfPCell(new Paragraph(registrationNumberPhrase));
                        registrationNumberCell.setBorder(Rectangle.NO_BORDER);
                        mainTable.addCell(registrationNumberCell);
                        String bookingDate = formatter.format(fbsBooking.getBookingDt());
                        Phrase bookingDatePhrase = fontselector.process("Booking Date :" + bookingDate);
                        PdfPCell bookingDateCell = new PdfPCell(new Paragraph(bookingDatePhrase));
                        bookingDateCell.setBorder(Rectangle.NO_BORDER);
                        bookingDateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        mainTable.addCell(bookingDateCell);
                        addEmptyParagraph(mainTable, 1, 2);

                        addEmptyParagraph(mainTable, 1, 2);
                        Phrase applicantDetailPhrase = fontselectorDesc.process("Applicant Details");
                        PdfPCell applicantDetailCell = new PdfPCell(new Paragraph(applicantDetailPhrase));
                        mainTable.addCell(applicantDetailCell);
                        Phrase coApplicantDetailPhrase = fontselectorDesc.process("Co-Applicant Details");
                        PdfPCell coApplicantDetailCell = new PdfPCell(new Paragraph(coApplicantDetailPhrase));
                        mainTable.addCell(coApplicantDetailCell);

                        mainTable.addCell(getApplicantDetail(firstFbsApplicant));
                        mainTable.addCell(getApplicantDetail(secFbsApplicant));
                        mainTable.addCell(getFlatDetail());
                        mainTable.addCell(getPLCChargeDetail());
                        mainTable.addCell(getflatCalculationTable());
                        mainTable.addCell(getOtherChargeDetail());
                        document.add(mainTable);

//--------------------------NEW PAGE START---------------------------------------

                        document.newPage();

                        float[] paymentScheduleTableWidth = {1.4f, 5.5f, 1.8f, 2.1f, 2.0f, 2.0f, 2.0f, 1.8f, 1.6f, 2.0f};
                        PdfPTable paymentScheduleTable = new PdfPTable(paymentScheduleTableWidth);

                        setUpPaymentScheduleTable(paymentScheduleTable);
                        document.add(paymentScheduleTable);


                        float[] paymentHistoryTableWidth = {2.0f, 2.0f, 2.0f, 2.0f, 2.0f, 2.0f, 4.0f, 4.0f, 2.0f, 2.0f, 2.5f};
                        PdfPTable paymentHistoryTable = new PdfPTable(paymentHistoryTableWidth);
                        paymentDetailHistory(paymentHistoryTable);
                        document.add(paymentHistoryTable);
                        document.newPage();
                        document.newPage();
                    }

                }

                document.close();
                if (requestType.equals("nonEmail")) {
                    response.setHeader("Content-Disposition", " filename=Applicant-File-For-Registration-No." + fbsBooking.getRegNumber());
                    response.setHeader("Expires", "0");
                    response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
                    response.setHeader("Pragma", "public");
                    response.setContentType("application/pdf");
                    ServletOutputStream out = response.getOutputStream();
                    baos.writeTo(out);
                    out.flush();
                }

            } catch (DocumentException de) {
                de.printStackTrace();
            }
        } catch (Exception e) {

            HttpSession session = request.getSession(false);
            session.setAttribute("reqPAGE", request.getHeader("referer"));
            System.out.println("EEXCEPTION IN COSUMER REPORT");
            response.sendRedirect(request.getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
            // e.printStackTrace();
        } finally {
            //out.close();
        }
    }

    void paymentDetailHistory(PdfPTable paymentHistoryTable) {

        addEmptyParagraph(paymentHistoryTable, 4, 12);

        Phrase paymentInformationPhrase = fontselectorhd.process("Payment Information");
        PdfPCell paymentInformationCell = new PdfPCell(new Paragraph(paymentInformationPhrase));
        paymentInformationCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        paymentInformationCell.setColspan(12);
        paymentInformationCell.setBorder(Rectangle.NO_BORDER);
        paymentHistoryTable.addCell(paymentInformationCell);
        addEmptyParagraph(
                paymentHistoryTable, 2, 12);
        if (!fbsPaymentList.isEmpty()) {
            Phrase serialNumberPhrase = fontselector.process("SR.NO.");
            PdfPCell serialNumberCell = new PdfPCell(new Paragraph(serialNumberPhrase));
            serialNumberCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            paymentHistoryTable.addCell(serialNumberCell);
            Phrase recieptNumberPhrase = fontselector.process("Receipt No.");
            PdfPCell recieptNumberCell = new PdfPCell(new Paragraph(recieptNumberPhrase));
            recieptNumberCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            paymentHistoryTable.addCell(recieptNumberCell);
            Phrase recieptDatePhrase = fontselector.process("Receipt Date");
            PdfPCell recieptDateCell = new PdfPCell(new Paragraph(recieptDatePhrase));
            recieptDateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            paymentHistoryTable.addCell(recieptDateCell);
            Phrase paymentModePhrase = fontselector.process("Payment Mode");
            PdfPCell paymentModeCell = new PdfPCell(new Paragraph(paymentModePhrase));
            paymentModeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            paymentHistoryTable.addCell(paymentModeCell);
            Phrase chequeNumberPhrase = fontselector.process("Cheque No.");
            PdfPCell chequeNumberCell = new PdfPCell(new Paragraph(chequeNumberPhrase));
            chequeNumberCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            paymentHistoryTable.addCell(chequeNumberCell);
            Phrase chequeDatePhrase = fontselector.process("Cheque Date");
            PdfPCell chequeDateCell = new PdfPCell(new Paragraph(chequeDatePhrase));
            chequeDateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            paymentHistoryTable.addCell(chequeDateCell);
            Phrase drawnOnPhrase = fontselector.process("Drawn On");
            PdfPCell drawnOnCell = new PdfPCell(new Paragraph(drawnOnPhrase));
            drawnOnCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            paymentHistoryTable.addCell(drawnOnCell);
            Phrase clearingBankPhrase = fontselector.process("Clearing Bank");
            PdfPCell clearingBankCell = new PdfPCell(new Paragraph(clearingBankPhrase));
            clearingBankCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            paymentHistoryTable.addCell(clearingBankCell);
            Phrase amountPhrase = fontselector.process("Amount");
            PdfPCell amountCell = new PdfPCell(new Paragraph(amountPhrase));
            amountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            paymentHistoryTable.addCell(amountCell);

            Phrase serviceTaxPhrase = fontselector.process("Service Tax");
            PdfPCell serviceTaxCell = new PdfPCell(new Paragraph(serviceTaxPhrase));
            serviceTaxCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            paymentHistoryTable.addCell(serviceTaxCell);
            Phrase paymentStatusPhrase = fontselector.process("Payment Status");
            PdfPCell paymentStatusCell = new PdfPCell(new Paragraph(paymentStatusPhrase));
            paymentStatusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            paymentHistoryTable.addCell(paymentStatusCell);

            processPaymentHistory(paymentHistoryTable);

            Phrase totalRecieveAmountPhrase = fontselectorhd2.process("Total Received Amount(A)");
            PdfPCell totalRecieveAmountCell = new PdfPCell(new Paragraph(totalRecieveAmountPhrase));
            totalRecieveAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalRecieveAmountCell.setColspan(9);
            totalRecieveAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            paymentHistoryTable.addCell(totalRecieveAmountCell);

            Phrase totalRecieveAmountPhraseValue = fontselectorhd2.process(decimalFormat.format(calculationManager.calculateClearedPaidAmount(fbsPaymentList)));
            PdfPCell totalRecieveAmountCellValue = new PdfPCell(new Paragraph(totalRecieveAmountPhraseValue));
            totalRecieveAmountCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalRecieveAmountCellValue.setColspan(5);
            totalRecieveAmountCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            paymentHistoryTable.addCell(totalRecieveAmountCellValue);
            Phrase totalServiceTaxAmountPhrase = fontselectorhd2.process("Total Service Tax(B)");
            PdfPCell totalServiceTaxAmountCell = new PdfPCell(new Paragraph(totalServiceTaxAmountPhrase));
            totalServiceTaxAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalServiceTaxAmountCell.setColspan(9);
            totalServiceTaxAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            paymentHistoryTable.addCell(totalServiceTaxAmountCell);
            Phrase totalServiceTaxAmountPhraseValue = fontselectorhd2.process((String.valueOf(calculationManager.calculateServiceTaxAmountOnCrearedAmount(fbsPaymentList))));
            PdfPCell totalServiceTaxAmountCellValue = new PdfPCell(new Paragraph(totalServiceTaxAmountPhraseValue));
            totalServiceTaxAmountCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalServiceTaxAmountCellValue.setColspan(5);
            totalServiceTaxAmountCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            paymentHistoryTable.addCell(totalServiceTaxAmountCellValue);

            Phrase totalPaidAmountPhrase = fontselectorhd2.process("Total Paid Amount(A-B)");
            PdfPCell totalPaidAmountCell = new PdfPCell(new Paragraph(totalPaidAmountPhrase));
            totalPaidAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalPaidAmountCell.setColspan(9);
            totalPaidAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            paymentHistoryTable.addCell(totalPaidAmountCell);
            Phrase totalPaidAmountPhraseValue = fontselectorhd2.process(String.valueOf(calculationManager.calculatePaidAmountAfterServiceTaxStatusCleared(fbsPaymentList)));
            PdfPCell totalPaidAmountCellValue = new PdfPCell(new Paragraph(totalPaidAmountPhraseValue));
            totalPaidAmountCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalPaidAmountCellValue.setColspan(5);
            totalPaidAmountCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            //cell28.setHorizontalAlignment(Element.ALIGN_RIGHT);
            paymentHistoryTable.addCell(totalPaidAmountCellValue);
        } else {
            Phrase noPaymentInformationPhrase = fontSelectorNormal.process("No Payment Information available for this Unit");
            PdfPCell noPaymentInformationCell = new PdfPCell(new Paragraph(noPaymentInformationPhrase));
            noPaymentInformationCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            noPaymentInformationCell.setColspan(12);
            noPaymentInformationCell.setBorder(Rectangle.NO_BORDER);
            paymentHistoryTable.addCell(noPaymentInformationCell);

        }



    }

    void processPaymentHistory(PdfPTable paymentHistoryTable) {
        for (int i = 0; i
                < fbsPaymentList.size(); i++) {

            Phrase serialNumberPhraseValue = fontselector.process(String.valueOf(i + 1));
            PdfPCell serialNumberCellValue = new PdfPCell(new Paragraph(serialNumberPhraseValue));
            serialNumberCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            paymentHistoryTable.addCell(serialNumberCellValue);

            Phrase recieptNumberPhraseValue = fontselector.process(String.valueOf(fbsPaymentList.get(i).getPaymentId()));//
            PdfPCell recieptNumberCellValue = new PdfPCell(new Paragraph(recieptNumberPhraseValue));
            recieptNumberCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            paymentHistoryTable.addCell(recieptNumberCellValue);

            Phrase recieptDatePhraseValue = fontselector.process(formatter.format(fbsPaymentList.get(i).getPaymentDate()));//
            PdfPCell recieptDateCellValue = new PdfPCell(new Paragraph(recieptDatePhraseValue));
            recieptDateCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            paymentHistoryTable.addCell(recieptDateCellValue);

            Phrase paymentModePhraseValue = fontselector.process(fbsPaymentList.get(i).getPaymentMode());
            PdfPCell paymentModeCellValue = new PdfPCell(new Paragraph(paymentModePhraseValue));
            paymentModeCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            paymentHistoryTable.addCell(paymentModeCellValue);
            Phrase chequeNumberPhraseValue;
            Phrase chequeDatePhraseValue;
            Phrase drawnOnPhraseValue;


            if (fbsPaymentList.get(i).getPaymentMode().equalsIgnoreCase("cheque")) {
                chequeNumberPhraseValue = fontselector.process(fbsPaymentList.get(i).getChequeNo());
                chequeDatePhraseValue = fontselector.process(formatter.format(fbsPaymentList.get(i).getChequeDate()));
                drawnOnPhraseValue = fontselector.process(fbsPaymentList.get(i).getDrawnOn());


            } else if (fbsPaymentList.get(i).getPaymentMode().equalsIgnoreCase("NEFT")) {
                chequeNumberPhraseValue = fontselector.process("N/A");
                chequeDatePhraseValue = fontselector.process("N/A");
                drawnOnPhraseValue = fontselector.process(fbsPaymentList.get(i).getDrawnOn());


            } else {
                chequeNumberPhraseValue = fontselector.process("N/A");
                chequeDatePhraseValue = fontselector.process("N/A");
                drawnOnPhraseValue = fontselector.process("N/A");


            }

            PdfPCell chequeNumberCellValue = new PdfPCell(new Paragraph(chequeNumberPhraseValue));
            chequeNumberCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            paymentHistoryTable.addCell(chequeNumberCellValue);



            PdfPCell chequeDateCellValue = new PdfPCell(new Paragraph(chequeDatePhraseValue));
            chequeDateCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            paymentHistoryTable.addCell(chequeDateCellValue);


            PdfPCell drawnOncellValue = new PdfPCell(new Paragraph(drawnOnPhraseValue));
            drawnOncellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            paymentHistoryTable.addCell(drawnOncellValue);
            Phrase clearingBankPhraseValue;
            if (fbsPaymentList.get(i).getPaymentMode().equalsIgnoreCase("Cash")) {
                clearingBankPhraseValue = fontselector.process("");
            } else {
                clearingBankPhraseValue = fontselector.process(fbsPaymentList.get(i).getClearingBank());
            }

            PdfPCell clearingBankCellValue = new PdfPCell(new Paragraph(clearingBankPhraseValue));
            clearingBankCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            paymentHistoryTable.addCell(clearingBankCellValue);

            Phrase amountPhraseValue = fontselector.process(String.valueOf(fbsPaymentList.get(i).getPaidAmount()));
            PdfPCell amountCellValue = new PdfPCell(new Paragraph(amountPhraseValue));
            amountCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            paymentHistoryTable.addCell(amountCellValue);

            Phrase serviceTaxAmountPhraseValue = fontselector.process(String.valueOf(calculationManager.calculateServiceTaxOnPaidAmount(fbsPaymentList.get(i))));
            PdfPCell serviceTaxAmountCellValue = new PdfPCell(new Paragraph(serviceTaxAmountPhraseValue));
            serviceTaxAmountCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            paymentHistoryTable.addCell(serviceTaxAmountCellValue);

            Phrase statusPhraseValue = fontselector.process(fbsPaymentList.get(i).getStatus());
            PdfPCell statusCellValue = new PdfPCell(new Paragraph(statusPhraseValue));
            statusCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            paymentHistoryTable.addCell(statusCellValue);



        }


    }

    void setUpPaymentScheduleTable(PdfPTable paymentScheduleTable) {
        Phrase paymentSchedulePhrase = fontselectorhd.process("Payment Schedule");
        PdfPCell paymentScheduleCell = new PdfPCell(new Paragraph(paymentSchedulePhrase));
        paymentScheduleCell.setColspan(10);
        paymentScheduleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        paymentScheduleCell.setBorder(Rectangle.NO_BORDER);
        paymentScheduleTable.addCell(paymentScheduleCell);

        addEmptyParagraph(
                paymentScheduleTable, 2, 10);

        Phrase planNamePhrase = fontselector.process("Plan Name :" + fbsPlanname.getFullName() + "(" + fbsPlanname.getPlanName() + ")");
        PdfPCell planNameCell = new PdfPCell(new Paragraph(planNamePhrase));
        planNameCell.setColspan(6);
        planNameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        planNameCell.setBorder(Rectangle.NO_BORDER);
        paymentScheduleTable.addCell(planNameCell);
        Phrase registrationNumberPhrase = fontselector.process("Registartion No.:" + fbsBooking.getRegNumber());
        PdfPCell registrationNumberCell = new PdfPCell(new Paragraph(registrationNumberPhrase));
        registrationNumberCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        registrationNumberCell.setColspan(6);
        registrationNumberCell.setBorder(Rectangle.NO_BORDER);
        paymentScheduleTable.addCell(registrationNumberCell);
        Phrase projectNamePhrase = fontselector.process("Project Name :" + fbsProject.getProjName());
        PdfPCell projectNameCell = new PdfPCell(new Paragraph(projectNamePhrase));
        projectNameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        projectNameCell.setColspan(4);
        projectNameCell.setBorder(Rectangle.NO_BORDER);
        paymentScheduleTable.addCell(projectNameCell);

        Phrase applicantNamePhrase = fontselector.process("Applicant Name :" + firstFbsApplicant.getApplicantName());
        PdfPCell applicantNameCell = new PdfPCell(new Paragraph(applicantNamePhrase));
        applicantNameCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        applicantNameCell.setColspan(6);
        applicantNameCell.setBorder(Rectangle.NO_BORDER);
        paymentScheduleTable.addCell(applicantNameCell);
        addEmptyParagraph(
                paymentScheduleTable, 3, 10);

        Phrase serialNumberPhrase = fontselector.process("SR.NO.");
        PdfPCell serialNumberCell = new PdfPCell(new Paragraph(serialNumberPhrase));
        serialNumberCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        paymentScheduleTable.addCell(serialNumberCell);
//2
        Phrase descriptionPhrase = fontselector.process("Description");
        PdfPCell descriptionCell = new PdfPCell(new Paragraph(descriptionPhrase));
        descriptionCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        paymentScheduleTable.addCell(descriptionCell);
//3
        Phrase dueDatePhrase = fontselector.process("Due Date");
        PdfPCell dueDateCell = new PdfPCell(new Paragraph(dueDatePhrase));
        dueDateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        paymentScheduleTable.addCell(dueDateCell);
//4
        Phrase installmentAmountPhrase = fontselector.process("Installment Amt.");
        PdfPCell installmentAmountCell = new PdfPCell(new Paragraph(installmentAmountPhrase));
        installmentAmountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        paymentScheduleTable.addCell(installmentAmountCell);
//5
        Phrase otherChargePhrase = fontselector.process("Other Charges");
        PdfPCell otherChargeCell = new PdfPCell(new Paragraph(otherChargePhrase));
        otherChargeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        paymentScheduleTable.addCell(otherChargeCell);
//6
        Phrase plcPhrase = fontselector.process("PLC");
        PdfPCell plcCell = new PdfPCell(new Paragraph(plcPhrase));
        plcCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        paymentScheduleTable.addCell(plcCell);
        //7
        Phrase payableAmountPhrase = fontselector.process(" Payable Amt.");
        PdfPCell payableAmountCell = new PdfPCell(new Paragraph(payableAmountPhrase));
        payableAmountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        paymentScheduleTable.addCell(payableAmountCell);
        //8
        Phrase recieveAmountPhrase = fontselector.process("Recieved Amt.");
        PdfPCell recieveAmountCell = new PdfPCell(new Paragraph(recieveAmountPhrase));
        recieveAmountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        paymentScheduleTable.addCell(recieveAmountCell);
        //9
        Phrase creditAmountPhrase = fontselector.process("Credit");
        PdfPCell creditAmountCell = new PdfPCell(new Paragraph(creditAmountPhrase));
        creditAmountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        paymentScheduleTable.addCell(creditAmountCell);
        //10
        Phrase dueAmountPhrase = fontselector.process("Due Amt.");
        PdfPCell dueAmountcell = new PdfPCell(new Paragraph(dueAmountPhrase));
        dueAmountcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        paymentScheduleTable.addCell(dueAmountcell);

        processPaymentSchedule(
                paymentScheduleTable);// values fill up



        Phrase totalPhrase = fontselectorhd3.process("Total");
        PdfPCell totalCell = new PdfPCell(new Paragraph(totalPhrase));
        totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalCell.setColspan(3);
        paymentScheduleTable.addCell(totalCell);

        Phrase totalBasicSalePricePhrase = fontselectorhd3.process(String.valueOf((calculationManager.calculateFlatBSPAfterDiscount(fbsFlatType, fbsPlanname, fbsDiscount))));//totalBasicSalePrice
        PdfPCell totalBasicSalePriceCell = new PdfPCell(new Paragraph(totalBasicSalePricePhrase));
        totalBasicSalePriceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        paymentScheduleTable.addCell(totalBasicSalePriceCell);
        //5
        Phrase totalOtherChargesPhrase = fontselectorhd3.process(String.valueOf((calculationManager.calculateOtherCharge(fbsChargeList) + calculationManager.calculateParkingCharge(fbsParkingList))));//totalOtherCharges
        PdfPCell totalOtherChargesCell = new PdfPCell(new Paragraph(totalOtherChargesPhrase));
        totalOtherChargesCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        paymentScheduleTable.addCell(totalOtherChargesCell);
        //6 plc
        Phrase totalPLCPhrase = fontselectorhd3.process(String.valueOf((calculationManager.calculatePLCCharge(fbsPlcAllots, fbsFlatType))));//totalPlcPercentage
        PdfPCell totalPLCCell = new PdfPCell(new Paragraph(totalPLCPhrase));
        totalPLCCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        paymentScheduleTable.addCell(totalPLCCell);
        //7 totalamountinstallment
        Phrase totalInstallmentPhrase = fontselectorhd3.process(String.valueOf((calculationManager.calculateTotalCost(fbsFlatType, fbsPlanname, fbsDiscount, fbsParkingList, fbsPlcAllots, fbsChargeList))));//sumofinstallmentamount
        PdfPCell totalInstallmentCell = new PdfPCell(new Paragraph(totalInstallmentPhrase));
        totalInstallmentCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        paymentScheduleTable.addCell(totalInstallmentCell);
        //8
        Phrase totalRecieveAmountPhrase = fontselectorhd3.process(String.valueOf(calculationManager.calculatePaidAmountAfterServiceTaxStatusCleared(fbsPaymentList)));//totalreceivedamount
        PdfPCell totalrecieveAmountCell = new PdfPCell(new Paragraph(totalRecieveAmountPhrase));
        totalrecieveAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        paymentScheduleTable.addCell(totalrecieveAmountCell);
        //9

        Phrase totalCreditAmountPhrase;
        Phrase totalOutStandingAmountPhrase;


        if (totalDueOrCreditAmount > 0) {
            totalCreditAmountPhrase = fontselectorhd3.process(String.valueOf(totalDueOrCreditAmount));//lastcredit
            totalOutStandingAmountPhrase = fontselectorhd3.process(String.valueOf(0));//outStandingAmount


        } else {
            totalDueOrCreditAmount *= (-1);
            totalCreditAmountPhrase = fontselectorhd3.process(String.valueOf(0));//lastcredit
            totalOutStandingAmountPhrase = fontselectorhd3.process(String.valueOf(totalDueOrCreditAmount));//outStandingAmount


        }
        PdfPCell totalCreditAmountCell = new PdfPCell(new Paragraph(totalCreditAmountPhrase));
        totalCreditAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        paymentScheduleTable.addCell(totalCreditAmountCell);
        //10

        PdfPCell totalOutStandingAmountCell = new PdfPCell(new Paragraph(totalOutStandingAmountPhrase));
        totalOutStandingAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        paymentScheduleTable.addCell(totalOutStandingAmountCell);



    }

    void processPaymentSchedule(PdfPTable paymentScheduleTable) {
        double plcInstallmentAmount = 0, bspInstallmentAmount = 0;



        for (int i = 0; i
                < fbsPayplanList.size(); i++) {
            Phrase serialNumberPhraseValue = fontselector.process(String.valueOf(i + 1));
            PdfPCell serialNumberCellValue = new PdfPCell(new Paragraph(serialNumberPhraseValue));
            serialNumberCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            paymentScheduleTable.addCell(serialNumberCellValue);

            Phrase planDescriptionPhraseValue = fontselectordes.process(fbsPayplanList.get(i).getPlanDesc().toUpperCase());
            PdfPCell planDescriptionCellValue = new PdfPCell(new Paragraph(planDescriptionPhraseValue));
            planDescriptionCellValue.setHorizontalAlignment(Element.ALIGN_LEFT);
            paymentScheduleTable.addCell(planDescriptionCellValue);

            String dueDate = formatter.format(fbsPayplanList.get(i).getDueDate());//firstDate
            Phrase dueDatePhraseValue = fontselector.process(dueDate);
            PdfPCell dueDateCellValue = new PdfPCell(new Paragraph(dueDatePhraseValue));
            dueDateCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            paymentScheduleTable.addCell(dueDateCellValue);
            bspInstallmentAmount = calculationManager.calculateBSPInstallmentAmount(fbsPayplanList.get(i), fbsFlatType, fbsPlanname, fbsDiscount);
            Phrase installmentAmountPhraseValue = fontselector.process(String.valueOf(calculationManager.roundOfUptoTwoDecimal(bspInstallmentAmount)));//netBasicSaleprice
            PdfPCell installmentAmountCellValue = new PdfPCell(new Paragraph(installmentAmountPhraseValue));
            installmentAmountCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            paymentScheduleTable.addCell(installmentAmountCellValue);


            double payableAmount = 0;
            Phrase otherChargePhraseValue;


            if (i == fbsPayplanList.size() - 1) {
                otherChargePhraseValue = fontselector.process(String.valueOf(calculationManager.calculateOtherCharge(fbsChargeList) + calculationManager.calculateParkingCharge(fbsParkingList)));//totalOtherCharges
                payableAmount += calculationManager.calculateOtherCharge(fbsChargeList) + calculationManager.calculateParkingCharge(fbsParkingList);


            } else {
                otherChargePhraseValue = fontselector.process("N/A");//totalOtherCharges


            }
            PdfPCell otherChargeCellValue = new PdfPCell(new Paragraph(otherChargePhraseValue));
            otherChargeCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            paymentScheduleTable.addCell(otherChargeCellValue);
            plcInstallmentAmount = calculationManager.calculatePLCInstallmentAmount(fbsPayplanList.get(i), fbsPlcAllots, fbsFlatType);
            Phrase plcPhraseValue = fontselector.process(String.valueOf(plcInstallmentAmount));//plc_percentage
            PdfPCell plcCellValue = new PdfPCell(new Paragraph(plcPhraseValue));
            plcCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            paymentScheduleTable.addCell(plcCellValue);

            payableAmount += bspInstallmentAmount + plcInstallmentAmount;
            Phrase payableAmountPhraseValue = fontselector.process(String.valueOf(calculationManager.roundOfUptoTwoDecimal(payableAmount)));//installment_amount
            PdfPCell payableAmountCellValue = new PdfPCell(new Paragraph(payableAmountPhraseValue));
            payableAmountCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            paymentScheduleTable.addCell(payableAmountCellValue);




            if (fbsPayplanList.get(i).getDueDate().compareTo(new Date()) == 0 || fbsPayplanList.get(i).getDueDate().compareTo(new Date()) > 0) {
                updateRecieveCreditAndDueAmout1(paymentScheduleTable);


            } else {

                updateRecieveCreditAndDueAmount(paymentScheduleTable, i, payableAmount);


            }
        }

    }

    void updateRecieveCreditAndDueAmout1(PdfPTable paymentScheduleTable) {

        Phrase recieveAmountPhraseValue = fontselector.process("0");
        Phrase creditAmountPhraseValue = fontselector.process("0");
        Phrase dueAmountPhraseValue = fontselector.process("0");
        PdfPCell recieveAmountCellValue = new PdfPCell(recieveAmountPhraseValue);
        recieveAmountCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        paymentScheduleTable.addCell(recieveAmountCellValue);

        PdfPCell creditAmountCellValue = new PdfPCell(creditAmountPhraseValue);
        creditAmountCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        paymentScheduleTable.addCell(creditAmountCellValue);


        PdfPCell dueAmountCellValue = new PdfPCell(new Paragraph(dueAmountPhraseValue));
        dueAmountCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        paymentScheduleTable.addCell(dueAmountCellValue);



    }

    void updateRecieveCreditAndDueAmount(PdfPTable paymentScheduleTable, int i, double payableAmount) {
        Phrase recieveAmountPhraseValue;


        double recieveAmountBetweenDate = 0;


        if (i == 0) {
            recieveAmountBetweenDate = calculationManager.calculateRecieveAmountBetweenDueDate(null, fbsPayplanList.get(i).getDueDate(), fbsPaymentList);
            recieveAmountPhraseValue = fontselector.process(String.valueOf(recieveAmountBetweenDate));//amountReceived


        } else {
            recieveAmountBetweenDate = calculationManager.calculateRecieveAmountBetweenDueDate(fbsPayplanList.get(i - 1).getDueDate(), fbsPayplanList.get(i).getDueDate(), fbsPaymentList);
            recieveAmountPhraseValue = fontselector.process(String.valueOf(recieveAmountBetweenDate));//amountReceived


        }


        PdfPCell recieveAmountCellValue = new PdfPCell(new Paragraph(recieveAmountPhraseValue));
        recieveAmountCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        paymentScheduleTable.addCell(recieveAmountCellValue);



        double creditAmount = recieveAmountBetweenDate - payableAmount;
        totalDueOrCreditAmount += creditAmount;
        Phrase creditAmountPhraseValue;
        Phrase dueAmountPhraseValue;


        if (creditAmount > 0) {

            creditAmountPhraseValue = fontselector.process(String.valueOf(creditAmount));//paidamtcredit
            dueAmountPhraseValue = fontselector.process(String.valueOf(0));//outStandingAmount


        } else if (creditAmount < 0) {
            creditAmount *= (-1);
            creditAmountPhraseValue = fontselector.process("0");//paidamtcredit
            dueAmountPhraseValue = fontselector.process(String.valueOf(String.valueOf(creditAmount)));//outStandingAmount


        } else {
            creditAmountPhraseValue = fontselector.process("0");//paidamtcredit
            dueAmountPhraseValue = fontselector.process(String.valueOf(0));//outStandingAmount


        }

        PdfPCell creditAmountCellValue = new PdfPCell(creditAmountPhraseValue);
        creditAmountCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        paymentScheduleTable.addCell(creditAmountCellValue);


        PdfPCell dueAmountCellValue = new PdfPCell(new Paragraph(dueAmountPhraseValue));
        dueAmountCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        paymentScheduleTable.addCell(dueAmountCellValue);



    }

    PdfPTable getFlatDetail() {
        float[] widths3 = {2.0f, 3.0f};
        PdfPTable flatDetailTable = new PdfPTable(widths3);

        Phrase projectPhrase = fontselector.process("Project :");
        PdfPCell projectCell = new PdfPCell(new Paragraph(projectPhrase));
        projectCell.setBorder(Rectangle.NO_BORDER);
        flatDetailTable.addCell(projectCell);
        Phrase projectPhraseValue = fontselectorhd2.process(fbsProject.getProjName());
        PdfPCell projectCellValue = new PdfPCell(new Paragraph(projectPhraseValue));
        projectCellValue.setBorder(Rectangle.NO_BORDER);
        flatDetailTable.addCell(projectCellValue);
        Phrase blockPhrase = fontselector.process("Block Name :");
        PdfPCell blockCell = new PdfPCell(new Paragraph(blockPhrase));
        blockCell.setBorder(Rectangle.NO_BORDER);
        flatDetailTable.addCell(blockCell);
        Phrase blockPhraseValue = fontselectorhd2.process(fbsBlock.getBlockName());
        PdfPCell blockCellValue = new PdfPCell(new Paragraph(blockPhraseValue));
        blockCellValue.setBorder(Rectangle.NO_BORDER);
        flatDetailTable.addCell(blockCellValue);
        Phrase unitPhrase = fontselector.process("Unit Code :");
        PdfPCell unitCell = new PdfPCell(new Paragraph(unitPhrase));
        unitCell.setBorder(Rectangle.NO_BORDER);
        flatDetailTable.addCell(unitCell);
        Phrase unitPhraseValue = fontselector.process(fbsBlock.getFbsProject().getProjAbvr() + "-" + fbsBlock.getBlockAbvr() + "-" + fbsFlat.getFlatNo());
        PdfPCell unitCellValue = new PdfPCell(new Paragraph(unitPhraseValue));
        unitCellValue.setBorder(Rectangle.NO_BORDER);
        flatDetailTable.addCell(unitCellValue);
        Phrase unitTypePhrase = fontselector.process("Unit Type :");
        PdfPCell unitTypeCell = new PdfPCell(new Paragraph(unitTypePhrase));
        unitTypeCell.setBorder(Rectangle.NO_BORDER);
        flatDetailTable.addCell(unitTypeCell);
        Phrase unitTypePhraseValue = fontselector.process(fbsFlatType.getFlatSpecification());
        PdfPCell unitTypeCellValue = new PdfPCell(new Paragraph(unitTypePhraseValue));
        unitTypeCellValue.setBorder(Rectangle.NO_BORDER);
        flatDetailTable.addCell(unitTypeCellValue);


        Phrase addressPhrase = fontselector.process("Address :");
        PdfPCell addressCell = new PdfPCell(new Paragraph(addressPhrase));
        addressCell.setBorder(Rectangle.NO_BORDER);
        flatDetailTable.addCell(addressCell);
        Phrase addressPhraseValue = fontselector.process(fbsProject.getAddress());
        PdfPCell addressCellValue = new PdfPCell(new Paragraph(addressPhraseValue));
        addressCellValue.setBorder(Rectangle.NO_BORDER);
        flatDetailTable.addCell(addressCellValue);

        Phrase planPhrase = fontselector.process("Plan Name :");
        PdfPCell planCell = new PdfPCell(new Paragraph(planPhrase));
        planCell.setBorder(Rectangle.NO_BORDER);
        flatDetailTable.addCell(planCell);




        return flatDetailTable;


    }

    PdfPTable getOtherChargeDetail() //fbs Charge List
    {
        float[] otherChargeTablewidth = {1f, 2.9f, 3.0f};
        PdfPTable otherChargeTable = new PdfPTable(otherChargeTablewidth);
        Phrase otherChargePhrase = fontselectorhd2.process("Other Charges Information");
        PdfPCell otherChargeCell = new PdfPCell(new Paragraph(otherChargePhrase));
        otherChargeCell.setBorder(Rectangle.NO_BORDER);
        otherChargeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        otherChargeCell.setColspan(3);
        otherChargeTable.addCell(otherChargeCell);

        Phrase otherChargeSerialNoPhrase = fontselector.process("S.No.");
        PdfPCell otherChargeSerialNoCell = new PdfPCell(new Paragraph(otherChargeSerialNoPhrase));
        otherChargeTable.addCell(otherChargeSerialNoCell);

        Phrase otherChargeDescriptionPhrase = fontselector.process("Description");
        PdfPCell otherChargeDescriptionCell = new PdfPCell(new Paragraph(otherChargeDescriptionPhrase));
        otherChargeTable.addCell(otherChargeDescriptionCell);
        Phrase otherChargeAmountPhrase = fontselector.process("Amount(Rs)");
        PdfPCell otherChargeAmountCell = new PdfPCell(new Paragraph(otherChargeAmountPhrase));
        otherChargeAmountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        otherChargeTable.addCell(otherChargeAmountCell);


        //other charge value

        // calculationManager.getFbsChargeList();
        //  List<FbsCharge> fbsChargeList = calculationManager.getFbsChargeList();



        for (int i = 0; i
                < fbsChargeList.size(); i++) {
            Phrase otherChargeSerialNoPhraseVlaue = fontselector.process(String.valueOf(i + 1));
            PdfPCell otherChargeSerialNoCellValue = new PdfPCell(new Paragraph(otherChargeSerialNoPhraseVlaue));
            otherChargeSerialNoCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            otherChargeTable.addCell(otherChargeSerialNoCellValue);

            Phrase otherChargeDescriptionPhraseValue = fontselector.process(fbsChargeList.get(i).getChargeName());
            PdfPCell otherChargeDescriptionCellValue = new PdfPCell(new Paragraph(otherChargeDescriptionPhraseValue));
            otherChargeTable.addCell(otherChargeDescriptionCellValue);

            Phrase otherChargeAmountPhraseValue = fontselector.process(String.valueOf(calculationManager.roundOfUptoTwoDecimal(fbsChargeList.get(i).getAmount()))); //fontselector.process(String.valueOf(fbsCharge.get(l).getAmount() * flat_Sba));
            PdfPCell otherChargeAmountCellValue = new PdfPCell(new Paragraph(otherChargeAmountPhraseValue));
            otherChargeAmountCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            otherChargeTable.addCell(otherChargeAmountCellValue);


        }

        //other charge value end

        Phrase otherChargeTotalPhrase = fontselectorhd2.process("Total");
        PdfPCell otherChargeTotalCell = new PdfPCell(new Paragraph(otherChargeTotalPhrase));
        otherChargeTotalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        otherChargeTotalCell.setColspan(2);
        otherChargeTable.addCell(otherChargeTotalCell);


        Phrase otherChargeTotalPhraseValue = fontselectorhd2.process(String.valueOf(calculationManager.roundOfUptoTwoDecimal(calculationManager.calculateOtherCharge(fbsChargeList)))); //totalOtherCharges
        PdfPCell otherChargeTotalCellValue = new PdfPCell(new Paragraph(otherChargeTotalPhraseValue));
        otherChargeTotalCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        otherChargeTable.addCell(otherChargeTotalCellValue);

        addEmptyParagraph(
                otherChargeTable, 3, 3);
        getParkingDetail(
                otherChargeTable);
        addEmptyParagraph(
                otherChargeTable, 1, 3);






        return otherChargeTable;



    }

    void getParkingDetail(PdfPTable otherChargeTable) {
        Phrase parkingDetailPhrase = fontselectorhd2.process("Parking Details");
        PdfPCell parkingDetailCell = new PdfPCell(new Paragraph(parkingDetailPhrase));
        parkingDetailCell.setBorder(Rectangle.NO_BORDER);
        parkingDetailCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        parkingDetailCell.setColspan(3);
        otherChargeTable.addCell(parkingDetailCell);
        Phrase serialNumberPhrase = fontselector.process("S.NO.");
        PdfPCell serialNumberCell = new PdfPCell(new Paragraph(serialNumberPhrase));
        serialNumberCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        otherChargeTable.addCell(serialNumberCell);
        Phrase parkingTypePhrase = fontselector.process("Parking Type");
        PdfPCell parkingTypeCell = new PdfPCell(new Paragraph(parkingTypePhrase));
        parkingTypeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        otherChargeTable.addCell(parkingTypeCell);
        Phrase parkingChargesPhrase = fontselector.process("Charges(Rs)");
        PdfPCell parkingChargesCell = new PdfPCell(new Paragraph(parkingChargesPhrase));
        parkingChargesCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        otherChargeTable.addCell(parkingChargesCell);

        //PArking Charges List
        // List<FbsParking> fbsParkingAllotList = calculationManager.getFbsParkingList();




        if (fbsParkingList.isEmpty()) {
            Phrase p3 = fontselector.process("No Parking Alloted");
            PdfPCell pc3 = new PdfPCell(new Paragraph(p3));
            pc3.setColspan(3);
            pc3.setHorizontalAlignment(Element.ALIGN_CENTER);
            otherChargeTable.addCell(pc3);


        } else {
            for (int i = 0; i
                    < fbsParkingList.size(); i++) {
                // parkingTypeId = fbsParkingAllotList.get(i).getParkingTypeId();
                // fbsParkingType = fbsParkingTypeFacade.find(parkingTypeId);
                Phrase serialNumberPhraseValue = fontselector.process(String.valueOf(i + 1));
                PdfPCell serialNumberCellValue = new PdfPCell(new Paragraph(serialNumberPhraseValue));
                serialNumberCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                otherChargeTable.addCell(serialNumberCellValue);
                Phrase parkingTypePhraseValue = fontselector.process(fbsParkingList.get(i).getFbsParkingType().getName());
                PdfPCell parkingTypeCellValue = new PdfPCell(new Paragraph(parkingTypePhraseValue));
                parkingTypeCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                otherChargeTable.addCell(parkingTypeCellValue);
                Phrase parkingChargePhraseValue = fontselector.process(String.valueOf(calculationManager.roundOfUptoTwoDecimal(fbsParkingList.get(i).getCharges())));
                PdfPCell parkingChargeCellValue = new PdfPCell(new Paragraph(parkingChargePhraseValue));
                parkingChargeCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
                otherChargeTable.addCell(parkingChargeCellValue);




            }

            PdfPCell parkingChargestotalCell = new PdfPCell(new Phrase(fontselectorhd2.process("Total")));
            parkingChargestotalCell.setColspan(2);
            parkingChargestotalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            otherChargeTable.addCell(parkingChargestotalCell);

            Phrase parkingChargesTotalPhraseValue = new Phrase(fontselectorhd2.process(String.valueOf(calculationManager.roundOfUptoTwoDecimal(calculationManager.calculateParkingCharge(fbsParkingList)))));
            PdfPCell parkingChargesTotalCellValue = new PdfPCell(parkingChargesTotalPhraseValue);
            parkingChargesTotalCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);


            otherChargeTable.addCell(parkingChargesTotalCellValue);


        }


    }

    PdfPTable getPLCChargeDetail() {
        float[] plcTableWidth = {1f, 2.9f, 3.0f};
        PdfPTable plcTable = new PdfPTable(plcTableWidth);

        Phrase plcTitlePhrase = fontselectorhd2.process("PLC Information");
        PdfPCell plcCell = new PdfPCell(new Paragraph(plcTitlePhrase));
        plcCell.setBorder(Rectangle.NO_BORDER);
        plcCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        plcCell.setColspan(3);
        plcTable.addCell(plcCell);

        Phrase plcSerialNoPhrase = fontselector.process("S.No.");
        PdfPCell plcSerialNoCell = new PdfPCell(new Paragraph(plcSerialNoPhrase));
        plcTable.addCell(plcSerialNoCell);
        Phrase plcNamePhrase = fontselector.process("PLC Name");
        PdfPCell plcNameCell = new PdfPCell(new Paragraph(plcNamePhrase));
        plcTable.addCell(plcNameCell);
        Phrase plcChargePhrase = fontselector.process("PLC Charge(Rs/sqft)");
        PdfPCell plcChargeCell = new PdfPCell(new Paragraph(plcChargePhrase));
        plcTable.addCell(plcChargeCell);

//PLC VALUE



        for (int i = 0; i
                < fbsPlcAllots.size(); i++) {
            Phrase plcSerialNoPhraseValue = fontselector.process(String.valueOf(i + 1));
            PdfPCell plcSerialNoCellValue = new PdfPCell(new Paragraph(plcSerialNoPhraseValue));
            plcTable.addCell(plcSerialNoCellValue);

            Phrase plcNamePhraseValue = fontselector.process(fbsPlcAllots.get(i).getFbsPlc().getPlcName());
            PdfPCell plcNameCellValue = new PdfPCell(new Paragraph(plcNamePhraseValue));
            plcTable.addCell(plcNameCellValue);
            Phrase plcChargePhraseValue = fontselector.process(String.valueOf(calculationManager.roundOfUptoTwoDecimal(fbsPlcAllots.get(i).getFbsPlc().getPlcCharge())));
            PdfPCell plcChargeCellValue = new PdfPCell(new Paragraph(plcChargePhraseValue));
            plcTable.addCell(plcChargeCellValue);


        }
//PLC Value END

        addEmptyParagraph(plcTable, 3, 3);
        getBrokerInformation(
                plcTable);


        return plcTable;



    }

    void getBrokerInformation(PdfPTable plcTable) {
        System.out.println("FbsBroker ID" + fbsBroker.getBrName());
        Phrase brokerInformationPhrase = fontselectorhd2.process("Broker Information");
        PdfPCell brokerInformationCell = new PdfPCell(new Paragraph(brokerInformationPhrase));
        brokerInformationCell.setBorder(Rectangle.NO_BORDER);
        brokerInformationCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        brokerInformationCell.setColspan(3);
        plcTable.addCell(brokerInformationCell);
        Phrase brokerIDPhrase = fontselector.process("Code");
        PdfPCell brokerIDCell = new PdfPCell(new Paragraph(brokerIDPhrase));
        plcTable.addCell(brokerIDCell);
        Phrase brokerNamePhrase = fontselector.process("Broker Name");
        PdfPCell brokerNameCell = new PdfPCell(new Paragraph(brokerNamePhrase));
        plcTable.addCell(brokerNameCell);
        Phrase brokerCategoryPhrase = fontselector.process("Broker Category");
        PdfPCell brokerCategoryCell = new PdfPCell(new Paragraph(brokerCategoryPhrase));
        plcTable.addCell(brokerCategoryCell);
        Phrase brokerIDPhraseValue = fontselector.process(String.valueOf(fbsBroker.getBrokerId()));
        PdfPCell brokerIDCellValue = new PdfPCell(new Paragraph(brokerIDPhraseValue));
        plcTable.addCell(brokerIDCellValue);
        Phrase brokerNamePhraseValue = fontselector.process(fbsBroker.getBrName().substring(0, 1).toUpperCase() + fbsBroker.getBrName().substring(1));
        PdfPCell brokerNameCellValue = new PdfPCell(new Paragraph(brokerNamePhraseValue));
        plcTable.addCell(brokerNameCellValue);

        Phrase brokerCategoryPhraseValue = fontselector.process(fbsBroker.getFbsBrokerCat().getCategoryName());
        PdfPCell brokerCategoryCellValue = new PdfPCell(new Paragraph(brokerCategoryPhraseValue));
        plcTable.addCell(brokerCategoryCellValue);



    }

    PdfPCell setUpTableHead(String projectName) {
        //float[] headTableWidth = {1.5f, 1.9f};
        //PdfPTable headTable = new PdfPTable(2);

        Phrase headPhrase = new Phrase("APPLICANT FILE  (" + projectName + ")");
        PdfPCell headcell = new PdfPCell(new Paragraph(headPhrase));
        headcell.setBorder(Rectangle.NO_BORDER);
        headcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headcell.setColspan(2);

        //  headTable.addCell(headcell);




        return headcell;



    }

    PdfPTable getApplicantDetail(FbsApplicant applicant) {
        float[] applicantTablewidth = {2.0f, 3.0f};
        PdfPTable table = new PdfPTable(applicantTablewidth);


        Phrase namePhrase = fontselector.process("Name :");
        PdfPCell nameCell = new PdfPCell(new Paragraph(namePhrase));
        nameCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(nameCell);


        Phrase namePhraseValue = fontselectorhd2.process(applicant.getApplicantName());
        PdfPCell nameCellValue = new PdfPCell(new Paragraph(namePhraseValue));
        nameCellValue.setBorder(Rectangle.NO_BORDER);
        table.addCell(nameCellValue);

        Phrase mobileNoPhrase = fontselector.process("Mobile :");
        PdfPCell mobileNoCell = new PdfPCell(new Paragraph(mobileNoPhrase));
        mobileNoCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(mobileNoCell);

        Phrase mobileNoPhraseValue = fontselectorhd2.process(applicant.getMobile());
        PdfPCell mobileNoCellValue = new PdfPCell(new Paragraph(mobileNoPhraseValue));
        mobileNoCellValue.setBorder(Rectangle.NO_BORDER);
        table.addCell(mobileNoCellValue);



        Phrase emailPhrase = fontselector.process("Email :");
        PdfPCell emailCell = new PdfPCell(new Paragraph(emailPhrase));
        emailCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(emailCell);


        Phrase emailPhraseValue = fontselectorhd2.process(applicant.getEmail());
        PdfPCell emailCellValue = new PdfPCell(new Paragraph(emailPhraseValue));
        emailCellValue.setBorder(Rectangle.NO_BORDER);
        table.addCell(emailCellValue);

        Phrase panNoPhrase = fontselector.process("Pan No :");
        PdfPCell panNoCell = new PdfPCell(new Paragraph(panNoPhrase));
        panNoCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(panNoCell);


        Phrase panNoPhraseValue = fontselectorhd2.process(applicant.getPanNo());
        PdfPCell panNoCellValue = new PdfPCell(new Paragraph(panNoPhraseValue));
        panNoCellValue.setBorder(Rectangle.NO_BORDER);
        table.addCell(panNoCellValue);


        Phrase addressPhrase = fontselector.process("Address :");
        PdfPCell addressCell = new PdfPCell(new Paragraph(addressPhrase));
        addressCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(addressCell);

        Phrase addressPhraseValue = fontselectorhd2.process(applicant.getResAdd());
        PdfPCell addressCellValue = new PdfPCell(new Paragraph(addressPhraseValue));
        addressCellValue.setBorder(Rectangle.NO_BORDER);
        table.addCell(addressCellValue);



        return table;


    }

    PdfPTable getflatCalculationTable() {
        float[] widths6 = {3.0f, 0.5f, 1.0f, 2.0f};
        PdfPTable calulationtable = new PdfPTable(widths6);
        Phrase superAreaPhrase = fontselector.process("Super Area(sqft) :");
        PdfPCell superAreaCell = new PdfPCell(new Paragraph(superAreaPhrase));
        superAreaCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        superAreaCell.setBorder(Rectangle.NO_BORDER);
        superAreaCell.setColspan(2);
        calulationtable.addCell(superAreaCell);

        Phrase superAreaPhraseValue = fontselector.process(String.valueOf(calculationManager.roundOfUptoTwoDecimal(fbsFlatType.getFlatSba())));//flat sba
        PdfPCell superAreaCellValue = new PdfPCell(new Paragraph(superAreaPhraseValue));
        superAreaCellValue.setColspan(2);
        superAreaCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        superAreaCellValue.setBorder(Rectangle.NO_BORDER);
        calulationtable.addCell(superAreaCellValue);


        Phrase basicRatePhrase = fontselector.process("Basic Rate(Rs/sqft) :");//BSP
        PdfPCell basicRateCell = new PdfPCell(new Paragraph(basicRatePhrase));
        basicRateCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        basicRateCell.setBorder(Rectangle.NO_BORDER);
        basicRateCell.setColspan(2);
        calulationtable.addCell(basicRateCell);


        Phrase basicRatePhraseValue = fontselector.process(String.valueOf(calculationManager.roundOfUptoTwoDecimal(fbsFlatType.getFlatBsp())));//BSP rate
        PdfPCell basicRateCellValue = new PdfPCell(new Paragraph(basicRatePhraseValue));
        basicRateCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        basicRateCellValue.setBorder(Rectangle.NO_BORDER);
        basicRateCellValue.setColspan(2);
        calulationtable.addCell(basicRateCellValue);
        Phrase multiplyPhrase = fontselector.process("MULTIPLY :");
        PdfPCell multiplyCell = new PdfPCell(new Paragraph(multiplyPhrase));
        multiplyCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        multiplyCell.setColspan(4);
        multiplyCell.setBorder(Rectangle.NO_BORDER);
        calulationtable.addCell(multiplyCell);

        Phrase linePhrase = fontselector.process("_________________________________________________");
        PdfPCell lineCell = new PdfPCell(new Paragraph(linePhrase));
        lineCell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED_ALL);
        lineCell.setBorder(Rectangle.NO_BORDER);
        lineCell.setColspan(4);
        calulationtable.addCell(lineCell);



        Phrase basicSalePricePhrase = fontselectorsale.process("Basic Sale Price  :");
        PdfPCell basicSalePriceCell = new PdfPCell(new Paragraph(basicSalePricePhrase));
        basicSalePriceCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        basicSalePriceCell.setBorder(Rectangle.NO_BORDER);
        basicSalePriceCell.setColspan(2);
        calulationtable.addCell(basicSalePriceCell);

        Phrase basicSalePricePhraseValue = fontselectorsale.process(String.valueOf(calculationManager.roundOfUptoTwoDecimal(calculationManager.calculateFlatBSP(fbsFlatType))));
        PdfPCell basicSalePriceCellValue = new PdfPCell(new Paragraph(basicSalePricePhraseValue));
        basicSalePriceCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        basicSalePriceCellValue.setBorder(Rectangle.NO_BORDER);
        basicSalePriceCellValue.setColspan(2);
        calulationtable.addCell(basicSalePriceCellValue);

        Phrase lessPhrase = fontselectorsale.process("Less :");
        PdfPCell lessCell = new PdfPCell(new Paragraph(lessPhrase));
        lessCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        lessCell.setBorder(Rectangle.NO_BORDER);
        lessCell.setColspan(4);
        calulationtable.addCell(lessCell);
        Phrase planDiscountPhrase = fontselector.process("Plan Discount :" + String.valueOf(calculationManager.roundOfUptoTwoDecimal(fbsPlanname.getDiscount())) + "%");
        PdfPCell planDiscountCell = new PdfPCell(new Paragraph(planDiscountPhrase));
        planDiscountCell.setColspan(2);
        planDiscountCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        planDiscountCell.setBorder(Rectangle.NO_BORDER);
        calulationtable.addCell(planDiscountCell);


        Phrase planDiscountPhraseValue = fontselector.process(String.valueOf(calculationManager.roundOfUptoTwoDecimal(calculationManager.calculatePlanDiscount(fbsPlanname, fbsFlatType))));
        PdfPCell planDiscountCellValue = new PdfPCell(new Paragraph(planDiscountPhraseValue));
        planDiscountCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        planDiscountCellValue.setBorder(Rectangle.NO_BORDER);
        planDiscountCellValue.setColspan(2);
        calulationtable.addCell(planDiscountCellValue);
        Phrase otherDiscountPhrase;
        if (fbsDiscount != null) {
            otherDiscountPhrase = fontselector.process("Other Discount :" + String.valueOf(fbsDiscount.getPercentage()) + "%");
        } else {
            otherDiscountPhrase = fontselector.process("Other Discount :" + String.valueOf(0) + "%");
        }
        PdfPCell otherDiscountCell = new PdfPCell(new Paragraph(otherDiscountPhrase));
        otherDiscountCell.setColspan(2);
        otherDiscountCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        otherDiscountCell.setBorder(Rectangle.NO_BORDER);
        calulationtable.addCell(otherDiscountCell);

        Phrase otherDiscountPhraseValue = fontselector.process(String.valueOf(calculationManager.roundOfUptoTwoDecimal(calculationManager.calculateBookingDiscount(fbsDiscount, fbsFlatType))));
        PdfPCell otherDiscountCellValue = new PdfPCell(new Paragraph(otherDiscountPhraseValue));
        otherDiscountCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        otherDiscountCellValue.setBorder(Rectangle.NO_BORDER);
        otherDiscountCellValue.setColspan(2);
        calulationtable.addCell(otherDiscountCellValue);


        calulationtable.addCell(lineCell);

        Phrase netBasicSalePricePhrase = fontselectorsale.process("Net Basic Sale Price:");
        PdfPCell netBasicSalePriceCell = new PdfPCell(new Paragraph(netBasicSalePricePhrase));
        netBasicSalePriceCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        netBasicSalePriceCell.setColspan(2);
        netBasicSalePriceCell.setBorder(Rectangle.NO_BORDER);
        calulationtable.addCell(netBasicSalePriceCell);
        Phrase netBasicSalePricePhraseValue = fontselectorsale.process(String.valueOf(calculationManager.roundOfUptoTwoDecimal(calculationManager.calculateFlatBSPAfterDiscount(fbsFlatType, fbsPlanname, fbsDiscount))));
        PdfPCell netBasicSalePriceCellValue = new PdfPCell(new Paragraph(netBasicSalePricePhraseValue));
        netBasicSalePriceCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        netBasicSalePriceCellValue.setBorder(Rectangle.NO_BORDER);
        netBasicSalePriceCellValue.setColspan(2);
        calulationtable.addCell(netBasicSalePriceCellValue);

        Phrase addPhrase = fontselectorsale.process("Add :");
        PdfPCell addCell = new PdfPCell(new Paragraph(addPhrase));
        addCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        addCell.setColspan(4);
        addCell.setBorder(Rectangle.NO_BORDER);
        calulationtable.addCell(addCell);
        Phrase plcPhrase = fontselector.process("PLC : " + String.valueOf(calculationManager.calculatePLC(fbsPlcAllots)) + " Rs/sqft");
        PdfPCell plcCell = new PdfPCell(new Paragraph(plcPhrase));
        plcCell.setColspan(2);
        plcCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        plcCell.setBorder(Rectangle.NO_BORDER);
        calulationtable.addCell(plcCell);
        Phrase plcPhraseValue = fontselector.process(String.valueOf(calculationManager.roundOfUptoTwoDecimal(calculationManager.calculatePLCCharge(fbsPlcAllots, fbsFlatType))));
        PdfPCell plcCellValue = new PdfPCell(new Paragraph(plcPhraseValue));
        plcCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        plcCellValue.setBorder(Rectangle.NO_BORDER);
        plcCellValue.setColspan(2);
        calulationtable.addCell(plcCellValue);
        Phrase otherChargePhrase = fontselector.process("Other Charges:");
        PdfPCell otherChargeCell = new PdfPCell(new Paragraph(otherChargePhrase));
        otherChargeCell.setColspan(2);
        otherChargeCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        otherChargeCell.setBorder(Rectangle.NO_BORDER);
        calulationtable.addCell(otherChargeCell);


        Phrase otherChargePhraseValue = fontselector.process(String.valueOf(calculationManager.roundOfUptoTwoDecimal(calculationManager.calculateOtherCharge(fbsChargeList))));
        PdfPCell otherChargeCellValue = new PdfPCell(new Paragraph(otherChargePhraseValue));
        otherChargeCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        otherChargeCellValue.setBorder(Rectangle.NO_BORDER);
        otherChargeCellValue.setColspan(2);
        calulationtable.addCell(otherChargeCellValue);

        Phrase parkingChargePhrase = fontselector.process("Parking Charges:");
        PdfPCell parkingChargeCell = new PdfPCell(new Paragraph(parkingChargePhrase));
        parkingChargeCell.setColspan(2);
        parkingChargeCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        parkingChargeCell.setBorder(Rectangle.NO_BORDER);
        calulationtable.addCell(parkingChargeCell);

        Phrase parkingChargePhraseValue = fontselector.process(String.valueOf(calculationManager.roundOfUptoTwoDecimal(calculationManager.calculateParkingCharge(fbsParkingList))));
        PdfPCell parkingChargeCellValue = new PdfPCell(new Paragraph(parkingChargePhraseValue));
        parkingChargeCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        parkingChargeCellValue.setBorder(Rectangle.NO_BORDER);
        parkingChargeCellValue.setColspan(2);
        calulationtable.addCell(parkingChargeCellValue);

        calulationtable.addCell(lineCell);

        Phrase totalCostPhrase = fontselectorsale.process("Total Cost(A):");
        PdfPCell totalCostCell = new PdfPCell(new Paragraph(totalCostPhrase));
        totalCostCell.setColspan(2);
        totalCostCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        totalCostCell.setBorder(Rectangle.NO_BORDER);
        calulationtable.addCell(totalCostCell);




        Phrase phrate18 = fontselectorsale.process(String.valueOf(calculationManager.roundOfUptoTwoDecimal(calculationManager.calculateTotalCost(fbsFlatType, fbsPlanname, fbsDiscount, fbsParkingList, fbsPlcAllots, fbsChargeList))));
        PdfPCell totalCostCellValue = new PdfPCell(new Paragraph(phrate18));
        totalCostCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalCostCellValue.setBorder(Rectangle.NO_BORDER);
        totalCostCellValue.setColspan(2);
        calulationtable.addCell(totalCostCellValue);

        //  Phrase phrate19 = fontselectorsale.process("Less :");
        // PdfPCell cellrate19 = new PdfPCell(new Paragraph(phrate19));
        // cellrate19.setHorizontalAlignment(Element.ALIGN_LEFT);
        // cellrate19.setBorder(Rectangle.NO_BORDER);
        // cellrate19.setColspan(4);
        calulationtable.addCell(lessCell);
        Phrase totalPaidAmountPhrase = fontselector.process("Total Paid Amount(C):");
        PdfPCell totalPaidAmountCell = new PdfPCell(new Paragraph(totalPaidAmountPhrase));
        totalPaidAmountCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        totalPaidAmountCell.setBorder(Rectangle.NO_BORDER);
        totalPaidAmountCell.setColspan(2);
        calulationtable.addCell(totalPaidAmountCell);
        Phrase paidAmountPhraseValue = fontselector.process(String.valueOf(calculationManager.roundOfUptoTwoDecimal(calculationManager.calculateClearedPaidAmount(fbsPaymentList))));//decimalFormat.format(Paid_amount)
        PdfPCell paidAmountCellValue = new PdfPCell(new Paragraph(paidAmountPhraseValue));
        paidAmountCellValue.setColspan(2);
        paidAmountCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        paidAmountCellValue.setBorder(Rectangle.NO_BORDER);
        calulationtable.addCell(paidAmountCellValue);

        Phrase serviceTaxPhrase = fontselector.process("Service Tax(D):");
        PdfPCell serviceTaxCell = new PdfPCell(new Paragraph(serviceTaxPhrase));
        serviceTaxCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        serviceTaxCell.setBorder(Rectangle.NO_BORDER);
        serviceTaxCell.setColspan(2);
        calulationtable.addCell(serviceTaxCell);

        Phrase serviceTaxPhraseValue = fontselector.process(calculationManager.roundOfUptoTwoDecimal((calculationManager.calculateServiceTaxAmountOnCrearedAmount(fbsPaymentList))));
        PdfPCell serviceTaxCellValue = new PdfPCell(new Paragraph(serviceTaxPhraseValue));
        serviceTaxCellValue.setColspan(2);
        serviceTaxCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        serviceTaxCellValue.setBorder(Rectangle.NO_BORDER);
        calulationtable.addCell(serviceTaxCellValue);

        Phrase paidAmountAfterServiceTaxPhrase = fontselectorsale.process("Paid Amount(B=C-D):");
        PdfPCell paidAmountAfterServiceTaxCell = new PdfPCell(new Paragraph(paidAmountAfterServiceTaxPhrase));
        paidAmountAfterServiceTaxCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        paidAmountAfterServiceTaxCell.setBorder(Rectangle.NO_BORDER);
        paidAmountAfterServiceTaxCell.setColspan(2);
        calulationtable.addCell(paidAmountAfterServiceTaxCell);

        Phrase paidAmountAfterServiceTaxPhraseValue = fontselectorsale.process(calculationManager.roundOfUptoTwoDecimal((calculationManager.calculateClearedPaidAmount(fbsPaymentList) - calculationManager.calculateServiceTaxAmountOnCrearedAmount(fbsPaymentList))));
        PdfPCell paidAmountAfterServiceTaxCellvalue = new PdfPCell(new Paragraph(paidAmountAfterServiceTaxPhraseValue));
        paidAmountAfterServiceTaxCellvalue.setColspan(2);
        paidAmountAfterServiceTaxCellvalue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        paidAmountAfterServiceTaxCellvalue.setBorder(Rectangle.NO_BORDER);
        calulationtable.addCell(paidAmountAfterServiceTaxCellvalue);
        calulationtable.addCell(lineCell);

        Phrase payableAmountPhrase = fontselectorsale.process("Total Payable Amount(A-B):");
        PdfPCell payableAmountCell = new PdfPCell(new Paragraph(payableAmountPhrase));
        //cell.setBorder(Rectangle.NO_BORDER);
        payableAmountCell.setColspan(3);
        payableAmountCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        payableAmountCell.setBorder(Rectangle.NO_BORDER);
        calulationtable.addCell(payableAmountCell);

        Phrase payableAmountPhraseValue = fontselectorsale.process(calculationManager.roundOfUptoTwoDecimal((calculationManager.calculateTotalCost(fbsFlatType, fbsPlanname, fbsDiscount, fbsParkingList, fbsPlcAllots, fbsChargeList) - calculationManager.calculateClearedPaidAmount(fbsPaymentList) + calculationManager.calculateServiceTaxAmountOnCrearedAmount(fbsPaymentList))));
        PdfPCell payableAmountCellValue = new PdfPCell(new Paragraph(payableAmountPhraseValue));
        //cell.setBorder(Rectangle.NO_BORDER);
        payableAmountCellValue.setColspan(1);
        payableAmountCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        payableAmountCellValue.setBorder(Rectangle.NO_BORDER);
        calulationtable.addCell(payableAmountCellValue);



        return calulationtable;


    }

    void addEmptyParagraph(PdfPTable pdfPTable, int numberOfParagraph, int columnSpan) {
        for (int i = 0; i
                < numberOfParagraph; i++) {
            Phrase phrase = fontselectorhd2.process("");
            PdfPCell cellSpace = new PdfPCell(new Paragraph(phrase));
            cellSpace.setBorder(Rectangle.NO_BORDER);
            cellSpace.setColspan(columnSpan);
            pdfPTable.addCell(cellSpace);



        }

    }

    void getDetail(int registrationNumber) {
        fbsBooking = calculationManager.getBooking(registrationNumber);
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
        // fbsApplicantList ;
        fbsPlcAllots = calculationManager.getPLCAllotList(fbsFlat);
        // fbsPLCList ;
        fbsParkingList = calculationManager.getParkingList(fbsBooking);
        fbsChargeList = calculationManager.getChargeList(fbsProject);
        fbsPaymentList = calculationManager.getClearedFbsPaymentList(fbsBooking);
        fbsPayplanList = calculationManager.getFbsPayplanList(fbsPlanname);
        System.out.println("fbsPlan List Size" + fbsPayplanList.size());


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
        processRequest(request, response);


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
        processRequest(request, response);


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
}