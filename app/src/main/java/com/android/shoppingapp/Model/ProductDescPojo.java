package com.android.shoppingapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductDescPojo {


    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("data")
    private List<Data> data;
    @Expose
    @SerializedName("status")
    private boolean status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public static class Data {
        @Expose
        @SerializedName("image")
        private List<Image> image;
        @Expose
        @SerializedName("rating_count")
        private int ratingCount;
        @Expose
        @SerializedName("is_wishlist")
        private int isWishlist;
        @Expose
        @SerializedName("wishlist")
        private int wishlist;
        @Expose
        @SerializedName("cart_qty")
        private int cartQty;
        @Expose
        @SerializedName("is_cart")
        private int isCart;
        @Expose
        @SerializedName("available")
        private String available;
        @Expose
        @SerializedName("category_name")
        private String categoryName;
        @Expose
        @SerializedName("collection_name")
        private String collectionName;
        @Expose
        @SerializedName("updated_at")
        private String updatedAt;
        @Expose
        @SerializedName("created_at")
        private String createdAt;
        @Expose
        @SerializedName("status")
        private int status;
        @Expose
        @SerializedName("sold_quantity")
        private String soldQuantity;
        @Expose
        @SerializedName("available_quantity")
        private String availableQuantity;
        @Expose
        @SerializedName("collection")
        private String collection;
        @Expose
        @SerializedName("shipping_charge")
        private int shippingCharge;
        @Expose
        @SerializedName("weight")
        private String weight;
        @Expose
        @SerializedName("price")
        private String price;
        @Expose
        @SerializedName("color")
        private String color;
        @Expose
        @SerializedName("description")
        private String description;
        @Expose
        @SerializedName("category")
        private int category;
        @Expose
        @SerializedName("size")
        private String size;
        @Expose
        @SerializedName("product_code")
        private String productCode;
        @Expose
        @SerializedName("product_name")
        private String productName;
        @Expose
        @SerializedName("id")
        private int id;

        public List<Image> getImage() {
            return image;
        }

        public void setImage(List<Image> image) {
            this.image = image;
        }

        public int getRatingCount() {
            return ratingCount;
        }

        public void setRatingCount(int ratingCount) {
            this.ratingCount = ratingCount;
        }

        public int getIsWishlist() {
            return isWishlist;
        }

        public void setIsWishlist(int isWishlist) {
            this.isWishlist = isWishlist;
        }

        public int getWishlist() {
            return wishlist;
        }

        public void setWishlist(int wishlist) {
            this.wishlist = wishlist;
        }

        public int getCartQty() {
            return cartQty;
        }

        public void setCartQty(int cartQty) {
            this.cartQty = cartQty;
        }

        public int getIsCart() {
            return isCart;
        }

        public void setIsCart(int isCart) {
            this.isCart = isCart;
        }

        public String getAvailable() {
            return available;
        }

        public void setAvailable(String available) {
            this.available = available;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getCollectionName() {
            return collectionName;
        }

        public void setCollectionName(String collectionName) {
            this.collectionName = collectionName;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getSoldQuantity() {
            return soldQuantity;
        }

        public void setSoldQuantity(String soldQuantity) {
            this.soldQuantity = soldQuantity;
        }

        public String getAvailableQuantity() {
            return availableQuantity;
        }

        public void setAvailableQuantity(String availableQuantity) {
            this.availableQuantity = availableQuantity;
        }

        public String getCollection() {
            return collection;
        }

        public void setCollection(String collection) {
            this.collection = collection;
        }

        public int getShippingCharge() {
            return shippingCharge;
        }

        public void setShippingCharge(int shippingCharge) {
            this.shippingCharge = shippingCharge;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class Image {
        @Expose
        @SerializedName("updated_at")
        private String updatedAt;
        @Expose
        @SerializedName("created_at")
        private String createdAt;
        @Expose
        @SerializedName("image")
        private String image;
        @Expose
        @SerializedName("product_id")
        private int productId;
        @Expose
        @SerializedName("id")
        private int id;

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
