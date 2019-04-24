package com.example.demo.model.custom;

/**
 * Created by ilcyw on 2018/12/26.
 */
public class CartCustom {
    private String cartId;

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    private String productName;
    private Double productPrice;
    private Double productCount;
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getProductName() {return productName;}

    public void setProductName(String productName) {this.productName = productName;}

    public Double getProductPrice() {return productPrice;}

    public void setProductPrice(Double productPrice) {this.productPrice = productPrice;}

    public Double getProductCount() {return productCount;}

    public void setProductCount(Double productCount) {this.productCount = productCount;}


    @Override
    public String toString() {
        return "CartCustom{" +
                "cartId='" + cartId + '\'' +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productCount=" + productCount +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
