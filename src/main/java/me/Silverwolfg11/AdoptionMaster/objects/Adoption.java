package me.Silverwolfg11.AdoptionMaster.objects;

import java.util.ArrayList;
import java.util.UUID;

public class Adoption {

    private UUID partner1, partner2;

    private ArrayList<UUID> children = new ArrayList<>();

    public Adoption(UUID partner1, UUID parnter2) {
        this.partner1 = partner1;
        this.partner2 = parnter2;
    }

    public void addChild(UUID child) {
        children.add(child);
    }

    public void removeChild(UUID child) {
        if(children.contains(child))
            children.remove(child);
    }

    public boolean containsChild(UUID child) {
        return children.contains(child);

    }

    public boolean isParent(UUID parent) {
        return partner1.equals(parent) || partner2.equals(parent);

    }

    public UUID getPartner1() {
        return partner1;
    }

    public UUID getPartner2() {
        return partner2;
    }

    public ArrayList<UUID> getChildren() {
        return children;
    }

}
