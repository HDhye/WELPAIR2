package com.hielectro.welpair.sales.controller;

import com.google.gson.Gson;
import com.hielectro.welpair.inventory.model.dto.CategoryDTO;
import com.hielectro.welpair.inventory.model.dto.ProductDTO;
import com.hielectro.welpair.order.model.dto.ProductOrderDTO;
import com.hielectro.welpair.payment.dto.OrderPaymentDTO;
import com.hielectro.welpair.payment.dto.PaymentDTO;
import com.hielectro.welpair.sales.model.dto.SalesDTO;
import com.hielectro.welpair.sales.model.service.SalesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/sales/")
public class SalesController {

    private final SalesService salesService;
    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }


    /**
     * 매출관리 (ng)
     * 월별 총매출액 정보 전달
     * 막대그래프로 단순 표현 예정
     */
    @GetMapping("admin_sales")
    public String getMonthSales(Model model, @ModelAttribute SalesDTO sales) {
        System.out.println("------------- 매출 컨트롤러 1-1-1 in -------------");

        sales.setPayment(new PaymentDTO());
        sales.setCategory(new CategoryDTO());

        PaymentDTO payment = sales.getPayment();
        CategoryDTO category = sales.getCategory();

        String paymentType = (payment != null && payment.getPaymentType() != null) ? payment.getPaymentType() : null;
        String categoryCode = (category != null && category.getCategoryCode() != null) ? category.getCategoryCode() : null;

        System.out.println("categoryCode = " + categoryCode);
        System.out.println("paymentType = " + paymentType);

        List<SalesDTO> monthlyList = salesService.getMonthSales(sales);
        model.addAttribute("monthlyList", monthlyList);
        System.out.println("monthlyList = " + monthlyList);
        System.out.println("------------- 매출 컨트롤러 1-1-2 out -------------");


        return "admin/sales/admin_sales";

    }



}