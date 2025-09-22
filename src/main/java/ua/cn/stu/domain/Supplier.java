package ua.cn.stu.domain;

public class Supplier {
    private Long supplierId;
    private String supplierName;
    private String supplierContact;
    private String supplierAddress;

    public Supplier() {}

    public Supplier(String supplierName, String supplierContact, String supplierAddress) {
        this.supplierName = supplierName;
        this.supplierContact = supplierContact;
        this.supplierAddress = supplierAddress;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierContact() {
        return supplierContact;
    }

    public void setSupplierContact(String supplierContact) {
        this.supplierContact = supplierContact;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    @Override
    public String toString() {
        return "ID: " + supplierId + " | " + supplierName + " | " + supplierContact;
    }
}
