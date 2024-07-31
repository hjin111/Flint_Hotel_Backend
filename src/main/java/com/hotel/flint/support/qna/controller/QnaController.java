package com.hotel.flint.support.qna.controller;

import com.hotel.flint.common.dto.CommonErrorDto;
import com.hotel.flint.common.dto.CommonResDto;
import com.hotel.flint.support.qna.dto.CreateQnaDto;
import com.hotel.flint.support.qna.dto.QnaListDto;
import com.hotel.flint.support.qna.service.QnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mypage/qna")
public class QnaController {

    private final QnaService qnaService;

    @Autowired
    public QnaController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    /**
     * qna 등록
     */
    @PostMapping("/create")
    public ResponseEntity<?> createQnA(@RequestBody CreateQnaDto dto) {

        try {
            qnaService.createQnA(dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "qna 등록 성공", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * qna 목록 조회 - 마이페이지에서 본인것 조회
     */
    @GetMapping("/list")
    public Page<QnaListDto> qnaList(@PageableDefault(size=10, sort = "writeTime"
            , direction = Sort.Direction.DESC) Pageable pageable) {

        return qnaService.qnaList(pageable);
    }

//    /**
//     * qna 상세 조회
//     */
//    @GetMapping("/detail/{id}")
//    public ResponseEntity<?> detailQnA() {
//
//    }
}
