/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.report;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.smp.realerp.entity.FbsPayment;
import org.smp.realerp.managedbean.LoginBean;
import org.smp.realerp.manager.CalculationManager;

/**
 *
 * @author SMP Technologies
 */
@WebServlet(name = "collectionReport", urlPatterns = {"/collectionReport"})
public class collectionReport extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @EJB
    CalculationManager calculationManager;
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    private static Font blackFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL, BaseColor.BLACK);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
    private static Font blackSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 6, Font.NORMAL, BaseColor.BLACK);
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date startDate = null; //dateFormat.parse(request.getParameter("startDate"));
    Date endDate = null;// dateFormat.parse(request.getParameter("endDate"));

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        try {
            /* TODO output your page here
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet collectionReport</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet collectionReport at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
             */
            try {
                startDate = dateFormat.parse(request.getParameter("startDate"));

            } catch (Exception e) {
                System.out.println("start Date is null ");
                // e.printStackTrace();
            }
            try {
                endDate = dateFormat.parse(request.getParameter("endDate"));


            } catch (Exception e) {
                System.out.println("End Date is null ");
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 0, 0, 0, 0);
            PdfWriter docWriter = PdfWriter.getInstance(document, bos);
            document.open();
            document.addTitle("Collection Report");
            document.addSubject("Using iText");
            document.addKeywords("Java, PDF, iText");
            PdfPTable mainTable = new PdfPTable(1);
            addEmptyParagraphToDocument(document, 5);
            mainTable.addCell(setupCompanyDetail());

            checkDate(startDate, endDate);
            mainTable.addCell(collectionTable(startDate, endDate));
            mainTable.addCell(collectionDetailTable(startDate, endDate));
            document.add(mainTable);
            document.newPage();
            document.close();
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            response.setContentType("application/pdf");
            bos.writeTo(out);
            out.flush();
        } catch (Exception e) {
            System.out.println("Exception in CollectionReport");
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

    PdfPTable collectionTable(Date startDate, Date endDate) {
        PdfPTable collectionTable = new PdfPTable(1);
        Phrase collectionReportPhrase = new Paragraph("Collection Report", smallBold);

        if (isDate(startDate) && isDate(endDate)) {
            collectionReportPhrase = new Paragraph("Collection Report From " + dateFormat.format(startDate) + " To " + dateFormat.format(endDate), smallBold);
        }

        PdfPCell collectionReportCell = new PdfPCell(collectionReportPhrase);
        collectionReportCell.setBorder(PdfPCell.NO_BORDER);
        collectionReportCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        collectionTable.addCell(collectionReportCell);


        return collectionTable;
    }

    PdfPTable collectionDetailTable(Date startDate, Date endDate) {
        PdfPTable collectionDetailTable = new PdfPTable(4);
        collectionDetailTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        PdfPCell unitCodeCell = new PdfPCell(new Phrase("UNIT CODE", smallBold));
        unitCodeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        collectionDetailTable.addCell(unitCodeCell);

        PdfPCell paymentDateCell = new PdfPCell(new Phrase("PAYMENT DATE", smallBold));
        paymentDateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        collectionDetailTable.addCell(paymentDateCell);

        PdfPCell statusCell = new PdfPCell(new Phrase("STATUS", smallBold));
        statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        collectionDetailTable.addCell(statusCell);

        PdfPCell paidAmountCell = new PdfPCell(new Phrase("PAID AMOUNT", smallBold));
        paidAmountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        collectionDetailTable.addCell(paidAmountCell);

        List<FbsPayment> fbsPaymentList = new ArrayList<FbsPayment>();
        if (!isDate(startDate) && !isDate(endDate)) {
            fbsPaymentList = calculationManager.getPaymentListForCompany(LoginBean.fbsLogin.getFbsCompany());
        } else {
            fbsPaymentList = calculationManager.getPaymentListBetweenStartDateAndEndDateForCompany(startDate, endDate, LoginBean.fbsLogin.getFbsCompany());
        }

        for (int i = 0; i < fbsPaymentList.size(); i++) {
            PdfPCell unitCodeCellValue = new PdfPCell(new Phrase(fbsPaymentList.get(i).getFbsBooking().getRegNumber().toString(), blackFont));
            unitCodeCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell paymentDateCellValue = new PdfPCell(new Phrase(dateFormat.format(fbsPaymentList.get(i).getPaymentDate()), blackFont));
            paymentDateCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell chequeStatusCellValue = new PdfPCell(new Phrase(fbsPaymentList.get(i).getStatus(), blackFont));
            chequeStatusCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell paidAmountCellValue = new PdfPCell(new Phrase(String.valueOf(calculationManager.roundOfUptoTwoDecimal(fbsPaymentList.get(i).getPaidAmount())), blackFont)); //new PdfPCell(new Phrase(String.valueOf(fbsPaymentList.get(i).getPaidAmount()).toString(), blackFont));
            paidAmountCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);

            collectionDetailTable.addCell(unitCodeCellValue);
            collectionDetailTable.addCell(paymentDateCellValue);
            collectionDetailTable.addCell(chequeStatusCellValue);
            collectionDetailTable.addCell(paidAmountCellValue);
        }

        PdfPCell totalPaidAmountCell = new PdfPCell(new Phrase("Total Paid Amount", smallBold));
        totalPaidAmountCell.setColspan(3);
        totalPaidAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        collectionDetailTable.addCell(totalPaidAmountCell);

        PdfPCell totalPaidAmountCellValue = new PdfPCell(new Phrase(calculationManager.roundOfUptoTwoDecimal(calculationManager.calculateTotalPaidAmount(fbsPaymentList)), smallBold));
        totalPaidAmountCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);

        collectionDetailTable.addCell(totalPaidAmountCellValue);

        PdfPCell clearedAmountCell = new PdfPCell(new Phrase("Total Cleared Amount", smallBold));
        clearedAmountCell.setColspan(3);
        clearedAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        collectionDetailTable.addCell(clearedAmountCell);

        PdfPCell clearedAmountCellValue = new PdfPCell(new Phrase(calculationManager.roundOfUptoTwoDecimal(calculationManager.calculateClearedPaidAmount(fbsPaymentList)), smallBold));
        clearedAmountCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);

        collectionDetailTable.addCell(clearedAmountCellValue);

        PdfPCell unClearedAmountCell = new PdfPCell(new Phrase("Total Pending Amount", smallBold));
        unClearedAmountCell.setColspan(3);
        unClearedAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        collectionDetailTable.addCell(unClearedAmountCell);

        PdfPCell unClearedAmountCellValue = new PdfPCell(new Phrase(calculationManager.roundOfUptoTwoDecimal(calculationManager.calculateTotalPaidAmount(fbsPaymentList) - calculationManager.calculateClearedPaidAmount(fbsPaymentList)), smallBold));
        unClearedAmountCellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);

        collectionDetailTable.addCell(unClearedAmountCellValue);
        return collectionDetailTable;
    }

    void addEmptyParagraphToDocument(Document document, int numberOfParagraph) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Paragraph("", blackFont));
        for (int i = 0; i < numberOfParagraph; i++) {
            paragraph.add(new Paragraph(" "));
        }
        document.add(paragraph);
    }

    void checkDate(Date startDate, Date endDate) {
        Date tempDate = null;

        if ((isDate(startDate) && isDate(endDate)) && startDate.after(endDate)) {
            tempDate = startDate;
            startDate = endDate;
            endDate = tempDate;
        }
    }

    boolean isDate(Date date) {
        if (date == null) {
            return false;
        } else {
            return true;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}