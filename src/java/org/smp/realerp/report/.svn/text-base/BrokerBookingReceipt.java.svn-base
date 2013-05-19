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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
@WebServlet(name = "BrokerBookingReceipt", urlPatterns = {"/brokerBookingReceipt"})
public class BrokerBookingReceipt extends HttpServlet {
    
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
    FbsProject fbsProject = new FbsProject();
    FbsPlanname fbsPlanname = new FbsPlanname();
    FbsFlatType fbsFlatType = new FbsFlatType();
    FbsDiscount fbsDiscount = new FbsDiscount();
    FbsBrokerCat fbsBrokerCat = new FbsBrokerCat();
    List<FbsProject> fbsProjectList = new ArrayList<FbsProject>();
    List<FbsBrPayment> fbsBrPaymentList = new ArrayList<FbsBrPayment>();
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
    double totalPaidAmount;
    double totalPendigAmount;
    double totalClearedAmount;
   
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        String registrationNo = request.getParameter("registrationNo");
        fbsCompany = fbsCompanyFacade.find(LoginBean.fbsLogin.getFbsCompany().getCompanyId());
        
//        HttpSession session = request.getSession();
//        List<FbsBooking> bookingList = (List<FbsBooking>) session.getAttribute("fbsBookingList");
//        System.out.println("size of bookingList is.............." + bookingList.size());
       
