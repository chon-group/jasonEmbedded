package group.chon.inmet;

public class IBGEMunicipio {
    Integer ibgeCode;
    public IBGEMunicipio(Integer ibgeCode){
        this.ibgeCode = ibgeCode;
    }

    public String toString(){
        return this.ibgeCode.toString();
    }

    public Integer getIbgeCode() {
        return ibgeCode;
    }
}
