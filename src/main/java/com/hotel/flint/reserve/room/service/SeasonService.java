package com.hotel.flint.reserve.room.service;

import com.hotel.flint.reserve.room.domain.Season;
import com.hotel.flint.reserve.room.repository.SeasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.ranges.Range;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;

@Service
public class SeasonService {

    private final SeasonRepository seasonRepository;
    @Autowired
    public SeasonService (SeasonRepository seasonRepository) {
        this.seasonRepository = seasonRepository;
    }

    private List<Season> peakSeasons;

    @PostConstruct // 생성자 주입 이후 실행됨
    public void init() {
        peakSeasons = seasonRepository.findAll();
    }

    public boolean isSeason(LocalDate date) {
        for (Season season: peakSeasons) {
            // 성수기 내의 날짜면
            if (!date.isBefore(season.getStartDate()) && !date.isAfter(season.getEndDate())) {
                return true;
            }
        }
        return false;
    }


}
