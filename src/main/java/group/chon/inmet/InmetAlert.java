package group.chon.inmet;

import java.util.ArrayList;

public class InmetAlert {
    private Boolean perceived;
    private Integer id;
    private String title;
    private String link;
    private String category;
    private String event;
    private String responseType;
    private String urgency;
    private String severity;
    private String certainty;
    private String senderName;
    private String description;
    private String instruction;
    private String web;
    private String colorRisk;
    private Long timeStampDateOnSet;
    private Long timeStampDateExpires;
    private ArrayList<IBGEMunicipio> ibgeMunicipios = new ArrayList<>();
    public InmetAlert(Integer id, String title, String link) {
        this.perceived = false;
        this.id = id;
        this.title = title;
        this.link = link;
    }

    public Boolean getPerceived() {
        return perceived;
    }

    public void setPerceived(Boolean perceived) {
        this.perceived = perceived;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getCertainty() {
        return certainty;
    }

    public void setCertainty(String certainty) {
        this.certainty = certainty;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getColorRisk() {
        return colorRisk;
    }

    public void setColorRisk(String colorRisk) {
        this.colorRisk = colorRisk;
    }

    public Long getTimeStampDateOnSet() {
        return timeStampDateOnSet;
    }

    public void setTimeStampDateOnSet(Long timeStampDateOnSet) {
        this.timeStampDateOnSet = timeStampDateOnSet;
    }

    public Long getTimeStampDateExpires() {
        return timeStampDateExpires;
    }

    public void setTimeStampDateExpires(Long timeStampDateExpires) {
        this.timeStampDateExpires = timeStampDateExpires;
    }

    public ArrayList<IBGEMunicipio> getIbgeMunicipios() {
        return ibgeMunicipios;
    }

    public void setIbgeMunicipios(ArrayList<IBGEMunicipio> ibgeMunicipios) {
        this.ibgeMunicipios = ibgeMunicipios;
    }
}
