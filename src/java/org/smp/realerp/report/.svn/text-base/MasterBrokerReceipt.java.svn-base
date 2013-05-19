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
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import org.smp.realerp.entity.*;
import org.smp.realerp.managedbean.LoginBean;
import org.smp.realerp.manager.BlockManager;
import org.smp.realerp.manager.BrokerManager;
import org.smp.realerp.session.FbsBookingFacade;
import org.smp.realerp.session.FbsBrokerFacade;
import org.smp.realerp.session.FbsCompanyFacade;
import org.smp.realerp.session.FbsProjectFacade;

/**
 *
 * @author smp
 */
@WebServlet(name = "MasterBrokerReceipt", urlPatterns = {"/masterBrokerReceipt"})
public class MasterBrokerReceipt extends HttpServlet {

    @PersistenceContext(unitName = "RealErpPU")
    EntityManager em;
    @EJB
    BrokerManager brokerManager;
    @EJB
    BlockManager blockManager;
    @EJB
    FbsCompanyFacade fbsCompanyFacade;
    @EJB
    FbsProjectFacade fbsProjectFacade;
    @EJB
    FbsBrokerFacade fbsBrokerFacade;
    @EJB
    FbsBookingFacade fbsBookingFacade;
    FbsBooking fbsBooking = new FbsBooking();
    FbsBroker fbsBroker = new FbsBroker();
    FbsCompany fbsCompany = new FbsCompany();
    FbsPlanname fbsPlanname = new FbsPlanname();
    FbsFlatType fbsFlatType = new FbsFlatType();
    FbsDiscount fbsDiscount = new FbsDiscount();
    FbsBrokerCat fbsBrokerCat = new FbsBrokerCat();
    Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    Font blackFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL, BaseColor.BLACK);
    Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
    Font blackSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 6, Font.NORMAL, BaseColor.BLACK);
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    double commisson;
    double payableAmount;
    double paidAmount;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        fbsCompany = fbsCompanyFacade.find(LoginBean.fbsLogin.getFbsCompany().getCompanyId());
        HttpSession session = request.getSession();
        List<FbsBooking> bookingList = (List<FbsBooking>) session.getAttribute("fbsBookingList");
        // System.out.println("size of bookingList is.............." + bookingList.size());
        if (bookingList.size() > 0) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Document document = new Document(PageSize.A4, 0, 0, 60, 30);
                PdfWriter docWriter = PdfWriter.getInstance(document, baos);
                document.open();
                document.addTitle("BROKER COMMISSION REPORT");
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

                //second table for broker commission receipt heading
                PdfPTable table2 = new PdfPTable(1);
                cell1 = new PdfPCell(new Paragraph("BROKER COMMISSION REPORT", smallBold));
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell1.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell1);
                outerTable.addCell(table2);

                //table for details
                float[] widths2 = {2.0f, 2.4f, 2.4f, 2.0f, 2.0f, 2.4f, 1.6f, 2.4f, 2.4f, 2.0f};
                PdfPTable brokerDetailtable = new PdfPTable(widths2);
                brokerDetailtable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
                paragraph = new Paragraph();

                PdfPCell pcell1 = new PdfPCell(new Phrase("Reg No.", smallBold));
                pcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell pcell2 = new PdfPCell(new Phrase("Broker Name", smallBold));
                pcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell pcell3 = new PdfPCell(new Phrase("Booking date", smallBold));
                pcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell pcell4 = new PdfPCell(new Phrase("Project", smallBold));
                pcell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell pcell5 = new PdfPCell(new Phrase("Block", smallBold));
                pcell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell pcell6 = new PdfPCell(new Phrase("FloorNo", smallBold));
                pcell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell pcell7 = new PdfPCell(new Phrase("FlatNo", smallBold));
                pcell7.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell pcell8 = new PdfPCell(new Phrase("Commission", smallBold));
                pcell8.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell pcell9 = new PdfPCell(new Phrase("Payable amt.", smallBold));
                pcell9.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell pcell10 = new PdfPCell(new Phrase("Paid amt.", smallBold));
                pcell10.setHorizontalAlignment(Element.ALIGN_CENTER);

                brokerDetailtable.addCell(pcell1);
                brokerDetailtable.addCell(pcell2);
                brokerDetailtable.addCell(pcell3);
                brokerDetailtable.addCell(pcell4);
                brokerDetailtable.addCell(pcell5);
                brokerDetailtable.addCell(pcell6);
                brokerDetailtable.addCell(pcell7);
                brokerDetailtable.addCell(pcell8);
                brokerDetailtable.addCell(pcell9);
                brokerDetailtable.addCell(pcell10);

                for (int i = 0; i < bookingList.size(); i++) {
                    fbsFlatType = bookingList.get(i).getFbsFlat().getFbsFlatType();
                    fbsPlanname = bookingList.get(i).getFbsPlanname();
                    fbsDiscount = bookingList.get(i).getFbsDiscount();
                    fbsBrokerCat = bookingList.get(i).getFbsBroker().getFbsBrokerCat();
                    fbsBooking = bookingList.get(i);
                    commisson = brokerManager.calculateBrokerCommission(fbsFlatType, fbsPlanname, fbsDiscount, fbsBrokerCat);
                    System.out.println("commission is........." + commisson);
                    payableAmount = brokerManager.calculateBrokerPayableAmount(fbsFlatType, fbsPlanname, fbsDiscount, fbsBrokerCat, fbsBooking);
                    paidAmount = brokerManager.calculateBrokerPaidAmount(fbsBooking);
                    PdfPCell pcell11 = new PdfPCell(new Phrase(bookingList.get(i).getRegNumber().toString(), blackFont));
                    pcell11.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell pcell22 = new PdfPCell(new Phrase(bookingList.get(i).getFbsBroker().getBrName(), blackFont));
                    pcell22.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell pcell33 = new PdfPCell(new Phrase(dateFormat.format(bookingList.get(i).getBookingDt()), blackFont));
                    pcell33.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell pcell44 = new PdfPCell(new Phrase(bookingList.get(i).getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getProjName(), blackFont));
                    pcell44.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell pcell55 = new PdfPCell(new Phrase(bookingList.get(i).getFbsFlat().getFbsFloor().getFbsBlock().getBlockName(), blackFont));
                    pcell55.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell pcell66 = new PdfPCell(new Phrase(blockManager.convertFloorNo(bookingList.get(i).getFbsFlat().getFbsFloor().getFloorNo()), blackFont));
                    pcell66.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell pcell77 = new PdfPCell(new Phrase(bookingList.get(i).getFbsFlat().getFbsFloor().getFbsBlock().getBlockAbvr().concat("-").concat(bookingList.get(i).getFbsFlat().getFlatNo()), blackFont));
                    pcell77.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell pcell88 = new PdfPCell(new Phrase(String.valueOf(commisson), blackFont));
                    pcell88.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell pcell99 = new PdfPCell(new Phrase(String.valueOf(payableAmount), blackFont));
                    pcell99.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell pcell00 = new PdfPCell(new Phrase(String.valueOf(paidAmount), blackFont));
                    pcell00.setHorizontalAlignment(Element.ALIGN_CENTER);

                    brokerDetailtable.addCell(pcell11);
                    brokerDetailtable.addCell(pcell22);
                    brokerDetailtable.addCell(pcell33);
                    brokerDetailtable.addCell(pcell44);
                    brokerDetailtable.addCell(pcell55);
                    brokerDetailtable.addCell(pcell66);
                    brokerDetailtable.addCell(pcell77);
                    brokerDetailtable.addCell(pcell88);
                    brokerDetailtable.addCell(pcell99);
                    brokerDetailtable.addCell(pcell00);
                }


                outerTable.addCell(brokerDetailtable);

                //table for footer information
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
                System.out.println("Exception in Master Broker Receipt" + ex);
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
