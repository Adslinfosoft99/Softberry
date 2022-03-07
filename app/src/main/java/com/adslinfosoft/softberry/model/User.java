package com.adslinfosoft.softberry.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shreshtha on 30/05/16.
 */
public class User implements Serializable {
    private int Id;
    private String mName;
    private String mAdd;
    private String Capacity;
    private String Company;

    private String Email;
    private String Mobile;
    private String Phone;
    private String DataList;
    private String DetailProblem;

    private String EstimateReport;

    private String FileSystem;
    private String FileType;
    private String NoOfFiles;
    private String NoOfPart;
    private String OpenPermited;
    private String Os;
    private String OsType;

    private ArrayList<Job> Jobs;
    private ArrayList<Job> CompleteJobs;

//    public User() {}
//
//    public static final Creator<User> CREATOR = new ClassLoaderCreator<User>() {
//        @Override
//        public User createFromParcel(Parcel source, ClassLoader loader) {
//            return new User();
//        }
//
//        @Override
//        public User createFromParcel(Parcel source) {
//            return null;
//        }
//
//        @Override
//        public User[] newArray(int size) {
//            return new User[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//
//    }



    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getAdd() {
        return mAdd;
    }

    public void setAdd(String mAdd) {
        this.mAdd = mAdd;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCapacity() {
        return Capacity;
    }

    public void setCapacity(String capacity) {
        Capacity = capacity;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }


    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getDataList() {
        return DataList;
    }

    public void setDataList(String dataList) {
        DataList = dataList;
    }

    public String getDetailProblem() {
        return DetailProblem;
    }

    public void setDetailProblem(String detailProblem) {
        DetailProblem = detailProblem;
    }

    public String getFileSystem() {
        return FileSystem;
    }

    public void setFileSystem(String fileSystem) {
        FileSystem = fileSystem;
    }

    public String getEstimateReport() {
        return EstimateReport;
    }

    public void setEstimateReport(String estimateReport) {
        EstimateReport = estimateReport;
    }

    public String getFileType() {
        return FileType;
    }

    public void setFileType(String fileType) {
        FileType = fileType;
    }

    public String getNoOfFiles() {
        return NoOfFiles;
    }

    public void setNoOfFiles(String noOfFiles) {
        NoOfFiles = noOfFiles;
    }

    public String getNoOfPart() {
        return NoOfPart;
    }

    public void setNoOfPart(String noOfPart) {
        NoOfPart = noOfPart;
    }

    public String getOpenPermited() {
        return OpenPermited;
    }

    public void setOpenPermited(String openPermited) {
        OpenPermited = openPermited;
    }

    public String getOsType() {
        return OsType;
    }

    public void setOsType(String osType) {
        OsType = osType;
    }

    public String getOs() {
        return Os;
    }

    public void setOs(String os) {
        Os = os;
    }

    public ArrayList<Job> getJobs() {
        return Jobs;
    }

    public void setJobs(ArrayList<Job> jobs) {
        Jobs = jobs;
    }

    public ArrayList<Job> getCompleteJobs() {
        return CompleteJobs;
    }

    public void setCompleteJobs(ArrayList<Job> jobs) {
        CompleteJobs = jobs;
    }


}
