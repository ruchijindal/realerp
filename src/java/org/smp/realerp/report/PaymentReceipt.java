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
import org.smp.realerp.entity.*;
import org.smp.realerp.managedbean.LoginBean;
import org.smp.realerp.session.FbsCompanyFacade;
import org.smp.realerp.session.FbsPaymentFacade;
import org.smp.realerp.session.FbsProjectFacade;


/**
 *
 * @author smp
 */
@WebServlet(name = "PaymentReceipt", urlPatterns = {"/paymentReceipt"})
public class PaymentReceipt extends HttpServlet {

    @PersistenceContext(unitName = "RealErpPU")
    EntityManager em;
    @EJB
    FbsCompanyFacade fbsCompanyFacade;
    @EJB
    FbsPaymentFacade fbsPaymentFacade;
    @EJB
    FbsProjectFacade fbsProjectFacade;
    FbsProject fbsProject = new FbsProject();
    FbsCompany fbsCompany = new FbsCompany();
    FbsPayment fbsPayment = new FbsPayment();
    FbsFlat fbsFlat = new FbsFlat();
    FbsApplicant fbsApplicant = new FbsApplicant();
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
    double serviceTax;
    double totalAmount;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        String paymentId = request.getParameter("paymentId");
        System.out.println("hellooooooo...............");
        if (paymentId != null) {
            fbsPayment = fbsPaymentFacade.find(Integer.parseInt(paymentId));
            fbsCompany = fbsCompanyFacade.find(LoginBean.fbsLogin.getFbsCompany().getCompanyId());
            fbsProjectList = (List<FbsProject>) fbsCompany.getFbsProjectCollection();
            fbsProject = fbsProjectFacade.find(fbsProjectList.get(0).getProjId());
            fbsFlat = fbsPayment.getFbsBooking().getFbsFlat();
            fbsApplicant = (FbsApplicant) em.createNamedQuery("FbsApplicant.findByFbsFlat&Status&ApplicantFlag").setParameter("fbsFlat", fbsFlat).setParameter("status", "booked").setParameter("applicantFlag", 1).getResultList().get(0);
            serviceTax = (fbsPayment.getPaidAmount() * fbsPayment.getServiceTax()) / 100;
            totalAmount = fbsPayment.getPaidAmount() - serviceTax;

            //for converting the rupees in stringLitreals
            int len, q = 0, r = 0;
            String ltr = " ";
            String Str = "Rupees";

            // int num =Integer.parseInt(fbsPayment.getPaidAmount().toString());
            int num = fbsPayment.getPaidAmount().intValue();
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
            }
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Document document = new Document(PageSize.A4, 0, 0, 60, 30);
                PdfWriter docWriter = PdfWriter.getInstance(document, baos);
                document.open();
                document.addTitle("Payment Receipt");
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
                //create the second inner table for receipt heading
                PdfPTable table2 = new PdfPTable(1);
                cell1 = new PdfPCell(new Paragraph("Payment Receipt", smallBold));
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell1.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell1);
                outerTable.addCell(table2);
                //create third table for receipt details
                PdfPTable table3 = new PdfPTable(2);
                table3.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
                paragraph = new Paragraph("");
                phrase1 = new Phrase("Receipt No:" + fbsPayment.getPaymentId(), smallBold);
                paragraph.add(phrase1);
                addEmptyLine(paragraph, 1);
                phrase1 = new Phrase("Customer Id:" + fbsPayment.getFbsBooking().getRegNumber(), smallBold);
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
                //create fourth table for applicant details
                PdfPTable table4 = new PdfPTable(1);
                table4.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
                paragraph = new Paragraph("");
                phrase1 = new Phrase("Received with thanks from ", blackFont);
                paragraph.add(phrase1);
                phrase1 = new Phrase("Mr. " + fbsApplicant.getApplicantName(), smallBold);
                paragraph.add(phrase1);
                phrase1 = new Phrase(" S/W/D/o " + fbsApplicant.getSWD() + " residing at " + fbsApplicant.getResAdd() + " a sum of Rs. ", blackFont);
                paragraph.add(phrase1);
                phrase1 = new Phrase(fbsPayment.getPaidAmount().toString() + "0/- (" + Str + " Only)", smallBold);
                paragraph.add(phrase1);
                phrase1 = new Phrase("against Unit No. ", blackFont);
                paragraph.add(phrase1);
                phrase1 = new Phrase(fbsPayment.getFbsBooking().getFbsFlat().getFlatNo() + " at Tower " + fbsPayment.getFbsBooking().getFbsFlat().getFbsFloor().getFbsBlock().getBlockId(), smallBold);
                paragraph.add(phrase1);
                phrase1 = new Phrase(" measuring ", blackFont);
                paragraph.add(phrase1);
                phrase1 = new Phrase(fbsFlat.getFbsFlatType().getFlatSba() + " SQ FT Super Area ", smallBold);
                paragraph.add(phrase1);
                phrase1 = new Phrase("(Approximately) in ", blackFont);
                phrase1 = new Phrase(fbsProject.getProjName() + ", " + fbsProject.getAddress() + ", " + fbsProject.getCity() + ", " + fbsProject.getState(), smallBold);
                paragraph.add(phrase1);
                phrase1 = new Phrase(" as per following details: ", blackFont);
                paragraph.add(phrase1);
                addEmptyLine(paragraph, 2);

                cell1 = new PdfPCell(paragraph);
                cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell1.setBorder(Rectangle.NO_BORDER);
                table4.addCell(cell1);

                if (fbsPayment.getPaymentMode().equals("Cheque")) {
                    PdfPTable chqDetailTable = new PdfPTable(7);
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
                    paragraph = new Paragraph();
                    paragraph.add(new Phrase("Service Tax", smallBold));
                    addEmptyLine(paragraph, 1);
                    paragraph.add(new Phrase("(Rs.)", smallBold));
                    PdfPCell chqCell6 = new PdfPCell(new Phrase(paragraph));
                    chqCell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                    paragraph = new Paragraph();
                    paragraph.add(new Phrase("Net Amount", smallBold));
                    addEmptyLine(paragraph, 1);
                    paragraph.add(new Phrase("(Rs.)", smallBold));
                    PdfPCell chqCell7 = new PdfPCell(new Phrase(paragraph));
                    chqCell7.setHorizontalAlignment(Element.ALIGN_CENTER);
                    chqDetailTable.addCell(chqCell1);
                    chqDetailTable.addCell(chqCell2);
                    chqDetailTable.addCell(chqCell3);
                    chqDetailTable.addCell(chqCell4);
                    chqDetailTable.addCell(chqCell5);
                    chqDetailTable.addCell(chqCell6);
                    chqDetailTable.addCell(chqCell7);

                    PdfPCell chqCell11 = new PdfPCell(new Phrase("1", smallBold));
                    chqCell11.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell chqCell22 = new PdfPCell(new Phrase(fbsPayment.getChequeNo(), blackFont));
                    chqCell22.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell chqCell33 = new PdfPCell(new Phrase(fbsPayment.getDrawnOn(), blackFont));
                    chqCell33.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell chqCell44 = new PdfPCell(new Phrase(dateFormat.format(fbsPayment.getPaymentDate()), blackFont));
                    chqCell44.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell chqCell55 = new PdfPCell(new Phrase(fbsPayment.getPaidAmount().toString() + "0/-", blackFont));
                    chqCell55.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell chqCell66 = new PdfPCell(new Phrase(String.valueOf(serviceTax) + "0/-", blackFont));
                    chqCell66.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell chqCell77 = new PdfPCell(new Phrase(String.valueOf(totalAmount) + "0/-", blackFont));
                    chqCell77.setHorizontalAlignment(Element.ALIGN_CENTER);
                    chqDetailTable.addCell(chqCell11);
                    chqDetailTable.addCell(chqCell22);
                    chqDetailTable.addCell(chqCell33);
                    chqDetailTable.addCell(chqCell44);
                    chqDetailTable.addCell(chqCell55);
                    chqDetailTable.addCell(chqCell66);
                    chqDetailTable.addCell(chqCell77);
                    table4.addCell(chqDetailTable);
                } else if (fbsPayment.getPaymentMode().equals("Cash")) {
                    PdfPTable cashDetailTable = new PdfPTable(5);
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
                    paragraph = new Paragraph();
                    paragraph.add(new Phrase("Service Tax", smallBold));
                    addEmptyLine(paragraph, 1);
                    paragraph.add(new Phrase("(Rs.)", smallBold));
                    PdfPCell cashCell4 = new PdfPCell(new Phrase(paragraph));
                    cashCell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                    paragraph = new Paragraph();
                    paragraph.add(new Phrase("Net Amount", smallBold));
                    addEmptyLine(paragraph, 1);
                    paragraph.add(new Phrase("(Rs.)", smallBold));
                    PdfPCell cashCell5 = new PdfPCell(new Phrase(paragraph));
                    cashCell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cashDetailTable.addCell(cashCell1);
                    cashDetailTable.addCell(cashCell2);
                    cashDetailTable.addCell(cashCell3);
                    cashDetailTable.addCell(cashCell4);
                    cashDetailTable.addCell(cashCell5);

                    PdfPCell cashCell11 = new PdfPCell(new Phrase("1", smallBold));
                    cashCell11.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell cashCell22 = new PdfPCell(new Phrase(dateFormat.format(fbsPayment.getPaymentDate()), blackFont));
                    cashCell22.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell cashCell33 = new PdfPCell(new Phrase(fbsPayment.getPaidAmount().toString() + "0/-", blackFont));
                    cashCell33.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell cashCell44 = new PdfPCell(new Phrase(String.valueOf(serviceTax) + "0/-", blackFont));
                    cashCell44.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell cashCell55 = new PdfPCell(new Phrase(String.valueOf(totalAmount) + "0/-", smallBold));
                    cashCell55.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cashDetailTable.addCell(cashCell11);
                    cashDetailTable.addCell(cashCell22);
                    cashDetailTable.addCell(cashCell33);
                    cashDetailTable.addCell(cashCell44);
                    cashDetailTable.addCell(cashCell55);
                    table4.addCell(cashDetailTable);
                } else if (fbsPayment.getPaymentMode().equals("RTGS/NEFT")) {
                    PdfPTable neftDetailTable = new PdfPTable(7);
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
                    paragraph = new Paragraph();
                    paragraph.add(new Phrase("Service Tax", smallBold));
                    addEmptyLine(paragraph, 1);
                    paragraph.add(new Phrase("(Rs.)", smallBold));
                    PdfPCell neftCell6 = new PdfPCell(new Phrase(paragraph));
                    neftCell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                    paragraph = new Paragraph();
                    paragraph.add(new Phrase("Net Amount", smallBold));
                    addEmptyLine(paragraph, 1);
                    paragraph.add(new Phrase("(Rs.)", smallBold));
                    PdfPCell neftCell7 = new PdfPCell(new Phrase(paragraph));
                    neftCell7.setHorizontalAlignment(Element.ALIGN_CENTER);
                    neftDetailTable.addCell(neftCell1);
                    neftDetailTable.addCell(neftCell2);
                    neftDetailTable.addCell(neftCell3);
                    neftDetailTable.addCell(neftCell4);
                    neftDetailTable.addCell(neftCell5);
                    neftDetailTable.addCell(neftCell6);
                    neftDetailTable.addCell(neftCell7);

                    PdfPCell neftcell11 = new PdfPCell(new Phrase("1", smallBold));
                    neftcell11.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell neftcell22 = new PdfPCell(new Phrase(fbsPayment.getTransactionId(), blackFont));
                    neftcell22.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell neftcell33 = new PdfPCell(new Phrase(fbsPayment.getDrawnOn(), blackFont));
                    neftcell33.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell neftcell44 = new PdfPCell(new Phrase(dateFormat.format(fbsPayment.getPaymentDate()), blackFont));
                    neftcell44.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell neftcell55 = new PdfPCell(new Phrase(fbsPayment.getPaidAmount().toString() + "/-", blackFont));
                    neftcell55.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell neftcell66 = new PdfPCell(new Phrase(String.valueOf(serviceTax) + "/-", blackFont));
                    neftcell66.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell neftcell77 = new PdfPCell(new Phrase(String.valueOf(totalAmount) + "/-", blackFont));
                    neftcell77.setHorizontalAlignment(Element.ALIGN_CENTER);
                    neftDetailTable.addCell(neftcell11);
                    neftDetailTable.addCell(neftcell22);
                    neftDetailTable.addCell(neftcell33);
                    neftDetailTable.addCell(neftcell44);
                    neftDetailTable.addCell(neftcell55);
                    neftDetailTable.addCell(neftcell66);
                    neftDetailTable.addCell(neftcell77);
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
                System.out.println("Exception in PaymentReceipt" + ex);
            } finally {
                //out.close();
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
    
}
