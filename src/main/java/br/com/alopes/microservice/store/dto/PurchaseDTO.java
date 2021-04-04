package br.com.alopes.microservice.store.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class PurchaseDTO {

    @JsonIgnore
    private Long purchaseId;

    private List<OrderItemDTO> items;

    private AddressDTO address;

    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

}
