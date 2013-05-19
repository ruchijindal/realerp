/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.report;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.smp.realerp.entity.FbsBrPayment;
import org.smp.realerp.entity.FbsCompany;
import org.smp.realerp.entity.FbsFlat;
import org.smp.realerp.entity.FbsProject;
import org.smp.realerp.managedbean.LoginBean;
import org.smp.realerp.session.FbsBrPaymentFacade;
import org.smp.realerp.session.FbsCompanyFacade;
import java.util.List;
import org.smp.realerp.session.FbsProjectFacade;

/**
 *
 * @author smp
 */
@WebServlet(name = "BrokerPaymentReceipt", urlPatterns = {"/brokerPaymentReceipt"})
public class BrokerPaymentReceipt extends HttpServlet {

    @PersistenceContext(unitName = "RealErpPU")
    EntityManager em;
    @EJB
    FbsCompanyFacade fbsCompanyFacade;
    @EJB
    FbsBrPaymentFacade fbsBrPaymentFacade;
    @EJB
    FbsProjectFacade fbsProjectFacade;
    FbsCompany fbsCompany = new FbsCompany();
    FbsProject fbsProject = new FbsProject();
    FbsBrPayment fbsBrPayment = new FbsBrPayment();
    FbsFlat fbsFlat = new FbsFlat();
    List<FbsProject> fbsProjectList = new ArrayList<FbsProject>();
    Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    Font blackFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL, BaseColor.BLACK);
    Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
    Font blackSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 6, Font.NORMAL, BaseColor.BLACK);
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    String[] unitdo = {"", " One", " Two", " Three", " Four", " Five",
        " Six", " Seven", " Eight", " Nine", " Ten", " Eleven", " Twelve",
        " Thirteen", " Fourteen", " Fifteen", " Sixteen", " Seventeen",
        " Eighteen", " Nineteen"};
    String[] tens = {"", "Ten", " Twenty", " Thirty", " Forty", " Fifty",
        " Sixty", " Seventy", " Eighty", " Ninety"};
    String[] digit = {"", " Hundred", " Thousand", " Lakh", " Crore"};
    double r;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DocumentException {
        response.setContentType("text/html;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        String paymentId = request.getParameter("paymentId");
        System.out.println("hellooooooo...............");
        if (paymentId != null) {
            fbsBrPayment = fbsBrPaymentFacade.find(Integer.parseInt(paymentId));
            fbsCompany = fbsCompanyFacade.find(LoginBean.fbsLogin.getFbsCompany().getCompanyId());
            fbsProjectList = (List<FbsProject>) fbsCompany.getFbsProjectCollection();
            fbsProject = fbsProjectFacade.find(fbsProjectList.get(0).getProjId());
            fbsFlat = fbsBrPayment.getFbsBooking().getFbsFlat();

            int len, q = 0, r = 0;
            String ltr = " ";
            String Str = "Rupees";

            int num = fbsBrPayment.getAmount().intValue();
            while (num > 0) {

                len = numberCount(num);

                //Take the length of the number and do letter conversion
                switch (len) {
                    case 8:
                        q = num / 10000000;
                        r = num % 10000000;
                        ltr = twonum(q);
                        Str = Str + ltr + digit[4];
                        num = r;
                        break;

                    case 7:
                    case 6:
                        q = num / 100000;
                        r = num % 100000;
                        ltr = twonum(q);
                        Str = Str + ltr + digit[3];
                        num = r;
                        break;

                    case 5:
                    case 4:

                        q = num / 1000;
                        r = num % 1000;
                        ltr = twonum(q);
                        Str = Str + ltr + digit[2];
                        num = r;
                        break;

                    case 3:


                        if (len == 3) {
                            r = num;
                        }
                        ltr = threenum(r);
                        Str = Str + ltr;
                        num = 0;
                        break;

                    case 2:

                        ltr = twonum(num);
                        Str = Str + ltr;
                        num = 0;
                        break;

                    case 1:
                        Str = Str + unitdo[num];
                        num = 0;
                        break;
                    default:

                        num = 0;
                        System.exit(1);


                }
                if (num == 0) {
                    System.out.println(Str + " Only");
                }

                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Document document = new Document(PageSize.A4, 0, 0, 60, 30);
                    PdfWriter docWriter = PdfWriter.getInstance(document, baos);
                    document.open();
                    document.addTitle("Broker Payment Receipt");
                    document.addSubject("Using iText");
                    document.addKeywords("Java, PDF, iText");

                    Paragraph preface = new Paragraph();
                    preface.add(new Paragraph("", blackFont));
                    addEmptyLine(preface, 1);
                    document.add(preface);
                    //create the outerMost table
                    PdfPTable outerTable = new PdfPTable(1);
                    //create the first inner table for company detail
                    PdfPTable table1 = new PdfPTable(1);
                    table1.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
                    Paragraph paragraph = new Paragraph("");
                    Phrase phrase1 = new Phrase(fbsCompany.getCompanyName(), smallBold);
                    paragraph.add(phrase1);
                    addEmptyLine(paragraph, 1);
                    phrase1 = new Phrase(fbsCompany.getAddress(), smallBold);
                    paragraph.add(phrase1);
                    addEmptyLine(paragraph, 1);
                    phrase1 = new Phrase("Telephone:" + fbsCompany.getTelNumber(), smallBold);
                    paragraph.add(phrase1);
                    addEmptyLine(paragraph, 1);
                    phrase1 = new Phrase("Email:" + fbsCompany.getEmail(), smallBold);
                    paragraph.add(phrase1);
                    PdfPCell cell1 = new PdfPCell(paragraph);
                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell1.setBorder(Rectangle.NO_BORDER);
                    table1.addCell(cell1);
                    outerTable.addCell(table1);
                    //create table for broker payment receipt heading 
                    PdfPTable table2 = new PdfPTable(1);
                    cell1 = new PdfPCell(new Paragraph("Broker Payment Receipt", smallBold));
                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell1.setBorder(Rectangle.NO_BORDER);
                    table2.addCell(cell1);
                    outerTable.addCell(table2);
                    //create third table for receipt details
                    PdfPTable table3 = new PdfPTable(2);
                    table3.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
                    paragraph = new Paragraph("");
                    phrase1 = new Phrase("Receipt No:" + fbsBrPayment.getPaymentId(), smallBold);
                    paragraph.add(phrase1);
                    addEmptyLine(paragraph, 1);
                    phrase1 = new Phrase("Customer Id:" + fbsBrPayment.getFbsBroker().getBrokerId(), smallBold);
                    paragraph.add(phrase1);
                    cell1 = new PdfPCell(paragraph);
                    cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell1.setBorder(Rectangle.NO_BORDER);
                    table3.addCell(cell1);
                    paragraph = new Paragraph("Date: " + dateFormat.format(new Date()), smallBold);
                    cell1 = new PdfPCell(paragraph);
                    cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell1.setBorder(Rectangle.NO_BORDER);
                    table3.addCell(cell1);
                    outerTable.addCell(table3);
                    //table4 for broker details
                    PdfPTable table4 = new PdfPTable(1);
                    table4.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
                    paragraph = new Paragraph("");
                    phrase1 = new Phrase("Amount paid to ", blackFont);
                    paragraph.add(phrase1);
                    phrase1 = new Phrase("Mr. " + fbsBrPayment.getFbsBroker().getBrName(), smallBold);
                    paragraph.add(phrase1);
                    phrase1 = new Phrase(" under the  broker  category of ", blackFont);
                    paragraph.add(phrase1);
                    phrase1 = new Phrase(fbsBrPayment.getFbsBroker().getFbsBrokerCat().getCategoryName(), smallBold);
                    paragraph.add(phrase1);
                    phrase1 = new Phrase(" with ", blackFont);
                    paragraph.add(phrase1);
                    phrase1 = new Phrase(fbsBrPayment.getFbsBroker().getFbsBrokerCat().getCommission() + "0%", smallBold);
                    paragraph.add(phrase1);
                    phrase1 = new Phrase("commission", blackFont);
                    paragraph.add(phrase1);
                    phrase1 = new Phrase(" residing at " + fbsBrPayment.getFbsBroker().getBrAdd() + " a sum of Rs. ", blackFont);
                    paragraph.add(phrase1);
                    phrase1 = new Phrase(fbsBrPayment.getAmount().toString() + "0/- (" + Str + " Only)", smallBold);
                    paragraph.add(phrase1);
                    phrase1 = new Phrase("against Flat No. ", blackFont);
                    paragraph.add(phrase1);
                    phrase1 = new Phrase(fbsBrPayment.getFbsBooking().getFbsFlat().getFlatNo() + " at Block " + fbsBrPayment.getFbsBooking().getFbsFlat().getFbsFloor().getFbsBlock().getBlockName(), smallBold);
                    paragraph.add(phrase1);
                    phrase1 = new Phrase(" measuring ", blackFont);
                    paragraph.add(phrase1);
                    phrase1 = new Phrase(fbsFlat.getFbsFlatType().getFlatSba() + " SQ FT Super Area ", smallBold);
                    paragraph.add(phrase1);
                    phrase1 = new Phrase("(Approximately) in ", blackFont);
                    paragraph.add(phrase1);
                    phrase1 = new Phrase(fbsProject.getProjName() + ", " + fbsProject.getAddress() + ", " + fbsProject.getCity() + ", " + fbsProject.getState(), smallBold);
                    paragraph.add(phrase1);
                    phrase1 = new Phrase(" as per following details: ", blackFont);
                    paragraph.add(phrase1);
                    addEmptyLine(paragraph, 2);
                    cell1 = new PdfPCell(paragraph);
                    cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell1.setBorder(Rectangle.NO_BORDER);
                    table4.addCell(cell1);

                    if (fbsBrPayment.getPaymentMode().equals("Cheque")) {
                        PdfPTable chqDetailTable = new PdfPTable(5);
                        PdfPCell chqCell1 = new PdfPCell(new Phrase("S.NO.", smallBold));
                        chqCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        PdfPCell chqCell2 = new PdfPCell(new Phrase("Cheque No.", smallBold));
                        chqCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                        PdfPCell chqCell3 = new PdfPCell(new Phrase("Drawn On", smallBold));
                        chqCell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                        PdfPCell chqCell4 = new PdfPCell(new Phrase("Dated", smallBold));
                        chqCell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                        paragraph = new Paragraph();
                        paragraph.add(new Phrase("Amount", smallBold));
                        addEmptyLine(paragraph, 1);
                        paragraph.add(new Phrase("(Rs.)", smallBold));
                        PdfPCell chqCell5 = new PdfPCell(new Phrase(paragraph));
                        chqCell5.setHorizontalAlignment(Element.ALIGN_CENTER);

                        chqDetailTable.addCell(chqCell1);
                        chqDetailTable.addCell(chqCell2);
                        chqDetailTable.addCell(chqCell3);
                        chqDetailTable.addCell(chqCell4);
                        chqDetailTable.addCell(chqCell5);

                        PdfPCell chqCell11 = new PdfPCell(new Phrase("1", smallBold));
                        chqCell11.setHorizontalAlignment(Element.ALIGN_CENTER);
                        PdfPCell chqCell22 = new PdfPCell(new Phrase(fbsBrPayment.getChequeNo(), blackFont));
                        chqCell22.setHorizontalAlignment(Element.ALIGN_CENTER);
                        PdfPCell chqCell33 = new PdfPCell(new Phrase(fbsBrPayment.getDrawnOn(), blackFont));
                        chqCell33.setHorizontalAlignment(Element.ALIGN_CENTER);
                        PdfPCell chqCell44 = new PdfPCell(new Phrase(dateFormat.format(fbsBrPayment.getPaymentDate()), blackFont));
                        chqCell44.setHorizontalAlignment(Element.ALIGN_CENTER);
                        PdfPCell chqCell55 = new PdfPCell(new Phrase(fbsBrPayment.getAmount().toString() + "0/-", blackFont));
                        chqCell55.setHorizontalAlignment(Element.ALIGN_CENTER);

                        chqDetailTable.addCell(chqCell11);
                        chqDetailTable.addCell(chqCell22);
                        chqDetailTable.addCell(chqCell33);
                        chqDetailTable.addCell(chqCell44);
                        chqDetailTable.addCell(chqCell55);
                        table4.addCell(chqDetailTable);
                    } else if (fbsBrPayment.getPaymentMode().equals("Cash")) {
                        PdfPTable cashDetailTable = new PdfPTable(3);
                        PdfPCell cashCell1 = new PdfPCell(new Phrase("S.NO.", smallBold));
                        cashCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        PdfPCell cashCell2 = new PdfPCell(new Phrase("Dated", smallBold));
                        cashCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                        paragraph = new Paragraph();
                        paragraph.add(new Phrase("Amount", smallBold));
                        addEmptyLine(paragraph, 1);
                        paragraph.add(new Phrase("(Rs.)", smallBold));
                        PdfPCell cashCell3 = new PdfPCell(new Phrase(paragraph));
                        cashCell3.setHorizontalAlignment(Element.ALIGN_CENTER);

                        cashDetailTable.addCell(cashCell1);
                        cashDetailTable.addCell(cashCell2);
                        cashDetailTable.addCell(cashCell3);

                        PdfPCell cashCell11 = new PdfPCell(new Phrase("1", smallBold));
                        cashCell11.setHorizontalAlignment(Element.ALIGN_CENTER);
                        PdfPCell cashCell22 = new PdfPCell(new Phrase(dateFormat.format(fbsBrPayment.getPaymentDate()), blackFont));
                        cashCell22.setHorizontalAlignment(Element.ALIGN_CENTER);
                        PdfPCell cashCell33 = new PdfPCell(new Phrase(fbsBrPayment.getAmount().toString() + "0/-", blackFont));
                        cashCell33.setHorizontalAlignment(Element.ALIGN_CENTER);

                        cashDetailTable.addCell(cashCell11);
                        cashDetailTable.addCell(cashCell22);
                        cashDetailTable.addCell(cashCell33);

                        table4.addCell(cashDetailTable);
                    } else if (fbsBrPayment.getPaymentMode().equals("RTGS/NEFT")) {
                        PdfPTable neftDetailTable = new PdfPTable(5);
                        PdfPCell neftCell1 = new PdfPCell(new Phrase("S.NO.", smallBold));
                        neftCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        PdfPCell neftCell2 = new PdfPCell(new Phrase("Transaction Id", smallBold));
                        neftCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                        PdfPCell neftCell3 = new PdfPCell(new Phrase("Bank Name", smallBold));
                        neftCell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                        PdfPCell neftCell4 = new PdfPCell(new Phrase("Dated", smallBold));
                        neftCell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                        paragraph = new Paragraph();
                        paragraph.add(new Phrase("Amount", smallBold));
                        addEmptyLine(paragraph, 1);
                        paragraph.add(new Phrase("(Rs.)", smallBold));
                        PdfPCell neftCell5 = new PdfPCell(new Phrase(paragraph));
                        neftCell5.setHorizontalAlignment(Element.ALIGN_CENTER);

                        neftDetailTable.addCell(neftCell1);
                        neftDetailTable.addCell(neftCell2);
                        neftDetailTable.addCell(neftCell3);
                        neftDetailTable.addCell(neftCell4);
                        neftDetailTable.addCell(neftCell5);

                        PdfPCell neftcell11 = new PdfPCell(new Phrase("1", smallBold));
                        neftcell11.setHorizontalAlignment(Element.ALIGN_CENTER);
                        PdfPCell neftcell22 = new PdfPCell(new Phrase(fbsBrPayment.getTransactionId(), blackFont));
                        neftcell22.setHorizontalAlignment(Element.ALIGN_CENTER);
                        PdfPCell neftcell33 = new PdfPCell(new Phrase(fbsBrPayment.getDrawnOn(), blackFont));
                        neftcell33.setHorizontalAlignment(Element.ALIGN_CENTER);
                        PdfPCell neftcell44 = new PdfPCell(new Phrase(dateFormat.format(fbsBrPayment.getPaymentDate()), blackFont));
                        neftcell44.setHorizontalAlignment(Element.ALIGN_CENTER);
                        PdfPCell neftcell55 = new PdfPCell(new Phrase(fbsBrPayment.getAmount().toString() + "/-", blackFont));
                        neftcell55.setHorizontalAlignment(Element.ALIGN_CENTER);

                        neftDetailTable.addCell(neftcell11);
                        neftDetailTable.addCell(neftcell22);
                        neftDetailTable.addCell(neftcell33);
                        neftDetailTable.addCell(neftcell44);
                        neftDetailTable.addCell(neftcell55);
                        table4.addCell(neftDetailTable);
                    }

                    outerTable.addCell(table4);
                    //create table 5 for footer information
                    PdfPTable table5 = new PdfPTable(1);
                    paragraph = new Paragraph("");
                    addEmptyLine(paragraph, 4);
                    phrase1 = new Phrase("For ", blackFont);
                    paragraph.add(phrase1);
                    phrase1 = new Phrase(fbsCompany.getCompanyName(), smallBold);
                    paragraph.add(phrase1);
                    addEmptyLine(paragraph, 1);
                    phrase1 = new Phrase("(Account Officer)", smallBold);
                    paragraph.add(phrase1);
                    addEmptyLine(paragraph, 1);
                    ZapfDingbatsList zapfDingbatsList = new ZapfDingbatsList(42, 15);
                    zapfDingbatsList.add(new ListItem("This receipt is subject to realization of cheque/draft.", blackSmallFont));
                    zapfDingbatsList.add(new ListItem("The receipts are not transferable without written consent of the company.", blackSmallFont));
                    zapfDingbatsList.add(new ListItem("This is only the receipt for the remittance as abouve and this does not entitle you to claim ownership / title of the above property unless you are the confirmed owner of the property, as per Company's record.", blackSmallFont));
                    cell1 = new PdfPCell();
                    cell1.addElement(paragraph);
                    cell1.addElement(zapfDingbatsList);
                    cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell1.setBorder(Rectangle.NO_BORDER);
                    table5.addCell(cell1);
                    outerTable.addCell(table5);

                    document.add(outerTable);
                    document.newPage();
                    document.close();

                    response.setHeader("Expires", "0");
                    response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
                    response.setHeader("Pragma", "public");
                    response.setContentType("application/pdf");

                    baos.writeTo(out);
                    out.flush();
                } catch (Exception ex) {
                    System.out.println("Exception in Broker Payment Receipt " + ex);
                } finally {
                    // out.close();
                }
            }
        }
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    int numberCount(int num) {
        int cnt = 0;

        while (num > 0) {
            r = num % 10;
            cnt++;
            num = num / 10;
        }

        return cnt;
    }

    //Function for Conversion of two digit
    String twonum(int numq) {
        int numr, nq;
        String ltr = "";

        nq = numq / 10;
        numr = numq % 10;

        if (numq > 19) {
            ltr = ltr + tens[nq] + unitdo[numr];
        } else {
            ltr = ltr + unitdo[numq];
        }

        return ltr;
    }

    //Function for Conversion of three digit
    String threenum(int numq) {
        int numr, nq;
        String ltr = "";

        nq = numq / 100;
        numr = numq % 100;

        if (numr == 0) {
            ltr = ltr + unitdo[nq] + digit[1];
        } else {
            ltr = ltr + unitdo[nq] + digit[1] + " and" + twonum(numr);
        }
        return ltr;



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
            Logger.getLogger(BrokerPaymentReceipt.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(BrokerPaymentReceipt.class.getName()).log(Level.SEVERE, null, ex);
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
}
