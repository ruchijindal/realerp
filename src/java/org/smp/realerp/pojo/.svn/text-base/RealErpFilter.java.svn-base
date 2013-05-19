/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.pojo;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.smp.realerp.entity.FbsApplicant;
import org.smp.realerp.entity.FbsLogin;
import org.smp.realerp.managedbean.LoginBean;

/**
 *
 * @author smp
 */
public class RealErpFilter implements Filter {

    private static final boolean debug = true;
    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;
    public static final String PAYMNET = "Payment";
    public static final String ADD_PAYMENT = "Add Payment";
    public static final String VIEW_PAYMENT = "View Paymnet";
    public static final String AUTHORIZE_PAYMENT = "Authorize Payment";
    public static final String BOOKING = "Booking";
    public static final String ADD_BOOKING = "Add Booking";
    public static final String VIEW_BOOKING = "View Booking";
    public static final String AUTHORIZE_BOOKING = "Authorize Booking";
    public static final String BROKER = "Broker";
    public static final String ADD_BROKER_PAYMENT = "Add Broker Payment";
    public static final String VIEW_BROKER_PAYMENT = "View Broker Payment";
    public static final String AUTHORIZE_BROKER_PAYMENT = "Authorize Broker Payemnt";
    public static final String COMPANY_SETTING = "Company Setting";
    public static final String COMPLAIN = "Complain";
    public static final String ADD_COMPPLAIN = "Add Complain";
    public static final String VIEW_COMPLAIN = "View Complain";
    public static final String RESOLVE_COMPLAIN = "Resolve Complain";
    public static final String PROJECT = "Project";
    public static final String ADD_PROJECT = "Add Project";
    public static final String VIEW_PROJECT = "View Project";
    public static final String AUTHORIZE_PROJECT = "Authorize Project";

