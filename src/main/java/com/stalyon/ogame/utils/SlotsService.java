package com.stalyon.ogame.utils;

import com.stalyon.ogame.OgameApiService;
import com.stalyon.ogame.dto.SlotsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SlotsService {

    @Autowired
    private OgameApiService ogameApiService;

    public Boolean hasEnoughFreeSlots(Integer freeSlotsToKeep) {
        SlotsDto slots = this.ogameApiService.getSlots();

        return slots.getInUse() + (slots.getExpTotal() - slots.getExpInUse()) + freeSlotsToKeep < slots.getTotal();
    }
}
