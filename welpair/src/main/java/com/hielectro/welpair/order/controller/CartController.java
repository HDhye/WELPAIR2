package com.hielectro.welpair.order.controller;


import com.hielectro.welpair.order.model.dto.CartDTO;
import com.hielectro.welpair.order.model.dto.CartGeneralDTO;
import com.hielectro.welpair.order.model.dto.CartSellProductDTO;
import com.hielectro.welpair.order.model.service.CartService;
import com.hielectro.welpair.sellproduct.model.dto.SellProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Controller
@RequestMapping({"/order"})
public class CartController {

    private final CartService cartService;


    private CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // default 매핑 메소드
//    @GetMapping({"{id}"})
//    public String defaultLocation(@PathVariable("id") String url) {
//        return "/consumer/order/" + url;
//    }

    @GetMapping("/cart/add")
    public String addCart() {
        return "/consumer/order/add";
    }


    // 카트 인서트용 메소드
    @ResponseBody
    @PostMapping(value = "/cart/add", produces = "application/json; charset=utf-8")
    public Map<String, String> addCart(@ModelAttribute CartSellProductDTO cartSellProduct,
                                       @RequestParam("empNo") String empNo) {
        // 카트별판매상품dto를 통해 매상품id와 수량 정보와, 회원정보ID가 넘어온다.
        System.out.println("선택상품 : " + cartSellProduct);

        // 결과 메세지 전달 map 객체
        Map<String, String> resultMap = new HashMap<>();

        // 판매상품 조회 메소드 결과객체
        SellProductDTO sellProduct =
                cartService.isSellProductById(cartSellProduct.getSellProductId());

        // 1. 정상 수량인지 체크(프론트에서도 검증)
        if (cartSellProduct.getCartAmount() < 1) {
            resultMap.put("message", "수량이 잘못되었습니다.");
            return resultMap;
        }
        // 2. 판매상품 ID를 통해 현재 판매중인 상품인지 조회
        else if (sellProduct.getIsSell().equals('N')) {
            resultMap.put("message", "판매중인 상품이 아닙니다.");
            return resultMap;
        }
        // 3. 정상 데이터로 검증 통과
        else {

            // 회원정보를 조회하여 카트가 생성되어있으면 카트번호를 조회해온다. 없는 경우 생성한다.
            CartDTO cart = cartService.checkoutCartByMemberId(empNo);
            log.info("cart");

            // 장바구니 미생성 회원
            if (cart == null) {
                // 장바구니 테이블을 생성한다.
                cartService.makeCart(empNo);
                // 다시 장바구니 정보 조회
                cart = cartService.checkoutCartByMemberId(empNo);
                // 장바구니(카트) 넘버를 세팅한다.
                cartSellProduct.setCartNo(cart.getCartNo());
            }

            cartSellProduct.setCartNo(cart.getCartNo());
            System.out.println("cartSell 정보 : " + cartSellProduct);

            // 장바구니에 같은 상품을 담은 경우
            int checkPrd = cartService.checkoutCartProductById(cartSellProduct);
            if (checkPrd > 0) {
                resultMap.put("message", "이미 장바구니에 존재하는 상품입니다.");
                return resultMap;
            }

            // cartSellProduct 테이블 데이터 삽입하러 가기
            int result = cartService.addCartSellProduct(cartSellProduct);
            if (result > 0) {
                //장바구니 담기 성공시
                System.out.println("장바구니 담기 성공1111");
                resultMap.put("message", "장바구니 담기에 성공하였습니다.");

                return resultMap;

            } else {   // 실패
                log.info("log 확인");
                System.out.println("장바구니 담기 실패2222");
                resultMap.put("message", "장바구니 담기에 실패하였습니다. 다시 시도해주세요.");
                return resultMap;
            }
        }
    }

    // 회원 장바구니 불러오기 메소드
    @GetMapping("cart")
    public String cartList(Model model
            , @AuthenticationPrincipal User user
            , @RequestParam(value = "empNo", required = false) String empNo
    ) {

        // 1. 회원 정보 받아서 해당 회원의 장바구니 조회
//        System.out.println(user);
//        System.out.println(user.getUsername());
        System.out.println(empNo);

        // 2. 장바구니 관련 테이블 리스트로 받아옴
        List<CartGeneralDTO> cartList = cartService.cartAllInfoSelect(empNo);

        // 3. 장바구니 상품정보 모델에 담아 뷰로 전달
        model.addAttribute("cartList", cartList);

        for (CartGeneralDTO cart : cartList) {

            priceMaker(cart);

        }

        return "consumer/order/cart";

    }

 //// json객체로 넘기겠다(포워드, 리다이렉트아님, 리로드안됨)
    // 수량변경시 가격변동 등 비동기처리 메소드
    @PostMapping("cart/amount-change")
    @ResponseBody
    public boolean cartAmountChange(@ModelAttribute CartSellProductDTO cartSellProduct, Model model
            , @RequestParam(value = "empNo", required = false) String empNo) {

        System.out.println(empNo);

        cartSellProduct.setCart(new CartDTO());
        cartSellProduct.getCart().setEmpNo(empNo);
        System.out.println();
        boolean result = cartService.cartAmountChange(cartSellProduct);
        System.out.println(result);




        return result;
    }

    // 단품 금액 생성 메소드
    public void priceMaker(CartGeneralDTO cart) {

        // 1품목 가격 (가격 * 수량)
        cart.setPrice((int)(cart.getProduct().getProductPrice() * ((1.0 - cart.getSellProduct().getDiscount()))) * cart.getCartSellProduct().getCartAmount());
        // 1품목 총합계 (1품목 가격 + 배송비)
        cart.setTotalPrice(cart.getPrice() + cart.getCartSellProduct().getDeliveryPrice());

    }

    // 예상 결제금액 생성메소드(체크박스 선택시 바뀐다)
    public void exptPriceMaker(CartGeneralDTO cart) {
        // 선택 총 합계
//        private int exptPrice;
//        private int exptDeliveryPrice;
//        private int exptTotalPrice;
//        int exptPrice = cart.getTotalPrice();
//        int exptDeliveryPrice = cart.getCartSellProduct().getDeliveryPrice();
    }
}




