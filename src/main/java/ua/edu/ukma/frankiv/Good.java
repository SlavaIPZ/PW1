package ua.edu.ukma.frankiv;

import java.util.Objects;

public class Good {
    private int id;
    private String name;
    private int quantity;

    public Good() {

    }

    public Good(int id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }



        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Good good = (Good) obj;
            return id == good.id && quantity == good.quantity && Objects.equals(name, good.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, quantity);
        }

}