package com.hacker.boot.elasticsearch.repository;

import com.hacker.boot.elasticsearch.bean.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 定义一个子接口，可以不做任何实现，泛型的value 是 主键的类型
 */
public interface BookRepository extends ElasticsearchRepository<Book, Integer> {

    /**
     * 自定义方法，同样不用任何实现
     */
    List<Book> findByBookNameLike(String bookName);
}
