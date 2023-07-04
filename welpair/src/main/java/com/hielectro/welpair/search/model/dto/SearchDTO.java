package com.hielectro.welpair.search.model.dto;

<<<<<<< HEAD
import com.hielectro.welpair.inventory.model.dto.ProductDTO;
import com.hielectro.welpair.sellproduct.model.dto.SellItemPageDTO;
import com.hielectro.welpair.sellproduct.model.dto.SellPageDTO;
import com.hielectro.welpair.sellproduct.model.dto.SellProductDTO;
import com.hielectro.welpair.sellproduct.model.dto.ThumbnailImageDTO;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SearchDTO {

    private int minPrice;
    private int maxPrice;
    private long sellprice;

    private SellPageDTO sellPage;
    private List<ThumbnailImageDTO> thumbnailImageList;
    private SellItemPageDTO sellItemPage;
    private SellProductDTO sellProduct;
    private ProductDTO product;
}
