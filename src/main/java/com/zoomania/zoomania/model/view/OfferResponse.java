package com.zoomania.zoomania.model.view;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OfferResponse {

    private List<OfferDetailsView> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public OfferResponse setContent(List<OfferDetailsView> content) {
        this.content = content;
        return this;
    }

    public OfferResponse setPageNo(int pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public OfferResponse setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public OfferResponse setTotalElements(long totalElements) {
        this.totalElements = totalElements;
        return this;
    }

    public OfferResponse setTotalPages(int totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public OfferResponse setLast(boolean last) {
        this.last = last;
        return this;
    }
}
