package group.chon.inmet;

import java.util.ArrayList;
import java.util.Objects;

public class InmetArrayAlerts {
    private ArrayList<InmetAlert> alertArray = new ArrayList<>();
    private Boolean hasNewItem = false;

    public Boolean getHasNewItem() {
        return hasNewItem;
    }

    public void setHasNewItem(Boolean hasNewItem) {
        this.hasNewItem = hasNewItem;
    }

    public boolean addItem(InmetAlert alert) {
        if (!alertExists(alert.getId())) {
            alertArray.add(alert);
            setHasNewItem(true);
            return true;
        }else{
            System.out.print(".");
            return false;
        }
    }
    public InmetAlert getLastUnperceivedAlert(){
        for (int i=0; i<alertArray.size(); i++){
            if(!alertArray.get(i).getPerceived()){
                alertArray.get(i).setPerceived(true);
                if(i==alertArray.size()-1){
                    setHasNewItem(false);
                }
                return alertArray.get(i);
            }
        }
        return null;
    }

    public boolean alertExists(Integer id) {
        for (InmetAlert item : alertArray) {
            if (Objects.equals(item.getId(), id)) {
                return true;
            }
        }
        return false;
    }



}
