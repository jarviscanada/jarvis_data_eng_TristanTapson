package ca.jrvs.apps.jdbc.util;

public interface DataTransferObject {
    long getId();

    void setId(long id);

    public String getFirstName();

    public void setFirstName(String firstName);

    public String getLastName();

    public void setLastName(String lastName);

    public String getEmail();

    public void setEmail(String email);

    public String getPhone();

    public void setPhone(String phone);

    public String getAddress();

    public void setAddress(String address);

    public String getCity();

    public void setCity(String city);

    public String getState();

    public void setState(String state);

    public String getZipCode();

    public void setZipCode(String zipCode);
}
