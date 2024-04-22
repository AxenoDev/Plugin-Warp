package fr.axeno.warp.config;

import org.bukkit.event.inventory.InventoryType;

public class MenuEnum {
    public enum Menu {
        LIST("Listes de warp", Slots.F.slots, InventoryType.CHEST),
        WARP_INFO("No Title", Slots.C.slots, InventoryType.CHEST);

        private final String title;
        private final int slots;
        private final InventoryType type;
        
        Menu(String title, int slots, InventoryType type) {
            this.title = title;
            this.slots = slots;
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public int getSlots() {
            return slots;
        }

        public InventoryType getType() {
            return type;
        }
    }

    public enum Slots {

        A(9),
        B(18),
        C(27),
        D(36),
        E(45),
        F(54);

        private final int slots;
        Slots(int slots) {
            this.slots = slots;
        }

        public int getSlots() {
            return slots;
        }
    }

}
