package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.util.DataTransferObject;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Order implements DataTransferObject {

    private long id;
    private String customerFirstName;
    private String customerLastName;
    private String customerEmail;
    private Date creationDate;
    private BigDecimal totalDue;
    private String status;
    private String salespersonFirstName;
    private String salespersonLastName;
    private String salespersonEmail;
    private List<OrderLine> orderLines;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getFirstName() {
        return null;
    }

    @Override
    public void setFirstName(String firstName) {

    }

    @Override
    public String getLastName() {
        return null;
    }

    @Override
    public void setLastName(String lastName) {

    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public void setEmail(String email) {

    }

    @Override
    public String getPhone() {
        return null;
    }

    @Override
    public void setPhone(String phone) {

    }

    @Override
    public String getAddress() {
        return null;
    }

    @Override
    public void setAddress(String address) {

    }

    @Override
    public String getCity() {
        return null;
    }

    @Override
    public void setCity(String city) {

    }

    @Override
    public String getState() {
        return null;
    }

    @Override
    public void setState(String state) {

    }

    @Override
    public String getZipCode() {
        return null;
    }

    @Override
    public void setZipCode(String zipCode) {

    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public BigDecimal getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(BigDecimal totalDue) {
        this.totalDue = totalDue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSalespersonFirstName() {
        return salespersonFirstName;
    }

    public void setSalespersonFirstName(String salespersonFirstName) {
        this.salespersonFirstName = salespersonFirstName;
    }

    public String getSalespersonLastName() {
        return salespersonLastName;
    }

    public void setSalespersonLastName(String salespersonLastName) {
        this.salespersonLastName = salespersonLastName;
    }

    public String getSalespersonEmail() {
        return salespersonEmail;
    }

    public void setSalespersonEmail(String salespersonEmail) {
        this.salespersonEmail = salespersonEmail;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    // NOTE: toString method reformatted for README screenshot...
    @Override
    public String toString() {
        return "Order (id=" + id + "){\n" +
                "customerFirstName='" + customerFirstName + '\'' + "\n" +
                "customerLastName='" + customerLastName + '\'' + "\n" +
                "customerEmail='" + customerEmail + '\'' + "\n" +
                "creationDate=" + creationDate + "\n" +
                "totalDue=" + totalDue + "\n" +
                "status='" + status + '\'' + "\n" +
                "salespersonFirstName='" + salespersonFirstName + '\'' + "\n" +
                "salespersonLastName='" + salespersonLastName + '\'' + "\n" +
                "salespersonEmail='" + salespersonEmail + '\'' + "\n" +
                "orderLines=" + orderLines.toString() + "\n" +
                '}';
    }
}