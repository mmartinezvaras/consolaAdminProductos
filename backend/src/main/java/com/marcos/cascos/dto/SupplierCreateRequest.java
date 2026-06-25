package com.marcos.cascos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public class SupplierCreateRequest {
    @NotBlank
    @Size(max = 255)
    private String name;

    @URL
    @Size(max = 255)
    private String website;

    @URL
    @Size(max = 500)
    private String purchaseUrl;

    @Size(max = 255)
    private String contactName;

    @Size(max = 50)
    private String phone;

    @Email
    @Size(max = 255)
    private String email;

    @Size(max = 2000)
    private String address;

    @Size(max = 2000)
    private String notes;

    private Boolean active;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    public String getPurchaseUrl() { return purchaseUrl; }
    public void setPurchaseUrl(String purchaseUrl) { this.purchaseUrl = purchaseUrl; }
    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
