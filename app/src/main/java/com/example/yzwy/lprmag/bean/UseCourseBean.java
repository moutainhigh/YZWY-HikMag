package com.example.yzwy.lprmag.bean;

/**
 *
 */
public class UseCourseBean {

    private String UseCourseName;//使用教程名称
    private String UseCourseKeyID;//使用教程号ID


    public UseCourseBean(String useCourseName, String useCourseKeyID) {
        UseCourseName = useCourseName;
        UseCourseKeyID = useCourseKeyID;
    }

    public String getUseCourseName() {
        return UseCourseName;
    }

    public void setUseCourseName(String useCourseName) {
        UseCourseName = useCourseName;
    }

    public String getUseCourseKeyID() {
        return UseCourseKeyID;
    }

    public void setUseCourseKeyID(String useCourseKeyID) {
        UseCourseKeyID = useCourseKeyID;
    }

    @Override
    public String toString() {
        return "UseCourseBean{" +
                "UseCourseName='" + UseCourseName + '\'' +
                ", UseCourseKeyID='" + UseCourseKeyID + '\'' +
                '}';
    }
}
