package com.hotel.flint.admin.service;

import com.hotel.flint.admin.domain.Dining;
import com.hotel.flint.admin.domain.Menu;
import com.hotel.flint.admin.dto.MenuSaveDto;
import com.hotel.flint.admin.repository.DiningRepository;
import com.hotel.flint.admin.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class DiningService {
    private final DiningRepository diningRepository;
    private final MenuRepository menuRepository;

    public DiningService(DiningRepository diningRepository, MenuRepository menuRepository){
        this.diningRepository = diningRepository;
        this.menuRepository = menuRepository;
    }

    public void addDiningMenu(MenuSaveDto menuSaveDto){
        Dining dining = diningRepository.findById(menuSaveDto.getDiningId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 Dining ID"));

        Menu menu = menuSaveDto.toEntity(dining);
        menuRepository.save(menu);
    }

    public void modDiningMenu(Long id, int newCost){
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 메뉴"));
        menu.menuUpdate(newCost);
    }
}
