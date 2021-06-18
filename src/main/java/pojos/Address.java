package pojos;

import lombok.Data;

@Data
public class Address {
    public String street;
    public String city;
    public String postalCode;
    public Coordinates coordinates;
}
