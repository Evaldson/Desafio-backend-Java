package com.challenge.backend.model.output;

import lombok.Data;

@Data
public class ProductOutput {
    private int id;
    private String title;
    private double price;
    private String description;
    private String category;
    private String image;
    private Rating rating;
    
    @Data
    public static class Rating {
        private double rate;
        private int count;

    }
    
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", image='" + image + '\'' +
                ", rating=" + rating +
                '}';
    }
}