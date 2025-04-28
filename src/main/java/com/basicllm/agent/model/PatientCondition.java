package com.basicllm.agent.model;

import lombok.Data;

/**
 * 病人病历
 */
@Data
public class PatientCondition {

    /**
     * 性别
     */
    private String gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 主诉
     */
    private String chiefComplaint;

    /**
     * 现病史
     * <p>现病史是记述患者病后的全过程，即发生、发展、演变和诊治经过。</p>
     */
    private String historyOfPresentIllness;

    /**
     * 既往史
     * <p>既往史既往史又称过去病史，即就医时医生向患者问询的患者既往的健康状况和过去曾经患过的疾病等方面的问题。
     * 既往健康状况包括饮食习惯等。</p>
     */
    private String pastHistory;

    /**
     * 个人史
     * <p>在疾病诊断中，个人史是指记录患者从出生到就诊期间与健康相关的重要经历和信息。</p>
     */
    private String personalHistory;

    /**
     * 过敏史
     */
    private String allergicHistory;

    /**
     * 生育史
     */
    private String reproductiveHistory;

    /**
     * 婚育史
     */
    private String pregnancyAndDelivery;

    /**
     * 流行病史
     */
    private String epidemicHistory;

    /**
     * 体格检查
     */
    private String physicalExamination;

    /**
     * 辅助检查
     */
    private String auxiliaryExamination;

    public String report() {
        StringBuilder report = new StringBuilder();
        report.append("性别：").append(str(gender)).append("\n");
        report.append("年龄：").append(str(age)).append("\n");
        report.append("主诉：").append(str(chiefComplaint)).append("\n");
        report.append("现病史：").append(str(historyOfPresentIllness)).append("\n");
        report.append("既往史：").append(str(pastHistory)).append("\n");
        report.append("个人史：").append(str(personalHistory)).append("\n");
        report.append("过敏史：").append(str(allergicHistory)).append("\n");
        report.append("生育史：").append(str(reproductiveHistory)).append("\n");
        report.append("婚育史：").append(str(pregnancyAndDelivery)).append("\n");
        report.append("流行病史：").append(str(epidemicHistory)).append("\n");
        report.append("体格检查：").append(str(physicalExamination)).append("\n");
        report.append("辅助检查：").append(str(auxiliaryExamination)).append("\n");
        return report.toString();
    }

    public String conciseReport() {
        StringBuilder report = new StringBuilder();

        addInfo(report,"性别：",gender);
        addInfo(report,"年龄：",age);
        addInfo(report,"主诉：",chiefComplaint);
        addInfo(report,"现病史：",historyOfPresentIllness);
        addInfo(report,"既往史：",pastHistory);
        addInfo(report,"个人史：",personalHistory);
        addInfo(report,"过敏史：",allergicHistory);
        addInfo(report,"生育史：",reproductiveHistory);
        addInfo(report,"婚育史：",pregnancyAndDelivery);
        addInfo(report,"流行病史：",epidemicHistory);
        addInfo(report,"体格检查：",physicalExamination);
        addInfo(report,"辅助检查：",auxiliaryExamination);

        return report.toString();
    }

    private String str(Object object) {
        if (object == null) {
            return "";
        }
        return object.toString();
    }

    private void addInfo(StringBuilder builder,String label,Object object) {
        if (object != null && !object.toString().trim().isEmpty()) {
            builder.append(label).append(object).append("\n");
        }
    }

}
