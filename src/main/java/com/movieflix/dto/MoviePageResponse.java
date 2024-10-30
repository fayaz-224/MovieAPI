package com.movieflix.dto;

import java.util.List;

//Record classes are short forms of entity classes, which avoid boilerplate code
public record MoviePageResponse(List<MovieDto> movieDtos,
                                Integer pageNumber,  //Page No. we want to see from, default page = 0
                                Integer pageSize,   //No. of data in a single page, default size = 3
                                long totalElements,  //Total no. of elements in entire pages
                                int totalPages,  //Total pages after pagination
                                boolean isLast) {
}
