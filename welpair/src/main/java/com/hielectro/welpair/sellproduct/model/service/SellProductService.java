package com.hielectro.welpair.sellproduct.model.service;

import java.util.List;
import java.util.Map;

import com.hielectro.welpair.board.model.dto.QnAManagerDTO;
import com.hielectro.welpair.board.model.dto.ReviewManagerDTO;
import com.hielectro.welpair.sellproduct.model.dto.SellProductDTO;
import com.hielectro.welpair.sellproduct.model.dto.SellProductDetailDTO;
import org.springframework.transaction.annotation.Transactional;

public interface SellProductService {
    public int sellProductSearchCount(Map<String, String> search);
    public List<SellProductDetailDTO> selectProductList(Map<String, String> productId);
    List<ReviewManagerDTO> selectReviewList(Map<String, Object> searchMap);

    @Transactional(rollbackFor = {Exception.class})
    int sellProductDelete(List<String> request) throws Exception;

    int reviewSearchCount(Map<String, Object> searchMap);

    List<QnAManagerDTO> selectQnAList(Map<String, Object> searchMap);

    int qnaSearchCount(Map<String, Object> searchMap);
}
