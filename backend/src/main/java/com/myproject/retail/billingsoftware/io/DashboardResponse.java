package com.myproject.retail.billingsoftware.io;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private Double todaySales;
    private Long todayOrderCount;
    private Long todayItemsSold;      
    private Double avgBillValue;      
    private List<OrderResponse> recentOrders;
}