        if (registrationNo != null) {
            fbsBooking = fbsBookingFacade.find(Integer.parseInt(registrationNo));
            fbsBroker = fbsBooking.getFbsBroker();
            fbsProjectList = (List<FbsProject>) fbsCompany.getFbsProjectCollection();
            fbsProject = fbsProjectFacade.find(fbsProjectList.get(0).getProjId());
            fbsPlanname = fbsBooking.getFbsPlanname();
            fbsFlatType = fbsBooking.getFbsFlat().getFbsFlatType();
            fbsDiscount = fbsBooking.getFbsDiscount();
            fbsBrokerCat = fbsBooking.getFbsBroker().getFbsBrokerCat();
            commisson = brokerManager.calculateBrokerCommission(fbsFlatType, fbsPlanname, fbsDiscount, fbsBrokerCat);
            System.out.println("commisssion is..........." + commisson);
            payableAmount = brokerManager.calculateBrokerPayableAmount(fbsFlatType, fbsPlanname, fbsDiscount, fbsBrokerCat, fbsBooking);
            paidAmount = brokerManager.calculateBrokerPaidAmount(fbsBooking);
            fbsBrPaymentList.clear();
            fbsBrPaymentList = (List<FbsBrPayment>) fbsBooking.getFbsBrPaymentCollection();
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Document document = new Document(PageSize.A4, 0, 0, 60, 30);
                PdfWriter docWriter = PdfWriter.getInstance(document, baos);
                document.open();
                document.addTitle("BROKER BOOKING REPORT");
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
                cell1 = new PdfPCell(new Paragraph("BROKER BOOKING REPORT", smallBold));
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell1.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell1);
                paragraph = new Paragraph("Registration No:" + fbsBooking.getRegNumber(), smallBold);
                cell1 = new PdfPCell(paragraph);
                cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell1.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell1);
                addEmptyLine(paragraph, 1);
                paragraph = new Paragraph("Date: " + dateFormat.format(new Date()), smallBold);
                cell1 = new PdfPCell(paragraph);
                cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell1.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell1);
                outerTable.addCell(table2);

                // table3 for broker details heading
                PdfPTable table3 = new PdfPTable(1);
                cell1 = new PdfPCell(new Paragraph("BROKER DETAILS", smallBold));
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell1.setBorder(Rectangle.NO_BORDER);
                table3.addCell(cell1);
                outerTable.addCell(table3);

                // table4 for broker details
                float[] widths = {3.5f, 6.5f, 2.4f, 4.0f, 1.6f, 2.1f};
                PdfPTable brokerDetailtable = new PdfPTable(widths);
                brokerDetailtable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
                paragraph = new Paragraph();
                
                PdfPCell cell2 = new PdfPCell(new Phrase("Name", smallBold));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell cell3 = new PdfPCell(new Phrase("Address", smallBold));
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell cell4 = new PdfPCell(new Phrase("Mobile", smallBold));
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell cell5 = new PdfPCell(new Phrase("Email", smallBold));
                cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell cell6 = new PdfPCell(new Phrase("Category", smallBold));
                cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell cell7 = new PdfPCell(new Phrase("Commission", smallBold));
                cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
                
                brokerDetailtable.addCell(cell2);
                brokerDetailtable.addCell(cell3);
                brokerDetailtable.addCell(cell4);
                brokerDetailtable.addCell(cell5);
                brokerDetailtable.addCell(cell6);
                brokerDetailtable.addCell(cell7);
                
                PdfPCell cell22 = new PdfPCell(new Phrase(capitalizeFirst(fbsBroker.getBrName()), blackFont));
                cell22.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell cell33 = new PdfPCell(new Phrase(capitalizeFirst(fbsBroker.getBrAdd()), blackFont));
                cell33.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell cell44 = new PdfPCell(new Phrase(fbsBroker.getBrMobile().toString(), blackFont));
                cell44.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell cell55 = new PdfPCell(new Phrase(fbsBroker.getBrMail(), blackFont));
                cell55.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell cell66 = new PdfPCell(new Phrase(fbsBroker.getFbsBrokerCat().getCategoryName(), blackFont));
                cell66.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell cell77 = new PdfPCell(new Phrase(fbsBroker.getFbsBrokerCat().getCommission().toString() + "%", blackFont));
                cell77.setHorizontalAlignment(Element.ALIGN_CENTER);
                
                brokerDetailtable.addCell(cell22);
                brokerDetailtable.addCell(cell33);
                brokerDetailtable.addCell(cell44);
                brokerDetailtable.addCell(cell55);
                brokerDetailtable.addCell(cell66);
                brokerDetailtable.addCell(cell77);
                
                cell1 = new PdfPCell(paragraph);
                cell1.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                cell1.setBorder(Rectangle.NO_BORDER);
                brokerDetailtable.addCell(cell1);
                
                outerTable.addCell(brokerDetailtable);

                //sixth table for commission detail heading
                PdfPTable table6 = new PdfPTable(1);
                cell1 = new PdfPCell(new Paragraph("COMMISSION DETAILS", smallBold));
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell1.setBorder(Rectangle.NO_BORDER);
                table6.addCell(cell1);
                outerTable.addCell(table6);

                //seventh table for commission details
                float[] widths1 = {2.5f, 1.8f, 1.8f, 1.6f, 3.3f, 2.0f, 2.6f, 2.0f};
                PdfPTable unitDetails = new PdfPTable(widths1);
                unitDetails.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
                paragraph = new Paragraph();
                
                PdfPCell ucell1 = new PdfPCell(new Phrase("Project Name", smallBold));
                ucell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell ucell2 = new PdfPCell(new Phrase("Block Name", smallBold));
                ucell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell ucell3 = new PdfPCell(new Phrase("Floor No", smallBold));
                ucell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell ucell4 = new PdfPCell(new Phrase("Flat No", smallBold));
                ucell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell ucell5 = new PdfPCell(new Phrase("Flat Type", smallBold));
                ucell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell ucell6 = new PdfPCell(new Phrase("Commission(A)", smallBold));
                ucell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell ucell7 = new PdfPCell(new Phrase("Payable Amt.(B)", smallBold));
                ucell7.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell ucell8 = new PdfPCell(new Phrase("Paid Amt.(C)", smallBold));
                ucell8.setHorizontalAlignment(Element.ALIGN_CENTER);
                
                unitDetails.addCell(ucell1);
                unitDetails.addCell(ucell2);
                unitDetails.addCell(ucell3);
                unitDetails.addCell(ucell4);
                unitDetails.addCell(ucell5);
                unitDetails.addCell(ucell6);
                unitDetails.addCell(ucell7);
                unitDetails.addCell(ucell8);
                
                PdfPCell ucell11 = new PdfPCell(new Phrase(capitalizeFirst(fbsProject.getProjName()), blackFont));
                ucell11.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell ucell22 = new PdfPCell(new Phrase(fbsBooking.getFbsFlat().getFbsFloor().getFbsBlock().getBlockName(), blackFont));
                ucell22.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell ucell33 = new PdfPCell(new Phrase(capitalizeFirst(blockManager.convertFloorNo(fbsBooking.getFbsFlat().getFbsFloor().getFloorNo())), blackFont));
                ucell33.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell ucell44 = new PdfPCell(new Phrase(capitalizeFirst(fbsBooking.getFbsFlat().getFbsFloor().getFbsBlock().getBlockAbvr().concat("-").concat(fbsBooking.getFbsFlat().getFlatNo())), blackFont));
                ucell44.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell ucell55 = new PdfPCell(new Phrase(capitalizeFirst(fbsBooking.getFbsFlat().getFbsFlatType().getFlatSpecification()), blackFont));
                ucell55.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell ucell66 = new PdfPCell(new Phrase(String.valueOf(commisson), blackFont));
                ucell66.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell ucell77 = new PdfPCell(new Phrase(String.valueOf(payableAmount), blackFont));
                ucell77.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell ucell88 = new PdfPCell(new Phrase(String.valueOf(paidAmount), blackFont));
                ucell88.setHorizontalAlignment(Element.ALIGN_CENTER);
                
                unitDetails.addCell(ucell11);
                unitDetails.addCell(ucell22);
                unitDetails.addCell(ucell33);
                unitDetails.addCell(ucell44);
                unitDetails.addCell(ucell55);
                unitDetails.addCell(ucell66);
                unitDetails.addCell(ucell77);
                unitDetails.addCell(ucell88);
                
                PdfPCell payableout = new PdfPCell(new Phrase("  Payable Outstanding Amount (B-C)", smallBold));
                payableout.setColspan(7);
                payableout.setHorizontalAlignment(Element.ALIGN_RIGHT);
                PdfPCell pamount = new PdfPCell(new Phrase(decimalFormat.format(payableAmount - paidAmount), smallBold));
                pamount.setHorizontalAlignment(Element.ALIGN_RIGHT);
                
                unitDetails.addCell(payableout);
                unitDetails.addCell(pamount);
                
                PdfPCell paidOut = new PdfPCell(new Phrase("Total Outstanding Amount(A-C)", smallBold));
                paidOut.setColspan(7);
                paidOut.setHorizontalAlignment(Element.ALIGN_RIGHT);
                PdfPCell paidout = new PdfPCell(new Phrase(decimalFormat.format(commisson - paidAmount), smallBold));
                paidout.setHorizontalAlignment(Element.ALIGN_RIGHT);
                
                unitDetails.addCell(paidOut);
                unitDetails.addCell(paidout);
                
                outerTable.addCell(unitDetails);

                //sixth table for payment detail heading
                PdfPTable table7 = new PdfPTable(1);
                cell1 = new PdfPCell(new Paragraph("PAYMENT DETAILS", smallBold));
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell1.setBorder(Rectangle.NO_BORDER);
                table7.addCell(cell1);
                outerTable.addCell(table7);

                //table for payment details
                float[] widths2 = {3.0f, 2.0f, 1.8f, 3.2f, 1.6f, 1.6f, 2.0f};
                PdfPTable paymentDetailtable = new PdfPTable(widths2);
                paymentDetailtable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
                paragraph = new Paragraph();
                
                PdfPCell pcell1 = new PdfPCell(new Phrase("Receipt No", smallBold));
                pcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell pcell2 = new PdfPCell(new Phrase("Payment Date", smallBold));
                pcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell pcell7 = new PdfPCell(new Phrase("Payment Mode", smallBold));
                pcell7.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell pcell5 = new PdfPCell(new Phrase("Cheque No./Trans.Id", smallBold));
                pcell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell pcell6 = new PdfPCell(new Phrase("Cheque Date", smallBold));
                pcell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell pcell3 = new PdfPCell(new Phrase("Status", smallBold));
                pcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell pcell4 = new PdfPCell(new Phrase("Amount", smallBold));
                pcell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
                
                paymentDetailtable.addCell(pcell1);
                paymentDetailtable.addCell(pcell2);
                paymentDetailtable.addCell(pcell7);
                paymentDetailtable.addCell(pcell5);
                paymentDetailtable.addCell(pcell6);
                paymentDetailtable.addCell(pcell3);
                paymentDetailtable.addCell(pcell4);
                totalPaidAmount = 0;
                totalClearedAmount = 0;
                totalPendigAmount = 0;
                
                for (int i = 0; i < fbsBrPaymentList.size(); i++) {
                    totalPaidAmount += fbsBrPaymentList.get(i).getAmount();
                    System.out.println("totalPaidAmount is........." + totalPaidAmount);
                    if (fbsBrPaymentList.get(i).getStatus().equalsIgnoreCase("Cleared")) {
                        totalClearedAmount += fbsBrPaymentList.get(i).getAmount();
                        System.out.println("totalclearedAmount is........." + totalClearedAmount);
                    } else {
                        totalPendigAmount += fbsBrPaymentList.get(i).getAmount();
                        System.out.println("totalPendigAmount is........." + totalPendigAmount);
                    }
                    PdfPCell pcell11 = new PdfPCell(new Phrase(fbsBrPaymentList.get(i).getPaymentId().toString(), blackFont));
                    pcell11.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell pcell22 = new PdfPCell(new Phrase(dateFormat.format(fbsBrPaymentList.get(i).getPaymentDate()), blackFont));
                    pcell22.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell pcell77 = new PdfPCell(new Phrase(fbsBrPaymentList.get(i).getPaymentMode(), blackFont));
                    pcell77.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell pcell33 = new PdfPCell(new Phrase(fbsBrPaymentList.get(i).getStatus(), blackFont));
                    pcell33.setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell pcell44 = new PdfPCell(new Phrase(decimalFormat.format(fbsBrPaymentList.get(i).getAmount()), blackFont));
                    pcell44.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    paymentDetailtable.addCell(pcell11);
                    paymentDetailtable.addCell(pcell22);
                    paymentDetailtable.addCell(pcell77);
                    
                    if (fbsBrPaymentList.get(i).getPaymentMode().equals("Cash")) {
                        PdfPCell pcell55 = new PdfPCell(new Phrase("----", blackFont));
                        pcell55.setHorizontalAlignment(Element.ALIGN_CENTER);
                        PdfPCell pcell66 = new PdfPCell(new Phrase("----", blackFont));
                        pcell66.setHorizontalAlignment(Element.ALIGN_CENTER);
                        
                        paymentDetailtable.addCell(pcell55);
                        paymentDetailtable.addCell(pcell66);
                        
                    } else if (fbsBrPaymentList.get(i).getPaymentMode().equals("Cheque")) {
                        PdfPCell pcell55 = new PdfPCell(new Phrase(fbsBrPaymentList.get(i).getChequeNo().toString(), blackFont));
                        pcell55.setHorizontalAlignment(Element.ALIGN_CENTER);
                        PdfPCell pcell66 = new PdfPCell(new Phrase(dateFormat.format(fbsBrPaymentList.get(i).getChequeDate()), blackFont));
                        pcell66.setHorizontalAlignment(Element.ALIGN_CENTER);
                        
                        paymentDetailtable.addCell(pcell55);
                        paymentDetailtable.addCell(pcell66);
                        
                    } else {
                        PdfPCell pcell55 = new PdfPCell(new Phrase(fbsBrPaymentList.get(i).getTransactionId(), blackFont));
                        pcell55.setHorizontalAlignment(Element.ALIGN_CENTER);
                        PdfPCell pcell66 = new PdfPCell(new Phrase("----", blackFont));
                        pcell66.setHorizontalAlignment(Element.ALIGN_CENTER);
                        
                        paymentDetailtable.addCell(pcell55);
                        paymentDetailtable.addCell(pcell66);
                    }
                    paymentDetailtable.addCell(pcell33);
                    paymentDetailtable.addCell(pcell44);
                    
                }
                
                PdfPCell totalpayment = new PdfPCell(new Phrase(" Total Recieved Amount ", smallBold));
                totalpayment.setColspan(6);
                totalpayment.setHorizontalAlignment(Element.ALIGN_RIGHT);
                PdfPCell payamount = new PdfPCell(new Phrase(decimalFormat.format(totalPaidAmount), smallBold));
                payamount.setHorizontalAlignment(Element.ALIGN_RIGHT);
                
                paymentDetailtable.addCell(totalpayment);
                paymentDetailtable.addCell(payamount);
                
                PdfPCell totalclear = new PdfPCell(new Phrase(" Total Cleared Amount ", smallBold));
                totalclear.setColspan(6);
                totalclear.setHorizontalAlignment(Element.ALIGN_RIGHT);
                PdfPCell totalclearamt = new PdfPCell(new Phrase(decimalFormat.format(totalClearedAmount), smallBold));
                totalclearamt.setHorizontalAlignment(Element.ALIGN_RIGHT);
                
                paymentDetailtable.addCell(totalclear);
                paymentDetailtable.addCell(totalclearamt);
                
                PdfPCell totalpending = new PdfPCell(new Phrase(" Total Pending Amount ", smallBold));
                totalpending.setColspan(6);
                totalpending.setHorizontalAlignment(Element.ALIGN_RIGHT);
                PdfPCell pendingamount = new PdfPCell(new Phrase(decimalFormat.format(totalPendigAmount), smallBold));
                pendingamount.setHorizontalAlignment(Element.ALIGN_RIGHT);
                
                paymentDetailtable.addCell(totalpending);
                paymentDetailtable.addCell(pendingamount);
                
                outerTable.addCell(paymentDetailtable);


                //for footer details
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
                System.out.println("Exception in Master Boker Receipt" + ex);
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
    
    private static String capitalizeFirst(String s) {
        int j = 0;
        String str = null;
        ArrayList<Integer> indexList = new ArrayList();
        if (s.length() == 0) {
            return s;
        } else if (s.indexOf(" ") > 0) {
            
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == ' ') {
                    indexList.add(i);
                }
            }
            str = s.substring(0, 1).toUpperCase() + s.substring(1, indexList.get(0)).toLowerCase();
            for (j = 0; j < indexList.size() - 1; j++) {
                str = str + " " + s.substring((indexList.get(j) + 1), (indexList.get(j) + 2)).toUpperCase() + s.substring((indexList.get(j) + 2), (indexList.get(j + 1))).toLowerCase();
            }
            str = str + " " + s.substring((indexList.get(j) + 1), (indexList.get(j) + 2)).toUpperCase() + s.substring((indexList.get(j) + 2));
            
            return str;
        }
        
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
        
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
