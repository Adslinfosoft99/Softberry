package com.adslinfosoft.softberry.API;

public class API {

    private static final String base_URL = "http://app.softberry.co.in/";


    public static final String LOGIN_URL = base_URL + "token";
    public static final String CHANGE_PASSWORD = base_URL + "api/account/ChangePassword";
    public static final String FORGOT_PAASWORD = base_URL + "api/account/ForgotPassword";
    public static final String GET_JOB_LIST = base_URL + "api/account/GetCustomerJobs";
    public static final String UPDATE_MEMBER_LOGIN = base_URL + "api/account/UpdateMemberLogin";
    public static final String GET_JOB_DETAILS = base_URL + "api/account/GetCustomerJobDetails";
    public static final String GET_PRODUCTS = base_URL + "api/product/GetProducts";
    public static final String JOB_ENQUIRY = base_URL + "api/account/SubmitJobEnquiry";
    public static final String GET_QUOTATION = "http://softberry.co.in/Admin/PrintQuotationInApp.aspx?Mode=Prnt&Cstr=";
    public static final String GET_CHALLAN = "http://softberry.co.in/Admin/PrintChallanInApp.aspx?Mode=Prnt&Cstr=";
    public static final String GET_INVOICE = "http://softberry.co.in/Admin/PrintInvoiceInApp.aspx?Mode=Prnt&Cstr=";
    public static final String GET_JOB_CHATS = base_URL + "api/account/GetChatMessages";
    public static final String SEND_MESSAGE = base_URL + "api/account/SendChatMessage";
    public static final String GET_ENQUIRY_LIST = base_URL + "api/account/GetJobsEnquiry";
    public static final String GET_ABOUT_US = base_URL + "api/account/GetAboutUs";
    public static final String GET_JOB_NUMBERS = base_URL + "api/account/GetClientJobNos";
    public static final String POST_COMPLAINT = base_URL + "api/account/JobComplaints";
    public static final String GET_COMPLAINT_LIST = base_URL + "api/account/GetJobComplaints";

}
