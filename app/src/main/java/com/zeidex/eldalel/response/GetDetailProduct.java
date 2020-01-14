package com.zeidex.eldalel.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDetailProduct {
    @SerializedName("code")
    String code;

    @SerializedName("message")
    String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    @SerializedName("data")
    Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        @SerializedName("product")
        Product product;

        @SerializedName("related")
        List<Related> related;

        public List<Related> getRelated() {
            return related;
        }

        public void setRelated(List<Related> related) {
            this.related = related;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }
    }


    public class Related{
        @SerializedName("id")
        String id;

        @SerializedName("subcategory_id")
        String subcategory_id;

        @SerializedName("name")
        String name;

        @SerializedName("name_ar")
        String name_ar;

        @SerializedName("price")
        String price;

        @SerializedName("old_price")
        String old_price;

        @SerializedName("wholesaler_price")
        String wholesale_price;

        @SerializedName("wholesaler_old_price")
        String wholesale_old_price;

        @SerializedName("shortDesc")
        String shortDesc;

        @SerializedName("shortDescAr")
        String shortDescAr;

        @SerializedName("status")
        String status;

        @SerializedName("offer_number")
        String offer_number;

        @SerializedName("group")
        String group;

        @SerializedName("sku")
        String sku;

        @SerializedName("storage_group")
        String storage_group;

        @SerializedName("favorite")
        String favorite;

        @SerializedName("discount")
        String discount;

        @SerializedName("discount_company")
        String discount_company;

        @SerializedName("available_quantity")
        String available_quantity;

        @SerializedName("cart_status")
        String cart;

        @SerializedName("photos")
        List<Photo> photos;

        public String getFavorite() {
            return favorite;
        }

        public void setFavorite(String favorite) {
            this.favorite = favorite;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSubcategory_id() {
            return subcategory_id;
        }

        public void setSubcategory_id(String subcategory_id) {
            this.subcategory_id = subcategory_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName_ar() {
            return name_ar;
        }

        public void setName_ar(String name_ar) {
            this.name_ar = name_ar;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getOld_price() {
            return old_price;
        }

        public void setOld_price(String old_price) {
            this.old_price = old_price;
        }

        public String getWholesale_price() {
            return wholesale_price;
        }

        public void setWholesale_price(String wholesale_price) {
            this.wholesale_price = wholesale_price;
        }

        public String getWholesale_old_price() {
            return wholesale_old_price;
        }

        public void setWholesale_old_price(String wholesale_old_price) {
            this.wholesale_old_price = wholesale_old_price;
        }

        public String getDiscount_company() {
            return discount_company;
        }

        public void setDiscount_company(String discount_company) {
            this.discount_company = discount_company;
        }

        public String getShortDesc() {
            return shortDesc;
        }

        public void setShortDesc(String shortDesc) {
            this.shortDesc = shortDesc;
        }

        public String getShortDescAr() {
            return shortDescAr;
        }

        public void setShortDescAr(String shortDescAr) {
            this.shortDescAr = shortDescAr;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getOffer_number() {
            return offer_number;
        }

        public void setOffer_number(String offer_number) {
            this.offer_number = offer_number;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getStorage_group() {
            return storage_group;
        }

        public void setStorage_group(String storage_group) {
            this.storage_group = storage_group;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getAvailable_quantity() {
            return available_quantity;
        }

        public void setAvailable_quantity(String available_quantity) {
            this.available_quantity = available_quantity;
        }

        public String getCart() {
            return cart;
        }

        public void setCart(String cart) {
            this.cart = cart;
        }

        public List<Photo> getPhotos() {
            return photos;
        }

        public void setPhotos(List<Photo> photos) {
            this.photos = photos;
        }
    }





    public class Category{
        @SerializedName("id")
        String id;

        @SerializedName("name")
        String name;

        @SerializedName("name_ar")
        String name_ar;

        @SerializedName("products")
        List<Product> products;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName_ar() {
            return name_ar;
        }

        public void setName_ar(String name_ar) {
            this.name_ar = name_ar;
        }

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }
    }

    public class Product{
        @SerializedName("id")
        String id;

        @SerializedName("subcategory_id")
        String subcategory_id;

        @SerializedName("name")
        String name;

        @SerializedName("name_ar")
        String name_ar;

        @SerializedName("price")
        Double price;

        @SerializedName("old_price")
        Double old_price;

        @SerializedName("wholesaler_price")
        Double wholesale_price;

        @SerializedName("wholesaler_old_price")
        Double wholesale_old_price;

        @SerializedName("long_description_en")
        String shortDesc;

        @SerializedName("long_description_ar")
        String shortDescAr;

        @SerializedName("description")
        String description;

        @SerializedName("status")
        String status;

        @SerializedName("offer_number")
        String offer_number;

        @SerializedName("group")
        String group;

        @SerializedName("sku")
        String sku;

        @SerializedName("storage_group")
        String storage_group;

        @SerializedName("discount_user")
        String discount;

        @SerializedName("discount_company")
        String discount_company;

        @SerializedName("photos")
        List<Photo> photos;

        @SerializedName("url")
        String url;

        @SerializedName("subcategory")
        SubCategory subcategory;

        @SerializedName("optiongroups")
        List<OptionGroups> optiongroups;

        @SerializedName("colors")
        List<Color> colors;

        @SerializedName("cart_status")
        String cart;

        @SerializedName("favorite")
        String favorite;

        @SerializedName("available_quantity")
        Integer availableQuantity;

        @SerializedName("capacities")
        List<Capacity> capacities;

        public String getFavorite() {
            return favorite;
        }

        public void setFavorite(String favorite) {
            this.favorite = favorite;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<Color> getColors() {
            return colors;
        }

        public void setColors(List<Color> colors) {
            this.colors = colors;
        }

        public List<Capacity> getCapacities() {
            return capacities;
        }

        public void setCapacities(List<Capacity> capacities) {
            this.capacities = capacities;
        }

        public List<OptionGroups> getOptiongroups() {
            return optiongroups;
        }

        public void setOptiongroups(List<OptionGroups> optiongroups) {
            this.optiongroups = optiongroups;
        }

        public SubCategory getSubcategory() {
            return subcategory;
        }

        public void setSubcategory(SubCategory subcategory) {
            this.subcategory = subcategory;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSubcategory_id() {
            return subcategory_id;
        }

        public void setSubcategory_id(String subcategory_id) {
            this.subcategory_id = subcategory_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName_ar() {
            return name_ar;
        }

        public void setName_ar(String name_ar) {
            this.name_ar = name_ar;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Double getOld_price() {
            return old_price;
        }

        public void setOld_price(Double old_price) {
            this.old_price = old_price;
        }

        public String getShortDesc() {
            return shortDesc;
        }

        public void setShortDesc(String shortDesc) {
            this.shortDesc = shortDesc;
        }

        public String getShortDescAr() {
            return shortDescAr;
        }

        public void setShortDescAr(String shortDescAr) {
            this.shortDescAr = shortDescAr;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getOffer_number() {
            return offer_number;
        }

        public void setOffer_number(String offer_number) {
            this.offer_number = offer_number;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getStorage_group() {
            return storage_group;
        }

        public void setStorage_group(String storage_group) {
            this.storage_group = storage_group;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String  getCart() {
            return cart;
        }

        public void setCart(String cart) {
            this.cart = cart;
        }

        public Integer getAvailableQuantity() {
            return availableQuantity;
        }

        public void setAvailableQuantity(Integer availableQuantity) {
            this.availableQuantity = availableQuantity;
        }

        public Double getWholesale_price() {
            return wholesale_price;
        }

        public void setWholesale_price(Double wholesale_price) {
            this.wholesale_price = wholesale_price;
        }

        public Double getWholesale_old_price() {
            return wholesale_old_price;
        }

        public void setWholesale_old_price(Double wholesale_old_price) {
            this.wholesale_old_price = wholesale_old_price;
        }

        public String getDiscount_company() {
            return discount_company;
        }

        public void setDiscount_company(String discount_company) {
            this.discount_company = discount_company;
        }

        public List<Photo> getPhotos() {
            return photos;
        }

        public void setPhotos(List<Photo> photos) {
            this.photos = photos;
        }
    }


    public class Capacity{
        @SerializedName("name")
        String name;

        @SerializedName("product_id")
        String product_id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }
    }

    public class Photo{
        @SerializedName("filename")
        String filename;
        @SerializedName("photo_path")
        String photoPath;

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getPhotoPath() {
            return photoPath;
        }

        public void setPhotoPath(String photoPath) {
            this.photoPath = photoPath;
        }
    }

    public class Color{
        @SerializedName("name")
        String name;

        @SerializedName("name_ar")
        String name_ar;

        @SerializedName("product_id")
        String product_id;

        @SerializedName("photo")
        String photo;

        public String getName_ar() {
            return name_ar;
        }

        public void setName_ar(String name_ar) {
            this.name_ar = name_ar;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }
    }

    public class SubCategory{
        @SerializedName("name")
        String name;

        @SerializedName("name_ar")
        String name_ar;

        @SerializedName("id")
        String id;

        @SerializedName("category_id")
        String category_id;

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName_ar() {
            return name_ar;
        }

        public void setName_ar(String name_ar) {
            this.name_ar = name_ar;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public class OptionGroups{
        @SerializedName("id")
        String id;

        @SerializedName("name")
        String name;

        @SerializedName("features")
        List<String> features;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<String> getFeatures() {
            return features;
        }

        public void setFeatures(List<String> features) {
            this.features = features;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
