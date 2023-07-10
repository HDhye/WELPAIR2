package com.hielectro.welpair.sellproduct.model.dao;

import java.util.List;
import java.util.Map;

import com.hielectro.welpair.inventory.model.dto.CategoryDTO;
import com.hielectro.welpair.inventory.model.dto.ProductDTO;
import com.hielectro.welpair.sellproduct.model.dto.*;
import org.apache.ibatis.annotations.Mapper;

import com.hielectro.welpair.board.model.dto.QnAManagerDTO;
import com.hielectro.welpair.board.model.dto.ReviewManagerDTO;

@Mapper
public interface SellProductMapper {
    // SELECT
    int sellProductSearchCount(Map<String, String> search);
    List<SellProductDetailDTO> selectProductList(Map<String, String> sellProductCode);
    SellProductDTO selectOneSellProduct(String pageNo);
    List<ReviewManagerDTO> selectReviewList(Map<String, Object> searchMap);
    int reviewSearchCount(Map<String, Object> searchMap);
    int qnaSearchCount(Map<String, Object> searchMap);
    List<QnAManagerDTO> selectQnAList(Map<String, Object> searchMap);
    List<ProductDTO> selectOptionList(ProductDTO product);
    List<ProductDTO> selectProductNameList(ProductDTO product);
    List<CategoryDTO> selectCategoryList();
    List<ProductDTO> selectProductStatus();


    // INSERT
    int insertSellProduct(SellProductDTO sellProduct);
    int insertSellPage(SellPageDTO sellPage);
    int insertSellItemPage(SellItemPageDTO sellItemPage);
    int insertThumbnail(ThumbnailImageDTO thumbnail);

    // DELETE
    int sellProductDelete(String id);
    int deleteThumbnail(String no);

    // UPDATE
    int updateSellPage(SellPageDTO sellPage);

    int updateSellProduct(SellProductDTO sellProduct);

}

