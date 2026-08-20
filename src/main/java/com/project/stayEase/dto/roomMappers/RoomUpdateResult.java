package com.project.stayEase.dto.roomMappers;

public record RoomUpdateResult( boolean priceChanged,
                                boolean quantityChanged) {
    public boolean requiresPricingUpdate() {
        return priceChanged || quantityChanged;
    }
}