    public RealErpFilter() {
    }

    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("RealErpFilter:DoBeforeProcessing");
        }

        // Write code here to process the request and/or response before
        // the rest of the filter chain is invoked.

        // For example, a logging filter might log items on the request object,
        // such as the parameters.
	/*
         * for (Enumeration en = request.getParameterNames();
         * en.hasMoreElements(); ) { String name = (String)en.nextElement();
         * String values[] = request.getParameterValues(name); int n =
         * values.length; StringBuffer buf = new StringBuffer();
         * buf.append(name); buf.append("="); for(int i=0; i < n; i++) {
         * buf.append(values[i]); if (i < n-1) buf.append(","); }
         * log(buf.toString()); }
         */
    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("RealErpFilter:DoAfterProcessing");
        }

        // Write code here to process the request and/or response after
        // the rest of the filter chain is invoked.

        // For example, a logging filter might log the attributes on the
        // request object after the request has been processed. 
	/*
         * for (Enumeration en = request.getAttributeNames();
         * en.hasMoreElements(); ) { String name = (String)en.nextElement();
         * Object value = request.getAttribute(name); log("attribute: " + name +
         * "=" + value.toString());
         *
         * }
         */

        // For example, a filter might append something to the response.
	/*
         * PrintWriter respOut = new PrintWriter(response.getWriter());
         * respOut.println("<P><B>This has been appended by an intrusive
         * filter.</B>");
         */
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        System.out.println("this is filter");
        HttpSession session = httpServletRequest.getSession(false);
        if (session != null) {
            FbsLogin fbsLogin = new FbsLogin();
            fbsLogin = LoginBean.fbsLogin;
            System.out.println("++++++++ " + fbsLogin.getLoginId());
            httpServletResponse.setHeader("Cache-Control", "no-cache"); //Forces caches to obtain a new copy of the page from the origin server
            httpServletResponse.setHeader("Cache-Control", "no-store"); //Directs caches not to store the page under any circumstance
            httpServletResponse.setDateHeader("Expires", 0); //Causes the proxy cache to see the page as "stale"
            httpServletResponse.setHeader("Pragma", "no-cache");
            if (fbsLogin.getLoginId() == null) {
                System.out.println("inside if");
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/faces/index.xhtml");
            } else {
                System.out.println("ID IS " + fbsLogin.getUserId());
                System.out.println("    " + fbsLogin.getFbsUser().getRoleName());
                String xmldata = fbsLogin.getFbsUser().getXmlFile();
                System.out.println("path " + httpServletRequest.getRequestURI());
                String filterPath = "";
                try {
                    filterPath = httpServletRequest.getRequestURI().substring(httpServletRequest.getRequestURI().lastIndexOf("/") + 1, httpServletRequest.getRequestURI().lastIndexOf("."));
                } catch (StringIndexOutOfBoundsException e) {
                    filterPath = httpServletRequest.getRequestURI().substring(httpServletRequest.getRequestURI().lastIndexOf("/") + 1);

                }
                System.out.println("Filetr path is " + filterPath);
                if (filterPath.equals("bookingList")) {
                    if (xmldata.contains(VIEW_BOOKING) || xmldata.contains(ADD_BOOKING) || xmldata.contains(AUTHORIZE_BOOKING)) {
                        chain.doFilter(request, response);
                    }

                } else if (filterPath.equals("graphicalEnquiry")) {
                    if (xmldata.contains(VIEW_BOOKING) || xmldata.contains(ADD_BOOKING) || xmldata.contains(AUTHORIZE_BOOKING)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }
                    //httpServletResponse.sendRedirect("#");
                } else if (filterPath.equals("bookingDetail")) {
                    if (xmldata.contains(VIEW_BOOKING) || xmldata.contains(ADD_BOOKING) || xmldata.contains(AUTHORIZE_BOOKING)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }
                } else if (filterPath.equals("booking")) {
                    if (xmldata.contains(ADD_BOOKING) || xmldata.contains(AUTHORIZE_BOOKING)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }
                } else if (filterPath.equals("transferBooking")) {
                    if (xmldata.contains(ADD_BOOKING) || xmldata.contains(AUTHORIZE_BOOKING)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }
                } else if (filterPath.equals("searchPayment")) {
                    if (xmldata.contains(VIEW_PAYMENT) || xmldata.contains(ADD_PAYMENT) || xmldata.contains(AUTHORIZE_PAYMENT)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }
                } else if (filterPath.equals("quickPayment")) {
                    System.out.println("I am in quick Payment");
                    if (xmldata.contains(ADD_PAYMENT) || xmldata.contains(AUTHORIZE_PAYMENT)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }
                } else if (filterPath.equals("brokerPayment")) {
                    if (xmldata.contains(VIEW_BROKER_PAYMENT) || xmldata.contains(ADD_BROKER_PAYMENT) || xmldata.contains(AUTHORIZE_BROKER_PAYMENT)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }

                } else if (filterPath.equals("brokerWiseBooking")) {
                    if (xmldata.contains(ADD_PAYMENT) || xmldata.contains(AUTHORIZE_PAYMENT)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }
                } else if (filterPath.equals("setProject")) {
                    if (xmldata.contains(COMPANY_SETTING)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }
                } else if (filterPath.equals("bankSetting")) {
                    if (xmldata.contains(COMPANY_SETTING)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }
                } else if (filterPath.equals("setBroker")) {
                    if (xmldata.contains(COMPANY_SETTING)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }
                } else if (filterPath.equals("setBrokerCategory")) {
                    if (xmldata.contains(COMPANY_SETTING)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }
                } else if (filterPath.equals("discountSetting")) {
                    if (xmldata.contains(COMPANY_SETTING)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }
                } else if (filterPath.equals("interestSetting")) {
                    if (xmldata.contains(COMPANY_SETTING)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }
                } else if (filterPath.equals("serviceTaxSettings")) {
                    if (xmldata.contains(COMPANY_SETTING)) {
                        chain.doFilter(request, response);
                    }
                } else if (filterPath.equals("editCompanyInformation")) {
                    if (xmldata.contains(COMPANY_SETTING)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }
                } else if (filterPath.equals("setUser")) {
                    if (xmldata.contains(COMPANY_SETTING)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }
                } else if (filterPath.equals("setUserRole")) {
                    if (xmldata.contains(COMPANY_SETTING)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }
                } else if (filterPath.equals("complaintList")) {
                    if (xmldata.contains(VIEW_COMPLAIN) || xmldata.contains(ADD_COMPPLAIN) || xmldata.contains(RESOLVE_COMPLAIN)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }
                } else if (filterPath.equals("complaint")) {
                    if (xmldata.contains(ADD_COMPPLAIN) || xmldata.contains(RESOLVE_COMPLAIN)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }
                } else if (filterPath.equals("complaintDetails")) {
                    if (xmldata.contains(ADD_COMPPLAIN) || xmldata.contains(RESOLVE_COMPLAIN)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }
                } else if (filterPath.equals("blockSettings")) {
                    if (xmldata.contains(VIEW_PROJECT) || xmldata.contains(ADD_PROJECT) || xmldata.contains(AUTHORIZE_PROJECT)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }
                } else if (filterPath.equals("flatTypeSettings")) {
                    if (xmldata.contains(VIEW_PROJECT) || xmldata.contains(ADD_PROJECT) || xmldata.contains(AUTHORIZE_PROJECT)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }
                } else if (filterPath.equals("planNameSettings")) {
                    if (xmldata.contains(VIEW_PROJECT) || xmldata.contains(ADD_PROJECT) || xmldata.contains(AUTHORIZE_PROJECT)) {
                        chain.doFilter(request, response);
                    }
                } else if (filterPath.equals("payPlanSettings")) {
                    if (xmldata.contains(VIEW_PROJECT) || xmldata.contains(ADD_PROJECT) || xmldata.contains(AUTHORIZE_PROJECT)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }
                } else if (filterPath.equals("plcSettings")) {
                    if (xmldata.contains(VIEW_PROJECT) || xmldata.contains(ADD_PROJECT) || xmldata.contains(AUTHORIZE_PROJECT)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }
                } else if (filterPath.equals("chargesSettings")) {
                    if (xmldata.contains(VIEW_PROJECT) || xmldata.contains(ADD_PROJECT) || xmldata.contains(AUTHORIZE_PROJECT)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }
                } else if (filterPath.equals("parkingTypeSetting")) {
                    if (xmldata.contains(VIEW_PROJECT) || xmldata.contains(ADD_PROJECT) || xmldata.contains(AUTHORIZE_PROJECT)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }
                } else if (filterPath.equals("ConsumerReport")) {
                    //System.out.println("consumer report is true");
                    chain.doFilter(request, response);

                } else if (filterPath.equals("dueLetterReport")) {
                    chain.doFilter(request, response);

                } else if (filterPath.equals("DownloadFile")) {
                    chain.doFilter(request, response);
                } else if (filterPath.equals("collectionReport")) {

                    chain.doFilter(request, response);

                } else if (filterPath.equals("brokerBookingReceipt")) {
                    chain.doFilter(request, response);
                } else if (filterPath.equals("blockDetailList")) {
                    if (xmldata.contains(COMPANY_SETTING)) {
                        chain.doFilter(request, response);
                    } else {
                        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                    }
                } else {
                    System.out.println("Currently no one have this  page authorization ");
                    chain.doFilter(request, response);
                    // httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/faces/jsfpages/common/errorPage.xhtml");
                }
            }
        } else {
            System.out.println("Session is Null");
            httpServletResponse.setHeader("Cache-Control", "no-cache"); //Forces caches to obtain a new copy of the page from the origin server
            httpServletResponse.setHeader("Cache-Control", "no-store"); //Directs caches not to store the page under any circumstance
            httpServletResponse.setDateHeader("Expires", 0); //Causes the proxy cache to see the page as "stale"
            httpServletResponse.setHeader("Pragma", "no-cache");
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/faces/index.xhtml");
          
        }
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
                log("RealErpFilter:Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("RealErpFilter()");
        }
        StringBuffer sb = new StringBuffer("RealErpFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }
}
