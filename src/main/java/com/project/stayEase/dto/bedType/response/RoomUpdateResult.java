package com.project.stayEase.dto.bedType.response;

public record RoomUpdateResult( boolean priceChanged,
                                boolean quantityChanged) {
    public boolean requiresPricingUpdate() {
        return priceChanged || quantityChanged;
    }
}
