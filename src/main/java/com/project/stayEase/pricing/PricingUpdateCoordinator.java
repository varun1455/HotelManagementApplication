package com.project.stayEase.pricing;


import com.project.stayEase.entity.Hotel;
import com.project.stayEase.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PricingUpdateCoordinator {

    private final HotelRepository hotelRepository;
    private final PricingUpdateService pricingUpdateService;

    public void updatePrices(){
        int page = 0;
        int batchSize = 100;
        while(true){
            Page<Hotel> hotelsPage = hotelRepository.findAll(PageRequest.of(page, batchSize));
            if(hotelsPage.isEmpty()){
                break;
            }

            hotelsPage.getContent().forEach(
                    hotel ->
                    {
                       pricingUpdateService.updateRollingWindowPricing(hotel);
                       pricingUpdateService.updateUrgencyWindowPricing(hotel);
                    }
            );

            page++;

        }
    }
}
